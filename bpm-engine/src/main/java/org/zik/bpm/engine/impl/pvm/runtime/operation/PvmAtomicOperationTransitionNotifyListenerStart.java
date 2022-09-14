// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationTransitionNotifyListenerStart extends PvmAtomicOperationActivityInstanceStart
{
    @Override
    protected ScopeImpl getScope(final PvmExecutionImpl execution) {
        return execution.getActivity();
    }
    
    @Override
    protected String getEventName() {
        return "start";
    }
    
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
        super.eventNotificationsCompleted(execution);
        final TransitionImpl transition = execution.getTransition();
        PvmActivity destination;
        if (transition == null) {
            destination = execution.getActivity();
        }
        else {
            destination = transition.getDestination();
        }
        execution.setTransition(null);
        execution.setActivity(destination);
        if (execution.isProcessInstanceStarting()) {
            execution.setProcessInstanceStarting(false);
        }
        execution.dispatchDelayedEventsAndPerformOperation(PvmAtomicOperationTransitionNotifyListenerStart.ACTIVITY_EXECUTE);
    }
    
    @Override
    public String getCanonicalName() {
        return "transition-notifiy-listener-start";
    }
    
    @Override
    public boolean shouldHandleFailureAsBpmnError() {
        return true;
    }
}
