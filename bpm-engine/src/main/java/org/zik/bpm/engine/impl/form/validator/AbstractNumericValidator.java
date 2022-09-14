// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

public abstract class AbstractNumericValidator implements FormFieldValidator
{
    @Override
    public boolean validate(final Object submittedValue, final FormFieldValidatorContext validatorContext) {
        if (submittedValue == null) {
            return this.isNullValid();
        }
        final String configurationString = validatorContext.getConfiguration();
        if (submittedValue instanceof Double) {
            Double configuration = null;
            try {
                configuration = Double.parseDouble(configurationString);
            }
            catch (NumberFormatException e) {
                throw new FormFieldConfigurationException(configurationString, "Cannot validate Double value " + submittedValue + ": configuration " + configurationString + " cannot be parsed as Double.");
            }
            return this.validate((Double)submittedValue, configuration);
        }
        if (submittedValue instanceof Float) {
            Float configuration2 = null;
            try {
                configuration2 = Float.parseFloat(configurationString);
            }
            catch (NumberFormatException e) {
                throw new FormFieldConfigurationException(configurationString, "Cannot validate Float value " + submittedValue + ": configuration " + configurationString + " cannot be parsed as Float.");
            }
            return this.validate((Float)submittedValue, configuration2);
        }
        if (submittedValue instanceof Long) {
            Long configuration3 = null;
            try {
                configuration3 = Long.parseLong(configurationString);
            }
            catch (NumberFormatException e) {
                throw new FormFieldConfigurationException(configurationString, "Cannot validate Long value " + submittedValue + ": configuration " + configurationString + " cannot be parsed as Long.");
            }
            return this.validate((Long)submittedValue, configuration3);
        }
        if (submittedValue instanceof Integer) {
            Integer configuration4 = null;
            try {
                configuration4 = Integer.parseInt(configurationString);
            }
            catch (NumberFormatException e) {
                throw new FormFieldConfigurationException(configurationString, "Cannot validate Integer value " + submittedValue + ": configuration " + configurationString + " cannot be parsed as Integer.");
            }
            return this.validate((Integer)submittedValue, configuration4);
        }
        if (submittedValue instanceof Short) {
            Short configuration5 = null;
            try {
                configuration5 = Short.parseShort(configurationString);
            }
            catch (NumberFormatException e) {
                throw new FormFieldConfigurationException(configurationString, "Cannot validate Short value " + submittedValue + ": configuration " + configurationString + " cannot be parsed as Short.");
            }
            return this.validate((Short)submittedValue, configuration5);
        }
        throw new FormFieldValidationException((Object)("Numeric validator " + this.getClass().getSimpleName() + " cannot be used on non-numeric value " + submittedValue));
    }
    
    protected boolean isNullValid() {
        return true;
    }
    
    protected abstract boolean validate(final Integer p0, final Integer p1);
    
    protected abstract boolean validate(final Long p0, final Long p1);
    
    protected abstract boolean validate(final Double p0, final Double p1);
    
    protected abstract boolean validate(final Float p0, final Float p1);
    
    protected abstract boolean validate(final Short p0, final Short p1);
}
