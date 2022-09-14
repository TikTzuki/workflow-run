// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception;

import org.zik.bpm.engine.BadUserRequestException;

public class NotValidException extends BadUserRequestException
{
    private static final long serialVersionUID = 1L;
    
    public NotValidException() {
    }
    
    public NotValidException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public NotValidException(final String message) {
        super(message);
    }
    
    public NotValidException(final Throwable cause) {
        super(cause);
    }
}
