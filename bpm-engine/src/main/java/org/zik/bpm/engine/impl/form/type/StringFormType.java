// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.type;

import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class StringFormType extends SimpleFormFieldType
{
    public static final String TYPE_NAME = "string";
    
    @Override
    public String getName() {
        return "string";
    }
    
    public TypedValue convertValue(final TypedValue propertyValue) {
        if (propertyValue instanceof StringValue) {
            return propertyValue;
        }
        final Object value = propertyValue.getValue();
        if (value == null) {
            return (TypedValue)Variables.stringValue((String)null, propertyValue.isTransient());
        }
        return (TypedValue)Variables.stringValue(value.toString(), propertyValue.isTransient());
    }
    
    @Override
    public Object convertFormValueToModelValue(final Object propertyValue) {
        return propertyValue.toString();
    }
    
    @Override
    public String convertModelValueToFormValue(final Object modelValue) {
        return (String)modelValue;
    }
}
