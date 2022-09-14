// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

public class ReadOnlyValidator implements FormFieldValidator
{
    @Override
    public boolean validate(final Object submittedValue, final FormFieldValidatorContext validatorContext) {
        return submittedValue == null;
    }
}
