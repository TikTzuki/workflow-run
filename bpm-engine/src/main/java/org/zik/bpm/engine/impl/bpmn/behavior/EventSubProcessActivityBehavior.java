// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class EventSubProcessActivityBehavior extends SubProcessActivityBehavior
{
    @Override
    public void complete(final ActivityExecution scopeExecution) {
        if (!LegacyBehavior.eventSubprocessComplete(scopeExecution)) {
            super.complete(scopeExecution);
        }
    }
    
    @Override
    public void concurrentChildExecutionEnded(final ActivityExecution scopeExecution, final ActivityExecution endedExecution) {
        if (!LegacyBehavior.eventSubprocessConcurrentChildExecutionEnded(scopeExecution, endedExecution)) {
            super.concurrentChildExecutionEnded(scopeExecution, endedExecution);
        }
    }
}
