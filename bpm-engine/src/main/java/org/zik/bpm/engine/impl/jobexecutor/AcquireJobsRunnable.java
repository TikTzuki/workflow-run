// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AcquireJobsRunnable implements Runnable
{
    private static final JobExecutorLogger LOG;
    protected final JobExecutor jobExecutor;
    protected volatile boolean isInterrupted;
    protected volatile boolean isJobAdded;
    protected final Object MONITOR;
    protected final AtomicBoolean isWaiting;
    
    public AcquireJobsRunnable(final JobExecutor jobExecutor) {
        this.isInterrupted = false;
        this.isJobAdded = false;
        this.MONITOR = new Object();
        this.isWaiting = new AtomicBoolean(false);
        this.jobExecutor = jobExecutor;
    }
    
    protected void suspendAcquisition(final long millis) {
        if (millis <= 0L) {
            return;
        }
        try {
            AcquireJobsRunnable.LOG.debugJobAcquisitionThreadSleeping(millis);
            synchronized (this.MONITOR) {
                if (!this.isInterrupted) {
                    this.isWaiting.set(true);
                    this.MONITOR.wait(millis);
                }
            }
            AcquireJobsRunnable.LOG.jobExecutorThreadWokeUp();
        }
        catch (InterruptedException e) {
            AcquireJobsRunnable.LOG.jobExecutionWaitInterrupted();
        }
        finally {
            this.isWaiting.set(false);
        }
    }
    
    public void stop() {
        synchronized (this.MONITOR) {
            this.isInterrupted = true;
            if (this.isWaiting.compareAndSet(true, false)) {
                this.MONITOR.notifyAll();
            }
        }
    }
    
    public void jobWasAdded() {
        this.isJobAdded = true;
        if (this.isWaiting.compareAndSet(true, false)) {
            synchronized (this.MONITOR) {
                this.MONITOR.notifyAll();
            }
        }
    }
    
    protected void clearJobAddedNotification() {
        this.isJobAdded = false;
    }
    
    public boolean isJobAdded() {
        return this.isJobAdded;
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
