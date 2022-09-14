// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.type;

import org.zik.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.BooleanValue;
import org.camunda.bpm.engine.variable.value.TypedValue;

public class BooleanFormType extends SimpleFormFieldType
{
    public static final String TYPE_NAME = "boolean";
    
    @Override
    public String getName() {
        return "boolean";
    }
    
    public TypedValue convertValue(final TypedValue propertyValue) {
        if (propertyValue instanceof BooleanValue) {
            return propertyValue;
        }
        final Object value = propertyValue.getValue();
        if (value == null) {
            return (TypedValue)Variables.booleanValue((Boolean)null, propertyValue.isTransient());
        }
        if (value instanceof Boolean || value instanceof String) {
            return (TypedValue)Variables.booleanValue(new Boolean(value.toString()), propertyValue.isTransient());
        }
        throw new ProcessEngineException("Value '" + value + "' is not of type Boolean.");
    }
    
    @Override
    public Object convertFormValueToModelValue(final Object propertyValue) {
        if (propertyValue == null || "".equals(propertyValue)) {
            return null;
        }
        return Boolean.valueOf(propertyValue.toString());
    }
    
    @Override
    public String convertModelValueToFormValue(final Object modelValue) {
        if (modelValue == null) {
            return null;
        }
        if (Boolean.class.isAssignableFrom(modelValue.getClass()) || Boolean.TYPE.isAssignableFrom(modelValue.getClass())) {
            return modelValue.toString();
        }
        throw new ProcessEngineException("Model value is not of type boolean, but of type " + modelValue.getClass().getName());
    }
}
