// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.cmmn.CaseDefinitionNotFoundException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;

public class CaseDefinitionCache extends ResourceDefinitionCache<CaseDefinitionEntity>
{
    public CaseDefinitionCache(final CacheFactory factory, final int cacheCapacity, final CacheDeployer cacheDeployer) {
        super(factory, cacheCapacity, cacheDeployer);
    }
    
    public CaseDefinitionEntity getCaseDefinitionById(final String caseDefinitionId) {
        this.checkInvalidDefinitionId(caseDefinitionId);
        CaseDefinitionEntity caseDefinition = this.getDefinition(caseDefinitionId);
        if (caseDefinition == null) {
            caseDefinition = this.findDeployedDefinitionById(caseDefinitionId);
        }
        return caseDefinition;
    }
    
    @Override
    protected AbstractResourceDefinitionManager<CaseDefinitionEntity> getManager() {
        return Context.getCommandContext().getCaseDefinitionManager();
    }
    
    @Override
    protected void checkInvalidDefinitionId(final String definitionId) {
        EnsureUtil.ensureNotNull("Invalid case definition id", "caseDefinitionId", definitionId);
    }
    
    @Override
    protected void checkDefinitionFound(final String definitionId, final CaseDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(CaseDefinitionNotFoundException.class, "no deployed case definition found with id '" + definitionId + "'", "caseDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKey(final String definitionKey, final CaseDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(CaseDefinitionNotFoundException.class, "no case definition deployed with key '" + definitionKey + "'", "caseDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId, final CaseDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(CaseDefinitionNotFoundException.class, "no case definition deployed with key '" + definitionKey + "' and tenant-id '" + tenantId + "'", "caseDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId, final CaseDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(CaseDefinitionNotFoundException.class, "no case definition deployed with key = '" + definitionKey + "', version = '" + definitionVersion + "' and tenant-id = '" + tenantId + "'", "caseDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId, final CaseDefinitionEntity definition) {
        throw new UnsupportedOperationException("Version tag is not implemented in case definition.");
    }
    
    @Override
    protected void checkInvalidDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey, final CaseDefinitionEntity definition) {
        EnsureUtil.ensureNotNull(CaseDefinitionNotFoundException.class, "no case definition deployed with key = '" + definitionKey + "' in deployment = '" + deploymentId + "'", "caseDefinition", definition);
    }
    
    @Override
    protected void checkInvalidDefinitionWasCached(final String deploymentId, final String definitionId, final CaseDefinitionEntity definition) {
        EnsureUtil.ensureNotNull("deployment '" + deploymentId + "' didn't put case definition '" + definitionId + "' in the cache", "cachedCaseDefinition", definition);
    }
}
