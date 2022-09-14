// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.type;

import org.zik.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;
import java.util.Map;

public class EnumFormType extends SimpleFormFieldType
{
    public static final String TYPE_NAME = "enum";
    protected Map<String, String> values;
    
    public EnumFormType(final Map<String, String> values) {
        this.values = values;
    }
    
    @Override
    public String getName() {
        return "enum";
    }
    
    @Override
    public Object getInformation(final String key) {
        if (key.equals("values")) {
            return this.values;
        }
        return null;
    }
    
    public TypedValue convertValue(final TypedValue propertyValue) {
        final Object value = propertyValue.getValue();
        if (value == null || String.class.isInstance(value)) {
            this.validateValue(value);
            return (TypedValue)Variables.stringValue((String)value, propertyValue.isTransient());
        }
        throw new ProcessEngineException("Value '" + value + "' is not of type String.");
    }
    
    protected void validateValue(final Object value) {
        if (value != null && this.values != null && !this.values.containsKey(value)) {
            throw new ProcessEngineException("Invalid value for enum form property: " + value);
        }
    }
    
    public Map<String, String> getValues() {
        return this.values;
    }
    
    @Override
    public Object convertFormValueToModelValue(final Object propertyValue) {
        this.validateValue(propertyValue);
        return propertyValue;
    }
    
    @Override
    public String convertModelValueToFormValue(final Object modelValue) {
        if (modelValue != null) {
            if (!(modelValue instanceof String)) {
                throw new ProcessEngineException("Model value should be a String");
            }
            this.validateValue(modelValue);
        }
        return (String)modelValue;
    }
}
