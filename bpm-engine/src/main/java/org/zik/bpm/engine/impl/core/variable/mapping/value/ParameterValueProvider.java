// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.core.variable.mapping.value;

import org.zik.bpm.engine.delegate.VariableScope;

public interface ParameterValueProvider
{
    Object getValue(final VariableScope p0);
    
    boolean isDynamic();
}
