// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.exception.NotAllowedException;

public class SuspendedEntityInteractionException extends NotAllowedException
{
    private static final long serialVersionUID = 1L;
    
    public SuspendedEntityInteractionException(final String message) {
        super(message);
    }
}
