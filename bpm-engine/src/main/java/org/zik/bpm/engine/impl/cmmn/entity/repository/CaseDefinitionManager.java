// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.entity.repository;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.impl.Page;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class CaseDefinitionManager extends AbstractManager implements AbstractResourceDefinitionManager<CaseDefinitionEntity>
{
    protected static final EnginePersistenceLogger LOG;
    
    public void insertCaseDefinition(final CaseDefinitionEntity caseDefinition) {
        this.getDbEntityManager().insert(caseDefinition);
    }
    
    public void deleteCaseDefinitionsByDeploymentId(final String deploymentId) {
        this.getDbEntityManager().delete(CaseDefinitionEntity.class, "deleteCaseDefinitionsByDeploymentId", deploymentId);
    }
    
    public CaseDefinitionEntity findCaseDefinitionById(final String caseDefinitionId) {
        return this.getDbEntityManager().selectById(CaseDefinitionEntity.class, caseDefinitionId);
    }
    
    public CaseDefinitionEntity findLatestCaseDefinitionByKey(final String caseDefinitionKey) {
        final List<CaseDefinitionEntity> caseDefinitions = (List<CaseDefinitionEntity>)this.getDbEntityManager().selectList("selectLatestCaseDefinitionByKey", this.configureParameterizedQuery(caseDefinitionKey));
        if (caseDefinitions.isEmpty()) {
            return null;
        }
        if (caseDefinitions.size() == 1) {
            return caseDefinitions.iterator().next();
        }
        throw CaseDefinitionManager.LOG.multipleTenantsForCaseDefinitionKeyException(caseDefinitionKey);
    }
    
    public CaseDefinitionEntity findLatestCaseDefinitionByKeyAndTenantId(final String caseDefinitionKey, final String tenantId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("caseDefinitionKey", caseDefinitionKey);
        parameters.put("tenantId", tenantId);
        if (tenantId == null) {
            return (CaseDefinitionEntity)this.getDbEntityManager().selectOne("selectLatestCaseDefinitionByKeyWithoutTenantId", parameters);
        }
        return (CaseDefinitionEntity)this.getDbEntityManager().selectOne("selectLatestCaseDefinitionByKeyAndTenantId", parameters);
    }
    
    public CaseDefinitionEntity findCaseDefinitionByKeyVersionAndTenantId(final String caseDefinitionKey, final Integer caseDefinitionVersion, final String tenantId) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("caseDefinitionVersion", caseDefinitionVersion);
        parameters.put("caseDefinitionKey", caseDefinitionKey);
        parameters.put("tenantId", tenantId);
        return (CaseDefinitionEntity)this.getDbEntityManager().selectOne("selectCaseDefinitionByKeyVersionAndTenantId", parameters);
    }
    
    public CaseDefinitionEntity findCaseDefinitionByDeploymentAndKey(final String deploymentId, final String caseDefinitionKey) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("deploymentId", deploymentId);
        parameters.put("caseDefinitionKey", caseDefinitionKey);
        return (CaseDefinitionEntity)this.getDbEntityManager().selectOne("selectCaseDefinitionByDeploymentAndKey", parameters);
    }
    
    public String findPreviousCaseDefinitionId(final String caseDefinitionKey, final Integer version, final String tenantId) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("key", caseDefinitionKey);
        params.put("version", version);
        params.put("tenantId", tenantId);
        return (String)this.getDbEntityManager().selectOne("selectPreviousCaseDefinitionId", params);
    }
    
    public List<CaseDefinition> findCaseDefinitionsByQueryCriteria(final CaseDefinitionQueryImpl caseDefinitionQuery, final Page page) {
        this.configureCaseDefinitionQuery(caseDefinitionQuery);
        return (List<CaseDefinition>)this.getDbEntityManager().selectList("selectCaseDefinitionsByQueryCriteria", caseDefinitionQuery, page);
    }
    
    public long findCaseDefinitionCountByQueryCriteria(final CaseDefinitionQueryImpl caseDefinitionQuery) {
        this.configureCaseDefinitionQuery(caseDefinitionQuery);
        return (long)this.getDbEntityManager().selectOne("selectCaseDefinitionCountByQueryCriteria", caseDefinitionQuery);
    }
    
    public List<CaseDefinition> findCaseDefinitionByDeploymentId(final String deploymentId) {
        return (List<CaseDefinition>)this.getDbEntityManager().selectList("selectCaseDefinitionByDeploymentId", deploymentId);
    }
    
    protected void configureCaseDefinitionQuery(final CaseDefinitionQueryImpl query) {
        this.getTenantManager().configureQuery(query);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
    
    @Override
    public CaseDefinitionEntity findLatestDefinitionByKey(final String key) {
        return this.findLatestCaseDefinitionByKey(key);
    }
    
    @Override
    public CaseDefinitionEntity findLatestDefinitionById(final String id) {
        return this.findCaseDefinitionById(id);
    }
    
    @Override
    public CaseDefinitionEntity getCachedResourceDefinitionEntity(final String definitionId) {
        return this.getDbEntityManager().getCachedEntity(CaseDefinitionEntity.class, definitionId);
    }
    
    @Override
    public CaseDefinitionEntity findLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        return this.findLatestCaseDefinitionByKeyAndTenantId(definitionKey, tenantId);
    }
    
    @Override
    public CaseDefinitionEntity findDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId) {
        throw new UnsupportedOperationException("Currently finding case definition by version tag and tenant is not implemented.");
    }
    
    @Override
    public CaseDefinitionEntity findDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId) {
        return this.findCaseDefinitionByKeyVersionAndTenantId(definitionKey, definitionVersion, tenantId);
    }
    
    @Override
    public CaseDefinitionEntity findDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        return this.findCaseDefinitionByDeploymentAndKey(deploymentId, definitionKey);
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
