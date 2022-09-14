// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.RuntimeService;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public class TimerStartEventJobHandler extends TimerEventJobHandler
{
    private static final JobExecutorLogger LOG;
    public static final String TYPE = "timer-start-event";
    
    @Override
    public String getType() {
        return "timer-start-event";
    }
    
    @Override
    public void execute(final TimerJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final DeploymentCache deploymentCache = Context.getProcessEngineConfiguration().getDeploymentCache();
        final String definitionKey = configuration.getTimerElementKey();
        final ProcessDefinition processDefinition = deploymentCache.findDeployedLatestProcessDefinitionByKeyAndTenantId(definitionKey, tenantId);
        try {
            this.startProcessInstance(commandContext, tenantId, processDefinition);
        }
        catch (RuntimeException e) {
            throw e;
        }
    }
    
    protected void startProcessInstance(final CommandContext commandContext, final String tenantId, final ProcessDefinition processDefinition) {
        if (!processDefinition.isSuspended()) {
            final RuntimeService runtimeService = commandContext.getProcessEngineConfiguration().getRuntimeService();
            runtimeService.createProcessInstanceByKey(processDefinition.getKey()).processDefinitionTenantId(tenantId).execute();
        }
        else {
            TimerStartEventJobHandler.LOG.ignoringSuspendedJob(processDefinition);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.JOB_EXECUTOR_LOGGER;
    }
}
