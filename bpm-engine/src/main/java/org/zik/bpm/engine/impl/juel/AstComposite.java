// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELContext;
import java.util.List;

public class AstComposite extends AstRightValue
{
    private final List<AstNode> nodes;
    
    public AstComposite(final List<AstNode> nodes) {
        this.nodes = nodes;
    }
    
    @Override
    public Object eval(final Bindings bindings, final ELContext context) {
        final StringBuilder b = new StringBuilder(16);
        for (int i = 0; i < this.getCardinality(); ++i) {
            b.append(bindings.convert(this.nodes.get(i).eval(bindings, context), String.class));
        }
        return b.toString();
    }
    
    @Override
    public String toString() {
        return "composite";
    }
    
    @Override
    public void appendStructure(final StringBuilder b, final Bindings bindings) {
        for (int i = 0; i < this.getCardinality(); ++i) {
            this.nodes.get(i).appendStructure(b, bindings);
        }
    }
    
    @Override
    public int getCardinality() {
        return this.nodes.size();
    }
    
    @Override
    public AstNode getChild(final int i) {
        return this.nodes.get(i);
    }
}
