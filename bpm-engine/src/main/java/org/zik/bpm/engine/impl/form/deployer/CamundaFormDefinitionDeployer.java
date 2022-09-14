// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.deployer;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Collections;
import org.zik.bpm.engine.impl.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.zik.bpm.engine.impl.core.model.Properties;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.util.EngineUtilLogger;
import org.zik.bpm.engine.impl.persistence.entity.CamundaFormDefinitionEntity;
import org.zik.bpm.engine.impl.AbstractDefinitionDeployer;

public class CamundaFormDefinitionDeployer extends AbstractDefinitionDeployer<CamundaFormDefinitionEntity>
{
    protected static final EngineUtilLogger LOG;
    public static final String[] FORM_RESOURCE_SUFFIXES;
    
    @Override
    protected String[] getResourcesSuffixes() {
        return CamundaFormDefinitionDeployer.FORM_RESOURCE_SUFFIXES;
    }
    
    @Override
    protected List<CamundaFormDefinitionEntity> transformDefinitions(final DeploymentEntity deployment, final ResourceEntity resource, final Properties properties) {
        final String formContent = new String(resource.getBytes(), StandardCharsets.UTF_8);
        try {
            final JsonObject formJsonObject = new Gson().fromJson(formContent, JsonObject.class);
            final String camundaFormDefinitionKey = JsonUtil.getString(formJsonObject, "id");
            final CamundaFormDefinitionEntity definition = new CamundaFormDefinitionEntity(camundaFormDefinitionKey, deployment.getId(), resource.getName(), deployment.getTenantId());
            return Collections.singletonList(definition);
        }
        catch (Exception e) {
            if (!this.getCommandContext().getProcessEngineConfiguration().isDisableStrictCamundaFormParsing()) {
                throw CamundaFormDefinitionDeployer.LOG.exceptionDuringFormParsing(e.getMessage(), resource.getName());
            }
            return Collections.emptyList();
        }
    }
    
    @Override
    protected CamundaFormDefinitionEntity findDefinitionByDeploymentAndKey(final String deploymentId, final String definitionKey) {
        return this.getCommandContext().getCamundaFormDefinitionManager().findDefinitionByDeploymentAndKey(deploymentId, definitionKey);
    }
    
    @Override
    protected CamundaFormDefinitionEntity findLatestDefinitionByKeyAndTenantId(final String definitionKey, final String tenantId) {
        return this.getCommandContext().getCamundaFormDefinitionManager().findLatestDefinitionByKeyAndTenantId(definitionKey, tenantId);
    }
    
    @Override
    protected void persistDefinition(final CamundaFormDefinitionEntity definition) {
        this.getCommandContext().getCamundaFormDefinitionManager().insert(definition);
    }
    
    @Override
    protected void addDefinitionToDeploymentCache(final DeploymentCache deploymentCache, final CamundaFormDefinitionEntity definition) {
        deploymentCache.addCamundaFormDefinition(definition);
    }
    
    static {
        LOG = ProcessEngineLogger.UTIL_LOGGER;
        FORM_RESOURCE_SUFFIXES = new String[] { "form" };
    }
}
