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
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.Date;
import org.zik.bpm.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import java.util.Iterator;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.batch.BatchJobConfiguration;
import org.zik.bpm.engine.impl.batch.BatchJobDeclaration;
import org.zik.bpm.engine.impl.batch.AbstractBatchJobHandler;

public class ProcessSetRemovalTimeJobHandler extends AbstractBatchJobHandler<SetRemovalTimeBatchConfiguration>
{
    public static final BatchJobDeclaration JOB_DECLARATION;
    
    @Override
    public void execute(final BatchJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final String byteArrayId = configuration.getConfigurationByteArrayId();
        final byte[] configurationByteArray = this.findByteArrayById(byteArrayId, commandContext).getBytes();
        final SetRemovalTimeBatchConfiguration batchConfiguration = this.readConfiguration(configurationByteArray);
        for (final String instanceId : batchConfiguration.getIds()) {
            final HistoricProcessInstanceEntity instance = this.findProcessInstanceById(instanceId, commandContext);
            if (instance != null) {
                if (batchConfiguration.isHierarchical() && this.hasHierarchy(instance)) {
                    final String rootProcessInstanceId = instance.getRootProcessInstanceId();
                    final HistoricProcessInstanceEntity rootInstance = this.findProcessInstanceById(rootProcessInstanceId, commandContext);
                    final Date removalTime = this.getOrCalculateRemovalTime(batchConfiguration, rootInstance, commandContext);
                    this.addRemovalTimeToHierarchy(rootProcessInstanceId, removalTime, commandContext);
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
    
    protected Date getOrCalculateRemovalTime(final SetRemovalTimeBatchConfiguration batchConfiguration, final HistoricProcessInstanceEntity instance, final CommandContext commandContext) {
        if (batchConfiguration.hasRemovalTime()) {
            return batchConfiguration.getRemovalTime();
        }
        if (this.hasBaseTime(instance, commandContext)) {
            return this.calculateRemovalTime(instance, commandContext);
        }
        return null;
    }
    
    protected void addRemovalTimeToHierarchy(final String rootProcessInstanceId, final Date removalTime, final CommandContext commandContext) {
        commandContext.getHistoricProcessInstanceManager().addRemovalTimeToProcessInstancesByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        if (this.isDmnEnabled(commandContext)) {
            commandContext.getHistoricDecisionInstanceManager().addRemovalTimeToDecisionsByRootProcessInstanceId(rootProcessInstanceId, removalTime);
        }
    }
    
    protected void addRemovalTime(final String instanceId, final Date removalTime, final CommandContext commandContext) {
        commandContext.getHistoricProcessInstanceManager().addRemovalTimeById(instanceId, removalTime);
        if (this.isDmnEnabled(commandContext)) {
            commandContext.getHistoricDecisionInstanceManager().addRemovalTimeToDecisionsByProcessInstanceId(instanceId, removalTime);
        }
    }
    
    protected boolean hasBaseTime(final HistoricProcessInstanceEntity instance, final CommandContext commandContext) {
        return this.isStrategyStart(commandContext) || (this.isStrategyEnd(commandContext) && this.isEnded(instance));
    }
    
    protected boolean isEnded(final HistoricProcessInstanceEntity instance) {
        return instance.getEndTime() != null;
    }
    
    protected boolean isStrategyStart(final CommandContext commandContext) {
        return "start".equals(this.getHistoryRemovalTimeStrategy(commandContext));
    }
    
    protected boolean isStrategyEnd(final CommandContext commandContext) {
        return "end".equals(this.getHistoryRemovalTimeStrategy(commandContext));
    }
    
    protected boolean hasHierarchy(final HistoricProcessInstanceEntity instance) {
        return instance.getRootProcessInstanceId() != null;
    }
    
    protected String getHistoryRemovalTimeStrategy(final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getHistoryRemovalTimeStrategy();
    }
    
    protected ProcessDefinition findProcessDefinitionById(final String processDefinitionId, final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(processDefinitionId);
    }
    
    protected boolean isDmnEnabled(final CommandContext commandContext) {
        return commandContext.getProcessEngineConfiguration().isDmnEnabled();
    }
    
    protected Date calculateRemovalTime(final HistoricProcessInstanceEntity processInstance, final CommandContext commandContext) {
        final ProcessDefinition processDefinition = this.findProcessDefinitionById(processInstance.getProcessDefinitionId(), commandContext);
        return commandContext.getProcessEngineConfiguration().getHistoryRemovalTimeProvider().calculateRemovalTime(processInstance, processDefinition);
    }
    
    protected ByteArrayEntity findByteArrayById(final String byteArrayId, final CommandContext commandContext) {
        return commandContext.getDbEntityManager().selectById(ByteArrayEntity.class, byteArrayId);
    }
    
    protected HistoricProcessInstanceEntity findProcessInstanceById(final String instanceId, final CommandContext commandContext) {
        return commandContext.getHistoricProcessInstanceManager().findHistoricProcessInstance(instanceId);
    }
    
    @Override
    public JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration() {
        return ProcessSetRemovalTimeJobHandler.JOB_DECLARATION;
    }
    
    @Override
    protected SetRemovalTimeBatchConfiguration createJobConfiguration(final SetRemovalTimeBatchConfiguration configuration, final List<String> processInstanceIds) {
        return new SetRemovalTimeBatchConfiguration(processInstanceIds).setRemovalTime(configuration.getRemovalTime()).setHasRemovalTime(configuration.hasRemovalTime()).setHierarchical(configuration.isHierarchical());
    }
    
    @Override
    protected JsonObjectConverter<SetRemovalTimeBatchConfiguration> getJsonConverterInstance() {
        return SetRemovalTimeJsonConverter.INSTANCE;
    }
    
    @Override
    public String getType() {
        return "process-set-removal-time";
    }
    
    static {
        JOB_DECLARATION = new BatchJobDeclaration("process-set-removal-time");
    }
}
