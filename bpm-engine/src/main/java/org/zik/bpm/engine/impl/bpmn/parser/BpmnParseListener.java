// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import java.util.List;
import org.zik.bpm.engine.impl.pvm.process.TransitionImpl;
import org.zik.bpm.engine.impl.variable.VariableDeclaration;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.util.xml.Element;

public interface BpmnParseListener
{
    void parseProcess(final Element p0, final ProcessDefinitionEntity p1);
    
    void parseStartEvent(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseExclusiveGateway(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseInclusiveGateway(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseParallelGateway(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseScriptTask(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseServiceTask(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseBusinessRuleTask(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseTask(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseManualTask(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseUserTask(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseEndEvent(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseBoundaryTimerEventDefinition(final Element p0, final boolean p1, final ActivityImpl p2);
    
    void parseBoundaryErrorEventDefinition(final Element p0, final boolean p1, final ActivityImpl p2, final ActivityImpl p3);
    
    void parseSubProcess(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseCallActivity(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseProperty(final Element p0, final VariableDeclaration p1, final ActivityImpl p2);
    
    void parseSequenceFlow(final Element p0, final ScopeImpl p1, final TransitionImpl p2);
    
    void parseSendTask(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseMultiInstanceLoopCharacteristics(final Element p0, final Element p1, final ActivityImpl p2);
    
    void parseIntermediateTimerEventDefinition(final Element p0, final ActivityImpl p1);
    
    void parseRootElement(final Element p0, final List<ProcessDefinitionEntity> p1);
    
    void parseReceiveTask(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseIntermediateSignalCatchEventDefinition(final Element p0, final ActivityImpl p1);
    
    void parseIntermediateMessageCatchEventDefinition(final Element p0, final ActivityImpl p1);
    
    void parseBoundarySignalEventDefinition(final Element p0, final boolean p1, final ActivityImpl p2);
    
    void parseEventBasedGateway(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseTransaction(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseCompensateEventDefinition(final Element p0, final ActivityImpl p1);
    
    void parseIntermediateThrowEvent(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseIntermediateCatchEvent(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseBoundaryEvent(final Element p0, final ScopeImpl p1, final ActivityImpl p2);
    
    void parseBoundaryMessageEventDefinition(final Element p0, final boolean p1, final ActivityImpl p2);
    
    void parseBoundaryEscalationEventDefinition(final Element p0, final boolean p1, final ActivityImpl p2);
    
    void parseBoundaryConditionalEventDefinition(final Element p0, final boolean p1, final ActivityImpl p2);
    
    void parseIntermediateConditionalEventDefinition(final Element p0, final ActivityImpl p1);
    
    void parseConditionalStartEventForEventSubprocess(final Element p0, final ActivityImpl p1, final boolean p2);
}
