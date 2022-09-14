// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class FormFieldValidatorInvocation extends DelegateInvocation
{
    protected FormFieldValidator formFieldValidator;
    protected Object submittedValue;
    protected FormFieldValidatorContext validatorContext;
    
    public FormFieldValidatorInvocation(final FormFieldValidator formFieldValidator, final Object submittedValue, final FormFieldValidatorContext validatorContext) {
        super(null, null);
        this.formFieldValidator = formFieldValidator;
        this.submittedValue = submittedValue;
        this.validatorContext = validatorContext;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.invocationResult = this.formFieldValidator.validate(this.submittedValue, this.validatorContext);
    }
    
    @Override
    public Boolean getInvocationResult() {
        return (Boolean)super.getInvocationResult();
    }
}
