// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.multitenancy;

import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.repository.CaseDefinition;

public class TenantIdProviderCaseInstanceContext
{
    protected CaseDefinition caseDefinition;
    protected VariableMap variables;
    protected DelegateExecution superExecution;
    protected DelegateCaseExecution superCaseExecution;
    
    public TenantIdProviderCaseInstanceContext(final CaseDefinition caseDefinition, final VariableMap variables) {
        this.caseDefinition = caseDefinition;
        this.variables = variables;
    }
    
    public TenantIdProviderCaseInstanceContext(final CaseDefinition caseDefinition, final VariableMap variables, final DelegateExecution superExecution) {
        this(caseDefinition, variables);
        this.superExecution = superExecution;
    }
    
    public TenantIdProviderCaseInstanceContext(final CaseDefinition caseDefinition, final VariableMap variables, final DelegateCaseExecution superCaseExecution) {
        this(caseDefinition, variables);
        this.superCaseExecution = superCaseExecution;
    }
    
    public CaseDefinition getCaseDefinition() {
        return this.caseDefinition;
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
