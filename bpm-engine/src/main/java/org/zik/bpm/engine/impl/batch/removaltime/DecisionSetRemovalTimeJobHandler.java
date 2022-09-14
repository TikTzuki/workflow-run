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
import org.zik.bpm.engine.repository.DecisionDefinition;
import java.util.Date;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInstanceEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class DecisionSetRemovalTimeJobHandler extends AbstractBatchJobHandler<SetRemovalTimeBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        if (this.isDmnEnabled(commandContext)) {
            final String byteArrayId = configuration.getConfigurationByteArrayId();
            final byte[] configurationByteArray = this.findByteArrayById(byteArrayId, commandContext).getBytes();
            final SetRemovalTimeBatchConfiguration batchConfiguration = this.readConfiguration(configurationByteArray);
            for (final String instanceId : batchConfiguration.getIds()) {
                final HistoricDecisionInstanceEntity instance = this.findDecisionInstanceById(instanceId, commandContext);
                if (instance != null) {
                    if (batchConfiguration.isHierarchical()) {
                        final String rootDecisionInstanceId = this.getRootDecisionInstance(instance);
                        final HistoricDecisionInstanceEntity rootInstance = this.findDecisionInstanceById(rootDecisionInstanceId, commandContext);
                        final Date removalTime = this.getOrCalculateRemovalTime(batchConfiguration, rootInstance, commandContext);
                        this.addRemovalTimeToHierarchy(rootDecisionInstanceId, removalTime, commandContext);
                    }
                    else {
                        final Date removalTime2 = this.getOrCalculateRemovalTime(batchConfiguration, instance, commandContext);
                        if (removalTime2 == instance.getRemovalTime()) {
                            continue;
                        }
                        this.addRemovalTime(instanceId, removalTime2, commandContext);
                    }
                }
            }
        }
    }
    
    protected String getRootDecisionInstance(final HistoricDecisionInstanceEntity instance) {
        return (instance.getRootDecisionInstanceId() == null) ? instance.getId() : instance.getRootDecisionInstanceId();
    }
    
    protected Date getOrCalculateRemovalTime(final SetRemovalTimeBatchConfiguration batchConfiguration, final HistoricDecisionInstanceEntity instance, final CommandContext commandContext) {
        if (batchConfiguration.hasRemovalTime()) {
            return batchConfiguration.getRemovalTime();
        }
        if (this.hasBaseTime(commandContext)) {
            return this.calculateRemovalTime(instance, commandContext);
        }
        return null;
    }
    
    protected void addRemovalTimeToHierarchy(final String instanceId, final Date removalTime, final CommandContext commandContext) {
        commandContext.getHistoricDecisionInstanceManager().addRemovalTimeToDecisionsByRootDecisionInstanceId(instanceId, removalTime);
    }
    
    protected void addRemovalTime(final String instanceId, final Date removalTime, final CommandContext commandContext) {
        commandContext.getHistoricDecisionInstanceManager().addRemovalTimeToDecisionsByDecisionInstanceId(instanceId, removalTime);
    }
    
    protected boolean hasBaseTime(final CommandContext commandContext) {
        return this.isStrategyStart(commandContext) || this.isStrategyEnd(commandContext);
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
    
    protected DecisionDefinition findDecisionDefinitionById(final String decisionDefinitionId, final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedDecisionDefinitionById(decisionDefinitionId);
    }
    
    protected boolean isDmnEnabled(final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().isDmnEnabled();
    }
    
    protected Date calculateRemovalTime(final HistoricDecisionInstanceEntity decisionInstance, final CommandContext commandContext) {
        final DecisionDefinition decisionDefinition = this.findDecisionDefinitionById(decisionInstance.getDecisionDefinitionId(), commandContext);
        return commandContext.getProcessEngineConfiguration().getHistoryRemovalTimeProvider().calculateRemovalTime(decisionInstance, decisionDefinition);
    }
    
    protected ByteArrayEntity findByteArrayById(final String byteArrayId, final CommandContext commandContext) {
        return commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, byteArrayId);
    }
    
    protected HistoricDecisionInstanceEntity findDecisionInstanceById(final String instanceId, final CommandContext commandContext) {
        return commandContext.getHistoricDecisionInstanceManager().findHistoricDecisionInstance(instanceId);
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return DecisionSetRemovalTimeJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected SetRemovalTimeBatchConfiguration createJobConfiguration(final SetRemovalTimeBatchConfiguration configuration, final List<String> decisionInstanceIds) {
        return new SetRemovalTimeBatchConfiguration(decisionInstanceIds).setRemovalTime(configuration.getRemovalTime()).setHasRemovalTime(configuration.hasRemovalTime()).setHierarchical(configuration.isHierarchical());
    }
    
    @Override
    protected JsonObjectConverter getJsonConverterInstance() {
        return SetRemovalTimeJsonConverter.INSTANCE;
    }
    
    @Override
    public String getType() {
        return "decision-set-removal-time";
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("decision-set-removal-time");
    }
}
