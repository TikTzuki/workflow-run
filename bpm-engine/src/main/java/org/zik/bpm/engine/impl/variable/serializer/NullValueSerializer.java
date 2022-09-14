// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.impl.value.NullValueImpl;

public class NullValueSerializer extends AbstractTypedValueSerializer<NullValueImpl>
{
    public NullValueSerializer() {
        super((ValueType)ValueType.NULL);
    }
    
    @Override
    public String getName() {
        return ValueType.NULL.getName().toLowerCase();
    }
    
    @Override
    public NullValueImpl convertToTypedValue(final UntypedValueImpl untypedValue) {
        return untypedValue.isTransient() ? NullValueImpl.INSTANCE_TRANSIENT : NullValueImpl.INSTANCE;
    }
    
    @Override
    public void writeValue(final NullValueImpl value, final ValueFields valueFields) {
    }
    
    @Override
    public NullValueImpl readValue(final ValueFields valueFields, final boolean deserialize, final boolean asTransientValue) {
        return NullValueImpl.INSTANCE;
    }
    
    @Override
    protected boolean canWriteValue(final TypedValue value) {
        return value.getValue() == null;
    }
}
