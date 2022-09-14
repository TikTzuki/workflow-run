// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.update;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.UpdateProcessInstancesSuspendStateCmd;
import org.zik.bpm.engine.impl.UpdateProcessInstancesSuspensionStateBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class UpdateProcessInstancesSuspendStateJobHandler extends AbstractBatchJobHandler<UpdateProcessInstancesSuspendStateBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public String getType() {
        return "instance-update-suspension-state";
    }
    
    @Override
    protected UpdateProcessInstancesSuspendStateBatchConfigurationJsonConverter getJsonConverterInstance() {
        return UpdateProcessInstancesSuspendStateBatchConfigurationJsonConverter.INSTANCE;
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return UpdateProcessInstancesSuspendStateJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected UpdateProcessInstancesSuspendStateBatchConfiguration createJobConfiguration(final UpdateProcessInstancesSuspendStateBatchConfiguration configuration, final List<String> processIdsForJob) {
        return new UpdateProcessInstancesSuspendStateBatchConfiguration(processIdsForJob, configuration.getSuspended());
    }
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final ByteArrayEntity configurationEntity = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, configuration.getConfigurationByteArrayId());
        final UpdateProcessInstancesSuspendStateBatchConfiguration batchConfiguration = this.readConfiguration(configurationEntity.getBytes());
        final CommandExecutor commandExecutor = commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequired();
        commandContext.executeWithOperationLogPrevented((Command<Object>)new UpdateProcessInstancesSuspendStateCmd(commandExecutor, new UpdateProcessInstancesSuspensionStateBuilderImpl(batchConfiguration.getIds()), batchConfiguration.getSuspended()));
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("instance-update-suspension-state");
    }
}
