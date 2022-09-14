// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import java.util.Iterator;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ProcessInstantiationBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetDeployedProcessDefinitionCmd implements Command<ProcessDefinitionEntity>
{
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String processDefinitionTenantId;
    protected boolean isTenantIdSet;
    protected final boolean checkReadPermission;
    
    public GetDeployedProcessDefinitionCmd(final String processDefinitionId, final boolean checkReadPermission) {
        this.isTenantIdSet = false;
        this.processDefinitionId = processDefinitionId;
        this.checkReadPermission = checkReadPermission;
    }
    
    public GetDeployedProcessDefinitionCmd(final ProcessInstantiationBuilderImpl instantiationBuilder, final boolean checkReadPermission) {
        this.isTenantIdSet = false;
        this.processDefinitionId = instantiationBuilder.getProcessDefinitionId();
        this.processDefinitionKey = instantiationBuilder.getProcessDefinitionKey();
        this.processDefinitionTenantId = instantiationBuilder.getProcessDefinitionTenantId();
        this.isTenantIdSet = instantiationBuilder.isProcessDefinitionTenantIdSet();
        this.checkReadPermission = checkReadPermission;
    }
    
    @Override
    public ProcessDefinitionEntity execute(final CommandContext commandContext) {
        EnsureUtil.ensureOnlyOneNotNull("either process definition id or key must be set", this.processDefinitionId, this.processDefinitionKey);
        final ProcessDefinitionEntity processDefinition = this.find(commandContext);
        if (this.checkReadPermission) {
            for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
                checker.checkReadProcessDefinition(processDefinition);
            }
        }
        return processDefinition;
    }
    
    protected ProcessDefinitionEntity find(final CommandContext commandContext) {
        final DeploymentCache deploymentCache = commandContext.getProcessEngineConfiguration().getDeploymentCache();
        if (this.processDefinitionId != null) {
            return this.findById(deploymentCache, this.processDefinitionId);
        }
        return this.findByKey(deploymentCache, this.processDefinitionKey);
    }
    
    protected ProcessDefinitionEntity findById(final DeploymentCache deploymentCache, final String processDefinitionId) {
        return deploymentCache.findDeployedProcessDefinitionById(processDefinitionId);
    }
    
    protected ProcessDefinitionEntity findByKey(final DeploymentCache deploymentCache, final String processDefinitionKey) {
        if (this.isTenantIdSet) {
            return deploymentCache.findDeployedLatestProcessDefinitionByKeyAndTenantId(processDefinitionKey, this.processDefinitionTenantId);
        }
        return deploymentCache.findDeployedLatestProcessDefinitionByKey(processDefinitionKey);
    }
}
