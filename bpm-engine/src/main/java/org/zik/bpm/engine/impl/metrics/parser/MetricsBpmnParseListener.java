// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics.parser;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.util.xml.Element;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateListener;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.bpmn.parser.AbstractBpmnParseListener;

public class MetricsBpmnParseListener extends AbstractBpmnParseListener
{
    public static MetricsExecutionListener ROOT_PROCESS_INSTANCE_START_COUNTER;
    public static MetricsExecutionListener ACTIVITY_INSTANCE_START_COUNTER;
    public static MetricsExecutionListener ACTIVITY_INSTANCE_END_COUNTER;
    
    protected void addListeners(final ActivityImpl activity) {
        activity.addBuiltInListener("start", MetricsBpmnParseListener.ACTIVITY_INSTANCE_START_COUNTER);
        activity.addBuiltInListener("end", MetricsBpmnParseListener.ACTIVITY_INSTANCE_END_COUNTER);
    }
    
    @Override
    public void parseProcess(final Element processElement, final ProcessDefinitionEntity processDefinition) {
        processDefinition.addBuiltInListener("start", MetricsBpmnParseListener.ROOT_PROCESS_INSTANCE_START_COUNTER);
    }
    
    @Override
    public void parseStartEvent(final Element startEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseExclusiveGateway(final Element exclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseInclusiveGateway(final Element inclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseParallelGateway(final Element parallelGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseScriptTask(final Element scriptTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseServiceTask(final Element serviceTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseBusinessRuleTask(final Element businessRuleTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseTask(final Element taskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseManualTask(final Element manualTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseUserTask(final Element userTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseEndEvent(final Element endEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseSubProcess(final Element subProcessElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseCallActivity(final Element callActivityElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseSendTask(final Element sendTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseReceiveTask(final Element receiveTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseEventBasedGateway(final Element eventBasedGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseTransaction(final Element transactionElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseIntermediateThrowEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseIntermediateCatchEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseBoundaryEvent(final Element boundaryEventElement, final ScopeImpl scopeElement, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    @Override
    public void parseMultiInstanceLoopCharacteristics(final Element activityElement, final Element multiInstanceLoopCharacteristicsElement, final ActivityImpl activity) {
        this.addListeners(activity);
    }
    
    static {
        MetricsBpmnParseListener.ROOT_PROCESS_INSTANCE_START_COUNTER = new MetricsExecutionListener("root-process-instance-start", delegateExecution -> delegateExecution.getId().equals(delegateExecution.getRootProcessInstanceId()));
        MetricsBpmnParseListener.ACTIVITY_INSTANCE_START_COUNTER = new MetricsExecutionListener("activity-instance-start");
        MetricsBpmnParseListener.ACTIVITY_INSTANCE_END_COUNTER = new MetricsExecutionListener("activity-instance-end");
    }
}
