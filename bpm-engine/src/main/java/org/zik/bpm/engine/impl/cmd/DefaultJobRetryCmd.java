// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Arrays;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.el.Expression;
import org.zik.bpm.engine.impl.util.ParseUtil;
import org.zik.bpm.engine.impl.bpmn.parser.DefaultFailedJobParseListener;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.calendar.DurationHelper;
import org.zik.bpm.engine.impl.bpmn.parser.FailedJobRetryConfiguration;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutorLogger;
import java.util.List;

public class DefaultJobRetryCmd extends JobRetryCmd
{
    public static final List<String> SUPPORTED_TYPES;
    private static final JobExecutorLogger LOG;
    
    public DefaultJobRetryCmd(final String jobId, final Throwable exception) {
        super(jobId, exception);
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        final JobEntity job = this.getJob();
        final ActivityImpl activity = this.getCurrentActivity(commandContext, job);
        if (activity == null) {
            DefaultJobRetryCmd.LOG.debugFallbackToDefaultRetryStrategy();
            this.executeStandardStrategy(commandContext);
        }
        else {
            try {
                this.executeCustomStrategy(commandContext, job, activity);
            }
            catch (Exception e) {
                DefaultJobRetryCmd.LOG.debugFallbackToDefaultRetryStrategy();
                this.executeStandardStrategy(commandContext);
            }
        }
        return null;
    }
    
    protected void executeStandardStrategy(final CommandContext commandContext) {
        final JobEntity job = this.getJob();
        if (job != null) {
            job.unlock();
            this.logException(job);
            this.decrementRetries(job);
            this.notifyAcquisition(commandContext);
        }
        else {
            DefaultJobRetryCmd.LOG.debugFailedJobNotFound(this.jobId);
        }
    }
    
    protected void executeCustomStrategy(final CommandContext commandContext, final JobEntity job, final ActivityImpl activity) throws Exception {
        final FailedJobRetryConfiguration retryConfiguration = this.getFailedJobRetryConfiguration(job, activity);
        if (retryConfiguration == null) {
            this.executeStandardStrategy(commandContext);
        }
        else {
            final boolean isFirstExecution = this.isFirstJobExecution(job);
            this.logException(job);
            if (isFirstExecution) {
                this.initializeRetries(job, retryConfiguration.getRetries());
            }
            else {
                DefaultJobRetryCmd.LOG.debugDecrementingRetriesForJob(job.getId());
            }
            final List<String> intervals = retryConfiguration.getRetryIntervals();
            final int intervalsCount = intervals.size();
            final int indexOfInterval = Math.max(0, Math.min(intervalsCount - 1, intervalsCount - (job.getRetries() - 1)));
            final DurationHelper durationHelper = this.getDurationHelper(intervals.get(indexOfInterval));
            job.setDuedate(durationHelper.getDateAfter());
            job.unlock();
            this.decrementRetries(job);
            this.notifyAcquisition(commandContext);
        }
    }
    
    protected ActivityImpl getCurrentActivity(final CommandContext commandContext, final JobEntity job) {
        final String type = job.getJobHandlerType();
        ActivityImpl activity = null;
        if (DefaultJobRetryCmd.SUPPORTED_TYPES.contains(type)) {
            final DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
            final ProcessDefinitionEntity processDefinitionEntity = deploymentCache.findDeployedProcessDefinitionById(job.getProcessDefinitionId());
            activity = processDefinitionEntity.findActivity(job.getActivityId());
        }
        return activity;
    }
    
    protected ExecutionEntity fetchExecutionEntity(final String executionId) {
        return Context.getCommandContext().getExecutionManager().findExecutionById(executionId);
    }
    
    protected FailedJobRetryConfiguration getFailedJobRetryConfiguration(final JobEntity job, final ActivityImpl activity) {
        FailedJobRetryConfiguration retryConfiguration;
        String retryIntervals;
        for (retryConfiguration = activity.getProperties().get(DefaultFailedJobParseListener.FAILED_JOB_CONFIGURATION); retryConfiguration != null && retryConfiguration.getExpression() != null; retryConfiguration = ParseUtil.parseRetryIntervals(retryIntervals)) {
            retryIntervals = this.getFailedJobRetryTimeCycle(job, retryConfiguration.getExpression());
        }
        return retryConfiguration;
    }
    
    protected String getFailedJobRetryTimeCycle(final JobEntity job, final Expression expression) {
        final String executionId = job.getExecutionId();
        ExecutionEntity execution = null;
        if (executionId != null) {
            execution = this.fetchExecutionEntity(executionId);
        }
        Object value = null;
        if (expression == null) {
            return null;
        }
        try {
            value = expression.getValue(execution, execution);
        }
        catch (Exception e) {
            DefaultJobRetryCmd.LOG.exceptionWhileParsingExpression(this.jobId, e.getCause().getMessage());
        }
        if (value instanceof String) {
            return (String)value;
        }
        return null;
    }
    
    protected DurationHelper getDurationHelper(final String failedJobRetryTimeCycle) throws Exception {
        return new DurationHelper(failedJobRetryTimeCycle);
    }
    
    protected boolean isFirstJobExecution(final JobEntity job) {
        return job.getExceptionByteArrayId() == null && job.getExceptionMessage() == null;
    }
    
    protected void initializeRetries(final JobEntity job, final int retries) {
        DefaultJobRetryCmd.LOG.debugInitiallyAppyingRetryCycleForJob(job.getId(), retries);
        job.setRetries(retries);
    }
    
    static {
        SUPPORTED_TYPES = Arrays.asList("timer-transition", "timer-intermediate-transition", "timer-start-event", "timer-start-event-subprocess", "async-continuation");
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
