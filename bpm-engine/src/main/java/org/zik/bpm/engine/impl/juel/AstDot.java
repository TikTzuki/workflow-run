// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.ELContext;

public class AstDot extends AstProperty
{
    protected final String property;
    
    public AstDot(final AstNode base, final String property, final boolean lvalue) {
        super(base, lvalue, true);
        this.property = property;
    }
    
    @Override
    protected String getProperty(final Bindings bindings, final ELContext context) throws ELException {
        return this.property;
    }
    
    @Override
    public String toString() {
        return ". " + this.property;
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        this.getChild(0).appendStructure(b, bindings);
        b.append(".");
        b.append(this.property);
    }
    
    @Override
    public int getCardinality() {
        return 1;
    }
}
