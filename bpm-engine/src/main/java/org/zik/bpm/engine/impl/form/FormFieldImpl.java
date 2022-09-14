// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import org.zik.bpm.engine.form.FormFieldValidationConstraint;
import java.util.List;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.zik.bpm.engine.form.FormType;
import org.zik.bpm.engine.form.FormField;

public class FormFieldImpl implements FormField
{
    protected boolean businessKey;
    protected String id;
    protected String label;
    protected FormType type;
    protected Object defaultValue;
    protected TypedValue value;
    protected List<FormFieldValidationConstraint> validationConstraints;
    protected Map<String, String> properties;
    
    public FormFieldImpl() {
        this.validationConstraints = new ArrayList<FormFieldValidationConstraint>();
        this.properties = new HashMap<String, String>();
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    @Override
    public FormType getType() {
        return this.type;
    }
    
    @Override
    public String getTypeName() {
        return this.type.getName();
    }
    
    public void setType(final FormType type) {
        this.type = type;
    }
    
    @Override
    public Object getDefaultValue() {
        return this.defaultValue;
    }
    
    @Override
    public TypedValue getValue() {
        return this.value;
    }
    
    public void setDefaultValue(final Object defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public void setValue(final TypedValue value) {
        this.value = value;
    }
    
    @Override
    public Map<String, String> getProperties() {
        return this.properties;
    }
    
    public void setProperties(final Map<String, String> properties) {
        this.properties = properties;
    }
    
    @Override
    public List<FormFieldValidationConstraint> getValidationConstraints() {
        return this.validationConstraints;
    }
    
    public void setValidationConstraints(final List<FormFieldValidationConstraint> validationConstraints) {
        this.validationConstraints = validationConstraints;
    }
    
    @Override
    public boolean isBusinessKey() {
        return this.businessKey;
    }
    
    public void setBusinessKey(final boolean businessKey) {
        this.businessKey = businessKey;
    }
}
