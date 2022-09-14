// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.value.PrimitiveValue;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.LongValue;

public class LongValueSerlializer extends PrimitiveValueSerializer<LongValue>
{
    public LongValueSerlializer() {
        super(ValueType.LONG);
    }
    
    @Override
    public LongValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        return Variables.longValue((Long)untypedValue.getValue(), untypedValue.isTransient());
    }
    
    @Override
    public LongValue readValue(final ValueFields valueFields, final boolean asTransientValue) {
        return Variables.longValue(valueFields.getLongValue(), asTransientValue);
    }
    
    @Override
    public void writeValue(final LongValue value, final ValueFields valueFields) {
        final Long longValue = (Long)value.getValue();
        valueFields.setLongValue(longValue);
        if (longValue != null) {
            valueFields.setTextValue(longValue.toString());
        }
        else {
            valueFields.setTextValue(null);
        }
    }
}
