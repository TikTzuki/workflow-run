// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.value.PrimitiveValue;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.DoubleValue;

public class DoubleValueSerializer extends PrimitiveValueSerializer<DoubleValue>
{
    public DoubleValueSerializer() {
        super(ValueType.DOUBLE);
    }
    
    @Override
    public DoubleValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        return Variables.doubleValue((Double)untypedValue.getValue(), untypedValue.isTransient());
    }
    
    @Override
    public void writeValue(final DoubleValue value, final ValueFields valueFields) {
        valueFields.setDoubleValue((Double)value.getValue());
    }
    
    @Override
    public DoubleValue readValue(final ValueFields valueFields, final boolean asTransientValue) {
        return Variables.doubleValue(valueFields.getDoubleValue(), asTransientValue);
    }
}
