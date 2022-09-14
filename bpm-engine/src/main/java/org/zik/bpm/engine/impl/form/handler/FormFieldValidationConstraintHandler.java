// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.impl.form.validator.FormFieldValidatorContext;
import org.zik.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.zik.bpm.engine.impl.form.validator.FormFieldValidatorException;
import org.zik.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.form.FormFieldValidationConstraintImpl;
import org.zik.bpm.engine.form.FormFieldValidationConstraint;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.form.validator.FormFieldValidator;

public class FormFieldValidationConstraintHandler
{
    protected String name;
    protected String config;
    protected FormFieldValidator validator;
    
    public FormFieldValidationConstraint createValidationConstraint(final ExecutionEntity execution) {
        return new FormFieldValidationConstraintImpl(this.name, this.config);
    }
    
    public void validate(final Object submittedValue, final VariableMap submittedValues, final FormFieldHandler formFieldHandler, final VariableScope variableScope) {
        try {
            final FormFieldValidatorContext context = new DefaultFormFieldValidatorContext(variableScope, this.config, submittedValues, formFieldHandler);
            if (!this.validator.validate(submittedValue, context)) {
                throw new FormFieldValidatorException(formFieldHandler.getId(), this.name, this.config, submittedValue, "Invalid value submitted for form field '" + formFieldHandler.getId() + "': validation of " + this + " failed.");
            }
        }
        catch (FormFieldValidationException e) {
            throw new FormFieldValidatorException(formFieldHandler.getId(), this.name, this.config, submittedValue, "Invalid value submitted for form field '" + formFieldHandler.getId() + "': validation of " + this + " failed.", e);
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setConfig(final String config) {
        this.config = config;
    }
    
    public String getConfig() {
        return this.config;
    }
    
    public void setValidator(final FormFieldValidator validator) {
        this.validator = validator;
    }
    
    public FormFieldValidator getValidator() {
        return this.validator;
    }
    
    @Override
    public String toString() {
        return this.name + ((this.config != null) ? ("(" + this.config + ")") : "");
    }
}
