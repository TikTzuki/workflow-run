// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.TimerEntity;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetJobDuedateCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    private final String jobId;
    private Date newDuedate;
    private final boolean cascade;
    
    public SetJobDuedateCmd(final String jobId, final Date newDuedate, final boolean cascade) {
        if (jobId == null || jobId.length() < 1) {
            throw new ProcessEngineException("The job id is mandatory, but '" + jobId + "' has been provided.");
        }
        this.jobId = jobId;
        this.newDuedate = newDuedate;
        this.cascade = cascade;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final JobEntity job = commandContext.getJobManager().findJobById(this.jobId);
        if (job != null) {
            for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
                checker.checkUpdateJob(job);
            }
            commandContext.getOperationLogManager().logJobOperation("SetDueDate", this.jobId, job.getJobDefinitionId(), job.getProcessInstanceId(), job.getProcessDefinitionId(), job.getProcessDefinitionKey(), Collections.singletonList(new PropertyChange("duedate", job.getDuedate(), this.newDuedate)));
            if (this.cascade && this.newDuedate != null && job instanceof TimerEntity) {
                final long offset = this.newDuedate.getTime() - job.getDuedate().getTime();
                ((TimerEntity)job).setRepeatOffset(((TimerEntity)job).getRepeatOffset() + offset);
            }
            job.setDuedate(this.newDuedate);
            return null;
        }
        throw new ProcessEngineException("No job found with id '" + this.jobId + "'.");
    }
}
