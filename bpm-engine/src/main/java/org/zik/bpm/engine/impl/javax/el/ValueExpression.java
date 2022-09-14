// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.javax.el;

public abstract class ValueExpression extends Expression
{
    private static final long serialVersionUID = 1L;
    
    public abstract Class<?> getExpectedType();
    
    public abstract Class<?> getType(final ELContext p0);
    
    public abstract Object getValue(final ELContext p0);
    
    public abstract boolean isReadOnly(final ELContext p0);
    
    public abstract void setValue(final ELContext p0, final Object p1);
    
    public ValueReference getValueReference(final ELContext context) {
        return null;
    }
}
