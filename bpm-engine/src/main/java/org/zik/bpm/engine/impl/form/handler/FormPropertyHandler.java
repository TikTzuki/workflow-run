// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.form.FormType;
import org.zik.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.el.StartProcessVariableScope;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.form.FormPropertyImpl;
import org.zik.bpm.engine.form.FormProperty;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.form.type.AbstractFormFieldType;

public class FormPropertyHandler
{
    protected String id;
    protected String name;
    protected AbstractFormFieldType type;
    protected boolean isReadable;
    protected boolean isWritable;
    protected boolean isRequired;
    protected String variableName;
    protected Expression variableExpression;
    protected Expression defaultExpression;
    
    public FormProperty createFormProperty(final ExecutionEntity execution) {
        final FormPropertyImpl formProperty = new FormPropertyImpl(this);
        Object modelValue = null;
        if (execution != null) {
            if (this.variableName != null || this.variableExpression == null) {
                final String varName = (this.variableName != null) ? this.variableName : this.id;
                if (execution.hasVariable(varName)) {
                    modelValue = execution.getVariable(varName);
                }
                else if (this.defaultExpression != null) {
                    modelValue = this.defaultExpression.getValue(execution);
                }
            }
            else {
                modelValue = this.variableExpression.getValue(execution);
            }
        }
        else if (this.defaultExpression != null) {
            modelValue = this.defaultExpression.getValue(StartProcessVariableScope.getSharedInstance());
        }
        if (modelValue instanceof String) {
            formProperty.setValue((String)modelValue);
        }
        else if (this.type != null) {
            final String formValue = this.type.convertModelValueToFormValue(modelValue);
            formProperty.setValue(formValue);
        }
        else if (modelValue != null) {
            formProperty.setValue(modelValue.toString());
        }
        return formProperty;
    }
    
    public void submitFormProperty(final VariableScope variableScope, final VariableMap variables) {
        if (!this.isWritable && variables.containsKey((Object)this.id)) {
            throw new ProcessEngineException("form property '" + this.id + "' is not writable");
        }
        if (this.isRequired && !variables.containsKey((Object)this.id) && this.defaultExpression == null) {
            throw new ProcessEngineException("form property '" + this.id + "' is required");
        }
        Object modelValue = null;
        if (variables.containsKey((Object)this.id)) {
            final Object propertyValue = variables.remove((Object)this.id);
            if (this.type != null) {
                modelValue = this.type.convertFormValueToModelValue(propertyValue);
            }
            else {
                modelValue = propertyValue;
            }
        }
        else if (this.defaultExpression != null) {
            final Object expressionValue = this.defaultExpression.getValue(variableScope);
            if (this.type != null && expressionValue != null) {
                modelValue = this.type.convertFormValueToModelValue(expressionValue.toString());
            }
            else if (expressionValue != null) {
                modelValue = expressionValue.toString();
            }
            else if (this.isRequired) {
                throw new ProcessEngineException("form property '" + this.id + "' is required");
            }
        }
        if (modelValue != null) {
            if (this.variableName != null) {
                variableScope.setVariable(this.variableName, modelValue);
            }
            else if (this.variableExpression != null) {
                this.variableExpression.setValue(modelValue, variableScope);
            }
            else {
                variableScope.setVariable(this.id, modelValue);
            }
        }
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public FormType getType() {
        return this.type;
    }
    
    public void setType(final AbstractFormFieldType type) {
        this.type = type;
    }
    
    public boolean isReadable() {
        return this.isReadable;
    }
    
    public void setReadable(final boolean isReadable) {
        this.isReadable = isReadable;
    }
    
    public boolean isRequired() {
        return this.isRequired;
    }
    
    public void setRequired(final boolean isRequired) {
        this.isRequired = isRequired;
    }
    
    public String getVariableName() {
        return this.variableName;
    }
    
    public void setVariableName(final String variableName) {
        this.variableName = variableName;
    }
    
    public Expression getVariableExpression() {
        return this.variableExpression;
    }
    
    public void setVariableExpression(final Expression variableExpression) {
        this.variableExpression = variableExpression;
    }
    
    public Expression getDefaultExpression() {
        return this.defaultExpression;
    }
    
    public void setDefaultExpression(final Expression defaultExpression) {
        this.defaultExpression = defaultExpression;
    }
    
    public boolean isWritable() {
        return this.isWritable;
    }
    
    public void setWritable(final boolean isWritable) {
        this.isWritable = isWritable;
    }
}
