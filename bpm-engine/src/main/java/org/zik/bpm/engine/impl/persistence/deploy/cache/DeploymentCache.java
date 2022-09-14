// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.deploy.cache;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.cmmn.CmmnModelInstance;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.commons.utils.cache.Cache;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionQueryImpl;
import org.zik.bpm.engine.impl.persistence.deploy.Deployer;
import org.zik.bpm.engine.impl.persistence.entity.CamundaFormDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;

import java.util.Iterator;
import java.util.List;

public class DeploymentCache {
    protected ProcessDefinitionCache processDefinitionEntityCache;
    protected CaseDefinitionCache caseDefinitionCache;
    protected DecisionDefinitionCache decisionDefinitionCache;
    protected DecisionRequirementsDefinitionCache decisionRequirementsDefinitionCache;
    protected CamundaFormDefinitionCache camundaFormDefinitionCache;
    protected BpmnModelInstanceCache bpmnModelInstanceCache;
    protected CmmnModelInstanceCache cmmnModelInstanceCache;
    protected DmnModelInstanceCache dmnModelInstanceCache;
    protected CacheDeployer cacheDeployer = new CacheDeployer();

    public DeploymentCache(CacheFactory factory, int cacheCapacity) {
        this.processDefinitionEntityCache = new ProcessDefinitionCache(factory, cacheCapacity, this.cacheDeployer);
        this.caseDefinitionCache = new CaseDefinitionCache(factory, cacheCapacity, this.cacheDeployer);
        this.decisionDefinitionCache = new DecisionDefinitionCache(factory, cacheCapacity, this.cacheDeployer);
        this.decisionRequirementsDefinitionCache = new DecisionRequirementsDefinitionCache(factory, cacheCapacity, this.cacheDeployer);
        this.camundaFormDefinitionCache = new CamundaFormDefinitionCache(factory, cacheCapacity, this.cacheDeployer);
        this.bpmnModelInstanceCache = new BpmnModelInstanceCache(factory, cacheCapacity, this.processDefinitionEntityCache);
        this.cmmnModelInstanceCache = new CmmnModelInstanceCache(factory, cacheCapacity, this.caseDefinitionCache);
        this.dmnModelInstanceCache = new DmnModelInstanceCache(factory, cacheCapacity, this.decisionDefinitionCache);
    }

    public void deploy(DeploymentEntity deployment) {
        this.cacheDeployer.deploy(deployment);
    }

    public ProcessDefinitionEntity findProcessDefinitionFromCache(String processDefinitionId) {
        return (ProcessDefinitionEntity) this.processDefinitionEntityCache.findDefinitionFromCache(processDefinitionId);
    }

    public ProcessDefinitionEntity findDeployedProcessDefinitionById(String processDefinitionId) {
        return (ProcessDefinitionEntity) this.processDefinitionEntityCache.findDeployedDefinitionById(processDefinitionId);
    }

    public ProcessDefinitionEntity findDeployedLatestProcessDefinitionByKey(String processDefinitionKey) {
        return (ProcessDefinitionEntity) this.processDefinitionEntityCache.findDeployedLatestDefinitionByKey(processDefinitionKey);
    }

    public ProcessDefinitionEntity findDeployedLatestProcessDefinitionByKeyAndTenantId(String processDefinitionKey, String tenantId) {
        return (ProcessDefinitionEntity) this.processDefinitionEntityCache.findDeployedLatestDefinitionByKeyAndTenantId(processDefinitionKey, tenantId);
    }

    public ProcessDefinitionEntity findDeployedProcessDefinitionByKeyVersionAndTenantId(String processDefinitionKey, Integer processDefinitionVersion, String tenantId) {
        return (ProcessDefinitionEntity) this.processDefinitionEntityCache.findDeployedDefinitionByKeyVersionAndTenantId(processDefinitionKey, processDefinitionVersion, tenantId);
    }

    public ProcessDefinitionEntity findDeployedProcessDefinitionByKeyVersionTagAndTenantId(String processDefinitionKey, String processDefinitionVersionTag, String tenantId) {
        return (ProcessDefinitionEntity) this.processDefinitionEntityCache.findDeployedDefinitionByKeyVersionTagAndTenantId(processDefinitionKey, processDefinitionVersionTag, tenantId);
    }

    public ProcessDefinitionEntity findDeployedProcessDefinitionByDeploymentAndKey(String deploymentId, String processDefinitionKey) {
        return (ProcessDefinitionEntity) this.processDefinitionEntityCache.findDeployedDefinitionByDeploymentAndKey(deploymentId, processDefinitionKey);
    }

    public ProcessDefinitionEntity resolveProcessDefinition(ProcessDefinitionEntity processDefinition) {
        return (ProcessDefinitionEntity) this.processDefinitionEntityCache.resolveDefinition(processDefinition);
    }

    public BpmnModelInstance findBpmnModelInstanceForProcessDefinition(ProcessDefinitionEntity processDefinitionEntity) {
        return (BpmnModelInstance) this.bpmnModelInstanceCache.findBpmnModelInstanceForDefinition(processDefinitionEntity);
    }

    public BpmnModelInstance findBpmnModelInstanceForProcessDefinition(String processDefinitionId) {
        return (BpmnModelInstance) this.bpmnModelInstanceCache.findBpmnModelInstanceForDefinition(processDefinitionId);
    }

    public void addProcessDefinition(ProcessDefinitionEntity processDefinition) {
        this.processDefinitionEntityCache.addDefinition(processDefinition);
    }

    public void removeProcessDefinition(String processDefinitionId) {
        this.processDefinitionEntityCache.removeDefinitionFromCache(processDefinitionId);
        this.bpmnModelInstanceCache.remove(processDefinitionId);
    }

    public void discardProcessDefinitionCache() {
        this.processDefinitionEntityCache.clear();
        this.bpmnModelInstanceCache.clear();
    }

    public void addCamundaFormDefinition(CamundaFormDefinitionEntity camundaFormDefinition) {
        this.camundaFormDefinitionCache.addDefinition(camundaFormDefinition);
    }

    public void removeCamundaFormDefinition(String camundaFormDefinitionId) {
        this.camundaFormDefinitionCache.removeDefinitionFromCache(camundaFormDefinitionId);
    }

    public void discardCamundaFormDefinitionCache() {
        this.camundaFormDefinitionCache.clear();
    }

    public CaseDefinitionEntity findCaseDefinitionFromCache(String caseDefinitionId) {
        return (CaseDefinitionEntity) this.caseDefinitionCache.findDefinitionFromCache(caseDefinitionId);
    }

    public CaseDefinitionEntity findDeployedCaseDefinitionById(String caseDefinitionId) {
        return (CaseDefinitionEntity) this.caseDefinitionCache.findDeployedDefinitionById(caseDefinitionId);
    }

    public CaseDefinitionEntity findDeployedLatestCaseDefinitionByKey(String caseDefinitionKey) {
        return (CaseDefinitionEntity) this.caseDefinitionCache.findDeployedLatestDefinitionByKey(caseDefinitionKey);
    }

    public CaseDefinitionEntity findDeployedLatestCaseDefinitionByKeyAndTenantId(String caseDefinitionKey, String tenantId) {
        return (CaseDefinitionEntity) this.caseDefinitionCache.findDeployedLatestDefinitionByKeyAndTenantId(caseDefinitionKey, tenantId);
    }

    public CaseDefinitionEntity findDeployedCaseDefinitionByKeyVersionAndTenantId(String caseDefinitionKey, Integer caseDefinitionVersion, String tenantId) {
        return (CaseDefinitionEntity) this.caseDefinitionCache.findDeployedDefinitionByKeyVersionAndTenantId(caseDefinitionKey, caseDefinitionVersion, tenantId);
    }

    public CaseDefinitionEntity findDeployedCaseDefinitionByDeploymentAndKey(String deploymentId, String caseDefinitionKey) {
        return (CaseDefinitionEntity) this.caseDefinitionCache.findDeployedDefinitionByDeploymentAndKey(deploymentId, caseDefinitionKey);
    }

    public CaseDefinitionEntity getCaseDefinitionById(String caseDefinitionId) {
        return this.caseDefinitionCache.getCaseDefinitionById(caseDefinitionId);
    }

    public CaseDefinitionEntity resolveCaseDefinition(CaseDefinitionEntity caseDefinition) {
        return (CaseDefinitionEntity) this.caseDefinitionCache.resolveDefinition(caseDefinition);
    }

    public CmmnModelInstance findCmmnModelInstanceForCaseDefinition(String caseDefinitionId) {
        return (CmmnModelInstance) this.cmmnModelInstanceCache.findBpmnModelInstanceForDefinition(caseDefinitionId);
    }

    public void addCaseDefinition(CaseDefinitionEntity caseDefinition) {
        this.caseDefinitionCache.addDefinition(caseDefinition);
    }

    public void removeCaseDefinition(String caseDefinitionId) {
        this.caseDefinitionCache.removeDefinitionFromCache(caseDefinitionId);
        this.cmmnModelInstanceCache.remove(caseDefinitionId);
    }

    public void discardCaseDefinitionCache() {
        this.caseDefinitionCache.clear();
        this.cmmnModelInstanceCache.clear();
    }

    public DecisionDefinitionEntity findDecisionDefinitionFromCache(String decisionDefinitionId) {
        return (DecisionDefinitionEntity) this.decisionDefinitionCache.findDefinitionFromCache(decisionDefinitionId);
    }

    public DecisionDefinitionEntity findDeployedDecisionDefinitionById(String decisionDefinitionId) {
        return (DecisionDefinitionEntity) this.decisionDefinitionCache.findDeployedDefinitionById(decisionDefinitionId);
    }

    public DecisionDefinition findDeployedLatestDecisionDefinitionByKey(String decisionDefinitionKey) {
        return (DecisionDefinition) this.decisionDefinitionCache.findDeployedLatestDefinitionByKey(decisionDefinitionKey);
    }

    public DecisionDefinition findDeployedLatestDecisionDefinitionByKeyAndTenantId(String decisionDefinitionKey, String tenantId) {
        return (DecisionDefinition) this.decisionDefinitionCache.findDeployedLatestDefinitionByKeyAndTenantId(decisionDefinitionKey, tenantId);
    }

    public DecisionDefinition findDeployedDecisionDefinitionByDeploymentAndKey(String deploymentId, String decisionDefinitionKey) {
        return (DecisionDefinition) this.decisionDefinitionCache.findDeployedDefinitionByDeploymentAndKey(deploymentId, decisionDefinitionKey);
    }

    public DecisionDefinition findDeployedDecisionDefinitionByKeyAndVersion(String decisionDefinitionKey, Integer decisionDefinitionVersion) {
        return this.decisionDefinitionCache.findDeployedDefinitionByKeyAndVersion(decisionDefinitionKey, decisionDefinitionVersion);
    }

    public DecisionDefinition findDeployedDecisionDefinitionByKeyVersionAndTenantId(String decisionDefinitionKey, Integer decisionDefinitionVersion, String tenantId) {
        return (DecisionDefinition) this.decisionDefinitionCache.findDeployedDefinitionByKeyVersionAndTenantId(decisionDefinitionKey, decisionDefinitionVersion, tenantId);
    }

    public DecisionDefinition findDeployedDecisionDefinitionByKeyVersionTagAndTenantId(String decisionDefinitionKey, String decisionDefinitionVersionTag, String tenantId) {
        return (DecisionDefinition) this.decisionDefinitionCache.findDeployedDefinitionByKeyVersionTagAndTenantId(decisionDefinitionKey, decisionDefinitionVersionTag, tenantId);
    }

    public DecisionDefinitionEntity resolveDecisionDefinition(DecisionDefinitionEntity decisionDefinition) {
        return (DecisionDefinitionEntity) this.decisionDefinitionCache.resolveDefinition(decisionDefinition);
    }

    public DmnModelInstance findDmnModelInstanceForDecisionDefinition(String decisionDefinitionId) {
        return (DmnModelInstance) this.dmnModelInstanceCache.findBpmnModelInstanceForDefinition(decisionDefinitionId);
    }

    public void addDecisionDefinition(DecisionDefinitionEntity decisionDefinition) {
        this.decisionDefinitionCache.addDefinition(decisionDefinition);
    }

    public void removeDecisionDefinition(String decisionDefinitionId) {
        this.decisionDefinitionCache.removeDefinitionFromCache(decisionDefinitionId);
        this.dmnModelInstanceCache.remove(decisionDefinitionId);
    }

    public void discardDecisionDefinitionCache() {
        this.decisionDefinitionCache.clear();
        this.dmnModelInstanceCache.clear();
    }

    public void addDecisionRequirementsDefinition(DecisionRequirementsDefinitionEntity decisionRequirementsDefinition) {
        this.decisionRequirementsDefinitionCache.addDefinition(decisionRequirementsDefinition);
    }

    public DecisionRequirementsDefinitionEntity findDecisionRequirementsDefinitionFromCache(String decisionRequirementsDefinitionId) {
        return (DecisionRequirementsDefinitionEntity) this.decisionRequirementsDefinitionCache.findDefinitionFromCache(decisionRequirementsDefinitionId);
    }

    public DecisionRequirementsDefinitionEntity findDeployedDecisionRequirementsDefinitionById(String decisionRequirementsDefinitionId) {
        return (DecisionRequirementsDefinitionEntity) this.decisionRequirementsDefinitionCache.findDeployedDefinitionById(decisionRequirementsDefinitionId);
    }

    public DecisionRequirementsDefinitionEntity resolveDecisionRequirementsDefinition(DecisionRequirementsDefinitionEntity decisionRequirementsDefinition) {
        return (DecisionRequirementsDefinitionEntity) this.decisionRequirementsDefinitionCache.resolveDefinition(decisionRequirementsDefinition);
    }

    public void discardDecisionRequirementsDefinitionCache() {
        this.decisionDefinitionCache.clear();
    }

    public void removeDecisionRequirementsDefinition(String decisionRequirementsDefinitionId) {
        this.decisionRequirementsDefinitionCache.removeDefinitionFromCache(decisionRequirementsDefinitionId);
    }

    public Cache<String, BpmnModelInstance> getBpmnModelInstanceCache() {
        return this.bpmnModelInstanceCache.getCache();
    }

    public Cache<String, CmmnModelInstance> getCmmnModelInstanceCache() {
        return this.cmmnModelInstanceCache.getCache();
    }

    public Cache<String, DmnModelInstance> getDmnDefinitionCache() {
        return this.dmnModelInstanceCache.getCache();
    }

    public Cache<String, DecisionDefinitionEntity> getDecisionDefinitionCache() {
        return this.decisionDefinitionCache.getCache();
    }

    public Cache<String, DecisionRequirementsDefinitionEntity> getDecisionRequirementsDefinitionCache() {
        return this.decisionRequirementsDefinitionCache.getCache();
    }

    public Cache<String, ProcessDefinitionEntity> getProcessDefinitionCache() {
        return this.processDefinitionEntityCache.getCache();
    }

    public Cache<String, CaseDefinitionEntity> getCaseDefinitionCache() {
        return this.caseDefinitionCache.getCache();
    }

    public void setDeployers(List<Deployer> deployers) {
        this.cacheDeployer.setDeployers(deployers);
    }

    public void removeDeployment(String deploymentId) {
        this.bpmnModelInstanceCache.removeAllDefinitionsByDeploymentId(deploymentId);
        if (Context.getProcessEngineConfiguration().isCmmnEnabled()) {
            this.cmmnModelInstanceCache.removeAllDefinitionsByDeploymentId(deploymentId);
        }

        if (Context.getProcessEngineConfiguration().isDmnEnabled()) {
            this.dmnModelInstanceCache.removeAllDefinitionsByDeploymentId(deploymentId);
            this.removeAllDecisionRequirementsDefinitionsByDeploymentId(deploymentId);
        }

    }

    protected void removeAllDecisionRequirementsDefinitionsByDeploymentId(String deploymentId) {
        List<DecisionRequirementsDefinition> allDefinitionsForDeployment = (new DecisionRequirementsDefinitionQueryImpl()).deploymentId(deploymentId).list();
        Iterator var3 = allDefinitionsForDeployment.iterator();

        while (var3.hasNext()) {
            DecisionRequirementsDefinition decisionRequirementsDefinition = (DecisionRequirementsDefinition) var3.next();

            try {
                this.removeDecisionDefinition(decisionRequirementsDefinition.getId());
            } catch (Exception var6) {
                ProcessEngineLogger.PERSISTENCE_LOGGER.removeEntryFromDeploymentCacheFailure("decision requirement", decisionRequirementsDefinition.getId(), var6);
            }
        }

    }

    public CachePurgeReport purgeCache() {
        CachePurgeReport result = new CachePurgeReport();
        Cache<String, ProcessDefinitionEntity> processDefinitionCache = this.getProcessDefinitionCache();
        if (!processDefinitionCache.isEmpty()) {
            result.addPurgeInformation("PROC_DEF_CACHE", processDefinitionCache.keySet());
            processDefinitionCache.clear();
        }

        Cache<String, BpmnModelInstance> bpmnModelInstanceCache = this.getBpmnModelInstanceCache();
        if (!bpmnModelInstanceCache.isEmpty()) {
            result.addPurgeInformation("BPMN_MODEL_INST_CACHE", bpmnModelInstanceCache.keySet());
            bpmnModelInstanceCache.clear();
        }

        Cache<String, CaseDefinitionEntity> caseDefinitionCache = this.getCaseDefinitionCache();
        if (!caseDefinitionCache.isEmpty()) {
            result.addPurgeInformation("CASE_DEF_CACHE", caseDefinitionCache.keySet());
            caseDefinitionCache.clear();
        }

        Cache<String, CmmnModelInstance> cmmnModelInstanceCache = this.getCmmnModelInstanceCache();
        if (!cmmnModelInstanceCache.isEmpty()) {
            result.addPurgeInformation("CASE_MODEL_INST_CACHE", cmmnModelInstanceCache.keySet());
            cmmnModelInstanceCache.clear();
        }

        Cache<String, DecisionDefinitionEntity> decisionDefinitionCache = this.getDecisionDefinitionCache();
        if (!decisionDefinitionCache.isEmpty()) {
            result.addPurgeInformation("DMN_DEF_CACHE", decisionDefinitionCache.keySet());
            decisionDefinitionCache.clear();
        }

        Cache<String, DmnModelInstance> dmnModelInstanceCache = this.getDmnDefinitionCache();
        if (!dmnModelInstanceCache.isEmpty()) {
            result.addPurgeInformation("DMN_MODEL_INST_CACHE", dmnModelInstanceCache.keySet());
            dmnModelInstanceCache.clear();
        }

        Cache<String, DecisionRequirementsDefinitionEntity> decisionRequirementsDefinitionCache = this.getDecisionRequirementsDefinitionCache();
        if (!decisionRequirementsDefinitionCache.isEmpty()) {
            result.addPurgeInformation("DMN_REQ_DEF_CACHE", decisionRequirementsDefinitionCache.keySet());
            decisionRequirementsDefinitionCache.clear();
        }

        return result;
    }
}
