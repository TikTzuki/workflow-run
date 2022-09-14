// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.management.UpdateJobSuspensionStateBuilderImpl;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.Collection;
import org.zik.bpm.engine.impl.Page;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import java.util.List;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskManager;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionManager;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.Iterator;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.impl.runtime.UpdateProcessInstanceSuspensionStateBuilderImpl;

public abstract class AbstractSetProcessInstanceStateCmd extends AbstractSetStateCmd
{
    protected final String processInstanceId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String processDefinitionTenantId;
    protected boolean isProcessDefinitionTenantIdSet;
    
    public AbstractSetProcessInstanceStateCmd(final UpdateProcessInstanceSuspensionStateBuilderImpl builder) {
        super(true, null);
        this.isProcessDefinitionTenantIdSet = false;
        this.processInstanceId = builder.getProcessInstanceId();
        this.processDefinitionId = builder.getProcessDefinitionId();
        this.processDefinitionKey = builder.getProcessDefinitionKey();
        this.processDefinitionTenantId = builder.getProcessDefinitionTenantId();
        this.isProcessDefinitionTenantIdSet = builder.isProcessDefinitionTenantIdSet();
    }
    
    @Override
    protected void checkParameters(final CommandContext commandContext) {
        if (this.processInstanceId == null && this.processDefinitionId == null && this.processDefinitionKey == null) {
            throw new ProcessEngineException("ProcessInstanceId, ProcessDefinitionId nor ProcessDefinitionKey cannot be null.");
        }
    }
    
    @Override
    protected void checkAuthorization(final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            if (this.processInstanceId != null) {
                checker.checkUpdateProcessInstanceSuspensionStateById(this.processInstanceId);
            }
            else if (this.processDefinitionId != null) {
                checker.checkUpdateProcessInstanceSuspensionStateByProcessDefinitionId(this.processDefinitionId);
            }
            else {
                if (this.processDefinitionKey == null) {
                    continue;
                }
                checker.checkUpdateProcessInstanceSuspensionStateByProcessDefinitionKey(this.processDefinitionKey);
            }
        }
    }
    
    @Override
    protected void updateSuspensionState(final CommandContext commandContext, final SuspensionState suspensionState) {
        final ExecutionManager executionManager = commandContext.getExecutionManager();
        final TaskManager taskManager = commandContext.getTaskManager();
        final ExternalTaskManager externalTaskManager = commandContext.getExternalTaskManager();
        if (this.processInstanceId != null) {
            executionManager.updateExecutionSuspensionStateByProcessInstanceId(this.processInstanceId, suspensionState);
            taskManager.updateTaskSuspensionStateByProcessInstanceId(this.processInstanceId, suspensionState);
            externalTaskManager.updateExternalTaskSuspensionStateByProcessInstanceId(this.processInstanceId, suspensionState);
        }
        else if (this.processDefinitionId != null) {
            executionManager.updateExecutionSuspensionStateByProcessDefinitionId(this.processDefinitionId, suspensionState);
            taskManager.updateTaskSuspensionStateByProcessDefinitionId(this.processDefinitionId, suspensionState);
            externalTaskManager.updateExternalTaskSuspensionStateByProcessDefinitionId(this.processDefinitionId, suspensionState);
        }
        else if (this.isProcessDefinitionTenantIdSet) {
            executionManager.updateExecutionSuspensionStateByProcessDefinitionKeyAndTenantId(this.processDefinitionKey, this.processDefinitionTenantId, suspensionState);
            taskManager.updateTaskSuspensionStateByProcessDefinitionKeyAndTenantId(this.processDefinitionKey, this.processDefinitionTenantId, suspensionState);
            externalTaskManager.updateExternalTaskSuspensionStateByProcessDefinitionKeyAndTenantId(this.processDefinitionKey, this.processDefinitionTenantId, suspensionState);
        }
        else {
            executionManager.updateExecutionSuspensionStateByProcessDefinitionKey(this.processDefinitionKey, suspensionState);
            taskManager.updateTaskSuspensionStateByProcessDefinitionKey(this.processDefinitionKey, suspensionState);
            externalTaskManager.updateExternalTaskSuspensionStateByProcessDefinitionKey(this.processDefinitionKey, suspensionState);
        }
    }
    
    @Override
    protected void triggerHistoryEvent(final CommandContext commandContext) {
        final HistoryLevel historyLevel = commandContext.getProcessEngineConfiguration().getHistoryLevel();
        final List<ProcessInstance> updatedProcessInstances = this.obtainProcessInstances(commandContext);
        if (this.getNewSuspensionState() != null && updatedProcessInstances != null) {
            for (final ProcessInstance processInstance : updatedProcessInstances) {
                if (historyLevel.isHistoryEventProduced(HistoryEventTypes.PROCESS_INSTANCE_UPDATE, processInstance)) {
                    HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                        @Override
                        public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                            final HistoricProcessInstanceEventEntity processInstanceUpdateEvt = (HistoricProcessInstanceEventEntity)producer.createProcessInstanceUpdateEvt((DelegateExecution)processInstance);
                            if (SuspensionState.SUSPENDED.getStateCode() == AbstractSetProcessInstanceStateCmd.this.getNewSuspensionState().getStateCode()) {
                                processInstanceUpdateEvt.setState("SUSPENDED");
                            }
                            else {
                                processInstanceUpdateEvt.setState("ACTIVE");
                            }
                            return processInstanceUpdateEvt;
                        }
                    });
                }
            }
        }
    }
    
    protected List<ProcessInstance> obtainProcessInstances(final CommandContext commandContext) {
        final ProcessInstanceQueryImpl query = new ProcessInstanceQueryImpl();
        if (this.processInstanceId != null) {
            query.processInstanceId(this.processInstanceId);
        }
        else if (this.processDefinitionId != null) {
            query.processDefinitionId(this.processDefinitionId);
        }
        else if (this.isProcessDefinitionTenantIdSet) {
            query.processDefinitionKey(this.processDefinitionKey);
            if (this.processDefinitionTenantId != null) {
                query.tenantIdIn(this.processDefinitionTenantId);
            }
            else {
                query.withoutTenantId();
            }
        }
        else {
            query.processDefinitionKey(this.processDefinitionKey);
        }
        final List<ProcessInstance> result = new ArrayList<ProcessInstance>();
        result.addAll(commandContext.getExecutionManager().findProcessInstancesByQueryCriteria(query, null));
        return result;
    }
    
    @Override
    protected void logUserOperation(final CommandContext commandContext) {
        final PropertyChange propertyChange = new PropertyChange("suspensionState", null, this.getNewSuspensionState().getName());
        commandContext.getOperationLogManager().logProcessInstanceOperation(this.getLogEntryOperation(), this.processInstanceId, this.processDefinitionId, this.processDefinitionKey, Collections.singletonList(propertyChange));
    }
    
    protected UpdateJobSuspensionStateBuilderImpl createJobCommandBuilder() {
        final UpdateJobSuspensionStateBuilderImpl builder = new UpdateJobSuspensionStateBuilderImpl();
        if (this.processInstanceId != null) {
            builder.byProcessInstanceId(this.processInstanceId);
        }
        else if (this.processDefinitionId != null) {
            builder.byProcessDefinitionId(this.processDefinitionId);
        }
        else if (this.processDefinitionKey != null) {
            builder.byProcessDefinitionKey(this.processDefinitionKey);
            if (this.isProcessDefinitionTenantIdSet && this.processDefinitionTenantId != null) {
                return builder.processDefinitionTenantId(this.processDefinitionTenantId);
            }
            if (this.isProcessDefinitionTenantIdSet) {
                return builder.processDefinitionWithoutTenantId();
            }
        }
        return builder;
    }
    
    @Override
    protected AbstractSetJobStateCmd getNextCommand() {
        final UpdateJobSuspensionStateBuilderImpl jobCommandBuilder = this.createJobCommandBuilder();
        return this.getNextCommand(jobCommandBuilder);
    }
    
    protected abstract AbstractSetJobStateCmd getNextCommand(final UpdateJobSuspensionStateBuilderImpl p0);
}
