// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationActivityStart extends PvmAtomicOperationActivityInstanceStart
{
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
        super.eventNotificationsCompleted(execution);
        execution.dispatchDelayedEventsAndPerformOperation(PvmAtomicOperationActivityStart.ACTIVITY_EXECUTE);
    }
    
    @Override
    protected String getEventName() {
        return "start";
    }
    
    @Override
    protected ScopeImpl getScope(final PvmExecutionImpl execution) {
        return execution.getActivity();
    }
    
    @Override
    public String getCanonicalName() {
        return "activity-start";
    }
    
    @Override
    public boolean shouldHandleFailureAsBpmnError() {
        return true;
    }
}
