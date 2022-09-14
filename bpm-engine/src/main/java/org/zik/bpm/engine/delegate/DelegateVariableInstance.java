// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

import org.zik.bpm.engine.runtime.VariableInstance;

public interface DelegateVariableInstance<T extends BaseDelegateExecution> extends VariableInstance, ProcessEngineServicesAware
{
    String getEventName();
    
    T getSourceExecution();
}
