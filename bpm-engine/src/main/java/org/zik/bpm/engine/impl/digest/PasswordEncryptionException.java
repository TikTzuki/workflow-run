// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.digest;

import org.zik.bpm.engine.ProcessEngineException;

public class PasswordEncryptionException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public PasswordEncryptionException(final String message) {
        super(message);
    }
}
