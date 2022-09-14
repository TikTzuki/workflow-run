// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

public interface ResourceDefinition
{
    String getId();
    
    String getCategory();
    
    String getName();
    
    String getKey();
    
    int getVersion();
    
    String getResourceName();
    
    String getDeploymentId();
    
    String getDiagramResourceName();
    
    String getTenantId();
    
    Integer getHistoryTimeToLive();
}
