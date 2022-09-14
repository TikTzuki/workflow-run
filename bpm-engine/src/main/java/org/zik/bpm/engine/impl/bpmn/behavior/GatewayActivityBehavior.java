// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public abstract class GatewayActivityBehavior extends FlowNodeActivityBehavior
{
    protected void lockConcurrentRoot(final ActivityExecution execution) {
        ActivityExecution concurrentRoot = null;
        if (execution.isConcurrent()) {
            concurrentRoot = execution.getParent();
        }
        else {
            concurrentRoot = execution;
        }
        ((ExecutionEntity)concurrentRoot).forceUpdate();
    }
}
