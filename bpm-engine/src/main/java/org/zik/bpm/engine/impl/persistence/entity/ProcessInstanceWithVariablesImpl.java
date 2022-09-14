// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.runtime.ProcessInstanceWithVariables;

public class ProcessInstanceWithVariablesImpl implements ProcessInstanceWithVariables
{
    protected final ExecutionEntity executionEntity;
    protected final VariableMap variables;
    
    public ProcessInstanceWithVariablesImpl(final ExecutionEntity executionEntity, final VariableMap variables) {
        this.executionEntity = executionEntity;
        this.variables = variables;
    }
    
    public ExecutionEntity getExecutionEntity() {
        return this.executionEntity;
    }
    
    @Override
    public VariableMap getVariables() {
        return this.variables;
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.executionEntity.getProcessDefinitionId();
    }
    
    @Override
    public String getBusinessKey() {
        return this.executionEntity.getBusinessKey();
    }
    
    @Override
    public String getCaseInstanceId() {
        return this.executionEntity.getCaseInstanceId();
    }
    
    @Override
    public boolean isSuspended() {
        return this.executionEntity.isSuspended();
    }
    
    @Override
    public String getId() {
        return this.executionEntity.getId();
    }
    
    @Override
    public String getRootProcessInstanceId() {
        return this.executionEntity.getRootProcessInstanceId();
    }
    
    @Override
    public boolean isEnded() {
        return this.executionEntity.isEnded();
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.executionEntity.getProcessInstanceId();
    }
    
    @Override
    public String getTenantId() {
        return this.executionEntity.getTenantId();
    }
}
