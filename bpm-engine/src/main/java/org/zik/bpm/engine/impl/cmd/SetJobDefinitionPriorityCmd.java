// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.oplog.UserOperationLogContextEntry;
import org.zik.bpm.engine.impl.oplog.UserOperationLogContextEntryBuilder;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.oplog.UserOperationLogContext;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetJobDefinitionPriorityCmd implements Command<Void>
{
    public static final String JOB_DEFINITION_OVERRIDING_PRIORITY = "overridingPriority";
    protected String jobDefinitionId;
    protected Long priority;
    protected boolean cascade;
    
    public SetJobDefinitionPriorityCmd(final String jobDefinitionId, final Long priority, final boolean cascade) {
        this.cascade = false;
        this.jobDefinitionId = jobDefinitionId;
        this.priority = priority;
        this.cascade = cascade;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull(NotValidException.class, "jobDefinitionId", (Object)this.jobDefinitionId);
        final JobDefinitionEntity jobDefinition = commandContext.getJobDefinitionManager().findById(this.jobDefinitionId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "Job definition with id '" + this.jobDefinitionId + "' does not exist", "jobDefinition", jobDefinition);
        this.checkUpdateProcess(commandContext, jobDefinition);
        final Long currentPriority = jobDefinition.getOverridingJobPriority();
        jobDefinition.setJobPriority(this.priority);
        final UserOperationLogContext opLogContext = new UserOperationLogContext();
        this.createJobDefinitionOperationLogEntry(opLogContext, currentPriority, jobDefinition);
        if (this.cascade && this.priority != null) {
            commandContext.getJobManager().updateJobPriorityByDefinitionId(this.jobDefinitionId, this.priority);
            this.createCascadeJobsOperationLogEntry(opLogContext, jobDefinition);
        }
        commandContext.getOperationLogManager().logUserOperations(opLogContext);
        return null;
    }
    
    protected void checkUpdateProcess(final CommandContext commandContext, final JobDefinitionEntity jobDefinition) {
        final String processDefinitionId = jobDefinition.getProcessDefinitionId();
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateProcessDefinitionById(processDefinitionId);
            if (this.cascade) {
                checker.checkUpdateProcessInstanceByProcessDefinitionId(processDefinitionId);
            }
        }
    }
    
    protected void createJobDefinitionOperationLogEntry(final UserOperationLogContext opLogContext, final Long previousPriority, final JobDefinitionEntity jobDefinition) {
        final PropertyChange propertyChange = new PropertyChange("overridingPriority", previousPriority, jobDefinition.getOverridingJobPriority());
        final UserOperationLogContextEntry entry = UserOperationLogContextEntryBuilder.entry("SetPriority", "JobDefinition").inContextOf(jobDefinition).propertyChanges(propertyChange).category("Operator").create();
        opLogContext.addEntry(entry);
    }
    
    protected void createCascadeJobsOperationLogEntry(final UserOperationLogContext opLogContext, final JobDefinitionEntity jobDefinition) {
        final PropertyChange propertyChange = new PropertyChange("priority", null, jobDefinition.getOverridingJobPriority());
        final UserOperationLogContextEntry entry = UserOperationLogContextEntryBuilder.entry("SetPriority", "Job").inContextOf(jobDefinition).propertyChanges(propertyChange).category("Operator").create();
        opLogContext.addEntry(entry);
    }
}
