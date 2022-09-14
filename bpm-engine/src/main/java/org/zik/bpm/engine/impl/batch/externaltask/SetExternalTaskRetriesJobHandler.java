// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.externaltask;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.SetExternalTasksRetriesCmd;
import org.zik.bpm.engine.impl.cmd.UpdateExternalTaskRetriesBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.SetRetriesBatchConfiguration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class SetExternalTaskRetriesJobHandler extends AbstractBatchJobHandler<SetRetriesBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public String getType() {
        return "set-external-task-retries";
    }
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final ByteArrayEntity configurationEntity = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, configuration.getConfigurationByteArrayId());
        final SetRetriesBatchConfiguration batchConfiguration = this.readConfiguration(configurationEntity.getBytes());
        commandContext.executeWithOperationLogPrevented((Command<Object>)new SetExternalTasksRetriesCmd(new UpdateExternalTaskRetriesBuilderImpl(batchConfiguration.getIds(), batchConfiguration.getRetries())));
        commandContext.getByteArrayManager().delete(configurationEntity);
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return SetExternalTaskRetriesJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected SetRetriesBatchConfiguration createJobConfiguration(final SetRetriesBatchConfiguration configuration, final List<String> processIdsForJob) {
        return new SetRetriesBatchConfiguration(processIdsForJob, configuration.getRetries());
    }
    
    @Override
    protected SetExternalTaskRetriesBatchConfigurationJsonConverter getJsonConverterInstance() {
        return SetExternalTaskRetriesBatchConfigurationJsonConverter.INSTANCE;
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("set-external-task-retries");
    }
}
