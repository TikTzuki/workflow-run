// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.runtime.InstantiationStack;
import org.zik.bpm.engine.impl.pvm.runtime.ScopeInstantiationContext;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ModificationObserverBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationActivityInitStackNotifyListenerStart extends PvmAtomicOperationActivityInstanceStart
{
    @Override
    public String getCanonicalName() {
        return "activity-init-stack-notify-listener-start";
    }
    
    @Override
    protected ScopeImpl getScope(final PvmExecutionImpl execution) {
        final ActivityImpl activity = execution.getActivity();
        if (activity != null) {
            return activity;
        }
        final PvmExecutionImpl parent = execution.getParent();
        if (parent != null) {
            return this.getScope(execution.getParent());
        }
        return execution.getProcessDefinition();
    }
    
    @Override
    protected String getEventName() {
        return "start";
    }
    
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
        super.eventNotificationsCompleted(execution);
        execution.activityInstanceStarted();
        final ScopeInstantiationContext startContext = execution.getScopeInstantiationContext();
        final InstantiationStack instantiationStack = startContext.getInstantiationStack();
        PvmExecutionImpl propagatingExecution = execution;
        final ActivityImpl activity = execution.getActivity();
        if (activity.getActivityBehavior() instanceof ModificationObserverBehavior) {
            final ModificationObserverBehavior behavior = (ModificationObserverBehavior)activity.getActivityBehavior();
            final List<ActivityExecution> concurrentExecutions = behavior.initializeScope(propagatingExecution, 1);
            propagatingExecution = concurrentExecutions.get(0);
        }
        if (instantiationStack.getActivities().isEmpty() && instantiationStack.getTargetActivity() != null) {
            propagatingExecution.setActivityInstanceId(null);
            startContext.applyVariables(propagatingExecution);
            propagatingExecution.setActivity(instantiationStack.getTargetActivity());
            propagatingExecution.disposeScopeInstantiationContext();
            propagatingExecution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationActivityInitStackNotifyListenerStart.ACTIVITY_START_CREATE_SCOPE);
        }
        else if (instantiationStack.getActivities().isEmpty() && instantiationStack.getTargetTransition() != null) {
            propagatingExecution.setActivityInstanceId(null);
            final PvmTransition transition = instantiationStack.getTargetTransition();
            startContext.applyVariables(propagatingExecution);
            propagatingExecution.setActivity(transition.getSource());
            propagatingExecution.setTransition(transition);
            propagatingExecution.disposeScopeInstantiationContext();
            propagatingExecution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationActivityInitStackNotifyListenerStart.TRANSITION_START_NOTIFY_LISTENER_TAKE);
        }
        else {
            propagatingExecution.setActivity(null);
            propagatingExecution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationActivityInitStackNotifyListenerStart.ACTIVITY_INIT_STACK);
        }
    }
}
