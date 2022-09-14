// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

public class BadUserRequestException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public BadUserRequestException() {
    }
    
    public BadUserRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public BadUserRequestException(final String message) {
        super(message);
    }
    
    public BadUserRequestException(final Throwable cause) {
        super(cause);
    }
}
