// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

public abstract class MethodExpression extends Expression
{
    private static final long serialVersionUID = 1L;
    
    public abstract MethodInfo getMethodInfo(final ELContext p0);
    
    public abstract Object invoke(final ELContext p0, final Object[] p1);
    
    public boolean isParmetersProvided() {
        return false;
    }
}
