// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;

public interface VariableSerializerFactory
{
    TypedValueSerializer<?> getSerializer(final String p0);
    
    TypedValueSerializer<?> getSerializer(final TypedValue p0);
}
