// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.pvm.delegate.CompositeActivityBehavior;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationActivityEnd implements PvmAtomicOperation
{
    protected PvmScope getScope(final PvmExecutionImpl execution) {
        return execution.getActivity();
    }
    
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        return execution.getActivity().isAsyncAfter();
    }
    
    @Override
    public boolean isAsyncCapable() {
        return false;
    }
    
    @Override
    public void execute(final PvmExecutionImpl execution) {
        if (execution.getActivityInstanceId() == null) {
            execution.setActivityInstanceId(execution.getParentActivityInstanceId());
        }
        final PvmActivity activity = execution.getActivity();
        final Map<ScopeImpl, PvmExecutionImpl> activityExecutionMapping = execution.createActivityExecutionMapping();
        PvmExecutionImpl propagatingExecution = execution;
        if (execution.isScope() && activity.isScope() && !LegacyBehavior.destroySecondNonScope(execution)) {
            execution.destroy();
            if (!execution.isConcurrent()) {
                execution.remove();
                propagatingExecution = execution.getParent();
                propagatingExecution.setActivity(execution.getActivity());
            }
        }
        propagatingExecution = LegacyBehavior.determinePropagatingExecutionOnEnd(propagatingExecution, activityExecutionMapping);
        final PvmScope flowScope = activity.getFlowScope();
        if (flowScope == activity.getProcessDefinition()) {
            if (propagatingExecution.isConcurrent()) {
                propagatingExecution.remove();
                propagatingExecution.getParent().tryPruneLastConcurrentChild();
                propagatingExecution.getParent().forceUpdate();
            }
            else {
                propagatingExecution.setEnded(true);
                if (!propagatingExecution.isPreserveScope()) {
                    propagatingExecution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationActivityEnd.PROCESS_END);
                }
            }
        }
        else {
            final PvmActivity flowScopeActivity = (PvmActivity)flowScope;
            final ActivityBehavior activityBehavior = flowScopeActivity.getActivityBehavior();
            if (!(activityBehavior instanceof CompositeActivityBehavior)) {
                throw new ProcessEngineException("Expected behavior of composite scope " + activity + " to be a CompositeActivityBehavior but got " + activityBehavior);
            }
            final CompositeActivityBehavior compositeActivityBehavior = (CompositeActivityBehavior)activityBehavior;
            if (propagatingExecution.isConcurrent() && !LegacyBehavior.isConcurrentScope(propagatingExecution)) {
                compositeActivityBehavior.concurrentChildExecutionEnded(propagatingExecution.getParent(), propagatingExecution);
            }
            else {
                propagatingExecution.setActivity(flowScopeActivity);
                compositeActivityBehavior.complete(propagatingExecution);
            }
        }
    }
    
    @Override
    public String getCanonicalName() {
        return "activity-end";
    }
}
