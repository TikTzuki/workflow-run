// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.ModificationBatchConfiguration;
import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import org.zik.bpm.engine.impl.batch.DeploymentMapping;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import java.util.Collections;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionManager;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.BatchPermissions;
import org.zik.bpm.engine.impl.batch.builder.BatchBuilder;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.ProcessInstanceModificationBuilderImpl;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;

public class ModifyProcessInstanceAsyncCmd implements Command<Batch>
{
    private static final CommandLogger LOG;
    protected ProcessInstanceModificationBuilderImpl builder;
    
    public ModifyProcessInstanceAsyncCmd(final ProcessInstanceModificationBuilderImpl builder) {
        this.builder = builder;
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final String processInstanceId = this.builder.getProcessInstanceId();
        final ExecutionManager executionManager = commandContext.getExecutionManager();
        final ExecutionEntity processInstance = executionManager.findExecutionById(processInstanceId);
        this.ensureProcessInstanceExists(processInstanceId, processInstance);
        final String processDefinitionId = processInstance.getProcessDefinitionId();
        final String tenantId = processInstance.getTenantId();
        final String deploymentId = commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(processDefinitionId).getDeploymentId();
        return new BatchBuilder(commandContext).type("instance-modification").config(this.getConfiguration(processDefinitionId, deploymentId)).tenantId(tenantId).totalJobs(1).permission(BatchPermissions.CREATE_BATCH_MODIFY_PROCESS_INSTANCES).operationLogHandler(this::writeOperationLog).build();
    }
    
    protected void ensureProcessInstanceExists(final String processInstanceId, final ExecutionEntity processInstance) {
        if (processInstance == null) {
            throw ModifyProcessInstanceAsyncCmd.LOG.processInstanceDoesNotExist(processInstanceId);
        }
    }
    
    protected String getLogEntryOperation() {
        return "ModifyProcessInstance";
    }
    
    protected void writeOperationLog(final CommandContext commandContext) {
        commandContext.getOperationLogManager().logProcessInstanceOperation(this.getLogEntryOperation(), this.builder.getProcessInstanceId(), null, null, Collections.singletonList(PropertyChange.EMPTY_CHANGE), this.builder.getAnnotation());
    }
    
    public BatchConfiguration getConfiguration(final String processDefinitionId, final String deploymentId) {
        return new ModificationBatchConfiguration(Collections.singletonList(this.builder.getProcessInstanceId()), DeploymentMappings.of(new DeploymentMapping(deploymentId, 1)), processDefinitionId, this.builder.getModificationOperations(), this.builder.isSkipCustomListeners(), this.builder.isSkipIoMappings());
    }
    
    static {
        LOG = ProcessEngineLogger.CMD_LOGGER;
    }
}
