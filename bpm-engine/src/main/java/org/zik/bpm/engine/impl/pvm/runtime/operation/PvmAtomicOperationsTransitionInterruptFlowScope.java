// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationsTransitionInterruptFlowScope extends PvmAtomicOperationInterruptScope
{
    @Override
    public String getCanonicalName() {
        return "transition-interrupt-scope";
    }
    
    @Override
    protected void scopeInterrupted(final PvmExecutionImpl execution) {
        execution.dispatchDelayedEventsAndPerformOperation(PvmAtomicOperationsTransitionInterruptFlowScope.TRANSITION_CREATE_SCOPE);
    }
    
    @Override
    protected PvmActivity getInterruptingActivity(final PvmExecutionImpl execution) {
        return execution.getTransition().getDestination();
    }
}
