// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELContext;

public final class AstNull extends AstLiteral
{
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        return null;
    }
    
    @Override
    public String toString() {
        return "null";
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        b.append("null");
    }
}
