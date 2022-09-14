// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

public interface BaseDelegateExecution extends VariableScope
{
    String getId();
    
    String getEventName();
    
    String getBusinessKey();
}
