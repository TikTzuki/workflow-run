// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

public class ProcessEngineException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public ProcessEngineException() {
    }
    
    public ProcessEngineException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ProcessEngineException(final String message) {
        super(message);
    }
    
    public ProcessEngineException(final Throwable cause) {
        super(cause);
    }
}
