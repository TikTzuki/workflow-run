// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.CompensationBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationDeleteCascadeFireActivityEnd extends PvmAtomicOperationActivityInstanceEnd
{
    @Override
    protected PvmExecutionImpl eventNotificationsStarted(final PvmExecutionImpl execution) {
        execution.setCanceled(true);
        return super.eventNotificationsStarted(execution);
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
        return "end";
    }
    
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
        final PvmActivity activity = execution.getActivity();
        if (execution.isScope() && (this.executesNonScopeActivity(execution) || this.isAsyncBeforeActivity(execution)) && !CompensationBehavior.executesNonScopeCompensationHandler(execution)) {
            execution.removeAllTasks();
            execution.leaveActivityInstance();
            execution.setActivity(this.getFlowScopeActivity(activity));
            execution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationDeleteCascadeFireActivityEnd.DELETE_CASCADE_FIRE_ACTIVITY_END);
        }
        else {
            if (execution.isScope()) {
                if (execution instanceof ExecutionEntity && !execution.isProcessInstanceExecution() && execution.isCanceled()) {
                    execution.setSkipIoMappings(execution.isSkipIoMappings() || execution.getProcessEngine().getProcessEngineConfiguration().isSkipOutputMappingOnCanceledActivities());
                }
                execution.destroy();
            }
            execution.remove();
            boolean continueRemoval = !execution.isDeleteRoot();
            if (continueRemoval) {
                PvmExecutionImpl propagatingExecution = execution.getParent();
                if (propagatingExecution != null && !propagatingExecution.isScope() && !propagatingExecution.hasChildren()) {
                    propagatingExecution.remove();
                    continueRemoval = !propagatingExecution.isDeleteRoot();
                    propagatingExecution = propagatingExecution.getParent();
                }
                if (continueRemoval && propagatingExecution != null && propagatingExecution.getActivity() == null && activity != null && activity.getFlowScope() != null) {
                    propagatingExecution.setActivity(this.getFlowScopeActivity(activity));
                }
            }
        }
    }
    
    protected boolean executesNonScopeActivity(final PvmExecutionImpl execution) {
        final ActivityImpl activity = execution.getActivity();
        return activity != null && !activity.isScope();
    }
    
    protected boolean isAsyncBeforeActivity(final PvmExecutionImpl execution) {
        return execution.getActivityId() != null && execution.getActivityInstanceId() == null;
    }
    
    protected ActivityImpl getFlowScopeActivity(final PvmActivity activity) {
        final ScopeImpl flowScope = activity.getFlowScope();
        ActivityImpl flowScopeActivity = null;
        if (flowScope.getProcessDefinition() != flowScope) {
            flowScopeActivity = (ActivityImpl)flowScope;
        }
        return flowScopeActivity;
    }
    
    @Override
    public String getCanonicalName() {
        return "delete-cascade-fire-activity-end";
    }
}
