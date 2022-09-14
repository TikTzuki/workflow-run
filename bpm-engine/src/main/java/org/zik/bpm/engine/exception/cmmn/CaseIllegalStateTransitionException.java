// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception.cmmn;

public class CaseIllegalStateTransitionException extends CaseException
{
    private static final long serialVersionUID = 1L;
    
    public CaseIllegalStateTransitionException() {
    }
    
    public CaseIllegalStateTransitionException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CaseIllegalStateTransitionException(final String message) {
        super(message);
    }
    
    public CaseIllegalStateTransitionException(final Throwable cause) {
        super(cause);
    }
}
