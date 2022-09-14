// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.MethodInfo;
import org.zik.bpm.engine.impl.javax.el.ValueReference;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public final class AstEval extends AstNode
{
    private final AstNode child;
    private final boolean deferred;
    
    public AstEval(final AstNode child, final boolean deferred) {
        this.child = child;
        this.deferred = deferred;
    }
    
    public boolean isDeferred() {
        return this.deferred;
    }
    
    @Override
    public boolean isLeftValue() {
        return this.getChild(0).isLeftValue();
    }
    
    @Override
    public boolean isMethodInvocation() {
        return this.getChild(0).isMethodInvocation();
    }
    
    @Override
    public ValueReference getValueReference(final Bindings bindings, final ELContext context) {
        return this.child.getValueReference(bindings, context);
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        return this.child.eval(bindings, context);
    }
    
    @Override
    public String toString() {
        return (this.deferred ? "#" : "$") + "{...}";
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        b.append(this.deferred ? "#{" : "${");
        this.child.appendStructure(b, bindings);
        b.append("}");
    }
    
    @Override
    public MethodInfo getMethodInfo(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes) {
        return this.child.getMethodInfo(bindings, context, returnType, paramTypes);
    }
    
    @Override
    public Object invoke(final Bindings bindings, final ELContext context, final Class<?> returnType, final Class<?>[] paramTypes, final Object[] paramValues) {
        return this.child.invoke(bindings, context, returnType, paramTypes, paramValues);
    }
    
    @Override
    public Class<?> getType(final Bindings bindings, final ELContext context) {
        return this.child.getType(bindings, context);
    }
    
    @Override
    public boolean isLiteralText() {
        return this.child.isLiteralText();
    }
    
    @Override
    public boolean isReadOnly(final Bindings bindings, final ELContext context) {
        return this.child.isReadOnly(bindings, context);
    }
    
    @Override
    public void setValue(final Bindings bindings, final ELContext context, final Object value) {
        this.child.setValue(bindings, context, value);
    }
    
    @Override
    public int getCardinality() {
        return 1;
    }
    
    @Override
    public AstNode getChild(final int i) {
        return (i == 0) ? this.child : null;
    }
}
