// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public interface ProcessDefinition extends ResourceDefinition
{
    String getDescription();
    
    boolean hasStartFormKey();
    
    boolean isSuspended();
    
    String getVersionTag();
    
    boolean isStartableInTasklist();
}
