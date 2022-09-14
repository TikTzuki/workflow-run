// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.dmn.DecisionEvaluationBuilderImpl;
import org.zik.bpm.engine.dmn.DecisionsEvaluationBuilder;
import org.zik.bpm.engine.impl.dmn.DecisionTableEvaluationBuilderImpl;
import org.zik.bpm.engine.dmn.DecisionEvaluationBuilder;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import java.util.Map;
import org.zik.bpm.engine.DecisionService;

public class DecisionServiceImpl extends ServiceImpl implements DecisionService
{
    @Override
    public DmnDecisionTableResult evaluateDecisionTableById(final String decisionDefinitionId, final Map<String, Object> variables) {
        return this.evaluateDecisionTableById(decisionDefinitionId).variables(variables).evaluate();
    }
    
    @Override
    public DmnDecisionTableResult evaluateDecisionTableByKey(final String decisionDefinitionKey, final Map<String, Object> variables) {
        return this.evaluateDecisionTableByKey(decisionDefinitionKey).variables(variables).evaluate();
    }
    
    @Override
    public DmnDecisionTableResult evaluateDecisionTableByKeyAndVersion(final String decisionDefinitionKey, final Integer version, final Map<String, Object> variables) {
        return this.evaluateDecisionTableByKey(decisionDefinitionKey).version(version).variables(variables).evaluate();
    }
    
    @Override
    public DecisionEvaluationBuilder evaluateDecisionTableByKey(final String decisionDefinitionKey) {
        return DecisionTableEvaluationBuilderImpl.evaluateDecisionTableByKey(this.commandExecutor, decisionDefinitionKey);
    }
    
    @Override
    public DecisionEvaluationBuilder evaluateDecisionTableById(final String decisionDefinitionId) {
        return DecisionTableEvaluationBuilderImpl.evaluateDecisionTableById(this.commandExecutor, decisionDefinitionId);
    }
    
    @Override
    public DecisionsEvaluationBuilder evaluateDecisionByKey(final String decisionDefinitionKey) {
        return DecisionEvaluationBuilderImpl.evaluateDecisionByKey(this.commandExecutor, decisionDefinitionKey);
    }
    
    @Override
    public DecisionsEvaluationBuilder evaluateDecisionById(final String decisionDefinitionId) {
        return DecisionEvaluationBuilderImpl.evaluateDecisionById(this.commandExecutor, decisionDefinitionId);
    }
}
