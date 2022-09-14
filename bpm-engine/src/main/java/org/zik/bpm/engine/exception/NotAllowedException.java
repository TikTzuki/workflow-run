// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception;

import org.zik.bpm.engine.BadUserRequestException;

public class NotAllowedException extends BadUserRequestException
{
    private static final long serialVersionUID = 1L;
    
    public NotAllowedException() {
    }
    
    public NotAllowedException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public NotAllowedException(final String message) {
        super(message);
    }
    
    public NotAllowedException(final Throwable cause) {
        super(cause);
    }
}
