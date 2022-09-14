// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.entity.repository;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.zik.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class DecisionDefinitionManager extends AbstractManager implements AbstractResourceDefinitionManager<DecisionDefinitionEntity>
{
    protected static final EnginePersistenceLogger LOG;
    
    public void insertDecisionDefinition(final DecisionDefinitionEntity decisionDefinition) {
        this.getDbEntityManager().insert(decisionDefinition);
        this.createDefaultAuthorizations(decisionDefinition);
    }
    
    public void deleteDecisionDefinitionsByDeploymentId(final String deploymentId) {
        this.getDbEntityManager().delete(DecisionDefinitionEntity.class, "deleteDecisionDefinitionsByDeploymentId", deploymentId);
    }
    
    public DecisionDefinitionEntity findDecisionDefinitionById(final String decisionDefinitionId) {
        return this.getDbEntityManager().selectById(DecisionDefinitionEntity.class, decisionDefinitionId);
    }
    
    public DecisionDefinitionEntity findLatestDecisionDefinitionByKey(final String decisionDefinitionKey) {
        final List<DecisionDefinitionEntity> decisionDefinitions = (List<DecisionDefinitionEntity>)this.getDbEntityManager().selectList("selectLatestDecisionDefinitionByKey", this.configureParameterizedQuery(decisionDefinitionKey));
        if (decisionDefinitions.isEmpty()) {
            return null;
        }
        if (decisionDefinitions.size() == 1) {
            return decisionDefinitions.iterator().next();
        }
        throw DecisionDefinitionManager.LOG.multipleTenantsForDecisionDefinitionKeyException(decisionDefinitionKey);
    }
    
    public DecisionDefinitionEntity findLatestDecisionDefinitionByKeyAndTenantId(final String decisionDefinitionKey, final String tenantId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("decisionDefinitionKey", decisionDefinitionKey);
        parameters.put("tenantId", tenantId);
        if (tenantId == null) {
            return (DecisionDefinitionEntity)this.getDbEntityManager().selectOne("selectLatestDecisionDefinitionByKeyWithoutTenantId", parameters);
        }
        return (DecisionDefinitionEntity)this.getDbEntityManager().selectOne("selectLatestDecisionDefinitionByKeyAndTenantId", parameters);
    }
    
    public DecisionDefinitionEntity findDecisionDefinitionByKeyAndVersion(final String decisionDefinitionKey, final Integer decisionDefinitionVersion) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("decisionDefinitionVersion", decisionDefinitionVersion);
        parameters.put("decisionDefinitionKey", decisionDefinitionKey);
        return (DecisionDefinitionEntity)this.getDbEntityManager().selectOne("selectDecisionDefinitionByKeyAndVersion", this.configureParameterizedQuery(parameters));
    }
    
    public DecisionDefinitionEntity findDecisionDefinitionByKeyVersionAndTenantId(final String decisionDefinitionKey, final Integer decisionDefinitionVersion, final String tenantId) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("decisionDefinitionVersion", decisionDefinitionVersion);
        parameters.put("decisionDefinitionKey", decisionDefinitionKey);
        parameters.put("tenantId", tenantId);
        if (tenantId == null) {
            return (DecisionDefinitionEntity)this.getDbEntityManager().selectOne("selectDecisionDefinitionByKeyVersionWithoutTenantId", parameters);
        }
        return (DecisionDefinitionEntity)this.getDbEntityManager().selectOne("selectDecisionDefinitionByKeyVersionAndTenantId", parameters);
    }
    
    public DecisionDefinitionEntity findDecisionDefinitionByKeyVersionTagAndTenantId(final String decisionDefinitionKey, final String decisionDefinitionVersionTag, final String tenantId) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("decisionDefinitionVersionTag", decisionDefinitionVersionTag);
        parameters.put("decisionDefinitionKey", decisionDefinitionKey);
        parameters.put("tenantId", tenantId);
        final ListQueryParameterObject parameterObject = new ListQueryParameterObject();
        parameterObject.setParameter(parameters);
        final List<DecisionDefinitionEntity> decisionDefinitions = (List<DecisionDefinitionEntity>)this.getDbEntityManager().selectList("selectDecisionDefinitionByKeyVersionTag", parameterObject);
        if (decisionDefinitions.size() == 1) {
            return decisionDefinitions.get(0);
        }
        if (decisionDefinitions.isEmpty()) {
            return null;
        }
        throw DecisionDefinitionManager.LOG.multipleDefinitionsForVersionTagException(decisionDefinitionKey, decisionDefinitionVersionTag);
    }
    
    public DecisionDefinitionEntity findDecisionDefinitionByDeploymentAndKey(final String deploymentId, final String decisionDefinitionKey) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("deploymentId", deploymentId);
        parameters.put("decisionDefinitionKey", decisionDefinitionKey);
        return (DecisionDefinitionEntity)this.getDbEntityManager().selectOne("selectDecisionDefinitionByDeploymentAndKey", parameters);
    }
    
    public List<DecisionDefinition> findDecisionDefinitionsByQueryCriteria(final DecisionDefinitionQueryImpl decisionDefinitionQuery, final Page page) {
        this.configureDecisionDefinitionQuery(decisionDefinitionQuery);
        return (List<DecisionDefinition>)this.getDbEntityManager().selectList("selectDecisionDefinitionsByQueryCriteria", decisionDefinitionQuery, page);
    }
    
    public long findDecisionDefinitionCountByQueryCriteria(final DecisionDefinitionQueryImpl decisionDefinitionQuery) {
        this.configureDecisionDefinitionQuery(decisionDefinitionQuery);
        return (long)this.getDbEntityManager().selectOne("selectDecisionDefinitionCountByQueryCriteria", decisionDefinitionQuery);
    }
    
    public String findPreviousDecisionDefinitionId(final String decisionDefinitionKey, final Integer version, final String tenantId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", decisionDefinitionKey);
        params.put("version", version);
        params.put("tenantId", tenantId);
        return (String)this.getDbEntityManager().selectOne("selectPreviousDecisionDefinitionId", params);
    }
    
    public List<DecisionDefinition> findDecisionDefinitionByDeploymentId(final String deploymentId) {
        return (List<DecisionDefinition>)this.getDbEntityManager().selectList("selectDecisionDefinitionByDeploymentId", deploymentId);
    }
    
    protected void createDefaultAuthorizations(final DecisionDefinition decisionDefinition) {
        if (this.isAuthorizationEnabled()) {
            final ResourceAuthorizationProvider provider = this.getResourceAuthorizationProvider();
            final AuthorizationEntity[] authorizations = provider.newDecisionDefinition(decisionDefinition);
            this.saveDefaultAuthorizations(authorizations);
        }
    }
    
    protected void configureDecisionDefinitionQuery(final DecisionDefinitionQueryImpl query) {
        this.getAuthorizationManager().configureDecisionDefinitionQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
    
    @Override
    public DecisionDefinitionEntity findLatestDefinitionById(final String id) {
        return this.findDecisionDefinitionById(id);
    }
    
    @Override
    public DecisionDefinitionEntity findLatestDefinitionByKey(final String key) {
        return this.findLatestDecisionDefinitionByKey(key);
    }
    
    @Override
    public DecisionDefinitionEntity getCachedResourceDefinitionEntity(final String definitionId) {
        return this.getDbEntityManager().getCachedEntity(DecisionDefinitionEntity.class, definitionId);
    }
    
    @Override
    public DecisionDefinitionEntity findLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        return this.findLatestDecisionDefinitionByKeyAndTenantId(definitionKey, tenantId);
    }
    
    @Override
    public DecisionDefinitionEntity findDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId) {
        return this.findDecisionDefinitionByKeyVersionAndTenantId(definitionKey, definitionVersion, tenantId);
    }
    
    @Override
    public DecisionDefinitionEntity findDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId) {
        return this.findDecisionDefinitionByKeyVersionTagAndTenantId(definitionKey, definitionVersionTag, tenantId);
    }
    
    @Override
    public DecisionDefinitionEntity findDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        return this.findDecisionDefinitionByDeploymentAndKey(deploymentId, definitionKey);
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
