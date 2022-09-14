// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.application.impl.event;

import java.util.List;
import org.zik.bpm.engine.impl.variable.VariableDeclaration;
import org.zik.bpm.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.util.xml.Element;
import org.zik.bpm.engine.impl.task.TaskDefinition;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.delegate.TaskListener;
import org.zik.bpm.engine.delegate.ExecutionListener;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParseListener;

public class ProcessApplicationEventParseListener implements BpmnParseListener
{
    public static final ExecutionListener EXECUTION_LISTENER;
    public static final TaskListener TASK_LISTENER;
    
    protected void addEndEventListener(final ScopeImpl activity) {
        activity.addExecutionListener("end", ProcessApplicationEventParseListener.EXECUTION_LISTENER);
    }
    
    protected void addStartEventListener(final ScopeImpl activity) {
        activity.addExecutionListener("start", ProcessApplicationEventParseListener.EXECUTION_LISTENER);
    }
    
    protected void addTakeEventListener(final TransitionImpl transition) {
        transition.addExecutionListener(ProcessApplicationEventParseListener.EXECUTION_LISTENER);
    }
    
    protected void addTaskAssignmentListeners(final TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener("assignment", ProcessApplicationEventParseListener.TASK_LISTENER);
    }
    
    protected void addTaskCreateListeners(final TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener("create", ProcessApplicationEventParseListener.TASK_LISTENER);
    }
    
    protected void addTaskCompleteListeners(final TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener("complete", ProcessApplicationEventParseListener.TASK_LISTENER);
    }
    
    protected void addTaskUpdateListeners(final TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener("update", ProcessApplicationEventParseListener.TASK_LISTENER);
    }
    
    protected void addTaskDeleteListeners(final TaskDefinition taskDefinition) {
        taskDefinition.addTaskListener("delete", ProcessApplicationEventParseListener.TASK_LISTENER);
    }
    
    @Override
    public void parseProcess(final Element processElement, final ProcessDefinitionEntity processDefinition) {
        this.addStartEventListener(processDefinition);
        this.addEndEventListener(processDefinition);
    }
    
    @Override
    public void parseStartEvent(final Element startEventElement, final ScopeImpl scope, final ActivityImpl startEventActivity) {
        this.addStartEventListener(startEventActivity);
        this.addEndEventListener(startEventActivity);
    }
    
    @Override
    public void parseExclusiveGateway(final Element exclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseInclusiveGateway(final Element inclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseParallelGateway(final Element parallelGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseScriptTask(final Element scriptTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseServiceTask(final Element serviceTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseBusinessRuleTask(final Element businessRuleTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseTask(final Element taskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseManualTask(final Element manualTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseUserTask(final Element userTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
        final UserTaskActivityBehavior activityBehavior = (UserTaskActivityBehavior)activity.getActivityBehavior();
        final TaskDefinition taskDefinition = activityBehavior.getTaskDefinition();
        this.addTaskCreateListeners(taskDefinition);
        this.addTaskAssignmentListeners(taskDefinition);
        this.addTaskCompleteListeners(taskDefinition);
        this.addTaskUpdateListeners(taskDefinition);
        this.addTaskDeleteListeners(taskDefinition);
    }
    
    @Override
    public void parseEndEvent(final Element endEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseBoundaryTimerEventDefinition(final Element timerEventDefinition, final boolean interrupting, final ActivityImpl timerActivity) {
    }
    
    @Override
    public void parseBoundaryErrorEventDefinition(final Element errorEventDefinition, final boolean interrupting, final ActivityImpl activity, final ActivityImpl nestedErrorEventActivity) {
    }
    
    @Override
    public void parseSubProcess(final Element subProcessElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseCallActivity(final Element callActivityElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseProperty(final Element propertyElement, final VariableDeclaration variableDeclaration, final ActivityImpl activity) {
    }
    
    @Override
    public void parseSequenceFlow(final Element sequenceFlowElement, final ScopeImpl scopeElement, final TransitionImpl transition) {
        this.addTakeEventListener(transition);
    }
    
    @Override
    public void parseSendTask(final Element sendTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseMultiInstanceLoopCharacteristics(final Element activityElement, final Element multiInstanceLoopCharacteristicsElement, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseRootElement(final Element rootElement, final List<ProcessDefinitionEntity> processDefinitions) {
    }
    
    @Override
    public void parseIntermediateTimerEventDefinition(final Element timerEventDefinition, final ActivityImpl timerActivity) {
    }
    
    @Override
    public void parseReceiveTask(final Element receiveTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseIntermediateSignalCatchEventDefinition(final Element signalEventDefinition, final ActivityImpl signalActivity) {
    }
    
    @Override
    public void parseBoundarySignalEventDefinition(final Element signalEventDefinition, final boolean interrupting, final ActivityImpl signalActivity) {
    }
    
    @Override
    public void parseEventBasedGateway(final Element eventBasedGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseTransaction(final Element transactionElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseCompensateEventDefinition(final Element compensateEventDefinition, final ActivityImpl compensationActivity) {
    }
    
    @Override
    public void parseIntermediateThrowEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseIntermediateCatchEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
    }
    
    @Override
    public void parseBoundaryEvent(final Element boundaryEventElement, final ScopeImpl scopeElement, final ActivityImpl activity) {
        this.addStartEventListener(activity);
        this.addEndEventListener(activity);
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
    
    static {
        EXECUTION_LISTENER = new ProcessApplicationEventListenerDelegate();
        TASK_LISTENER = new ProcessApplicationEventListenerDelegate();
    }
}
