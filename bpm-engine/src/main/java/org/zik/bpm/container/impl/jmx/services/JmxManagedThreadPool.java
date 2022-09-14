// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.container.impl.jmx.services;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.concurrent.TimeUnit;
import org.zik.bpm.container.impl.spi.PlatformServiceContainer;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.BlockingQueue;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.container.impl.spi.PlatformService;
import org.zik.bpm.container.impl.threading.se.SeExecutorService;

public class JmxManagedThreadPool extends SeExecutorService implements JmxManagedThreadPoolMBean, PlatformService<JmxManagedThreadPool>
{
    private static final ContainerIntegrationLogger LOG;
    protected final BlockingQueue<Runnable> threadPoolQueue;
    
    public JmxManagedThreadPool(final BlockingQueue<Runnable> queue, final ThreadPoolExecutor executor) {
        super(executor);
        this.threadPoolQueue = queue;
    }
    
    @Override
    public void start(final PlatformServiceContainer mBeanServiceContainer) {
    }
    
    @Override
    public void stop(final PlatformServiceContainer mBeanServiceContainer) {
        this.threadPoolQueue.clear();
        this.threadPoolExecutor.shutdown();
        try {
            if (!this.threadPoolExecutor.awaitTermination(60L, TimeUnit.SECONDS)) {
                JmxManagedThreadPool.LOG.timeoutDuringShutdownOfThreadPool(60, TimeUnit.SECONDS);
            }
        }
        catch (InterruptedException e) {
            JmxManagedThreadPool.LOG.interruptedWhileShuttingDownThreadPool(e);
        }
    }
    
    @Override
    public JmxManagedThreadPool getValue() {
        return this;
    }
    
    @Override
    public void setCorePoolSize(final int corePoolSize) {
        this.threadPoolExecutor.setCorePoolSize(corePoolSize);
    }
    
    @Override
    public void setMaximumPoolSize(final int maximumPoolSize) {
        this.threadPoolExecutor.setMaximumPoolSize(maximumPoolSize);
    }
    
    @Override
    public int getMaximumPoolSize() {
        return this.threadPoolExecutor.getMaximumPoolSize();
    }
    
    public void setKeepAliveTime(final long time, final TimeUnit unit) {
        this.threadPoolExecutor.setKeepAliveTime(time, unit);
    }
    
    @Override
    public void purgeThreadPool() {
        this.threadPoolExecutor.purge();
    }
    
    @Override
    public int getPoolSize() {
        return this.threadPoolExecutor.getPoolSize();
    }
    
    @Override
    public int getActiveCount() {
        return this.threadPoolExecutor.getActiveCount();
    }
    
    @Override
    public int getLargestPoolSize() {
        return this.threadPoolExecutor.getLargestPoolSize();
    }
    
    @Override
    public long getTaskCount() {
        return this.threadPoolExecutor.getTaskCount();
    }
    
    @Override
    public long getCompletedTaskCount() {
        return this.threadPoolExecutor.getCompletedTaskCount();
    }
    
    @Override
    public int getQueueCount() {
        return this.threadPoolQueue.size();
    }
    
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.threadPoolExecutor;
    }
    
    static {
        LOG = ProcessEngineLogger.CONTAINER_INTEGRATION_LOGGER;
    }
}
