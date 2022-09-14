// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ConstantValueProvider;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;

public class BatchMonitorJobDeclaration extends JobDeclaration<BatchEntity, MessageEntity>
{
    private static final long serialVersionUID = 1L;
    
    public BatchMonitorJobDeclaration() {
        super("batch-monitor-job");
    }
    
    @Override
    protected ExecutionEntity resolveExecution(final BatchEntity batch) {
        return null;
    }
    
    @Override
    protected MessageEntity newJobInstance(final BatchEntity batch) {
        return new MessageEntity();
    }
    
    @Override
    protected JobHandlerConfiguration resolveJobHandlerConfiguration(final BatchEntity batch) {
        return new BatchMonitorJobHandler.BatchMonitorJobConfiguration(batch.getId());
    }
    
    @Override
    protected String resolveJobDefinitionId(final BatchEntity batch) {
        return batch.getMonitorJobDefinitionId();
    }
    
    @Override
    public ParameterValueProvider getJobPriorityProvider() {
        final long batchJobPriority = Context.getProcessEngineConfiguration().getBatchJobPriority();
        return new ConstantValueProvider(batchJobPriority);
    }
}
