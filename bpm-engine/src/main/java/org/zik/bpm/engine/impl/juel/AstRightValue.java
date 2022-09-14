// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ValueReference;
import org.zik.bpm.engine.impl.javax.el.MethodInfo;
import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public abstract class AstRightValue extends AstNode
{
    @Override
    public final boolean isLiteralText() {
        return false;
    }
    
    @Override
    public final Class<?> getType(final Bindings bindings, final ELContext context) {
        return null;
    }
    
    @Override
    public final boolean isReadOnly(final Bindings bindings, final ELContext context) {
        return true;
    }
    
    @Override
    public final void setValue(final Bindings bindings, final ELContext context, final Object value) {
        throw new ELException(LocalMessages.get("error.value.set.rvalue", this.getStructuralId(bindings)));
    }
    
    @Override
    public final MethodInfo getMethodInfo(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes) {
        return null;
    }
    
    @Override
    public final Object invoke(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes, final Object[] paramValues) {
        throw new ELException(LocalMessages.get("error.method.invalid", this.getStructuralId(bindings)));
    }
    
    @Override
    public final boolean isLeftValue() {
        return false;
    }
    
    @Override
    public boolean isMethodInvocation() {
        return false;
    }
    
    @Override
    public final ValueReference getValueReference(final Bindings bindings, final ELContext context) {
        return null;
    }
}
