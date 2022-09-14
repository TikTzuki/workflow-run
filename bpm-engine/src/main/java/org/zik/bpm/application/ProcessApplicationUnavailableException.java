// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

public class ProcessApplicationUnavailableException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public ProcessApplicationUnavailableException() {
    }
    
    public ProcessApplicationUnavailableException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ProcessApplicationUnavailableException(final String message) {
        super(message);
    }
    
    public ProcessApplicationUnavailableException(final Throwable cause) {
        super(cause);
    }
}
