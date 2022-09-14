// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import java.util.Set;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.HashSet;
import java.util.Map;
import java.util.Collection;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.core.model.Properties;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.cfg.IdGenerator;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.impl.persistence.deploy.Deployer;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;

public abstract class AbstractDefinitionDeployer<DefinitionEntity extends ResourceDefinitionEntity> implements Deployer
{
    public static final String[] DIAGRAM_SUFFIXES;
    private final CommandLogger LOG;
    protected IdGenerator idGenerator;
    
    public AbstractDefinitionDeployer() {
        this.LOG = ProcessEngineLogger.CMD_LOGGER;
    }
    
    public IdGenerator getIdGenerator() {
        return this.idGenerator;
    }
    
    public void setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
    
    @Override
    public void deploy(final DeploymentEntity deployment) {
        this.LOG.debugProcessingDeployment(deployment.getName());
        final Properties properties = new Properties();
        final List<DefinitionEntity> definitions = this.parseDefinitionResources(deployment, properties);
        this.ensureNoDuplicateDefinitionKeys(definitions);
        this.postProcessDefinitions(deployment, definitions, properties);
    }
    
    protected List<DefinitionEntity> parseDefinitionResources(final DeploymentEntity deployment, final Properties properties) {
        final List<DefinitionEntity> definitions = new ArrayList<DefinitionEntity>();
        for (final ResourceEntity resource : deployment.getResources().values()) {
            this.LOG.debugProcessingResource(resource.getName());
            if (this.isResourceHandled(resource)) {
                definitions.addAll(this.transformResource(deployment, resource, properties));
            }
        }
        return definitions;
    }
    
    protected boolean isResourceHandled(final ResourceEntity resource) {
        final String resourceName = resource.getName();
        for (final String suffix : this.getResourcesSuffixes()) {
            if (resourceName.endsWith(suffix)) {
                return true;
            }
        }
        return false;
    }
    
    protected abstract String[] getResourcesSuffixes();
    
    protected Collection<DefinitionEntity> transformResource(final DeploymentEntity deployment, final ResourceEntity resource, final Properties properties) {
        final String resourceName = resource.getName();
        final List<DefinitionEntity> definitions = this.transformDefinitions(deployment, resource, properties);
        for (final DefinitionEntity definition : definitions) {
            definition.setResourceName(resourceName);
            final String diagramResourceName = this.getDiagramResourceForDefinition(deployment, resourceName, definition, deployment.getResources());
            if (diagramResourceName != null) {
                definition.setDiagramResourceName(diagramResourceName);
            }
        }
        return definitions;
    }
    
    protected abstract List<DefinitionEntity> transformDefinitions(final DeploymentEntity p0, final ResourceEntity p1, final Properties p2);
    
    protected String getDiagramResourceForDefinition(final DeploymentEntity deployment, final String resourceName, final DefinitionEntity definition, final Map<String, ResourceEntity> resources) {
        for (final String diagramSuffix : this.getDiagramSuffixes()) {
            final String definitionDiagramResource = this.getDefinitionDiagramResourceName(resourceName, definition, diagramSuffix);
            final String diagramForFileResource = this.getGeneralDiagramResourceName(resourceName, definition, diagramSuffix);
            if (resources.containsKey(definitionDiagramResource)) {
                return definitionDiagramResource;
            }
            if (resources.containsKey(diagramForFileResource)) {
                return diagramForFileResource;
            }
        }
        return null;
    }
    
    protected String getDefinitionDiagramResourceName(final String resourceName, final DefinitionEntity definition, final String diagramSuffix) {
        final String fileResourceBase = this.stripDefinitionFileSuffix(resourceName);
        final String definitionKey = definition.getKey();
        return fileResourceBase + definitionKey + "." + diagramSuffix;
    }
    
    protected String getGeneralDiagramResourceName(final String resourceName, final DefinitionEntity definition, final String diagramSuffix) {
        final String fileResourceBase = this.stripDefinitionFileSuffix(resourceName);
        return fileResourceBase + diagramSuffix;
    }
    
    protected String stripDefinitionFileSuffix(final String resourceName) {
        for (final String suffix : this.getResourcesSuffixes()) {
            if (resourceName.endsWith(suffix)) {
                return resourceName.substring(0, resourceName.length() - suffix.length());
            }
        }
        return resourceName;
    }
    
    protected String[] getDiagramSuffixes() {
        return AbstractDefinitionDeployer.DIAGRAM_SUFFIXES;
    }
    
    protected void ensureNoDuplicateDefinitionKeys(final List<DefinitionEntity> definitions) {
        final Set<String> keys = new HashSet<String>();
        for (final DefinitionEntity definition : definitions) {
            final String key = definition.getKey();
            if (keys.contains(key)) {
                throw new ProcessEngineException("The deployment contains definitions with the same key '" + key + "' (id attribute), this is not allowed");
            }
            keys.add(key);
        }
    }
    
    protected void postProcessDefinitions(final DeploymentEntity deployment, final List<DefinitionEntity> definitions, final Properties properties) {
        if (deployment.isNew()) {
            this.persistDefinitions(deployment, definitions, properties);
        }
        else {
            this.loadDefinitions(deployment, definitions, properties);
        }
    }
    
    protected void persistDefinitions(final DeploymentEntity deployment, final List<DefinitionEntity> definitions, final Properties properties) {
        for (final DefinitionEntity definition : definitions) {
            final String definitionKey = definition.getKey();
            final String tenantId = deployment.getTenantId();
            final DefinitionEntity latestDefinition = this.findLatestDefinitionByKeyAndTenantId(definitionKey, tenantId);
            this.updateDefinitionByLatestDefinition(deployment, definition, latestDefinition);
            this.persistDefinition(definition);
            this.registerDefinition(deployment, definition, properties);
        }
    }
    
    protected void updateDefinitionByLatestDefinition(final DeploymentEntity deployment, final DefinitionEntity definition, final DefinitionEntity latestDefinition) {
        definition.setVersion(this.getNextVersion(deployment, definition, latestDefinition));
        definition.setId(this.generateDefinitionId(deployment, definition, latestDefinition));
        definition.setDeploymentId(deployment.getId());
        definition.setTenantId(deployment.getTenantId());
    }
    
    protected void loadDefinitions(final DeploymentEntity deployment, final List<DefinitionEntity> definitions, final Properties properties) {
        for (final DefinitionEntity definition : definitions) {
            final String deploymentId = deployment.getId();
            final String definitionKey = definition.getKey();
            final DefinitionEntity persistedDefinition = this.findDefinitionByDeploymentAndKey(deploymentId, definitionKey);
            this.handlePersistedDefinition(definition, persistedDefinition, deployment, properties);
        }
    }
    
    protected void handlePersistedDefinition(final DefinitionEntity definition, final DefinitionEntity persistedDefinition, final DeploymentEntity deployment, final Properties properties) {
        this.persistedDefinitionLoaded(deployment, definition, persistedDefinition);
        this.updateDefinitionByPersistedDefinition(deployment, definition, persistedDefinition);
        this.registerDefinition(deployment, definition, properties);
    }
    
    protected void updateDefinitionByPersistedDefinition(final DeploymentEntity deployment, final DefinitionEntity definition, final DefinitionEntity persistedDefinition) {
        definition.setVersion(persistedDefinition.getVersion());
        definition.setId(persistedDefinition.getId());
        definition.setDeploymentId(deployment.getId());
        definition.setTenantId(persistedDefinition.getTenantId());
    }
    
    protected void persistedDefinitionLoaded(final DeploymentEntity deployment, final DefinitionEntity definition, final DefinitionEntity persistedDefinition) {
    }
    
    protected abstract DefinitionEntity findDefinitionByDeploymentAndKey(final String p0, final String p1);
    
    protected abstract DefinitionEntity findLatestDefinitionByKeyAndTenantId(final String p0, final String p1);
    
    protected abstract void persistDefinition(final DefinitionEntity p0);
    
    protected void registerDefinition(final DeploymentEntity deployment, final DefinitionEntity definition, final Properties properties) {
        final DeploymentCache deploymentCache = this.getDeploymentCache();
        this.addDefinitionToDeploymentCache(deploymentCache, definition);
        this.definitionAddedToDeploymentCache(deployment, definition, properties);
        deployment.addDeployedArtifact(definition);
    }
    
    protected abstract void addDefinitionToDeploymentCache(final DeploymentCache p0, final DefinitionEntity p1);
    
    protected void definitionAddedToDeploymentCache(final DeploymentEntity deployment, final DefinitionEntity definition, final Properties properties) {
    }
    
    protected int getNextVersion(final DeploymentEntity deployment, final DefinitionEntity newDefinition, final DefinitionEntity latestDefinition) {
        int result = 1;
        if (latestDefinition != null) {
            final int latestVersion = latestDefinition.getVersion();
            result = latestVersion + 1;
        }
        return result;
    }
    
    protected String generateDefinitionId(final DeploymentEntity deployment, final DefinitionEntity newDefinition, final DefinitionEntity latestDefinition) {
        final String nextId = this.idGenerator.getNextId();
        final String definitionKey = newDefinition.getKey();
        final int definitionVersion = newDefinition.getVersion();
        String definitionId = definitionKey + ":" + definitionVersion + ":" + nextId;
        if (definitionId.length() > 64) {
            definitionId = nextId;
        }
        return definitionId;
    }
    
    protected ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return Context.getProcessEngineConfiguration();
    }
    
    protected CommandContext getCommandContext() {
        return Context.getCommandContext();
    }
    
    protected DeploymentCache getDeploymentCache() {
        return this.getProcessEngineConfiguration().getDeploymentCache();
    }
    
    static {
        DIAGRAM_SUFFIXES = new String[] { "png", "jpg", "gif", "svg" };
    }
}
