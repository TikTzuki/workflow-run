// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.runtime.Callback;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationActivityNotifyListenerEnd extends PvmAtomicOperationActivityInstanceEnd
{
    @Override
    protected ScopeImpl getScope(final PvmExecutionImpl execution) {
        return execution.getActivity();
    }
    
    @Override
    protected String getEventName() {
        return "end";
    }
    
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
        execution.dispatchDelayedEventsAndPerformOperation(new Callback<PvmExecutionImpl, Void>() {
            @Override
            public Void callback(final PvmExecutionImpl execution) {
                execution.leaveActivityInstance();
                execution.setActivityInstanceId(null);
                execution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperation.ACTIVITY_END);
                return null;
            }
        });
    }
    
    @Override
    public String getCanonicalName() {
        return "activity-notify-listener-end";
    }
    
    @Override
    public boolean shouldHandleFailureAsBpmnError() {
        return true;
    }
}
