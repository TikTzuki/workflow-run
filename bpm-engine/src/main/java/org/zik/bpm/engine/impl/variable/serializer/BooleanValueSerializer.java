// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.value.PrimitiveValue;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.BooleanValue;

public class BooleanValueSerializer extends PrimitiveValueSerializer<BooleanValue>
{
    private static final Long TRUE;
    private static final Long FALSE;
    
    public BooleanValueSerializer() {
        super(ValueType.BOOLEAN);
    }
    
    @Override
    public BooleanValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        return Variables.booleanValue((Boolean)untypedValue.getValue(), untypedValue.isTransient());
    }
    
    @Override
    public BooleanValue readValue(final ValueFields valueFields, final boolean asTransientValue) {
        Boolean boolValue = null;
        final Long longValue = valueFields.getLongValue();
        if (longValue != null) {
            boolValue = longValue.equals(BooleanValueSerializer.TRUE);
        }
        return Variables.booleanValue(boolValue, asTransientValue);
    }
    
    @Override
    public void writeValue(final BooleanValue variableValue, final ValueFields valueFields) {
        Long longValue = null;
        final Boolean boolValue = (Boolean)variableValue.getValue();
        if (boolValue != null) {
            longValue = (boolValue ? BooleanValueSerializer.TRUE : BooleanValueSerializer.FALSE);
        }
        valueFields.setLongValue(longValue);
    }
    
    static {
        TRUE = 1L;
        FALSE = 0L;
    }
}
