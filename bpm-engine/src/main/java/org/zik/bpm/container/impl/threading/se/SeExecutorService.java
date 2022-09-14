// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.threading.se;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.jobexecutor.ExecuteJobsRunnable;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.ExecutorService;

public class SeExecutorService implements ExecutorService
{
    private static final ContainerIntegrationLogger LOG;
    protected ThreadPoolExecutor threadPoolExecutor;
    
    public SeExecutorService(final ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
    
    @Override
    public boolean schedule(final Runnable runnable, final boolean isLongRunning) {
        if (isLongRunning) {
            return this.executeLongRunning(runnable);
        }
        return this.executeShortRunning(runnable);
    }
    
    protected boolean executeLongRunning(final Runnable runnable) {
        new Thread(runnable).start();
        return true;
    }
    
    protected boolean executeShortRunning(final Runnable runnable) {
        try {
            this.threadPoolExecutor.execute(runnable);
            return true;
        }
        catch (RejectedExecutionException e) {
            SeExecutorService.LOG.debugRejectedExecutionException(e);
            return false;
        }
    }
    
    @Override
    public Runnable getExecuteJobsRunnable(final List<String> jobIds, final ProcessEngineImpl processEngine) {
        return new ExecuteJobsRunnable(jobIds, processEngine);
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
