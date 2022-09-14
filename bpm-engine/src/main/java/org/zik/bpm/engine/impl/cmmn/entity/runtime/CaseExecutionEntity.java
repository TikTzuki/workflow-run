// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.runtime;

import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.camunda.bpm.model.cmmn.instance.CmmnElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.camunda.bpm.model.xml.type.ModelElementType;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.ProcessEngineServices;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.cfg.multitenancy.TenantIdProvider;
import org.zik.bpm.engine.impl.cfg.multitenancy.TenantIdProviderCaseInstanceContext;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnSentryPart;
import org.zik.bpm.engine.impl.cmmn.model.CmmnActivity;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.impl.cmmn.operation.CmmnAtomicOperation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.core.variable.scope.*;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.zik.bpm.engine.impl.history.producer.CmmnHistoryEventProducer;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.*;
import org.zik.bpm.engine.impl.pvm.PvmProcessDefinition;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.task.TaskDecorator;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.runtime.CaseExecution;
import org.zik.bpm.engine.runtime.CaseInstance;

import java.util.*;


public class CaseExecutionEntity extends CmmnExecution implements CaseExecution, CaseInstance, DbEntity, HasDbRevision, HasDbReferences, VariableStore.VariablesProvider<VariableInstanceEntity> {
    private static final long serialVersionUID = 1L;
    protected transient CaseExecutionEntity caseInstance;
    protected transient CaseExecutionEntity parent;
    protected List<CaseExecutionEntity> caseExecutions;
    protected List<CaseSentryPartEntity> caseSentryParts;
    protected Map<String, List<CmmnSentryPart>> sentries;
    protected transient ExecutionEntity subProcessInstance;
    protected transient ExecutionEntity superExecution;
    protected transient CaseExecutionEntity subCaseInstance;
    protected transient CaseExecutionEntity superCaseExecution;
    protected VariableStore<VariableInstanceEntity> variableStore = new VariableStore(this, new VariableStore.VariableStoreObserver[]{new CaseExecutionEntityReferencer(this)});
    protected int revision = 1;
    protected String caseDefinitionId;
    protected String activityId;
    protected String caseInstanceId;
    protected String parentId;
    protected String superCaseExecutionId;
    protected String superExecutionId;
    protected String activityName;
    protected String activityType;
    protected String activityDescription;

    public CaseExecutionEntity() {
    }

    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }

    public CmmnCaseDefinition getCaseDefinition() {
        this.ensureCaseDefinitionInitialized();
        return this.caseDefinition;
    }

    public void setCaseDefinition(CmmnCaseDefinition caseDefinition) {
        super.setCaseDefinition(caseDefinition);
        this.caseDefinitionId = null;
        if (caseDefinition != null) {
            this.caseDefinitionId = caseDefinition.getId();
        }

    }

    protected void ensureCaseDefinitionInitialized() {
        if (this.caseDefinition == null && this.caseDefinitionId != null) {
            CaseDefinitionEntity deployedCaseDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().getCaseDefinitionById(this.caseDefinitionId);
            this.setCaseDefinition(deployedCaseDefinition);
        }

    }

    public CaseExecutionEntity getParent() {
        this.ensureParentInitialized();
        return this.parent;
    }

    public void setParent(CmmnExecution parent) {
        this.parent = (CaseExecutionEntity) parent;
        if (parent != null) {
            this.parentId = parent.getId();
        } else {
            this.parentId = null;
        }

    }

    protected void ensureParentInitialized() {
        if (this.parent == null && this.parentId != null) {
            if (this.isExecutionTreePrefetchEnabled()) {
                this.ensureCaseExecutionTreeInitialized();
            } else {
                this.parent = Context.getCommandContext().getCaseExecutionManager().findCaseExecutionById(this.parentId);
            }
        }

    }

    protected void ensureCaseExecutionTreeInitialized() {
        List<CaseExecutionEntity> executions = Context.getCommandContext().getCaseExecutionManager().findChildCaseExecutionsByCaseInstanceId(this.caseInstanceId);
        CaseExecutionEntity caseInstance = null;
        Map<String, CaseExecutionEntity> executionMap = new HashMap();
        Iterator var4 = executions.iterator();

        CaseExecutionEntity execution;
        while (var4.hasNext()) {
            execution = (CaseExecutionEntity) var4.next();
            execution.caseExecutions = new ArrayList();
            executionMap.put(execution.getId(), execution);
            if (execution.isCaseInstanceExecution()) {
                caseInstance = execution;
            }
        }

        var4 = executions.iterator();

        while (var4.hasNext()) {
            execution = (CaseExecutionEntity) var4.next();
            String parentId = execution.getParentId();
            CaseExecutionEntity parent = (CaseExecutionEntity) executionMap.get(parentId);
            if (!execution.isCaseInstanceExecution()) {
                execution.caseInstance = caseInstance;
                execution.parent = parent;
                parent.caseExecutions.add(execution);
            } else {
                execution.caseInstance = execution;
            }
        }

    }

    protected boolean isExecutionTreePrefetchEnabled() {
        return Context.getProcessEngineConfiguration().isExecutionTreePrefetchEnabled();
    }

    public String getParentId() {
        return this.parentId;
    }

    public CmmnActivity getActivity() {
        this.ensureActivityInitialized();
        return super.getActivity();
    }

    public void setActivity(CmmnActivity activity) {
        super.setActivity(activity);
        if (activity != null) {
            this.activityId = activity.getId();
            this.activityName = activity.getName();
            this.activityType = this.getActivityProperty(activity, "activityType");
            this.activityDescription = this.getActivityProperty(activity, "description");
        } else {
            this.activityId = null;
            this.activityName = null;
            this.activityType = null;
            this.activityDescription = null;
        }

    }

    protected void ensureActivityInitialized() {
        if (this.activity == null && this.activityId != null) {
            this.setActivity(this.getCaseDefinition().findActivity(this.activityId));
        }

    }

    protected String getActivityProperty(CmmnActivity activity, String property) {
        String result = null;
        if (activity != null) {
            Object value = activity.getProperty(property);
            if (value != null && value instanceof String) {
                result = (String) value;
            }
        }

        return result;
    }

    public String getActivityId() {
        return this.activityId;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public String getActivityType() {
        return this.activityType;
    }

    public String getActivityDescription() {
        return this.activityDescription;
    }

    public List<CaseExecutionEntity> getCaseExecutions() {
        return new ArrayList(this.getCaseExecutionsInternal());
    }

    protected List<CaseExecutionEntity> getCaseExecutionsInternal() {
        this.ensureCaseExecutionsInitialized();
        return this.caseExecutions;
    }

    protected void ensureCaseExecutionsInitialized() {
        if (this.caseExecutions == null) {
            this.caseExecutions = Context.getCommandContext().getCaseExecutionManager().findChildCaseExecutionsByParentCaseExecutionId(this.id);
        }

    }

    public TaskEntity getTask() {
        this.ensureTaskInitialized();
        return this.task;
    }

    protected void ensureTaskInitialized() {
        if (this.task == null) {
            this.task = Context.getCommandContext().getTaskManager().findTaskByCaseExecutionId(this.id);
        }

    }

    public TaskEntity createTask(TaskDecorator taskDecorator) {
        TaskEntity task = super.createTask(taskDecorator);
        this.fireHistoricCaseActivityInstanceUpdate();
        return task;
    }

    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }

    public CaseExecutionEntity getCaseInstance() {
        this.ensureCaseInstanceInitialized();
        return this.caseInstance;
    }

    public void setCaseInstance(CmmnExecution caseInstance) {
        this.caseInstance = (CaseExecutionEntity) caseInstance;
        if (caseInstance != null) {
            this.caseInstanceId = this.caseInstance.getId();
        }

    }

    protected void ensureCaseInstanceInitialized() {
        if (this.caseInstance == null && this.caseInstanceId != null) {
            this.caseInstance = Context.getCommandContext().getCaseExecutionManager().findCaseExecutionById(this.caseInstanceId);
        }

    }

    public boolean isCaseInstanceExecution() {
        return this.parentId == null;
    }

    public void create(Map<String, Object> variables) {
        if (this.tenantId == null) {
            this.provideTenantId(variables);
        }

        super.create(variables);
    }

    protected void provideTenantId(Map<String, Object> variables) {
        TenantIdProvider tenantIdProvider = Context.getProcessEngineConfiguration().getTenantIdProvider();
        if (tenantIdProvider != null) {
            VariableMap variableMap = Variables.fromMap(variables);
            CaseDefinition caseDefinition = (CaseDefinition) this.getCaseDefinition();
            TenantIdProviderCaseInstanceContext ctx = null;
            if (this.superExecutionId != null) {
                ctx = new TenantIdProviderCaseInstanceContext(caseDefinition, variableMap, this.getSuperExecution());
            } else if (this.superCaseExecutionId != null) {
                ctx = new TenantIdProviderCaseInstanceContext(caseDefinition, variableMap, this.getSuperCaseExecution());
            } else {
                ctx = new TenantIdProviderCaseInstanceContext(caseDefinition, variableMap);
            }

            this.tenantId = tenantIdProvider.provideTenantIdForCaseInstance(ctx);
        }

    }

    protected CaseExecutionEntity createCaseExecution(CmmnActivity activity) {
        CaseExecutionEntity child = this.newCaseExecution();
        child.setActivity(activity);
        child.setParent(this);
        this.getCaseExecutionsInternal().add(child);
        child.setCaseInstance(this.getCaseInstance());
        child.setCaseDefinition(this.getCaseDefinition());
        if (this.tenantId != null) {
            child.setTenantId(this.tenantId);
        }

        return child;
    }

    protected CaseExecutionEntity newCaseExecution() {
        CaseExecutionEntity newCaseExecution = new CaseExecutionEntity();
        Context.getCommandContext().getCaseExecutionManager().insertCaseExecution(newCaseExecution);
        return newCaseExecution;
    }

    public String getSuperExecutionId() {
        return this.superExecutionId;
    }

    public void setSuperExecutionId(String superProcessExecutionId) {
        this.superExecutionId = superProcessExecutionId;
    }

    public ExecutionEntity getSuperExecution() {
        this.ensureSuperExecutionInstanceInitialized();
        return this.superExecution;
    }

    public void setSuperExecution(PvmExecutionImpl superExecution) {
        if (this.superExecutionId != null) {
            this.ensureSuperExecutionInstanceInitialized();
            this.superExecution.setSubCaseInstance((CmmnExecution) null);
        }

        this.superExecution = (ExecutionEntity) superExecution;
        if (superExecution != null) {
            this.superExecutionId = superExecution.getId();
            this.superExecution.setSubCaseInstance(this);
        } else {
            this.superExecutionId = null;
        }

    }

    protected void ensureSuperExecutionInstanceInitialized() {
        if (this.superExecution == null && this.superExecutionId != null) {
            this.superExecution = Context.getCommandContext().getExecutionManager().findExecutionById(this.superExecutionId);
        }

    }

    public ExecutionEntity getSubProcessInstance() {
        this.ensureSubProcessInstanceInitialized();
        return this.subProcessInstance;
    }

    public void setSubProcessInstance(PvmExecutionImpl subProcessInstance) {
        this.subProcessInstance = (ExecutionEntity) subProcessInstance;
    }

    public ExecutionEntity createSubProcessInstance(PvmProcessDefinition processDefinition) {
        return this.createSubProcessInstance(processDefinition, (String) null);
    }

    public ExecutionEntity createSubProcessInstance(PvmProcessDefinition processDefinition, String businessKey) {
        return this.createSubProcessInstance(processDefinition, businessKey, this.getCaseInstanceId());
    }

    public ExecutionEntity createSubProcessInstance(PvmProcessDefinition processDefinition, String businessKey, String caseInstanceId) {
        ExecutionEntity subProcessInstance = (ExecutionEntity) processDefinition.createProcessInstance(businessKey, caseInstanceId);
        String tenantId = ((ProcessDefinitionEntity) processDefinition).getTenantId();
        if (tenantId != null) {
            subProcessInstance.setTenantId(tenantId);
        } else {
            subProcessInstance.setTenantId(this.tenantId);
        }

        subProcessInstance.setSuperCaseExecution(this);
        this.setSubProcessInstance(subProcessInstance);
        this.fireHistoricCaseActivityInstanceUpdate();
        return subProcessInstance;
    }

    protected void ensureSubProcessInstanceInitialized() {
        if (this.subProcessInstance == null) {
            this.subProcessInstance = Context.getCommandContext().getExecutionManager().findSubProcessInstanceBySuperCaseExecutionId(this.id);
        }

    }

    public CaseExecutionEntity getSubCaseInstance() {
        this.ensureSubCaseInstanceInitialized();
        return this.subCaseInstance;
    }

    public void setSubCaseInstance(CmmnExecution subCaseInstance) {
        this.subCaseInstance = (CaseExecutionEntity) subCaseInstance;
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

        subCaseInstance.setSuperCaseExecution(this);
        this.setSubCaseInstance(subCaseInstance);
        this.fireHistoricCaseActivityInstanceUpdate();
        return subCaseInstance;
    }

    public void fireHistoricCaseActivityInstanceUpdate() {
        ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        HistoryLevel historyLevel = configuration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.CASE_ACTIVITY_INSTANCE_UPDATE, this)) {
            CmmnHistoryEventProducer eventProducer = configuration.getCmmnHistoryEventProducer();
            HistoryEventHandler eventHandler = configuration.getHistoryEventHandler();
            HistoryEvent event = eventProducer.createCaseActivityInstanceUpdateEvt(this);
            eventHandler.handleEvent(event);
        }

    }

    protected void ensureSubCaseInstanceInitialized() {
        if (this.subCaseInstance == null) {
            this.subCaseInstance = Context.getCommandContext().getCaseExecutionManager().findSubCaseInstanceBySuperCaseExecutionId(this.id);
        }

    }

    public String getSuperCaseExecutionId() {
        return this.superCaseExecutionId;
    }

    public void setSuperCaseExecutionId(String superCaseExecutionId) {
        this.superCaseExecutionId = superCaseExecutionId;
    }

    public CmmnExecution getSuperCaseExecution() {
        this.ensureSuperCaseExecutionInitialized();
        return this.superCaseExecution;
    }

    public void setSuperCaseExecution(CmmnExecution superCaseExecution) {
        this.superCaseExecution = (CaseExecutionEntity) superCaseExecution;
        if (superCaseExecution != null) {
            this.superCaseExecutionId = superCaseExecution.getId();
        } else {
            this.superCaseExecutionId = null;
        }

    }

    protected void ensureSuperCaseExecutionInitialized() {
        if (this.superCaseExecution == null && this.superCaseExecutionId != null) {
            this.superCaseExecution = Context.getCommandContext().getCaseExecutionManager().findCaseExecutionById(this.superCaseExecutionId);
        }

    }

    public List<CaseSentryPartEntity> getCaseSentryParts() {
        this.ensureCaseSentryPartsInitialized();
        return this.caseSentryParts;
    }

    protected void ensureCaseSentryPartsInitialized() {
        if (this.caseSentryParts == null) {
            this.caseSentryParts = Context.getCommandContext().getCaseSentryPartManager().findCaseSentryPartsByCaseExecutionId(this.id);
            this.sentries = new HashMap();

            CaseSentryPartEntity sentryPart;
            List<CmmnSentryPart> parts;
            for (Iterator<CaseSentryPartEntity> var1 = this.caseSentryParts.iterator(); var1.hasNext(); parts.add(sentryPart)) {
                sentryPart = (CaseSentryPartEntity) var1.next();
                String sentryId = sentryPart.getSentryId();
                parts = this.sentries.get(sentryId);
                if (parts == null) {
                    parts = new ArrayList();
                    this.sentries.put(sentryId, parts);
                }
            }
        }

    }

    protected void addSentryPart(CmmnSentryPart sentryPart) {
        CaseSentryPartEntity entity = (CaseSentryPartEntity) sentryPart;
        this.getCaseSentryParts().add(entity);
        String sentryId = sentryPart.getSentryId();
        List<CmmnSentryPart> parts = (List) this.sentries.get(sentryId);
        if (parts == null) {
            parts = new ArrayList();
            this.sentries.put(sentryId, parts);
        }

        ((List) parts).add(entity);
    }

    protected Map<String, List<CmmnSentryPart>> getSentries() {
        this.ensureCaseSentryPartsInitialized();
        return this.sentries;
    }

    protected List<CmmnSentryPart> findSentry(String sentryId) {
        this.ensureCaseSentryPartsInitialized();
        return this.sentries.get(sentryId);
    }

    protected CaseSentryPartEntity newSentryPart() {
        CaseSentryPartEntity caseSentryPart = new CaseSentryPartEntity();
        Context.getCommandContext().getCaseSentryPartManager().insertCaseSentryPart(caseSentryPart);
        return caseSentryPart;
    }

    protected VariableStore<VariableInstanceEntity> getVariableStore() {
        return this.variableStore;
    }

    protected VariableInstanceFactory<VariableInstanceEntity> getVariableInstanceFactory() {
        return VariableInstanceEntityFactory.INSTANCE;
    }

    protected List<VariableInstanceLifecycleListener<VariableInstanceEntity>> getVariableInstanceLifecycleListeners() {
        return Arrays.asList(VariableInstanceEntityPersistenceListener.INSTANCE, VariableInstanceSequenceCounterListener.INSTANCE, VariableInstanceHistoryListener.INSTANCE, CmmnVariableInvocationListener.INSTANCE, new VariableOnPartListener(this));
    }

    public Collection<VariableInstanceEntity> provideVariables() {
        return Context.getCommandContext().getVariableInstanceManager().findVariableInstancesByCaseExecutionId(this.id);
    }

    public Collection<VariableInstanceEntity> provideVariables(Collection<String> variableNames) {
        return Context.getCommandContext().getVariableInstanceManager().findVariableInstancesByCaseExecutionIdAndVariableNames(this.id, variableNames);
    }

    public String toString() {
        return this.isCaseInstanceExecution() ? "CaseInstance[" + this.getToStringIdentity() + "]" : "CaseExecution[" + this.getToStringIdentity() + "]";
    }

    protected String getToStringIdentity() {
        return this.id;
    }

    public void remove() {
        super.remove();
        Iterator var1 = this.variableStore.getVariables().iterator();

        while (var1.hasNext()) {
            VariableInstanceEntity variableInstance = (VariableInstanceEntity) var1.next();
            this.invokeVariableLifecycleListenersDelete(variableInstance, this, Arrays.asList(VariableInstanceEntityPersistenceListener.INSTANCE));
            this.variableStore.removeVariable(variableInstance.getName());
        }

        CommandContext commandContext = Context.getCommandContext();
        Iterator var5 = this.getCaseSentryParts().iterator();

        while (var5.hasNext()) {
            CaseSentryPartEntity sentryPart = (CaseSentryPartEntity) var5.next();
            commandContext.getCaseSentryPartManager().deleteSentryPart(sentryPart);
        }

        commandContext.getCaseExecutionManager().deleteCaseExecution(this);
    }

    public int getRevision() {
        return this.revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public int getRevisionNext() {
        return this.revision + 1;
    }

    public void forceUpdate() {
        Context.getCommandContext().getDbEntityManager().forceUpdate(this);
    }

    public Set<String> getReferencedEntityIds() {
        Set<String> referenceIds = new HashSet();
        if (this.parentId != null) {
            referenceIds.add(this.parentId);
        }

        if (this.superCaseExecutionId != null) {
            referenceIds.add(this.superCaseExecutionId);
        }

        return referenceIds;
    }

    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        Map<String, Class> referenceIdAndClass = new HashMap();
        if (this.parentId != null) {
            referenceIdAndClass.put(this.parentId, CaseExecutionEntity.class);
        }

        if (this.superCaseExecutionId != null) {
            referenceIdAndClass.put(this.superCaseExecutionId, CaseExecutionEntity.class);
        }

        if (this.caseDefinitionId != null) {
            referenceIdAndClass.put(this.caseDefinitionId, CmmnCaseDefinition.class);
        }

        return referenceIdAndClass;
    }

    public Object getPersistentState() {
        Map<String, Object> persistentState = new HashMap();
        persistentState.put("caseDefinitionId", this.caseDefinitionId);
        persistentState.put("businessKey", this.businessKey);
        persistentState.put("activityId", this.activityId);
        persistentState.put("parentId", this.parentId);
        persistentState.put("currentState", this.currentState);
        persistentState.put("previousState", this.previousState);
        persistentState.put("superExecutionId", this.superExecutionId);
        return persistentState;
    }

    public CmmnModelInstance getCmmnModelInstance() {
        return this.caseDefinitionId != null ? Context.getProcessEngineConfiguration().getDeploymentCache().findCmmnModelInstanceForCaseDefinition(this.caseDefinitionId) : null;
    }

    public CmmnElement getCmmnModelElementInstance() {
        CmmnModelInstance cmmnModelInstance = this.getCmmnModelInstance();
        if (cmmnModelInstance != null) {
            ModelElementInstance modelElementInstance = cmmnModelInstance.getModelElementById(this.activityId);

            try {
                return (CmmnElement) modelElementInstance;
            } catch (ClassCastException var5) {
                ModelElementType elementType = modelElementInstance.getElementType();
                throw new ProcessEngineException("Cannot cast " + modelElementInstance + " to CmmnElement. Is of type " + elementType.getTypeName() + " Namespace " + elementType.getTypeNamespace(), var5);
            }
        } else {
            return null;
        }
    }

    public ProcessEngineServices getProcessEngineServices() {
        return Context.getProcessEngineConfiguration().getProcessEngine();
    }

    public ProcessEngine getProcessEngine() {
        return Context.getProcessEngineConfiguration().getProcessEngine();
    }

    public String getCaseDefinitionTenantId() {
        CaseDefinitionEntity caseDefinition = (CaseDefinitionEntity) this.getCaseDefinition();
        return caseDefinition.getTenantId();
    }

    public <T extends CoreExecution> void performOperation(CoreAtomicOperation<T> operation) {
        Context.getCommandContext().performOperation((CmmnAtomicOperation) operation, this);
    }

    public <T extends CoreExecution> void performOperationSync(CoreAtomicOperation<T> operation) {
        Context.getCommandContext().performOperation((CmmnAtomicOperation) operation, this);
    }
}
