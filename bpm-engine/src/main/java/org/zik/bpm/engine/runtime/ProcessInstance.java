// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface ProcessInstance extends Execution
{
    String getProcessDefinitionId();
    
    String getBusinessKey();
    
    String getRootProcessInstanceId();
    
    String getCaseInstanceId();
    
    boolean isSuspended();
}
