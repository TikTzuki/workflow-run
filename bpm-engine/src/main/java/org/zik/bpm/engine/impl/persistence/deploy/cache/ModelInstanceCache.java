// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.repository.ResourceDefinition;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.GetDeploymentResourceCmd;
import java.io.InputStream;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.commons.utils.cache.Cache;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.camunda.bpm.model.xml.ModelInstance;

public abstract class ModelInstanceCache<InstanceType extends ModelInstance, DefinitionType extends ResourceDefinitionEntity>
{
    protected static final EnginePersistenceLogger LOG;
    protected Cache<String, InstanceType> instanceCache;
    protected ResourceDefinitionCache<DefinitionType> definitionCache;
    
    public ModelInstanceCache(final CacheFactory factory, final int cacheCapacity, final ResourceDefinitionCache<DefinitionType> definitionCache) {
        this.instanceCache = factory.createCache(cacheCapacity);
        this.definitionCache = definitionCache;
    }
    
    public InstanceType findBpmnModelInstanceForDefinition(final DefinitionType definitionEntity) {
        InstanceType bpmnModelInstance = (InstanceType)this.instanceCache.get((Object)definitionEntity.getId());
        if (bpmnModelInstance == null) {
            bpmnModelInstance = this.loadAndCacheBpmnModelInstance(definitionEntity);
        }
        return bpmnModelInstance;
    }
    
    public InstanceType findBpmnModelInstanceForDefinition(final String definitionId) {
        InstanceType bpmnModelInstance = (InstanceType)this.instanceCache.get((Object)definitionId);
        if (bpmnModelInstance == null) {
            final DefinitionType definition = this.definitionCache.findDeployedDefinitionById(definitionId);
            bpmnModelInstance = this.loadAndCacheBpmnModelInstance(definition);
        }
        return bpmnModelInstance;
    }
    
    protected InstanceType loadAndCacheBpmnModelInstance(final DefinitionType definitionEntity) {
        final CommandContext commandContext = Context.getCommandContext();
        final InputStream bpmnResourceInputStream = commandContext.runWithoutAuthorization((Command<InputStream>)new GetDeploymentResourceCmd(definitionEntity.getDeploymentId(), definitionEntity.getResourceName()));
        try {
            final InstanceType bpmnModelInstance = this.readModelFromStream(bpmnResourceInputStream);
            this.instanceCache.put((Object)definitionEntity.getId(), (Object)bpmnModelInstance);
            return bpmnModelInstance;
        }
        catch (Exception e) {
            this.throwLoadModelException(definitionEntity.getId(), e);
            return null;
        }
    }
    
    public void removeAllDefinitionsByDeploymentId(final String deploymentId) {
        final List<? extends ResourceDefinition> allDefinitionsForDeployment = this.getAllDefinitionsForDeployment(deploymentId);
        for (final ResourceDefinition definition : allDefinitionsForDeployment) {
            try {
                this.instanceCache.remove((Object)definition.getId());
                this.definitionCache.removeDefinitionFromCache(definition.getId());
            }
            catch (Exception e) {
                this.logRemoveEntryFromDeploymentCacheFailure(definition.getId(), e);
            }
        }
    }
    
    public void remove(final String definitionId) {
        this.instanceCache.remove((Object)definitionId);
    }
    
    public void clear() {
        this.instanceCache.clear();
    }
    
    public Cache<String, InstanceType> getCache() {
        return this.instanceCache;
    }
    
    protected abstract void throwLoadModelException(final String p0, final Exception p1);
    
    protected abstract void logRemoveEntryFromDeploymentCacheFailure(final String p0, final Exception p1);
    
    protected abstract InstanceType readModelFromStream(final InputStream p0);
    
    protected abstract List<? extends ResourceDefinition> getAllDefinitionsForDeployment(final String p0);
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
