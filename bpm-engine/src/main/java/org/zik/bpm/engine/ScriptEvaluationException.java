// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

public class ScriptEvaluationException extends ScriptEngineException
{
    private static final long serialVersionUID = 1L;
    
    public ScriptEvaluationException() {
    }
    
    public ScriptEvaluationException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ScriptEvaluationException(final String message) {
        super(message);
    }
    
    public ScriptEvaluationException(final Throwable cause) {
        super(cause);
    }
}
