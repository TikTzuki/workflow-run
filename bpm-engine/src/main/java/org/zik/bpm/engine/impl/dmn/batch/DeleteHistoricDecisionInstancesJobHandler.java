// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.batch;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.dmn.cmd.DeleteHistoricDecisionInstancesBulkCmd;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class DeleteHistoricDecisionInstancesJobHandler extends AbstractBatchJobHandler<BatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public String getType() {
        return "historic-decision-instance-deletion";
    }
    
    @Override
    protected DeleteHistoricDecisionInstanceBatchConfigurationJsonConverter getJsonConverterInstance() {
        return DeleteHistoricDecisionInstanceBatchConfigurationJsonConverter.INSTANCE;
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return DeleteHistoricDecisionInstancesJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected BatchConfiguration createJobConfiguration(final BatchConfiguration configuration, final List<String> decisionIdsForJob) {
        return new BatchConfiguration(decisionIdsForJob);
    }
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final ByteArrayEntity configurationEntity = commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, configuration.getConfigurationByteArrayId());
        final BatchConfiguration batchConfiguration = this.readConfiguration(configurationEntity.getBytes());
        commandContext.executeWithOperationLogPrevented((Command<Object>)new DeleteHistoricDecisionInstancesBulkCmd(batchConfiguration.getIds()));
        commandContext.getByteArrayManager().delete(configurationEntity);
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("historic-decision-instance-deletion");
    }
}
