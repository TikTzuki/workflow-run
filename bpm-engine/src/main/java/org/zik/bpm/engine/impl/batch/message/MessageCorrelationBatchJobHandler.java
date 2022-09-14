// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.message;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.core.variable.VariableUtil;
import java.util.Iterator;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.CorrelateAllMessageCmd;
import org.zik.bpm.engine.runtime.MessageCorrelationBuilder;
import org.zik.bpm.engine.impl.MessageCorrelationBuilderImpl;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import java.util.List;
import org.zik.bpm.engine.impl.json.MessageCorrelationBatchConfigurationJsonConverter;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class MessageCorrelationBatchJobHandler extends AbstractBatchJobHandler<MessageCorrelationBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public String getType() {
        return "correlate-message";
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return MessageCorrelationBatchJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected MessageCorrelationBatchConfigurationJsonConverter getJsonConverterInstance() {
        return MessageCorrelationBatchConfigurationJsonConverter.INSTANCE;
    }
    
    @Override
    protected MessageCorrelationBatchConfiguration createJobConfiguration(final MessageCorrelationBatchConfiguration configuration, final List<String> processIdsForJob) {
        return new MessageCorrelationBatchConfiguration(processIdsForJob, configuration.getMessageName(), configuration.getBatchId());
    }
    
    @Override
    protected void postProcessJob(final MessageCorrelationBatchConfiguration configuration, final JobEntity job, final MessageCorrelationBatchConfiguration jobConfiguration) {
        if (jobConfiguration.getIds() != null && jobConfiguration.getIds().size() == 1) {
            job.setProcessInstanceId(jobConfiguration.getIds().get(0));
        }
    }
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final ByteArrayEntity configurationEntity = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, configuration.getConfigurationByteArrayId());
        final MessageCorrelationBatchConfiguration batchConfiguration = this.readConfiguration(configurationEntity.getBytes());
        final String batchId = batchConfiguration.getBatchId();
        final MessageCorrelationBuilderImpl correlationBuilder = new MessageCorrelationBuilderImpl(commandContext, batchConfiguration.getMessageName());
        correlationBuilder.executionsOnly();
        this.setVariables(batchId, correlationBuilder, commandContext);
        for (final String id : batchConfiguration.getIds()) {
            correlationBuilder.processInstanceId(id);
            commandContext.executeWithOperationLogPrevented((Command<Object>)new CorrelateAllMessageCmd(correlationBuilder, false, false));
        }
        commandContext.getByteArrayManager().delete(configurationEntity);
    }
    
    protected void setVariables(final String batchId, final MessageCorrelationBuilder correlationBuilder, final CommandContext commandContext) {
        Map<String, ?> variables = null;
        if (batchId != null) {
            variables = VariableUtil.findBatchVariablesSerialized(batchId, commandContext);
            if (variables != null) {
                correlationBuilder.setVariables((Map<String, Object>)new VariableMapImpl((Map)new HashMap(variables)));
            }
        }
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("correlate-message");
    }
}
