// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import org.zik.bpm.engine.ProcessEngineException;

public class ELException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public ELException() {
    }
    
    public ELException(final String message) {
        super(message);
    }
    
    public ELException(final Throwable cause) {
        super(cause);
    }
    
    public ELException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
