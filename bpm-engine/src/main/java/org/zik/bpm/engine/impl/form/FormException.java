// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form;

import org.zik.bpm.engine.ProcessEngineException;

public class FormException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public FormException() {
    }
    
    public FormException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public FormException(final String message) {
        super(message);
    }
    
    public FormException(final Throwable cause) {
        super(cause);
    }
}
