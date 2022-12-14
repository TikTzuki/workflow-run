// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

public final class ActivityTypes
{
    public static final String MULTI_INSTANCE_BODY = "multiInstanceBody";
    public static final String GATEWAY_EXCLUSIVE = "exclusiveGateway";
    public static final String GATEWAY_INCLUSIVE = "inclusiveGateway";
    public static final String GATEWAY_PARALLEL = "parallelGateway";
    public static final String GATEWAY_COMPLEX = "complexGateway";
    public static final String GATEWAY_EVENT_BASED = "eventBasedGateway";
    public static final String TASK = "task";
    public static final String TASK_SCRIPT = "scriptTask";
    public static final String TASK_SERVICE = "serviceTask";
    public static final String TASK_BUSINESS_RULE = "businessRuleTask";
    public static final String TASK_MANUAL_TASK = "manualTask";
    public static final String TASK_USER_TASK = "userTask";
    public static final String TASK_SEND_TASK = "sendTask";
    public static final String TASK_RECEIVE_TASK = "receiveTask";
    public static final String SUB_PROCESS = "subProcess";
    public static final String SUB_PROCESS_AD_HOC = "adHocSubProcess";
    public static final String CALL_ACTIVITY = "callActivity";
    public static final String TRANSACTION = "transaction";
    public static final String BOUNDARY_TIMER = "boundaryTimer";
    public static final String BOUNDARY_MESSAGE = "boundaryMessage";
    public static final String BOUNDARY_SIGNAL = "boundarySignal";
    public static final String BOUNDARY_COMPENSATION = "compensationBoundaryCatch";
    public static final String BOUNDARY_ERROR = "boundaryError";
    public static final String BOUNDARY_ESCALATION = "boundaryEscalation";
    public static final String BOUNDARY_CANCEL = "cancelBoundaryCatch";
    public static final String BOUNDARY_CONDITIONAL = "boundaryConditional";
    public static final String START_EVENT = "startEvent";
    public static final String START_EVENT_TIMER = "startTimerEvent";
    public static final String START_EVENT_MESSAGE = "messageStartEvent";
    public static final String START_EVENT_SIGNAL = "signalStartEvent";
    public static final String START_EVENT_ESCALATION = "escalationStartEvent";
    public static final String START_EVENT_COMPENSATION = "compensationStartEvent";
    public static final String START_EVENT_ERROR = "errorStartEvent";
    public static final String START_EVENT_CONDITIONAL = "conditionalStartEvent";
    public static final String INTERMEDIATE_EVENT_CATCH = "intermediateCatchEvent";
    public static final String INTERMEDIATE_EVENT_MESSAGE = "intermediateMessageCatch";
    public static final String INTERMEDIATE_EVENT_TIMER = "intermediateTimer";
    public static final String INTERMEDIATE_EVENT_LINK = "intermediateLinkCatch";
    public static final String INTERMEDIATE_EVENT_SIGNAL = "intermediateSignalCatch";
    public static final String INTERMEDIATE_EVENT_CONDITIONAL = "intermediateConditional";
    public static final String INTERMEDIATE_EVENT_THROW = "intermediateThrowEvent";
    public static final String INTERMEDIATE_EVENT_SIGNAL_THROW = "intermediateSignalThrow";
    public static final String INTERMEDIATE_EVENT_COMPENSATION_THROW = "intermediateCompensationThrowEvent";
    public static final String INTERMEDIATE_EVENT_MESSAGE_THROW = "intermediateMessageThrowEvent";
    public static final String INTERMEDIATE_EVENT_NONE_THROW = "intermediateNoneThrowEvent";
    public static final String INTERMEDIATE_EVENT_ESCALATION_THROW = "intermediateEscalationThrowEvent";
    public static final String END_EVENT_ERROR = "errorEndEvent";
    public static final String END_EVENT_CANCEL = "cancelEndEvent";
    public static final String END_EVENT_TERMINATE = "terminateEndEvent";
    public static final String END_EVENT_MESSAGE = "messageEndEvent";
    public static final String END_EVENT_SIGNAL = "signalEndEvent";
    public static final String END_EVENT_COMPENSATION = "compensationEndEvent";
    public static final String END_EVENT_ESCALATION = "escalationEndEvent";
    public static final String END_EVENT_NONE = "noneEndEvent";
    
    private ActivityTypes() {
    }
}
