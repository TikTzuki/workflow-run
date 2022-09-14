// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.mapping.value;

import org.zik.bpm.engine.delegate.VariableScope;

public class ConstantValueProvider implements ParameterValueProvider
{
    protected Object value;
    
    public ConstantValueProvider(final Object value) {
        this.value = value;
    }
    
    @Override
    public Object getValue(final VariableScope scope) {
        return this.value;
    }
    
    public void setValue(final Object value) {
        this.value = value;
    }
    
    @Override
    public boolean isDynamic() {
        return false;
    }
}
