// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

public interface DelegateCaseVariableInstance extends DelegateVariableInstance<DelegateCaseExecution>
{
    String getEventName();
    
    DelegateCaseExecution getSourceExecution();
}
