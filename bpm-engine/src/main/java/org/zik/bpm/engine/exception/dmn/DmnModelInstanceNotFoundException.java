// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception.dmn;

public class DmnModelInstanceNotFoundException extends DecisionException
{
    private static final long serialVersionUID = 1L;
    
    public DmnModelInstanceNotFoundException() {
    }
    
    public DmnModelInstanceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DmnModelInstanceNotFoundException(final String message) {
        super(message);
    }
    
    public DmnModelInstanceNotFoundException(final Throwable cause) {
        super(cause);
    }
}
