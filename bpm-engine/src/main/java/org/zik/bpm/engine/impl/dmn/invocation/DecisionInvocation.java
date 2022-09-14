// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.invocation;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.camunda.bpm.engine.variable.context.VariableContext;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class DecisionInvocation extends DelegateInvocation
{
    protected DecisionDefinition decisionDefinition;
    protected VariableContext variableContext;
    
    public DecisionInvocation(final DecisionDefinition decisionDefinition, final VariableContext variableContext) {
        super(null, (ResourceDefinitionEntity)decisionDefinition);
        this.decisionDefinition = decisionDefinition;
        this.variableContext = variableContext;
    }
    
    @Override
    protected void invoke() throws Exception {
        final DmnEngine dmnEngine = Context.getProcessEngineConfiguration().getDmnEngine();
        this.invocationResult = dmnEngine.evaluateDecision((DmnDecision)this.decisionDefinition, this.variableContext);
    }
    
    @Override
    public DmnDecisionResult getInvocationResult() {
        return (DmnDecisionResult)super.getInvocationResult();
    }
    
    public DecisionDefinition getDecisionDefinition() {
        return this.decisionDefinition;
    }
}
