// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

public class OptimisticLockingException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public OptimisticLockingException(final String message) {
        super(message);
    }
    
    public OptimisticLockingException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
