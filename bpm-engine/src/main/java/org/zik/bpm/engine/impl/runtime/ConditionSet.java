// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import org.zik.bpm.engine.impl.ConditionEvaluationBuilderImpl;
import org.camunda.bpm.engine.variable.VariableMap;

public class ConditionSet
{
    protected final String businessKey;
    protected final String processDefinitionId;
    protected final VariableMap variables;
    protected final String tenantId;
    protected final boolean isTenantIdSet;
    
    public ConditionSet(final ConditionEvaluationBuilderImpl builder) {
        this.businessKey = builder.getBusinessKey();
        this.processDefinitionId = builder.getProcessDefinitionId();
        this.variables = builder.getVariables();
        this.tenantId = builder.getTenantId();
        this.isTenantIdSet = builder.isTenantIdSet();
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public VariableMap getVariables() {
        return this.variables;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    @Override
    public String toString() {
        return "ConditionSet [businessKey=" + this.businessKey + ", processDefinitionId=" + this.processDefinitionId + ", variables=" + this.variables + ", tenantId=" + this.tenantId + ", isTenantIdSet=" + this.isTenantIdSet + "]";
    }
}
