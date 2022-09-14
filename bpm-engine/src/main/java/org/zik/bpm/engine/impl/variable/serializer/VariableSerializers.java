// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import java.util.List;
import org.camunda.bpm.engine.variable.value.TypedValue;

public interface VariableSerializers
{
    TypedValueSerializer findSerializerForValue(final TypedValue p0, final VariableSerializerFactory p1);
    
    TypedValueSerializer findSerializerForValue(final TypedValue p0);
    
    TypedValueSerializer<?> getSerializerByName(final String p0);
    
    VariableSerializers addSerializer(final TypedValueSerializer<?> p0);
    
    VariableSerializers addSerializer(final TypedValueSerializer<?> p0, final int p1);
    
    VariableSerializers removeSerializer(final TypedValueSerializer<?> p0);
    
    int getSerializerIndex(final TypedValueSerializer<?> p0);
    
    int getSerializerIndexByName(final String p0);
    
    VariableSerializers join(final VariableSerializers p0);
    
    List<TypedValueSerializer<?>> getSerializers();
}
