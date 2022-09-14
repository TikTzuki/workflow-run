// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.impl.RestartProcessInstancesBatchConfiguration;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.cmd.AbstractProcessInstanceModificationCommand;
import java.util.List;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.BatchPermissions;
import org.zik.bpm.engine.impl.batch.builder.BatchBuilder;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.RestartProcessInstanceBuilderImpl;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.cmd.AbstractRestartProcessInstanceCmd;

public class RestartProcessInstancesBatchCmd extends AbstractRestartProcessInstanceCmd<Batch>
{
    private final CommandLogger LOG;
    
    public RestartProcessInstancesBatchCmd(final CommandExecutor commandExecutor, final RestartProcessInstanceBuilderImpl builder) {
        super(commandExecutor, builder);
        this.LOG = ProcessEngineLogger.CMD_LOGGER;
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final Collection<String> collectedInstanceIds = this.collectProcessInstanceIds();
        final List<AbstractProcessInstanceModificationCommand> instructions = this.builder.getInstructions();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Restart instructions cannot be empty", "instructions", instructions);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Process instance ids cannot be empty", "processInstanceIds", collectedInstanceIds);
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "Process instance ids cannot be null", "processInstanceIds", collectedInstanceIds);
        final String processDefinitionId = this.builder.getProcessDefinitionId();
        final ProcessDefinitionEntity processDefinition = this.getProcessDefinition(commandContext, processDefinitionId);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Process definition cannot be null", processDefinition);
        this.ensureTenantAuthorized(commandContext, processDefinition);
        final String tenantId = processDefinition.getTenantId();
        return new BatchBuilder(commandContext).type("instance-restart").config(this.getConfiguration(collectedInstanceIds, processDefinition.getDeploymentId())).permission(BatchPermissions.CREATE_BATCH_RESTART_PROCESS_INSTANCES).tenantId(tenantId).operationLogHandler((ctx, instanceCount) -> this.writeUserOperationLog(ctx, processDefinition, instanceCount, true)).build();
    }
    
    protected void ensureTenantAuthorized(final CommandContext commandContext, final ProcessDefinitionEntity processDefinition) {
        if (!commandContext.getTenantManager().isAuthenticatedTenant(processDefinition.getTenantId())) {
            throw this.LOG.exceptionCommandWithUnauthorizedTenant("restart process instances of process definition '" + processDefinition.getId() + "'");
        }
    }
    
    public BatchConfiguration getConfiguration(final Collection<String> instanceIds, final String deploymentId) {
        return new RestartProcessInstancesBatchConfiguration(new ArrayList<String>(instanceIds), DeploymentMappings.of(new DeploymentMapping(deploymentId, instanceIds.size())), this.builder.getInstructions(), this.builder.getProcessDefinitionId(), this.builder.isInitialVariables(), this.builder.isSkipCustomListeners(), this.builder.isSkipIoMappings(), this.builder.isWithoutBusinessKey());
    }
}
