// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.camunda.bpm.engine.variable.VariableMap;

public interface MessageCorrelationResultWithVariables extends MessageCorrelationResult
{
    VariableMap getVariables();
}
