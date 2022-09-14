// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.migration.validation.activity;

import java.util.Arrays;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.zik.bpm.engine.impl.bpmn.behavior.EventSubProcessStartEventActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.behavior.BoundaryEventActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.List;

public class SupportedPassiveEventTriggerActivityValidator implements MigrationActivityValidator
{
    public static SupportedPassiveEventTriggerActivityValidator INSTANCE;
    public static final List<String> supportedTypes;
    
    @Override
    public boolean valid(final ActivityImpl activity) {
        return activity != null && (!this.isPassivelyWaitingEvent(activity) || this.isSupportedEventType(activity));
    }
    
    public boolean isPassivelyWaitingEvent(final ActivityImpl activity) {
        return activity.getActivityBehavior() instanceof BoundaryEventActivityBehavior || activity.getActivityBehavior() instanceof EventSubProcessStartEventActivityBehavior;
    }
    
    public boolean isSupportedEventType(final ActivityImpl activity) {
        return SupportedPassiveEventTriggerActivityValidator.supportedTypes.contains(activity.getProperties().get(BpmnProperties.TYPE));
    }
    
    static {
        SupportedPassiveEventTriggerActivityValidator.INSTANCE = new SupportedPassiveEventTriggerActivityValidator();
        supportedTypes = Arrays.asList("boundaryMessage", "boundarySignal", "boundaryTimer", "compensationBoundaryCatch", "boundaryConditional", "messageStartEvent", "signalStartEvent", "startTimerEvent", "compensationStartEvent", "conditionalStartEvent");
    }
}
