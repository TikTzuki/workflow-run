// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutorLogger;
import org.zik.bpm.engine.impl.interceptor.Command;

public class UnlockJobCmd implements Command<Void>
{
    protected static final long serialVersionUID = 1L;
    private static final JobExecutorLogger LOG;
    protected String jobId;
    
    public UnlockJobCmd(final String jobId) {
        this.jobId = jobId;
    }
    
    protected JobEntity getJob() {
        return Context.getCommandContext().getJobManager().findJobById(this.jobId);
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final JobEntity job = this.getJob();
        if (Context.getJobExecutorContext() == null) {
            EnsureUtil.ensureNotNull("Job with id " + this.jobId + " does not exist", "job", job);
        }
        else if (Context.getJobExecutorContext() != null && job == null) {
            UnlockJobCmd.LOG.debugAcquiredJobNotFound(this.jobId);
            return null;
        }
        job.unlock();
        return null;
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
