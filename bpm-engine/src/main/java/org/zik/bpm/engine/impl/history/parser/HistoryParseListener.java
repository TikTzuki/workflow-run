// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.parser;

import org.zik.bpm.engine.impl.context.Context;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.variable.VariableDeclaration;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import org.zik.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateListener;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.util.xml.Element;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.delegate.TaskListener;
import org.zik.bpm.engine.delegate.ExecutionListener;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParseListener;

public class HistoryParseListener implements BpmnParseListener
{
    protected ExecutionListener PROCESS_INSTANCE_START_LISTENER;
    protected ExecutionListener PROCESS_INSTANCE_END_LISTENER;
    protected ExecutionListener ACTIVITY_INSTANCE_START_LISTENER;
    protected ExecutionListener ACTIVITY_INSTANCE_END_LISTENER;
    protected TaskListener USER_TASK_ASSIGNMENT_HANDLER;
    protected TaskListener USER_TASK_ID_HANDLER;
    protected HistoryLevel historyLevel;
    
    public HistoryParseListener(final HistoryEventProducer historyEventProducer) {
        this.initExecutionListeners(historyEventProducer);
    }
    
    protected void initExecutionListeners(final HistoryEventProducer historyEventProducer) {
        this.PROCESS_INSTANCE_START_LISTENER = new ProcessInstanceStartListener(historyEventProducer);
        this.PROCESS_INSTANCE_END_LISTENER = new ProcessInstanceEndListener(historyEventProducer);
        this.ACTIVITY_INSTANCE_START_LISTENER = new ActivityInstanceStartListener(historyEventProducer);
        this.ACTIVITY_INSTANCE_END_LISTENER = new ActivityInstanceEndListener(historyEventProducer);
        this.USER_TASK_ASSIGNMENT_HANDLER = new ActivityInstanceUpdateListener(historyEventProducer);
        this.USER_TASK_ID_HANDLER = this.USER_TASK_ASSIGNMENT_HANDLER;
    }
    
    @Override
    public void parseProcess(final Element processElement, final ProcessDefinitionEntity processDefinition) {
        this.ensureHistoryLevelInitialized();
        if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.PROCESS_INSTANCE_END, null)) {
            processDefinition.addBuiltInListener("end", this.PROCESS_INSTANCE_END_LISTENER);
        }
    }
    
    @Override
    public void parseExclusiveGateway(final Element exclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseInclusiveGateway(final Element inclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseCallActivity(final Element callActivityElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseManualTask(final Element manualTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseReceiveTask(final Element receiveTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseScriptTask(final Element scriptTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseTask(final Element taskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseUserTask(final Element userTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.ensureHistoryLevelInitialized();
        this.addActivityHandlers(activity);
        if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.TASK_INSTANCE_CREATE, null)) {
            final TaskDefinition taskDefinition = ((UserTaskActivityBehavior)activity.getActivityBehavior()).getTaskDefinition();
            taskDefinition.addBuiltInTaskListener("assignment", this.USER_TASK_ASSIGNMENT_HANDLER);
            taskDefinition.addBuiltInTaskListener("create", this.USER_TASK_ID_HANDLER);
        }
    }
    
    @Override
    public void parseServiceTask(final Element serviceTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseBusinessRuleTask(final Element businessRuleTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseSubProcess(final Element subProcessElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseStartEvent(final Element startEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseSendTask(final Element sendTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseEndEvent(final Element endEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseParallelGateway(final Element parallelGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseBoundaryTimerEventDefinition(final Element timerEventDefinition, final boolean interrupting, final ActivityImpl timerActivity) {
    }
    
    @Override
    public void parseBoundaryErrorEventDefinition(final Element errorEventDefinition, final boolean interrupting, final ActivityImpl activity, final ActivityImpl nestedErrorEventActivity) {
    }
    
    @Override
    public void parseIntermediateTimerEventDefinition(final Element timerEventDefinition, final ActivityImpl timerActivity) {
    }
    
    @Override
    public void parseProperty(final Element propertyElement, final VariableDeclaration variableDeclaration, final ActivityImpl activity) {
    }
    
    @Override
    public void parseSequenceFlow(final Element sequenceFlowElement, final ScopeImpl scopeElement, final TransitionImpl transition) {
    }
    
    @Override
    public void parseRootElement(final Element rootElement, final List<ProcessDefinitionEntity> processDefinitions) {
    }
    
    @Override
    public void parseBoundarySignalEventDefinition(final Element signalEventDefinition, final boolean interrupting, final ActivityImpl signalActivity) {
    }
    
    @Override
    public void parseEventBasedGateway(final Element eventBasedGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseMultiInstanceLoopCharacteristics(final Element activityElement, final Element multiInstanceLoopCharacteristicsElement, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseIntermediateSignalCatchEventDefinition(final Element signalEventDefinition, final ActivityImpl signalActivity) {
    }
    
    @Override
    public void parseTransaction(final Element transactionElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseCompensateEventDefinition(final Element compensateEventDefinition, final ActivityImpl compensationActivity) {
    }
    
    @Override
    public void parseIntermediateThrowEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseIntermediateCatchEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        if (!activity.getProperty("type").equals("intermediateLinkCatch")) {
            this.addActivityHandlers(activity);
        }
    }
    
    @Override
    public void parseBoundaryEvent(final Element boundaryEventElement, final ScopeImpl scopeElement, final ActivityImpl activity) {
        this.addActivityHandlers(activity);
    }
    
    @Override
    public void parseIntermediateMessageCatchEventDefinition(final Element messageEventDefinition, final ActivityImpl nestedActivity) {
    }
    
    @Override
    public void parseBoundaryMessageEventDefinition(final Element element, final boolean interrupting, final ActivityImpl messageActivity) {
    }
    
    @Override
    public void parseBoundaryEscalationEventDefinition(final Element escalationEventDefinition, final boolean interrupting, final ActivityImpl boundaryEventActivity) {
    }
    
    @Override
    public void parseBoundaryConditionalEventDefinition(final Element element, final boolean interrupting, final ActivityImpl conditionalActivity) {
    }
    
    @Override
    public void parseIntermediateConditionalEventDefinition(final Element conditionalEventDefinition, final ActivityImpl conditionalActivity) {
    }
    
    @Override
    public void parseConditionalStartEventForEventSubprocess(final Element element, final ActivityImpl conditionalActivity, final boolean interrupting) {
    }
    
    protected void addActivityHandlers(final ActivityImpl activity) {
        this.ensureHistoryLevelInitialized();
        if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.ACTIVITY_INSTANCE_START, null)) {
            activity.addBuiltInListener("start", this.ACTIVITY_INSTANCE_START_LISTENER, 0);
        }
        if (this.historyLevel.isHistoryEventProduced(HistoryEventTypes.ACTIVITY_INSTANCE_END, null)) {
            activity.addBuiltInListener("end", this.ACTIVITY_INSTANCE_END_LISTENER);
        }
    }
    
    protected void ensureHistoryLevelInitialized() {
        if (this.historyLevel == null) {
            this.historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        }
    }
}
