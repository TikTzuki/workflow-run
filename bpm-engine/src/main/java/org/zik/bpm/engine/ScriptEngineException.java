// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.ProcessEngineException;

public class ScriptEngineException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public ScriptEngineException() {
    }
    
    public ScriptEngineException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ScriptEngineException(final String message) {
        super(message);
    }
    
    public ScriptEngineException(final Throwable cause) {
        super(cause);
    }
}
