// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.value.PrimitiveValue;
import org.camunda.bpm.engine.variable.Variables;
import java.util.Date;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.DateValue;

public class DateValueSerializer extends PrimitiveValueSerializer<DateValue>
{
    public DateValueSerializer() {
        super(ValueType.DATE);
    }
    
    @Override
    public DateValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        return Variables.dateValue((Date)untypedValue.getValue(), untypedValue.isTransient());
    }
    
    @Override
    public DateValue readValue(final ValueFields valueFields, final boolean asTransientValue) {
        final Long longValue = valueFields.getLongValue();
        Date dateValue = null;
        if (longValue != null) {
            dateValue = new Date(longValue);
        }
        return Variables.dateValue(dateValue, asTransientValue);
    }
    
    @Override
    public void writeValue(final DateValue typedValue, final ValueFields valueFields) {
        final Date dateValue = (Date)typedValue.getValue();
        if (dateValue != null) {
            valueFields.setLongValue(dateValue.getTime());
        }
        else {
            valueFields.setLongValue(null);
        }
    }
}
