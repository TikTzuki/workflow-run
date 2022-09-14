// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.query.Query;
import org.camunda.bpm.model.xml.ModelInstance;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionQueryImpl;
import org.zik.bpm.engine.repository.DecisionDefinition;
import java.util.List;
import org.camunda.bpm.model.dmn.Dmn;
import java.io.InputStream;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.camunda.bpm.model.dmn.DmnModelInstance;

public class DmnModelInstanceCache extends ModelInstanceCache<DmnModelInstance, DecisionDefinitionEntity>
{
    public DmnModelInstanceCache(final CacheFactory factory, final int cacheCapacity, final ResourceDefinitionCache<DecisionDefinitionEntity> definitionCache) {
        super(factory, cacheCapacity, definitionCache);
    }
    
    @Override
    protected void throwLoadModelException(final String definitionId, final Exception e) {
        throw DmnModelInstanceCache.LOG.loadModelException("DMN", "decision", definitionId, e);
    }
    
    @Override
    protected DmnModelInstance readModelFromStream(final InputStream cmmnResourceInputStream) {
        return Dmn.readModelFromStream(cmmnResourceInputStream);
    }
    
    @Override
    protected void logRemoveEntryFromDeploymentCacheFailure(final String definitionId, final Exception e) {
        DmnModelInstanceCache.LOG.removeEntryFromDeploymentCacheFailure("decision", definitionId, e);
    }
    
    @Override
    protected List<DecisionDefinition> getAllDefinitionsForDeployment(final String deploymentId) {
        return ((Query<T, DecisionDefinition>)new DecisionDefinitionQueryImpl().deploymentId(deploymentId)).list();
    }
}
