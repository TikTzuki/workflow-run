// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.validator;

import org.zik.bpm.engine.impl.form.FormException;

public class FormFieldValidationException extends FormException
{
    private static final long serialVersionUID = 1L;
    protected Object detail;
    
    public FormFieldValidationException() {
    }
    
    public FormFieldValidationException(final Object detail) {
        this.detail = detail;
    }
    
    public FormFieldValidationException(final Object detail, final String message, final Throwable cause) {
        super(message, cause);
        this.detail = detail;
    }
    
    public FormFieldValidationException(final Object detail, final String message) {
        super(message);
        this.detail = detail;
    }
    
    public FormFieldValidationException(final Object detail, final Throwable cause) {
        super(cause);
        this.detail = detail;
    }
    
    public <T> T getDetail() {
        return (T)this.detail;
    }
}
