// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.deployer;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionManager;
import java.util.Iterator;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import java.util.Collections;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.List;
import org.zik.bpm.engine.impl.core.model.Properties;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnTransformer;
import org.zik.bpm.engine.impl.dmn.DecisionLogger;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.zik.bpm.engine.impl.AbstractDefinitionDeployer;

public class DecisionRequirementsDefinitionDeployer extends AbstractDefinitionDeployer<DecisionRequirementsDefinitionEntity>
{
    protected static final DecisionLogger LOG;
    protected DmnTransformer transformer;
    
    @Override
    protected String[] getResourcesSuffixes() {
        return DecisionDefinitionDeployer.DMN_RESOURCE_SUFFIXES;
    }
    
    @Override
    protected List<DecisionRequirementsDefinitionEntity> transformDefinitions(final DeploymentEntity deployment, final ResourceEntity resource, final Properties properties) {
        final byte[] bytes = resource.getBytes();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            final DecisionRequirementsDefinitionEntity drd = (DecisionRequirementsDefinitionEntity)this.transformer.createTransform().modelInstance((InputStream)inputStream).transformDecisionRequirementsGraph();
            return Collections.singletonList(drd);
        }
        catch (Exception e) {
            throw DecisionRequirementsDefinitionDeployer.LOG.exceptionParseDmnResource(resource.getName(), e);
        }
    }
    
    @Override
    protected DecisionRequirementsDefinitionEntity findDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        return this.getDecisionRequirementsDefinitionManager().findDecisionRequirementsDefinitionByDeploymentAndKey(deploymentId, definitionKey);
    }
    
    @Override
    protected DecisionRequirementsDefinitionEntity findLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        return this.getDecisionRequirementsDefinitionManager().findLatestDecisionRequirementsDefinitionByKeyAndTenantId(definitionKey, tenantId);
    }
    
    @Override
    protected void persistDefinition(final DecisionRequirementsDefinitionEntity definition) {
        if (isDecisionRequirementsDefinitionPersistable(definition)) {
            this.getDecisionRequirementsDefinitionManager().insertDecisionRequirementsDefinition(definition);
        }
    }
    
    @Override
    protected void addDefinitionToDeploymentCache(final DeploymentCache deploymentCache, final DecisionRequirementsDefinitionEntity definition) {
        if (isDecisionRequirementsDefinitionPersistable(definition)) {
            deploymentCache.addDecisionRequirementsDefinition(definition);
        }
    }
    
    @Override
    protected void ensureNoDuplicateDefinitionKeys(final List<DecisionRequirementsDefinitionEntity> definitions) {
        final ArrayList<DecisionRequirementsDefinitionEntity> persistableDefinitions = new ArrayList<DecisionRequirementsDefinitionEntity>();
        for (final DecisionRequirementsDefinitionEntity definition : definitions) {
            if (isDecisionRequirementsDefinitionPersistable(definition)) {
                persistableDefinitions.add(definition);
            }
        }
        super.ensureNoDuplicateDefinitionKeys(persistableDefinitions);
    }
    
    public static boolean isDecisionRequirementsDefinitionPersistable(final DecisionRequirementsDefinitionEntity definition) {
        return definition.getDecisions().size() > 1;
    }
    
    @Override
    protected void updateDefinitionByPersistedDefinition(final DeploymentEntity deployment, final DecisionRequirementsDefinitionEntity definition, final DecisionRequirementsDefinitionEntity persistedDefinition) {
        if (persistedDefinition != null) {
            super.updateDefinitionByPersistedDefinition(deployment, definition, persistedDefinition);
        }
    }
    
    protected DecisionRequirementsDefinitionManager getDecisionRequirementsDefinitionManager() {
        return this.getCommandContext().getDecisionRequirementsDefinitionManager();
    }
    
    public DmnTransformer getTransformer() {
        return this.transformer;
    }
    
    public void setTransformer(final DmnTransformer transformer) {
        this.transformer = transformer;
    }
    
    static {
        LOG = ProcessEngineLogger.DECISION_LOGGER;
    }
}
