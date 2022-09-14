// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.pvm.PvmException;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.behavior.FlowNodeActivityBehavior;
import org.zik.bpm.engine.impl.util.ActivityBehaviorUtil;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.PvmLogger;

public class PvmAtomicOperationActivityLeave implements PvmAtomicOperation
{
    private static final PvmLogger LOG;
    
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        return false;
    }
    
    @Override
    public void execute(final PvmExecutionImpl execution) {
        execution.activityInstanceDone();
        final ActivityBehavior activityBehavior = ActivityBehaviorUtil.getActivityBehavior(execution);
        if (activityBehavior instanceof FlowNodeActivityBehavior) {
            final FlowNodeActivityBehavior behavior = (FlowNodeActivityBehavior)activityBehavior;
            final ActivityImpl activity = execution.getActivity();
            final String activityInstanceId = execution.getActivityInstanceId();
            if (activityInstanceId != null) {
                PvmAtomicOperationActivityLeave.LOG.debugLeavesActivityInstance(execution, activityInstanceId);
            }
            try {
                behavior.doLeave(execution);
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e2) {
                throw new PvmException("couldn't leave activity <" + activity.getProperty("type") + " id=\"" + activity.getId() + "\" ...>: " + e2.getMessage(), e2);
            }
            return;
        }
        throw new PvmException("Behavior of current activity is not an instance of " + FlowNodeActivityBehavior.class.getSimpleName() + ". Execution " + execution);
    }
    
    @Override
    public String getCanonicalName() {
        return "activity-leave";
    }
    
    @Override
    public boolean isAsyncCapable() {
        return false;
    }
    
    static {
        LOG = PvmLogger.PVM_LOGGER;
    }
}
