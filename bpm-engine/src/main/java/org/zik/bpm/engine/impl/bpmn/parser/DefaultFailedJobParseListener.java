// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.zik.bpm.engine.impl.util.ParseUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.util.xml.Element;
import org.zik.bpm.engine.impl.core.model.PropertyKey;
import org.zik.bpm.engine.impl.util.xml.Namespace;

public class DefaultFailedJobParseListener extends AbstractBpmnParseListener
{
    protected static final String TYPE = "type";
    protected static final String START_TIMER_EVENT = "startTimerEvent";
    protected static final String BOUNDARY_TIMER = "boundaryTimer";
    protected static final String INTERMEDIATE_SIGNAL_THROW = "intermediateSignalThrow";
    protected static final String INTERMEDIATE_TIMER = "intermediateTimer";
    protected static final String SIGNAL_EVENT_DEFINITION = "signalEventDefinition";
    protected static final String MULTI_INSTANCE_LOOP_CHARACTERISTICS = "multiInstanceLoopCharacteristics";
    protected static final String EXTENSION_ELEMENTS = "extensionElements";
    protected static final String FAILED_JOB_RETRY_TIME_CYCLE = "failedJobRetryTimeCycle";
    @Deprecated
    public static final Namespace FOX_ENGINE_NS;
    public static final PropertyKey<FailedJobRetryConfiguration> FAILED_JOB_CONFIGURATION;
    
    @Override
    public void parseStartEvent(final Element startEventElement, final ScopeImpl scope, final ActivityImpl startEventActivity) {
        final String type = startEventActivity.getProperties().get(BpmnProperties.TYPE);
        if ((type != null && type.equals("startTimerEvent")) || this.isAsync(startEventActivity)) {
            this.setFailedJobRetryTimeCycleValue(startEventElement, startEventActivity);
        }
    }
    
    @Override
    public void parseBoundaryEvent(final Element boundaryEventElement, final ScopeImpl scopeElement, final ActivityImpl nestedActivity) {
        final String type = nestedActivity.getProperties().get(BpmnProperties.TYPE);
        if ((type != null && type.equals("boundaryTimer")) || this.isAsync(nestedActivity)) {
            this.setFailedJobRetryTimeCycleValue(boundaryEventElement, nestedActivity);
        }
    }
    
    @Override
    public void parseIntermediateThrowEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        final String type = activity.getProperties().get(BpmnProperties.TYPE);
        if (type != null) {
            this.setFailedJobRetryTimeCycleValue(intermediateEventElement, activity);
        }
    }
    
    @Override
    public void parseIntermediateCatchEvent(final Element intermediateEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        final String type = activity.getProperties().get(BpmnProperties.TYPE);
        if ((type != null && type.equals("intermediateTimer")) || this.isAsync(activity)) {
            this.setFailedJobRetryTimeCycleValue(intermediateEventElement, activity);
        }
    }
    
    @Override
    public void parseEndEvent(final Element endEventElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(endEventElement, activity);
    }
    
    @Override
    public void parseExclusiveGateway(final Element exclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(exclusiveGwElement, activity);
    }
    
    @Override
    public void parseInclusiveGateway(final Element exclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(exclusiveGwElement, activity);
    }
    
    @Override
    public void parseEventBasedGateway(final Element exclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(exclusiveGwElement, activity);
    }
    
    @Override
    public void parseParallelGateway(final Element exclusiveGwElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(exclusiveGwElement, activity);
    }
    
    @Override
    public void parseScriptTask(final Element scriptTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(scriptTaskElement, activity);
    }
    
    @Override
    public void parseServiceTask(final Element serviceTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(serviceTaskElement, activity);
    }
    
    @Override
    public void parseBusinessRuleTask(final Element businessRuleTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(businessRuleTaskElement, activity);
    }
    
    @Override
    public void parseTask(final Element taskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(taskElement, activity);
    }
    
    @Override
    public void parseUserTask(final Element userTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(userTaskElement, activity);
    }
    
    @Override
    public void parseCallActivity(final Element callActivityElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(callActivityElement, activity);
    }
    
    @Override
    public void parseReceiveTask(final Element receiveTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(receiveTaskElement, activity);
    }
    
    @Override
    public void parseSendTask(final Element sendTaskElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(sendTaskElement, activity);
    }
    
    @Override
    public void parseSubProcess(final Element subProcessElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(subProcessElement, activity);
    }
    
    @Override
    public void parseTransaction(final Element transactionElement, final ScopeImpl scope, final ActivityImpl activity) {
        this.parseActivity(transactionElement, activity);
    }
    
    protected boolean isAsync(final ActivityImpl activity) {
        return activity.isAsyncBefore() || activity.isAsyncAfter();
    }
    
    protected void parseActivity(final Element element, final ActivityImpl activity) {
        if (this.isMultiInstance(activity)) {
            final ActivityImpl miBody = activity.getParentFlowScopeActivity();
            if (this.isAsync(miBody)) {
                this.setFailedJobRetryTimeCycleValue(element, miBody);
            }
            if (this.isAsync(activity)) {
                final Element multiInstanceLoopCharacteristics = element.element("multiInstanceLoopCharacteristics");
                this.setFailedJobRetryTimeCycleValue(multiInstanceLoopCharacteristics, activity);
            }
        }
        else if (this.isAsync(activity)) {
            this.setFailedJobRetryTimeCycleValue(element, activity);
        }
    }
    
    protected void setFailedJobRetryTimeCycleValue(final Element element, final ActivityImpl activity) {
        String failedJobRetryTimeCycleConfiguration = null;
        final Element extensionElements = element.element("extensionElements");
        if (extensionElements != null) {
            Element failedJobRetryTimeCycleElement = extensionElements.elementNS(DefaultFailedJobParseListener.FOX_ENGINE_NS, "failedJobRetryTimeCycle");
            if (failedJobRetryTimeCycleElement == null) {
                failedJobRetryTimeCycleElement = extensionElements.elementNS(BpmnParse.CAMUNDA_BPMN_EXTENSIONS_NS, "failedJobRetryTimeCycle");
            }
            if (failedJobRetryTimeCycleElement != null) {
                failedJobRetryTimeCycleConfiguration = failedJobRetryTimeCycleElement.getText();
            }
        }
        if (failedJobRetryTimeCycleConfiguration == null || failedJobRetryTimeCycleConfiguration.isEmpty()) {
            failedJobRetryTimeCycleConfiguration = Context.getProcessEngineConfiguration().getFailedJobRetryTimeCycle();
        }
        if (failedJobRetryTimeCycleConfiguration != null) {
            final FailedJobRetryConfiguration configuration = ParseUtil.parseRetryIntervals(failedJobRetryTimeCycleConfiguration);
            activity.getProperties().set(DefaultFailedJobParseListener.FAILED_JOB_CONFIGURATION, configuration);
        }
    }
    
    protected boolean isMultiInstance(final ActivityImpl activity) {
        final ActivityImpl parent = activity.getParentFlowScopeActivity();
        return parent != null && parent.getActivityBehavior() instanceof MultiInstanceActivityBehavior;
    }
    
    static {
        FOX_ENGINE_NS = new Namespace("http://www.camunda.com/fox");
        FAILED_JOB_CONFIGURATION = new PropertyKey<FailedJobRetryConfiguration>("FAILED_JOB_CONFIGURATION");
    }
}
