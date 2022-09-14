// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public abstract class AbstractPvmAtomicOperationTransitionNotifyListenerTake extends AbstractPvmEventAtomicOperation
{
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
        final PvmActivity destination = execution.getTransition().getDestination();
        switch (destination.getActivityStartBehavior()) {
            case DEFAULT: {
                execution.setActivity(destination);
                execution.dispatchDelayedEventsAndPerformOperation(AbstractPvmAtomicOperationTransitionNotifyListenerTake.TRANSITION_CREATE_SCOPE);
                break;
            }
            case INTERRUPT_FLOW_SCOPE: {
                execution.setActivity(null);
                execution.performOperation((CoreAtomicOperation<CoreExecution>)AbstractPvmAtomicOperationTransitionNotifyListenerTake.TRANSITION_INTERRUPT_FLOW_SCOPE);
                break;
            }
            default: {
                throw new ProcessEngineException("Unsupported start behavior for activity '" + destination + "' started from a sequence flow: " + destination.getActivityStartBehavior());
            }
        }
    }
    
    @Override
    protected CoreModelElement getScope(final PvmExecutionImpl execution) {
        return execution.getTransition();
    }
    
    @Override
    protected String getEventName() {
        return "take";
    }
}
