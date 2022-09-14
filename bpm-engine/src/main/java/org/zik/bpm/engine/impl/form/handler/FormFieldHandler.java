// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.camunda.bpm.engine.variable.VariableMap;
import java.util.Iterator;
import org.zik.bpm.engine.form.FormFieldValidationConstraint;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.Variables;
import org.zik.bpm.engine.impl.form.validator.FormFieldValidationException;
import org.zik.bpm.engine.form.FormType;
import org.zik.bpm.engine.impl.el.StartProcessVariableScope;
import org.zik.bpm.engine.impl.form.FormFieldImpl;
import org.zik.bpm.engine.form.FormField;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.zik.bpm.engine.delegate.Expression;

public class FormFieldHandler
{
    protected String id;
    protected Expression label;
    protected AbstractFormFieldType type;
    protected Expression defaultValueExpression;
    protected Map<String, String> properties;
    protected List<FormFieldValidationConstraintHandler> validationHandlers;
    protected boolean businessKey;
    
    public FormFieldHandler() {
        this.properties = new HashMap<String, String>();
        this.validationHandlers = new ArrayList<FormFieldValidationConstraintHandler>();
    }
    
    public FormField createFormField(final ExecutionEntity executionEntity) {
        final FormFieldImpl formField = new FormFieldImpl();
        formField.setId(this.id);
        final VariableScope variableScope = (executionEntity != null) ? executionEntity : StartProcessVariableScope.getSharedInstance();
        if (this.label != null) {
            final Object labelValueObject = this.label.getValue(variableScope);
            if (labelValueObject != null) {
                formField.setLabel(labelValueObject.toString());
            }
        }
        formField.setBusinessKey(this.businessKey);
        formField.setType(this.type);
        Object defaultValue = null;
        if (this.defaultValueExpression != null) {
            defaultValue = this.defaultValueExpression.getValue(variableScope);
            if (defaultValue != null) {
                formField.setDefaultValue(this.type.convertFormValueToModelValue(defaultValue));
            }
            else {
                formField.setDefaultValue(null);
            }
        }
        final TypedValue value = variableScope.getVariableTyped(this.id);
        if (value != null) {
            TypedValue formValue;
            try {
                formValue = this.type.convertToFormValue(value);
            }
            catch (Exception exception) {
                throw new FormFieldValidationException(this.id, "failed to convert '" + this.id + "'", exception);
            }
            formField.setValue(formValue);
        }
        else {
            final TypedValue typedDefaultValue = this.type.convertToModelValue(Variables.untypedValue(defaultValue));
            formField.setValue(this.type.convertToFormValue(typedDefaultValue));
        }
        formField.setProperties(this.properties);
        final List<FormFieldValidationConstraint> validationConstraints = formField.getValidationConstraints();
        for (final FormFieldValidationConstraintHandler validationHandler : this.validationHandlers) {
            if (!"validator".equals(validationHandler.name)) {
                validationConstraints.add(validationHandler.createValidationConstraint(executionEntity));
            }
        }
        return formField;
    }
    
    public void handleSubmit(final VariableScope variableScope, final VariableMap values, final VariableMap allValues) {
        final TypedValue submittedValue = values.getValueTyped(this.id);
        values.remove((Object)this.id);
        for (final FormFieldValidationConstraintHandler validationHandler : this.validationHandlers) {
            Object value = null;
            if (submittedValue != null) {
                value = submittedValue.getValue();
            }
            validationHandler.validate(value, allValues, this, variableScope);
        }
        TypedValue modelValue = null;
        if (submittedValue != null) {
            if (this.type != null) {
                modelValue = this.type.convertToModelValue(submittedValue);
            }
            else {
                modelValue = submittedValue;
            }
        }
        else if (this.defaultValueExpression != null) {
            final TypedValue expressionValue = Variables.untypedValue(this.defaultValueExpression.getValue(variableScope));
            if (this.type != null) {
                modelValue = this.type.convertToModelValue(Variables.untypedValue((Object)expressionValue));
            }
            else if (expressionValue != null) {
                modelValue = (TypedValue)Variables.stringValue(expressionValue.getValue().toString());
            }
        }
        if (modelValue != null && this.id != null) {
            variableScope.setVariable(this.id, modelValue);
        }
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public Expression getLabel() {
        return this.label;
    }
    
    public void setLabel(final Expression name) {
        this.label = name;
    }
    
    public void setType(final AbstractFormFieldType formType) {
        this.type = formType;
    }
    
    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }
    
    public Map<String, String> getProperties() {
        return this.properties;
    }
    
    public FormType getType() {
        return this.type;
    }
    
    public Expression getDefaultValueExpression() {
        return this.defaultValueExpression;
    }
    
    public void setDefaultValueExpression(final Expression defaultValue) {
        this.defaultValueExpression = defaultValue;
    }
    
    public List<FormFieldValidationConstraintHandler> getValidationHandlers() {
        return this.validationHandlers;
    }
    
    public void setValidationHandlers(final List<FormFieldValidationConstraintHandler> validationHandlers) {
        this.validationHandlers = validationHandlers;
    }
    
    public void setBusinessKey(final boolean businessKey) {
        this.businessKey = businessKey;
    }
    
    public boolean isBusinessKey() {
        return this.businessKey;
    }
}
