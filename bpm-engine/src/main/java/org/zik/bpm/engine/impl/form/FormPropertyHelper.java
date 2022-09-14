// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form;

import org.zik.bpm.engine.impl.form.handler.StartFormHandler;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.camunda.bpm.engine.variable.VariableMap;

public class FormPropertyHelper
{
    public static void initFormPropertiesOnScope(final VariableMap variables, final PvmExecutionImpl execution) {
        final ProcessDefinitionEntity pd = (ProcessDefinitionEntity)execution.getProcessDefinition();
        final StartFormHandler startFormHandler = pd.getStartFormHandler();
        startFormHandler.submitFormVariables(variables, execution);
    }
}
