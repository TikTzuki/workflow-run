// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.repository;

import org.zik.bpm.engine.repository.ResourceDefinition;

public interface ResourceDefinitionEntity<T extends ResourceDefinition> extends ResourceDefinition
{
    void setId(final String p0);
    
    void setCategory(final String p0);
    
    void setName(final String p0);
    
    void setKey(final String p0);
    
    void setVersion(final int p0);
    
    void setResourceName(final String p0);
    
    void setDeploymentId(final String p0);
    
    void setDiagramResourceName(final String p0);
    
    void setTenantId(final String p0);
    
    ResourceDefinitionEntity getPreviousDefinition();
    
    void updateModifiableFieldsFromEntity(final T p0);
    
    void setHistoryTimeToLive(final Integer p0);
}
