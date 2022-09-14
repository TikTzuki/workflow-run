// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.TypedValue;

public interface TypedValueSerializer<T extends TypedValue>
{
    String getName();
    
    ValueType getType();
    
    void writeValue(final T p0, final ValueFields p1);
    
    T readValue(final ValueFields p0, final boolean p1, final boolean p2);
    
    boolean canHandle(final TypedValue p0);
    
    T convertToTypedValue(final UntypedValueImpl p0);
    
    String getSerializationDataformat();
    
    boolean isMutableValue(final T p0);
}
