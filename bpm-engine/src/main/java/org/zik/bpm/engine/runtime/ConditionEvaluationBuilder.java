// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.List;
import java.util.Map;

public interface ConditionEvaluationBuilder
{
    ConditionEvaluationBuilder processInstanceBusinessKey(final String p0);
    
    ConditionEvaluationBuilder processDefinitionId(final String p0);
    
    ConditionEvaluationBuilder setVariable(final String p0, final Object p1);
    
    ConditionEvaluationBuilder setVariables(final Map<String, Object> p0);
    
    ConditionEvaluationBuilder tenantId(final String p0);
    
    ConditionEvaluationBuilder withoutTenantId();
    
    List<ProcessInstance> evaluateStartConditions();
}
