// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.query.Query;
import org.camunda.bpm.model.xml.ModelInstance;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionQueryImpl;
import org.zik.bpm.engine.repository.CaseDefinition;
import java.util.List;
import org.camunda.bpm.model.cmmn.Cmmn;
import java.io.InputStream;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;

public class CmmnModelInstanceCache extends ModelInstanceCache<CmmnModelInstance, CaseDefinitionEntity>
{
    public CmmnModelInstanceCache(final CacheFactory factory, final int cacheCapacity, final ResourceDefinitionCache<CaseDefinitionEntity> definitionCache) {
        super(factory, cacheCapacity, definitionCache);
    }
    
    @Override
    protected void throwLoadModelException(final String definitionId, final Exception e) {
        throw CmmnModelInstanceCache.LOG.loadModelException("CMMN", "case", definitionId, e);
    }
    
    @Override
    protected CmmnModelInstance readModelFromStream(final InputStream cmmnResourceInputStream) {
        return Cmmn.readModelFromStream(cmmnResourceInputStream);
    }
    
    @Override
    protected void logRemoveEntryFromDeploymentCacheFailure(final String definitionId, final Exception e) {
        CmmnModelInstanceCache.LOG.removeEntryFromDeploymentCacheFailure("case", definitionId, e);
    }
    
    @Override
    protected List<CaseDefinition> getAllDefinitionsForDeployment(final String deploymentId) {
        return ((Query<T, CaseDefinition>)new CaseDefinitionQueryImpl().deploymentId(deploymentId)).list();
    }
}
