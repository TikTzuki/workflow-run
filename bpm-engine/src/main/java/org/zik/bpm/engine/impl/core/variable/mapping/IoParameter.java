// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.mapping;

import org.zik.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;

public abstract class IoParameter
{
    protected String name;
    protected ParameterValueProvider valueProvider;
    
    public IoParameter(final String name, final ParameterValueProvider valueProvider) {
        this.name = name;
        this.valueProvider = valueProvider;
    }
    
    public void execute(final AbstractVariableScope scope) {
        this.execute(scope, scope.getParentVariableScope());
    }
    
    protected abstract void execute(final AbstractVariableScope p0, final AbstractVariableScope p1);
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public ParameterValueProvider getValueProvider() {
        return this.valueProvider;
    }
    
    public void setValueProvider(final ParameterValueProvider valueProvider) {
        this.valueProvider = valueProvider;
    }
}
