// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.runtime.Execution;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.runtime.MessageCorrelationResultType;

public class CorrelationHandlerResult
{
    protected MessageCorrelationResultType resultType;
    protected ExecutionEntity executionEntity;
    protected ProcessDefinitionEntity processDefinitionEntity;
    protected String startEventActivityId;
    
    public static CorrelationHandlerResult matchedExecution(final ExecutionEntity executionEntity) {
        final CorrelationHandlerResult messageCorrelationResult = new CorrelationHandlerResult();
        messageCorrelationResult.resultType = MessageCorrelationResultType.Execution;
        messageCorrelationResult.executionEntity = executionEntity;
        return messageCorrelationResult;
    }
    
    public static CorrelationHandlerResult matchedProcessDefinition(final ProcessDefinitionEntity processDefinitionEntity, final String startEventActivityId) {
        final CorrelationHandlerResult messageCorrelationResult = new CorrelationHandlerResult();
        messageCorrelationResult.processDefinitionEntity = processDefinitionEntity;
        messageCorrelationResult.startEventActivityId = startEventActivityId;
        messageCorrelationResult.resultType = MessageCorrelationResultType.ProcessDefinition;
        return messageCorrelationResult;
    }
    
    public ExecutionEntity getExecutionEntity() {
        return this.executionEntity;
    }
    
    public ProcessDefinitionEntity getProcessDefinitionEntity() {
        return this.processDefinitionEntity;
    }
    
    public String getStartEventActivityId() {
        return this.startEventActivityId;
    }
    
    public MessageCorrelationResultType getResultType() {
        return this.resultType;
    }
    
    public Execution getExecution() {
        return this.executionEntity;
    }
    
    public ProcessDefinition getProcessDefinition() {
        return this.processDefinitionEntity;
    }
}
