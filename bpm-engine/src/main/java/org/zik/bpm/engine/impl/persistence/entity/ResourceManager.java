// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class ResourceManager extends AbstractManager
{
    public void insertResource(final ResourceEntity resource) {
        this.getDbEntityManager().insert(resource);
    }
    
    public void deleteResourcesByDeploymentId(final String deploymentId) {
        this.getDbEntityManager().delete(ResourceEntity.class, "deleteResourcesByDeploymentId", deploymentId);
    }
    
    public ResourceEntity findResourceByDeploymentIdAndResourceName(final String deploymentId, final String resourceName) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("deploymentId", deploymentId);
        params.put("resourceName", resourceName);
        return (ResourceEntity)this.getDbEntityManager().selectOne("selectResourceByDeploymentIdAndResourceName", params);
    }
    
    public List<ResourceEntity> findResourceByDeploymentIdAndResourceNames(final String deploymentId, final String... resourceNames) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("deploymentId", deploymentId);
        params.put("resourceNames", resourceNames);
        return (List<ResourceEntity>)this.getDbEntityManager().selectList("selectResourceByDeploymentIdAndResourceNames", params);
    }
    
    public ResourceEntity findResourceByDeploymentIdAndResourceId(final String deploymentId, final String resourceId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("deploymentId", deploymentId);
        params.put("resourceId", resourceId);
        return (ResourceEntity)this.getDbEntityManager().selectOne("selectResourceByDeploymentIdAndResourceId", params);
    }
    
    public List<ResourceEntity> findResourceByDeploymentIdAndResourceIds(final String deploymentId, final String... resourceIds) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("deploymentId", deploymentId);
        params.put("resourceIds", resourceIds);
        return (List<ResourceEntity>)this.getDbEntityManager().selectList("selectResourceByDeploymentIdAndResourceIds", params);
    }
    
    public List<ResourceEntity> findResourcesByDeploymentId(final String deploymentId) {
        return (List<ResourceEntity>)this.getDbEntityManager().selectList("selectResourcesByDeploymentId", deploymentId);
    }
    
    public Map<String, ResourceEntity> findLatestResourcesByDeploymentName(final String deploymentName, final Set<String> resourcesToFind, final String source, final String tenantId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("deploymentName", deploymentName);
        params.put("resourcesToFind", resourcesToFind);
        params.put("source", source);
        params.put("tenantId", tenantId);
        final List<ResourceEntity> resources = (List<ResourceEntity>)this.getDbEntityManager().selectList("selectLatestResourcesByDeploymentName", params);
        final Map<String, ResourceEntity> existingResourcesByName = new HashMap<String, ResourceEntity>();
        for (final ResourceEntity existingResource : resources) {
            existingResourcesByName.put(existingResource.getName(), existingResource);
        }
        return existingResourcesByName;
    }
    
    public ResourceEntity findLicenseKeyResource() {
        final PropertyEntity licenseProperty = (PropertyEntity)this.getDbEntityManager().selectOne("selectProperty", "camunda-license-key-id");
        return (licenseProperty == null) ? null : ((ResourceEntity)this.getDbEntityManager().selectOne("selectResourceById", licenseProperty.value));
    }
}
