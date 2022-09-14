// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

public interface Expression
{
    Object getValue(final VariableScope p0);
    
    void setValue(final Object p0, final VariableScope p1);
    
    String getExpressionText();
    
    boolean isLiteralText();
}
