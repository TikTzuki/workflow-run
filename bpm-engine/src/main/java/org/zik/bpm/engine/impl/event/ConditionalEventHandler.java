// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.event;

import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.bpmn.behavior.ConditionalEventBehavior;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.core.variable.event.VariableEvent;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;

public class ConditionalEventHandler implements EventHandler
{
    @Override
    public String getEventHandlerType() {
        return EventType.CONDITONAL.name();
    }
    
    @Override
    public void handleEvent(final EventSubscriptionEntity eventSubscription, final Object payload, final Object localPayload, final String businessKey, final CommandContext commandContext) {
        if (payload != null && !(payload instanceof VariableEvent)) {
            throw new ProcessEngineException("Payload have to be " + VariableEvent.class.getName() + ", to evaluate condition.");
        }
        final VariableEvent variableEvent = (VariableEvent)payload;
        final ActivityImpl activity = eventSubscription.getActivity();
        final ActivityBehavior activityBehavior = activity.getActivityBehavior();
        if (activityBehavior instanceof ConditionalEventBehavior) {
            final ConditionalEventBehavior conditionalBehavior = (ConditionalEventBehavior)activityBehavior;
            conditionalBehavior.leaveOnSatisfiedCondition(eventSubscription, variableEvent);
            return;
        }
        throw new ProcessEngineException("Conditional Event has not correct behavior: " + activityBehavior);
    }
}
