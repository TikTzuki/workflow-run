// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.task;

public interface IdentityLink
{
    String getId();
    
    String getType();
    
    String getUserId();
    
    String getGroupId();
    
    String getTaskId();
    
    String getProcessDefId();
    
    String getTenantId();
}
