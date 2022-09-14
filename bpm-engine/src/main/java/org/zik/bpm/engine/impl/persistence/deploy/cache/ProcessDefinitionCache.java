// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;

public class ProcessDefinitionCache extends ResourceDefinitionCache<ProcessDefinitionEntity>
{
    public ProcessDefinitionCache(final CacheFactory factory, final int cacheCapacity, final CacheDeployer cacheDeployer) {
        super(factory, cacheCapacity, cacheDeployer);
    }
    
    @Override
    protected AbstractResourceDefinitionManager<ProcessDefinitionEntity> getManager() {
        return Context.getCommandContext().getProcessDefinitionManager();
    }
    
    @Override
    protected void checkInvalidDefinitionId(final String definitionId) {
        EnsureUtil.ensureNotNull("Invalid process definition id", "processDefinitionId", definitionId);
    }
    
    @Override
    protected void checkDefinitionFound(final String definitionId, final ProcessDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no deployed process definition found with id '" + definitionId + "'", "processDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKey(final String definitionKey, final ProcessDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no processes deployed with key '" + definitionKey + "'", "processDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId, final ProcessDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no processes deployed with key '" + definitionKey + "' and tenant-id '" + tenantId + "'", "processDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId, final ProcessDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no processes deployed with key = '" + definitionKey + "', version = '" + definitionVersion + "' and tenant-id = '" + tenantId + "'", "processDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId, final ProcessDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no processes deployed with key = '" + definitionKey + "', versionTag = '" + definitionVersionTag + "' and tenant-id = '" + tenantId + "'", "processDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey, final ProcessDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no processes deployed with key = '" + definitionKey + "' in deployment = '" + deploymentId + "'", "processDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionWasCached(final String deploymentId, final String definitionId, final ProcessDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("deployment '" + deploymentId + "' didn't put process definition '" + definitionId + "' in the cache", "cachedProcessDefinition", definition);
    }
}
