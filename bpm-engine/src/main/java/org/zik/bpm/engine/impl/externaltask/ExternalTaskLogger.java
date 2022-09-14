// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.externaltask;

import org.zik.bpm.engine.impl.bpmn.parser.CamundaErrorEventDefinition;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class ExternalTaskLogger extends ProcessEngineLogger
{
    public void couldNotDeterminePriority(final ExecutionEntity execution, final Object value, final ProcessEngineException e) {
        this.logWarn("001", "Could not determine priority for external task created in context of execution {}. Using default priority {}", new Object[] { execution, value, e });
    }
    
    public void errorEventDefinitionEvaluationException(final String taskId, final CamundaErrorEventDefinition errorEventDefinition, final Exception exception) {
        this.logDebug("002", "Evaluation of error event definition's expression {} on external task {} failed and will be considered as 'false'. Received exception: {}", new Object[] { errorEventDefinition.getExpression(), taskId, exception.getMessage() });
    }
}
