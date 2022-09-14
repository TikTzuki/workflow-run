// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class SubmitFormVariablesInvocation extends DelegateInvocation
{
    protected FormHandler formHandler;
    protected VariableMap properties;
    protected VariableScope variableScope;
    
    public SubmitFormVariablesInvocation(final FormHandler formHandler, final VariableMap properties, final VariableScope variableScope) {
        super(null, null);
        this.formHandler = formHandler;
        this.properties = properties;
        this.variableScope = variableScope;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.formHandler.submitFormVariables(this.properties, this.variableScope);
    }
}
