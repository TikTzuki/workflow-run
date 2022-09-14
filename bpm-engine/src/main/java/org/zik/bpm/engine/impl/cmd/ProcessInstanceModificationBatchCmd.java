// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ModificationBatchConfiguration;
import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import org.zik.bpm.engine.impl.batch.DeploymentMapping;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
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
import org.zik.bpm.engine.impl.ModificationBuilderImpl;
import org.zik.bpm.engine.batch.Batch;

public class ProcessInstanceModificationBatchCmd extends AbstractModificationCmd<Batch>
{
    public ProcessInstanceModificationBatchCmd(final ModificationBuilderImpl modificationBuilderImpl) {
        super(modificationBuilderImpl);
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final List<AbstractProcessInstanceModificationCommand> instructions = this.builder.getInstructions();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Modification instructions cannot be empty", instructions);
        final Collection<String> collectedInstanceIds = this.collectProcessInstanceIds();
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Process instance ids cannot be empty", "Process instance ids", collectedInstanceIds);
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "Process instance ids cannot be null", "Process instance ids", collectedInstanceIds);
        final String processDefinitionId = this.builder.getProcessDefinitionId();
        final ProcessDefinitionEntity processDefinition = this.getProcessDefinition(commandContext, processDefinitionId);
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Process definition id cannot be null", processDefinition);
        final String tenantId = processDefinition.getTenantId();
        final String annotation = this.builder.getAnnotation();
        return new BatchBuilder(commandContext).type("instance-modification").config(this.getConfiguration(collectedInstanceIds, processDefinition.getDeploymentId())).tenantId(tenantId).permission(BatchPermissions.CREATE_BATCH_MODIFY_PROCESS_INSTANCES).operationLogHandler((ctx, instanceCount) -> this.writeUserOperationLog(ctx, processDefinition, instanceCount, true, annotation)).build();
    }
    
    public BatchConfiguration getConfiguration(final Collection<String> instanceIds, final String deploymentId) {
        return new ModificationBatchConfiguration(new ArrayList<String>(instanceIds), DeploymentMappings.of(new DeploymentMapping(deploymentId, instanceIds.size())), this.builder.getProcessDefinitionId(), this.builder.getInstructions(), this.builder.isSkipCustomListeners(), this.builder.isSkipIoMappings());
    }
}
