// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.persistence.entity.CamundaFormDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.AbstractResourceDefinitionManager;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class CamundaFormDefinitionManager extends AbstractManager implements AbstractResourceDefinitionManager<CamundaFormDefinitionEntity>
{
    protected static final EnginePersistenceLogger LOG;
    
    @Override
    public CamundaFormDefinitionEntity findLatestDefinitionByKey(final String key) {
        final List<CamundaFormDefinitionEntity> camundaFormDefinitions = (List<CamundaFormDefinitionEntity>)this.getDbEntityManager().selectList("selectLatestCamundaFormDefinitionByKey", this.configureParameterizedQuery(key));
        if (camundaFormDefinitions.isEmpty()) {
            return null;
        }
        if (camundaFormDefinitions.size() == 1) {
            return camundaFormDefinitions.iterator().next();
        }
        throw CamundaFormDefinitionManager.LOG.multipleTenantsForCamundaFormDefinitionKeyException(key);
    }
    
    @Override
    public CamundaFormDefinitionEntity findLatestDefinitionById(final String id) {
        return this.getDbEntityManager().selectById(CamundaFormDefinitionEntity.class, id);
    }
    
    @Override
    public CamundaFormDefinitionEntity findLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("camundaFormDefinitionKey", definitionKey);
        parameters.put("tenantId", tenantId);
        if (tenantId == null) {
            return (CamundaFormDefinitionEntity)this.getDbEntityManager().selectOne("selectLatestCamundaFormDefinitionByKeyWithoutTenantId", parameters);
        }
        return (CamundaFormDefinitionEntity)this.getDbEntityManager().selectOne("selectLatestCamundaDefinitionByKeyAndTenantId", parameters);
    }
    
    @Override
    public CamundaFormDefinitionEntity findDefinitionByKeyVersionAndTenantId(final String definitionKey, final Integer definitionVersion, final String tenantId) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("camundaFormDefinitionVersion", definitionVersion);
        parameters.put("camundaFormDefinitionKey", definitionKey);
        parameters.put("tenantId", tenantId);
        if (tenantId == null) {
            return (CamundaFormDefinitionEntity)this.getDbEntityManager().selectOne("selectCamundaFormDefinitionByKeyVersionWithoutTenantId", parameters);
        }
        return (CamundaFormDefinitionEntity)this.getDbEntityManager().selectOne("selectCamundaFormDefinitionByKeyVersionAndTenantId", parameters);
    }
    
    @Override
    public CamundaFormDefinitionEntity findDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("deploymentId", deploymentId);
        parameters.put("camundaFormDefinitionKey", definitionKey);
        return (CamundaFormDefinitionEntity)this.getDbEntityManager().selectOne("selectCamundaFormDefinitionByDeploymentAndKey", parameters);
    }
    
    public List<CamundaFormDefinitionEntity> findDefinitionsByDeploymentId(final String deploymentId) {
        return (List<CamundaFormDefinitionEntity>)this.getDbEntityManager().selectList("selectCamundaFormDefinitionByDeploymentId", deploymentId);
    }
    
    @Override
    public CamundaFormDefinitionEntity getCachedResourceDefinitionEntity(final String definitionId) {
        return this.getDbEntityManager().getCachedEntity(CamundaFormDefinitionEntity.class, definitionId);
    }
    
    @Override
    public CamundaFormDefinitionEntity findDefinitionByKeyVersionTagAndTenantId(final String definitionKey, final String definitionVersionTag, final String tenantId) {
        throw new UnsupportedOperationException("Currently finding Camunda Form definition by version tag and tenant is not implemented.");
    }
    
    public void deleteCamundaFormDefinitionsByDeploymentId(final String deploymentId) {
        this.getDbEntityManager().delete(CamundaFormDefinitionEntity.class, "deleteCamundaFormDefinitionsByDeploymentId", deploymentId);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
