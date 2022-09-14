// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

public class PropertyNotWritableException extends ELException
{
    private static final long serialVersionUID = 1L;
    
    public PropertyNotWritableException() {
    }
    
    public PropertyNotWritableException(final String message) {
        super(message);
    }
    
    public PropertyNotWritableException(final Throwable cause) {
        super(cause);
    }
    
    public PropertyNotWritableException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
