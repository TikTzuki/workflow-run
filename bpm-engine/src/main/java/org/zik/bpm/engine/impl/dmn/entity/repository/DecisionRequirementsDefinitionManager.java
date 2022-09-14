// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.entity.repository;

import org.zik.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.zik.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.Page;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class DecisionRequirementsDefinitionManager extends AbstractManager implements AbstractResourceDefinitionManager<DecisionRequirementsDefinitionEntity>
{
    public void insertDecisionRequirementsDefinition(final DecisionRequirementsDefinitionEntity decisionRequirementsDefinition) {
        this.getDbEntityManager().insert(decisionRequirementsDefinition);
        this.createDefaultAuthorizations(decisionRequirementsDefinition);
    }
    
    public void deleteDecisionRequirementsDefinitionsByDeploymentId(final String deploymentId) {
        this.getDbEntityManager().delete(DecisionDefinitionEntity.class, "deleteDecisionRequirementsDefinitionsByDeploymentId", deploymentId);
    }
    
    public DecisionRequirementsDefinitionEntity findDecisionRequirementsDefinitionById(final String decisionRequirementsDefinitionId) {
        return this.getDbEntityManager().selectById(DecisionRequirementsDefinitionEntity.class, decisionRequirementsDefinitionId);
    }
    
    public String findPreviousDecisionRequirementsDefinitionId(final String decisionRequirementsDefinitionKey, final Integer version, final String tenantId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", decisionRequirementsDefinitionKey);
        params.put("version", version);
        params.put("tenantId", tenantId);
        return (String)this.getDbEntityManager().selectOne("selectPreviousDecisionRequirementsDefinitionId", params);
    }
    
    public List<DecisionRequirementsDefinition> findDecisionRequirementsDefinitionByDeploymentId(final String deploymentId) {
        return (List<DecisionRequirementsDefinition>)this.getDbEntityManager().selectList("selectDecisionRequirementsDefinitionByDeploymentId", deploymentId);
    }
    
    public DecisionRequirementsDefinitionEntity findDecisionRequirementsDefinitionByDeploymentAndKey(final String deploymentId, final String decisionRequirementsDefinitionKey) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("deploymentId", deploymentId);
        parameters.put("decisionRequirementsDefinitionKey", decisionRequirementsDefinitionKey);
        return (DecisionRequirementsDefinitionEntity)this.getDbEntityManager().selectOne("selectDecisionRequirementsDefinitionByDeploymentAndKey", parameters);
    }
    
    public DecisionRequirementsDefinitionEntity findLatestDecisionRequirementsDefinitionByKeyAndTenantId(final String decisionRequirementsDefinitionKey, final String tenantId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("decisionRequirementsDefinitionKey", decisionRequirementsDefinitionKey);
        parameters.put("tenantId", tenantId);
        if (tenantId == null) {
            return (DecisionRequirementsDefinitionEntity)this.getDbEntityManager().selectOne("selectLatestDecisionRequirementsDefinitionByKeyWithoutTenantId", parameters);
        }
        return (DecisionRequirementsDefinitionEntity)this.getDbEntityManager().selectOne("selectLatestDecisionRequirementsDefinitionByKeyAndTenantId", parameters);
    }
    
    public List<DecisionRequirementsDefinition> findDecisionRequirementsDefinitionsByQueryCriteria(final DecisionRequirementsDefinitionQueryImpl query, final Page page) {
        this.configureDecisionRequirementsDefinitionQuery(query);
        return (List<DecisionRequirementsDefinition>)this.getDbEntityManager().selectList("selectDecisionRequirementsDefinitionsByQueryCriteria", query, page);
    }
    
    public long findDecisionRequirementsDefinitionCountByQueryCriteria(final DecisionRequirementsDefinitionQueryImpl query) {
        this.configureDecisionRequirementsDefinitionQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectDecisionRequirementsDefinitionCountByQueryCriteria", query);
    }
    
    protected void createDefaultAuthorizations(final DecisionRequirementsDefinition decisionRequirementsDefinition) {
        if (this.isAuthorizationEnabled()) {
            final ResourceAuthorizationProvider provider = this.getResourceAuthorizationProvider();
            final AuthorizationEntity[] authorizations = provider.newDecisionRequirementsDefinition(decisionRequirementsDefinition);
            this.saveDefaultAuthorizations(authorizations);
        }
    }
    
    protected void configureDecisionRequirementsDefinitionQuery(final DecisionRequirementsDefinitionQueryImpl query) {
        this.getAuthorizationManager().configureDecisionRequirementsDefinitionQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    @Override
    public DecisionRequirementsDefinitionEntity findLatestDefinitionByKey(final String key) {
        return null;
    }
    
    @Override
    public DecisionRequirementsDefinitionEntity findLatestDefinitionById(final String id) {
        return this.getDbEntityManager().selectById(DecisionRequirementsDefinitionEntity.class, id);
    }
    
    @Override
    public DecisionRequirementsDefinitionEntity findLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        return null;
    }
    
    @Override
    public DecisionRequirementsDefinitionEntity findDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId) {
        return null;
    }
    
    @Override
    public DecisionRequirementsDefinitionEntity findDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId) {
        return null;
    }
    
    @Override
    public DecisionRequirementsDefinitionEntity findDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        return null;
    }
    
    @Override
    public DecisionRequirementsDefinitionEntity getCachedResourceDefinitionEntity(final String definitionId) {
        return this.getDbEntityManager().getCachedEntity(DecisionRequirementsDefinitionEntity.class, definitionId);
    }
}
