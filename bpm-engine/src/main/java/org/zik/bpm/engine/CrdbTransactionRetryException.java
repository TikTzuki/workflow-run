// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

public class CrdbTransactionRetryException extends OptimisticLockingException
{
    private static final long serialVersionUID = 1L;
    
    public CrdbTransactionRetryException(final String message) {
        super(message);
    }
    
    public CrdbTransactionRetryException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
