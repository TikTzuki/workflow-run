// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception.cmmn;

public class CaseDefinitionNotFoundException extends CaseException
{
    private static final long serialVersionUID = 1L;
    
    public CaseDefinitionNotFoundException() {
    }
    
    public CaseDefinitionNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CaseDefinitionNotFoundException(final String message) {
        super(message);
    }
    
    public CaseDefinitionNotFoundException(final Throwable cause) {
        super(cause);
    }
}
