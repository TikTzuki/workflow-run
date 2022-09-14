// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELContext;

public final class AstBoolean extends AstLiteral
{
    private final boolean value;
    
    public AstBoolean(final boolean value) {
        this.value = value;
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        return this.value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        b.append(this.value);
    }
}
