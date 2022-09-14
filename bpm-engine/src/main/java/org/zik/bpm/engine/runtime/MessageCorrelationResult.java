// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface MessageCorrelationResult
{
    Execution getExecution();
    
    ProcessInstance getProcessInstance();
    
    MessageCorrelationResultType getResultType();
}
