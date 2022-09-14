// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteJobCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String jobId;
    
    public DeleteJobCmd(final String jobId) {
        this.jobId = jobId;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("jobId", (Object)this.jobId);
        final JobEntity job = commandContext.getJobManager().findJobById(this.jobId);
        EnsureUtil.ensureNotNull("No job found with id '" + this.jobId + "'", "job", job);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateJob(job);
        }
        if (job.getLockOwner() != null || job.getLockExpirationTime() != null) {
            throw new ProcessEngineException("Cannot delete job when the job is being executed. Try again later.");
        }
        commandContext.getOperationLogManager().logJobOperation("Delete", this.jobId, job.getJobDefinitionId(), job.getProcessInstanceId(), job.getProcessDefinitionId(), job.getProcessDefinitionKey(), PropertyChange.EMPTY_CHANGE);
        job.delete();
        return null;
    }
}
