// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

public class MethodNotFoundException extends ELException
{
    private static final long serialVersionUID = 1L;
    
    public MethodNotFoundException() {
    }
    
    public MethodNotFoundException(final String message) {
        super(message);
    }
    
    public MethodNotFoundException(final Throwable cause) {
        super(cause);
    }
    
    public MethodNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
