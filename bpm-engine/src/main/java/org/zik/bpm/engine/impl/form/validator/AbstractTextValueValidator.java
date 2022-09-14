// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

import org.zik.bpm.engine.ProcessEngineException;

public abstract class AbstractTextValueValidator implements FormFieldValidator
{
    @Override
    public boolean validate(final Object submittedValue, final FormFieldValidatorContext validatorContext) {
        if (submittedValue == null) {
            return this.isNullValid();
        }
        final String configuration = validatorContext.getConfiguration();
        if (submittedValue instanceof String) {
            return this.validate((String)submittedValue, configuration);
        }
        throw new ProcessEngineException("String validator " + this.getClass().getSimpleName() + " cannot be used on non-string value of type " + submittedValue.getClass());
    }
    
    protected abstract boolean validate(final String p0, final String p1);
    
    protected boolean isNullValid() {
        return true;
    }
}
