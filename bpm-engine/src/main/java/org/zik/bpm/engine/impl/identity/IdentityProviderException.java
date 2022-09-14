// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import org.zik.bpm.engine.ProcessEngineException;

public class IdentityProviderException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public IdentityProviderException(final String message) {
        super(message);
    }
    
    public IdentityProviderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
