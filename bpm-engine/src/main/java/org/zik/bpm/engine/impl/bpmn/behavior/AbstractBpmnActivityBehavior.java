// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.bpmn.helper.ErrorPropagationException;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnExceptionHandler;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.event.EventType;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class AbstractBpmnActivityBehavior extends FlowNodeActivityBehavior
{
    protected static final BpmnBehaviorLogger LOG;
    
    @Override
    public void doLeave(final ActivityExecution execution) {
        final PvmActivity currentActivity = execution.getActivity();
        final ActivityImpl compensationHandler = ((ActivityImpl)currentActivity).findCompensationHandler();
        if (compensationHandler != null && !this.isCompensationEventSubprocess(compensationHandler)) {
            this.createCompensateEventSubscription(execution, compensationHandler);
        }
        super.doLeave(execution);
    }
    
    protected boolean isCompensationEventSubprocess(final ActivityImpl activity) {
        return activity.isCompensationHandler() && activity.isSubProcessScope() && activity.isTriggeredByEvent();
    }
    
    protected void createCompensateEventSubscription(final ActivityExecution execution, final ActivityImpl compensationHandler) {
        final PvmActivity currentActivity = execution.getActivity();
        final ActivityExecution scopeExecution = execution.findExecutionForFlowScope(currentActivity.getFlowScope());
        EventSubscriptionEntity.createAndInsert((ExecutionEntity)scopeExecution, EventType.COMPENSATE, compensationHandler);
    }
    
    protected void executeWithErrorPropagation(final ActivityExecution execution, final Callable<Void> toExecute) throws Exception {
        final String activityInstanceId = execution.getActivityInstanceId();
        try {
            toExecute.call();
        }
        catch (Exception ex) {
            if (activityInstanceId.equals(execution.getActivityInstanceId())) {
                try {
                    BpmnExceptionHandler.propagateException(execution, ex);
                    return;
                }
                catch (ErrorPropagationException e) {
                    throw ex;
                }
            }
            throw ex;
        }
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        if ("compensationDone".equals(signalName)) {
            this.signalCompensationDone(execution);
        }
        else {
            super.signal(execution, signalName, signalData);
        }
    }
    
    protected void signalCompensationDone(final ActivityExecution execution) {
        if (((PvmExecutionImpl)execution).getNonEventScopeExecutions().isEmpty()) {
            if (execution.getParent() != null) {
                final ActivityExecution parent = execution.getParent();
                execution.remove();
                parent.signal("compensationDone", null);
            }
        }
        else {
            ((ExecutionEntity)execution).forceUpdate();
        }
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
