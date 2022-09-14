// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnExceptionHandler;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;

public class ErrorEndEventActivityBehavior extends AbstractBpmnActivityBehavior
{
    protected String errorCode;
    private ParameterValueProvider errorMessageExpression;
    
    public ErrorEndEventActivityBehavior(final String errorCode, final ParameterValueProvider errorMessage) {
        this.errorCode = errorCode;
        this.errorMessageExpression = errorMessage;
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        final String errorMessageValue = (this.errorMessageExpression != null) ? ((String)this.errorMessageExpression.getValue(execution)) : null;
        BpmnExceptionHandler.propagateError(this.errorCode, errorMessageValue, null, execution);
    }
    
    public String getErrorCode() {
        return this.errorCode;
    }
    
    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }
    
    public ParameterValueProvider getErrorMessageExpression() {
        return this.errorMessageExpression;
    }
    
    public void setErrorMessageExpression(final ParameterValueProvider errorMessage) {
        this.errorMessageExpression = errorMessage;
    }
}
