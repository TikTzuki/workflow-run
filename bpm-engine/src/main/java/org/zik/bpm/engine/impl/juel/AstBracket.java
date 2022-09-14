// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public class AstBracket extends AstProperty
{
    protected final AstNode property;
    
    public AstBracket(final AstNode base, final AstNode property, final boolean lvalue, final boolean strict) {
        super(base, lvalue, strict);
        this.property = property;
    }
    
    @Override
    protected Object getProperty(final Bindings bindings, final ELContext context) throws ELException {
        return this.property.eval(bindings, context);
    }
    
    @Override
    public String toString() {
        return "[...]";
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        this.getChild(0).appendStructure(b, bindings);
        b.append("[");
        this.getChild(1).appendStructure(b, bindings);
        b.append("]");
    }
    
    @Override
    public int getCardinality() {
        return 2;
    }
    
    @Override
    public AstNode getChild(final int i) {
        return (i == 1) ? this.property : super.getChild(i);
    }
}
