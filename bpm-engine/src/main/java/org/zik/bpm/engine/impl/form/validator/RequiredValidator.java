// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

import org.camunda.bpm.engine.variable.value.TypedValue;

public class RequiredValidator implements FormFieldValidator
{
    @Override
    public boolean validate(final Object submittedValue, final FormFieldValidatorContext validatorContext) {
        if (submittedValue == null) {
            final TypedValue value = validatorContext.getVariableScope().getVariableTyped(validatorContext.getFormFieldHandler().getId());
            return value != null && value.getValue() != null;
        }
        if (submittedValue instanceof String) {
            return submittedValue != null && !((String)submittedValue).isEmpty();
        }
        return submittedValue != null;
    }
}
