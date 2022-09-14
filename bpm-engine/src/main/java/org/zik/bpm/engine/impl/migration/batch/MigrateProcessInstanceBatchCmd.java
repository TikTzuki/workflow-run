// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.batch;

import java.util.List;
import org.zik.bpm.engine.impl.batch.DeploymentMappings;
import org.zik.bpm.engine.impl.batch.DeploymentMapping;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.migration.MigrationPlan;
import java.util.Map;
import org.zik.bpm.engine.impl.core.variable.VariableUtil;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.BatchPermissions;
import org.zik.bpm.engine.impl.batch.builder.BatchBuilder;
import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.migration.MigrationPlanExecutionBuilderImpl;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.migration.AbstractMigrationCmd;

public class MigrateProcessInstanceBatchCmd extends AbstractMigrationCmd implements Command<Batch>
{
    public MigrateProcessInstanceBatchCmd(final MigrationPlanExecutionBuilderImpl builder) {
        super(builder);
    }
    
    @Override
    public Batch execute(final CommandContext commandContext) {
        final Collection<String> collectedInstanceIds = this.collectProcessInstanceIds();
        final MigrationPlan migrationPlan = this.executionBuilder.getMigrationPlan();
        EnsureUtil.ensureNotNull(BadUserRequestException.class, "Migration plan cannot be null", "migration plan", migrationPlan);
        EnsureUtil.ensureNotEmpty(BadUserRequestException.class, "Process instance ids cannot empty", "process instance ids", collectedInstanceIds);
        EnsureUtil.ensureNotContainsNull(BadUserRequestException.class, "Process instance ids cannot be null", "process instance ids", collectedInstanceIds);
        final ProcessDefinitionEntity sourceDefinition = this.resolveSourceProcessDefinition(commandContext);
        final ProcessDefinitionEntity targetDefinition = this.resolveTargetProcessDefinition(commandContext);
        final String tenantId = sourceDefinition.getTenantId();
        final Map<String, Object> variables = (Map<String, Object>)migrationPlan.getVariables();
        final Batch batch = new BatchBuilder(commandContext).type("instance-migration").config(this.getConfiguration(collectedInstanceIds, sourceDefinition.getDeploymentId())).permission(BatchPermissions.CREATE_BATCH_MIGRATE_PROCESS_INSTANCES).permissionHandler(ctx -> this.checkAuthorizations(ctx, sourceDefinition, targetDefinition)).tenantId(tenantId).operationLogHandler((ctx, instanceCount) -> this.writeUserOperationLog(ctx, sourceDefinition, targetDefinition, instanceCount, variables, true)).build();
        if (variables != null) {
            final String batchId = batch.getId();
            VariableUtil.setVariablesByBatchId(variables, batchId);
        }
        return batch;
    }
    
    public BatchConfiguration getConfiguration(final Collection<String> instanceIds, final String deploymentId) {
        return new MigrationBatchConfiguration(new ArrayList<String>(instanceIds), DeploymentMappings.of(new DeploymentMapping(deploymentId, instanceIds.size())), this.executionBuilder.getMigrationPlan(), this.executionBuilder.isSkipCustomListeners(), this.executionBuilder.isSkipIoMappings());
    }
}
