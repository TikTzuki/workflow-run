// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.context;

import org.zik.bpm.engine.impl.context.BpmnExecutionContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateExecution;

public class DelegateExecutionContext
{
    public static DelegateExecution getCurrentDelegationExecution() {
        final BpmnExecutionContext bpmnExecutionContext = Context.getBpmnExecutionContext();
        ExecutionEntity executionEntity = null;
        if (bpmnExecutionContext != null) {
            executionEntity = bpmnExecutionContext.getExecution();
        }
        return executionEntity;
    }
}
