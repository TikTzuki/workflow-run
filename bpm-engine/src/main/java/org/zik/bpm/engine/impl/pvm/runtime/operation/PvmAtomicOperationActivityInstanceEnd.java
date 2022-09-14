// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.pvm.runtime.CompensationBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.PvmLogger;

public abstract class PvmAtomicOperationActivityInstanceEnd extends AbstractPvmEventAtomicOperation
{
    private static final PvmLogger LOG;
    
    @Override
    protected PvmExecutionImpl eventNotificationsStarted(final PvmExecutionImpl execution) {
        execution.incrementSequenceCounter();
        final PvmExecutionImpl parent = execution.getParent();
        final PvmActivity activity = execution.getActivity();
        if (parent != null && execution.isScope() && activity != null && activity.isScope() && (activity.getActivityBehavior() instanceof CompositeActivityBehavior || (CompensationBehavior.isCompensationThrowing(execution) && !LegacyBehavior.isCompensationThrowing(execution)))) {
            PvmAtomicOperationActivityInstanceEnd.LOG.debugLeavesActivityInstance(execution, execution.getActivityInstanceId());
            execution.setActivityInstanceId(parent.getActivityInstanceId());
            parent.leaveActivityInstance();
        }
        execution.setTransition(null);
        return execution;
    }
    
    @Override
    protected void eventNotificationsFailed(final PvmExecutionImpl execution, final Exception e) {
        execution.activityInstanceEndListenerFailure();
        super.eventNotificationsFailed(execution, e);
    }
    
    @Override
    protected boolean isSkipNotifyListeners(final PvmExecutionImpl execution) {
        return execution.hasFailedOnEndListeners() || execution.getActivityInstanceId() == null;
    }
    
    static {
        LOG = ProcessEngineLogger.PVM_LOGGER;
    }
}
