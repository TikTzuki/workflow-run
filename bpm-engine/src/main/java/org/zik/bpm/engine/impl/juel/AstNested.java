// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELContext;

public final class AstNested extends AstRightValue
{
    private final AstNode child;
    
    public AstNested(final AstNode child) {
        this.child = child;
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        return this.child.eval(bindings, context);
    }
    
    @Override
    public String toString() {
        return "(...)";
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        b.append("(");
        this.child.appendStructure(b, bindings);
        b.append(")");
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
