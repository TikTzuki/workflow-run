// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cfg.TransactionListener;

public class ExclusiveJobAddedNotification implements TransactionListener
{
    private static final JobExecutorLogger LOG;
    protected final String jobId;
    protected final JobExecutorContext jobExecutorContext;
    
    public ExclusiveJobAddedNotification(final String jobId, final JobExecutorContext jobExecutorContext) {
        this.jobId = jobId;
        this.jobExecutorContext = jobExecutorContext;
    }
    
    @Override
    public void execute(final CommandContext commandContext) {
        ExclusiveJobAddedNotification.LOG.debugAddingNewExclusiveJobToJobExecutorCOntext(this.jobId);
        this.jobExecutorContext.getCurrentProcessorJobQueue().add(this.jobId);
        this.logExclusiveJobAdded(commandContext);
    }
    
    protected void logExclusiveJobAdded(final CommandContext commandContext) {
        if (commandContext.getProcessEngineConfiguration().isMetricsEnabled()) {
            commandContext.getProcessEngineConfiguration().getMetricsRegistry().markOccurrence("job-locked-exclusive");
        }
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
