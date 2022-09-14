// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.dmn.DecisionDefinitionNotFoundException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionManager;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;

public class DecisionDefinitionCache extends ResourceDefinitionCache<DecisionDefinitionEntity>
{
    public DecisionDefinitionCache(final CacheFactory factory, final int cacheCapacity, final CacheDeployer cacheDeployer) {
        super(factory, cacheCapacity, cacheDeployer);
    }
    
    public DecisionDefinitionEntity findDeployedDefinitionByKeyAndVersion(final String definitionKey, final Integer definitionVersion) {
        DecisionDefinitionEntity definition = ((DecisionDefinitionManager)this.getManager()).findDecisionDefinitionByKeyAndVersion(definitionKey, definitionVersion);
        this.checkInvalidDefinitionByKeyAndVersion(definitionKey, definitionVersion, definition);
        definition = this.resolveDefinition(definition);
        return definition;
    }
    
    @Override
    protected AbstractResourceDefinitionManager<DecisionDefinitionEntity> getManager() {
        return Context.getCommandContext().getDecisionDefinitionManager();
    }
    
    @Override
    protected void checkInvalidDefinitionId(final String definitionId) {
        EnsureUtil.ensureNotNull("Invalid decision definition id", "decisionDefinitionId", definitionId);
    }
    
    @Override
    protected void checkDefinitionFound(final String definitionId, final DecisionDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(DecisionDefinitionNotFoundException.class, "no deployed decision definition found with id '" + definitionId + "'", "decisionDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKey(final String definitionKey, final DecisionDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(DecisionDefinitionNotFoundException.class, "no decision definition deployed with key '" + definitionKey + "'", "decisionDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId, final DecisionDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(DecisionDefinitionNotFoundException.class, "no decision definition deployed with key '" + definitionKey + "' and tenant-id '" + tenantId + "'", "decisionDefinition", definition);
    }
    
    protected void checkInvalidDefinitionByKeyAndVersion(final String decisionDefinitionKey, final Integer decisionDefinitionVersion, final DecisionDefinitionEntity decisionDefinition) {
        EnsureUtil.ensureNotNull(DecisionDefinitionNotFoundException.class, "no decision definition deployed with key = '" + decisionDefinitionKey + "' and version = '" + decisionDefinitionVersion + "'", "decisionDefinition", decisionDefinition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId, final DecisionDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(DecisionDefinitionNotFoundException.class, "no decision definition deployed with key = '" + definitionKey + "', version = '" + definitionVersion + "' and tenant-id '" + tenantId + "'", "decisionDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId, final DecisionDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(DecisionDefinitionNotFoundException.class, "no decision definition deployed with key = '" + definitionKey + "', versionTag = '" + definitionVersionTag + "' and tenant-id '" + tenantId + "'", "decisionDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey, final DecisionDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(DecisionDefinitionNotFoundException.class, "no decision definition deployed with key = '" + definitionKey + "' in deployment = '" + deploymentId + "'", "decisionDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionWasCached(final String deploymentId, final String definitionId, final DecisionDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("deployment '" + deploymentId + "' didn't put decision definition '" + definitionId + "' in the cache", "cachedDecisionDefinition", definition);
    }
}
