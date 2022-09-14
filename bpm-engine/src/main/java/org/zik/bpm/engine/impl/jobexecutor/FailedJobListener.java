// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.Command;

public class FailedJobListener implements Command<Void>
{
    private static final JobExecutorLogger LOG;
    protected CommandExecutor commandExecutor;
    protected JobFailureCollector jobFailureCollector;
    protected int countRetries;
    protected int totalRetries;
    
    public FailedJobListener(final CommandExecutor commandExecutor, final JobFailureCollector jobFailureCollector) {
        this.countRetries = 0;
        this.totalRetries = 3;
        this.commandExecutor = commandExecutor;
        this.jobFailureCollector = jobFailureCollector;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.initTotalRetries(commandContext);
        this.logJobFailure(commandContext);
        final FailedJobCommandFactory failedJobCommandFactory = commandContext.getFailedJobCommandFactory();
        final String jobId = this.jobFailureCollector.getJobId();
        final Command<Object> cmd = failedJobCommandFactory.getCommand(jobId, this.jobFailureCollector.getFailure());
        this.commandExecutor.execute((Command<Object>)new FailedJobListenerCmd(jobId, cmd));
        return null;
    }
    
    private void initTotalRetries(final CommandContext commandContext) {
        this.totalRetries = commandContext.getProcessEngineConfiguration().getFailedJobListenerMaxRetries();
    }
    
    protected void fireHistoricJobFailedEvt(final JobEntity job) {
        final CommandContext commandContext = Context.getCommandContext();
        job.incrementSequenceCounter();
        commandContext.getHistoricJobLogManager().fireJobFailedEvent(job, this.jobFailureCollector.getFailure());
    }
    
    protected void logJobFailure(final CommandContext commandContext) {
        if (commandContext.getProcessEngineConfiguration().isMetricsEnabled()) {
            commandContext.getProcessEngineConfiguration().getMetricsRegistry().markOccurrence("job-failed");
        }
    }
    
    public void incrementCountRetries() {
        ++this.countRetries;
    }
    
    public int getRetriesLeft() {
        return Math.max(0, this.totalRetries - this.countRetries);
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
    
    protected class FailedJobListenerCmd implements Command<Void>
    {
        protected String jobId;
        protected Command<Object> cmd;
        
        public FailedJobListenerCmd(final String jobId, final Command<Object> cmd) {
            this.jobId = jobId;
            this.cmd = cmd;
        }
        
        @Override
        public Void execute(final CommandContext commandContext) {
            final JobEntity job = commandContext.getJobManager().findJobById(this.jobId);
            if (job != null) {
                job.setFailedActivityId(FailedJobListener.this.jobFailureCollector.getFailedActivityId());
                FailedJobListener.this.fireHistoricJobFailedEvt(job);
                this.cmd.execute(commandContext);
            }
            else {
                FailedJobListener.LOG.debugFailedJobNotFound(this.jobId);
            }
            return null;
        }
    }
}
