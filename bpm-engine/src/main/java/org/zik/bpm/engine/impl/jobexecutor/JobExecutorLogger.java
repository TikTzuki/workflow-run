// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Collection;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.OptimisticLockingException;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class JobExecutorLogger extends ProcessEngineLogger
{
    public void debugAcquiredJobNotFound(final String jobId) {
        this.logDebug("001", "Acquired job with id '{}' not found.", new Object[] { jobId });
    }
    
    public void exceptionWhileExecutingJob(final JobEntity job, final Throwable exception) {
        this.logWarn("002", "Exception while executing job {}: ", new Object[] { job, exception });
    }
    
    public void debugFallbackToDefaultRetryStrategy() {
        this.logDebug("003", "Falling back to default retry strategy", new Object[0]);
    }
    
    public void debugDecrementingRetriesForJob(final String id) {
        this.logDebug("004", "Decrementing retries of job {}", new Object[] { id });
    }
    
    public void debugInitiallyAppyingRetryCycleForJob(final String id, final int times) {
        this.logDebug("005", "Applying job retry time cycle for the first time for job {}, retires {}", new Object[] { id, times });
    }
    
    public void exceptionWhileExecutingJob(final String nextJobId, final Throwable t) {
        if (t instanceof OptimisticLockingException && !this.isDebugEnabled()) {
            this.logWarn("006", "Exception while executing job {}: {}. To see the full stacktrace set logging level to DEBUG.", new Object[] { nextJobId, t.getClass().getSimpleName() });
        }
        else {
            this.logWarn("006", "Exception while executing job {}: ", new Object[] { nextJobId, t });
        }
    }
    
    public void couldNotDeterminePriority(final ExecutionEntity execution, final Object value, final ProcessEngineException e) {
        this.logWarn("007", "Could not determine priority for job created in context of execution {}. Using default priority {}", new Object[] { execution, value, e });
    }
    
    public void debugAddingNewExclusiveJobToJobExecutorCOntext(final String jobId) {
        this.logDebug("008", "Adding new exclusive job to job executor context. Job Id='{}'", new Object[] { jobId });
    }
    
    public void timeoutDuringShutdown() {
        this.logWarn("009", "Timeout during shutdown of job executor. The current running jobs could not end within 60 seconds after shutdown operation", new Object[0]);
    }
    
    public void interruptedWhileShuttingDownjobExecutor(final InterruptedException e) {
        this.logWarn("010", "Interrupted while shutting down the job executor", new Object[] { e });
    }
    
    public void debugJobAcquisitionThreadSleeping(final long millis) {
        this.logDebug("011", "Job acquisition thread sleeping for {} millis", new Object[] { millis });
    }
    
    public void jobExecutorThreadWokeUp() {
        this.logDebug("012", "Job acquisition thread woke up", new Object[0]);
    }
    
    public void jobExecutionWaitInterrupted() {
        this.logDebug("013", "Job Execution wait interrupted", new Object[0]);
    }
    
    public void startingUpJobExecutor(final String name) {
        this.logInfo("014", "Starting up the JobExecutor[{}].", new Object[] { name });
    }
    
    public void shuttingDownTheJobExecutor(final String name) {
        this.logInfo("015", "Shutting down the JobExecutor[{}]", new Object[] { name });
    }
    
    public void ignoringSuspendedJob(final ProcessDefinition processDefinition) {
        this.logDebug("016", "Ignoring job of suspended {}", new Object[] { processDefinition });
    }
    
    public void debugNotifyingJobExecutor(final String string) {
        this.logDebug("017", "Notifying Job Executor of new job {}", new Object[] { string });
    }
    
    public void startingToAcquireJobs(final String name) {
        this.logInfo("018", "{} starting to acquire jobs", new Object[] { name });
    }
    
    public void exceptionDuringJobAcquisition(final Exception e) {
        this.logError("019", "Exception during job acquisition {}", new Object[] { e.getMessage(), e });
    }
    
    public void stoppedJobAcquisition(final String name) {
        this.logInfo("020", "{} stopped job acquisition", new Object[] { name });
    }
    
    public void exceptionWhileUnlockingJob(final String jobId, final Throwable t) {
        this.logWarn("021", "Exception while unaquiring job {}: ", new Object[] { jobId, t });
    }
    
    public void acquiredJobs(final String processEngine, final AcquiredJobs acquiredJobs) {
        this.logDebug("022", "Acquired {} jobs for process engine '{}': {}", new Object[] { acquiredJobs.size(), processEngine, acquiredJobs.getJobIdBatches() });
    }
    
    public void executeJobs(final String processEngine, final Collection<String> jobs) {
        this.logDebug("023", "Execute jobs for process engine '{}': {}", new Object[] { processEngine, jobs });
    }
    
    public void debugFailedJobNotFound(final String jobId) {
        this.logDebug("024", "Failed job with id '{}' not found.", new Object[] { jobId });
    }
    
    public ProcessEngineException wrapJobExecutionFailure(final JobFailureCollector jobFailureCollector, final Throwable cause) {
        final JobEntity job = jobFailureCollector.getJob();
        if (job != null) {
            return new ProcessEngineException(this.exceptionMessage("025", "Exception while executing job {}: ", new Object[] { jobFailureCollector.getJob() }), cause);
        }
        return new ProcessEngineException(this.exceptionMessage("025", "Exception while executing job {}: ", new Object[] { jobFailureCollector.getJobId() }), cause);
    }
    
    public ProcessEngineException jobNotFoundException(final String jobId) {
        return new ProcessEngineException(this.exceptionMessage("026", "No job found with id '{}'", new Object[] { jobId }));
    }
    
    public void exceptionWhileParsingExpression(final String jobId, final String exceptionMessage) {
        this.logWarn("027", "Falling back to default retry strategy. Exception while executing job {}: {}", new Object[] { jobId, exceptionMessage });
    }
    
    public void warnHistoryCleanupBatchWindowNotFound() {
        this.logWarn("028", "Batch window for history cleanup was not calculated. History cleanup job(s) will be suspended.", new Object[0]);
    }
    
    public void infoJobExecutorDoesNotHandleHistoryCleanupJobs(final ProcessEngineConfigurationImpl config) {
        final Long jobExecutorPriorityRangeMin = config.getJobExecutorPriorityRangeMin();
        final Long jobExecutorPriorityRangeMax = config.getJobExecutorPriorityRangeMax();
        this.logInfo("029", "JobExecutor is configured for priority range {}-{}. History cleanup jobs will not be handled, because they are outside the priority range ({}).", new Object[] { (jobExecutorPriorityRangeMin == null) ? 0L : jobExecutorPriorityRangeMin, (jobExecutorPriorityRangeMax == null) ? Long.MAX_VALUE : jobExecutorPriorityRangeMax, config.getHistoryCleanupJobPriority() });
    }
    
    public void infoJobExecutorDoesNotHandleBatchJobs(final ProcessEngineConfigurationImpl config) {
        final Long jobExecutorPriorityRangeMin = config.getJobExecutorPriorityRangeMin();
        final Long jobExecutorPriorityRangeMax = config.getJobExecutorPriorityRangeMax();
        this.logInfo("030", "JobExecutor is configured for priority range {}-{}. Batch jobs will not be handled, because they are outside the priority range ({}).", new Object[] { (jobExecutorPriorityRangeMin == null) ? 0L : jobExecutorPriorityRangeMin, (jobExecutorPriorityRangeMax == null) ? Long.MAX_VALUE : jobExecutorPriorityRangeMax, config.getBatchJobPriority() });
    }
    
    public ProcessEngineException jobExecutorPriorityRangeException(final String reason) {
        return new ProcessEngineException(this.exceptionMessage("031", "Invalid configuration for job executor priority range. Reason: {}", new Object[] { reason }));
    }
}
