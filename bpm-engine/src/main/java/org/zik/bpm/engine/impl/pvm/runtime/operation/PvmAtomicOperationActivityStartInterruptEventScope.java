// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationActivityStartInterruptEventScope extends PvmAtomicOperationInterruptScope
{
    @Override
    public String getCanonicalName() {
        return "activity-start-interrupt-scope";
    }
    
    @Override
    protected void scopeInterrupted(final PvmExecutionImpl execution) {
        execution.setActivityInstanceId(null);
        execution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationActivityStartInterruptEventScope.ACTIVITY_START_CREATE_SCOPE);
    }
    
    @Override
    protected PvmActivity getInterruptingActivity(final PvmExecutionImpl execution) {
        final PvmActivity nextActivity = execution.getNextActivity();
        execution.setNextActivity(null);
        return nextActivity;
    }
}
