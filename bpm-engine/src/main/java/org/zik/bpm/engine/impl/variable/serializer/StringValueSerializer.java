// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.serializer;

import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.bpm.engine.variable.value.PrimitiveValue;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.UntypedValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.StringValue;

public class StringValueSerializer extends PrimitiveValueSerializer<StringValue>
{
    public static final String EMPTY_STRING = "!emptyString!";
    
    public StringValueSerializer() {
        super(ValueType.STRING);
    }
    
    @Override
    public StringValue convertToTypedValue(final UntypedValueImpl untypedValue) {
        return Variables.stringValue((String)untypedValue.getValue(), untypedValue.isTransient());
    }
    
    @Override
    public StringValue readValue(final ValueFields valueFields, final boolean asTransientValue) {
        String textValue = valueFields.getTextValue();
        if (textValue == null && "!emptyString!".equals(valueFields.getTextValue2())) {
            textValue = "";
        }
        return Variables.stringValue(textValue, asTransientValue);
    }
    
    @Override
    public void writeValue(final StringValue variableValue, final ValueFields valueFields) {
        final String value = (String)variableValue.getValue();
        valueFields.setTextValue(value);
        if ("".equals(value)) {
            valueFields.setTextValue2("!emptyString!");
        }
    }
}
