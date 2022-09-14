// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.dmn;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import java.util.Map;

public interface DecisionsEvaluationBuilder
{
    DecisionsEvaluationBuilder decisionDefinitionTenantId(final String p0);
    
    DecisionsEvaluationBuilder decisionDefinitionWithoutTenantId();
    
    DecisionsEvaluationBuilder version(final Integer p0);
    
    DecisionsEvaluationBuilder variables(final Map<String, Object> p0);
    
    DmnDecisionResult evaluate();
}
