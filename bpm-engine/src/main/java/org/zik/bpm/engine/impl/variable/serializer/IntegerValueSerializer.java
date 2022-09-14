// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.value.PrimitiveValue;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.IntegerValue;

public class IntegerValueSerializer extends PrimitiveValueSerializer<IntegerValue>
{
    public IntegerValueSerializer() {
        super(ValueType.INTEGER);
    }
    
    @Override
    public IntegerValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        return Variables.integerValue((Integer)untypedValue.getValue(), untypedValue.isTransient());
    }
    
    @Override
    public void writeValue(final IntegerValue variableValue, final ValueFields valueFields) {
        final Integer value = (Integer)variableValue.getValue();
        if (value != null) {
            valueFields.setLongValue((long)value);
            valueFields.setTextValue(value.toString());
        }
        else {
            valueFields.setLongValue(null);
            valueFields.setTextValue(null);
        }
    }
    
    @Override
    public IntegerValue readValue(final ValueFields valueFields, final boolean asTransientValue) {
        Integer intValue = null;
        if (valueFields.getLongValue() != null) {
            intValue = valueFields.getLongValue().intValue();
        }
        return Variables.integerValue(intValue, asTransientValue);
    }
}
