// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.dmn;

import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import java.util.Map;

public interface DecisionEvaluationBuilder
{
    DecisionEvaluationBuilder decisionDefinitionTenantId(final String p0);
    
    DecisionEvaluationBuilder decisionDefinitionWithoutTenantId();
    
    DecisionEvaluationBuilder version(final Integer p0);
    
    DecisionEvaluationBuilder variables(final Map<String, Object> p0);
    
    DmnDecisionTableResult evaluate();
}
