// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.form.validator.FormFieldValidatorContext;

public class DefaultFormFieldValidatorContext implements FormFieldValidatorContext
{
    protected VariableScope variableScope;
    protected String configuration;
    protected VariableMap submittedValues;
    protected FormFieldHandler formFieldHandler;
    
    public DefaultFormFieldValidatorContext(final VariableScope variableScope, final String configuration, final VariableMap submittedValues, final FormFieldHandler formFieldHandler) {
        this.variableScope = variableScope;
        this.configuration = configuration;
        this.submittedValues = submittedValues;
        this.formFieldHandler = formFieldHandler;
    }
    
    @Override
    public FormFieldHandler getFormFieldHandler() {
        return this.formFieldHandler;
    }
    
    @Override
    public DelegateExecution getExecution() {
        if (this.variableScope instanceof DelegateExecution) {
            return (DelegateExecution)this.variableScope;
        }
        if (this.variableScope instanceof TaskEntity) {
            return ((TaskEntity)this.variableScope).getExecution();
        }
        return null;
    }
    
    @Override
    public VariableScope getVariableScope() {
        return this.variableScope;
    }
    
    @Override
    public String getConfiguration() {
        return this.configuration;
    }
    
    public void setConfiguration(final String configuration) {
        this.configuration = configuration;
    }
    
    @Override
    public Map<String, Object> getSubmittedValues() {
        return (Map<String, Object>)this.submittedValues;
    }
}
