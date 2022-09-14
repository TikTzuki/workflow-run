// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

import org.camunda.bpm.model.bpmn.instance.FlowElement;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;

public interface BpmnModelExecutionContext
{
    BpmnModelInstance getBpmnModelInstance();
    
    FlowElement getBpmnModelElementInstance();
}
