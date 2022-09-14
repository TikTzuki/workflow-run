// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import java.util.HashSet;
import org.camunda.bpm.engine.variable.type.ValueType;
import java.util.Set;
import org.camunda.bpm.engine.variable.value.TypedValue;

public abstract class AbstractTypedValueSerializer<T extends TypedValue> implements TypedValueSerializer<T>
{
    public static final Set<String> BINARY_VALUE_TYPES;
    protected ValueType valueType;
    
    public AbstractTypedValueSerializer(final ValueType type) {
        this.valueType = type;
    }
    
    @Override
    public ValueType getType() {
        return this.valueType;
    }
    
    @Override
    public String getSerializationDataformat() {
        return null;
    }
    
    @Override
    public boolean canHandle(final TypedValue value) {
        return (value.getType() == null || this.valueType.getClass().isAssignableFrom(value.getType().getClass())) && this.canWriteValue(value);
    }
    
    protected abstract boolean canWriteValue(final TypedValue p0);
    
    @Override
    public boolean isMutableValue(final T typedValue) {
        return false;
    }
    
    static {
        (BINARY_VALUE_TYPES = new HashSet<String>()).add(ValueType.BYTES.getName());
        AbstractTypedValueSerializer.BINARY_VALUE_TYPES.add(ValueType.FILE.getName());
    }
}
