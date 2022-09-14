// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.transformer;

import org.zik.bpm.engine.ProcessEngineException;

public class CmmnTransformException extends ProcessEngineException
{
    private static final long serialVersionUID = 1L;
    
    public CmmnTransformException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CmmnTransformException(final String message) {
        super(message);
    }
}
