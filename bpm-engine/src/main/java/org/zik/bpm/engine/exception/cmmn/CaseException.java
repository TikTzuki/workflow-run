// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception.cmmn;

import org.zik.bpm.engine.ProcessEngineException;

public class CaseException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public CaseException() {
    }
    
    public CaseException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CaseException(final String message) {
        super(message);
    }
    
    public CaseException(final Throwable cause) {
        super(cause);
    }
}
