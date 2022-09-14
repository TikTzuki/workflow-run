// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.type.PrimitiveValueType;
import org.camunda.bpm.engine.variable.value.PrimitiveValue;

public abstract class PrimitiveValueSerializer<T extends PrimitiveValue<?>> extends AbstractTypedValueSerializer<T>
{
    public PrimitiveValueSerializer(final PrimitiveValueType variableType) {
        super((ValueType)variableType);
    }
    
    @Override
    public String getName() {
        return this.valueType.getName();
    }
    
    @Override
    public T readValue(final ValueFields valueFields, final boolean deserializeObjectValue, final boolean asTransientValue) {
        return this.readValue(valueFields, asTransientValue);
    }
    
    public abstract T readValue(final ValueFields p0, final boolean p1);
    
    public PrimitiveValueType getType() {
        return (PrimitiveValueType)super.getType();
    }
    
    @Override
    protected boolean canWriteValue(final TypedValue typedValue) {
        final Object value = typedValue.getValue();
        final Class<?> javaType = (Class<?>)this.getType().getJavaType();
        return value == null || javaType.isAssignableFrom(value.getClass());
    }
}
