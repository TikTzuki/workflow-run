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

public class BatchJobDeclaration extends JobDeclaration<BatchJobContext, MessageEntity>
{
    public BatchJobDeclaration(final String jobHandlerType) {
        super(jobHandlerType);
    }
    
    @Override
    protected ExecutionEntity resolveExecution(final BatchJobContext context) {
        return null;
    }
    
    @Override
    protected MessageEntity newJobInstance(final BatchJobContext context) {
        return new MessageEntity();
    }
    
    @Override
    protected JobHandlerConfiguration resolveJobHandlerConfiguration(final BatchJobContext context) {
        return new BatchJobConfiguration(context.getConfiguration().getId());
    }
    
    @Override
    protected String resolveJobDefinitionId(final BatchJobContext context) {
        return context.getBatch().getBatchJobDefinitionId();
    }
    
    @Override
    public ParameterValueProvider getJobPriorityProvider() {
        final long batchJobPriority = Context.getProcessEngineConfiguration().getBatchJobPriority();
        return new ConstantValueProvider(batchJobPriority);
    }
}
