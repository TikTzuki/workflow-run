// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.multitenancy;

import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.repository.ProcessDefinition;

public class TenantIdProviderProcessInstanceContext
{
    protected ProcessDefinition processDefinition;
    protected VariableMap variables;
    protected DelegateExecution superExecution;
    protected DelegateCaseExecution superCaseExecution;
    
    public TenantIdProviderProcessInstanceContext(final ProcessDefinition processDefinition, final VariableMap variables) {
        this.processDefinition = processDefinition;
        this.variables = variables;
    }
    
    public TenantIdProviderProcessInstanceContext(final ProcessDefinition processDefinition, final VariableMap variables, final DelegateExecution superExecution) {
        this(processDefinition, variables);
        this.superExecution = superExecution;
    }
    
    public TenantIdProviderProcessInstanceContext(final ProcessDefinition processDefinition, final VariableMap variables, final DelegateCaseExecution superCaseExecution) {
        this(processDefinition, variables);
        this.superCaseExecution = superCaseExecution;
    }
    
    public ProcessDefinition getProcessDefinition() {
        return this.processDefinition;
    }
    
    public VariableMap getVariables() {
        return this.variables;
    }
    
    public DelegateExecution getSuperExecution() {
        return this.superExecution;
    }
    
    public DelegateCaseExecution getSuperCaseExecution() {
        return this.superCaseExecution;
    }
}
