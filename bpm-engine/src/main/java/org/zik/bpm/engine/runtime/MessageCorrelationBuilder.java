// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.List;
import java.util.Map;

public interface MessageCorrelationBuilder
{
    MessageCorrelationBuilder processInstanceBusinessKey(final String p0);
    
    MessageCorrelationBuilder processInstanceVariableEquals(final String p0, final Object p1);
    
    MessageCorrelationBuilder processInstanceVariablesEqual(final Map<String, Object> p0);
    
    MessageCorrelationBuilder localVariableEquals(final String p0, final Object p1);
    
    MessageCorrelationBuilder localVariablesEqual(final Map<String, Object> p0);
    
    MessageCorrelationBuilder processInstanceId(final String p0);
    
    MessageCorrelationBuilder processDefinitionId(final String p0);
    
    MessageCorrelationBuilder setVariable(final String p0, final Object p1);
    
    MessageCorrelationBuilder setVariableLocal(final String p0, final Object p1);
    
    MessageCorrelationBuilder setVariables(final Map<String, Object> p0);
    
    MessageCorrelationBuilder setVariablesLocal(final Map<String, Object> p0);
    
    MessageCorrelationBuilder tenantId(final String p0);
    
    MessageCorrelationBuilder withoutTenantId();
    
    MessageCorrelationBuilder startMessageOnly();
    
    void correlate();
    
    MessageCorrelationResult correlateWithResult();
    
    MessageCorrelationResultWithVariables correlateWithResultAndVariables(final boolean p0);
    
    void correlateExclusively();
    
    void correlateAll();
    
    List<MessageCorrelationResult> correlateAllWithResult();
    
    List<MessageCorrelationResultWithVariables> correlateAllWithResultAndVariables(final boolean p0);
    
    ProcessInstance correlateStartMessage();
}
