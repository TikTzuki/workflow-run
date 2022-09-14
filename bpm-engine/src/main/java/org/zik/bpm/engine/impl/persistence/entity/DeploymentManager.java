// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.DeploymentQueryImpl;
import org.zik.bpm.engine.impl.form.entity.CamundaFormDefinitionManager;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionManager;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionManager;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.repository.CaseDefinition;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.DeleteProcessDefinitionsByIdsCmd;
import java.util.Arrays;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.Iterator;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class DeploymentManager extends AbstractManager
{
    public void insertDeployment(final DeploymentEntity deployment) {
        this.getDbEntityManager().insert(deployment);
        this.createDefaultAuthorizations(deployment);
        for (final ResourceEntity resource : deployment.getResources().values()) {
            resource.setDeploymentId(deployment.getId());
            resource.setType(ResourceTypes.REPOSITORY.getValue());
            resource.setCreateTime(ClockUtil.getCurrentTime());
            this.getResourceManager().insertResource(resource);
        }
        Context.getProcessEngineConfiguration().getDeploymentCache().deploy(deployment);
    }
    
    public void deleteDeployment(final String deploymentId, final boolean cascade) {
        this.deleteDeployment(deploymentId, cascade, false, false);
    }
    
    public void deleteDeployment(final String deploymentId, final boolean cascade, final boolean skipCustomListeners, final boolean skipIoMappings) {
        final List<ProcessDefinition> processDefinitions = this.getProcessDefinitionManager().findProcessDefinitionsByDeploymentId(deploymentId);
        if (cascade) {
            for (final ProcessDefinition processDefinition : processDefinitions) {
                final String processDefinitionId = processDefinition.getId();
                this.getProcessInstanceManager().deleteProcessInstancesByProcessDefinition(processDefinitionId, "deleted deployment", true, skipCustomListeners, skipIoMappings);
            }
            this.getHistoricJobLogManager().deleteHistoricJobLogsByDeploymentId(deploymentId);
        }
        for (final ProcessDefinition processDefinition : processDefinitions) {
            final String processDefinitionId = processDefinition.getId();
            final CommandContext commandContext = Context.getCommandContext();
            commandContext.runWithoutAuthorization(new DeleteProcessDefinitionsByIdsCmd(Arrays.asList(processDefinitionId), cascade, false, skipCustomListeners, false));
        }
        this.deleteCaseDeployment(deploymentId, cascade);
        this.deleteDecisionDeployment(deploymentId, cascade);
        this.deleteDecisionRequirementDeployment(deploymentId);
        this.deleteCamundaFormDefinitionDeployment(deploymentId);
        this.getResourceManager().deleteResourcesByDeploymentId(deploymentId);
        this.deleteAuthorizations(Resources.DEPLOYMENT, deploymentId);
        this.getDbEntityManager().delete(DeploymentEntity.class, "deleteDeployment", deploymentId);
    }
    
    protected void deleteCaseDeployment(final String deploymentId, final boolean cascade) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration.isCmmnEnabled()) {
            final List<CaseDefinition> caseDefinitions = this.getCaseDefinitionManager().findCaseDefinitionByDeploymentId(deploymentId);
            if (cascade) {
                for (final CaseDefinition caseDefinition : caseDefinitions) {
                    final String caseDefinitionId = caseDefinition.getId();
                    this.getCaseInstanceManager().deleteCaseInstancesByCaseDefinition(caseDefinitionId, "deleted deployment", true);
                }
            }
            this.getCaseDefinitionManager().deleteCaseDefinitionsByDeploymentId(deploymentId);
            for (final CaseDefinition caseDefinition : caseDefinitions) {
                final String processDefinitionId = caseDefinition.getId();
                Context.getProcessEngineConfiguration().getDeploymentCache().removeCaseDefinition(processDefinitionId);
            }
        }
    }
    
    protected void deleteDecisionDeployment(final String deploymentId, final boolean cascade) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration.isDmnEnabled()) {
            final DecisionDefinitionManager decisionDefinitionManager = this.getDecisionDefinitionManager();
            final List<DecisionDefinition> decisionDefinitions = decisionDefinitionManager.findDecisionDefinitionByDeploymentId(deploymentId);
            if (cascade) {
                for (final DecisionDefinition decisionDefinition : decisionDefinitions) {
                    this.getHistoricDecisionInstanceManager().deleteHistoricDecisionInstancesByDecisionDefinitionId(decisionDefinition.getId());
                }
            }
            decisionDefinitionManager.deleteDecisionDefinitionsByDeploymentId(deploymentId);
            final DeploymentCache deploymentCache = processEngineConfiguration.getDeploymentCache();
            for (final DecisionDefinition decisionDefinition2 : decisionDefinitions) {
                final String decisionDefinitionId = decisionDefinition2.getId();
                deploymentCache.removeDecisionDefinition(decisionDefinitionId);
            }
        }
    }
    
    protected void deleteDecisionRequirementDeployment(final String deploymentId) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration.isDmnEnabled()) {
            final DecisionRequirementsDefinitionManager manager = this.getDecisionRequirementsDefinitionManager();
            final List<DecisionRequirementsDefinition> decisionRequirementsDefinitions = manager.findDecisionRequirementsDefinitionByDeploymentId(deploymentId);
            manager.deleteDecisionRequirementsDefinitionsByDeploymentId(deploymentId);
            final DeploymentCache deploymentCache = processEngineConfiguration.getDeploymentCache();
            for (final DecisionRequirementsDefinition decisionRequirementsDefinition : decisionRequirementsDefinitions) {
                final String decisionDefinitionId = decisionRequirementsDefinition.getId();
                deploymentCache.removeDecisionRequirementsDefinition(decisionDefinitionId);
            }
        }
    }
    
    protected void deleteCamundaFormDefinitionDeployment(final String deploymentId) {
        final CamundaFormDefinitionManager manager = this.getCamundaFormDefinitionManager();
        final List<CamundaFormDefinitionEntity> camundaFormDefinitions = manager.findDefinitionsByDeploymentId(deploymentId);
        manager.deleteCamundaFormDefinitionsByDeploymentId(deploymentId);
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        final DeploymentCache deploymentCache = processEngineConfiguration.getDeploymentCache();
        for (final CamundaFormDefinitionEntity camundaFormDefinition : camundaFormDefinitions) {
            deploymentCache.removeCamundaFormDefinition(camundaFormDefinition.getId());
        }
    }
    
    public DeploymentEntity findLatestDeploymentByName(final String deploymentName) {
        final List<?> list = (List<?>)this.getDbEntityManager().selectList("selectDeploymentsByName", deploymentName, 0, 1);
        if (list != null && !list.isEmpty()) {
            return (DeploymentEntity)list.get(0);
        }
        return null;
    }
    
    public DeploymentEntity findDeploymentById(final String deploymentId) {
        return this.getDbEntityManager().selectById(DeploymentEntity.class, deploymentId);
    }
    
    public List<DeploymentEntity> findDeploymentsByIds(final String... deploymentsIds) {
        return (List<DeploymentEntity>)this.getDbEntityManager().selectList("selectDeploymentsByIds", deploymentsIds);
    }
    
    public long findDeploymentCountByQueryCriteria(final DeploymentQueryImpl deploymentQuery) {
        this.configureQuery(deploymentQuery);
        return (long)this.getDbEntityManager().selectOne("selectDeploymentCountByQueryCriteria", deploymentQuery);
    }
    
    public List<Deployment> findDeploymentsByQueryCriteria(final DeploymentQueryImpl deploymentQuery, final Page page) {
        this.configureQuery(deploymentQuery);
        return (List<Deployment>)this.getDbEntityManager().selectList("selectDeploymentsByQueryCriteria", deploymentQuery, page);
    }
    
    public List<String> getDeploymentResourceNames(final String deploymentId) {
        return (List<String>)this.getDbEntityManager().selectList("selectResourceNamesByDeploymentId", deploymentId);
    }
    
    public List<String> findDeploymentIdsByProcessInstances(final List<String> processInstanceIds) {
        return (List<String>)this.getDbEntityManager().selectList("selectDeploymentIdsByProcessInstances", processInstanceIds);
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void flush() {
    }
    
    protected void createDefaultAuthorizations(final DeploymentEntity deployment) {
        if (this.isAuthorizationEnabled()) {
            final ResourceAuthorizationProvider provider = this.getResourceAuthorizationProvider();
            final AuthorizationEntity[] authorizations = provider.newDeployment(deployment);
            this.saveDefaultAuthorizations(authorizations);
        }
    }
    
    protected void configureQuery(final DeploymentQueryImpl query) {
        this.getAuthorizationManager().configureDeploymentQuery(query);
        this.getTenantManager().configureQuery(query);
    }
}
