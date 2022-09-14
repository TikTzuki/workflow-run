// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.juel;

public interface FunctionNode extends Node
{
    String getName();
    
    int getIndex();
    
    int getParamCount();
    
    boolean isVarArgs();
}
