// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationActivityStartCreateScope extends PvmAtomicOperationCreateScope
{
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        final PvmActivity activity = execution.getActivity();
        return activity.isAsyncBefore();
    }
    
    @Override
    public boolean isAsyncCapable() {
        return true;
    }
    
    @Override
    public String getCanonicalName() {
        return "activity-start-create-scope";
    }
    
    @Override
    protected void scopeCreated(final PvmExecutionImpl execution) {
        execution.setIgnoreAsync(false);
        execution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationActivityStartCreateScope.ACTIVITY_START);
    }
}
