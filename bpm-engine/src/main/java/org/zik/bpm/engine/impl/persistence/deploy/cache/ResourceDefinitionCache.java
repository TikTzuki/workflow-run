// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.commons.utils.cache.Cache;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;

public abstract class ResourceDefinitionCache<T extends ResourceDefinitionEntity>
{
    protected Cache<String, T> cache;
    protected CacheDeployer cacheDeployer;
    
    public ResourceDefinitionCache(final CacheFactory factory, final int cacheCapacity, final CacheDeployer cacheDeployer) {
        this.cache = factory.createCache(cacheCapacity);
        this.cacheDeployer = cacheDeployer;
    }
    
    public T findDefinitionFromCache(final String definitionId) {
        return (T)this.cache.get((Object)definitionId);
    }
    
    public T findDeployedDefinitionById(final String definitionId) {
        this.checkInvalidDefinitionId(definitionId);
        T definition = this.getManager().getCachedResourceDefinitionEntity(definitionId);
        if (definition == null) {
            definition = this.getManager().findLatestDefinitionById(definitionId);
        }
        this.checkDefinitionFound(definitionId, definition);
        definition = this.resolveDefinition(definition);
        return definition;
    }
    
    public T findDeployedLatestDefinitionByKey(final String definitionKey) {
        T definition = this.getManager().findLatestDefinitionByKey(definitionKey);
        this.checkInvalidDefinitionByKey(definitionKey, definition);
        definition = this.resolveDefinition(definition);
        return definition;
    }
    
    public T findDeployedLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        T definition = this.getManager().findLatestDefinitionByKeyAndTenantId(definitionKey, tenantId);
        this.checkInvalidDefinitionByKeyAndTenantId(definitionKey, tenantId, definition);
        definition = this.resolveDefinition(definition);
        return definition;
    }
    
    public T findDeployedDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId) {
        final CommandContext commandContext = Context.getCommandContext();
        T definition = commandContext.runWithoutAuthorization((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                return ResourceDefinitionCache.this.getManager().findDefinitionByKeyVersionAndTenantId(definitionKey, definitionVersion, tenantId);
            }
        });
        this.checkInvalidDefinitionByKeyVersionAndTenantId(definitionKey, definitionVersion, tenantId, definition);
        definition = this.resolveDefinition(definition);
        return definition;
    }
    
    public T findDeployedDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId) {
        final CommandContext commandContext = Context.getCommandContext();
        T definition = commandContext.runWithoutAuthorization((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                return ResourceDefinitionCache.this.getManager().findDefinitionByKeyVersionTagAndTenantId(definitionKey, definitionVersionTag, tenantId);
            }
        });
        this.checkInvalidDefinitionByKeyVersionTagAndTenantId(definitionKey, definitionVersionTag, tenantId, definition);
        definition = this.resolveDefinition(definition);
        return definition;
    }
    
    public T findDeployedDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        T definition = this.getManager().findDefinitionByDeploymentAndKey(deploymentId, definitionKey);
        this.checkInvalidDefinitionByDeploymentAndKey(deploymentId, definitionKey, definition);
        definition = this.resolveDefinition(definition);
        return definition;
    }
    
    public T resolveDefinition(final T definition) {
        final String definitionId = definition.getId();
        final String deploymentId = definition.getDeploymentId();
        T cachedDefinition = (T)this.cache.get((Object)definitionId);
        if (cachedDefinition == null) {
            synchronized (this) {
                cachedDefinition = (T)this.cache.get((Object)definitionId);
                if (cachedDefinition == null) {
                    final DeploymentEntity deployment = Context.getCommandContext().getDeploymentManager().findDeploymentById(deploymentId);
                    deployment.setNew(false);
                    this.cacheDeployer.deployOnlyGivenResourcesOfDeployment(deployment, definition.getResourceName(), definition.getDiagramResourceName());
                    cachedDefinition = (T)this.cache.get((Object)definitionId);
                }
            }
            this.checkInvalidDefinitionWasCached(deploymentId, definitionId, cachedDefinition);
        }
        if (cachedDefinition != null) {
            cachedDefinition.updateModifiableFieldsFromEntity(definition);
        }
        return cachedDefinition;
    }
    
    public void addDefinition(final T definition) {
        this.cache.put((Object)definition.getId(), (Object)definition);
    }
    
    public T getDefinition(final String id) {
        return (T)this.cache.get((Object)id);
    }
    
    public void removeDefinitionFromCache(final String id) {
        this.cache.remove((Object)id);
    }
    
    public void clear() {
        this.cache.clear();
    }
    
    public Cache<String, T> getCache() {
        return this.cache;
    }
    
    protected abstract AbstractResourceDefinitionManager<T> getManager();
    
    protected abstract void checkInvalidDefinitionId(final String p0);
    
    protected abstract void checkDefinitionFound(final String p0, final T p1);
    
    protected abstract void checkInvalidDefinitionByKey(final String p0, final T p1);
    
    protected abstract void checkInvalidDefinitionByKeyAndTenantId(final String p0, final String p1, final T p2);
    
    protected abstract void checkInvalidDefinitionByKeyVersionAndTenantId(final String p0, final Integer p1, final String p2, final T p3);
    
    protected abstract void checkInvalidDefinitionByKeyVersionTagAndTenantId(final String p0, final String p1, final String p2, final T p3);
    
    protected abstract void checkInvalidDefinitionByDeploymentAndKey(final String p0, final String p1, final T p2);
    
    protected abstract void checkInvalidDefinitionWasCached(final String p0, final String p1, final T p2);
}
