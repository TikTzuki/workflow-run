// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception;

import org.zik.bpm.engine.BadUserRequestException;

public class NotFoundException extends BadUserRequestException
{
    private static final long serialVersionUID = 1L;
    
    public NotFoundException() {
    }
    
    public NotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public NotFoundException(final String message) {
        super(message);
    }
    
    public NotFoundException(final Throwable cause) {
        super(cause);
    }
}
