// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

import org.camunda.bpm.engine.variable.VariableMap;

public interface DelegateVariableMapping
{
    void mapInputVariables(final DelegateExecution p0, final VariableMap p1);
    
    void mapOutputVariables(final DelegateExecution p0, final VariableScope p1);
}
