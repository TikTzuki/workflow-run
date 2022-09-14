// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public abstract class PvmAtomicOperationCancelActivity implements PvmAtomicOperation
{
    @Override
    public void execute(final PvmExecutionImpl execution) {
        final PvmActivity cancellingActivity = execution.getNextActivity();
        execution.setNextActivity(null);
        execution.setActive(true);
        PvmExecutionImpl propagatingExecution = null;
        if (LegacyBehavior.isConcurrentScope(execution)) {
            LegacyBehavior.cancelConcurrentScope(execution, (PvmActivity)cancellingActivity.getEventScope());
            propagatingExecution = execution;
        }
        else {
            execution.deleteCascade("Cancel scope activity " + cancellingActivity + " executed.");
            propagatingExecution = execution.getParent();
        }
        propagatingExecution.setActivity(cancellingActivity);
        propagatingExecution.setActive(true);
        propagatingExecution.setEnded(false);
        this.activityCancelled(propagatingExecution);
    }
    
    protected abstract void activityCancelled(final PvmExecutionImpl p0);
    
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        return false;
    }
}
