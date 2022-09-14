// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import java.util.List;
import org.zik.bpm.engine.impl.bpmn.helper.CompensationUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.PvmActivity;

public class CancelEndEventActivityBehavior extends AbstractBpmnActivityBehavior
{
    protected PvmActivity cancelBoundaryEvent;
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        EnsureUtil.ensureNotNull("Could not find cancel boundary event for cancel end event " + execution.getActivity(), "cancelBoundaryEvent", this.cancelBoundaryEvent);
        final List<EventSubscriptionEntity> compensateEventSubscriptions = CompensationUtil.collectCompensateEventSubscriptionsForScope(execution);
        if (compensateEventSubscriptions.isEmpty()) {
            this.leave(execution);
        }
        else {
            CompensationUtil.throwCompensationEvent(compensateEventSubscriptions, execution, false);
        }
    }
    
    @Override
    public void doLeave(final ActivityExecution execution) {
        final ScopeImpl eventScope = (ScopeImpl)this.cancelBoundaryEvent.getEventScope();
        final ActivityExecution boundaryEventScopeExecution = execution.findExecutionForFlowScope(eventScope);
        boundaryEventScopeExecution.executeActivity(this.cancelBoundaryEvent);
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        if (!execution.hasChildren()) {
            this.leave(execution);
        }
        else {
            ((ExecutionEntity)execution).forceUpdate();
        }
    }
    
    public void setCancelBoundaryEvent(final PvmActivity cancelBoundaryEvent) {
        this.cancelBoundaryEvent = cancelBoundaryEvent;
    }
    
    public PvmActivity getCancelBoundaryEvent() {
        return this.cancelBoundaryEvent;
    }
}
