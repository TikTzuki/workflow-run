// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.batch;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.core.variable.VariableUtil;
import org.zik.bpm.engine.migration.MigrationPlanExecutionBuilder;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.migration.MigrateProcessInstanceCmd;
import org.zik.bpm.engine.impl.migration.MigrationPlanExecutionBuilderImpl;
import org.zik.bpm.engine.migration.MigrationPlan;
import org.zik.bpm.engine.impl.migration.MigrationPlanImpl;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import java.util.List;
import org.zik.bpm.engine.impl.json.MigrationBatchConfigurationJsonConverter;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class MigrationBatchJobHandler extends AbstractBatchJobHandler<MigrationBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public String getType() {
        return "instance-migration";
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return MigrationBatchJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected MigrationBatchConfigurationJsonConverter getJsonConverterInstance() {
        return MigrationBatchConfigurationJsonConverter.INSTANCE;
    }
    
    @Override
    protected MigrationBatchConfiguration createJobConfiguration(final MigrationBatchConfiguration configuration, final List<String> processIdsForJob) {
        return new MigrationBatchConfiguration(processIdsForJob, configuration.getMigrationPlan(), configuration.isSkipCustomListeners(), configuration.isSkipIoMappings(), configuration.getBatchId());
    }
    
    @Override
    protected void postProcessJob(final MigrationBatchConfiguration configuration, final JobEntity job, final MigrationBatchConfiguration jobConfiguration) {
        if (job.getDeploymentId() == null) {
            final CommandContext commandContext = Context.getCommandContext();
            final String sourceProcessDefinitionId = configuration.getMigrationPlan().getSourceProcessDefinitionId();
            final ProcessDefinitionEntity processDefinition = this.getProcessDefinition(commandContext, sourceProcessDefinitionId);
            job.setDeploymentId(processDefinition.getDeploymentId());
        }
    }
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final ByteArrayEntity configurationEntity = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, configuration.getConfigurationByteArrayId());
        final MigrationBatchConfiguration batchConfiguration = this.readConfiguration(configurationEntity.getBytes());
        final MigrationPlanImpl migrationPlan = (MigrationPlanImpl)batchConfiguration.getMigrationPlan();
        final String batchId = batchConfiguration.getBatchId();
        this.setVariables(batchId, migrationPlan, commandContext);
        final MigrationPlanExecutionBuilder executionBuilder = commandContext.getProcessEngineConfiguration().getRuntimeService().newMigration(migrationPlan).processInstanceIds(batchConfiguration.getIds());
        if (batchConfiguration.isSkipCustomListeners()) {
            executionBuilder.skipCustomListeners();
        }
        if (batchConfiguration.isSkipIoMappings()) {
            executionBuilder.skipIoMappings();
        }
        commandContext.executeWithOperationLogPrevented((Command<Object>)new MigrateProcessInstanceCmd((MigrationPlanExecutionBuilderImpl)executionBuilder, true));
        commandContext.getByteArrayManager().delete(configurationEntity);
    }
    
    protected void setVariables(final String batchId, final MigrationPlanImpl migrationPlan, final CommandContext commandContext) {
        Map<String, ?> variables = null;
        if (batchId != null) {
            variables = VariableUtil.findBatchVariablesSerialized(batchId, commandContext);
            if (variables != null) {
                migrationPlan.setVariables((VariableMap)new VariableMapImpl((Map)new HashMap(variables)));
            }
        }
    }
    
    protected ProcessDefinitionEntity getProcessDefinition(final CommandContext commandContext, final String processDefinitionId) {
        return commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(processDefinitionId);
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("instance-migration");
    }
}
