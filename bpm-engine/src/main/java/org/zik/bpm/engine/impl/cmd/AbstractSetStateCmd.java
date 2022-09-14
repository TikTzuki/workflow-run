// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionManager;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.TimerEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.impl.interceptor.Command;

public abstract class AbstractSetStateCmd implements Command<Void>
{
    protected static final String SUSPENSION_STATE_PROPERTY = "suspensionState";
    protected boolean includeSubResources;
    protected boolean isLogUserOperationDisabled;
    protected Date executionDate;
    
    public AbstractSetStateCmd(final boolean includeSubResources, final Date executionDate) {
        this.includeSubResources = includeSubResources;
        this.executionDate = executionDate;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        this.checkParameters(commandContext);
        this.checkAuthorization(commandContext);
        if (this.executionDate == null) {
            this.updateSuspensionState(commandContext, this.getNewSuspensionState());
            if (this.isIncludeSubResources()) {
                final AbstractSetStateCmd cmd = this.getNextCommand();
                if (cmd != null) {
                    cmd.disableLogUserOperation();
                    commandContext.runWithoutAuthorization((Command<Object>)cmd);
                }
            }
            this.triggerHistoryEvent(commandContext);
        }
        else {
            this.scheduleSuspensionStateUpdate(commandContext);
        }
        if (!this.isLogUserOperationDisabled()) {
            this.logUserOperation(commandContext);
        }
        return null;
    }
    
    protected void triggerHistoryEvent(final CommandContext commandContext) {
    }
    
    public void disableLogUserOperation() {
        this.isLogUserOperationDisabled = true;
    }
    
    protected boolean isLogUserOperationDisabled() {
        return this.isLogUserOperationDisabled;
    }
    
    protected boolean isIncludeSubResources() {
        return this.includeSubResources;
    }
    
    protected void scheduleSuspensionStateUpdate(final CommandContext commandContext) {
        final TimerEntity timer = new TimerEntity();
        final JobHandlerConfiguration jobHandlerConfiguration = this.getJobHandlerConfiguration();
        timer.setDuedate(this.executionDate);
        timer.setJobHandlerType(this.getDelayedExecutionJobHandlerType());
        timer.setJobHandlerConfigurationRaw(jobHandlerConfiguration.toCanonicalString());
        timer.setDeploymentId(this.getDeploymentId(commandContext));
        commandContext.getJobManager().schedule(timer);
    }
    
    protected String getDelayedExecutionJobHandlerType() {
        return null;
    }
    
    protected JobHandlerConfiguration getJobHandlerConfiguration() {
        return null;
    }
    
    protected AbstractSetStateCmd getNextCommand() {
        return null;
    }
    
    protected String getDeploymentId(final CommandContext commandContext) {
        return null;
    }
    
    protected abstract void checkAuthorization(final CommandContext p0);
    
    protected abstract void checkParameters(final CommandContext p0);
    
    protected abstract void updateSuspensionState(final CommandContext p0, final SuspensionState p1);
    
    protected abstract void logUserOperation(final CommandContext p0);
    
    protected abstract String getLogEntryOperation();
    
    protected abstract SuspensionState getNewSuspensionState();
    
    protected String getDeploymentIdByProcessDefinition(final CommandContext commandContext, final String processDefinitionId) {
        ProcessDefinitionEntity definition = commandContext.getProcessDefinitionManager().getCachedResourceDefinitionEntity(processDefinitionId);
        if (definition == null) {
            definition = commandContext.getProcessDefinitionManager().findLatestDefinitionById(processDefinitionId);
        }
        if (definition != null) {
            return definition.getDeploymentId();
        }
        return null;
    }
    
    protected String getDeploymentIdByProcessDefinitionKey(final CommandContext commandContext, final String processDefinitionKey, final boolean tenantIdSet, final String tenantId) {
        ProcessDefinitionEntity definition = null;
        if (tenantIdSet) {
            definition = commandContext.getProcessDefinitionManager().findLatestProcessDefinitionByKeyAndTenantId(processDefinitionKey, tenantId);
        }
        else {
            final List<ProcessDefinitionEntity> definitions = commandContext.getProcessDefinitionManager().findLatestProcessDefinitionsByKey(processDefinitionKey);
            definition = (definitions.isEmpty() ? null : definitions.get(0));
        }
        if (definition != null) {
            return definition.getDeploymentId();
        }
        return null;
    }
    
    protected String getDeploymentIdByJobDefinition(final CommandContext commandContext, final String jobDefinitionId) {
        final JobDefinitionManager jobDefinitionManager = commandContext.getJobDefinitionManager();
        final JobDefinitionEntity jobDefinition = jobDefinitionManager.findById(jobDefinitionId);
        if (jobDefinition != null && jobDefinition.getProcessDefinitionId() != null) {
            return this.getDeploymentIdByProcessDefinition(commandContext, jobDefinition.getProcessDefinitionId());
        }
        return null;
    }
}
