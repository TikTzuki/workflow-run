// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.CamundaFormDefinitionEntity;

public class CamundaFormDefinitionCache extends ResourceDefinitionCache<CamundaFormDefinitionEntity>
{
    public CamundaFormDefinitionCache(final CacheFactory factory, final int cacheCapacity, final CacheDeployer cacheDeployer) {
        super(factory, cacheCapacity, cacheDeployer);
    }
    
    @Override
    protected AbstractResourceDefinitionManager<CamundaFormDefinitionEntity> getManager() {
        return Context.getCommandContext().getCamundaFormDefinitionManager();
    }
    
    @Override
    protected void checkInvalidDefinitionId(final String definitionId) {
        EnsureUtil.ensureNotNull("Invalid camunda form definition id", "camundaFormDefinitionId", definitionId);
    }
    
    @Override
    protected void checkDefinitionFound(final String definitionId, final CamundaFormDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no deployed camunda form definition found with id '" + definitionId + "'", "camundaFormDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKey(final String definitionKey, final CamundaFormDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no deployed camunda form definition found with key '" + definitionKey + "'", "camundaFormDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId, final CamundaFormDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no deployed camunda form definition found with key '" + definitionKey + "' and tenant-id '" + tenantId + "'", "camundaFormDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId, final CamundaFormDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no deployed camunda form definition found with key '" + definitionKey + "', version '" + definitionVersion + "' and tenant-id '" + tenantId + "'", "camundaFormDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId, final CamundaFormDefinitionEntity definition) {
    }
    
    @Override
    protected void checkInvalidDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey, final CamundaFormDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no deployed camunda form definition found with key '" + definitionKey + "' in deployment '" + deploymentId + "'", "camundaFormDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionWasCached(final String deploymentId, final String definitionId, final CamundaFormDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("deployment '" + deploymentId + "' didn't put camunda form definition '" + definitionId + "' in the cache", "cachedProcessDefinition", definition);
    }
}
