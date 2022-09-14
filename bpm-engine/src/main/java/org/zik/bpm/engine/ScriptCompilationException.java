// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

public class ScriptCompilationException extends ScriptEngineException
{
    private static final long serialVersionUID = 1L;
    
    public ScriptCompilationException() {
    }
    
    public ScriptCompilationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ScriptCompilationException(final String message) {
        super(message);
    }
    
    public ScriptCompilationException(final Throwable cause) {
        super(cause);
    }
}
