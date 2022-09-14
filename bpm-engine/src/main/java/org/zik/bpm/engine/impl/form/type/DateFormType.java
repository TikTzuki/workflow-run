// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.type;

import org.camunda.bpm.engine.variable.type.ValueType;
import java.text.ParseException;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Date;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class DateFormType extends AbstractFormFieldType
{
    public static final String TYPE_NAME = "date";
    protected String datePattern;
    protected DateFormat dateFormat;
    
    public DateFormType(final String datePattern) {
        this.datePattern = datePattern;
        this.dateFormat = new SimpleDateFormat(datePattern);
    }
    
    @Override
    public String getName() {
        return "date";
    }
    
    @Override
    public Object getInformation(final String key) {
        if ("datePattern".equals(key)) {
            return this.datePattern;
        }
        return null;
    }
    
    @Override
    public TypedValue convertToModelValue(final TypedValue propertyValue) {
        final Object value = propertyValue.getValue();
        if (value == null) {
            return (TypedValue)Variables.dateValue((Date)null, propertyValue.isTransient());
        }
        if (value instanceof Date) {
            return (TypedValue)Variables.dateValue((Date)value, propertyValue.isTransient());
        }
        if (value instanceof String) {
            final String strValue = ((String)value).trim();
            if (strValue.isEmpty()) {
                return (TypedValue)Variables.dateValue((Date)null, propertyValue.isTransient());
            }
            try {
                return (TypedValue)Variables.dateValue((Date)this.dateFormat.parseObject(strValue), propertyValue.isTransient());
            }
            catch (ParseException e) {
                throw new ProcessEngineException("Could not parse value '" + value + "' as date using date format '" + this.datePattern + "'.");
            }
        }
        throw new ProcessEngineException("Value '" + value + "' cannot be transformed into a Date.");
    }
    
    @Override
    public TypedValue convertToFormValue(final TypedValue modelValue) {
        if (modelValue.getValue() == null) {
            return (TypedValue)Variables.stringValue("", modelValue.isTransient());
        }
        if (modelValue.getType() == ValueType.DATE) {
            return (TypedValue)Variables.stringValue(this.dateFormat.format(modelValue.getValue()), modelValue.isTransient());
        }
        throw new ProcessEngineException("Expected value to be of type '" + ValueType.DATE + "' but got '" + modelValue.getType() + "'.");
    }
    
    @Override
    public Object convertFormValueToModelValue(final Object propertyValue) {
        if (propertyValue == null || "".equals(propertyValue)) {
            return null;
        }
        try {
            return this.dateFormat.parseObject(propertyValue.toString());
        }
        catch (ParseException e) {
            throw new ProcessEngineException("invalid date value " + propertyValue);
        }
    }
    
    @Override
    public String convertModelValueToFormValue(final Object modelValue) {
        if (modelValue == null) {
            return null;
        }
        return this.dateFormat.format(modelValue);
    }
}
