// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.authorization.Authorization;
import org.zik.bpm.engine.impl.util.PermissionConverter;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import java.util.Collections;
import java.util.Arrays;
import org.zik.bpm.engine.history.HistoricTaskInstance;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.oplog.UserOperationLogContextEntryBuilder;
import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.oplog.UserOperationLogContext;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Map;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.UserOperationLogQueryImpl;
import org.zik.bpm.engine.impl.history.event.UserOperationLogEntryEventEntity;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class UserOperationLogManager extends AbstractHistoricManager
{
    public UserOperationLogEntry findOperationLogById(final String entryId) {
        return this.getDbEntityManager().selectById(UserOperationLogEntryEventEntity.class, entryId);
    }
    
    public long findOperationLogEntryCountByQueryCriteria(final UserOperationLogQueryImpl query) {
        this.getAuthorizationManager().configureUserOperationLogQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectUserOperationLogEntryCountByQueryCriteria", query);
    }
    
    public List<UserOperationLogEntry> findOperationLogEntriesByQueryCriteria(final UserOperationLogQueryImpl query, final Page page) {
        this.getAuthorizationManager().configureUserOperationLogQuery(query);
        return (List<UserOperationLogEntry>)this.getDbEntityManager().selectList("selectUserOperationLogEntriesByQueryCriteria", query, page);
    }
    
    public void addRemovalTimeToUserOperationLogByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(UserOperationLogEntryEventEntity.class, "updateUserOperationLogByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToUserOperationLogByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(UserOperationLogEntryEventEntity.class, "updateUserOperationLogByProcessInstanceId", parameters);
    }
    
    public void updateOperationLogAnnotationByOperationId(final String operationId, final String annotation) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("operationId", operationId);
        parameters.put("annotation", annotation);
        this.getDbEntityManager().updatePreserveOrder(UserOperationLogEntryEventEntity.class, "updateOperationLogAnnotationByOperationId", parameters);
    }
    
    public void deleteOperationLogEntryById(final String entryId) {
        if (this.isHistoryEventProduced()) {
            this.getDbEntityManager().delete(UserOperationLogEntryEventEntity.class, "deleteUserOperationLogEntryById", entryId);
        }
    }
    
    public DbOperation deleteOperationLogByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(UserOperationLogEntryEventEntity.class, "deleteUserOperationLogByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
    
    public void logUserOperations(final UserOperationLogContext context) {
        if (this.isUserOperationLogEnabled()) {
            this.fireUserOperationLog(context);
        }
    }
    
    public void logUserOperation(final IdentityOperationResult operationResult, final String userId) {
        this.logUserOperation(this.getOperationType(operationResult), userId);
    }
    
    public void logUserOperation(final String operation, final String userId) {
        if (operation != null && this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "User").category("Admin").propertyChanges(new PropertyChange("userId", null, userId));
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logGroupOperation(final IdentityOperationResult operationResult, final String groupId) {
        this.logGroupOperation(this.getOperationType(operationResult), groupId);
    }
    
    public void logGroupOperation(final String operation, final String groupId) {
        if (operation != null && this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Group").category("Admin").propertyChanges(new PropertyChange("groupId", null, groupId));
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logTenantOperation(final IdentityOperationResult operationResult, final String tenantId) {
        this.logTenantOperation(this.getOperationType(operationResult), tenantId);
    }
    
    public void logTenantOperation(final String operation, final String tenantId) {
        if (operation != null && this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Tenant").category("Admin").propertyChanges(new PropertyChange("tenantId", null, tenantId));
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logMembershipOperation(final IdentityOperationResult operationResult, final String userId, final String groupId, final String tenantId) {
        this.logMembershipOperation(this.getOperationType(operationResult), userId, groupId, tenantId);
    }
    
    public void logMembershipOperation(final String operation, final String userId, final String groupId, final String tenantId) {
        if (operation != null && this.isUserOperationLogEnabled()) {
            final String entityType = (tenantId == null) ? "Group membership" : "TenantMembership";
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, entityType).category("Admin");
            final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
            if (userId != null) {
                propertyChanges.add(new PropertyChange("userId", null, userId));
            }
            if (groupId != null) {
                propertyChanges.add(new PropertyChange("groupId", null, groupId));
            }
            if (tenantId != null) {
                propertyChanges.add(new PropertyChange("tenantId", null, tenantId));
            }
            entryBuilder.propertyChanges(propertyChanges);
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logTaskOperations(final String operation, final TaskEntity task, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Task").category("TaskWorker").inContextOf(task, propertyChanges);
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logTaskOperations(final String operation, final HistoricTaskInstance historicTask, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Task").inContextOf(historicTask, propertyChanges).category("Operator");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logLinkOperation(final String operation, final TaskEntity task, final PropertyChange propertyChange) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "IdentityLink").category("TaskWorker").inContextOf(task, Arrays.asList(propertyChange));
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logProcessInstanceOperation(final String operation, final List<PropertyChange> propertyChanges) {
        this.logProcessInstanceOperation(operation, null, null, null, propertyChanges);
    }
    
    public void logProcessInstanceOperation(final String operation, final String processInstanceId, final String processDefinitionId, final String processDefinitionKey, final List<PropertyChange> propertyChanges) {
        this.logProcessInstanceOperation(operation, processInstanceId, processDefinitionId, processDefinitionKey, propertyChanges, null);
    }
    
    public void logProcessInstanceOperation(final String operation, final String processInstanceId, final String processDefinitionId, final String processDefinitionKey, final List<PropertyChange> propertyChanges, final String annotation) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "ProcessInstance").propertyChanges(propertyChanges).processInstanceId(processInstanceId).processDefinitionId(processDefinitionId).processDefinitionKey(processDefinitionKey).category("Operator");
            if (annotation != null) {
                entryBuilder.annotation(annotation);
            }
            if (processInstanceId != null) {
                final ExecutionEntity instance = this.getProcessInstanceManager().findExecutionById(processInstanceId);
                if (instance != null) {
                    entryBuilder.inContextOf(instance);
                }
            }
            else if (processDefinitionId != null) {
                final ProcessDefinitionEntity definition = this.getProcessDefinitionManager().findLatestProcessDefinitionById(processDefinitionId);
                if (definition != null) {
                    entryBuilder.inContextOf(definition);
                }
            }
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logProcessDefinitionOperation(final String operation, final String processDefinitionId, final String processDefinitionKey, final PropertyChange propertyChange) {
        this.logProcessDefinitionOperation(operation, processDefinitionId, processDefinitionKey, Collections.singletonList(propertyChange));
    }
    
    public void logProcessDefinitionOperation(final String operation, final String processDefinitionId, final String processDefinitionKey, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "ProcessDefinition").propertyChanges(propertyChanges).processDefinitionId(processDefinitionId).processDefinitionKey(processDefinitionKey).category("Operator");
            if (processDefinitionId != null) {
                final ProcessDefinitionEntity definition = this.getProcessDefinitionManager().findLatestProcessDefinitionById(processDefinitionId);
                entryBuilder.inContextOf(definition);
            }
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logCaseInstanceOperation(final String operation, final String caseInstanceId, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "CaseInstance").caseInstanceId(caseInstanceId).propertyChanges(propertyChanges).category("Operator");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logCaseDefinitionOperation(final String operation, final String caseDefinitionId, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "CaseDefinition").propertyChanges(propertyChanges).caseDefinitionId(caseDefinitionId).category("Operator");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logDecisionDefinitionOperation(final String operation, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "DecisionDefinition").propertyChanges(propertyChanges).category("Operator");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logJobOperation(final String operation, final String jobId, final String jobDefinitionId, final String processInstanceId, final String processDefinitionId, final String processDefinitionKey, final PropertyChange propertyChange) {
        this.logJobOperation(operation, jobId, jobDefinitionId, processInstanceId, processDefinitionId, processDefinitionKey, Collections.singletonList(propertyChange));
    }
    
    public void logJobOperation(final String operation, final String jobId, final String jobDefinitionId, final String processInstanceId, final String processDefinitionId, final String processDefinitionKey, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Job").jobId(jobId).jobDefinitionId(jobDefinitionId).processDefinitionId(processDefinitionId).processDefinitionKey(processDefinitionKey).propertyChanges(propertyChanges).category("Operator");
            if (jobId != null) {
                final JobEntity job = this.getJobManager().findJobById(jobId);
                if (job != null) {
                    entryBuilder.inContextOf(job);
                }
            }
            else if (jobDefinitionId != null) {
                final JobDefinitionEntity jobDefinition = this.getJobDefinitionManager().findById(jobDefinitionId);
                if (jobDefinition != null) {
                    entryBuilder.inContextOf(jobDefinition);
                }
            }
            else if (processInstanceId != null) {
                final ExecutionEntity processInstance = this.getProcessInstanceManager().findExecutionById(processInstanceId);
                if (processInstance != null) {
                    entryBuilder.inContextOf(processInstance);
                }
            }
            else if (processDefinitionId != null) {
                final ProcessDefinitionEntity definition = this.getProcessDefinitionManager().findLatestProcessDefinitionById(processDefinitionId);
                if (definition != null) {
                    entryBuilder.inContextOf(definition);
                }
            }
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logJobDefinitionOperation(final String operation, final String jobDefinitionId, final String processDefinitionId, final String processDefinitionKey, final PropertyChange propertyChange) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "JobDefinition").jobDefinitionId(jobDefinitionId).processDefinitionId(processDefinitionId).processDefinitionKey(processDefinitionKey).propertyChanges(propertyChange).category("Operator");
            if (jobDefinitionId != null) {
                final JobDefinitionEntity jobDefinition = this.getJobDefinitionManager().findById(jobDefinitionId);
                if (jobDefinition != null) {
                    entryBuilder.inContextOf(jobDefinition);
                }
            }
            else if (processDefinitionId != null) {
                final ProcessDefinitionEntity definition = this.getProcessDefinitionManager().findLatestProcessDefinitionById(processDefinitionId);
                if (definition != null) {
                    entryBuilder.inContextOf(definition);
                }
            }
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logAttachmentOperation(final String operation, final TaskEntity task, final PropertyChange propertyChange) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Attachment").category("TaskWorker").inContextOf(task, Arrays.asList(propertyChange));
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logAttachmentOperation(final String operation, final ExecutionEntity processInstance, final PropertyChange propertyChange) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Attachment").category("TaskWorker").inContextOf(processInstance, Arrays.asList(propertyChange));
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logVariableOperation(final String operation, final String executionId, final String taskId, final PropertyChange propertyChange) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Variable").propertyChanges(propertyChange);
            if (executionId != null) {
                final ExecutionEntity execution = this.getProcessInstanceManager().findExecutionById(executionId);
                entryBuilder.inContextOf(execution).category("Operator");
            }
            else if (taskId != null) {
                final TaskEntity task = this.getTaskManager().findTaskById(taskId);
                entryBuilder.inContextOf(task, Arrays.asList(propertyChange)).category("TaskWorker");
            }
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logHistoricVariableOperation(final String operation, final HistoricProcessInstanceEntity historicProcessInstance, final ResourceDefinitionEntity<?> definition, final PropertyChange propertyChange) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Variable").category("Operator").propertyChanges(propertyChange).inContextOf(historicProcessInstance, definition, Arrays.asList(propertyChange));
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logHistoricVariableOperation(final String operation, final HistoricVariableInstanceEntity historicVariableInstance, final ResourceDefinitionEntity<?> definition, final PropertyChange propertyChange) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Variable").category("Operator").propertyChanges(propertyChange).inContextOf(historicVariableInstance, definition, Arrays.asList(propertyChange));
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logDeploymentOperation(final String operation, final String deploymentId, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Deployment").deploymentId(deploymentId).propertyChanges(propertyChanges).category("Operator");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logBatchOperation(final String operation, final List<PropertyChange> propertyChange) {
        this.logBatchOperation(operation, null, propertyChange);
    }
    
    public void logBatchOperation(final String operation, final String batchId, final PropertyChange propertyChange) {
        this.logBatchOperation(operation, batchId, Collections.singletonList(propertyChange));
    }
    
    public void logBatchOperation(final String operation, final String batchId, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Batch").batchId(batchId).propertyChanges(propertyChanges).category("Operator");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logDecisionInstanceOperation(final String operation, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "DecisionInstance").propertyChanges(propertyChanges).category("Operator");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logExternalTaskOperation(final String operation, final ExternalTaskEntity externalTask, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "ExternalTask").propertyChanges(propertyChanges).category("Operator");
            if (externalTask != null) {
                ExecutionEntity instance = null;
                ProcessDefinitionEntity definition = null;
                if (externalTask.getProcessInstanceId() != null) {
                    instance = this.getProcessInstanceManager().findExecutionById(externalTask.getProcessInstanceId());
                }
                else if (externalTask.getProcessDefinitionId() != null) {
                    definition = this.getProcessDefinitionManager().findLatestProcessDefinitionById(externalTask.getProcessDefinitionId());
                }
                entryBuilder.processInstanceId(externalTask.getProcessInstanceId()).processDefinitionId(externalTask.getProcessDefinitionId()).processDefinitionKey(externalTask.getProcessDefinitionKey()).inContextOf(externalTask, instance, definition);
            }
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logMetricsOperation(final String operation, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Metrics").propertyChanges(propertyChanges).category("Operator");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logTaskMetricsOperation(final String operation, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "TaskMetrics").propertyChanges(propertyChanges).category("Operator");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logFilterOperation(final String operation, final String filterId) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Filter").propertyChanges(new PropertyChange("filterId", null, filterId)).category("TaskWorker");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logPropertyOperation(final String operation, final List<PropertyChange> propertyChanges) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Property").propertyChanges(propertyChanges).category("Admin");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logSetAnnotationOperation(final String operationId) {
        this.logAnnotationOperation(operationId, "OperationLog", "operationId", "SetAnnotation");
    }
    
    public void logClearAnnotationOperation(final String operationId) {
        this.logAnnotationOperation(operationId, "OperationLog", "operationId", "ClearAnnotation");
    }
    
    public void logSetIncidentAnnotationOperation(final String incidentId) {
        this.logAnnotationOperation(incidentId, "Incident", "incidentId", "SetAnnotation");
    }
    
    public void logClearIncidentAnnotationOperation(final String incidentId) {
        this.logAnnotationOperation(incidentId, "Incident", "incidentId", "ClearAnnotation");
    }
    
    protected void logAnnotationOperation(final String id, final String type, final String idProperty, final String operationType) {
        if (this.isUserOperationLogEnabled()) {
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operationType, type).propertyChanges(new PropertyChange(idProperty, null, id)).category("Operator");
            final UserOperationLogContext context = new UserOperationLogContext();
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    public void logAuthorizationOperation(final String operation, final AuthorizationEntity authorization, final AuthorizationEntity previousValues) {
        if (this.isUserOperationLogEnabled()) {
            final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
            propertyChanges.add(new PropertyChange("permissionBits", (previousValues == null) ? null : Integer.valueOf(previousValues.getPermissions()), authorization.getPermissions()));
            propertyChanges.add(new PropertyChange("permissions", (previousValues == null) ? null : this.getPermissionStringList(previousValues), this.getPermissionStringList(authorization)));
            propertyChanges.add(new PropertyChange("type", (previousValues == null) ? null : Integer.valueOf(previousValues.getAuthorizationType()), authorization.getAuthorizationType()));
            propertyChanges.add(new PropertyChange("resource", (previousValues == null) ? null : this.getResourceName(previousValues.getResourceType()), this.getResourceName(authorization.getResourceType())));
            propertyChanges.add(new PropertyChange("resourceId", (previousValues == null) ? null : previousValues.getResourceId(), authorization.getResourceId()));
            if (authorization.getUserId() != null || (previousValues != null && previousValues.getUserId() != null)) {
                propertyChanges.add(new PropertyChange("userId", (previousValues == null) ? null : previousValues.getUserId(), authorization.getUserId()));
            }
            if (authorization.getGroupId() != null || (previousValues != null && previousValues.getGroupId() != null)) {
                propertyChanges.add(new PropertyChange("groupId", (previousValues == null) ? null : previousValues.getGroupId(), authorization.getGroupId()));
            }
            final UserOperationLogContext context = new UserOperationLogContext();
            final UserOperationLogContextEntryBuilder entryBuilder = UserOperationLogContextEntryBuilder.entry(operation, "Authorization").propertyChanges(propertyChanges).category("Admin");
            context.addEntry(entryBuilder.create());
            this.fireUserOperationLog(context);
        }
    }
    
    protected String getPermissionStringList(final AuthorizationEntity authorization) {
        final Permission[] permissionsForResource = Context.getProcessEngineConfiguration().getPermissionProvider().getPermissionsForResource(authorization.getResourceType());
        final Permission[] permissions = authorization.getPermissions(permissionsForResource);
        final String[] namesForPermissions = PermissionConverter.getNamesForPermissions(authorization, permissions);
        if (namesForPermissions.length == 0) {
            return Permissions.NONE.getName();
        }
        return StringUtil.trimToMaximumLengthAllowed(StringUtil.join(Arrays.asList(namesForPermissions).iterator()));
    }
    
    protected String getResourceName(final int resourceType) {
        return Context.getProcessEngineConfiguration().getPermissionProvider().getNameForResource(resourceType);
    }
    
    public boolean isUserOperationLogEnabled() {
        return this.isHistoryEventProduced() && ((this.isUserOperationLogEnabledOnCommandContext() && this.isUserAuthenticated()) || !this.writeUserOperationLogOnlyWithLoggedInUser());
    }
    
    protected boolean isHistoryEventProduced() {
        final HistoryLevel historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        return historyLevel.isHistoryEventProduced(HistoryEventTypes.USER_OPERATION_LOG, null);
    }
    
    protected boolean isUserAuthenticated() {
        final String userId = this.getAuthenticatedUserId();
        return userId != null && !userId.isEmpty();
    }
    
    protected String getAuthenticatedUserId() {
        final CommandContext commandContext = Context.getCommandContext();
        return commandContext.getAuthenticatedUserId();
    }
    
    protected void fireUserOperationLog(final UserOperationLogContext context) {
        if (context.getUserId() == null) {
            context.setUserId(this.getAuthenticatedUserId());
        }
        HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
            @Override
            public List<HistoryEvent> createHistoryEvents(final HistoryEventProducer producer) {
                return producer.createUserOperationLogEvents(context);
            }
        });
    }
    
    protected boolean writeUserOperationLogOnlyWithLoggedInUser() {
        return Context.getCommandContext().isRestrictUserOperationLogToAuthenticatedUsers();
    }
    
    protected boolean isUserOperationLogEnabledOnCommandContext() {
        return Context.getCommandContext().isUserOperationLogEnabled();
    }
    
    protected String getOperationType(final IdentityOperationResult operationResult) {
        final String operation = operationResult.getOperation();
        switch (operation) {
            case "create": {
                return "Create";
            }
            case "update": {
                return "Update";
            }
            case "delete": {
                return "Delete";
            }
            case "unlock": {
                return "Unlock";
            }
            default: {
                return null;
            }
        }
    }
}
