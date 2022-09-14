// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application;

public class ProcessApplicationExecutionException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public ProcessApplicationExecutionException() {
    }
    
    public ProcessApplicationExecutionException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ProcessApplicationExecutionException(final String message) {
        super(message);
    }
    
    public ProcessApplicationExecutionException(final Throwable cause) {
        super(cause);
    }
}
