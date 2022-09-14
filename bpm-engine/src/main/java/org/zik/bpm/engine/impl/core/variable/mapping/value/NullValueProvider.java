// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.mapping.value;

import org.zik.bpm.engine.delegate.VariableScope;

public class NullValueProvider implements ParameterValueProvider
{
    @Override
    public Object getValue(final VariableScope variableScope) {
        return null;
    }
    
    @Override
    public boolean isDynamic() {
        return false;
    }
}
