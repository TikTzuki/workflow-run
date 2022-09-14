// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.listener;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.delegate.CaseExecutionListener;

public class ExpressionCaseExecutionListener implements CaseExecutionListener
{
    protected Expression expression;
    
    public ExpressionCaseExecutionListener(final Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public void notify(final DelegateCaseExecution caseExecution) throws Exception {
        this.expression.getValue(caseExecution);
    }
    
    public String getExpressionText() {
        return this.expression.getExpressionText();
    }
}
