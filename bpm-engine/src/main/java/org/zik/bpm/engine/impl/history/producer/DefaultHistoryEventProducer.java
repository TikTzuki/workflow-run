// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.producer;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.history.event.HistoricExternalTaskLogEntity;
import org.zik.bpm.engine.history.ExternalTaskState;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.externaltask.ExternalTask;
import org.zik.bpm.engine.management.JobDefinition;
import org.zik.bpm.engine.history.JobState;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.history.DefaultHistoryRemovalTimeProvider;
import org.zik.bpm.engine.impl.util.ParseUtil;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.history.event.HistoricIdentityLinkLogEventEntity;
import org.zik.bpm.engine.task.IdentityLink;
import org.zik.bpm.engine.impl.cfg.IdGenerator;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.impl.persistence.entity.HistoricJobLogEventEntity;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.impl.history.event.HistoricFormPropertyEventEntity;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.history.IncidentState;
import org.zik.bpm.engine.impl.persistence.entity.IncidentEntity;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.impl.history.event.HistoricIncidentEventEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.oplog.UserOperationLogContextEntry;
import org.zik.bpm.engine.impl.oplog.UserOperationLogContext;
import org.zik.bpm.engine.impl.history.event.UserOperationLogEntryEventEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.history.event.HistoricVariableUpdateEventEntity;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.history.event.HistoricTaskInstanceEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.migration.instance.MigratingActivityInstance;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.runtime.CompensationBehavior;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.zik.bpm.engine.impl.cfg.ConfigurationLogger;

public class DefaultHistoryEventProducer implements HistoryEventProducer
{
    protected static final ConfigurationLogger LOG;
    
    protected void initActivityInstanceEvent(final HistoricActivityInstanceEventEntity evt, final ExecutionEntity execution, final HistoryEventType eventType) {
        PvmScope eventSource = execution.getActivity();
        if (eventSource == null) {
            eventSource = (PvmScope)execution.getEventSource();
        }
        final String activityInstanceId = execution.getActivityInstanceId();
        String parentActivityInstanceId = null;
        final ExecutionEntity parentExecution = execution.getParent();
        if (parentExecution != null && CompensationBehavior.isCompensationThrowing(parentExecution) && execution.getActivity() != null) {
            parentActivityInstanceId = CompensationBehavior.getParentActivityInstanceId(execution);
        }
        else {
            parentActivityInstanceId = execution.getParentActivityInstanceId();
        }
        this.initActivityInstanceEvent(evt, execution, eventSource, activityInstanceId, parentActivityInstanceId, eventType);
    }
    
    protected void initActivityInstanceEvent(final HistoricActivityInstanceEventEntity evt, final MigratingActivityInstance migratingActivityInstance, final HistoryEventType eventType) {
        final PvmScope eventSource = migratingActivityInstance.getTargetScope();
        final String activityInstanceId = migratingActivityInstance.getActivityInstanceId();
        final MigratingActivityInstance parentInstance = migratingActivityInstance.getParent();
        String parentActivityInstanceId = null;
        if (parentInstance != null) {
            parentActivityInstanceId = parentInstance.getActivityInstanceId();
        }
        final ExecutionEntity execution = migratingActivityInstance.resolveRepresentativeExecution();
        this.initActivityInstanceEvent(evt, execution, eventSource, activityInstanceId, parentActivityInstanceId, eventType);
    }
    
    protected void initActivityInstanceEvent(final HistoricActivityInstanceEventEntity evt, final ExecutionEntity execution, final PvmScope eventSource, final String activityInstanceId, final String parentActivityInstanceId, final HistoryEventType eventType) {
        evt.setId(activityInstanceId);
        evt.setEventType(eventType.getEventName());
        evt.setActivityInstanceId(activityInstanceId);
        evt.setParentActivityInstanceId(parentActivityInstanceId);
        evt.setProcessDefinitionId(execution.getProcessDefinitionId());
        evt.setProcessInstanceId(execution.getProcessInstanceId());
        evt.setExecutionId(execution.getId());
        evt.setTenantId(execution.getTenantId());
        evt.setRootProcessInstanceId(execution.getRootProcessInstanceId());
        if (this.isHistoryRemovalTimeStrategyStart()) {
            this.provideRemovalTime(evt);
        }
        final ProcessDefinitionEntity definition = execution.getProcessDefinition();
        if (definition != null) {
            evt.setProcessDefinitionKey(definition.getKey());
        }
        evt.setActivityId(eventSource.getId());
        evt.setActivityName((String)eventSource.getProperty("name"));
        evt.setActivityType((String)eventSource.getProperty("type"));
        final ExecutionEntity subProcessInstance = execution.getSubProcessInstance();
        if (subProcessInstance != null) {
            evt.setCalledProcessInstanceId(subProcessInstance.getId());
        }
        final CaseExecutionEntity subCaseInstance = execution.getSubCaseInstance();
        if (subCaseInstance != null) {
            evt.setCalledCaseInstanceId(subCaseInstance.getId());
        }
    }
    
    protected void initProcessInstanceEvent(final HistoricProcessInstanceEventEntity evt, final ExecutionEntity execution, final HistoryEventType eventType) {
        final String processDefinitionId = execution.getProcessDefinitionId();
        final String processInstanceId = execution.getProcessInstanceId();
        final String executionId = execution.getId();
        final String caseInstanceId = execution.getCaseInstanceId();
        final String tenantId = execution.getTenantId();
        final ProcessDefinitionEntity definition = execution.getProcessDefinition();
        String processDefinitionKey = null;
        if (definition != null) {
            processDefinitionKey = definition.getKey();
        }
        evt.setId(processInstanceId);
        evt.setEventType(eventType.getEventName());
        evt.setProcessDefinitionKey(processDefinitionKey);
        evt.setProcessDefinitionId(processDefinitionId);
        evt.setProcessInstanceId(processInstanceId);
        evt.setExecutionId(executionId);
        evt.setBusinessKey(execution.getProcessBusinessKey());
        evt.setCaseInstanceId(caseInstanceId);
        evt.setTenantId(tenantId);
        evt.setRootProcessInstanceId(execution.getRootProcessInstanceId());
        if (execution.getSuperCaseExecution() != null) {
            evt.setSuperCaseInstanceId(execution.getSuperCaseExecution().getCaseInstanceId());
        }
        if (execution.getSuperExecution() != null) {
            evt.setSuperProcessInstanceId(execution.getSuperExecution().getProcessInstanceId());
        }
    }
    
    protected void initTaskInstanceEvent(final HistoricTaskInstanceEventEntity evt, final TaskEntity taskEntity, final HistoryEventType eventType) {
        String processDefinitionKey = null;
        final ProcessDefinitionEntity definition = taskEntity.getProcessDefinition();
        if (definition != null) {
            processDefinitionKey = definition.getKey();
        }
        final String processDefinitionId = taskEntity.getProcessDefinitionId();
        final String processInstanceId = taskEntity.getProcessInstanceId();
        final String executionId = taskEntity.getExecutionId();
        String caseDefinitionKey = null;
        final CaseDefinitionEntity caseDefinition = taskEntity.getCaseDefinition();
        if (caseDefinition != null) {
            caseDefinitionKey = caseDefinition.getKey();
        }
        final String caseDefinitionId = taskEntity.getCaseDefinitionId();
        final String caseExecutionId = taskEntity.getCaseExecutionId();
        final String caseInstanceId = taskEntity.getCaseInstanceId();
        final String tenantId = taskEntity.getTenantId();
        evt.setId(taskEntity.getId());
        evt.setEventType(eventType.getEventName());
        evt.setTaskId(taskEntity.getId());
        evt.setProcessDefinitionKey(processDefinitionKey);
        evt.setProcessDefinitionId(processDefinitionId);
        evt.setProcessInstanceId(processInstanceId);
        evt.setExecutionId(executionId);
        evt.setCaseDefinitionKey(caseDefinitionKey);
        evt.setCaseDefinitionId(caseDefinitionId);
        evt.setCaseExecutionId(caseExecutionId);
        evt.setCaseInstanceId(caseInstanceId);
        evt.setAssignee(taskEntity.getAssignee());
        evt.setDescription(taskEntity.getDescription());
        evt.setDueDate(taskEntity.getDueDate());
        evt.setFollowUpDate(taskEntity.getFollowUpDate());
        evt.setName(taskEntity.getName());
        evt.setOwner(taskEntity.getOwner());
        evt.setParentTaskId(taskEntity.getParentTaskId());
        evt.setPriority(taskEntity.getPriority());
        evt.setTaskDefinitionKey(taskEntity.getTaskDefinitionKey());
        evt.setTenantId(tenantId);
        final ExecutionEntity execution = taskEntity.getExecution();
        if (execution != null) {
            evt.setActivityInstanceId(execution.getActivityInstanceId());
            evt.setRootProcessInstanceId(execution.getRootProcessInstanceId());
            if (this.isHistoryRemovalTimeStrategyStart()) {
                this.provideRemovalTime(evt);
            }
        }
    }
    
    protected void initHistoricVariableUpdateEvt(final HistoricVariableUpdateEventEntity evt, final VariableInstanceEntity variableInstance, final HistoryEventType eventType) {
        evt.setEventType(eventType.getEventName());
        evt.setTimestamp(ClockUtil.getCurrentTime());
        evt.setVariableInstanceId(variableInstance.getId());
        evt.setProcessInstanceId(variableInstance.getProcessInstanceId());
        evt.setExecutionId(variableInstance.getExecutionId());
        evt.setCaseInstanceId(variableInstance.getCaseInstanceId());
        evt.setCaseExecutionId(variableInstance.getCaseExecutionId());
        evt.setTaskId(variableInstance.getTaskId());
        evt.setRevision(variableInstance.getRevision());
        evt.setVariableName(variableInstance.getName());
        evt.setSerializerName(variableInstance.getSerializerName());
        evt.setTenantId(variableInstance.getTenantId());
        evt.setUserOperationId(Context.getCommandContext().getOperationId());
        final ExecutionEntity execution = variableInstance.getExecution();
        if (execution != null) {
            final ProcessDefinitionEntity definition = execution.getProcessDefinition();
            if (definition != null) {
                evt.setProcessDefinitionId(definition.getId());
                evt.setProcessDefinitionKey(definition.getKey());
            }
            evt.setRootProcessInstanceId(execution.getRootProcessInstanceId());
            if (this.isHistoryRemovalTimeStrategyStart()) {
                this.provideRemovalTime(evt);
            }
        }
        final CaseExecutionEntity caseExecution = variableInstance.getCaseExecution();
        if (caseExecution != null) {
            final CaseDefinitionEntity definition2 = (CaseDefinitionEntity)caseExecution.getCaseDefinition();
            if (definition2 != null) {
                evt.setCaseDefinitionId(definition2.getId());
                evt.setCaseDefinitionKey(definition2.getKey());
            }
        }
        evt.setTextValue(variableInstance.getTextValue());
        evt.setTextValue2(variableInstance.getTextValue2());
        evt.setDoubleValue(variableInstance.getDoubleValue());
        evt.setLongValue(variableInstance.getLongValue());
        if (variableInstance.getByteArrayValueId() != null) {
            evt.setByteValue(variableInstance.getByteArrayValue());
        }
    }
    
    protected void initUserOperationLogEvent(final UserOperationLogEntryEventEntity evt, final UserOperationLogContext context, final UserOperationLogContextEntry contextEntry, final PropertyChange propertyChange) {
        evt.setDeploymentId(contextEntry.getDeploymentId());
        evt.setEntityType(contextEntry.getEntityType());
        evt.setOperationType(contextEntry.getOperationType());
        evt.setOperationId(context.getOperationId());
        evt.setUserId(context.getUserId());
        evt.setProcessDefinitionId(contextEntry.getProcessDefinitionId());
        evt.setProcessDefinitionKey(contextEntry.getProcessDefinitionKey());
        evt.setProcessInstanceId(contextEntry.getProcessInstanceId());
        evt.setExecutionId(contextEntry.getExecutionId());
        evt.setCaseDefinitionId(contextEntry.getCaseDefinitionId());
        evt.setCaseInstanceId(contextEntry.getCaseInstanceId());
        evt.setCaseExecutionId(contextEntry.getCaseExecutionId());
        evt.setTaskId(contextEntry.getTaskId());
        evt.setJobId(contextEntry.getJobId());
        evt.setJobDefinitionId(contextEntry.getJobDefinitionId());
        evt.setBatchId(contextEntry.getBatchId());
        evt.setCategory(contextEntry.getCategory());
        evt.setTimestamp(ClockUtil.getCurrentTime());
        evt.setRootProcessInstanceId(contextEntry.getRootProcessInstanceId());
        evt.setExternalTaskId(contextEntry.getExternalTaskId());
        evt.setAnnotation(contextEntry.getAnnotation());
        if (this.isHistoryRemovalTimeStrategyStart()) {
            this.provideRemovalTime(evt);
        }
        evt.setProperty(propertyChange.getPropertyName());
        evt.setOrgValue(propertyChange.getOrgValueString());
        evt.setNewValue(propertyChange.getNewValueString());
    }
    
    protected void initHistoricIncidentEvent(final HistoricIncidentEventEntity evt, final Incident incident, final HistoryEventType eventType) {
        evt.setId(incident.getId());
        evt.setProcessDefinitionId(incident.getProcessDefinitionId());
        evt.setProcessInstanceId(incident.getProcessInstanceId());
        evt.setExecutionId(incident.getExecutionId());
        evt.setCreateTime(incident.getIncidentTimestamp());
        evt.setIncidentType(incident.getIncidentType());
        evt.setActivityId(incident.getActivityId());
        evt.setCauseIncidentId(incident.getCauseIncidentId());
        evt.setRootCauseIncidentId(incident.getRootCauseIncidentId());
        evt.setConfiguration(incident.getConfiguration());
        evt.setIncidentMessage(incident.getIncidentMessage());
        evt.setTenantId(incident.getTenantId());
        evt.setJobDefinitionId(incident.getJobDefinitionId());
        evt.setHistoryConfiguration(incident.getHistoryConfiguration());
        evt.setFailedActivityId(incident.getFailedActivityId());
        evt.setAnnotation(incident.getAnnotation());
        final String jobId = incident.getConfiguration();
        if (jobId != null && this.isHistoryRemovalTimeStrategyStart()) {
            final HistoricBatchEntity historicBatch = this.getHistoricBatchByJobId(jobId);
            if (historicBatch != null) {
                evt.setRemovalTime(historicBatch.getRemovalTime());
            }
        }
        final IncidentEntity incidentEntity = (IncidentEntity)incident;
        final ProcessDefinitionEntity definition = incidentEntity.getProcessDefinition();
        if (definition != null) {
            evt.setProcessDefinitionKey(definition.getKey());
        }
        final ExecutionEntity execution = incidentEntity.getExecution();
        if (execution != null) {
            evt.setRootProcessInstanceId(execution.getRootProcessInstanceId());
            if (this.isHistoryRemovalTimeStrategyStart()) {
                this.provideRemovalTime(evt);
            }
        }
        evt.setEventType(eventType.getEventName());
        IncidentState incidentState = IncidentState.DEFAULT;
        if (HistoryEventTypes.INCIDENT_DELETE.equals(eventType)) {
            incidentState = IncidentState.DELETED;
        }
        else if (HistoryEventTypes.INCIDENT_RESOLVE.equals(eventType)) {
            incidentState = IncidentState.RESOLVED;
        }
        evt.setIncidentState(incidentState.getStateCode());
    }
    
    protected HistoryEvent createHistoricVariableEvent(final VariableInstanceEntity variableInstance, final VariableScope sourceVariableScope, final HistoryEventType eventType) {
        String scopeActivityInstanceId = null;
        String sourceActivityInstanceId = null;
        if (variableInstance.getExecutionId() != null) {
            final ExecutionEntity scopeExecution = Context.getCommandContext().getDbEntityManager().selectById(ExecutionEntity.class, variableInstance.getExecutionId());
            if (variableInstance.getTaskId() == null && !variableInstance.isConcurrentLocal()) {
                scopeActivityInstanceId = scopeExecution.getParentActivityInstanceId();
            }
            else {
                scopeActivityInstanceId = scopeExecution.getActivityInstanceId();
            }
        }
        else if (variableInstance.getCaseExecutionId() != null) {
            scopeActivityInstanceId = variableInstance.getCaseExecutionId();
        }
        ExecutionEntity sourceExecution = null;
        CaseExecutionEntity sourceCaseExecution = null;
        if (sourceVariableScope instanceof ExecutionEntity) {
            sourceExecution = (ExecutionEntity)sourceVariableScope;
            sourceActivityInstanceId = sourceExecution.getActivityInstanceId();
        }
        else if (sourceVariableScope instanceof TaskEntity) {
            sourceExecution = ((TaskEntity)sourceVariableScope).getExecution();
            if (sourceExecution != null) {
                sourceActivityInstanceId = sourceExecution.getActivityInstanceId();
            }
            else {
                sourceCaseExecution = ((TaskEntity)sourceVariableScope).getCaseExecution();
                if (sourceCaseExecution != null) {
                    sourceActivityInstanceId = sourceCaseExecution.getId();
                }
            }
        }
        else if (sourceVariableScope instanceof CaseExecutionEntity) {
            sourceCaseExecution = (CaseExecutionEntity)sourceVariableScope;
            sourceActivityInstanceId = sourceCaseExecution.getId();
        }
        final HistoricVariableUpdateEventEntity evt = this.newVariableUpdateEventEntity(sourceExecution);
        this.initHistoricVariableUpdateEvt(evt, variableInstance, eventType);
        this.initSequenceCounter(variableInstance, evt);
        evt.setScopeActivityInstanceId(scopeActivityInstanceId);
        evt.setActivityInstanceId(sourceActivityInstanceId);
        if (sourceExecution != null && sourceExecution.isProcessInstanceStarting() && HistoryEventTypes.VARIABLE_INSTANCE_CREATE.equals(eventType)) {
            if (variableInstance.getSequenceCounter() == 1L) {
                evt.setInitial(true);
            }
            if (sourceActivityInstanceId == null && sourceExecution.getActivity() != null && sourceExecution.getTransition() == null) {
                evt.setActivityInstanceId(sourceExecution.getProcessInstanceId());
            }
        }
        return evt;
    }
    
    protected HistoricProcessInstanceEventEntity newProcessInstanceEventEntity(final ExecutionEntity execution) {
        return new HistoricProcessInstanceEventEntity();
    }
    
    protected HistoricActivityInstanceEventEntity newActivityInstanceEventEntity(final ExecutionEntity execution) {
        return new HistoricActivityInstanceEventEntity();
    }
    
    protected HistoricTaskInstanceEventEntity newTaskInstanceEventEntity(final DelegateTask task) {
        return new HistoricTaskInstanceEventEntity();
    }
    
    protected HistoricVariableUpdateEventEntity newVariableUpdateEventEntity(final ExecutionEntity execution) {
        return new HistoricVariableUpdateEventEntity();
    }
    
    protected HistoricFormPropertyEventEntity newHistoricFormPropertyEvent() {
        return new HistoricFormPropertyEventEntity();
    }
    
    protected HistoricIncidentEventEntity newIncidentEventEntity(final Incident incident) {
        return new HistoricIncidentEventEntity();
    }
    
    protected HistoricJobLogEventEntity newHistoricJobLogEntity(final Job job) {
        return new HistoricJobLogEventEntity();
    }
    
    protected HistoricBatchEntity newBatchEventEntity(final BatchEntity batch) {
        return new HistoricBatchEntity();
    }
    
    protected HistoricProcessInstanceEventEntity loadProcessInstanceEventEntity(final ExecutionEntity execution) {
        return this.newProcessInstanceEventEntity(execution);
    }
    
    protected HistoricActivityInstanceEventEntity loadActivityInstanceEventEntity(final ExecutionEntity execution) {
        return this.newActivityInstanceEventEntity(execution);
    }
    
    protected HistoricTaskInstanceEventEntity loadTaskInstanceEvent(final DelegateTask task) {
        return this.newTaskInstanceEventEntity(task);
    }
    
    protected HistoricIncidentEventEntity loadIncidentEvent(final Incident incident) {
        return this.newIncidentEventEntity(incident);
    }
    
    protected HistoricBatchEntity loadBatchEntity(final BatchEntity batch) {
        return this.newBatchEventEntity(batch);
    }
    
    @Override
    public HistoryEvent createProcessInstanceStartEvt(final DelegateExecution execution) {
        final ExecutionEntity executionEntity = (ExecutionEntity)execution;
        final HistoricProcessInstanceEventEntity evt = this.newProcessInstanceEventEntity(executionEntity);
        this.initProcessInstanceEvent(evt, executionEntity, HistoryEventTypes.PROCESS_INSTANCE_START);
        evt.setStartActivityId(executionEntity.getActivityId());
        evt.setStartTime(ClockUtil.getCurrentTime());
        final ExecutionEntity superExecution = executionEntity.getSuperExecution();
        if (superExecution != null) {
            evt.setSuperProcessInstanceId(superExecution.getProcessInstanceId());
        }
        evt.setState("ACTIVE");
        evt.setStartUserId(Context.getCommandContext().getAuthenticatedUserId());
        if (this.isHistoryRemovalTimeStrategyStart()) {
            if (this.isRootProcessInstance(evt)) {
                final Date removalTime = this.calculateRemovalTime(evt);
                evt.setRemovalTime(removalTime);
            }
            else {
                this.provideRemovalTime(evt);
            }
        }
        return evt;
    }
    
    @Override
    public HistoryEvent createProcessInstanceUpdateEvt(final DelegateExecution execution) {
        final ExecutionEntity executionEntity = (ExecutionEntity)execution;
        final HistoricProcessInstanceEventEntity evt = this.loadProcessInstanceEventEntity(executionEntity);
        this.initProcessInstanceEvent(evt, executionEntity, HistoryEventTypes.PROCESS_INSTANCE_UPDATE);
        if (executionEntity.isSuspended()) {
            evt.setState("SUSPENDED");
        }
        else {
            evt.setState("ACTIVE");
        }
        return evt;
    }
    
    @Override
    public HistoryEvent createProcessInstanceMigrateEvt(final DelegateExecution execution) {
        final ExecutionEntity executionEntity = (ExecutionEntity)execution;
        final HistoricProcessInstanceEventEntity evt = this.newProcessInstanceEventEntity(executionEntity);
        this.initProcessInstanceEvent(evt, executionEntity, HistoryEventTypes.PROCESS_INSTANCE_MIGRATE);
        if (executionEntity.isSuspended()) {
            evt.setState("SUSPENDED");
        }
        else {
            evt.setState("ACTIVE");
        }
        return evt;
    }
    
    @Override
    public HistoryEvent createProcessInstanceEndEvt(final DelegateExecution execution) {
        final ExecutionEntity executionEntity = (ExecutionEntity)execution;
        final HistoricProcessInstanceEventEntity evt = this.loadProcessInstanceEventEntity(executionEntity);
        this.initProcessInstanceEvent(evt, executionEntity, HistoryEventTypes.PROCESS_INSTANCE_END);
        this.determineEndState(executionEntity, evt);
        evt.setEndActivityId(executionEntity.getActivityId());
        evt.setEndTime(ClockUtil.getCurrentTime());
        if (evt.getStartTime() != null) {
            evt.setDurationInMillis(evt.getEndTime().getTime() - evt.getStartTime().getTime());
        }
        if (this.isRootProcessInstance(evt) && this.isHistoryRemovalTimeStrategyEnd()) {
            final Date removalTime = this.calculateRemovalTime(evt);
            if (removalTime != null) {
                this.addRemovalTimeToHistoricProcessInstances(evt.getRootProcessInstanceId(), removalTime);
                if (this.isDmnEnabled()) {
                    this.addRemovalTimeToHistoricDecisions(evt.getRootProcessInstanceId(), removalTime);
                }
            }
        }
        if (executionEntity.getDeleteReason() != null) {
            evt.setDeleteReason(executionEntity.getDeleteReason());
        }
        return evt;
    }
    
    protected void addRemovalTimeToHistoricDecisions(final String rootProcessInstanceId, final Date removalTime) {
        Context.getCommandContext().getHistoricDecisionInstanceManager().addRemovalTimeToDecisionsByRootProcessInstanceId(rootProcessInstanceId, removalTime);
    }
    
    protected void addRemovalTimeToHistoricProcessInstances(final String rootProcessInstanceId, final Date removalTime) {
        Context.getCommandContext().getHistoricProcessInstanceManager().addRemovalTimeToProcessInstancesByRootProcessInstanceId(rootProcessInstanceId, removalTime);
    }
    
    protected boolean isDmnEnabled() {
        return Context.getCommandContext().getProcessEngineConfiguration().isDmnEnabled();
    }
    
    protected void determineEndState(final ExecutionEntity executionEntity, final HistoricProcessInstanceEventEntity evt) {
        if (executionEntity.getActivity() != null) {
            evt.setState("COMPLETED");
        }
        else if (executionEntity.isExternallyTerminated()) {
            evt.setState("EXTERNALLY_TERMINATED");
        }
        else if (!executionEntity.isExternallyTerminated()) {
            evt.setState("INTERNALLY_TERMINATED");
        }
    }
    
    @Override
    public HistoryEvent createActivityInstanceStartEvt(final DelegateExecution execution) {
        final ExecutionEntity executionEntity = (ExecutionEntity)execution;
        final HistoricActivityInstanceEventEntity evt = this.newActivityInstanceEventEntity(executionEntity);
        this.initActivityInstanceEvent(evt, executionEntity, HistoryEventTypes.ACTIVITY_INSTANCE_START);
        this.initSequenceCounter(executionEntity, evt);
        evt.setStartTime(ClockUtil.getCurrentTime());
        return evt;
    }
    
    @Override
    public HistoryEvent createActivityInstanceUpdateEvt(final DelegateExecution execution) {
        return this.createActivityInstanceUpdateEvt(execution, null);
    }
    
    @Override
    public HistoryEvent createActivityInstanceUpdateEvt(final DelegateExecution execution, final DelegateTask task) {
        final ExecutionEntity executionEntity = (ExecutionEntity)execution;
        final HistoricActivityInstanceEventEntity evt = this.loadActivityInstanceEventEntity(executionEntity);
        this.initActivityInstanceEvent(evt, executionEntity, HistoryEventTypes.ACTIVITY_INSTANCE_UPDATE);
        if (task != null) {
            evt.setTaskId(task.getId());
            evt.setTaskAssignee(task.getAssignee());
        }
        return evt;
    }
    
    @Override
    public HistoryEvent createActivityInstanceMigrateEvt(final MigratingActivityInstance actInstance) {
        final HistoricActivityInstanceEventEntity evt = this.loadActivityInstanceEventEntity(actInstance.resolveRepresentativeExecution());
        this.initActivityInstanceEvent(evt, actInstance, HistoryEventTypes.ACTIVITY_INSTANCE_MIGRATE);
        return evt;
    }
    
    @Override
    public HistoryEvent createActivityInstanceEndEvt(final DelegateExecution execution) {
        final ExecutionEntity executionEntity = (ExecutionEntity)execution;
        final HistoricActivityInstanceEventEntity evt = this.loadActivityInstanceEventEntity(executionEntity);
        evt.setActivityInstanceState(executionEntity.getActivityInstanceState());
        this.initActivityInstanceEvent(evt, (ExecutionEntity)execution, HistoryEventTypes.ACTIVITY_INSTANCE_END);
        evt.setEndTime(ClockUtil.getCurrentTime());
        if (evt.getStartTime() != null) {
            evt.setDurationInMillis(evt.getEndTime().getTime() - evt.getStartTime().getTime());
        }
        return evt;
    }
    
    @Override
    public HistoryEvent createTaskInstanceCreateEvt(final DelegateTask task) {
        final HistoricTaskInstanceEventEntity evt = this.newTaskInstanceEventEntity(task);
        this.initTaskInstanceEvent(evt, (TaskEntity)task, HistoryEventTypes.TASK_INSTANCE_CREATE);
        evt.setStartTime(ClockUtil.getCurrentTime());
        return evt;
    }
    
    @Override
    public HistoryEvent createTaskInstanceUpdateEvt(final DelegateTask task) {
        final HistoricTaskInstanceEventEntity evt = this.loadTaskInstanceEvent(task);
        this.initTaskInstanceEvent(evt, (TaskEntity)task, HistoryEventTypes.TASK_INSTANCE_UPDATE);
        return evt;
    }
    
    @Override
    public HistoryEvent createTaskInstanceMigrateEvt(final DelegateTask task) {
        final HistoricTaskInstanceEventEntity evt = this.loadTaskInstanceEvent(task);
        this.initTaskInstanceEvent(evt, (TaskEntity)task, HistoryEventTypes.TASK_INSTANCE_MIGRATE);
        return evt;
    }
    
    @Override
    public HistoryEvent createTaskInstanceCompleteEvt(final DelegateTask task, final String deleteReason) {
        final HistoricTaskInstanceEventEntity evt = this.loadTaskInstanceEvent(task);
        this.initTaskInstanceEvent(evt, (TaskEntity)task, HistoryEventTypes.TASK_INSTANCE_COMPLETE);
        evt.setEndTime(ClockUtil.getCurrentTime());
        if (evt.getStartTime() != null) {
            evt.setDurationInMillis(evt.getEndTime().getTime() - evt.getStartTime().getTime());
        }
        evt.setDeleteReason(deleteReason);
        return evt;
    }
    
    @Override
    public List<HistoryEvent> createUserOperationLogEvents(final UserOperationLogContext context) {
        final List<HistoryEvent> historyEvents = new ArrayList<HistoryEvent>();
        final String operationId = Context.getCommandContext().getOperationId();
        context.setOperationId(operationId);
        for (final UserOperationLogContextEntry entry : context.getEntries()) {
            for (final PropertyChange propertyChange : entry.getPropertyChanges()) {
                final UserOperationLogEntryEventEntity evt = new UserOperationLogEntryEventEntity();
                this.initUserOperationLogEvent(evt, context, entry, propertyChange);
                historyEvents.add(evt);
            }
        }
        return historyEvents;
    }
    
    @Override
    public HistoryEvent createHistoricVariableCreateEvt(final VariableInstanceEntity variableInstance, final VariableScope sourceVariableScope) {
        return this.createHistoricVariableEvent(variableInstance, sourceVariableScope, HistoryEventTypes.VARIABLE_INSTANCE_CREATE);
    }
    
    @Override
    public HistoryEvent createHistoricVariableDeleteEvt(final VariableInstanceEntity variableInstance, final VariableScope sourceVariableScope) {
        return this.createHistoricVariableEvent(variableInstance, sourceVariableScope, HistoryEventTypes.VARIABLE_INSTANCE_DELETE);
    }
    
    @Override
    public HistoryEvent createHistoricVariableUpdateEvt(final VariableInstanceEntity variableInstance, final VariableScope sourceVariableScope) {
        return this.createHistoricVariableEvent(variableInstance, sourceVariableScope, HistoryEventTypes.VARIABLE_INSTANCE_UPDATE);
    }
    
    @Override
    public HistoryEvent createHistoricVariableMigrateEvt(final VariableInstanceEntity variableInstance) {
        return this.createHistoricVariableEvent(variableInstance, null, HistoryEventTypes.VARIABLE_INSTANCE_MIGRATE);
    }
    
    @Override
    public HistoryEvent createFormPropertyUpdateEvt(final ExecutionEntity execution, final String propertyId, final String propertyValue, final String taskId) {
        final IdGenerator idGenerator = Context.getProcessEngineConfiguration().getIdGenerator();
        final HistoricFormPropertyEventEntity historicFormPropertyEntity = this.newHistoricFormPropertyEvent();
        historicFormPropertyEntity.setId(idGenerator.getNextId());
        historicFormPropertyEntity.setEventType(HistoryEventTypes.FORM_PROPERTY_UPDATE.getEventName());
        historicFormPropertyEntity.setTimestamp(ClockUtil.getCurrentTime());
        historicFormPropertyEntity.setExecutionId(execution.getId());
        historicFormPropertyEntity.setProcessDefinitionId(execution.getProcessDefinitionId());
        historicFormPropertyEntity.setProcessInstanceId(execution.getProcessInstanceId());
        historicFormPropertyEntity.setPropertyId(propertyId);
        historicFormPropertyEntity.setPropertyValue(propertyValue);
        historicFormPropertyEntity.setTaskId(taskId);
        historicFormPropertyEntity.setTenantId(execution.getTenantId());
        historicFormPropertyEntity.setUserOperationId(Context.getCommandContext().getOperationId());
        historicFormPropertyEntity.setRootProcessInstanceId(execution.getRootProcessInstanceId());
        if (this.isHistoryRemovalTimeStrategyStart()) {
            this.provideRemovalTime(historicFormPropertyEntity);
        }
        final ProcessDefinitionEntity definition = execution.getProcessDefinition();
        if (definition != null) {
            historicFormPropertyEntity.setProcessDefinitionKey(definition.getKey());
        }
        this.initSequenceCounter(execution, historicFormPropertyEntity);
        if (execution.isProcessInstanceStarting()) {
            historicFormPropertyEntity.setActivityInstanceId(execution.getProcessInstanceId());
        }
        else {
            historicFormPropertyEntity.setActivityInstanceId(execution.getActivityInstanceId());
        }
        return historicFormPropertyEntity;
    }
    
    @Override
    public HistoryEvent createHistoricIncidentCreateEvt(final Incident incident) {
        return this.createHistoricIncidentEvt(incident, HistoryEventTypes.INCIDENT_CREATE);
    }
    
    @Override
    public HistoryEvent createHistoricIncidentUpdateEvt(final Incident incident) {
        return this.createHistoricIncidentEvt(incident, HistoryEventTypes.INCIDENT_UPDATE);
    }
    
    @Override
    public HistoryEvent createHistoricIncidentResolveEvt(final Incident incident) {
        return this.createHistoricIncidentEvt(incident, HistoryEventTypes.INCIDENT_RESOLVE);
    }
    
    @Override
    public HistoryEvent createHistoricIncidentDeleteEvt(final Incident incident) {
        return this.createHistoricIncidentEvt(incident, HistoryEventTypes.INCIDENT_DELETE);
    }
    
    @Override
    public HistoryEvent createHistoricIncidentMigrateEvt(final Incident incident) {
        return this.createHistoricIncidentEvt(incident, HistoryEventTypes.INCIDENT_MIGRATE);
    }
    
    protected HistoryEvent createHistoricIncidentEvt(final Incident incident, final HistoryEventTypes eventType) {
        final HistoricIncidentEventEntity evt = this.loadIncidentEvent(incident);
        this.initHistoricIncidentEvent(evt, incident, eventType);
        if (HistoryEventTypes.INCIDENT_RESOLVE.equals(eventType) || HistoryEventTypes.INCIDENT_DELETE.equals(eventType)) {
            evt.setEndTime(ClockUtil.getCurrentTime());
        }
        return evt;
    }
    
    @Override
    public HistoryEvent createHistoricIdentityLinkAddEvent(final IdentityLink identityLink) {
        return this.createHistoricIdentityLinkEvt(identityLink, HistoryEventTypes.IDENTITY_LINK_ADD);
    }
    
    @Override
    public HistoryEvent createHistoricIdentityLinkDeleteEvent(final IdentityLink identityLink) {
        return this.createHistoricIdentityLinkEvt(identityLink, HistoryEventTypes.IDENTITY_LINK_DELETE);
    }
    
    protected HistoryEvent createHistoricIdentityLinkEvt(final IdentityLink identityLink, final HistoryEventTypes eventType) {
        final HistoricIdentityLinkLogEventEntity evt = this.newIdentityLinkEventEntity();
        this.initHistoricIdentityLinkEvent(evt, identityLink, eventType);
        return evt;
    }
    
    protected HistoricIdentityLinkLogEventEntity newIdentityLinkEventEntity() {
        return new HistoricIdentityLinkLogEventEntity();
    }
    
    protected void initHistoricIdentityLinkEvent(final HistoricIdentityLinkLogEventEntity evt, final IdentityLink identityLink, final HistoryEventType eventType) {
        if (identityLink.getTaskId() != null) {
            final TaskEntity task = Context.getCommandContext().getTaskManager().findTaskById(identityLink.getTaskId());
            evt.setProcessDefinitionId(task.getProcessDefinitionId());
            if (task.getProcessDefinition() != null) {
                evt.setProcessDefinitionKey(task.getProcessDefinition().getKey());
            }
            final ExecutionEntity execution = task.getExecution();
            if (execution != null) {
                evt.setRootProcessInstanceId(execution.getRootProcessInstanceId());
                if (this.isHistoryRemovalTimeStrategyStart()) {
                    this.provideRemovalTime(evt);
                }
            }
        }
        if (identityLink.getProcessDefId() != null) {
            evt.setProcessDefinitionId(identityLink.getProcessDefId());
            final ProcessDefinitionEntity definition = Context.getProcessEngineConfiguration().getDeploymentCache().findProcessDefinitionFromCache(identityLink.getProcessDefId());
            evt.setProcessDefinitionKey(definition.getKey());
        }
        evt.setTime(ClockUtil.getCurrentTime());
        evt.setType(identityLink.getType());
        evt.setUserId(identityLink.getUserId());
        evt.setGroupId(identityLink.getGroupId());
        evt.setTaskId(identityLink.getTaskId());
        evt.setTenantId(identityLink.getTenantId());
        String operationType = "add";
        if (eventType.getEventName().equals(HistoryEventTypes.IDENTITY_LINK_DELETE.getEventName())) {
            operationType = "delete";
        }
        evt.setOperationType(operationType);
        evt.setEventType(eventType.getEventName());
        evt.setAssignerId(Context.getCommandContext().getAuthenticatedUserId());
    }
    
    @Override
    public HistoryEvent createBatchStartEvent(final Batch batch) {
        final HistoryEvent historicBatch = this.createBatchEvent((BatchEntity)batch, HistoryEventTypes.BATCH_START);
        if (this.isHistoryRemovalTimeStrategyStart()) {
            this.provideRemovalTime((HistoricBatchEntity)historicBatch);
        }
        return historicBatch;
    }
    
    @Override
    public HistoryEvent createBatchEndEvent(final Batch batch) {
        final HistoryEvent historicBatch = this.createBatchEvent((BatchEntity)batch, HistoryEventTypes.BATCH_END);
        if (this.isHistoryRemovalTimeStrategyEnd()) {
            this.provideRemovalTime((HistoricBatchEntity)historicBatch);
            this.addRemovalTimeToHistoricJobLog((HistoricBatchEntity)historicBatch);
            this.addRemovalTimeToHistoricIncidents((HistoricBatchEntity)historicBatch);
        }
        return historicBatch;
    }
    
    protected HistoryEvent createBatchEvent(final BatchEntity batch, final HistoryEventTypes eventType) {
        final HistoricBatchEntity event = this.loadBatchEntity(batch);
        event.setId(batch.getId());
        event.setType(batch.getType());
        event.setTotalJobs(batch.getTotalJobs());
        event.setBatchJobsPerSeed(batch.getBatchJobsPerSeed());
        event.setInvocationsPerBatchJob(batch.getInvocationsPerBatchJob());
        event.setSeedJobDefinitionId(batch.getSeedJobDefinitionId());
        event.setMonitorJobDefinitionId(batch.getMonitorJobDefinitionId());
        event.setBatchJobDefinitionId(batch.getBatchJobDefinitionId());
        event.setTenantId(batch.getTenantId());
        event.setEventType(eventType.getEventName());
        if (HistoryEventTypes.BATCH_START.equals(eventType)) {
            event.setStartTime(ClockUtil.getCurrentTime());
            event.setCreateUserId(Context.getCommandContext().getAuthenticatedUserId());
        }
        if (HistoryEventTypes.BATCH_END.equals(eventType)) {
            event.setEndTime(ClockUtil.getCurrentTime());
        }
        return event;
    }
    
    @Override
    public HistoryEvent createHistoricJobLogCreateEvt(final Job job) {
        return this.createHistoricJobLogEvt(job, HistoryEventTypes.JOB_CREATE);
    }
    
    @Override
    public HistoryEvent createHistoricJobLogFailedEvt(final Job job, final Throwable exception) {
        final HistoricJobLogEventEntity event = (HistoricJobLogEventEntity)this.createHistoricJobLogEvt(job, HistoryEventTypes.JOB_FAIL);
        if (exception != null) {
            event.setJobExceptionMessage(exception.getMessage());
            final String exceptionStacktrace = ExceptionUtil.getExceptionStacktrace(exception);
            final byte[] exceptionBytes = StringUtil.toByteArray(exceptionStacktrace);
            final ByteArrayEntity byteArray = ExceptionUtil.createJobExceptionByteArray(exceptionBytes, ResourceTypes.HISTORY);
            byteArray.setRootProcessInstanceId(event.getRootProcessInstanceId());
            if (this.isHistoryRemovalTimeStrategyStart()) {
                byteArray.setRemovalTime(event.getRemovalTime());
            }
            event.setExceptionByteArrayId(byteArray.getId());
        }
        return event;
    }
    
    @Override
    public HistoryEvent createHistoricJobLogSuccessfulEvt(final Job job) {
        return this.createHistoricJobLogEvt(job, HistoryEventTypes.JOB_SUCCESS);
    }
    
    @Override
    public HistoryEvent createHistoricJobLogDeleteEvt(final Job job) {
        return this.createHistoricJobLogEvt(job, HistoryEventTypes.JOB_DELETE);
    }
    
    protected HistoryEvent createHistoricJobLogEvt(final Job job, final HistoryEventType eventType) {
        final HistoricJobLogEventEntity event = this.newHistoricJobLogEntity(job);
        this.initHistoricJobLogEvent(event, job, eventType);
        return event;
    }
    
    protected void initHistoricJobLogEvent(final HistoricJobLogEventEntity evt, final Job job, final HistoryEventType eventType) {
        final Date currentTime = ClockUtil.getCurrentTime();
        evt.setTimestamp(currentTime);
        final JobEntity jobEntity = (JobEntity)job;
        evt.setJobId(jobEntity.getId());
        evt.setJobDueDate(jobEntity.getDuedate());
        evt.setJobRetries(jobEntity.getRetries());
        evt.setJobPriority(jobEntity.getPriority());
        final String hostName = Context.getCommandContext().getProcessEngineConfiguration().getHostname();
        evt.setHostname(hostName);
        if ("history-cleanup".equals(jobEntity.getJobHandlerType())) {
            final String timeToLive = Context.getProcessEngineConfiguration().getHistoryCleanupJobLogTimeToLive();
            if (timeToLive != null) {
                try {
                    final Integer timeToLiveDays = ParseUtil.parseHistoryTimeToLive(timeToLive);
                    final Date removalTime = DefaultHistoryRemovalTimeProvider.determineRemovalTime(currentTime, timeToLiveDays);
                    evt.setRemovalTime(removalTime);
                }
                catch (ProcessEngineException e) {
                    final ProcessEngineException wrappedException = DefaultHistoryEventProducer.LOG.invalidPropertyValue("historyCleanupJobLogTimeToLive", timeToLive, e);
                    DefaultHistoryEventProducer.LOG.invalidPropertyValue(wrappedException);
                }
            }
        }
        final JobDefinition jobDefinition = jobEntity.getJobDefinition();
        if (jobDefinition != null) {
            evt.setJobDefinitionId(jobDefinition.getId());
            evt.setJobDefinitionType(jobDefinition.getJobType());
            evt.setJobDefinitionConfiguration(jobDefinition.getJobConfiguration());
            final String historicBatchId = jobDefinition.getJobConfiguration();
            if (historicBatchId != null && this.isHistoryRemovalTimeStrategyStart()) {
                final HistoricBatchEntity historicBatch = this.getHistoricBatchById(historicBatchId);
                if (historicBatch != null) {
                    evt.setRemovalTime(historicBatch.getRemovalTime());
                }
            }
        }
        else {
            evt.setJobDefinitionType(jobEntity.getJobHandlerType());
        }
        evt.setActivityId(jobEntity.getActivityId());
        evt.setFailedActivityId(jobEntity.getFailedActivityId());
        evt.setExecutionId(jobEntity.getExecutionId());
        evt.setProcessInstanceId(jobEntity.getProcessInstanceId());
        evt.setProcessDefinitionId(jobEntity.getProcessDefinitionId());
        evt.setProcessDefinitionKey(jobEntity.getProcessDefinitionKey());
        evt.setDeploymentId(jobEntity.getDeploymentId());
        evt.setTenantId(jobEntity.getTenantId());
        final ExecutionEntity execution = jobEntity.getExecution();
        if (execution != null) {
            evt.setRootProcessInstanceId(execution.getRootProcessInstanceId());
            if (this.isHistoryRemovalTimeStrategyStart()) {
                this.provideRemovalTime(evt);
            }
        }
        this.initSequenceCounter(jobEntity, evt);
        JobState state = null;
        if (HistoryEventTypes.JOB_CREATE.equals(eventType)) {
            state = JobState.CREATED;
        }
        else if (HistoryEventTypes.JOB_FAIL.equals(eventType)) {
            state = JobState.FAILED;
        }
        else if (HistoryEventTypes.JOB_SUCCESS.equals(eventType)) {
            state = JobState.SUCCESSFUL;
        }
        else if (HistoryEventTypes.JOB_DELETE.equals(eventType)) {
            state = JobState.DELETED;
        }
        evt.setState(state.getStateCode());
    }
    
    @Override
    public HistoryEvent createHistoricExternalTaskLogCreatedEvt(final ExternalTask task) {
        return this.initHistoricExternalTaskLog((ExternalTaskEntity)task, ExternalTaskState.CREATED);
    }
    
    @Override
    public HistoryEvent createHistoricExternalTaskLogFailedEvt(final ExternalTask task) {
        final HistoricExternalTaskLogEntity event = this.initHistoricExternalTaskLog((ExternalTaskEntity)task, ExternalTaskState.FAILED);
        event.setErrorMessage(task.getErrorMessage());
        final String errorDetails = ((ExternalTaskEntity)task).getErrorDetails();
        if (errorDetails != null) {
            event.setErrorDetails(errorDetails);
        }
        return event;
    }
    
    @Override
    public HistoryEvent createHistoricExternalTaskLogSuccessfulEvt(final ExternalTask task) {
        return this.initHistoricExternalTaskLog((ExternalTaskEntity)task, ExternalTaskState.SUCCESSFUL);
    }
    
    @Override
    public HistoryEvent createHistoricExternalTaskLogDeletedEvt(final ExternalTask task) {
        return this.initHistoricExternalTaskLog((ExternalTaskEntity)task, ExternalTaskState.DELETED);
    }
    
    protected HistoricExternalTaskLogEntity initHistoricExternalTaskLog(final ExternalTaskEntity entity, final ExternalTaskState state) {
        final HistoricExternalTaskLogEntity event = new HistoricExternalTaskLogEntity();
        event.setTimestamp(ClockUtil.getCurrentTime());
        event.setExternalTaskId(entity.getId());
        event.setTopicName(entity.getTopicName());
        event.setWorkerId(entity.getWorkerId());
        event.setPriority(entity.getPriority());
        event.setRetries(entity.getRetries());
        event.setActivityId(entity.getActivityId());
        event.setActivityInstanceId(entity.getActivityInstanceId());
        event.setExecutionId(entity.getExecutionId());
        event.setProcessInstanceId(entity.getProcessInstanceId());
        event.setProcessDefinitionId(entity.getProcessDefinitionId());
        event.setProcessDefinitionKey(entity.getProcessDefinitionKey());
        event.setTenantId(entity.getTenantId());
        event.setState(state.getStateCode());
        final ExecutionEntity execution = entity.getExecution();
        if (execution != null) {
            event.setRootProcessInstanceId(execution.getRootProcessInstanceId());
            if (this.isHistoryRemovalTimeStrategyStart()) {
                this.provideRemovalTime(event);
            }
        }
        return event;
    }
    
    protected boolean isRootProcessInstance(final HistoricProcessInstanceEventEntity evt) {
        return evt.getProcessInstanceId().equals(evt.getRootProcessInstanceId());
    }
    
    protected boolean isHistoryRemovalTimeStrategyStart() {
        return "start".equals(this.getHistoryRemovalTimeStrategy());
    }
    
    protected boolean isHistoryRemovalTimeStrategyEnd() {
        return "end".equals(this.getHistoryRemovalTimeStrategy());
    }
    
    protected String getHistoryRemovalTimeStrategy() {
        return Context.getProcessEngineConfiguration().getHistoryRemovalTimeStrategy();
    }
    
    protected Date calculateRemovalTime(final HistoryEvent historyEvent) {
        final String processDefinitionId = historyEvent.getProcessDefinitionId();
        final ProcessDefinition processDefinition = this.findProcessDefinitionById(processDefinitionId);
        return Context.getProcessEngineConfiguration().getHistoryRemovalTimeProvider().calculateRemovalTime((HistoricProcessInstanceEventEntity)historyEvent, processDefinition);
    }
    
    protected Date calculateRemovalTime(final HistoricBatchEntity historicBatch) {
        return Context.getProcessEngineConfiguration().getHistoryRemovalTimeProvider().calculateRemovalTime(historicBatch);
    }
    
    protected void provideRemovalTime(final HistoricBatchEntity historicBatch) {
        final Date removalTime = this.calculateRemovalTime(historicBatch);
        if (removalTime != null) {
            historicBatch.setRemovalTime(removalTime);
        }
    }
    
    protected void provideRemovalTime(final HistoryEvent historyEvent) {
        final String rootProcessInstanceId = historyEvent.getRootProcessInstanceId();
        if (rootProcessInstanceId != null) {
            final HistoricProcessInstanceEventEntity historicRootProcessInstance = this.getHistoricRootProcessInstance(rootProcessInstanceId);
            if (historicRootProcessInstance != null) {
                final Date removalTime = historicRootProcessInstance.getRemovalTime();
                historyEvent.setRemovalTime(removalTime);
            }
        }
    }
    
    protected HistoricProcessInstanceEventEntity getHistoricRootProcessInstance(final String rootProcessInstanceId) {
        return Context.getCommandContext().getDbEntityManager().selectById(HistoricProcessInstanceEventEntity.class, rootProcessInstanceId);
    }
    
    protected ProcessDefinition findProcessDefinitionById(final String processDefinitionId) {
        return Context.getCommandContext().getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(processDefinitionId);
    }
    
    protected HistoricBatchEntity getHistoricBatchById(final String batchId) {
        return Context.getCommandContext().getHistoricBatchManager().findHistoricBatchById(batchId);
    }
    
    protected HistoricBatchEntity getHistoricBatchByJobId(final String jobId) {
        return Context.getCommandContext().getHistoricBatchManager().findHistoricBatchByJobId(jobId);
    }
    
    protected void addRemovalTimeToHistoricJobLog(final HistoricBatchEntity historicBatch) {
        final Date removalTime = historicBatch.getRemovalTime();
        if (removalTime != null) {
            Context.getCommandContext().getHistoricJobLogManager().addRemovalTimeToJobLogByBatchId(historicBatch.getId(), removalTime);
        }
    }
    
    protected void addRemovalTimeToHistoricIncidents(final HistoricBatchEntity historicBatch) {
        final Date removalTime = historicBatch.getRemovalTime();
        if (removalTime != null) {
            Context.getCommandContext().getHistoricIncidentManager().addRemovalTimeToHistoricIncidentsByBatchId(historicBatch.getId(), removalTime);
        }
    }
    
    protected void initSequenceCounter(final ExecutionEntity execution, final HistoryEvent event) {
        this.initSequenceCounter(execution.getSequenceCounter(), event);
    }
    
    protected void initSequenceCounter(final VariableInstanceEntity variable, final HistoryEvent event) {
        this.initSequenceCounter(variable.getSequenceCounter(), event);
    }
    
    protected void initSequenceCounter(final JobEntity job, final HistoryEvent event) {
        this.initSequenceCounter(job.getSequenceCounter(), event);
    }
    
    protected void initSequenceCounter(final long sequenceCounter, final HistoryEvent event) {
        event.setSequenceCounter(sequenceCounter);
    }
    
    static {
        LOG = ProcessEngineLogger.CONFIG_LOGGER;
    }
}
