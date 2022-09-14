// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.ProcessEngineServices;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.cfg.multitenancy.TenantIdProvider;
import org.zik.bpm.engine.impl.cfg.multitenancy.TenantIdProviderProcessInstanceContext;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.core.variable.scope.*;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.incident.IncidentContext;
import org.zik.bpm.engine.impl.incident.IncidentHandling;
import org.zik.bpm.engine.impl.interceptor.AtomicOperationInvocation;
import org.zik.bpm.engine.impl.jobexecutor.MessageJobDeclaration;
import org.zik.bpm.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmProcessDefinition;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.ActivityInstanceState;
import org.zik.bpm.engine.impl.pvm.runtime.AtomicOperation;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;
import org.zik.bpm.engine.impl.tree.ExecutionTopDownWalker;
import org.zik.bpm.engine.impl.tree.TreeVisitor;
import org.zik.bpm.engine.impl.util.BitMaskUtil;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.variable.VariableDeclaration;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.runtime.Execution;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.runtime.ProcessInstance;

import java.util.*;


public class ExecutionEntity extends PvmExecutionImpl implements Execution, ProcessInstance, DbEntity, HasDbRevision, HasDbReferences, VariableStore.VariablesProvider<VariableInstanceEntity> {
    private static final long serialVersionUID = 1L;
    protected static final EnginePersistenceLogger LOG;
    public static final int EVENT_SUBSCRIPTIONS_STATE_BIT = 1;
    public static final int TASKS_STATE_BIT = 2;
    public static final int JOBS_STATE_BIT = 3;
    public static final int INCIDENT_STATE_BIT = 4;
    public static final int VARIABLES_STATE_BIT = 5;
    public static final int SUB_PROCESS_INSTANCE_STATE_BIT = 6;
    public static final int SUB_CASE_INSTANCE_STATE_BIT = 7;
    public static final int EXTERNAL_TASKS_BIT = 8;
    protected transient ExecutionEntity processInstance;
    protected transient ExecutionEntity parent;
    protected transient List<PvmExecutionImpl> executions;
    protected transient ExecutionEntity superExecution;
    protected transient CaseExecutionEntity superCaseExecution;
    protected transient ExecutionEntity subProcessInstance;
    protected transient CaseExecutionEntity subCaseInstance;
    protected boolean shouldQueryForSubprocessInstance = false;
    protected boolean shouldQueryForSubCaseInstance = false;
    protected transient List<EventSubscriptionEntity> eventSubscriptions;
    protected transient List<JobEntity> jobs;
    protected transient List<TaskEntity> tasks;
    protected transient List<ExternalTaskEntity> externalTasks;
    protected transient List<IncidentEntity> incidents;
    protected int cachedEntityState;
    protected transient VariableStore<VariableInstanceEntity> variableStore = new VariableStore(this, new VariableStore.VariableStoreObserver[]{new ExecutionEntityReferencer(this)});
    protected int suspensionState;
    protected int revision;
    protected String processDefinitionId;
    protected String activityId;
    protected String activityName;
    protected String processInstanceId;
    protected String parentId;
    protected String superExecutionId;
    protected String rootProcessInstanceId;
    protected String superCaseExecutionId;
    protected transient List<ExecutionObserver> executionObservers;
    protected transient List<VariableInstanceLifecycleListener<VariableInstanceEntity>> registeredVariableListeners;

    public ExecutionEntity() {
        this.suspensionState = SuspensionState.ACTIVE.getStateCode();
        this.revision = 1;
        this.executionObservers = new ArrayList();
        this.registeredVariableListeners = new ArrayList();
    }

    public ExecutionEntity createExecution() {
        ExecutionEntity createdExecution = createNewExecution();
        createdExecution.setSequenceCounter(this.getSequenceCounter());
        createdExecution.setParent(this);
        createdExecution.setProcessDefinition(this.getProcessDefinition());
        createdExecution.setProcessInstance(this.getProcessInstance());
        createdExecution.setActivity(this.getActivity());
        createdExecution.setSuspensionState(this.getSuspensionState());
        createdExecution.activityInstanceId = this.activityInstanceId;
        if (this.tenantId != null) {
            createdExecution.setTenantId(this.tenantId);
        }

        createdExecution.setStartContext(this.scopeInstantiationContext);
        createdExecution.skipCustomListeners = this.skipCustomListeners;
        createdExecution.skipIoMapping = this.skipIoMapping;
        LOG.createChildExecution(createdExecution, this);
        return createdExecution;
    }

    public ExecutionEntity createSubProcessInstance(PvmProcessDefinition processDefinition, String businessKey, String caseInstanceId) {
        this.shouldQueryForSubprocessInstance = true;
        ExecutionEntity subProcessInstance = (ExecutionEntity) super.createSubProcessInstance(processDefinition, businessKey, caseInstanceId);
        String tenantId = ((ProcessDefinitionEntity) processDefinition).getTenantId();
        if (tenantId != null) {
            subProcessInstance.setTenantId(tenantId);
        } else {
            subProcessInstance.setTenantId(this.tenantId);
        }

        this.fireHistoricActivityInstanceUpdate();
        return subProcessInstance;
    }

    protected static ExecutionEntity createNewExecution() {
        ExecutionEntity newExecution = new ExecutionEntity();
        initializeAssociations(newExecution);
        newExecution.insert();
        return newExecution;
    }

    protected PvmExecutionImpl newExecution() {
        return createNewExecution();
    }

    public CaseExecutionEntity createSubCaseInstance(CmmnCaseDefinition caseDefinition) {
        return this.createSubCaseInstance(caseDefinition, (String) null);
    }

    public CaseExecutionEntity createSubCaseInstance(CmmnCaseDefinition caseDefinition, String businessKey) {
        CaseExecutionEntity subCaseInstance = (CaseExecutionEntity) caseDefinition.createCaseInstance(businessKey);
        String tenantId = ((CaseDefinitionEntity) caseDefinition).getTenantId();
        if (tenantId != null) {
            subCaseInstance.setTenantId(tenantId);
        } else {
            subCaseInstance.setTenantId(this.tenantId);
        }

        subCaseInstance.setSuperExecution(this);
        this.setSubCaseInstance(subCaseInstance);
        this.fireHistoricActivityInstanceUpdate();
        return subCaseInstance;
    }

    public void fireHistoricActivityInstanceUpdate() {
        ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        HistoryLevel historyLevel = configuration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.ACTIVITY_INSTANCE_UPDATE, this)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                public HistoryEvent createHistoryEvent(HistoryEventProducer producer) {
                    return producer.createActivityInstanceUpdateEvt(ExecutionEntity.this);
                }
            });
        }

    }

    public void initialize() {
        LOG.initializeExecution(this);
        ScopeImpl scope = this.getScopeActivity();
        this.ensureParentInitialized();
        List<VariableDeclaration> variableDeclarations = (List) scope.getProperty("variableDeclarations");
        Iterator var3;
        if (variableDeclarations != null) {
            var3 = variableDeclarations.iterator();

            while (var3.hasNext()) {
                VariableDeclaration variableDeclaration = (VariableDeclaration) var3.next();
                variableDeclaration.initialize(this, this.parent);
            }
        }

        if (this.isProcessInstanceExecution()) {
            String initiatorVariableName = (String) this.processDefinition.getProperty("initiatorVariableName");
            if (initiatorVariableName != null) {
                String authenticatedUserId = Context.getCommandContext().getAuthenticatedUserId();
                this.setVariable(initiatorVariableName, authenticatedUserId);
            }
        }

        var3 = EventSubscriptionDeclaration.getDeclarationsForScope(scope).values().iterator();

        while (var3.hasNext()) {
            EventSubscriptionDeclaration declaration = (EventSubscriptionDeclaration) var3.next();
            if (!declaration.isStartEvent()) {
                declaration.createSubscriptionForExecution(this);
            }
        }

    }

    public void initializeTimerDeclarations() {
        LOG.initializeTimerDeclaration(this);
        ScopeImpl scope = this.getScopeActivity();
        this.createTimerInstances(TimerDeclarationImpl.getDeclarationsForScope(scope).values());
        Iterator var2 = TimerDeclarationImpl.getTimeoutListenerDeclarationsForScope(scope).values().iterator();

        while (var2.hasNext()) {
            Map<String, TimerDeclarationImpl> timerDeclarations = (Map) var2.next();
            this.createTimerInstances(timerDeclarations.values());
        }

    }

    protected void createTimerInstances(Collection<TimerDeclarationImpl> timerDeclarations) {
        Iterator var2 = timerDeclarations.iterator();

        while (var2.hasNext()) {
            TimerDeclarationImpl timerDeclaration = (TimerDeclarationImpl) var2.next();
            timerDeclaration.createTimerInstance(this);
        }

    }

    protected static void initializeAssociations(ExecutionEntity execution) {
        execution.executions = new ArrayList();
        execution.variableStore.setVariablesProvider(VariableCollectionProvider.emptyVariables());
        execution.variableStore.forceInitialization();
        execution.eventSubscriptions = new ArrayList();
        execution.jobs = new ArrayList();
        execution.tasks = new ArrayList();
        execution.externalTasks = new ArrayList();
        execution.incidents = new ArrayList();
        execution.cachedEntityState = 0;
    }

    public void start(Map<String, Object> variables, VariableMap formProperties) {
        if (this.getSuperExecution() == null) {
            this.setRootProcessInstanceId(this.processInstanceId);
        } else {
            ExecutionEntity superExecution = this.getSuperExecution();
            this.setRootProcessInstanceId(superExecution.getRootProcessInstanceId());
        }

        this.provideTenantId(variables, formProperties);
        super.start(variables, formProperties);
    }

    public void startWithoutExecuting(Map<String, Object> variables) {
        this.setRootProcessInstanceId(this.getProcessInstanceId());
        this.provideTenantId(variables, (VariableMap) null);
        super.startWithoutExecuting(variables);
    }

    protected void provideTenantId(Map<String, Object> variables, VariableMap properties) {
        if (this.tenantId == null) {
            TenantIdProvider tenantIdProvider = Context.getProcessEngineConfiguration().getTenantIdProvider();
            if (tenantIdProvider != null) {
                VariableMap variableMap = Variables.fromMap(variables);
                if (properties != null && !properties.isEmpty()) {
                    variableMap.putAll(properties);
                }

                ProcessDefinition processDefinition = this.getProcessDefinition();
                TenantIdProviderProcessInstanceContext ctx;
                if (this.superExecutionId != null) {
                    ctx = new TenantIdProviderProcessInstanceContext(processDefinition, variableMap, this.getSuperExecution());
                } else if (this.superCaseExecutionId != null) {
                    ctx = new TenantIdProviderProcessInstanceContext(processDefinition, variableMap, this.getSuperCaseExecution());
                } else {
                    ctx = new TenantIdProviderProcessInstanceContext(processDefinition, variableMap);
                }

                this.tenantId = tenantIdProvider.provideTenantIdForProcessInstance(ctx);
            }
        }

    }

    public void fireHistoricProcessStartEvent() {
        ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        HistoryLevel historyLevel = configuration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.PROCESS_INSTANCE_START, this.processInstance)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                public HistoryEvent createHistoryEvent(HistoryEventProducer producer) {
                    return producer.createProcessInstanceStartEvt(ExecutionEntity.this.processInstance);
                }
            });
        }

    }

    public void destroy() {
        this.ensureParentInitialized();
        this.ensureActivityInitialized();
        if (this.activity != null && this.activity.getIoMapping() != null && !this.skipIoMapping) {
            this.activity.getIoMapping().executeOutputParameters(this);
        }

        this.clearExecution();
        super.destroy();
        this.removeEventSubscriptionsExceptCompensation();
    }

    public void removeAllTasks() {
        this.removeTasks((String) null);
        this.removeExternalTasks();
    }

    protected void clearExecution() {
        Iterator var1 = this.executionObservers.iterator();

        while (var1.hasNext()) {
            ExecutionObserver observer = (ExecutionObserver) var1.next();
            observer.onClear(this);
        }

        this.removeAllTasks();
        this.removeVariablesLocalInternal();
        this.removeJobs();
        this.removeIncidents();
    }

    public void removeVariablesLocalInternal() {
        Iterator var1 = this.variableStore.getVariables().iterator();

        while (var1.hasNext()) {
            VariableInstanceEntity variableInstance = (VariableInstanceEntity) var1.next();
            this.invokeVariableLifecycleListenersDelete(variableInstance, this, Collections.singletonList(this.getVariablePersistenceListener()));
            this.removeVariableInternal(variableInstance);
        }

    }

    public void interrupt(String reason, boolean skipCustomListeners, boolean skipIoMappings, boolean externallyTerminated) {
        if (this.preserveScope) {
            this.removeActivityJobs(reason);
        } else {
            this.removeJobs();
            this.removeEventSubscriptionsExceptCompensation();
        }

        this.removeTasks(reason);
        super.interrupt(reason, skipCustomListeners, skipIoMappings, externallyTerminated);
    }

    protected void removeActivityJobs(String reason) {
        if (this.activityId != null) {
            Iterator var2 = this.getJobs().iterator();

            while (var2.hasNext()) {
                JobEntity job = (JobEntity) var2.next();
                if (this.activityId.equals(job.getActivityId())) {
                    job.delete();
                    this.removeJob(job);
                }
            }
        }

    }

    public <T extends CoreExecution> void performOperation(CoreAtomicOperation<T> operation) {
        if (operation instanceof AtomicOperation) {
            this.performOperation((AtomicOperation) operation);
        } else {
            super.performOperation(operation);
        }

    }

    public <T extends CoreExecution> void performOperationSync(CoreAtomicOperation<T> operation) {
        if (operation instanceof AtomicOperation) {
            this.performOperationSync((AtomicOperation) operation);
        } else {
            super.performOperationSync(operation);
        }

    }

    public void performOperation(AtomicOperation executionOperation) {
        boolean async = !this.isIgnoreAsync() && executionOperation.isAsync(this);
        if (!async && this.requiresUnsuspendedExecution(executionOperation)) {
            this.ensureNotSuspended();
        }

        Context.getCommandInvocationContext().performOperation(executionOperation, this, async);
    }

    public void performOperationSync(AtomicOperation executionOperation) {
        if (this.requiresUnsuspendedExecution(executionOperation)) {
            this.ensureNotSuspended();
        }

        Context.getCommandInvocationContext().performOperation(executionOperation, this);
    }

    protected void ensureNotSuspended() {
        if (this.isSuspended()) {
            throw LOG.suspendedEntityException("Execution", this.id);
        }
    }

    protected boolean requiresUnsuspendedExecution(AtomicOperation executionOperation) {
        return executionOperation != PvmAtomicOperation.TRANSITION_DESTROY_SCOPE && executionOperation != PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_TAKE && executionOperation != PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_END && executionOperation != PvmAtomicOperation.TRANSITION_CREATE_SCOPE && executionOperation != PvmAtomicOperation.TRANSITION_NOTIFY_LISTENER_START && executionOperation != PvmAtomicOperation.DELETE_CASCADE && executionOperation != PvmAtomicOperation.DELETE_CASCADE_FIRE_ACTIVITY_END;
    }

    public void scheduleAtomicOperationAsync(AtomicOperationInvocation executionOperationInvocation) {
        MessageJobDeclaration messageJobDeclaration = null;
        List<MessageJobDeclaration> messageJobDeclarations = (List) this.getActivity().getProperty("messageJobDeclaration");
        if (messageJobDeclarations != null) {
            Iterator var4 = messageJobDeclarations.iterator();

            while (var4.hasNext()) {
                MessageJobDeclaration declaration = (MessageJobDeclaration) var4.next();
                if (declaration.isApplicableForOperation(executionOperationInvocation.getOperation())) {
                    messageJobDeclaration = declaration;
                    break;
                }
            }
        }

        if (messageJobDeclaration != null) {
            MessageEntity message = (MessageEntity) messageJobDeclaration.createJobInstance(executionOperationInvocation);
            Context.getCommandContext().getJobManager().send(message);
        } else {
            throw LOG.requiredAsyncContinuationException(this.getActivity().getId());
        }
    }

    public boolean isActive(String activityId) {
        return this.findExecution(activityId) != null;
    }

    public void inactivate() {
        this.isActive = false;
    }

    public void addExecutionObserver(ExecutionObserver observer) {
        this.executionObservers.add(observer);
    }

    public void removeExecutionObserver(ExecutionObserver observer) {
        this.executionObservers.remove(observer);
    }

    public List<PvmExecutionImpl> getExecutions() {
        this.ensureExecutionsInitialized();
        return this.executions;
    }

    public List<PvmExecutionImpl> getExecutionsAsCopy() {
        return new ArrayList(this.getExecutions());
    }

    protected void ensureExecutionsInitialized() {
        if (this.executions == null) {
            if (this.isExecutionTreePrefetchEnabled()) {
                this.ensureExecutionTreeInitialized();
            } else {
                this.executions = new ArrayList<PvmExecutionImpl>();
                this.executions = Context.getCommandContext().getExecutionManager().findChildExecutionsByParentExecutionId(this.id);
            }
        }

    }

    protected boolean isExecutionTreePrefetchEnabled() {
        return Context.getProcessEngineConfiguration().isExecutionTreePrefetchEnabled();
    }

    public void setExecutions(List<ExecutionEntity> executions) {
        this.executions = executions;
    }

    public String getProcessBusinessKey() {
        return this.getProcessInstance().getBusinessKey();
    }

    public ProcessDefinitionEntity getProcessDefinition() {
        this.ensureProcessDefinitionInitialized();
        return (ProcessDefinitionEntity) this.processDefinition;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }

    protected void ensureProcessDefinitionInitialized() {
        if (this.processDefinition == null && this.processDefinitionId != null) {
            ProcessDefinitionEntity deployedProcessDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(this.processDefinitionId);
            this.setProcessDefinition(deployedProcessDefinition);
        }

    }

    public void setProcessDefinition(ProcessDefinitionImpl processDefinition) {
        this.processDefinition = processDefinition;
        if (processDefinition != null) {
            this.processDefinitionId = processDefinition.getId();
        } else {
            this.processDefinitionId = null;
        }

    }

    public ExecutionEntity getProcessInstance() {
        this.ensureProcessInstanceInitialized();
        return this.processInstance;
    }

    protected void ensureProcessInstanceInitialized() {
        if (this.processInstance == null && this.processInstanceId != null) {
            if (this.id.equals(this.processInstanceId)) {
                this.processInstance = this;
            } else if (this.isExecutionTreePrefetchEnabled()) {
                this.ensureExecutionTreeInitialized();
            } else {
                this.processInstance = Context.getCommandContext().getExecutionManager().findExecutionById(this.processInstanceId);
            }
        }

    }

    public void setProcessInstance(PvmExecutionImpl processInstance) {
        this.processInstance = (ExecutionEntity) processInstance;
        if (processInstance != null) {
            this.processInstanceId = this.processInstance.getId();
        }

    }

    public boolean isProcessInstanceExecution() {
        return this.parentId == null;
    }

    public boolean isProcessInstanceStarting() {
        return this.processInstance != null && this.processInstance.isStarting;
    }

    public ActivityImpl getActivity() {
        this.ensureActivityInitialized();
        return super.getActivity();
    }

    public String getActivityId() {
        return this.activityId;
    }

    protected void ensureActivityInitialized() {
        if (this.activity == null && this.activityId != null) {
            this.setActivity(this.getProcessDefinition().findActivity(this.activityId));
        }

    }

    public void setActivity(PvmActivity activity) {
        super.setActivity(activity);
        if (activity != null) {
            this.activityId = activity.getId();
            this.activityName = (String) activity.getProperty("name");
        } else {
            this.activityId = null;
            this.activityName = null;
        }

    }

    protected String generateActivityInstanceId(String activityId) {
        if (activityId.equals(this.processDefinitionId)) {
            return this.processInstanceId;
        } else {
            String nextId = Context.getProcessEngineConfiguration().getIdGenerator().getNextId();
            String compositeId = activityId + ":" + nextId;
            return compositeId.length() > 64 ? String.valueOf(nextId) : compositeId;
        }
    }

    public ExecutionEntity getParent() {
        this.ensureParentInitialized();
        return this.parent;
    }

    protected void ensureParentInitialized() {
        if (this.parent == null && this.parentId != null) {
            if (this.isExecutionTreePrefetchEnabled()) {
                this.ensureExecutionTreeInitialized();
            } else {
                this.parent = Context.getCommandContext().getExecutionManager().findExecutionById(this.parentId);
            }
        }

    }

    public void setParentExecution(PvmExecutionImpl parent) {
        this.parent = (ExecutionEntity) parent;
        if (parent != null) {
            this.parentId = parent.getId();
        } else {
            this.parentId = null;
        }

    }

    public String getSuperExecutionId() {
        return this.superExecutionId;
    }

    public ExecutionEntity getSuperExecution() {
        this.ensureSuperExecutionInitialized();
        return this.superExecution;
    }

    public void setSuperExecution(PvmExecutionImpl superExecution) {
        if (this.superExecutionId != null) {
            this.ensureSuperExecutionInitialized();
            this.superExecution.setSubProcessInstance((PvmExecutionImpl) null);
        }

        this.superExecution = (ExecutionEntity) superExecution;
        if (superExecution != null) {
            this.superExecutionId = superExecution.getId();
            this.superExecution.setSubProcessInstance(this);
        } else {
            this.superExecutionId = null;
        }

    }

    protected void ensureSuperExecutionInitialized() {
        if (this.superExecution == null && this.superExecutionId != null) {
            this.superExecution = Context.getCommandContext().getExecutionManager().findExecutionById(this.superExecutionId);
        }

    }

    public ExecutionEntity getSubProcessInstance() {
        this.ensureSubProcessInstanceInitialized();
        return this.subProcessInstance;
    }

    public void setSubProcessInstance(PvmExecutionImpl subProcessInstance) {
        this.shouldQueryForSubprocessInstance = subProcessInstance != null;
        this.subProcessInstance = (ExecutionEntity) subProcessInstance;
    }

    protected void ensureSubProcessInstanceInitialized() {
        if (this.shouldQueryForSubprocessInstance && this.subProcessInstance == null) {
            this.subProcessInstance = Context.getCommandContext().getExecutionManager().findSubProcessInstanceBySuperExecutionId(this.id);
        }

    }

    public String getSuperCaseExecutionId() {
        return this.superCaseExecutionId;
    }

    public void setSuperCaseExecutionId(String superCaseExecutionId) {
        this.superCaseExecutionId = superCaseExecutionId;
    }

    public CaseExecutionEntity getSuperCaseExecution() {
        this.ensureSuperCaseExecutionInitialized();
        return this.superCaseExecution;
    }

    public void setSuperCaseExecution(CmmnExecution superCaseExecution) {
        this.superCaseExecution = (CaseExecutionEntity) superCaseExecution;
        if (superCaseExecution != null) {
            this.superCaseExecutionId = superCaseExecution.getId();
            this.caseInstanceId = superCaseExecution.getCaseInstanceId();
        } else {
            this.superCaseExecutionId = null;
            this.caseInstanceId = null;
        }

    }

    protected void ensureSuperCaseExecutionInitialized() {
        if (this.superCaseExecution == null && this.superCaseExecutionId != null) {
            this.superCaseExecution = Context.getCommandContext().getCaseExecutionManager().findCaseExecutionById(this.superCaseExecutionId);
        }

    }

    public CaseExecutionEntity getSubCaseInstance() {
        this.ensureSubCaseInstanceInitialized();
        return this.subCaseInstance;
    }

    public void setSubCaseInstance(CmmnExecution subCaseInstance) {
        this.shouldQueryForSubCaseInstance = subCaseInstance != null;
        this.subCaseInstance = (CaseExecutionEntity) subCaseInstance;
    }

    protected void ensureSubCaseInstanceInitialized() {
        if (this.shouldQueryForSubCaseInstance && this.subCaseInstance == null) {
            this.subCaseInstance = Context.getCommandContext().getCaseExecutionManager().findSubCaseInstanceBySuperExecutionId(this.id);
        }

    }

    public void remove() {
        super.remove();
        this.clearExecution();
        this.removeEventSubscriptions();
        Context.getCommandContext().getExecutionManager().deleteExecution(this);
    }

    protected void removeEventSubscriptionsExceptCompensation() {
        List<EventSubscriptionEntity> eventSubscriptions = this.getEventSubscriptions();
        Iterator var2 = eventSubscriptions.iterator();

        while (var2.hasNext()) {
            EventSubscriptionEntity eventSubscriptionEntity = (EventSubscriptionEntity) var2.next();
            if (!EventType.COMPENSATE.name().equals(eventSubscriptionEntity.getEventType())) {
                eventSubscriptionEntity.delete();
            }
        }

    }

    public void removeEventSubscriptions() {
        Iterator var1 = this.getEventSubscriptions().iterator();

        while (var1.hasNext()) {
            EventSubscriptionEntity eventSubscription = (EventSubscriptionEntity) var1.next();
            if (this.getReplacedBy() != null) {
                eventSubscription.setExecution(this.getReplacedBy());
            } else {
                eventSubscription.delete();
            }
        }

    }

    private void removeJobs() {
        Iterator var1 = this.getJobs().iterator();

        while (var1.hasNext()) {
            Job job = (Job) var1.next();
            if (this.isReplacedByParent()) {
                ((JobEntity) job).setExecution(this.getReplacedBy());
            } else {
                ((JobEntity) job).delete();
            }
        }

    }

    private void removeIncidents() {
        Iterator var1 = this.getIncidents().iterator();

        IncidentEntity incident;
        while (var1.hasNext()) {
            incident = (IncidentEntity) var1.next();
            if (this.isReplacedByParent()) {
                incident.setExecution(this.getReplacedBy());
            } else {
                IncidentContext incidentContext = this.createIncidentContext(incident.getConfiguration());
                IncidentHandling.removeIncidents(incident.getIncidentType(), incidentContext, false);
            }
        }

        var1 = this.getIncidents().iterator();

        while (var1.hasNext()) {
            incident = (IncidentEntity) var1.next();
            incident.delete();
        }

    }

    protected void removeTasks(String reason) {
        if (reason == null) {
            reason = "deleted";
        }

        Iterator var2 = this.getTasks().iterator();

        while (true) {
            TaskEntity task;
            label28:
            do {
                while (var2.hasNext()) {
                    task = (TaskEntity) var2.next();
                    if (this.isReplacedByParent()) {
                        continue label28;
                    }

                    task.delete(reason, false, this.skipCustomListeners);
                }

                return;
            } while (task.getExecution() != null && task.getExecution() == this.replacedBy);

            task.setExecution(this.replacedBy);
            this.getReplacedBy().addTask(task);
        }
    }

    protected void removeExternalTasks() {
        Iterator var1 = this.getExternalTasks().iterator();

        while (var1.hasNext()) {
            ExternalTaskEntity externalTask = (ExternalTaskEntity) var1.next();
            externalTask.delete();
        }

    }

    public ExecutionEntity getReplacedBy() {
        return (ExecutionEntity) this.replacedBy;
    }

    public ExecutionEntity resolveReplacedBy() {
        return (ExecutionEntity) super.resolveReplacedBy();
    }

    public void replace(PvmExecutionImpl execution) {
        ExecutionEntity replacedExecution = (ExecutionEntity) execution;
        this.setListenerIndex(replacedExecution.getListenerIndex());
        replacedExecution.setListenerIndex(0);
        replacedExecution.moveTasksTo(this);
        replacedExecution.moveExternalTasksTo(this);
        replacedExecution.moveActivityLocalJobsTo(this);
        if (!replacedExecution.isEnded()) {
            if (replacedExecution.getParent() == this) {
                replacedExecution.moveVariablesTo(this);
            } else {
                replacedExecution.moveConcurrentLocalVariablesTo(this);
            }
        }

        super.replace(replacedExecution);
    }

    public void onConcurrentExpand(PvmExecutionImpl scopeExecution) {
        ExecutionEntity scopeExecutionEntity = (ExecutionEntity) scopeExecution;
        scopeExecutionEntity.moveConcurrentLocalVariablesTo(this);
        super.onConcurrentExpand(scopeExecutionEntity);
    }

    protected void moveTasksTo(ExecutionEntity other) {
        Iterator var2 = this.getTasksInternal().iterator();

        while (var2.hasNext()) {
            TaskEntity task = (TaskEntity) var2.next();
            task.setExecution(other);
            Collection<VariableInstanceEntity> variables = task.getVariablesInternal();
            Iterator var5 = variables.iterator();

            while (var5.hasNext()) {
                VariableInstanceEntity variable = (VariableInstanceEntity) var5.next();
                variable.setExecution(other);
            }

            other.addTask(task);
        }

        this.getTasksInternal().clear();
    }

    protected void moveExternalTasksTo(ExecutionEntity other) {
        Iterator var2 = this.getExternalTasksInternal().iterator();

        while (var2.hasNext()) {
            ExternalTaskEntity externalTask = (ExternalTaskEntity) var2.next();
            externalTask.setExecutionId(other.getId());
            externalTask.setExecution(other);
            other.addExternalTask(externalTask);
        }

        this.getExternalTasksInternal().clear();
    }

    protected void moveActivityLocalJobsTo(ExecutionEntity other) {
        if (this.activityId != null) {
            Iterator var2 = this.getJobs().iterator();

            while (var2.hasNext()) {
                JobEntity job = (JobEntity) var2.next();
                if (this.activityId.equals(job.getActivityId())) {
                    this.removeJob(job);
                    job.setExecution(other);
                }
            }
        }

    }

    protected void moveVariablesTo(ExecutionEntity other) {
        List<VariableInstanceEntity> variables = this.variableStore.getVariables();
        this.variableStore.removeVariables();
        Iterator var3 = variables.iterator();

        while (var3.hasNext()) {
            VariableInstanceEntity variable = (VariableInstanceEntity) var3.next();
            this.moveVariableTo(variable, other);
        }

    }

    protected void moveVariableTo(VariableInstanceEntity variable, ExecutionEntity other) {
        if (other.variableStore.containsKey(variable.getName())) {
            CoreVariableInstance existingInstance = other.variableStore.getVariable(variable.getName());
            existingInstance.setValue(variable.getTypedValue(false));
            this.invokeVariableLifecycleListenersUpdate(existingInstance, this);
            this.invokeVariableLifecycleListenersDelete(variable, this, Collections.singletonList(this.getVariablePersistenceListener()));
        } else {
            other.variableStore.addVariable(variable);
        }

    }

    protected void moveConcurrentLocalVariablesTo(ExecutionEntity other) {
        List<VariableInstanceEntity> variables = this.variableStore.getVariables();
        Iterator var3 = variables.iterator();

        while (var3.hasNext()) {
            VariableInstanceEntity variable = (VariableInstanceEntity) var3.next();
            if (variable.isConcurrentLocal()) {
                this.moveVariableTo(variable, other);
            }
        }

    }

    public void addVariableListener(VariableInstanceLifecycleListener<VariableInstanceEntity> listener) {
        this.registeredVariableListeners.add(listener);
    }

    public void removeVariableListener(VariableInstanceLifecycleListener<VariableInstanceEntity> listener) {
        this.registeredVariableListeners.remove(listener);
    }

    public Collection<VariableInstanceEntity> provideVariables() {
        return Context.getCommandContext().getVariableInstanceManager().findVariableInstancesByExecutionId(this.id);
    }

    public Collection<VariableInstanceEntity> provideVariables(Collection<String> variableNames) {
        return Context.getCommandContext().getVariableInstanceManager().findVariableInstancesByExecutionIdAndVariableNames(this.id, variableNames);
    }

    protected void ensureExecutionTreeInitialized() {
        List<ExecutionEntity> executions = Context.getCommandContext().getExecutionManager().findExecutionsByProcessInstanceId(this.processInstanceId);
        ExecutionEntity processInstance = this.isProcessInstanceExecution() ? this : null;
        if (processInstance == null) {
            Iterator var3 = executions.iterator();

            while (var3.hasNext()) {
                ExecutionEntity execution = (ExecutionEntity) var3.next();
                if (execution.isProcessInstanceExecution()) {
                    processInstance = execution;
                }
            }
        }

        processInstance.restoreProcessInstance(executions, (Collection) null, (Collection) null, (Collection) null, (Collection) null, (Collection) null, (Collection) null);
    }

    public void restoreProcessInstance(Collection<ExecutionEntity> executions, Collection<EventSubscriptionEntity> eventSubscriptions, Collection<VariableInstanceEntity> variables, Collection<TaskEntity> tasks, Collection<JobEntity> jobs, Collection<IncidentEntity> incidents, Collection<ExternalTaskEntity> externalTasks) {
        if (!this.isProcessInstanceExecution()) {
            throw LOG.restoreProcessInstanceException(this);
        } else {
            Map<String, ExecutionEntity> executionsMap = new HashMap();
            Iterator var9 = executions.iterator();

            while (var9.hasNext()) {
                ExecutionEntity execution = (ExecutionEntity) var9.next();
                executionsMap.put(execution.getId(), execution);
            }

            Map<String, List<VariableInstanceEntity>> variablesByScope = new HashMap();
            Iterator var15;
            if (variables != null) {
                var15 = variables.iterator();

                while (var15.hasNext()) {
                    VariableInstanceEntity variable = (VariableInstanceEntity) var15.next();
                    CollectionUtil.addToMapOfLists(variablesByScope, variable.getVariableScopeId(), variable);
                }
            }

            var15 = executions.iterator();

            while (var15.hasNext()) {
                ExecutionEntity execution = (ExecutionEntity) var15.next();
                if (execution.executions == null) {
                    execution.executions = new ArrayList();
                }

                if (execution.eventSubscriptions == null && eventSubscriptions != null) {
                    execution.eventSubscriptions = new ArrayList();
                }

                if (variables != null) {
                    execution.variableStore.setVariablesProvider(new VariableCollectionProvider((Collection) variablesByScope.get(execution.id)));
                }

                String parentId = execution.getParentId();
                ExecutionEntity parent = (ExecutionEntity) executionsMap.get(parentId);
                if (!execution.isProcessInstanceExecution()) {
                    if (parent == null) {
                        throw LOG.resolveParentOfExecutionFailedException(parentId, execution.getId());
                    }

                    execution.processInstance = this;
                    execution.parent = parent;
                    if (parent.executions == null) {
                        parent.executions = new ArrayList();
                    }

                    parent.executions.add(execution);
                } else {
                    execution.processInstance = execution;
                }
            }

            ExecutionEntity execution;
            if (eventSubscriptions != null) {
                var15 = eventSubscriptions.iterator();

                while (var15.hasNext()) {
                    EventSubscriptionEntity eventSubscription = (EventSubscriptionEntity) var15.next();
                    execution = (ExecutionEntity) executionsMap.get(eventSubscription.getExecutionId());
                    if (execution == null) {
                        throw LOG.executionNotFoundException(eventSubscription.getExecutionId());
                    }

                    execution.addEventSubscription(eventSubscription);
                }
            }

            if (jobs != null) {
                var15 = jobs.iterator();

                while (var15.hasNext()) {
                    JobEntity job = (JobEntity) var15.next();
                    execution = (ExecutionEntity) executionsMap.get(job.getExecutionId());
                    job.setExecution(execution);
                }
            }

            if (tasks != null) {
                var15 = tasks.iterator();

                while (var15.hasNext()) {
                    TaskEntity task = (TaskEntity) var15.next();
                    execution = (ExecutionEntity) executionsMap.get(task.getExecutionId());
                    task.setExecution(execution);
                    execution.addTask(task);
                    if (variables != null) {
                        task.variableStore.setVariablesProvider(new VariableCollectionProvider((Collection) variablesByScope.get(task.id)));
                    }
                }
            }

            if (incidents != null) {
                var15 = incidents.iterator();

                while (var15.hasNext()) {
                    IncidentEntity incident = (IncidentEntity) var15.next();
                    execution = (ExecutionEntity) executionsMap.get(incident.getExecutionId());
                    incident.setExecution(execution);
                }
            }

            if (externalTasks != null) {
                var15 = externalTasks.iterator();

                while (var15.hasNext()) {
                    ExternalTaskEntity externalTask = (ExternalTaskEntity) var15.next();
                    execution = (ExecutionEntity) executionsMap.get(externalTask.getExecutionId());
                    externalTask.setExecution(execution);
                    execution.addExternalTask(externalTask);
                }
            }

        }
    }

    public Object getPersistentState() {
        Map<String, Object> persistentState = new HashMap();
        persistentState.put("processDefinitionId", this.processDefinitionId);
        persistentState.put("businessKey", this.businessKey);
        persistentState.put("activityId", this.activityId);
        persistentState.put("activityInstanceId", this.activityInstanceId);
        persistentState.put("isActive", this.isActive);
        persistentState.put("isConcurrent", this.isConcurrent);
        persistentState.put("isScope", this.isScope);
        persistentState.put("isEventScope", this.isEventScope);
        persistentState.put("parentId", this.parentId);
        persistentState.put("superExecution", this.superExecutionId);
        persistentState.put("superCaseExecutionId", this.superCaseExecutionId);
        persistentState.put("caseInstanceId", this.caseInstanceId);
        persistentState.put("suspensionState", this.suspensionState);
        persistentState.put("cachedEntityState", this.getCachedEntityState());
        persistentState.put("sequenceCounter", this.getSequenceCounter());
        return persistentState;
    }

    public void insert() {
        Context.getCommandContext().getExecutionManager().insertExecution(this);
    }

    public int getRevisionNext() {
        return this.revision + 1;
    }

    public void forceUpdate() {
        Context.getCommandContext().getDbEntityManager().forceUpdate(this);
    }

    public String toString() {
        return this.isProcessInstanceExecution() ? "ProcessInstance[" + this.getToStringIdentity() + "]" : (this.isConcurrent ? "Concurrent" : "") + (this.isScope ? "Scope" : "") + "Execution[" + this.getToStringIdentity() + "]";
    }

    protected String getToStringIdentity() {
        return this.id;
    }

    public List<EventSubscriptionEntity> getEventSubscriptionsInternal() {
        this.ensureEventSubscriptionsInitialized();
        return this.eventSubscriptions;
    }

    public List<EventSubscriptionEntity> getEventSubscriptions() {
        return new ArrayList(this.getEventSubscriptionsInternal());
    }

    public List<EventSubscriptionEntity> getCompensateEventSubscriptions() {
        List<EventSubscriptionEntity> eventSubscriptions = this.getEventSubscriptionsInternal();
        List<EventSubscriptionEntity> result = new ArrayList(eventSubscriptions.size());
        Iterator var3 = eventSubscriptions.iterator();

        while (var3.hasNext()) {
            EventSubscriptionEntity eventSubscriptionEntity = (EventSubscriptionEntity) var3.next();
            if (eventSubscriptionEntity.isSubscriptionForEventType(EventType.COMPENSATE)) {
                result.add(eventSubscriptionEntity);
            }
        }

        return result;
    }

    public List<EventSubscriptionEntity> getCompensateEventSubscriptions(String activityId) {
        List<EventSubscriptionEntity> eventSubscriptions = this.getEventSubscriptionsInternal();
        List<EventSubscriptionEntity> result = new ArrayList(eventSubscriptions.size());
        Iterator var4 = eventSubscriptions.iterator();

        while (var4.hasNext()) {
            EventSubscriptionEntity eventSubscriptionEntity = (EventSubscriptionEntity) var4.next();
            if (eventSubscriptionEntity.isSubscriptionForEventType(EventType.COMPENSATE) && activityId.equals(eventSubscriptionEntity.getActivityId())) {
                result.add(eventSubscriptionEntity);
            }
        }

        return result;
    }

    protected void ensureEventSubscriptionsInitialized() {
        if (this.eventSubscriptions == null) {
            this.eventSubscriptions = Context.getCommandContext().getEventSubscriptionManager().findEventSubscriptionsByExecution(this.id);
        }

    }

    public void addEventSubscription(EventSubscriptionEntity eventSubscriptionEntity) {
        List<EventSubscriptionEntity> eventSubscriptionsInternal = this.getEventSubscriptionsInternal();
        if (!eventSubscriptionsInternal.contains(eventSubscriptionEntity)) {
            eventSubscriptionsInternal.add(eventSubscriptionEntity);
        }

    }

    public void removeEventSubscription(EventSubscriptionEntity eventSubscriptionEntity) {
        this.getEventSubscriptionsInternal().remove(eventSubscriptionEntity);
    }

    protected void ensureJobsInitialized() {
        if (this.jobs == null) {
            this.jobs = Context.getCommandContext().getJobManager().findJobsByExecutionId(this.id);
        }

    }

    protected List<JobEntity> getJobsInternal() {
        this.ensureJobsInitialized();
        return this.jobs;
    }

    public List<JobEntity> getJobs() {
        return new ArrayList(this.getJobsInternal());
    }

    public void addJob(JobEntity jobEntity) {
        List<JobEntity> jobsInternal = this.getJobsInternal();
        if (!jobsInternal.contains(jobEntity)) {
            jobsInternal.add(jobEntity);
        }

    }

    public void removeJob(JobEntity job) {
        this.getJobsInternal().remove(job);
    }

    protected void ensureIncidentsInitialized() {
        if (this.incidents == null) {
            this.incidents = Context.getCommandContext().getIncidentManager().findIncidentsByExecution(this.id);
        }

    }

    protected List<IncidentEntity> getIncidentsInternal() {
        this.ensureIncidentsInitialized();
        return this.incidents;
    }

    public List<IncidentEntity> getIncidents() {
        return new ArrayList(this.getIncidentsInternal());
    }

    public void addIncident(IncidentEntity incident) {
        List<IncidentEntity> incidentsInternal = this.getIncidentsInternal();
        if (!incidentsInternal.contains(incident)) {
            incidentsInternal.add(incident);
        }

    }

    public void removeIncident(IncidentEntity incident) {
        this.getIncidentsInternal().remove(incident);
    }

    public IncidentEntity getIncidentByCauseIncidentId(String causeIncidentId) {
        Iterator var2 = this.getIncidents().iterator();

        IncidentEntity incident;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            incident = (IncidentEntity) var2.next();
        } while (incident.getCauseIncidentId() == null || !incident.getCauseIncidentId().equals(causeIncidentId));

        return incident;
    }

    protected void ensureTasksInitialized() {
        if (this.tasks == null) {
            this.tasks = Context.getCommandContext().getTaskManager().findTasksByExecutionId(this.id);
        }

    }

    protected List<TaskEntity> getTasksInternal() {
        this.ensureTasksInitialized();
        return this.tasks;
    }

    public List<TaskEntity> getTasks() {
        return new ArrayList(this.getTasksInternal());
    }

    public void addTask(TaskEntity taskEntity) {
        List<TaskEntity> tasksInternal = this.getTasksInternal();
        if (!tasksInternal.contains(taskEntity)) {
            tasksInternal.add(taskEntity);
        }

    }

    public void removeTask(TaskEntity task) {
        this.getTasksInternal().remove(task);
    }

    protected void ensureExternalTasksInitialized() {
        if (this.externalTasks == null) {
            this.externalTasks = Context.getCommandContext().getExternalTaskManager().findExternalTasksByExecutionId(this.id);
        }

    }

    protected List<ExternalTaskEntity> getExternalTasksInternal() {
        this.ensureExternalTasksInitialized();
        return this.externalTasks;
    }

    public void addExternalTask(ExternalTaskEntity externalTask) {
        this.getExternalTasksInternal().add(externalTask);
    }

    public void removeExternalTask(ExternalTaskEntity externalTask) {
        this.getExternalTasksInternal().remove(externalTask);
    }

    public List<ExternalTaskEntity> getExternalTasks() {
        return new ArrayList(this.getExternalTasksInternal());
    }

    protected VariableStore<CoreVariableInstance> getVariableStore() {
        return this.variableStore;
    }

    protected VariableInstanceFactory<CoreVariableInstance> getVariableInstanceFactory() {
        return VariableInstanceEntityFactory.INSTANCE;
    }

    protected List<VariableInstanceLifecycleListener<CoreVariableInstance>> getVariableInstanceLifecycleListeners() {
        List<VariableInstanceLifecycleListener<CoreVariableInstance>> listeners = new ArrayList();
        listeners.add(this.getVariablePersistenceListener());
        listeners.add(new VariableInstanceConcurrentLocalInitializer(this));
        listeners.add(VariableInstanceSequenceCounterListener.INSTANCE);
        listeners.add(VariableInstanceHistoryListener.INSTANCE);
        listeners.add(new VariableListenerInvocationListener(this));
        listeners.addAll(this.registeredVariableListeners);
        return listeners;
    }

    public VariableInstanceLifecycleListener<CoreVariableInstance> getVariablePersistenceListener() {
        return VariableInstanceEntityPersistenceListener.INSTANCE;
    }

    public Collection<VariableInstanceEntity> getVariablesInternal() {
        return this.variableStore.getVariables();
    }

    public void removeVariableInternal(VariableInstanceEntity variable) {
        if (this.variableStore.containsValue(variable)) {
            this.variableStore.removeVariable(variable.getName());
        }

    }

    public void addVariableInternal(VariableInstanceEntity variable) {
        if (this.variableStore.containsKey(variable.getName())) {
            VariableInstanceEntity existingVariable = (VariableInstanceEntity) this.variableStore.getVariable(variable.getName());
            existingVariable.setValue(variable.getTypedValue());
            variable.delete();
        } else {
            this.variableStore.addVariable(variable);
        }

    }

    public void handleConditionalEventOnVariableChange(VariableEvent variableEvent) {
        List<EventSubscriptionEntity> subScriptions = this.getEventSubscriptions();
        Iterator var3 = subScriptions.iterator();

        while (var3.hasNext()) {
            EventSubscriptionEntity subscription = (EventSubscriptionEntity) var3.next();
            if (EventType.CONDITONAL.name().equals(subscription.getEventType())) {
                subscription.processEventSync(variableEvent);
            }
        }

    }

    public void dispatchEvent(VariableEvent variableEvent) {
        final List<ExecutionEntity> execs = new ArrayList();
        (new ExecutionTopDownWalker(this)).addPreVisitor(new TreeVisitor<ExecutionEntity>() {
            public void visit(ExecutionEntity obj) {
                if (!obj.getEventSubscriptions().isEmpty() && (obj.isInState(ActivityInstanceState.DEFAULT) || !obj.getActivity().isScope())) {
                    execs.add(obj);
                }

            }
        }).walkUntil();
        Iterator var3 = execs.iterator();

        while (var3.hasNext()) {
            ExecutionEntity execution = (ExecutionEntity) var3.next();
            execution.handleConditionalEventOnVariableChange(variableEvent);
        }

    }

    public void setCachedEntityState(int cachedEntityState) {
        this.cachedEntityState = cachedEntityState;
        if (this.jobs == null && !BitMaskUtil.isBitOn(cachedEntityState, 3)) {
            this.jobs = new ArrayList();
        }

        if (this.tasks == null && !BitMaskUtil.isBitOn(cachedEntityState, 2)) {
            this.tasks = new ArrayList();
        }

        if (this.eventSubscriptions == null && !BitMaskUtil.isBitOn(cachedEntityState, 1)) {
            this.eventSubscriptions = new ArrayList();
        }

        if (this.incidents == null && !BitMaskUtil.isBitOn(cachedEntityState, 4)) {
            this.incidents = new ArrayList();
        }

        if (!this.variableStore.isInitialized() && !BitMaskUtil.isBitOn(cachedEntityState, 5)) {
            this.variableStore.setVariablesProvider(VariableCollectionProvider.emptyVariables());
            this.variableStore.forceInitialization();
        }

        if (this.externalTasks == null && !BitMaskUtil.isBitOn(cachedEntityState, 8)) {
            this.externalTasks = new ArrayList();
        }

        this.shouldQueryForSubprocessInstance = BitMaskUtil.isBitOn(cachedEntityState, 6);
        this.shouldQueryForSubCaseInstance = BitMaskUtil.isBitOn(cachedEntityState, 7);
    }

    public int getCachedEntityState() {
        this.cachedEntityState = 0;
        this.cachedEntityState = BitMaskUtil.setBit(this.cachedEntityState, 2, this.tasks == null || this.tasks.size() > 0);
        this.cachedEntityState = BitMaskUtil.setBit(this.cachedEntityState, 1, this.eventSubscriptions == null || this.eventSubscriptions.size() > 0);
        this.cachedEntityState = BitMaskUtil.setBit(this.cachedEntityState, 3, this.jobs == null || this.jobs.size() > 0);
        this.cachedEntityState = BitMaskUtil.setBit(this.cachedEntityState, 4, this.incidents == null || this.incidents.size() > 0);
        this.cachedEntityState = BitMaskUtil.setBit(this.cachedEntityState, 5, !this.variableStore.isInitialized() || !this.variableStore.isEmpty());
        this.cachedEntityState = BitMaskUtil.setBit(this.cachedEntityState, 6, this.shouldQueryForSubprocessInstance);
        this.cachedEntityState = BitMaskUtil.setBit(this.cachedEntityState, 7, this.shouldQueryForSubCaseInstance);
        this.cachedEntityState = BitMaskUtil.setBit(this.cachedEntityState, 8, this.externalTasks == null || this.externalTasks.size() > 0);
        return this.cachedEntityState;
    }

    public int getCachedEntityStateRaw() {
        return this.cachedEntityState;
    }

    public String getRootProcessInstanceId() {
        if (this.isProcessInstanceExecution()) {
            return this.rootProcessInstanceId;
        } else {
            ExecutionEntity processInstance = this.getProcessInstance();
            return processInstance.rootProcessInstanceId;
        }
    }

    public String getRootProcessInstanceIdRaw() {
        return this.rootProcessInstanceId;
    }

    public void setRootProcessInstanceId(String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    public String getProcessInstanceId() {
        return this.processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
        if (this.id.equals(processInstanceId)) {
            this.processInstance = this;
        }

    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getRevision() {
        return this.revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public void setSuperExecutionId(String superExecutionId) {
        this.superExecutionId = superExecutionId;
    }

    public Set<String> getReferencedEntityIds() {
        Set<String> referenceIds = new HashSet();
        if (this.superExecutionId != null) {
            referenceIds.add(this.superExecutionId);
        }

        if (this.parentId != null) {
            referenceIds.add(this.parentId);
        }

        return referenceIds;
    }

    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        Map<String, Class> referenceIdAndClass = new HashMap();
        if (this.superExecutionId != null) {
            referenceIdAndClass.put(this.superExecutionId, ExecutionEntity.class);
        }

        if (this.parentId != null) {
            referenceIdAndClass.put(this.parentId, ExecutionEntity.class);
        }

        if (this.processInstanceId != null) {
            referenceIdAndClass.put(this.processInstanceId, ExecutionEntity.class);
        }

        if (this.processDefinitionId != null) {
            referenceIdAndClass.put(this.processDefinitionId, ProcessDefinitionEntity.class);
        }

        return referenceIdAndClass;
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

    public String getCurrentActivityId() {
        return this.activityId;
    }

    public String getCurrentActivityName() {
        return this.activityName;
    }

    public FlowElement getBpmnModelElementInstance() {
        BpmnModelInstance bpmnModelInstance = this.getBpmnModelInstance();
        if (bpmnModelInstance != null) {
            ModelElementInstance modelElementInstance = null;
            if ("take".equals(this.eventName)) {
                modelElementInstance = bpmnModelInstance.getModelElementById(this.transition.getId());
            } else {
                modelElementInstance = bpmnModelInstance.getModelElementById(this.activityId);
            }

            try {
                return (FlowElement) modelElementInstance;
            } catch (ClassCastException var5) {
                ModelElementType elementType = modelElementInstance.getElementType();
                throw LOG.castModelInstanceException(modelElementInstance, "FlowElement", elementType.getTypeName(), elementType.getTypeNamespace(), var5);
            }
        } else {
            return null;
        }
    }

    public BpmnModelInstance getBpmnModelInstance() {
        return this.processDefinitionId != null ? Context.getProcessEngineConfiguration().getDeploymentCache().findBpmnModelInstanceForProcessDefinition(this.processDefinitionId) : null;
    }

    public ProcessEngineServices getProcessEngineServices() {
        return Context.getProcessEngineConfiguration().getProcessEngine();
    }

    public ProcessEngine getProcessEngine() {
        return Context.getProcessEngineConfiguration().getProcessEngine();
    }

    public String getProcessDefinitionTenantId() {
        return this.getProcessDefinition().getTenantId();
    }

    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
