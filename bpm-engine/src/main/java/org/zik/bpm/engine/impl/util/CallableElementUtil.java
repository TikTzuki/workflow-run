// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.cmmn.model.CmmnCaseDefinition;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.el.StartProcessVariableScope;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;

public class CallableElementUtil
{
    public static DeploymentCache getDeploymentCache() {
        return Context.getProcessEngineConfiguration().getDeploymentCache();
    }
    
    public static ProcessDefinitionImpl getProcessDefinitionToCall(final VariableScope execution, final String defaultTenantId, final BaseCallableElement callableElement) {
        final String processDefinitionKey = callableElement.getDefinitionKey(execution);
        final String tenantId = callableElement.getDefinitionTenantId(execution, defaultTenantId);
        return getCalledProcessDefinition(execution, callableElement, processDefinitionKey, tenantId);
    }
    
    public static ProcessDefinition getStaticallyBoundProcessDefinition(final String callingProcessDefinitionId, final String activityId, final BaseCallableElement callableElement, final String tenantId) {
        if (callableElement.hasDynamicReferences()) {
            return null;
        }
        final VariableScope emptyVariableScope = StartProcessVariableScope.getSharedInstance();
        final String targetTenantId = callableElement.getDefinitionTenantId(emptyVariableScope, tenantId);
        try {
            final String processDefinitionKey = callableElement.getDefinitionKey(emptyVariableScope);
            return getCalledProcessDefinition(emptyVariableScope, callableElement, processDefinitionKey, targetTenantId);
        }
        catch (ProcessEngineException e) {
            ProcessEngineLogger.UTIL_LOGGER.debugCouldNotResolveCallableElement(callingProcessDefinitionId, activityId, e);
            return null;
        }
    }
    
    private static ProcessDefinitionEntity getCalledProcessDefinition(final VariableScope execution, final BaseCallableElement callableElement, final String processDefinitionKey, final String tenantId) {
        final DeploymentCache deploymentCache = getDeploymentCache();
        ProcessDefinitionEntity processDefinition = null;
        if (callableElement.isLatestBinding()) {
            processDefinition = deploymentCache.findDeployedLatestProcessDefinitionByKeyAndTenantId(processDefinitionKey, tenantId);
        }
        else if (callableElement.isDeploymentBinding()) {
            final String deploymentId = callableElement.getDeploymentId();
            processDefinition = deploymentCache.findDeployedProcessDefinitionByDeploymentAndKey(deploymentId, processDefinitionKey);
        }
        else if (callableElement.isVersionBinding()) {
            final Integer version = callableElement.getVersion(execution);
            processDefinition = deploymentCache.findDeployedProcessDefinitionByKeyVersionAndTenantId(processDefinitionKey, version, tenantId);
        }
        else if (callableElement.isVersionTagBinding()) {
            final String versionTag = callableElement.getVersionTag(execution);
            processDefinition = deploymentCache.findDeployedProcessDefinitionByKeyVersionTagAndTenantId(processDefinitionKey, versionTag, tenantId);
        }
        return processDefinition;
    }
    
    public static CmmnCaseDefinition getCaseDefinitionToCall(final VariableScope execution, final String defaultTenantId, final BaseCallableElement callableElement) {
        final String caseDefinitionKey = callableElement.getDefinitionKey(execution);
        final String tenantId = callableElement.getDefinitionTenantId(execution, defaultTenantId);
        final DeploymentCache deploymentCache = getDeploymentCache();
        CmmnCaseDefinition caseDefinition = null;
        if (callableElement.isLatestBinding()) {
            caseDefinition = deploymentCache.findDeployedLatestCaseDefinitionByKeyAndTenantId(caseDefinitionKey, tenantId);
        }
        else if (callableElement.isDeploymentBinding()) {
            final String deploymentId = callableElement.getDeploymentId();
            caseDefinition = deploymentCache.findDeployedCaseDefinitionByDeploymentAndKey(deploymentId, caseDefinitionKey);
        }
        else if (callableElement.isVersionBinding()) {
            final Integer version = callableElement.getVersion(execution);
            caseDefinition = deploymentCache.findDeployedCaseDefinitionByKeyVersionAndTenantId(caseDefinitionKey, version, tenantId);
        }
        return caseDefinition;
    }
    
    public static DecisionDefinition getDecisionDefinitionToCall(final VariableScope execution, final String defaultTenantId, final BaseCallableElement callableElement) {
        final String decisionDefinitionKey = callableElement.getDefinitionKey(execution);
        final String tenantId = callableElement.getDefinitionTenantId(execution, defaultTenantId);
        final DeploymentCache deploymentCache = getDeploymentCache();
        DecisionDefinition decisionDefinition = null;
        if (callableElement.isLatestBinding()) {
            decisionDefinition = deploymentCache.findDeployedLatestDecisionDefinitionByKeyAndTenantId(decisionDefinitionKey, tenantId);
        }
        else if (callableElement.isDeploymentBinding()) {
            final String deploymentId = callableElement.getDeploymentId();
            decisionDefinition = deploymentCache.findDeployedDecisionDefinitionByDeploymentAndKey(deploymentId, decisionDefinitionKey);
        }
        else if (callableElement.isVersionBinding()) {
            final Integer version = callableElement.getVersion(execution);
            decisionDefinition = deploymentCache.findDeployedDecisionDefinitionByKeyVersionAndTenantId(decisionDefinitionKey, version, tenantId);
        }
        else if (callableElement.isVersionTagBinding()) {
            final String versionTag = callableElement.getVersionTag(execution);
            decisionDefinition = deploymentCache.findDeployedDecisionDefinitionByKeyVersionTagAndTenantId(decisionDefinitionKey, versionTag, tenantId);
        }
        return decisionDefinition;
    }
}
