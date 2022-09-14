// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.Map;

public interface ActivityInstantiationBuilder<T extends ActivityInstantiationBuilder<T>>
{
    T setVariable(final String p0, final Object p1);
    
    T setVariableLocal(final String p0, final Object p1);
    
    T setVariables(final Map<String, Object> p0);
    
    T setVariablesLocal(final Map<String, Object> p0);
}
