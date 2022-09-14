// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.ProcessEngineException;

public class ClassLoadingException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    protected String className;
    
    public ClassLoadingException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public ClassLoadingException(final String message, final String className, final Throwable cause) {
        this(message, cause);
        this.className = className;
    }
    
    public String getClassName() {
        return this.className;
    }
}
