// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

public class MaxLengthValidator extends AbstractTextValueValidator
{
    @Override
    protected boolean validate(final String submittedValue, final String configuration) {
        Integer maxLength = null;
        try {
            maxLength = Integer.parseInt(configuration);
        }
        catch (NumberFormatException e) {
            throw new FormFieldConfigurationException(configuration, "Cannot validate \"maxlength\": configuration " + configuration + " cannot be interpreted as Integer");
        }
        return submittedValue.length() <= maxLength;
    }
}
