// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.RestartProcessInstancesCmd;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class RestartProcessInstancesJobHandler extends AbstractBatchJobHandler<RestartProcessInstancesBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public String getType() {
        return "instance-restart";
    }
    
    @Override
    protected void postProcessJob(final RestartProcessInstancesBatchConfiguration configuration, final JobEntity job, final RestartProcessInstancesBatchConfiguration jobConfiguration) {
        if (job.getDeploymentId() == null) {
            final CommandContext commandContext = Context.getCommandContext();
            final ProcessDefinitionEntity processDefinitionEntity = commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(configuration.getProcessDefinitionId());
            job.setDeploymentId(processDefinitionEntity.getDeploymentId());
        }
    }
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final ByteArrayEntity configurationEntity = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, configuration.getConfigurationByteArrayId());
        final RestartProcessInstancesBatchConfiguration batchConfiguration = this.readConfiguration(configurationEntity.getBytes());
        final String processDefinitionId = batchConfiguration.getProcessDefinitionId();
        final RestartProcessInstanceBuilderImpl builder = new RestartProcessInstanceBuilderImpl(processDefinitionId);
        builder.processInstanceIds(batchConfiguration.getIds());
        builder.setInstructions(batchConfiguration.getInstructions());
        if (batchConfiguration.isInitialVariables()) {
            builder.initialSetOfVariables();
        }
        if (batchConfiguration.isSkipCustomListeners()) {
            builder.skipCustomListeners();
        }
        if (batchConfiguration.isWithoutBusinessKey()) {
            builder.withoutBusinessKey();
        }
        if (batchConfiguration.isSkipIoMappings()) {
            builder.skipIoMappings();
        }
        final CommandExecutor commandExecutor = commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired();
        commandContext.executeWithOperationLogPrevented((Command<Object>)new RestartProcessInstancesCmd(commandExecutor, builder));
        commandContext.getByteArrayManager().delete(configurationEntity);
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return RestartProcessInstancesJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected RestartProcessInstancesBatchConfiguration createJobConfiguration(final RestartProcessInstancesBatchConfiguration configuration, final List<String> processIdsForJob) {
        return new RestartProcessInstancesBatchConfiguration(processIdsForJob, configuration.getInstructions(), configuration.getProcessDefinitionId(), configuration.isInitialVariables(), configuration.isSkipCustomListeners(), configuration.isSkipIoMappings(), configuration.isWithoutBusinessKey());
    }
    
    @Override
    protected RestartProcessInstancesBatchConfigurationJsonConverter getJsonConverterInstance() {
        return RestartProcessInstancesBatchConfigurationJsonConverter.INSTANCE;
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("instance-restart");
    }
}
