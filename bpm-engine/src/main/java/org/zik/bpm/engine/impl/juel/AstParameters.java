// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

import org.zik.bpm.engine.impl.javax.el.ELContext;
import java.util.List;

public class AstParameters extends AstRightValue
{
    private final List<AstNode> nodes;
    
    public AstParameters(final List<AstNode> nodes) {
        this.nodes = nodes;
    }
    
    @Override
    public Object[] eval(final Bindings bindings, final ELContext context) {
        final Object[] result = new Object[this.nodes.size()];
        for (int i = 0; i < this.nodes.size(); ++i) {
            result[i] = this.nodes.get(i).eval(bindings, context);
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "(...)";
    }
    
    @Override
    public void appendStructure(final StringBuilder builder, final Bindings bindings) {
        builder.append("(");
        for (int i = 0; i < this.nodes.size(); ++i) {
            if (i > 0) {
                builder.append(", ");
            }
            this.nodes.get(i).appendStructure(builder, bindings);
        }
        builder.append(")");
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
