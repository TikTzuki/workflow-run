// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence;

public interface AbstractResourceDefinitionManager<T>
{
    T findLatestDefinitionByKey(final String p0);
    
    T findLatestDefinitionById(final String p0);
    
    T findLatestDefinitionByKeyAndTenantId(final String p0, final String p1);
    
    T findDefinitionByKeyVersionAndTenantId(final String p0, final Integer p1, final String p2);
    
    T findDefinitionByDeploymentAndKey(final String p0, final String p1);
    
    T getCachedResourceDefinitionEntity(final String p0);
    
    T findDefinitionByKeyVersionTagAndTenantId(final String p0, final String p1, final String p2);
}
