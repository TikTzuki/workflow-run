// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import org.zik.bpm.engine.impl.json.ModificationBatchConfigurationJsonConverter;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class ModificationBatchJobHandler extends AbstractBatchJobHandler<ModificationBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public String getType() {
        return "instance-modification";
    }
    
    @Override
    protected void postProcessJob(final ModificationBatchConfiguration configuration, final JobEntity job, final ModificationBatchConfiguration jobConfiguration) {
        if (job.getDeploymentId() == null) {
            final CommandContext commandContext = Context.getCommandContext();
            final ProcessDefinitionEntity processDefinitionEntity = commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(configuration.getProcessDefinitionId());
            job.setDeploymentId(processDefinitionEntity.getDeploymentId());
        }
    }
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final ByteArrayEntity configurationEntity = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, configuration.getConfigurationByteArrayId());
        final ModificationBatchConfiguration batchConfiguration = this.readConfiguration(configurationEntity.getBytes());
        final ModificationBuilderImpl executionBuilder = (ModificationBuilderImpl)commandContext.getProcessEngineConfiguration().getRuntimeService().createModification(batchConfiguration.getProcessDefinitionId()).processInstanceIds(batchConfiguration.getIds());
        executionBuilder.setInstructions(batchConfiguration.getInstructions());
        if (batchConfiguration.isSkipCustomListeners()) {
            executionBuilder.skipCustomListeners();
        }
        if (batchConfiguration.isSkipIoMappings()) {
            executionBuilder.skipIoMappings();
        }
        executionBuilder.execute(false);
        commandContext.getByteArrayManager().delete(configurationEntity);
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return ModificationBatchJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected ModificationBatchConfiguration createJobConfiguration(final ModificationBatchConfiguration configuration, final List<String> processIdsForJob) {
        return new ModificationBatchConfiguration(processIdsForJob, configuration.getProcessDefinitionId(), configuration.getInstructions(), configuration.isSkipCustomListeners(), configuration.isSkipIoMappings());
    }
    
    @Override
    protected ModificationBatchConfigurationJsonConverter getJsonConverterInstance() {
        return ModificationBatchConfigurationJsonConverter.INSTANCE;
    }
    
    protected ProcessDefinitionEntity getProcessDefinition(final CommandContext commandContext, final String processDefinitionId) {
        return commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(processDefinitionId);
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("instance-modification");
    }
}
