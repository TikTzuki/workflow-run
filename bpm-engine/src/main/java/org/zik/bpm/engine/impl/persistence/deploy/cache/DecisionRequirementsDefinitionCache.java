// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;

public class DecisionRequirementsDefinitionCache extends ResourceDefinitionCache<DecisionRequirementsDefinitionEntity>
{
    public DecisionRequirementsDefinitionCache(final CacheFactory factory, final int cacheCapacity, final CacheDeployer cacheDeployer) {
        super(factory, cacheCapacity, cacheDeployer);
    }
    
    @Override
    protected AbstractResourceDefinitionManager<DecisionRequirementsDefinitionEntity> getManager() {
        return Context.getCommandContext().getDecisionRequirementsDefinitionManager();
    }
    
    @Override
    protected void checkInvalidDefinitionId(final String definitionId) {
        EnsureUtil.ensureNotNull("Invalid decision requirements definition id", "decisionRequirementsDefinitionId", definitionId);
    }
    
    @Override
    protected void checkDefinitionFound(final String definitionId, final DecisionRequirementsDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("no deployed decision requirements definition found with id '" + definitionId + "'", "decisionRequirementsDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKey(final String definitionKey, final DecisionRequirementsDefinitionEntity definition) {
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId, final DecisionRequirementsDefinitionEntity definition) {
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId, final DecisionRequirementsDefinitionEntity definition) {
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId, final DecisionRequirementsDefinitionEntity definition) {
    }
    
    @Override
    protected void checkInvalidDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey, final DecisionRequirementsDefinitionEntity definition) {
    }
    
    @Override
    protected void checkInvalidDefinitionWasCached(final String deploymentId, final String definitionId, final DecisionRequirementsDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("deployment '" + deploymentId + "' didn't put decision requirements definition '" + definitionId + "' in the cache", "cachedDecisionRequirementsDefinition", definition);
    }
}
