// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ArrayBlockingQueue;

public class DefaultJobExecutor extends ThreadPoolJobExecutor
{
    private static final JobExecutorLogger LOG;
    protected int queueSize;
    protected int corePoolSize;
    protected int maxPoolSize;
    
    public DefaultJobExecutor() {
        this.queueSize = 3;
        this.corePoolSize = 3;
        this.maxPoolSize = 10;
    }
    
    @Override
    protected void startExecutingJobs() {
        if (this.threadPoolExecutor == null || this.threadPoolExecutor.isShutdown()) {
            final BlockingQueue<Runnable> threadPoolQueue = new ArrayBlockingQueue<Runnable>(this.queueSize);
            (this.threadPoolExecutor = new ThreadPoolExecutor(this.corePoolSize, this.maxPoolSize, 0L, TimeUnit.MILLISECONDS, threadPoolQueue)).setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        }
        super.startExecutingJobs();
    }
    
    @Override
    protected void stopExecutingJobs() {
        super.stopExecutingJobs();
        this.threadPoolExecutor.shutdown();
        try {
            if (!this.threadPoolExecutor.awaitTermination(60L, TimeUnit.SECONDS)) {
                DefaultJobExecutor.LOG.timeoutDuringShutdown();
            }
        }
        catch (InterruptedException e) {
            DefaultJobExecutor.LOG.interruptedWhileShuttingDownjobExecutor(e);
        }
    }
    
    public int getQueueSize() {
        return this.queueSize;
    }
    
    public void setQueueSize(final int queueSize) {
        this.queueSize = queueSize;
    }
    
    public int getCorePoolSize() {
        return this.corePoolSize;
    }
    
    public void setCorePoolSize(final int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }
    
    public int getMaxPoolSize() {
        return this.maxPoolSize;
    }
    
    public void setMaxPoolSize(final int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
