// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.removaltime;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.batch.BatchJobContext;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import java.util.Date;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class BatchSetRemovalTimeJobHandler extends AbstractBatchJobHandler<SetRemovalTimeBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final String byteArrayId = configuration.getConfigurationByteArrayId();
        final byte[] configurationByteArray = this.findByteArrayById(byteArrayId, commandContext).getBytes();
        final SetRemovalTimeBatchConfiguration batchConfiguration = this.readConfiguration(configurationByteArray);
        for (final String instanceId : batchConfiguration.getIds()) {
            final HistoricBatchEntity instance = this.findBatchById(instanceId, commandContext);
            if (instance != null) {
                final Date removalTime = this.getOrCalculateRemovalTime(batchConfiguration, instance, commandContext);
                if (removalTime == instance.getRemovalTime()) {
                    continue;
                }
                this.addRemovalTime(instanceId, removalTime, commandContext);
            }
        }
    }
    
    protected Date getOrCalculateRemovalTime(final SetRemovalTimeBatchConfiguration batchConfiguration, final HistoricBatchEntity instance, final CommandContext commandContext) {
        if (batchConfiguration.hasRemovalTime()) {
            return batchConfiguration.getRemovalTime();
        }
        if (this.hasBaseTime(instance, commandContext)) {
            return this.calculateRemovalTime(instance, commandContext);
        }
        return null;
    }
    
    protected void addRemovalTime(final String instanceId, final Date removalTime, final CommandContext commandContext) {
        commandContext.getHistoricBatchManager().addRemovalTimeById(instanceId, removalTime);
    }
    
    protected boolean hasBaseTime(final HistoricBatchEntity instance, final CommandContext commandContext) {
        return this.isStrategyStart(commandContext) || (this.isStrategyEnd(commandContext) && this.isEnded(instance));
    }
    
    protected boolean isEnded(final HistoricBatchEntity instance) {
        return instance.getEndTime() != null;
    }
    
    protected boolean isStrategyStart(final CommandContext commandContext) {
        return "start".equals(this.getHistoryRemovalTimeStrategy(commandContext));
    }
    
    protected boolean isStrategyEnd(final CommandContext commandContext) {
        return "end".equals(this.getHistoryRemovalTimeStrategy(commandContext));
    }
    
    protected String getHistoryRemovalTimeStrategy(final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getHistoryRemovalTimeStrategy();
    }
    
    protected boolean isDmnEnabled(final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().isDmnEnabled();
    }
    
    protected Date calculateRemovalTime(final HistoricBatchEntity batch, final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getHistoryRemovalTimeProvider().calculateRemovalTime(batch);
    }
    
    protected ByteArrayEntity findByteArrayById(final String byteArrayId, final CommandContext commandContext) {
        return commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, byteArrayId);
    }
    
    protected HistoricBatchEntity findBatchById(final String instanceId, final CommandContext commandContext) {
        return commandContext.getHistoricBatchManager().findHistoricBatchById(instanceId);
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return BatchSetRemovalTimeJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected SetRemovalTimeBatchConfiguration createJobConfiguration(final SetRemovalTimeBatchConfiguration configuration, final List<String> batchIds) {
        return new SetRemovalTimeBatchConfiguration(batchIds).setRemovalTime(configuration.getRemovalTime()).setHasRemovalTime(configuration.hasRemovalTime());
    }
    
    @Override
    protected JsonObjectConverter getJsonConverterInstance() {
        return SetRemovalTimeJsonConverter.INSTANCE;
    }
    
    @Override
    public String getType() {
        return "batch-set-removal-time";
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("batch-set-removal-time");
    }
}
