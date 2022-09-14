// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.helper;

import org.zik.bpm.engine.impl.bpmn.parser.CamundaErrorEventDefinition;
import org.zik.bpm.engine.impl.bpmn.parser.ConditionalEventDefinition;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import java.util.Map;
import org.zik.bpm.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.zik.bpm.engine.impl.core.model.PropertyMapKey;
import org.zik.bpm.engine.impl.bpmn.parser.ErrorEventDefinition;
import org.zik.bpm.engine.impl.bpmn.parser.EscalationEventDefinition;
import org.zik.bpm.engine.impl.core.model.PropertyListKey;
import org.zik.bpm.engine.impl.core.model.PropertyKey;

public class BpmnProperties
{
    public static final PropertyKey<String> TYPE;
    public static final PropertyListKey<EscalationEventDefinition> ESCALATION_EVENT_DEFINITIONS;
    public static final PropertyListKey<ErrorEventDefinition> ERROR_EVENT_DEFINITIONS;
    public static final PropertyMapKey<String, TimerDeclarationImpl> TIMER_DECLARATIONS;
    public static final PropertyMapKey<String, Map<String, TimerDeclarationImpl>> TIMEOUT_LISTENER_DECLARATIONS;
    public static final PropertyMapKey<String, EventSubscriptionDeclaration> EVENT_SUBSCRIPTION_DECLARATIONS;
    public static final PropertyKey<ActivityImpl> COMPENSATION_BOUNDARY_EVENT;
    public static final PropertyKey<ActivityImpl> INITIAL_ACTIVITY;
    public static final PropertyKey<Boolean> TRIGGERED_BY_EVENT;
    public static final PropertyKey<Boolean> HAS_CONDITIONAL_EVENTS;
    public static final PropertyKey<ConditionalEventDefinition> CONDITIONAL_EVENT_DEFINITION;
    public static final PropertyKey<Map<String, String>> EXTENSION_PROPERTIES;
    public static final PropertyListKey<CamundaErrorEventDefinition> CAMUNDA_ERROR_EVENT_DEFINITION;
    
    static {
        TYPE = new PropertyKey<String>("type");
        ESCALATION_EVENT_DEFINITIONS = new PropertyListKey<EscalationEventDefinition>("escalationEventDefinitions");
        ERROR_EVENT_DEFINITIONS = new PropertyListKey<ErrorEventDefinition>("errorEventDefinitions");
        TIMER_DECLARATIONS = new PropertyMapKey<String, TimerDeclarationImpl>("timerDeclarations", false);
        TIMEOUT_LISTENER_DECLARATIONS = new PropertyMapKey<String, Map<String, TimerDeclarationImpl>>("timerListenerDeclarations", false);
        EVENT_SUBSCRIPTION_DECLARATIONS = new PropertyMapKey<String, EventSubscriptionDeclaration>("eventDefinitions", false);
        COMPENSATION_BOUNDARY_EVENT = new PropertyKey<ActivityImpl>("compensationBoundaryEvent");
        INITIAL_ACTIVITY = new PropertyKey<ActivityImpl>("initial");
        TRIGGERED_BY_EVENT = new PropertyKey<Boolean>("triggeredByEvent");
        HAS_CONDITIONAL_EVENTS = new PropertyKey<Boolean>("hasConditionalEvents");
        CONDITIONAL_EVENT_DEFINITION = new PropertyKey<ConditionalEventDefinition>("conditionalEventDefinition");
        EXTENSION_PROPERTIES = new PropertyKey<Map<String, String>>("extensionProperties");
        CAMUNDA_ERROR_EVENT_DEFINITION = new PropertyListKey<CamundaErrorEventDefinition>("camundaErrorEventDefinition");
    }
}
