// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.exception;

import org.zik.bpm.engine.ProcessEngineException;

public class DeploymentResourceNotFoundException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public DeploymentResourceNotFoundException() {
    }
    
    public DeploymentResourceNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public DeploymentResourceNotFoundException(final String message) {
        super(message);
    }
    
    public DeploymentResourceNotFoundException(final Throwable cause) {
        super(cause);
    }
}
