// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.ProcessEngineServices;
import org.zik.bpm.engine.delegate.BpmnError;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.delegate.TaskListener;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.exception.NullValueException;
import org.zik.bpm.engine.form.CamundaFormRef;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnExceptionHandler;
import org.zik.bpm.engine.impl.bpmn.helper.ErrorPropagationException;
import org.zik.bpm.engine.impl.bpmn.helper.EscalationHandler;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceFactory;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceLifecycleListener;
import org.zik.bpm.engine.impl.core.variable.scope.VariableStore;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.impl.form.CamundaFormRefImpl;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandContextListener;
import org.zik.bpm.engine.impl.interceptor.CommandInvocationContext;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import org.zik.bpm.engine.impl.task.delegate.TaskListenerInvocation;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.task.DelegationState;
import org.zik.bpm.engine.task.IdentityLink;
import org.zik.bpm.engine.task.Task;

import java.io.Serializable;
import java.util.*;


public class TaskEntity extends AbstractVariableScope<VariableInstanceEntity> implements Task, DelegateTask, Serializable, DbEntity, HasDbRevision, HasDbReferences, CommandContextListener, VariableStore.VariablesProvider<VariableInstanceEntity> {
    protected static final List<VariableInstanceLifecycleListener<VariableInstanceEntity>> DEFAULT_VARIABLE_LIFECYCLE_LISTENERS;
    protected static final EnginePersistenceLogger LOG;
    public static final String DELETE_REASON_COMPLETED = "completed";
    public static final String DELETE_REASON_DELETED = "deleted";
    private static final long serialVersionUID = 1L;
    protected String id;
    protected int revision;
    protected String owner;
    protected String assignee;
    protected DelegationState delegationState;
    protected String parentTaskId;
    protected transient TaskEntity parentTask;
    protected String name;
    protected String description;
    protected int priority;
    protected Date createTime;
    protected Date dueDate;
    protected Date followUpDate;
    protected int suspensionState;
    protected TaskState lifecycleState;
    protected String tenantId;
    protected boolean isIdentityLinksInitialized;
    protected transient List<IdentityLinkEntity> taskIdentityLinkEntities;
    protected String executionId;
    protected transient ExecutionEntity execution;
    protected String processInstanceId;
    protected transient ExecutionEntity processInstance;
    protected String processDefinitionId;
    protected String caseExecutionId;
    protected transient CaseExecutionEntity caseExecution;
    protected String caseInstanceId;
    protected String caseDefinitionId;
    protected transient TaskDefinition taskDefinition;
    protected String taskDefinitionKey;
    protected boolean isDeleted;
    protected String deleteReason;
    protected String eventName;
    protected boolean isFormKeyInitialized;
    protected String formKey;
    protected CamundaFormRef camundaFormRef;
    protected transient VariableStore<VariableInstanceEntity> variableStore;
    protected transient boolean skipCustomListeners;
    protected transient Map<String, PropertyChange> propertyChanges;
    protected transient List<PropertyChange> identityLinkChanges;
    protected List<VariableInstanceLifecycleListener<VariableInstanceEntity>> customLifecycleListeners;
    public static final String ASSIGNEE = "assignee";
    public static final String DELEGATION = "delegation";
    public static final String DELETE = "delete";
    public static final String DESCRIPTION = "description";
    public static final String DUE_DATE = "dueDate";
    public static final String FOLLOW_UP_DATE = "followUpDate";
    public static final String NAME = "name";
    public static final String OWNER = "owner";
    public static final String PARENT_TASK = "parentTask";
    public static final String PRIORITY = "priority";
    public static final String CASE_INSTANCE_ID = "caseInstanceId";

    public TaskEntity() {
        this.priority = 50;
        this.suspensionState = SuspensionState.ACTIVE.getStateCode();
        this.lifecycleState = TaskEntity.TaskState.STATE_INIT;
        this.isIdentityLinksInitialized = false;
        this.taskIdentityLinkEntities = new ArrayList();
        this.isFormKeyInitialized = false;
        this.variableStore = new VariableStore(this, new VariableStore.VariableStoreObserver[]{new TaskEntityReferencer(this)});
        this.skipCustomListeners = false;
        this.propertyChanges = new HashMap();
        this.identityLinkChanges = new ArrayList();
        this.lifecycleState = TaskEntity.TaskState.STATE_CREATED;
    }

    public TaskEntity(String id) {
        this(TaskEntity.TaskState.STATE_INIT);
        this.id = id;
    }

    protected TaskEntity(TaskState initialState) {
        this.priority = 50;
        this.suspensionState = SuspensionState.ACTIVE.getStateCode();
        this.lifecycleState = TaskEntity.TaskState.STATE_INIT;
        this.isIdentityLinksInitialized = false;
        this.taskIdentityLinkEntities = new ArrayList();
        this.isFormKeyInitialized = false;
        this.variableStore = new VariableStore(this, new VariableStore.VariableStoreObserver[]{new TaskEntityReferencer(this)});
        this.skipCustomListeners = false;
        this.propertyChanges = new HashMap();
        this.identityLinkChanges = new ArrayList();
        this.isIdentityLinksInitialized = true;
        this.setCreateTime(ClockUtil.getCurrentTime());
        this.lifecycleState = initialState;
    }

    public TaskEntity(ExecutionEntity execution) {
        this(TaskEntity.TaskState.STATE_INIT);
        this.setExecution(execution);
        this.skipCustomListeners = execution.isSkipCustomListeners();
        this.setTenantId(execution.getTenantId());
        execution.addTask(this);
    }

    public TaskEntity(CaseExecutionEntity caseExecution) {
        this(TaskEntity.TaskState.STATE_INIT);
        this.setCaseExecution(caseExecution);
    }

    public void insert() {
        CommandContext commandContext = Context.getCommandContext();
        TaskManager taskManager = commandContext.getTaskManager();
        taskManager.insertTask(this);
    }

    protected void propagateExecutionTenantId(ExecutionEntity execution) {
        if (execution != null) {
            this.setTenantId(execution.getTenantId());
        }

    }

    public void propagateParentTaskTenantId() {
        if (this.parentTaskId != null) {
            TaskEntity parentTask = Context.getCommandContext().getTaskManager().findTaskById(this.parentTaskId);
            if (this.tenantId != null && !this.tenantIdIsSame(parentTask)) {
                throw LOG.cannotSetDifferentTenantIdOnSubtask(this.parentTaskId, parentTask.getTenantId(), this.tenantId);
            }

            this.setTenantId(parentTask.getTenantId());
        }

    }

    public void update() {
        this.ensureTenantIdNotChanged();
        this.registerCommandContextCloseListener();
        CommandContext commandContext = Context.getCommandContext();
        DbEntityManager dbEntityManger = commandContext.getDbEntityManager();
        dbEntityManger.merge(this);
    }

    protected void ensureTenantIdNotChanged() {
        TaskEntity persistentTask = Context.getCommandContext().getTaskManager().findTaskById(this.id);
        if (persistentTask != null) {
            boolean changed = !this.tenantIdIsSame(persistentTask);
            if (changed) {
                throw LOG.cannotChangeTenantIdOfTask(this.id, persistentTask.tenantId, this.tenantId);
            }
        }

    }

    protected boolean tenantIdIsSame(TaskEntity otherTask) {
        String otherTenantId = otherTask.getTenantId();
        if (otherTenantId == null) {
            return this.tenantId == null;
        } else {
            return otherTenantId.equals(this.tenantId);
        }
    }

    public void complete() {
        if (!TaskEntity.TaskState.STATE_COMPLETED.equals(this.lifecycleState) && !"complete".equals(this.eventName) && !"delete".equals(this.eventName)) {
            if (this.caseExecutionId != null) {
                this.getCaseExecution().manualComplete();
            } else {
                this.ensureTaskActive();
                boolean shouldDeleteTask = this.transitionTo(TaskEntity.TaskState.STATE_COMPLETED);
                if (shouldDeleteTask) {
                    Context.getCommandContext().getTaskManager().deleteTask(this, "completed", false, this.skipCustomListeners);
                    if (this.executionId != null) {
                        ExecutionEntity execution = this.getExecution();
                        execution.removeTask(this);
                        execution.signal((String) null, (Object) null);
                    }
                }

            }
        } else {
            throw LOG.invokeTaskListenerException(new IllegalStateException("invalid task state"));
        }
    }

    public void caseExecutionCompleted() {
        this.ensureTaskActive();
        this.transitionTo(TaskEntity.TaskState.STATE_COMPLETED);
        Context.getCommandContext().getTaskManager().deleteTask(this, "completed", false, false);
    }

    public void delete(String deleteReason, boolean cascade) {
        this.deleteReason = deleteReason;
        if (!"completed".equals(deleteReason) && !TaskEntity.TaskState.STATE_DELETED.equals(this.lifecycleState)) {
            this.transitionTo(TaskEntity.TaskState.STATE_DELETED);
        }

        Context.getCommandContext().getTaskManager().deleteTask(this, deleteReason, cascade, this.skipCustomListeners);
        if (this.executionId != null) {
            ExecutionEntity execution = this.getExecution();
            execution.removeTask(this);
        }

    }

    public void delete(String deleteReason, boolean cascade, boolean skipCustomListeners) {
        this.skipCustomListeners = skipCustomListeners;
        this.delete(deleteReason, cascade);
    }

    public void delegate(String userId) {
        this.setDelegationState(DelegationState.PENDING);
        if (this.getOwner() == null) {
            this.setOwner(this.getAssignee());
        }

        this.setAssignee(userId);
    }

    public void resolve() {
        this.setDelegationState(DelegationState.RESOLVED);
        this.setAssignee(this.owner);
    }

    public Object getPersistentState() {
        Map<String, Object> persistentState = new HashMap();
        persistentState.put("assignee", this.assignee);
        persistentState.put("owner", this.owner);
        persistentState.put("name", this.name);
        persistentState.put("priority", this.priority);
        if (this.executionId != null) {
            persistentState.put("executionId", this.executionId);
        }

        if (this.processDefinitionId != null) {
            persistentState.put("processDefinitionId", this.processDefinitionId);
        }

        if (this.caseExecutionId != null) {
            persistentState.put("caseExecutionId", this.caseExecutionId);
        }

        if (this.caseInstanceId != null) {
            persistentState.put("caseInstanceId", this.caseInstanceId);
        }

        if (this.caseDefinitionId != null) {
            persistentState.put("caseDefinitionId", this.caseDefinitionId);
        }

        if (this.createTime != null) {
            persistentState.put("createTime", this.createTime);
        }

        if (this.description != null) {
            persistentState.put("description", this.description);
        }

        if (this.dueDate != null) {
            persistentState.put("dueDate", this.dueDate);
        }

        if (this.followUpDate != null) {
            persistentState.put("followUpDate", this.followUpDate);
        }

        if (this.parentTaskId != null) {
            persistentState.put("parentTaskId", this.parentTaskId);
        }

        if (this.delegationState != null) {
            persistentState.put("delegationState", this.delegationState);
        }

        if (this.tenantId != null) {
            persistentState.put("tenantId", this.tenantId);
        }

        persistentState.put("suspensionState", this.suspensionState);
        return persistentState;
    }

    public int getRevisionNext() {
        return this.revision + 1;
    }

    public void ensureParentTaskActive() {
        if (this.parentTaskId != null) {
            TaskEntity parentTask = Context.getCommandContext().getTaskManager().findTaskById(this.parentTaskId);
            EnsureUtil.ensureNotNull(NullValueException.class, "Parent task with id '" + this.parentTaskId + "' does not exist", "parentTask", parentTask);
            if (parentTask.suspensionState == SuspensionState.SUSPENDED.getStateCode()) {
                throw LOG.suspendedEntityException("parent task", this.id);
            }
        }

    }

    protected void ensureTaskActive() {
        if (this.suspensionState == SuspensionState.SUSPENDED.getStateCode()) {
            throw LOG.suspendedEntityException("task", this.id);
        }
    }

    public UserTask getBpmnModelElementInstance() {
        BpmnModelInstance bpmnModelInstance = this.getBpmnModelInstance();
        if (bpmnModelInstance != null) {
            ModelElementInstance modelElementInstance = bpmnModelInstance.getModelElementById(this.taskDefinitionKey);

            try {
                return (UserTask) modelElementInstance;
            } catch (ClassCastException var5) {
                ModelElementType elementType = modelElementInstance.getElementType();
                throw LOG.castModelInstanceException(modelElementInstance, "UserTask", elementType.getTypeName(), elementType.getTypeNamespace(), var5);
            }
        } else {
            return null;
        }
    }

    public BpmnModelInstance getBpmnModelInstance() {
        return this.processDefinitionId != null ? Context.getProcessEngineConfiguration().getDeploymentCache().findBpmnModelInstanceForProcessDefinition(this.processDefinitionId) : null;
    }

    protected VariableStore<VariableInstanceEntity> getVariableStore() {
        return this.variableStore;
    }

    protected VariableInstanceFactory<VariableInstanceEntity> getVariableInstanceFactory() {
        return VariableInstanceEntityFactory.INSTANCE;
    }

    protected List<VariableInstanceLifecycleListener<VariableInstanceEntity>> getVariableInstanceLifecycleListeners() {
        if (this.customLifecycleListeners != null && !this.customLifecycleListeners.isEmpty()) {
            List<VariableInstanceLifecycleListener<VariableInstanceEntity>> listeners = new ArrayList();
            listeners.addAll(DEFAULT_VARIABLE_LIFECYCLE_LISTENERS);
            listeners.addAll(this.customLifecycleListeners);
            return listeners;
        } else {
            return DEFAULT_VARIABLE_LIFECYCLE_LISTENERS;
        }
    }

    public void addCustomLifecycleListener(VariableInstanceLifecycleListener<VariableInstanceEntity> customLifecycleListener) {
        if (this.customLifecycleListeners == null) {
            this.customLifecycleListeners = new ArrayList();
        }

        this.customLifecycleListeners.add(customLifecycleListener);
    }

    public VariableInstanceLifecycleListener<VariableInstanceEntity> removeCustomLifecycleListener(VariableInstanceLifecycleListener<VariableInstanceEntity> customLifecycleListener) {
        if (this.customLifecycleListeners != null) {
            this.customLifecycleListeners.remove(customLifecycleListener);
        }

        return customLifecycleListener;
    }

    public void dispatchEvent(VariableEvent variableEvent) {
        if (this.execution != null && variableEvent.getVariableInstance().getTaskId() == null) {
            this.execution.handleConditionalEventOnVariableChange(variableEvent);
        }

    }

    public Collection<VariableInstanceEntity> provideVariables() {
        return Context.getCommandContext().getVariableInstanceManager().findVariableInstancesByTaskId(this.id);
    }

    public Collection<VariableInstanceEntity> provideVariables(Collection<String> variableNames) {
        return Context.getCommandContext().getVariableInstanceManager().findVariableInstancesByTaskIdAndVariableNames(this.id, variableNames);
    }

    public AbstractVariableScope getParentVariableScope() {
        if (this.getExecution() != null) {
            return this.execution;
        } else if (this.getCaseExecution() != null) {
            return this.caseExecution;
        } else {
            return this.getParentTask() != null ? this.parentTask : null;
        }
    }

    public String getVariableScopeKey() {
        return "task";
    }

    public TaskEntity getParentTask() {
        if (this.parentTask == null && this.parentTaskId != null) {
            this.parentTask = Context.getCommandContext().getTaskManager().findTaskById(this.parentTaskId);
        }

        return this.parentTask;
    }

    public ExecutionEntity getExecution() {
        if (this.execution == null && this.executionId != null) {
            this.execution = Context.getCommandContext().getExecutionManager().findExecutionById(this.executionId);
        }

        return this.execution;
    }

    public void setExecution(PvmExecutionImpl execution) {
        if (execution != null) {
            this.execution = (ExecutionEntity) execution;
            this.executionId = this.execution.getId();
            this.processInstanceId = this.execution.getProcessInstanceId();
            this.processDefinitionId = this.execution.getProcessDefinitionId();
            ExecutionEntity instance = this.execution.getProcessInstance();
            if (instance != null) {
                this.caseInstanceId = instance.getCaseInstanceId();
            }
        } else {
            this.execution = null;
            this.executionId = null;
            this.processInstanceId = null;
            this.processDefinitionId = null;
            this.caseInstanceId = null;
        }

    }

    public CaseExecutionEntity getCaseExecution() {
        this.ensureCaseExecutionInitialized();
        return this.caseExecution;
    }

    protected void ensureCaseExecutionInitialized() {
        if (this.caseExecution == null && this.caseExecutionId != null) {
            this.caseExecution = Context.getCommandContext().getCaseExecutionManager().findCaseExecutionById(this.caseExecutionId);
        }

    }

    public void setCaseExecution(CaseExecutionEntity caseExecution) {
        if (caseExecution != null) {
            this.caseExecution = caseExecution;
            this.caseExecutionId = this.caseExecution.getId();
            this.caseInstanceId = this.caseExecution.getCaseInstanceId();
            this.caseDefinitionId = this.caseExecution.getCaseDefinitionId();
            this.tenantId = this.caseExecution.getTenantId();
        } else {
            this.caseExecution = null;
            this.caseExecutionId = null;
            this.caseInstanceId = null;
            this.caseDefinitionId = null;
            this.tenantId = null;
        }

    }

    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }

    public void setCaseExecutionId(String caseExecutionId) {
        this.caseExecutionId = caseExecutionId;
    }

    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }

    public void setCaseInstanceId(String caseInstanceId) {
        this.registerCommandContextCloseListener();
        this.propertyChanged("caseInstanceId", this.caseInstanceId, caseInstanceId);
        this.caseInstanceId = caseInstanceId;
    }

    public CaseDefinitionEntity getCaseDefinition() {
        return this.caseDefinitionId != null ? Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedCaseDefinitionById(this.caseDefinitionId) : null;
    }

    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }

    public void setCaseDefinitionId(String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }

    public IdentityLinkEntity addIdentityLink(String userId, String groupId, String type) {
        this.ensureTaskActive();
        IdentityLinkEntity identityLink = this.newIdentityLink(userId, groupId, type);
        identityLink.insert();
        this.getIdentityLinks().add(identityLink);
        this.fireAddIdentityLinkAuthorizationProvider(type, userId, groupId);
        return identityLink;
    }

    public void fireIdentityLinkHistoryEvents(String userId, String groupId, String type, HistoryEventTypes historyEventType) {
        IdentityLinkEntity identityLinkEntity = this.newIdentityLink(userId, groupId, type);
        identityLinkEntity.fireHistoricIdentityLinkEvent(historyEventType);
    }

    public IdentityLinkEntity newIdentityLink(String userId, String groupId, String type) {
        IdentityLinkEntity identityLinkEntity = new IdentityLinkEntity();
        identityLinkEntity.setTask(this);
        identityLinkEntity.setUserId(userId);
        identityLinkEntity.setGroupId(groupId);
        identityLinkEntity.setType(type);
        identityLinkEntity.setTenantId(this.getTenantId());
        return identityLinkEntity;
    }

    public void deleteIdentityLink(String userId, String groupId, String type) {
        this.ensureTaskActive();
        List<IdentityLinkEntity> identityLinks = Context.getCommandContext().getIdentityLinkManager().findIdentityLinkByTaskUserGroupAndType(this.id, userId, groupId, type);
        Iterator var5 = identityLinks.iterator();

        while (var5.hasNext()) {
            IdentityLinkEntity identityLink = (IdentityLinkEntity) var5.next();
            this.fireDeleteIdentityLinkAuthorizationProvider(type, userId, groupId);
            identityLink.delete();
        }

    }

    public void deleteIdentityLinks() {
        List<IdentityLinkEntity> identityLinkEntities = this.getIdentityLinks();
        Iterator var2 = identityLinkEntities.iterator();

        while (var2.hasNext()) {
            IdentityLinkEntity identityLinkEntity = (IdentityLinkEntity) var2.next();
            this.fireDeleteIdentityLinkAuthorizationProvider(identityLinkEntity.getType(), identityLinkEntity.getUserId(), identityLinkEntity.getGroupId());
            identityLinkEntity.delete(false);
        }

        this.isIdentityLinksInitialized = false;
    }

    public Set<IdentityLink> getCandidates() {
        Set<IdentityLink> potentialOwners = new HashSet();
        Iterator var2 = this.getIdentityLinks().iterator();

        while (var2.hasNext()) {
            IdentityLinkEntity identityLinkEntity = (IdentityLinkEntity) var2.next();
            if ("candidate".equals(identityLinkEntity.getType())) {
                potentialOwners.add(identityLinkEntity);
            }
        }

        return potentialOwners;
    }

    public void addCandidateUser(String userId) {
        this.addIdentityLink(userId, (String) null, "candidate");
    }

    public void addCandidateUsers(Collection<String> candidateUsers) {
        Iterator var2 = candidateUsers.iterator();

        while (var2.hasNext()) {
            String candidateUser = (String) var2.next();
            this.addCandidateUser(candidateUser);
        }

    }

    public void addCandidateGroup(String groupId) {
        this.addIdentityLink((String) null, groupId, "candidate");
    }

    public void addCandidateGroups(Collection<String> candidateGroups) {
        Iterator var2 = candidateGroups.iterator();

        while (var2.hasNext()) {
            String candidateGroup = (String) var2.next();
            this.addCandidateGroup(candidateGroup);
        }

    }

    public void addGroupIdentityLink(String groupId, String identityLinkType) {
        this.addIdentityLink((String) null, groupId, identityLinkType);
    }

    public void addUserIdentityLink(String userId, String identityLinkType) {
        this.addIdentityLink(userId, (String) null, identityLinkType);
    }

    public void deleteCandidateGroup(String groupId) {
        this.deleteGroupIdentityLink(groupId, "candidate");
    }

    public void deleteCandidateUser(String userId) {
        this.deleteUserIdentityLink(userId, "candidate");
    }

    public void deleteGroupIdentityLink(String groupId, String identityLinkType) {
        if (groupId != null) {
            this.deleteIdentityLink((String) null, groupId, identityLinkType);
        }

    }

    public void deleteUserIdentityLink(String userId, String identityLinkType) {
        if (userId != null) {
            this.deleteIdentityLink(userId, (String) null, identityLinkType);
        }

    }

    public List<IdentityLinkEntity> getIdentityLinks() {
        if (!this.isIdentityLinksInitialized) {
            this.taskIdentityLinkEntities = Context.getCommandContext().getIdentityLinkManager().findIdentityLinksByTaskId(this.id);
            this.isIdentityLinksInitialized = true;
        }

        return this.taskIdentityLinkEntities;
    }

    public Map<String, Object> getActivityInstanceVariables() {
        return (Map) (this.execution != null ? this.execution.getVariables() : Collections.EMPTY_MAP);
    }

    public void setExecutionVariables(Map<String, Object> parameters) {
        AbstractVariableScope scope = this.getParentVariableScope();
        if (scope != null) {
            scope.setVariables(parameters);
        }

    }

    public String toString() {
        return "Task[" + this.id + "]";
    }

    public void setName(String taskName) {
        this.registerCommandContextCloseListener();
        this.propertyChanged("name", this.name, taskName);
        this.name = taskName;
    }

    public void setDescription(String description) {
        this.registerCommandContextCloseListener();
        this.propertyChanged("description", this.description, description);
        this.description = description;
    }

    public void setAssignee(String assignee) {
        Date timestamp = ClockUtil.getCurrentTime();
        this.ensureTaskActive();
        this.registerCommandContextCloseListener();
        String oldAssignee = this.assignee;
        if (assignee != null || oldAssignee != null) {
            this.addIdentityLinkChanges("assignee", oldAssignee, assignee);
            this.propertyChanged("assignee", oldAssignee, assignee);
            this.assignee = assignee;
            CommandContext commandContext = Context.getCommandContext();
            if (commandContext != null) {
                if (commandContext.getDbEntityManager().contains(this)) {
                    this.fireAssigneeAuthorizationProvider(oldAssignee, assignee);
                    this.fireHistoricIdentityLinks();
                }

                if (commandContext.getProcessEngineConfiguration().isTaskMetricsEnabled() && assignee != null && !assignee.equals(oldAssignee)) {
                    commandContext.getMeterLogManager().insert(new TaskMeterLogEntity(assignee, timestamp));
                }
            }

        }
    }

    public void setOwner(String owner) {
        this.ensureTaskActive();
        this.registerCommandContextCloseListener();
        String oldOwner = this.owner;
        if (owner != null || oldOwner != null) {
            this.addIdentityLinkChanges("owner", oldOwner, owner);
            this.propertyChanged("owner", oldOwner, owner);
            this.owner = owner;
            CommandContext commandContext = Context.getCommandContext();
            if (commandContext != null && commandContext.getDbEntityManager().contains(this)) {
                this.fireOwnerAuthorizationProvider(oldOwner, owner);
                this.fireHistoricIdentityLinks();
            }

        }
    }

    public void setDueDate(Date dueDate) {
        this.registerCommandContextCloseListener();
        this.propertyChanged("dueDate", this.dueDate, dueDate);
        this.dueDate = dueDate;
    }

    public void setPriority(int priority) {
        this.registerCommandContextCloseListener();
        this.propertyChanged("priority", this.priority, priority);
        this.priority = priority;
    }

    public void setParentTaskId(String parentTaskId) {
        this.registerCommandContextCloseListener();
        this.propertyChanged("parentTask", this.parentTaskId, parentTaskId);
        this.parentTaskId = parentTaskId;
    }

    public void setNameWithoutCascade(String taskName) {
        this.name = taskName;
    }

    public void setDescriptionWithoutCascade(String description) {
        this.description = description;
    }

    public void setAssigneeWithoutCascade(String assignee) {
        this.assignee = assignee;
    }

    public void setOwnerWithoutCascade(String owner) {
        this.owner = owner;
    }

    public void setDueDateWithoutCascade(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setPriorityWithoutCascade(int priority) {
        this.priority = priority;
    }

    public void setCaseInstanceIdWithoutCascade(String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }

    public void setParentTaskIdWithoutCascade(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public void setTaskDefinitionKeyWithoutCascade(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public void setDelegationStateWithoutCascade(DelegationState delegationState) {
        this.delegationState = delegationState;
    }

    public void setDelegationStateString(String delegationState) {
        if (delegationState == null) {
            this.setDelegationStateWithoutCascade((DelegationState) null);
        } else {
            this.setDelegationStateWithoutCascade(DelegationState.valueOf(delegationState));
        }

    }

    public void setFollowUpDateWithoutCascade(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public boolean fireEvent(String taskEventName) {
        List<TaskListener> taskEventListeners = this.getListenersForEvent(taskEventName);
        if (taskEventListeners != null) {
            Iterator var3 = taskEventListeners.iterator();

            while (var3.hasNext()) {
                TaskListener taskListener = (TaskListener) var3.next();
                if (!this.invokeListener(taskEventName, taskListener)) {
                    return false;
                }
            }
        }

        return true;
    }

    protected List<TaskListener> getListenersForEvent(String event) {
        TaskDefinition resolvedTaskDefinition = this.getTaskDefinition();
        if (resolvedTaskDefinition != null) {
            return this.skipCustomListeners ? resolvedTaskDefinition.getBuiltinTaskListeners(event) : resolvedTaskDefinition.getTaskListeners(event);
        } else {
            return null;
        }
    }

    protected TaskListener getTimeoutListener(String timeoutId) {
        TaskDefinition resolvedTaskDefinition = this.getTaskDefinition();
        return resolvedTaskDefinition == null ? null : resolvedTaskDefinition.getTimeoutTaskListener(timeoutId);
    }

    protected boolean invokeListener(String taskEventName, TaskListener taskListener) {
        boolean popProcessDataContext = false;
        CommandInvocationContext commandInvocationContext = Context.getCommandInvocationContext();
        CoreExecution execution = this.getExecution();
        if (execution == null) {
            execution = this.getCaseExecution();
        } else if (commandInvocationContext != null) {
            popProcessDataContext = commandInvocationContext.getProcessDataContext().pushSection((ExecutionEntity) execution);
        }

        if (execution != null) {
            this.setEventName(taskEventName);
        }

        try {
            boolean result = this.invokeListener((CoreExecution) execution, taskEventName, taskListener);
            if (popProcessDataContext) {
                commandInvocationContext.getProcessDataContext().popSection();
            }

            return result;
        } catch (Exception var7) {
            throw LOG.invokeTaskListenerException(var7);
        }
    }

    protected boolean invokeListener(CoreExecution currentExecution, String eventName, TaskListener taskListener) throws Exception {
        boolean isBpmnTask = currentExecution instanceof ActivityExecution && currentExecution != null;
        TaskListenerInvocation listenerInvocation = new TaskListenerInvocation(taskListener, this, currentExecution);

        try {
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(listenerInvocation);
            return true;
        } catch (Exception var9) {
            Exception ex = var9;
            if (isBpmnTask && !eventName.equals("delete")) {
                try {
                    BpmnExceptionHandler.propagateException((ActivityExecution) currentExecution, ex);
                    return false;
                } catch (ErrorPropagationException var8) {
                    throw var9;
                }
            } else {
                throw var9;
            }
        }
    }

    protected void propertyChanged(String propertyName, Object orgValue, Object newValue) {
        if (this.propertyChanges.containsKey(propertyName)) {
            Object oldOrgValue = ((PropertyChange) this.propertyChanges.get(propertyName)).getOrgValue();
            if ((oldOrgValue != null || newValue != null) && (oldOrgValue == null || !oldOrgValue.equals(newValue))) {
                ((PropertyChange) this.propertyChanges.get(propertyName)).setNewValue(newValue);
            } else {
                this.propertyChanges.remove(propertyName);
            }
        } else if (orgValue == null && newValue != null || orgValue != null && newValue == null || orgValue != null && !orgValue.equals(newValue)) {
            this.propertyChanges.put(propertyName, new PropertyChange(propertyName, orgValue, newValue));
        }

    }

    public void fireAuthorizationProvider() {
        PropertyChange assigneePropertyChange = (PropertyChange) this.propertyChanges.get("assignee");
        String oldOwner;
        if (assigneePropertyChange != null) {
            String oldAssignee = assigneePropertyChange.getOrgValueString();
            oldOwner = assigneePropertyChange.getNewValueString();
            this.fireAssigneeAuthorizationProvider(oldAssignee, oldOwner);
        }

        PropertyChange ownerPropertyChange = (PropertyChange) this.propertyChanges.get("owner");
        if (ownerPropertyChange != null) {
            oldOwner = ownerPropertyChange.getOrgValueString();
            String newOwner = ownerPropertyChange.getNewValueString();
            this.fireOwnerAuthorizationProvider(oldOwner, newOwner);
        }

    }

    public boolean transitionTo(TaskState state) {
        this.lifecycleState = state;
        switch (state) {
            case STATE_CREATED:
                CommandContext commandContext = Context.getCommandContext();
                if (commandContext != null) {
                    commandContext.getHistoricTaskInstanceManager().createHistoricTask(this);
                }

                return this.fireEvent("create") && this.fireAssignmentEvent();
            case STATE_COMPLETED:
                return this.fireEvent("complete") && TaskEntity.TaskState.STATE_COMPLETED.equals(this.lifecycleState);
            case STATE_DELETED:
                return this.fireEvent("delete");
            case STATE_INIT:
            default:
                throw new ProcessEngineException(String.format("Task %s cannot transition into state %s.", this.id, state));
        }
    }

    public boolean triggerUpdateEvent() {
        if (this.lifecycleState != TaskEntity.TaskState.STATE_CREATED) {
            return true;
        } else {
            return this.fireEvent("update") && this.fireAssignmentEvent();
        }
    }

    public boolean triggerTimeoutEvent(String timeoutId) {
        TaskListener taskListener = this.getTimeoutListener(timeoutId);
        if (taskListener == null) {
            throw LOG.invokeTaskListenerException(new NotFoundException("Cannot find timeout taskListener with id '" + timeoutId + "' for task " + this.id));
        } else {
            return this.invokeListener("timeout", taskListener);
        }
    }

    protected boolean fireAssignmentEvent() {
        PropertyChange assigneePropertyChange = (PropertyChange) this.propertyChanges.get("assignee");
        return assigneePropertyChange != null ? this.fireEvent("assignment") : true;
    }

    protected void fireAssigneeAuthorizationProvider(String oldAssignee, String newAssignee) {
        this.fireAuthorizationProvider("assignee", oldAssignee, newAssignee);
    }

    protected void fireOwnerAuthorizationProvider(String oldOwner, String newOwner) {
        this.fireAuthorizationProvider("owner", oldOwner, newOwner);
    }

    protected void fireAuthorizationProvider(String property, String oldValue, String newValue) {
        if (this.isAuthorizationEnabled() && this.caseExecutionId == null) {
            ResourceAuthorizationProvider provider = this.getResourceAuthorizationProvider();
            AuthorizationEntity[] authorizations = null;
            if ("assignee".equals(property)) {
                authorizations = provider.newTaskAssignee(this, oldValue, newValue);
            } else if ("owner".equals(property)) {
                authorizations = provider.newTaskOwner(this, oldValue, newValue);
            }

            this.saveAuthorizations(authorizations);
        }

    }

    protected void fireAddIdentityLinkAuthorizationProvider(String type, String userId, String groupId) {
        if (this.isAuthorizationEnabled() && this.caseExecutionId == null) {
            ResourceAuthorizationProvider provider = this.getResourceAuthorizationProvider();
            AuthorizationEntity[] authorizations = null;
            if (userId != null) {
                authorizations = provider.newTaskUserIdentityLink(this, userId, type);
            } else if (groupId != null) {
                authorizations = provider.newTaskGroupIdentityLink(this, groupId, type);
            }

            this.saveAuthorizations(authorizations);
        }

    }

    protected void fireDeleteIdentityLinkAuthorizationProvider(String type, String userId, String groupId) {
        if (this.isAuthorizationEnabled() && this.caseExecutionId == null) {
            ResourceAuthorizationProvider provider = this.getResourceAuthorizationProvider();
            AuthorizationEntity[] authorizations = null;
            if (userId != null) {
                authorizations = provider.deleteTaskUserIdentityLink(this, userId, type);
            } else if (groupId != null) {
                authorizations = provider.deleteTaskGroupIdentityLink(this, groupId, type);
            }

            this.deleteAuthorizations(authorizations);
        }

    }

    protected ResourceAuthorizationProvider getResourceAuthorizationProvider() {
        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        return processEngineConfiguration.getResourceAuthorizationProvider();
    }

    protected void saveAuthorizations(AuthorizationEntity[] authorizations) {
        CommandContext commandContext = Context.getCommandContext();
        TaskManager taskManager = commandContext.getTaskManager();
        taskManager.saveDefaultAuthorizations(authorizations);
    }

    protected void deleteAuthorizations(AuthorizationEntity[] authorizations) {
        CommandContext commandContext = Context.getCommandContext();
        TaskManager taskManager = commandContext.getTaskManager();
        taskManager.deleteDefaultAuthorizations(authorizations);
    }

    protected boolean isAuthorizationEnabled() {
        return Context.getProcessEngineConfiguration().isAuthorizationEnabled();
    }

    public void setTaskDefinition(TaskDefinition taskDefinition) {
        this.taskDefinition = taskDefinition;
        this.taskDefinitionKey = taskDefinition.getKey();
    }

    public TaskDefinition getTaskDefinition() {
        if (this.taskDefinition == null && this.taskDefinitionKey != null) {
            Map<String, TaskDefinition> taskDefinitions = null;
            if (this.processDefinitionId != null) {
                ProcessDefinitionEntity processDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(this.processDefinitionId);
                taskDefinitions = processDefinition.getTaskDefinitions();
            } else {
                CaseDefinitionEntity caseDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedCaseDefinitionById(this.caseDefinitionId);
                taskDefinitions = caseDefinition.getTaskDefinitions();
            }

            this.taskDefinition = (TaskDefinition) taskDefinitions.get(this.taskDefinitionKey);
        }

        return this.taskDefinition;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRevision() {
        return this.revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public int getPriority() {
        return this.priority;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExecutionId() {
        return this.executionId;
    }

    public String getProcessInstanceId() {
        return this.processInstanceId;
    }

    public boolean isStandaloneTask() {
        return this.executionId == null && this.caseExecutionId == null;
    }

    public ProcessDefinitionEntity getProcessDefinition() {
        return this.processDefinitionId != null ? Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(this.processDefinitionId) : null;
    }

    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }

    public void initializeFormKey() {
        this.isFormKeyInitialized = true;
        if (this.taskDefinitionKey != null) {
            TaskDefinition taskDefinition = this.getTaskDefinition();
            if (taskDefinition != null) {
                Expression formKey = taskDefinition.getFormKey();
                if (formKey != null) {
                    this.formKey = (String) formKey.getValue(this);
                } else {
                    Expression formRef = taskDefinition.getCamundaFormDefinitionKey();
                    String formRefBinding = taskDefinition.getCamundaFormDefinitionBinding();
                    Expression formRefVersion = taskDefinition.getCamundaFormDefinitionVersion();
                    if (formRef != null && formRefBinding != null) {
                        String formRefValue = (String) formRef.getValue(this);
                        if (formRefValue != null) {
                            CamundaFormRefImpl camFormRef = new CamundaFormRefImpl(formRefValue, formRefBinding);
                            if (formRefBinding.equals("version") && formRefVersion != null) {
                                String formRefVersionValue = (String) formRefVersion.getValue(this);
                                camFormRef.setVersion(Integer.parseInt(formRefVersionValue));
                            }

                            this.camundaFormRef = camFormRef;
                        }
                    }
                }
            }
        }

    }

    public String getFormKey() {
        if (!this.isFormKeyInitialized) {
            throw LOG.uninitializedFormKeyException();
        } else {
            return this.formKey;
        }
    }

    public CamundaFormRef getCamundaFormRef() {
        if (!this.isFormKeyInitialized) {
            throw LOG.uninitializedFormKeyException();
        } else {
            return this.camundaFormRef;
        }
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getAssignee() {
        return this.assignee;
    }

    public String getTaskDefinitionKey() {
        return this.taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        if (taskDefinitionKey == null && this.taskDefinitionKey != null || taskDefinitionKey != null && !taskDefinitionKey.equals(this.taskDefinitionKey)) {
            this.taskDefinition = null;
            this.formKey = null;
            this.isFormKeyInitialized = false;
        }

        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public ExecutionEntity getProcessInstance() {
        if (this.processInstance == null && this.processInstanceId != null) {
            this.processInstance = Context.getCommandContext().getExecutionManager().findExecutionById(this.processInstanceId);
        }

        return this.processInstance;
    }

    public void setProcessInstance(ExecutionEntity processInstance) {
        this.processInstance = processInstance;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getOwner() {
        return this.owner;
    }

    public DelegationState getDelegationState() {
        return this.delegationState;
    }

    public void setDelegationState(DelegationState delegationState) {
        this.propertyChanged("delegation", this.delegationState, delegationState);
        this.delegationState = delegationState;
    }

    public String getDelegationStateString() {
        return this.delegationState != null ? this.delegationState.toString() : null;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public String getDeleteReason() {
        return this.deleteReason;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getParentTaskId() {
        return this.parentTaskId;
    }

    public int getSuspensionState() {
        return this.suspensionState;
    }

    public void setSuspensionState(int suspensionState) {
        this.suspensionState = suspensionState;
    }

    public boolean isSuspended() {
        return this.suspensionState == SuspensionState.SUSPENDED.getStateCode();
    }

    public Date getFollowUpDate() {
        return this.followUpDate;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.registerCommandContextCloseListener();
        this.propertyChanged("followUpDate", this.followUpDate, followUpDate);
        this.followUpDate = followUpDate;
    }

    public Collection<VariableInstanceEntity> getVariablesInternal() {
        return this.variableStore.getVariables();
    }

    public void onCommandContextClose(CommandContext commandContext) {
        if (commandContext.getDbEntityManager().isDirty(this)) {
            commandContext.getHistoricTaskInstanceManager().updateHistoricTaskInstance(this);
        }

    }

    public void onCommandFailed(CommandContext commandContext, Throwable t) {
    }

    protected void registerCommandContextCloseListener() {
        CommandContext commandContext = Context.getCommandContext();
        if (commandContext != null) {
            commandContext.registerCommandContextListener(this);
        }

    }

    public Map<String, PropertyChange> getPropertyChanges() {
        return this.propertyChanges;
    }

    public void logUserOperation(String operation) {
        if ("Complete".equals(operation) || "Delete".equals(operation)) {
            this.propertyChanged("delete", false, true);
        }

        CommandContext commandContext = Context.getCommandContext();
        if (commandContext != null) {
            List<PropertyChange> values = new ArrayList(this.propertyChanges.values());
            commandContext.getOperationLogManager().logTaskOperations(operation, this, values);
            this.fireHistoricIdentityLinks();
            this.propertyChanges.clear();
        }

    }

    public void fireHistoricIdentityLinks() {
        Iterator var1 = this.identityLinkChanges.iterator();

        while (var1.hasNext()) {
            PropertyChange propertyChange = (PropertyChange) var1.next();
            String oldValue = propertyChange.getOrgValueString();
            String propertyName = propertyChange.getPropertyName();
            if (oldValue != null) {
                this.fireIdentityLinkHistoryEvents(oldValue, (String) null, propertyName, HistoryEventTypes.IDENTITY_LINK_DELETE);
            }

            String newValue = propertyChange.getNewValueString();
            if (newValue != null) {
                this.fireIdentityLinkHistoryEvents(newValue, (String) null, propertyName, HistoryEventTypes.IDENTITY_LINK_ADD);
            }
        }

        this.identityLinkChanges.clear();
    }

    public ProcessEngineServices getProcessEngineServices() {
        return Context.getProcessEngineConfiguration().getProcessEngine();
    }

    public ProcessEngine getProcessEngine() {
        return Context.getProcessEngineConfiguration().getProcessEngine();
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            TaskEntity other = (TaskEntity) obj;
            if (this.id == null) {
                if (other.id != null) {
                    return false;
                }
            } else if (!this.id.equals(other.id)) {
                return false;
            }

            return true;
        }
    }

    public void executeMetrics(String metricsName, CommandContext commandContext) {
        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if ("activity-instance-start".equals(metricsName) && processEngineConfiguration.isMetricsEnabled()) {
            processEngineConfiguration.getMetricsRegistry().markOccurrence("activity-instance-start");
        }

        if ("unique-task-workers".equals(metricsName) && processEngineConfiguration.isTaskMetricsEnabled() && this.assignee != null && this.propertyChanges.containsKey("assignee")) {
            commandContext.getMeterLogManager().insert(new TaskMeterLogEntity(this.assignee, ClockUtil.getCurrentTime()));
        }

    }

    public void addIdentityLinkChanges(String type, String oldProperty, String newProperty) {
        this.identityLinkChanges.add(new PropertyChange(type, oldProperty, newProperty));
    }

    public void setVariablesLocal(Map<String, ?> variables, boolean skipJavaSerializationFormatCheck) {
        super.setVariablesLocal(variables, skipJavaSerializationFormatCheck);
        Context.getCommandContext().getDbEntityManager().forceUpdate(this);
    }

    public Set<String> getReferencedEntityIds() {
        Set<String> referencedEntityIds = new HashSet();
        return referencedEntityIds;
    }

    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        Map<String, Class> referenceIdAndClass = new HashMap();
        if (this.processDefinitionId != null) {
            referenceIdAndClass.put(this.processDefinitionId, ProcessDefinitionEntity.class);
        }

        if (this.processInstanceId != null) {
            referenceIdAndClass.put(this.processInstanceId, ExecutionEntity.class);
        }

        if (this.executionId != null) {
            referenceIdAndClass.put(this.executionId, ExecutionEntity.class);
        }

        if (this.caseDefinitionId != null) {
            referenceIdAndClass.put(this.caseDefinitionId, CaseDefinitionEntity.class);
        }

        if (this.caseExecutionId != null) {
            referenceIdAndClass.put(this.caseExecutionId, CaseExecutionEntity.class);
        }

        return referenceIdAndClass;
    }

    public void bpmnError(String errorCode, String errorMessage, Map<String, Object> variables) {
        this.ensureTaskActive();
        ActivityExecution activityExecution = this.getExecution();
        BpmnError bpmnError = null;
        if (errorMessage != null) {
            bpmnError = new BpmnError(errorCode, errorMessage);
        } else {
            bpmnError = new BpmnError(errorCode);
        }

        try {
            if (variables != null && !variables.isEmpty()) {
                activityExecution.setVariables(variables);
            }

            BpmnExceptionHandler.propagateBpmnError(bpmnError, activityExecution);
        } catch (Exception var7) {
            throw ProcessEngineLogger.CMD_LOGGER.exceptionBpmnErrorPropagationFailed(errorCode, var7);
        }
    }

    public void escalation(String escalationCode, Map<String, Object> variables) {
        this.ensureTaskActive();
        ActivityExecution activityExecution = this.getExecution();
        if (variables != null && !variables.isEmpty()) {
            activityExecution.setVariables(variables);
        }

        EscalationHandler.propagateEscalation(activityExecution, escalationCode);
    }

    static {
        DEFAULT_VARIABLE_LIFECYCLE_LISTENERS = Arrays.asList(VariableInstanceEntityPersistenceListener.INSTANCE, VariableInstanceSequenceCounterListener.INSTANCE, VariableInstanceHistoryListener.INSTANCE);
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }

    public static enum TaskState {
        STATE_INIT,
        STATE_CREATED,
        STATE_COMPLETED,
        STATE_DELETED;

        private TaskState() {
        }
    }
}
