// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.engine;

import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;
import java.util.Collections;
import java.util.Map;
import org.zik.bpm.engine.form.FormType;
import org.zik.bpm.engine.impl.form.FormFieldValidationConstraintImpl;
import java.util.ArrayList;
import org.zik.bpm.engine.form.FormFieldValidationConstraint;
import java.util.List;
import org.zik.bpm.engine.form.FormProperty;
import org.zik.bpm.engine.form.FormField;

public class FormPropertyAdapter implements FormField
{
    protected FormProperty formProperty;
    protected List<FormFieldValidationConstraint> validationConstraints;
    
    public FormPropertyAdapter(final FormProperty formProperty) {
        this.formProperty = formProperty;
        this.validationConstraints = new ArrayList<FormFieldValidationConstraint>();
        if (formProperty.isRequired()) {
            this.validationConstraints.add(new FormFieldValidationConstraintImpl("required", null));
        }
        if (!formProperty.isWritable()) {
            this.validationConstraints.add(new FormFieldValidationConstraintImpl("readonly", null));
        }
    }
    
    @Override
    public String getId() {
        return this.formProperty.getId();
    }
    
    @Override
    public String getLabel() {
        return this.formProperty.getName();
    }
    
    @Override
    public FormType getType() {
        return this.formProperty.getType();
    }
    
    @Override
    public String getTypeName() {
        return this.formProperty.getType().getName();
    }
    
    @Override
    public Object getDefaultValue() {
        return this.formProperty.getValue();
    }
    
    @Override
    public List<FormFieldValidationConstraint> getValidationConstraints() {
        return this.validationConstraints;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return Collections.emptyMap();
    }
    
    @Override
    public boolean isBusinessKey() {
        return false;
    }
    
    public TypedValue getDefaultValueTyped() {
        return this.getValue();
    }
    
    @Override
    public TypedValue getValue() {
        return (TypedValue)Variables.stringValue(this.formProperty.getValue());
    }
}
