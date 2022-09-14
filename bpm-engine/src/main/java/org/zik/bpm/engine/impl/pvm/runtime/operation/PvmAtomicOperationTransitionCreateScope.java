// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationTransitionCreateScope extends PvmAtomicOperationCreateScope
{
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        final PvmActivity activity = execution.getActivity();
        return activity.isAsyncBefore();
    }
    
    @Override
    public String getCanonicalName() {
        return "transition-create-scope";
    }
    
    @Override
    protected void scopeCreated(final PvmExecutionImpl execution) {
        execution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationTransitionCreateScope.TRANSITION_NOTIFY_LISTENER_START);
    }
    
    @Override
    public boolean isAsyncCapable() {
        return true;
    }
}
