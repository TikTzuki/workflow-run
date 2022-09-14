// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface ProcessElementInstance
{
    String getId();
    
    String getParentActivityInstanceId();
    
    String getProcessDefinitionId();
    
    String getProcessInstanceId();
}
