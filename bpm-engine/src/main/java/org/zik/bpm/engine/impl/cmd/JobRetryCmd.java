// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.cfg.TransactionContext;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutor;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import org.zik.bpm.engine.impl.jobexecutor.MessageAddedNotification;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.OptimisticLockingException;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class JobRetryCmd implements Command<Object>
{
    protected static final long serialVersionUID = 1L;
    protected String jobId;
    protected Throwable exception;
    
    public JobRetryCmd(final String jobId, final Throwable exception) {
        this.jobId = jobId;
        this.exception = exception;
    }
    
    protected JobEntity getJob() {
        return Context.getCommandContext().getJobManager().findJobById(this.jobId);
    }
    
    protected void logException(final JobEntity job) {
        if (this.exception != null) {
            job.setExceptionMessage(this.exception.getMessage());
            job.setExceptionStacktrace(this.getExceptionStacktrace());
        }
    }
    
    protected void decrementRetries(final JobEntity job) {
        if (this.exception == null || this.shouldDecrementRetriesFor(this.exception)) {
            job.setRetries(job.getRetries() - 1);
        }
    }
    
    protected String getExceptionStacktrace() {
        return ExceptionUtil.getExceptionStacktrace(this.exception);
    }
    
    protected boolean shouldDecrementRetriesFor(final Throwable t) {
        return !(t instanceof OptimisticLockingException);
    }
    
    protected void notifyAcquisition(final CommandContext commandContext) {
        final JobExecutor jobExecutor = Context.getProcessEngineConfiguration().getJobExecutor();
        final MessageAddedNotification messageAddedNotification = new MessageAddedNotification(jobExecutor);
        final TransactionContext transactionContext = commandContext.getTransactionContext();
        transactionContext.addTransactionListener(TransactionState.COMMITTED, messageAddedNotification);
    }
}
