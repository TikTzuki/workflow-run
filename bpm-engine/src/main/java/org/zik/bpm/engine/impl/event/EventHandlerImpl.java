// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.event;

import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.bpmn.behavior.EventSubProcessStartEventActivityBehavior;
import java.util.Map;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionEntity;

public class EventHandlerImpl implements EventHandler
{
    private final EventType eventType;
    
    public EventHandlerImpl(final EventType eventType) {
        this.eventType = eventType;
    }
    
    public void handleIntermediateEvent(final EventSubscriptionEntity eventSubscription, final Object payload, final Object localPayload, final CommandContext commandContext) {
        final PvmExecutionImpl execution = eventSubscription.getExecution();
        ActivityImpl activity = eventSubscription.getActivity();
        EnsureUtil.ensureNotNull("Error while sending signal for event subscription '" + eventSubscription.getId() + "': no activity associated with event subscription", "activity", activity);
        if (payload instanceof Map) {
            execution.setVariables((Map<String, ?>)payload);
        }
        if (localPayload instanceof Map) {
            execution.setVariablesLocal((Map<String, ?>)localPayload);
        }
        if (activity.equals(execution.getActivity())) {
            execution.signal("signal", null);
        }
        else {
            if (activity.getActivityBehavior() instanceof EventSubProcessStartEventActivityBehavior) {
                activity = (ActivityImpl)activity.getFlowScope();
            }
            execution.executeEventHandlerActivity(activity);
        }
    }
    
    @Override
    public void handleEvent(final EventSubscriptionEntity eventSubscription, final Object payload, final Object localPayload, final String businessKey, final CommandContext commandContext) {
        this.handleIntermediateEvent(eventSubscription, payload, localPayload, commandContext);
    }
    
    @Override
    public String getEventHandlerType() {
        return this.eventType.name();
    }
}
