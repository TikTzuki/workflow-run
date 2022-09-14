// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.job;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.SetJobsRetriesCmd;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.SetRetriesBatchConfiguration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class SetJobRetriesJobHandler extends AbstractBatchJobHandler<SetRetriesBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public String getType() {
        return "set-job-retries";
    }
    
    @Override
    protected SetJobRetriesBatchConfigurationJsonConverter getJsonConverterInstance() {
        return SetJobRetriesBatchConfigurationJsonConverter.INSTANCE;
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return SetJobRetriesJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected SetRetriesBatchConfiguration createJobConfiguration(final SetRetriesBatchConfiguration configuration, final List<String> jobIds) {
        return new SetRetriesBatchConfiguration(jobIds, configuration.getRetries());
    }
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final ByteArrayEntity configurationEntity = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, configuration.getConfigurationByteArrayId());
        final SetRetriesBatchConfiguration batchConfiguration = this.readConfiguration(configurationEntity.getBytes());
        commandContext.executeWithOperationLogPrevented((Command<Object>)new SetJobsRetriesCmd(batchConfiguration.getIds(), batchConfiguration.getRetries()));
        commandContext.getByteArrayManager().delete(configurationEntity);
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("set-job-retries");
    }
}
