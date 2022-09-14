// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.value.PrimitiveValue;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.ShortValue;

public class ShortValueSerializer extends PrimitiveValueSerializer<ShortValue>
{
    public ShortValueSerializer() {
        super(ValueType.SHORT);
    }
    
    @Override
    public ShortValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        return Variables.shortValue((Short)untypedValue.getValue(), untypedValue.isTransient());
    }
    
    @Override
    public ShortValue readValue(final ValueFields valueFields, final boolean asTransientValue) {
        final Long longValue = valueFields.getLongValue();
        Short shortValue = null;
        if (longValue != null) {
            shortValue = longValue.shortValue();
        }
        return Variables.shortValue(shortValue, asTransientValue);
    }
    
    @Override
    public void writeValue(final ShortValue value, final ValueFields valueFields) {
        final Short shortValue = (Short)value.getValue();
        if (shortValue != null) {
            valueFields.setLongValue((long)shortValue);
            valueFields.setTextValue(value.toString());
        }
        else {
            valueFields.setLongValue(null);
            valueFields.setTextValue(null);
        }
    }
}
