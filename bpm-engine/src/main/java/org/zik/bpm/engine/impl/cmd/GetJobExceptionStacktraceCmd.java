// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetJobExceptionStacktraceCmd implements Command<String>, Serializable
{
    private static final long serialVersionUID = 1L;
    private String jobId;
    
    public GetJobExceptionStacktraceCmd(final String jobId) {
        this.jobId = jobId;
    }
    
    @Override
    public String execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("jobId", (Object)this.jobId);
        final JobEntity job = commandContext.getJobManager().findJobById(this.jobId);
        EnsureUtil.ensureNotNull("No job found with id " + this.jobId, "job", job);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadJob(job);
        }
        return job.getExceptionStacktrace();
    }
}
