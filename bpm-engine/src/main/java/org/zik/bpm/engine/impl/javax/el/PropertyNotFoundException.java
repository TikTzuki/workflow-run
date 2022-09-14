// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

public class PropertyNotFoundException extends ELException
{
    private static final long serialVersionUID = 1L;
    
    public PropertyNotFoundException() {
    }
    
    public PropertyNotFoundException(final String message) {
        super(message);
    }
    
    public PropertyNotFoundException(final Throwable cause) {
        super(cause);
    }
    
    public PropertyNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
