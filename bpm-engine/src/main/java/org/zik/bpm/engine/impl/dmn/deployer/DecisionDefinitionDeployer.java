// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.deployer;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionManager;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import java.util.Iterator;
import java.util.Collection;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.camunda.bpm.dmn.engine.DmnDecision;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.core.model.Properties;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnTransformer;
import org.zik.bpm.engine.impl.dmn.DecisionLogger;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.impl.AbstractDefinitionDeployer;

public class DecisionDefinitionDeployer extends AbstractDefinitionDeployer<DecisionDefinitionEntity>
{
    protected static final DecisionLogger LOG;
    public static final String[] DMN_RESOURCE_SUFFIXES;
    protected DmnTransformer transformer;
    
    @Override
    protected String[] getResourcesSuffixes() {
        return DecisionDefinitionDeployer.DMN_RESOURCE_SUFFIXES;
    }
    
    @Override
    protected List<DecisionDefinitionEntity> transformDefinitions(final DeploymentEntity deployment, final ResourceEntity resource, final Properties properties) {
        final List<DecisionDefinitionEntity> decisions = new ArrayList<DecisionDefinitionEntity>();
        final DecisionRequirementsDefinitionEntity deployedDrd = this.findDeployedDrdForResource(deployment, resource.getName());
        if (deployedDrd == null) {
            throw DecisionDefinitionDeployer.LOG.exceptionNoDrdForResource(resource.getName());
        }
        final Collection<DmnDecision> decisionsOfDrd = (Collection<DmnDecision>)deployedDrd.getDecisions();
        for (final DmnDecision decisionOfDrd : decisionsOfDrd) {
            final DecisionDefinitionEntity decisionEntity = (DecisionDefinitionEntity)decisionOfDrd;
            if (DecisionRequirementsDefinitionDeployer.isDecisionRequirementsDefinitionPersistable(deployedDrd)) {
                decisionEntity.setDecisionRequirementsDefinitionId(deployedDrd.getId());
                decisionEntity.setDecisionRequirementsDefinitionKey(deployedDrd.getKey());
            }
            decisions.add(decisionEntity);
        }
        if (!DecisionRequirementsDefinitionDeployer.isDecisionRequirementsDefinitionPersistable(deployedDrd)) {
            deployment.removeArtifact(deployedDrd);
        }
        return decisions;
    }
    
    protected DecisionRequirementsDefinitionEntity findDeployedDrdForResource(final DeploymentEntity deployment, final String resourceName) {
        final List<DecisionRequirementsDefinitionEntity> deployedDrds = deployment.getDeployedArtifacts(DecisionRequirementsDefinitionEntity.class);
        if (deployedDrds != null) {
            for (final DecisionRequirementsDefinitionEntity deployedDrd : deployedDrds) {
                if (deployedDrd.getResourceName().equals(resourceName)) {
                    return deployedDrd;
                }
            }
        }
        return null;
    }
    
    @Override
    protected DecisionDefinitionEntity findDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        return this.getDecisionDefinitionManager().findDecisionDefinitionByDeploymentAndKey(deploymentId, definitionKey);
    }
    
    @Override
    protected DecisionDefinitionEntity findLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        return this.getDecisionDefinitionManager().findLatestDecisionDefinitionByKeyAndTenantId(definitionKey, tenantId);
    }
    
    @Override
    protected void persistDefinition(final DecisionDefinitionEntity definition) {
        this.getDecisionDefinitionManager().insertDecisionDefinition(definition);
    }
    
    @Override
    protected void addDefinitionToDeploymentCache(final DeploymentCache deploymentCache, final DecisionDefinitionEntity definition) {
        deploymentCache.addDecisionDefinition(definition);
    }
    
    protected DecisionDefinitionManager getDecisionDefinitionManager() {
        return this.getCommandContext().getDecisionDefinitionManager();
    }
    
    public DmnTransformer getTransformer() {
        return this.transformer;
    }
    
    public void setTransformer(final DmnTransformer transformer) {
        this.transformer = transformer;
    }
    
    static {
        LOG = ProcessEngineLogger.DECISION_LOGGER;
        DMN_RESOURCE_SUFFIXES = new String[] { "dmn11.xml", "dmn" };
    }
}
