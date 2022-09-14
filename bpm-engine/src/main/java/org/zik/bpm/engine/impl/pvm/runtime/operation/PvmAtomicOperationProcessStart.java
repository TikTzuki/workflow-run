// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.pvm.runtime.Callback;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationProcessStart extends AbstractPvmEventAtomicOperation
{
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        return execution.getActivity().isAsyncBefore();
    }
    
    @Override
    public boolean isAsyncCapable() {
        return true;
    }
    
    @Override
    protected ScopeImpl getScope(final PvmExecutionImpl execution) {
        return execution.getProcessDefinition();
    }
    
    @Override
    protected String getEventName() {
        return "start";
    }
    
    @Override
    protected PvmExecutionImpl eventNotificationsStarted(final PvmExecutionImpl execution) {
        execution.setProcessInstanceStarting(true);
        if (execution.getActivity() != null && execution.getActivity().isAsyncBefore()) {
            LegacyBehavior.createMissingHistoricVariables(execution);
        }
        return execution;
    }
    
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
        execution.continueIfExecutionDoesNotAffectNextOperation(new Callback<PvmExecutionImpl, Void>() {
            @Override
            public Void callback(final PvmExecutionImpl execution) {
                execution.dispatchEvent(null);
                return null;
            }
        }, new Callback<PvmExecutionImpl, Void>() {
            @Override
            public Void callback(final PvmExecutionImpl execution) {
                execution.setIgnoreAsync(true);
                execution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperation.ACTIVITY_START_CREATE_SCOPE);
                return null;
            }
        }, execution);
    }
    
    @Override
    public String getCanonicalName() {
        return "process-start";
    }
}
