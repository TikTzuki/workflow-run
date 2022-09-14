// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.variables;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.SetExecutionVariablesCmd;
import org.zik.bpm.engine.impl.core.variable.VariableUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class BatchSetVariablesHandler extends AbstractBatchJobHandler<BatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final String byteArrayId = configuration.getConfigurationByteArrayId();
        final ByteArrayEntity byteArray = this.findByteArrayById(byteArrayId, commandContext);
        final byte[] configurationByteArray = byteArray.getBytes();
        final BatchConfiguration batchConfiguration = this.readConfiguration(configurationByteArray);
        final String batchId = batchConfiguration.getBatchId();
        final Map<String, ?> variables = VariableUtil.findBatchVariablesSerialized(batchId, commandContext);
        final List<String> processInstanceIds = batchConfiguration.getIds();
        for (final String processInstanceId : processInstanceIds) {
            commandContext.executeWithOperationLogPrevented((Command<Object>)new SetExecutionVariablesCmd(processInstanceId, variables, false, true));
        }
        commandContext.getByteArrayManager().delete(byteArray);
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return BatchSetVariablesHandler.JOB_DECLARATION;
    }
    
    @Override
    protected BatchConfiguration createJobConfiguration(final BatchConfiguration configuration, final List<String> processIdsForJob) {
        return new BatchConfiguration(processIdsForJob);
    }
    
    @Override
    protected JsonObjectConverter<BatchConfiguration> getJsonConverterInstance() {
        return SetVariablesJsonConverter.INSTANCE;
    }
    
    @Override
    public String getType() {
        return "set-variables";
    }
    
    @Override
    protected void postProcessJob(final BatchConfiguration configuration, final JobEntity job, final BatchConfiguration jobConfiguration) {
        if (jobConfiguration.getIds() != null && jobConfiguration.getIds().size() == 1) {
            job.setProcessInstanceId(jobConfiguration.getIds().get(0));
        }
    }
    
    protected ByteArrayEntity findByteArrayById(final String byteArrayId, final CommandContext commandContext) {
        return commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, byteArrayId);
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("set-variables");
    }
}
