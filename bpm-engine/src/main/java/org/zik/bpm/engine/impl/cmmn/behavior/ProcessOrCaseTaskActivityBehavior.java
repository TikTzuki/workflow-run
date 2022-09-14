// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.behavior;

import org.zik.bpm.engine.impl.core.model.BaseCallableElement;
import org.zik.bpm.engine.impl.core.model.CallableElement;
import org.zik.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import java.util.Map;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnActivityExecution;

public abstract class ProcessOrCaseTaskActivityBehavior extends CallingTaskActivityBehavior implements TransferVariablesActivityBehavior
{
    @Override
    protected void performStart(final CmmnActivityExecution execution) {
        final VariableMap variables = this.getInputVariables(execution);
        final String businessKey = this.getBusinessKey(execution);
        this.triggerCallableElement(execution, (Map<String, Object>)variables, businessKey);
        if (execution.isActive() && !this.isBlocking(execution)) {
            execution.complete();
        }
    }
    
    @Override
    public void transferVariables(final VariableScope sourceScope, final CmmnActivityExecution caseExecution) {
        final VariableMap variables = this.getOutputVariables(sourceScope);
        caseExecution.setVariables((Map<String, ?>)variables);
    }
    
    @Override
    public CallableElement getCallableElement() {
        return (CallableElement)this.callableElement;
    }
    
    protected String getBusinessKey(final CmmnActivityExecution execution) {
        return this.getCallableElement().getBusinessKey(execution);
    }
    
    protected VariableMap getInputVariables(final CmmnActivityExecution execution) {
        return this.getCallableElement().getInputVariables(execution);
    }
    
    protected VariableMap getOutputVariables(final VariableScope variableScope) {
        return this.getCallableElement().getOutputVariables(variableScope);
    }
    
    protected abstract void triggerCallableElement(final CmmnActivityExecution p0, final Map<String, Object> p1, final String p2);
}
