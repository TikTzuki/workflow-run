// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.invocation;

import java.util.Set;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.context.VariableContext;

public class VariableScopeContext implements VariableContext
{
    protected final VariableScope variableScope;
    
    public VariableScopeContext(final VariableScope variableScope) {
        this.variableScope = variableScope;
    }
    
    public TypedValue resolve(final String variableName) {
        return this.variableScope.getVariableTyped(variableName);
    }
    
    public boolean containsVariable(final String variableName) {
        return this.variableScope.hasVariable(variableName);
    }
    
    public Set<String> keySet() {
        return this.variableScope.getVariableNames();
    }
    
    public static VariableScopeContext wrap(final VariableScope variableScope) {
        return new VariableScopeContext(variableScope);
    }
}
