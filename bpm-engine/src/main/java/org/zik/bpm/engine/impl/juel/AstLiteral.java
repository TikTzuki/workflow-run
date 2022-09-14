// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

public abstract class AstLiteral extends AstRightValue
{
    @Override
    public final int getCardinality() {
        return 0;
    }
    
    @Override
    public final AstNode getChild(final int i) {
        return null;
    }
}
