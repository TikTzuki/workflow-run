// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import java.util.List;
import org.zik.bpm.engine.impl.bpmn.helper.CompensationUtil;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.parser.CompensateEventDefinition;

public class CompensationEventActivityBehavior extends FlowNodeActivityBehavior
{
    protected final CompensateEventDefinition compensateEventDefinition;
    
    public CompensationEventActivityBehavior(final CompensateEventDefinition compensateEventDefinition) {
        this.compensateEventDefinition = compensateEventDefinition;
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        final List<EventSubscriptionEntity> eventSubscriptions = this.collectEventSubscriptions(execution);
        if (eventSubscriptions.isEmpty()) {
            this.leave(execution);
        }
        else {
            CompensationUtil.throwCompensationEvent(eventSubscriptions, execution, false);
        }
    }
    
    protected List<EventSubscriptionEntity> collectEventSubscriptions(final ActivityExecution execution) {
        final String activityRef = this.compensateEventDefinition.getActivityRef();
        if (activityRef != null) {
            return CompensationUtil.collectCompensateEventSubscriptionsForActivity(execution, activityRef);
        }
        return CompensationUtil.collectCompensateEventSubscriptionsForScope(execution);
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        if (((PvmExecutionImpl)execution).getNonEventScopeExecutions().isEmpty()) {
            this.leave(execution);
        }
        else {
            ((ExecutionEntity)execution).forceUpdate();
        }
    }
}
