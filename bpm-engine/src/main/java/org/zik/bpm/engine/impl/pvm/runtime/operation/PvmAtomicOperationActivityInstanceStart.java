// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.runtime.CompensationBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public abstract class PvmAtomicOperationActivityInstanceStart extends AbstractPvmEventAtomicOperation
{
    @Override
    protected PvmExecutionImpl eventNotificationsStarted(final PvmExecutionImpl execution) {
        execution.incrementSequenceCounter();
        execution.activityInstanceStarting();
        execution.enterActivityInstance();
        execution.setTransition(null);
        return execution;
    }
    
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
        final PvmExecutionImpl parent = execution.getParent();
        final PvmActivity activity = execution.getActivity();
        if (parent != null && execution.isScope() && activity.isScope() && this.canHaveChildScopes(execution)) {
            parent.setActivityInstanceId(execution.getActivityInstanceId());
        }
    }
    
    protected boolean canHaveChildScopes(final PvmExecutionImpl execution) {
        final PvmActivity activity = execution.getActivity();
        return activity.getActivityBehavior() instanceof CompositeActivityBehavior || CompensationBehavior.isCompensationThrowing(execution);
    }
}
