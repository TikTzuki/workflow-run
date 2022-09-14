// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.listener;

import org.zik.bpm.engine.delegate.DelegateVariableInstance;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateCaseVariableInstance;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.delegate.CaseVariableListener;

public class ExpressionCaseVariableListener implements CaseVariableListener
{
    protected Expression expression;
    
    public ExpressionCaseVariableListener(final Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public void notify(final DelegateCaseVariableInstance variableInstance) throws Exception {
        final DelegateCaseVariableInstanceImpl variableInstanceImpl = (DelegateCaseVariableInstanceImpl)variableInstance;
        this.expression.getValue(variableInstanceImpl.getScopeExecution());
    }
    
    public String getExpressionText() {
        return this.expression.getExpressionText();
    }
}
