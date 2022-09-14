// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import org.zik.bpm.engine.impl.util.ClassLoaderUtil;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class SequentialJobAcquisitionRunnable extends AcquireJobsRunnable
{
    protected final JobExecutorLogger LOG;
    protected JobAcquisitionContext acquisitionContext;
    
    public SequentialJobAcquisitionRunnable(final JobExecutor jobExecutor) {
        super(jobExecutor);
        this.LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
        this.acquisitionContext = this.initializeAcquisitionContext();
    }
    
    @Override
    public synchronized void run() {
        this.LOG.startingToAcquireJobs(this.jobExecutor.getName());
        final JobAcquisitionStrategy acquisitionStrategy = this.initializeAcquisitionStrategy();
        while (!this.isInterrupted) {
            this.acquisitionContext.reset();
            this.acquisitionContext.setAcquisitionTime(System.currentTimeMillis());
            final Iterator<ProcessEngineImpl> engineIterator = this.jobExecutor.engineIterator();
            final ClassLoader classLoaderBeforeExecution = ClassLoaderUtil.switchToProcessEngineClassloader();
            try {
                while (engineIterator.hasNext()) {
                    final ProcessEngineImpl currentProcessEngine = engineIterator.next();
                    if (!this.jobExecutor.hasRegisteredEngine(currentProcessEngine)) {
                        continue;
                    }
                    final AcquiredJobs acquiredJobs = this.acquireJobs(this.acquisitionContext, acquisitionStrategy, currentProcessEngine);
                    this.executeJobs(this.acquisitionContext, currentProcessEngine, acquiredJobs);
                }
            }
            catch (Exception e) {
                this.LOG.exceptionDuringJobAcquisition(e);
                this.acquisitionContext.setAcquisitionException(e);
            }
            finally {
                ClassLoaderUtil.setContextClassloader(classLoaderBeforeExecution);
            }
            this.acquisitionContext.setJobAdded(this.isJobAdded);
            this.configureNextAcquisitionCycle(this.acquisitionContext, acquisitionStrategy);
            this.clearJobAddedNotification();
            long waitTime = acquisitionStrategy.getWaitTime();
            waitTime = Math.max(0L, this.acquisitionContext.getAcquisitionTime() + waitTime - System.currentTimeMillis());
            this.suspendAcquisition(waitTime);
        }
        this.LOG.stoppedJobAcquisition(this.jobExecutor.getName());
    }
    
    protected JobAcquisitionContext initializeAcquisitionContext() {
        return new JobAcquisitionContext();
    }
    
    protected void configureNextAcquisitionCycle(final JobAcquisitionContext acquisitionContext, final JobAcquisitionStrategy acquisitionStrategy) {
        acquisitionStrategy.reconfigure(acquisitionContext);
    }
    
    protected JobAcquisitionStrategy initializeAcquisitionStrategy() {
        return new BackoffJobAcquisitionStrategy(this.jobExecutor);
    }
    
    public JobAcquisitionContext getAcquisitionContext() {
        return this.acquisitionContext;
    }
    
    protected void executeJobs(final JobAcquisitionContext context, final ProcessEngineImpl currentProcessEngine, final AcquiredJobs acquiredJobs) {
        final List<List<String>> additionalJobs = context.getAdditionalJobsByEngine().get(currentProcessEngine.getName());
        if (additionalJobs != null) {
            for (final List<String> jobBatch : additionalJobs) {
                this.LOG.executeJobs(currentProcessEngine.getName(), jobBatch);
                this.jobExecutor.executeJobs(jobBatch, currentProcessEngine);
            }
        }
        for (final List<String> jobIds : acquiredJobs.getJobIdBatches()) {
            this.LOG.executeJobs(currentProcessEngine.getName(), jobIds);
            this.jobExecutor.executeJobs(jobIds, currentProcessEngine);
        }
    }
    
    protected AcquiredJobs acquireJobs(final JobAcquisitionContext context, final JobAcquisitionStrategy acquisitionStrategy, final ProcessEngineImpl currentProcessEngine) {
        final CommandExecutor commandExecutor = currentProcessEngine.getProcessEngineConfiguration().getCommandExecutorTxRequired();
        final int numJobsToAcquire = acquisitionStrategy.getNumJobsToAcquire(currentProcessEngine.getName());
        AcquiredJobs acquiredJobs = null;
        if (numJobsToAcquire > 0) {
            this.jobExecutor.logAcquisitionAttempt(currentProcessEngine);
            acquiredJobs = commandExecutor.execute(this.jobExecutor.getAcquireJobsCmd(numJobsToAcquire));
        }
        else {
            acquiredJobs = new AcquiredJobs(numJobsToAcquire);
        }
        context.submitAcquiredJobs(currentProcessEngine.getName(), acquiredJobs);
        this.jobExecutor.logAcquiredJobs(currentProcessEngine, acquiredJobs.size());
        this.jobExecutor.logAcquisitionFailureJobs(currentProcessEngine, acquiredJobs.getNumberOfJobsFailedToLock());
        this.LOG.acquiredJobs(currentProcessEngine.getName(), acquiredJobs);
        return acquiredJobs;
    }
}
