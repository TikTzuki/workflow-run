// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.MethodInfo;
import org.zik.bpm.engine.impl.javax.el.ValueReference;
import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public final class AstText extends AstNode
{
    private final String value;
    
    public AstText(final String value) {
        this.value = value;
    }
    
    @Override
    public boolean isLiteralText() {
        return true;
    }
    
    @Override
    public boolean isLeftValue() {
        return false;
    }
    
    @Override
    public boolean isMethodInvocation() {
        return false;
    }
    
    @Override
    public Class<?> getType(final Bindings bindings, final ELContext context) {
        return null;
    }
    
    @Override
    public boolean isReadOnly(final Bindings bindings, final ELContext context) {
        return true;
    }
    
    @Override
    public void setValue(final Bindings bindings, final ELContext context, final Object value) {
        throw new ELException(LocalMessages.get("error.value.set.rvalue", this.getStructuralId(bindings)));
    }
    
    @Override
    public ValueReference getValueReference(final Bindings bindings, final ELContext context) {
        return null;
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        return this.value;
    }
    
    @Override
    public MethodInfo getMethodInfo(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes) {
        return null;
    }
    
    @Override
    public Object invoke(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes, final Object[] paramValues) {
        return (returnType == null) ? this.value : bindings.convert(this.value, returnType);
    }
    
    @Override
    public String toString() {
        return "\"" + this.value + "\"";
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        final int end = this.value.length() - 1;
        for (int i = 0; i < end; ++i) {
            final char c = this.value.charAt(i);
            if ((c == '#' || c == '$') && this.value.charAt(i + 1) == '{') {
                b.append('\\');
            }
            b.append(c);
        }
        if (end >= 0) {
            b.append(this.value.charAt(end));
        }
    }
    
    @Override
    public int getCardinality() {
        return 0;
    }
    
    @Override
    public AstNode getChild(final int i) {
        return null;
    }
}
