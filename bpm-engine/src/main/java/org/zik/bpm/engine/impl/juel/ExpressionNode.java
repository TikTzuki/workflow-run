// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.MethodInfo;
import org.zik.bpm.engine.impl.javax.el.ValueReference;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public interface ExpressionNode extends Node
{
    boolean isLiteralText();
    
    boolean isLeftValue();
    
    boolean isMethodInvocation();
    
    Object getValue(final Bindings p0, final ELContext p1, final Class<?> p2);
    
    ValueReference getValueReference(final Bindings p0, final ELContext p1);
    
    Class<?> getType(final Bindings p0, final ELContext p1);
    
    boolean isReadOnly(final Bindings p0, final ELContext p1);
    
    void setValue(final Bindings p0, final ELContext p1, final Object p2);
    
    MethodInfo getMethodInfo(final Bindings p0, final ELContext p1, final Class<?> p2, final Class<?>[] p3);
    
    Object invoke(final Bindings p0, final ELContext p1, final Class<?> p2, final Class<?>[] p3, final Object[] p4);
    
    String getStructuralId(final Bindings p0);
}
