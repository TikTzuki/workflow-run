// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

import org.zik.bpm.engine.impl.form.FormException;

public class FormFieldValidatorException extends FormException
{
    private static final long serialVersionUID = 1L;
    protected final String id;
    protected final String name;
    protected final String config;
    protected final Object value;
    
    public FormFieldValidatorException(final String id, final String name, final String config, final Object value, final String message, final Throwable cause) {
        super(message, cause);
        this.id = id;
        this.name = name;
        this.config = config;
        this.value = value;
    }
    
    public FormFieldValidatorException(final String id, final String name, final String config, final Object value, final String message) {
        super(message);
        this.id = id;
        this.name = name;
        this.config = config;
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getConfig() {
        return this.config;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public String getId() {
        return this.id;
    }
}
