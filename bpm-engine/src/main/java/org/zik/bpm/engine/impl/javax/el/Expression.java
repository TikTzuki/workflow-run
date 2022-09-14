// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

import java.io.Serializable;

public abstract class Expression implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public abstract boolean equals(final Object p0);
    
    public abstract String getExpressionString();
    
    @Override
    public abstract int hashCode();
    
    public abstract boolean isLiteralText();
}
