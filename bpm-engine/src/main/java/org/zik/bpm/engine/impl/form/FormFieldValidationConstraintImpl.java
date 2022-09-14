// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form;

import java.io.Serializable;
import org.zik.bpm.engine.form.FormFieldValidationConstraint;

public class FormFieldValidationConstraintImpl implements FormFieldValidationConstraint, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String name;
    protected String configuration;
    
    public FormFieldValidationConstraintImpl() {
    }
    
    public FormFieldValidationConstraintImpl(final String name, final String configuration) {
        this.name = name;
        this.configuration = configuration;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public Object getConfiguration() {
        return this.configuration;
    }
    
    public void setConfiguration(final String configuration) {
        this.configuration = configuration;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
}
