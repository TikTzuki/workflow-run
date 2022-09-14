// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.pvm.PvmException;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.pvm.runtime.Callback;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.PvmLogger;

public class PvmAtomicOperationActivityExecute implements PvmAtomicOperation
{
    private static final PvmLogger LOG;
    
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        return false;
    }
    
    @Override
    public void execute(final PvmExecutionImpl execution) {
        execution.activityInstanceStarted();
        execution.continueIfExecutionDoesNotAffectNextOperation(new Callback<PvmExecutionImpl, Void>() {
            @Override
            public Void callback(final PvmExecutionImpl execution) {
                if (execution.getActivity().isScope()) {
                    execution.dispatchEvent(null);
                }
                return null;
            }
        }, new Callback<PvmExecutionImpl, Void>() {
            @Override
            public Void callback(final PvmExecutionImpl execution) {
                final ActivityBehavior activityBehavior = ActivityBehaviorUtil.getActivityBehavior(execution);
                final ActivityImpl activity = execution.getActivity();
                PvmAtomicOperationActivityExecute.LOG.debugExecutesActivity(execution, activity, activityBehavior.getClass().getName());
                try {
                    activityBehavior.execute((ActivityExecution)execution);
                }
                catch (RuntimeException e) {
                    throw e;
                }
                catch (Exception e2) {
                    throw new PvmException("couldn't execute activity <" + activity.getProperty("type") + " id=\"" + activity.getId() + "\" ...>: " + e2.getMessage(), e2);
                }
                return null;
            }
        }, execution);
    }
    
    @Override
    public String getCanonicalName() {
        return "activity-execute";
    }
    
    @Override
    public boolean isAsyncCapable() {
        return false;
    }
    
    static {
        LOG = PvmLogger.PVM_LOGGER;
    }
}
