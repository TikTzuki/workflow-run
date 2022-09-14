// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetJobPriorityCmd implements Command<Void>
{
    public static final String JOB_PRIORITY_PROPERTY = "priority";
    protected String jobId;
    protected long priority;
    
    public SetJobPriorityCmd(final String jobId, final long priority) {
        this.jobId = jobId;
        this.priority = priority;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("job id must not be null", "jobId", this.jobId);
        final JobEntity job = commandContext.getJobManager().findJobById(this.jobId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "No job found with id '" + this.jobId + "'", "job", job);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateJob(job);
        }
        final long currentPriority = job.getPriority();
        job.setPriority(this.priority);
        this.createOpLogEntry(commandContext, currentPriority, job);
        return null;
    }
    
    protected void createOpLogEntry(final CommandContext commandContext, final long previousPriority, final JobEntity job) {
        final PropertyChange propertyChange = new PropertyChange("priority", previousPriority, job.getPriority());
        commandContext.getOperationLogManager().logJobOperation("SetPriority", job.getId(), job.getJobDefinitionId(), job.getProcessInstanceId(), job.getProcessDefinitionId(), job.getProcessDefinitionKey(), propertyChange);
    }
}
