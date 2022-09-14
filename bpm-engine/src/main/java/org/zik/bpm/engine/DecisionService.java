// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.dmn.DecisionsEvaluationBuilder;
import org.zik.bpm.engine.dmn.DecisionEvaluationBuilder;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import java.util.Map;

public interface DecisionService
{
    DmnDecisionTableResult evaluateDecisionTableById(final String p0, final Map<String, Object> p1);
    
    DmnDecisionTableResult evaluateDecisionTableByKey(final String p0, final Map<String, Object> p1);
    
    DmnDecisionTableResult evaluateDecisionTableByKeyAndVersion(final String p0, final Integer p1, final Map<String, Object> p2);
    
    DecisionEvaluationBuilder evaluateDecisionTableByKey(final String p0);
    
    DecisionEvaluationBuilder evaluateDecisionTableById(final String p0);
    
    DecisionsEvaluationBuilder evaluateDecisionByKey(final String p0);
    
    DecisionsEvaluationBuilder evaluateDecisionById(final String p0);
}
