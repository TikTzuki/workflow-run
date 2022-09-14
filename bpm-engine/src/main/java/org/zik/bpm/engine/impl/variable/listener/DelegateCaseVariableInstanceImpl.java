// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.listener;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.runtime.VariableInstance;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.ProcessEngineServices;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.delegate.DelegateCaseVariableInstance;

public class DelegateCaseVariableInstanceImpl implements DelegateCaseVariableInstance
{
    protected String eventName;
    protected DelegateCaseExecution sourceExecution;
    protected DelegateCaseExecution scopeExecution;
    protected String variableId;
    protected String processDefinitionId;
    protected String processInstanceId;
    protected String executionId;
    protected String caseInstanceId;
    protected String caseExecutionId;
    protected String taskId;
    protected String activityInstanceId;
    protected String tenantId;
    protected String errorMessage;
    protected String name;
    protected TypedValue value;
    
    @Override
    public String getEventName() {
        return this.eventName;
    }
    
    public void setEventName(final String eventName) {
        this.eventName = eventName;
    }
    
    @Override
    public DelegateCaseExecution getSourceExecution() {
        return this.sourceExecution;
    }
    
    public void setSourceExecution(final DelegateCaseExecution sourceExecution) {
        this.sourceExecution = sourceExecution;
    }
    
    public DelegateCaseExecution getScopeExecution() {
        return this.scopeExecution;
    }
    
    public void setScopeExecution(final DelegateCaseExecution scopeExecution) {
        this.scopeExecution = scopeExecution;
    }
    
    @Override
    public String getId() {
        return this.variableId;
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    @Override
    public String getExecutionId() {
        return this.executionId;
    }
    
    @Override
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    @Override
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    @Override
    public String getTaskId() {
        return this.taskId;
    }
    
    @Override
    public String getBatchId() {
        return null;
    }
    
    @Override
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getTypeName() {
        if (this.value != null) {
            return this.value.getType().getName();
        }
        return null;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Object getValue() {
        if (this.value != null) {
            return this.value.getValue();
        }
        return null;
    }
    
    @Override
    public TypedValue getTypedValue() {
        return this.value;
    }
    
    @Override
    public ProcessEngineServices getProcessEngineServices() {
        return Context.getProcessEngineConfiguration().getProcessEngine();
    }
    
    @Override
    public ProcessEngine getProcessEngine() {
        return Context.getProcessEngineConfiguration().getProcessEngine();
    }
    
    public static DelegateCaseVariableInstanceImpl fromVariableInstance(final VariableInstance variableInstance) {
        final DelegateCaseVariableInstanceImpl delegateInstance = new DelegateCaseVariableInstanceImpl();
        delegateInstance.variableId = variableInstance.getId();
        delegateInstance.processDefinitionId = variableInstance.getProcessDefinitionId();
        delegateInstance.processInstanceId = variableInstance.getProcessInstanceId();
        delegateInstance.executionId = variableInstance.getExecutionId();
        delegateInstance.caseExecutionId = variableInstance.getCaseExecutionId();
        delegateInstance.caseInstanceId = variableInstance.getCaseInstanceId();
        delegateInstance.taskId = variableInstance.getTaskId();
        delegateInstance.activityInstanceId = variableInstance.getActivityInstanceId();
        delegateInstance.tenantId = variableInstance.getTenantId();
        delegateInstance.errorMessage = variableInstance.getErrorMessage();
        delegateInstance.name = variableInstance.getName();
        delegateInstance.value = variableInstance.getTypedValue();
        return delegateInstance;
    }
}
