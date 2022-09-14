// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.runtime;

import org.camunda.bpm.engine.variable.VariableMap;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.runtime.MessageCorrelationResultType;
import org.zik.bpm.engine.runtime.Execution;
import org.zik.bpm.engine.runtime.MessageCorrelationResultWithVariables;

public class MessageCorrelationResultImpl implements MessageCorrelationResultWithVariables
{
    protected final Execution execution;
    protected final MessageCorrelationResultType resultType;
    protected ProcessInstance processInstance;
    protected VariableMap variables;
    
    public MessageCorrelationResultImpl(final CorrelationHandlerResult handlerResult) {
        this.execution = handlerResult.getExecution();
        this.resultType = handlerResult.getResultType();
    }
    
    @Override
    public Execution getExecution() {
        return this.execution;
    }
    
    @Override
    public ProcessInstance getProcessInstance() {
        return this.processInstance;
    }
    
    public void setProcessInstance(final ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }
    
    @Override
    public MessageCorrelationResultType getResultType() {
        return this.resultType;
    }
    
    @Override
    public VariableMap getVariables() {
        return this.variables;
    }
    
    public void setVariables(final VariableMap variables) {
        this.variables = variables;
    }
}
