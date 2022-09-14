// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cfg.TransactionListener;

public class MessageAddedNotification implements TransactionListener
{
    private final JobExecutorLogger LOG;
    protected JobExecutor jobExecutor;
    
    public MessageAddedNotification(final JobExecutor jobExecutor) {
        this.LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
        this.jobExecutor = jobExecutor;
    }
    
    @Override
    public void execute(final CommandContext commandContext) {
        this.LOG.debugNotifyingJobExecutor("notifying job executor of new job");
        this.jobExecutor.jobWasAdded();
    }
}
