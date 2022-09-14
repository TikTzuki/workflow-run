// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface Execution
{
    String getId();
    
    boolean isSuspended();
    
    boolean isEnded();
    
    String getProcessInstanceId();
    
    String getTenantId();
}
