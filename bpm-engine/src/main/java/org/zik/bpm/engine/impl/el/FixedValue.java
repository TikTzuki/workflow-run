// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.Expression;

public class FixedValue implements Expression
{
    private Object value;
    
    public FixedValue(final Object value) {
        this.value = value;
    }
    
    @Override
    public Object getValue(final VariableScope variableScope) {
        return this.value;
    }
    
    public Object getValue(final VariableScope variableScope, final BaseDelegateExecution contextExecution) {
        return this.getValue(variableScope);
    }
    
    @Override
    public void setValue(final Object value, final VariableScope variableScope) {
        throw new ProcessEngineException("Cannot change fixed value");
    }
    
    @Override
    public String getExpressionText() {
        return this.value.toString();
    }
    
    @Override
    public boolean isLiteralText() {
        return true;
    }
}
