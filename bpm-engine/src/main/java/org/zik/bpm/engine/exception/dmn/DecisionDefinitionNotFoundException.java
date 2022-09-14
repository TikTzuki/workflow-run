// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception.dmn;

public class DecisionDefinitionNotFoundException extends DecisionException
{
    private static final long serialVersionUID = 1L;
    
    public DecisionDefinitionNotFoundException() {
    }
    
    public DecisionDefinitionNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DecisionDefinitionNotFoundException(final String message) {
        super(message);
    }
    
    public DecisionDefinitionNotFoundException(final Throwable cause) {
        super(cause);
    }
}
