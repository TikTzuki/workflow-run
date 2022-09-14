// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

import org.zik.bpm.engine.ProcessEngineException;

public class PvmException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public PvmException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public PvmException(final String message) {
        super(message);
    }
}
