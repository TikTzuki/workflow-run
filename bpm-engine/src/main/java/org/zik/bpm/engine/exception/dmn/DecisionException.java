// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception.dmn;

import org.zik.bpm.engine.ProcessEngineException;

public class DecisionException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public DecisionException() {
    }
    
    public DecisionException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DecisionException(final String message) {
        super(message);
    }
    
    public DecisionException(final Throwable cause) {
        super(cause);
    }
}
