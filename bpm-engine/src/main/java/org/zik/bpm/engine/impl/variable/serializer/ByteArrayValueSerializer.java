// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.PrimitiveValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.impl.util.IoUtil;
import java.io.InputStream;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.BytesValue;

public class ByteArrayValueSerializer extends PrimitiveValueSerializer<BytesValue>
{
    public ByteArrayValueSerializer() {
        super(ValueType.BYTES);
    }
    
    @Override
    public BytesValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        final Object value = untypedValue.getValue();
        if (value instanceof byte[]) {
            return Variables.byteArrayValue((byte[])value, untypedValue.isTransient());
        }
        final byte[] data = IoUtil.readInputStream((InputStream)value, null);
        return Variables.byteArrayValue(data, untypedValue.isTransient());
    }
    
    @Override
    public BytesValue readValue(final ValueFields valueFields, final boolean asTransientValue) {
        return Variables.byteArrayValue(valueFields.getByteArrayValue(), asTransientValue);
    }
    
    @Override
    public void writeValue(final BytesValue variableValue, final ValueFields valueFields) {
        valueFields.setByteArrayValue((byte[])variableValue.getValue());
    }
    
    @Override
    protected boolean canWriteValue(final TypedValue typedValue) {
        return super.canWriteValue(typedValue) || typedValue.getValue() instanceof InputStream;
    }
}
