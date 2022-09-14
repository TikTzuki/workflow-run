// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.listener;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.delegate.ExecutionListener;

public class ExpressionExecutionListener implements ExecutionListener
{
    protected Expression expression;
    
    public ExpressionExecutionListener(final Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public void notify(final DelegateExecution execution) throws Exception {
        this.expression.getValue(execution);
    }
    
    public String getExpressionText() {
        return this.expression.getExpressionText();
    }
}
