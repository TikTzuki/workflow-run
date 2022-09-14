// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELContext;

public final class AstNumber extends AstLiteral
{
    private final Number value;
    
    public AstNumber(final Number value) {
        this.value = value;
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        return this.value;
    }
    
    @Override
    public String toString() {
        return this.value.toString();
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        b.append(this.value);
    }
}
