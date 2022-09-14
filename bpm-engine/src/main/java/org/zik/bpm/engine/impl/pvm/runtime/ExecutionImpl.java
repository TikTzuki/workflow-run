// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime;

import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnCaseInstance;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.delegate.ProcessEngineServicesAware;
import org.zik.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.zik.bpm.engine.delegate.BpmnModelExecutionContext;
import org.camunda.bpm.model.bpmn.instance.FlowElement;
import java.util.Collections;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceLifecycleListener;
import org.zik.bpm.engine.impl.core.variable.scope.SimpleVariableInstance;
import org.zik.bpm.engine.impl.core.variable.scope.VariableInstanceFactory;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.core.variable.CoreVariableInstance;
import org.zik.bpm.engine.impl.core.variable.scope.VariableStore;
import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionImpl;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.zik.bpm.engine.impl.pvm.PvmProcessInstance;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import java.io.Serializable;

public class ExecutionImpl extends PvmExecutionImpl implements Serializable, ActivityExecution, PvmProcessInstance
{
    private static final long serialVersionUID = 1L;
    private static AtomicInteger idGenerator;
    protected ExecutionImpl processInstance;
    protected ExecutionImpl parent;
    protected List<ExecutionImpl> executions;
    protected ExecutionImpl superExecution;
    protected ExecutionImpl subProcessInstance;
    protected CaseExecutionImpl superCaseExecution;
    protected CaseExecutionImpl subCaseInstance;
    protected VariableStore<CoreVariableInstance> variableStore;
    
    public ExecutionImpl() {
        this.variableStore = new VariableStore<CoreVariableInstance>();
    }
    
    @Override
    public ExecutionImpl createExecution() {
        final ExecutionImpl createdExecution = this.newExecution();
        createdExecution.setSequenceCounter(this.getSequenceCounter());
        createdExecution.setParent(this);
        createdExecution.setProcessDefinition(this.getProcessDefinition());
        createdExecution.setProcessInstance(this.getProcessInstance());
        createdExecution.setActivity(this.getActivity());
        createdExecution.activityInstanceId = this.activityInstanceId;
        createdExecution.setStartContext(this.scopeInstantiationContext);
        createdExecution.skipCustomListeners = this.skipCustomListeners;
        createdExecution.skipIoMapping = this.skipIoMapping;
        return createdExecution;
    }
    
    @Override
    protected ExecutionImpl newExecution() {
        return new ExecutionImpl();
    }
    
    @Override
    public void initialize() {
    }
    
    @Override
    public void initializeTimerDeclarations() {
    }
    
    @Override
    public ExecutionImpl getParent() {
        return this.parent;
    }
    
    @Override
    public void setParentExecution(final PvmExecutionImpl parent) {
        this.parent = (ExecutionImpl)parent;
    }
    
    @Override
    public List<ExecutionImpl> getExecutionsAsCopy() {
        return new ArrayList<ExecutionImpl>(this.getExecutions());
    }
    
    @Override
    public List<ExecutionImpl> getExecutions() {
        if (this.executions == null) {
            this.executions = new ArrayList<ExecutionImpl>();
        }
        return this.executions;
    }
    
    @Override
    public ExecutionImpl getSuperExecution() {
        return this.superExecution;
    }
    
    @Override
    public void setSuperExecution(final PvmExecutionImpl superExecution) {
        this.superExecution = (ExecutionImpl)superExecution;
        if (superExecution != null) {
            superExecution.setSubProcessInstance(null);
        }
    }
    
    @Override
    public ExecutionImpl getSubProcessInstance() {
        return this.subProcessInstance;
    }
    
    @Override
    public void setSubProcessInstance(final PvmExecutionImpl subProcessInstance) {
        this.subProcessInstance = (ExecutionImpl)subProcessInstance;
    }
    
    @Override
    public CaseExecutionImpl getSuperCaseExecution() {
        return this.superCaseExecution;
    }
    
    @Override
    public void setSuperCaseExecution(final CmmnExecution superCaseExecution) {
        this.superCaseExecution = (CaseExecutionImpl)superCaseExecution;
    }
    
    @Override
    public CaseExecutionImpl getSubCaseInstance() {
        return this.subCaseInstance;
    }
    
    @Override
    public void setSubCaseInstance(final CmmnExecution subCaseInstance) {
        this.subCaseInstance = (CaseExecutionImpl)subCaseInstance;
    }
    
    @Override
    public CaseExecutionImpl createSubCaseInstance(final CmmnCaseDefinition caseDefinition) {
        return this.createSubCaseInstance(caseDefinition, null);
    }
    
    @Override
    public CaseExecutionImpl createSubCaseInstance(final CmmnCaseDefinition caseDefinition, final String businessKey) {
        final CaseExecutionImpl caseInstance = (CaseExecutionImpl)caseDefinition.createCaseInstance(businessKey);
        this.subCaseInstance.setSuperExecution(this);
        this.setSubCaseInstance(this.subCaseInstance);
        return caseInstance;
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinition.getId();
    }
    
    @Override
    public ExecutionImpl getProcessInstance() {
        return this.processInstance;
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.getProcessInstance().getId();
    }
    
    @Override
    public String getBusinessKey() {
        return this.getProcessInstance().getBusinessKey();
    }
    
    @Override
    public void setBusinessKey(final String businessKey) {
        this.businessKey = businessKey;
    }
    
    @Override
    public String getProcessBusinessKey() {
        return this.getProcessInstance().getBusinessKey();
    }
    
    @Override
    public void setProcessInstance(final PvmExecutionImpl processInstance) {
        this.processInstance = (ExecutionImpl)processInstance;
    }
    
    @Override
    protected String generateActivityInstanceId(final String activityId) {
        final int nextId = ExecutionImpl.idGenerator.incrementAndGet();
        final String compositeId = activityId + ":" + nextId;
        if (compositeId.length() > 64) {
            return String.valueOf(nextId);
        }
        return compositeId;
    }
    
    @Override
    public String toString() {
        if (this.isProcessInstanceExecution()) {
            return "ProcessInstance[" + this.getToStringIdentity() + "]";
        }
        return (this.isEventScope ? "EventScope" : "") + (this.isConcurrent ? "Concurrent" : "") + (this.isScope() ? "Scope" : "") + "Execution[" + this.getToStringIdentity() + "]";
    }
    
    @Override
    protected String getToStringIdentity() {
        return Integer.toString(System.identityHashCode(this));
    }
    
    @Override
    public String getId() {
        return String.valueOf(System.identityHashCode(this));
    }
    
    @Override
    protected VariableStore<CoreVariableInstance> getVariableStore() {
        return this.variableStore;
    }
    
    @Override
    protected VariableInstanceFactory<CoreVariableInstance> getVariableInstanceFactory() {
        return (VariableInstanceFactory<CoreVariableInstance>)SimpleVariableInstance.SimpleVariableInstanceFactory.INSTANCE;
    }
    
    @Override
    protected List<VariableInstanceLifecycleListener<CoreVariableInstance>> getVariableInstanceLifecycleListeners() {
        return Collections.emptyList();
    }
    
    @Override
    public ExecutionImpl getReplacedBy() {
        return (ExecutionImpl)this.replacedBy;
    }
    
    public void setExecutions(final List<ExecutionImpl> executions) {
        this.executions = executions;
    }
    
    @Override
    public String getCurrentActivityName() {
        String currentActivityName = null;
        if (this.activity != null) {
            currentActivityName = (String)this.activity.getProperty("name");
        }
        return currentActivityName;
    }
    
    @Override
    public FlowElement getBpmnModelElementInstance() {
        throw new UnsupportedOperationException(BpmnModelExecutionContext.class.getName() + " is unsupported in transient ExecutionImpl");
    }
    
    @Override
    public BpmnModelInstance getBpmnModelInstance() {
        throw new UnsupportedOperationException(BpmnModelExecutionContext.class.getName() + " is unsupported in transient ExecutionImpl");
    }
    
    @Override
    public ProcessEngineServices getProcessEngineServices() {
        throw new UnsupportedOperationException(ProcessEngineServicesAware.class.getName() + " is unsupported in transient ExecutionImpl");
    }
    
    @Override
    public ProcessEngine getProcessEngine() {
        throw new UnsupportedOperationException(ProcessEngineServicesAware.class.getName() + " is unsupported in transient ExecutionImpl");
    }
    
    @Override
    public void forceUpdate() {
    }
    
    @Override
    public void fireHistoricProcessStartEvent() {
    }
    
    @Override
    protected void removeVariablesLocalInternal() {
    }
    
    static {
        ExecutionImpl.idGenerator = new AtomicInteger();
    }
}
