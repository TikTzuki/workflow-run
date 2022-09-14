// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import java.util.Arrays;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.jobexecutor.TimerChangeProcessDefinitionSuspensionStateJobHandler;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.runtime.UpdateProcessInstanceSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionManager;
import org.zik.bpm.engine.impl.management.UpdateJobDefinitionSuspensionStateBuilderImpl;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.repository.UpdateProcessDefinitionSuspensionStateBuilderImpl;

public abstract class AbstractSetProcessDefinitionStateCmd extends AbstractSetStateCmd
{
    public static final String INCLUDE_PROCESS_INSTANCES_PROPERTY = "includeProcessInstances";
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String tenantId;
    protected boolean isTenantIdSet;
    
    public AbstractSetProcessDefinitionStateCmd(final UpdateProcessDefinitionSuspensionStateBuilderImpl builder) {
        super(builder.isIncludeProcessInstances(), builder.getExecutionDate());
        this.isTenantIdSet = false;
        this.processDefinitionId = builder.getProcessDefinitionId();
        this.processDefinitionKey = builder.getProcessDefinitionKey();
        this.isTenantIdSet = builder.isTenantIdSet();
        this.tenantId = builder.getProcessDefinitionTenantId();
    }
    
    @Override
    protected void checkParameters(final CommandContext commandContext) {
        if (this.processDefinitionId == null && this.processDefinitionKey == null) {
            throw new ProcessEngineException("Process definition id / key cannot be null");
        }
    }
    
    @Override
    protected void checkAuthorization(final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            if (this.processDefinitionId != null) {
                checker.checkUpdateProcessDefinitionSuspensionStateById(this.processDefinitionId);
                if (!this.includeSubResources) {
                    continue;
                }
                checker.checkUpdateProcessInstanceSuspensionStateByProcessDefinitionId(this.processDefinitionId);
            }
            else {
                if (this.processDefinitionKey == null) {
                    continue;
                }
                checker.checkUpdateProcessDefinitionSuspensionStateByKey(this.processDefinitionKey);
                if (!this.includeSubResources) {
                    continue;
                }
                checker.checkUpdateProcessInstanceSuspensionStateByProcessDefinitionKey(this.processDefinitionKey);
            }
        }
    }
    
    @Override
    protected void updateSuspensionState(final CommandContext commandContext, final SuspensionState suspensionState) {
        final ProcessDefinitionManager processDefinitionManager = commandContext.getProcessDefinitionManager();
        if (this.processDefinitionId != null) {
            processDefinitionManager.updateProcessDefinitionSuspensionStateById(this.processDefinitionId, suspensionState);
        }
        else if (this.isTenantIdSet) {
            processDefinitionManager.updateProcessDefinitionSuspensionStateByKeyAndTenantId(this.processDefinitionKey, this.tenantId, suspensionState);
        }
        else {
            processDefinitionManager.updateProcessDefinitionSuspensionStateByKey(this.processDefinitionKey, suspensionState);
        }
        commandContext.runWithoutAuthorization((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final UpdateJobDefinitionSuspensionStateBuilderImpl jobDefinitionSuspensionStateBuilder = AbstractSetProcessDefinitionStateCmd.this.createJobDefinitionCommandBuilder();
                final AbstractSetJobDefinitionStateCmd jobDefinitionCmd = AbstractSetProcessDefinitionStateCmd.this.getSetJobDefinitionStateCmd(jobDefinitionSuspensionStateBuilder);
                jobDefinitionCmd.disableLogUserOperation();
                jobDefinitionCmd.execute(commandContext);
                return null;
            }
        });
    }
    
    protected UpdateJobDefinitionSuspensionStateBuilderImpl createJobDefinitionCommandBuilder() {
        final UpdateJobDefinitionSuspensionStateBuilderImpl jobDefinitionBuilder = new UpdateJobDefinitionSuspensionStateBuilderImpl();
        if (this.processDefinitionId != null) {
            jobDefinitionBuilder.byProcessDefinitionId(this.processDefinitionId);
        }
        else if (this.processDefinitionKey != null) {
            jobDefinitionBuilder.byProcessDefinitionKey(this.processDefinitionKey);
            if (this.isTenantIdSet && this.tenantId != null) {
                jobDefinitionBuilder.processDefinitionTenantId(this.tenantId);
            }
            else if (this.isTenantIdSet) {
                jobDefinitionBuilder.processDefinitionWithoutTenantId();
            }
        }
        return jobDefinitionBuilder;
    }
    
    protected UpdateProcessInstanceSuspensionStateBuilderImpl createProcessInstanceCommandBuilder() {
        final UpdateProcessInstanceSuspensionStateBuilderImpl processInstanceBuilder = new UpdateProcessInstanceSuspensionStateBuilderImpl();
        if (this.processDefinitionId != null) {
            processInstanceBuilder.byProcessDefinitionId(this.processDefinitionId);
        }
        else if (this.processDefinitionKey != null) {
            processInstanceBuilder.byProcessDefinitionKey(this.processDefinitionKey);
            if (this.isTenantIdSet && this.tenantId != null) {
                processInstanceBuilder.processDefinitionTenantId(this.tenantId);
            }
            else if (this.isTenantIdSet) {
                processInstanceBuilder.processDefinitionWithoutTenantId();
            }
        }
        return processInstanceBuilder;
    }
    
    @Override
    protected JobHandlerConfiguration getJobHandlerConfiguration() {
        if (this.processDefinitionId != null) {
            return TimerChangeProcessDefinitionSuspensionStateJobHandler.ProcessDefinitionSuspensionStateConfiguration.byProcessDefinitionId(this.processDefinitionId, this.isIncludeSubResources());
        }
        if (this.isTenantIdSet) {
            return TimerChangeProcessDefinitionSuspensionStateJobHandler.ProcessDefinitionSuspensionStateConfiguration.byProcessDefinitionKeyAndTenantId(this.processDefinitionKey, this.tenantId, this.isIncludeSubResources());
        }
        return TimerChangeProcessDefinitionSuspensionStateJobHandler.ProcessDefinitionSuspensionStateConfiguration.byProcessDefinitionKey(this.processDefinitionKey, this.isIncludeSubResources());
    }
    
    @Override
    protected void logUserOperation(final CommandContext commandContext) {
        final PropertyChange suspensionStateChanged = new PropertyChange("suspensionState", null, this.getNewSuspensionState().getName());
        final PropertyChange includeProcessInstances = new PropertyChange("includeProcessInstances", null, this.isIncludeSubResources());
        commandContext.getOperationLogManager().logProcessDefinitionOperation(this.getLogEntryOperation(), this.processDefinitionId, this.processDefinitionKey, Arrays.asList(suspensionStateChanged, includeProcessInstances));
    }
    
    @Override
    protected abstract String getDelayedExecutionJobHandlerType();
    
    protected abstract AbstractSetJobDefinitionStateCmd getSetJobDefinitionStateCmd(final UpdateJobDefinitionSuspensionStateBuilderImpl p0);
    
    @Override
    protected AbstractSetProcessInstanceStateCmd getNextCommand() {
        final UpdateProcessInstanceSuspensionStateBuilderImpl processInstanceCommandBuilder = this.createProcessInstanceCommandBuilder();
        return this.getNextCommand(processInstanceCommandBuilder);
    }
    
    @Override
    protected String getDeploymentId(final CommandContext commandContext) {
        if (this.processDefinitionId != null) {
            return this.getDeploymentIdByProcessDefinition(commandContext, this.processDefinitionId);
        }
        if (this.processDefinitionKey != null) {
            return this.getDeploymentIdByProcessDefinitionKey(commandContext, this.processDefinitionKey, this.isTenantIdSet, this.tenantId);
        }
        return null;
    }
    
    protected abstract AbstractSetProcessInstanceStateCmd getNextCommand(final UpdateProcessInstanceSuspensionStateBuilderImpl p0);
}
