// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParse;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;
import org.zik.bpm.engine.impl.util.xml.Element;

public interface FormHandler
{
    void parseConfiguration(final Element p0, final DeploymentEntity p1, final ProcessDefinitionEntity p2, final BpmnParse p3);
    
    void submitFormVariables(final VariableMap p0, final VariableScope p1);
}
