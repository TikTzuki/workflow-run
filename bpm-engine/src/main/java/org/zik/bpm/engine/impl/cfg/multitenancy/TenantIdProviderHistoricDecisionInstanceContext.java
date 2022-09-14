// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg.multitenancy;

import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.repository.DecisionDefinition;

public class TenantIdProviderHistoricDecisionInstanceContext
{
    protected DecisionDefinition decisionDefinition;
    protected DelegateExecution execution;
    protected DelegateCaseExecution caseExecution;
    
    public TenantIdProviderHistoricDecisionInstanceContext(final DecisionDefinition decisionDefinition) {
        this.decisionDefinition = decisionDefinition;
    }
    
    public TenantIdProviderHistoricDecisionInstanceContext(final DecisionDefinition decisionDefinition, final DelegateExecution execution) {
        this(decisionDefinition);
        this.execution = execution;
    }
    
    public TenantIdProviderHistoricDecisionInstanceContext(final DecisionDefinition decisionDefinition, final DelegateCaseExecution caseExecution) {
        this(decisionDefinition);
        this.caseExecution = caseExecution;
    }
    
    public DecisionDefinition getDecisionDefinition() {
        return this.decisionDefinition;
    }
    
    public DelegateExecution getExecution() {
        return this.execution;
    }
    
    public DelegateCaseExecution getCaseExecution() {
        return this.caseExecution;
    }
}
