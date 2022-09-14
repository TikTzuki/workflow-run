// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception.cmmn;

public class CaseExecutionNotFoundException extends CaseException
{
    private static final long serialVersionUID = 1L;
    
    public CaseExecutionNotFoundException() {
    }
    
    public CaseExecutionNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CaseExecutionNotFoundException(final String message) {
        super(message);
    }
    
    public CaseExecutionNotFoundException(final Throwable cause) {
        super(cause);
    }
}
