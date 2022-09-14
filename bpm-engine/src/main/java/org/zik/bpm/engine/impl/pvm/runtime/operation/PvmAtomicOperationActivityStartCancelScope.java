// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationActivityStartCancelScope extends PvmAtomicOperationCancelActivity
{
    @Override
    public String getCanonicalName() {
        return "activity-start-cancel-scope";
    }
    
    @Override
    protected void activityCancelled(final PvmExecutionImpl execution) {
        execution.setActivityInstanceId(null);
        execution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationActivityStartCancelScope.ACTIVITY_START_CREATE_SCOPE);
    }
    
    protected PvmActivity getCancellingActivity(final PvmExecutionImpl execution) {
        return execution.getNextActivity();
    }
    
    @Override
    public boolean isAsyncCapable() {
        return false;
    }
}
