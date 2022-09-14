// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELContext;

public abstract class AstNode implements ExpressionNode
{
    @Override
    public final Object getValue(final Bindings bindings, final ELContext context, final Class<?> type) {
        Object value = this.eval(bindings, context);
        if (type != null) {
            value = bindings.convert(value, type);
        }
        return value;
    }
    
    public abstract void appendStructure(final StringBuilder p0, final Bindings p1);
    
    public abstract Object eval(final Bindings p0, final ELContext p1);
    
    @Override
    public final String getStructuralId(final Bindings bindings) {
        final StringBuilder builder = new StringBuilder();
        this.appendStructure(builder, bindings);
        return builder.toString();
    }
}
