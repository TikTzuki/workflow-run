// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception.cmmn;

public class CmmnModelInstanceNotFoundException extends CaseException
{
    private static final long serialVersionUID = 1L;
    
    public CmmnModelInstanceNotFoundException() {
    }
    
    public CmmnModelInstanceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CmmnModelInstanceNotFoundException(final String message) {
        super(message);
    }
    
    public CmmnModelInstanceNotFoundException(final Throwable cause) {
        super(cause);
    }
}
