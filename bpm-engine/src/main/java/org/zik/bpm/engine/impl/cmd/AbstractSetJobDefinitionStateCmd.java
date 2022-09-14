// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.management.UpdateJobSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.jobexecutor.TimerChangeJobDefinitionSuspensionStateJobHandler;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.JobManager;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionManager;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.management.UpdateJobDefinitionSuspensionStateBuilderImpl;
import java.util.Date;

public abstract class AbstractSetJobDefinitionStateCmd extends AbstractSetStateCmd
{
    protected String jobDefinitionId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected Date executionDate;
    protected String processDefinitionTenantId;
    protected boolean isProcessDefinitionTenantIdSet;
    
    public AbstractSetJobDefinitionStateCmd(final UpdateJobDefinitionSuspensionStateBuilderImpl builder) {
        super(builder.isIncludeJobs(), builder.getExecutionDate());
        this.isProcessDefinitionTenantIdSet = false;
        this.jobDefinitionId = builder.getJobDefinitionId();
        this.processDefinitionId = builder.getProcessDefinitionId();
        this.processDefinitionKey = builder.getProcessDefinitionKey();
        this.isProcessDefinitionTenantIdSet = builder.isProcessDefinitionTenantIdSet();
        this.processDefinitionTenantId = builder.getProcessDefinitionTenantId();
    }
    
    @Override
    protected void checkParameters(final CommandContext commandContext) {
        if (this.jobDefinitionId == null && this.processDefinitionId == null && this.processDefinitionKey == null) {
            throw new ProcessEngineException("Job definition id, process definition id nor process definition key cannot be null");
        }
    }
    
    @Override
    protected void checkAuthorization(final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            if (this.jobDefinitionId != null) {
                final JobDefinitionManager jobDefinitionManager = commandContext.getJobDefinitionManager();
                final JobDefinitionEntity jobDefinition = jobDefinitionManager.findById(this.jobDefinitionId);
                if (jobDefinition == null || jobDefinition.getProcessDefinitionKey() == null) {
                    continue;
                }
                final String processDefinitionKey = jobDefinition.getProcessDefinitionKey();
                checker.checkUpdateProcessDefinitionByKey(processDefinitionKey);
                if (!this.includeSubResources) {
                    continue;
                }
                checker.checkUpdateProcessInstanceByProcessDefinitionKey(processDefinitionKey);
            }
            else if (this.processDefinitionId != null) {
                checker.checkUpdateProcessDefinitionById(this.processDefinitionId);
                if (!this.includeSubResources) {
                    continue;
                }
                checker.checkUpdateProcessInstanceByProcessDefinitionId(this.processDefinitionId);
            }
            else {
                if (this.processDefinitionKey == null) {
                    continue;
                }
                checker.checkUpdateProcessDefinitionByKey(this.processDefinitionKey);
                if (!this.includeSubResources) {
                    continue;
                }
                checker.checkUpdateProcessInstanceByProcessDefinitionKey(this.processDefinitionKey);
            }
        }
    }
    
    @Override
    protected void updateSuspensionState(final CommandContext commandContext, final SuspensionState suspensionState) {
        final JobDefinitionManager jobDefinitionManager = commandContext.getJobDefinitionManager();
        final JobManager jobManager = commandContext.getJobManager();
        if (this.jobDefinitionId != null) {
            jobDefinitionManager.updateJobDefinitionSuspensionStateById(this.jobDefinitionId, suspensionState);
        }
        else if (this.processDefinitionId != null) {
            jobDefinitionManager.updateJobDefinitionSuspensionStateByProcessDefinitionId(this.processDefinitionId, suspensionState);
            jobManager.updateStartTimerJobSuspensionStateByProcessDefinitionId(this.processDefinitionId, suspensionState);
        }
        else if (this.processDefinitionKey != null) {
            if (!this.isProcessDefinitionTenantIdSet) {
                jobDefinitionManager.updateJobDefinitionSuspensionStateByProcessDefinitionKey(this.processDefinitionKey, suspensionState);
                jobManager.updateStartTimerJobSuspensionStateByProcessDefinitionKey(this.processDefinitionKey, suspensionState);
            }
            else {
                jobDefinitionManager.updateJobDefinitionSuspensionStateByProcessDefinitionKeyAndTenantId(this.processDefinitionKey, this.processDefinitionTenantId, suspensionState);
                jobManager.updateStartTimerJobSuspensionStateByProcessDefinitionKeyAndTenantId(this.processDefinitionKey, this.processDefinitionTenantId, suspensionState);
            }
        }
    }
    
    @Override
    protected JobHandlerConfiguration getJobHandlerConfiguration() {
        if (this.jobDefinitionId != null) {
            return TimerChangeJobDefinitionSuspensionStateJobHandler.JobDefinitionSuspensionStateConfiguration.byJobDefinitionId(this.jobDefinitionId, this.isIncludeSubResources());
        }
        if (this.processDefinitionId != null) {
            return TimerChangeJobDefinitionSuspensionStateJobHandler.JobDefinitionSuspensionStateConfiguration.byProcessDefinitionId(this.processDefinitionId, this.isIncludeSubResources());
        }
        if (!this.isProcessDefinitionTenantIdSet) {
            return TimerChangeJobDefinitionSuspensionStateJobHandler.JobDefinitionSuspensionStateConfiguration.byProcessDefinitionKey(this.processDefinitionKey, this.isIncludeSubResources());
        }
        return TimerChangeJobDefinitionSuspensionStateJobHandler.JobDefinitionSuspensionStateConfiguration.ByProcessDefinitionKeyAndTenantId(this.processDefinitionKey, this.processDefinitionTenantId, this.isIncludeSubResources());
    }
    
    @Override
    protected void logUserOperation(final CommandContext commandContext) {
        final PropertyChange propertyChange = new PropertyChange("suspensionState", null, this.getNewSuspensionState().getName());
        commandContext.getOperationLogManager().logJobDefinitionOperation(this.getLogEntryOperation(), this.jobDefinitionId, this.processDefinitionId, this.processDefinitionKey, propertyChange);
    }
    
    protected UpdateJobSuspensionStateBuilderImpl createJobCommandBuilder() {
        final UpdateJobSuspensionStateBuilderImpl builder = new UpdateJobSuspensionStateBuilderImpl();
        if (this.jobDefinitionId != null) {
            builder.byJobDefinitionId(this.jobDefinitionId);
        }
        else if (this.processDefinitionId != null) {
            builder.byProcessDefinitionId(this.processDefinitionId);
        }
        else if (this.processDefinitionKey != null) {
            builder.byProcessDefinitionKey(this.processDefinitionKey);
            if (this.isProcessDefinitionTenantIdSet && this.processDefinitionTenantId != null) {
                builder.processDefinitionTenantId(this.processDefinitionTenantId);
            }
            else if (this.isProcessDefinitionTenantIdSet) {
                builder.processDefinitionWithoutTenantId();
            }
        }
        return builder;
    }
    
    @Override
    protected abstract String getDelayedExecutionJobHandlerType();
    
    @Override
    protected AbstractSetStateCmd getNextCommand() {
        final UpdateJobSuspensionStateBuilderImpl jobCommandBuilder = this.createJobCommandBuilder();
        return this.getNextCommand(jobCommandBuilder);
    }
    
    @Override
    protected String getDeploymentId(final CommandContext commandContext) {
        if (this.jobDefinitionId != null) {
            return this.getDeploymentIdByJobDefinition(commandContext, this.jobDefinitionId);
        }
        if (this.processDefinitionId != null) {
            return this.getDeploymentIdByProcessDefinition(commandContext, this.processDefinitionId);
        }
        if (this.processDefinitionKey != null) {
            return this.getDeploymentIdByProcessDefinitionKey(commandContext, this.processDefinitionKey, this.isProcessDefinitionTenantIdSet, this.processDefinitionTenantId);
        }
        return null;
    }
    
    protected abstract AbstractSetJobStateCmd getNextCommand(final UpdateJobSuspensionStateBuilderImpl p0);
}
