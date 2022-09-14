// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.task.listener;

import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.delegate.TaskListener;

public class ExpressionTaskListener implements TaskListener
{
    protected Expression expression;
    
    public ExpressionTaskListener(final Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public void notify(final DelegateTask delegateTask) {
        this.expression.getValue(delegateTask);
    }
    
    public String getExpressionText() {
        return this.expression.getExpressionText();
    }
}
