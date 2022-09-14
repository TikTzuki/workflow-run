// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.deployer;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionManager;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import java.util.List;
import org.zik.bpm.engine.impl.core.model.Properties;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.cmmn.transformer.CmmnTransformer;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.AbstractDefinitionDeployer;

public class CmmnDeployer extends AbstractDefinitionDeployer<CaseDefinitionEntity>
{
    public static final String[] CMMN_RESOURCE_SUFFIXES;
    protected ExpressionManager expressionManager;
    protected CmmnTransformer transformer;
    
    @Override
    protected String[] getResourcesSuffixes() {
        return CmmnDeployer.CMMN_RESOURCE_SUFFIXES;
    }
    
    @Override
    protected List<CaseDefinitionEntity> transformDefinitions(final DeploymentEntity deployment, final ResourceEntity resource, final Properties properties) {
        return this.transformer.createTransform().deployment(deployment).resource(resource).transform();
    }
    
    @Override
    protected CaseDefinitionEntity findDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        return this.getCaseDefinitionManager().findCaseDefinitionByDeploymentAndKey(deploymentId, definitionKey);
    }
    
    @Override
    protected CaseDefinitionEntity findLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        return this.getCaseDefinitionManager().findLatestCaseDefinitionByKeyAndTenantId(definitionKey, tenantId);
    }
    
    @Override
    protected void persistDefinition(final CaseDefinitionEntity definition) {
        this.getCaseDefinitionManager().insertCaseDefinition(definition);
    }
    
    @Override
    protected void addDefinitionToDeploymentCache(final DeploymentCache deploymentCache, final CaseDefinitionEntity definition) {
        deploymentCache.addCaseDefinition(definition);
    }
    
    protected CaseDefinitionManager getCaseDefinitionManager() {
        return this.getCommandContext().getCaseDefinitionManager();
    }
    
    public ExpressionManager getExpressionManager() {
        return this.expressionManager;
    }
    
    public void setExpressionManager(final ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }
    
    public CmmnTransformer getTransformer() {
        return this.transformer;
    }
    
    public void setTransformer(final CmmnTransformer transformer) {
        this.transformer = transformer;
    }
    
    static {
        CMMN_RESOURCE_SUFFIXES = new String[] { "cmmn11.xml", "cmmn10.xml", "cmmn" };
    }
}
