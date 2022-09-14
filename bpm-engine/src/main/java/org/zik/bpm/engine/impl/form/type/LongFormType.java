// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.type;

import org.zik.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.LongValue;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class LongFormType extends SimpleFormFieldType
{
    public static final String TYPE_NAME = "long";
    
    @Override
    public String getName() {
        return "long";
    }
    
    public TypedValue convertValue(final TypedValue propertyValue) {
        if (propertyValue instanceof LongValue) {
            return propertyValue;
        }
        final Object value = propertyValue.getValue();
        if (value == null) {
            return (TypedValue)Variables.longValue((Long)null, propertyValue.isTransient());
        }
        if (value instanceof Number || value instanceof String) {
            return (TypedValue)Variables.longValue(new Long(value.toString()), propertyValue.isTransient());
        }
        throw new ProcessEngineException("Value '" + value + "' is not of type Long.");
    }
    
    @Override
    public Object convertFormValueToModelValue(final Object propertyValue) {
        if (propertyValue == null || "".equals(propertyValue)) {
            return null;
        }
        return new Long(propertyValue.toString());
    }
    
    @Override
    public String convertModelValueToFormValue(final Object modelValue) {
        if (modelValue == null) {
            return null;
        }
        return modelValue.toString();
    }
}
