// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

import org.zik.bpm.engine.impl.form.FormException;

public class FormFieldConfigurationException extends FormException
{
    private static final long serialVersionUID = 1L;
    protected String configuration;
    
    public FormFieldConfigurationException(final String configuration) {
        this.configuration = configuration;
    }
    
    public FormFieldConfigurationException(final String configuration, final String message, final Throwable cause) {
        super(message, cause);
        this.configuration = configuration;
    }
    
    public FormFieldConfigurationException(final String configuration, final String message) {
        super(message);
        this.configuration = configuration;
    }
    
    public FormFieldConfigurationException(final String configuration, final Throwable cause) {
        super(cause);
        this.configuration = configuration;
    }
    
    public String getConfiguration() {
        return this.configuration;
    }
}
