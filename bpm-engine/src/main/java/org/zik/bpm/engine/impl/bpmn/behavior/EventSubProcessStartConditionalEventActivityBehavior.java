// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.zik.bpm.engine.impl.bpmn.parser.ConditionalEventDefinition;

public class EventSubProcessStartConditionalEventActivityBehavior extends EventSubProcessStartEventActivityBehavior implements ConditionalEventBehavior
{
    protected final ConditionalEventDefinition conditionalEvent;
    
    public EventSubProcessStartConditionalEventActivityBehavior(final ConditionalEventDefinition conditionalEvent) {
        this.conditionalEvent = conditionalEvent;
    }
    
    @Override
    public ConditionalEventDefinition getConditionalEventDefinition() {
        return this.conditionalEvent;
    }
    
    @Override
    public void leaveOnSatisfiedCondition(final EventSubscriptionEntity eventSubscription, final VariableEvent variableEvent) {
        final PvmExecutionImpl execution = eventSubscription.getExecution();
        if (execution != null && !execution.isEnded() && execution.isScope() && this.conditionalEvent.tryEvaluate(variableEvent, execution)) {
            ActivityImpl activity = eventSubscription.getActivity();
            activity = (ActivityImpl)activity.getFlowScope();
            execution.executeEventHandlerActivity(activity);
        }
    }
}
