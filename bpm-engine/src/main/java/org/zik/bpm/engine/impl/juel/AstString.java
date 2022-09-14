// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELContext;

public final class AstString extends AstLiteral
{
    private final String value;
    
    public AstString(final String value) {
        this.value = value;
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        return this.value;
    }
    
    @Override
    public String toString() {
        return "\"" + this.value + "\"";
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        b.append("'");
        for (int length = this.value.length(), i = 0; i < length; ++i) {
            final char c = this.value.charAt(i);
            if (c == '\\' || c == '\'') {
                b.append('\\');
            }
            b.append(c);
        }
        b.append("'");
    }
}
