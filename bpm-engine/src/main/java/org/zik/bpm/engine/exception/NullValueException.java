// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception;

import org.zik.bpm.engine.ProcessEngineException;

public class NullValueException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public NullValueException() {
    }
    
    public NullValueException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public NullValueException(final String message) {
        super(message);
    }
    
    public NullValueException(final Throwable cause) {
        super(cause);
    }
}
