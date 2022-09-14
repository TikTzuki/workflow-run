// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.zik.bpm.engine.impl.delegate.ExpressionSetInvocation;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.ELException;
import org.zik.bpm.engine.impl.javax.el.MethodNotFoundException;
import org.zik.bpm.engine.impl.javax.el.PropertyNotFoundException;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.delegate.ExpressionGetInvocation;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;

public class JuelExpression implements Expression
{
    protected String expressionText;
    protected ValueExpression valueExpression;
    protected ExpressionManager expressionManager;
    
    public JuelExpression(final ValueExpression valueExpression, final ExpressionManager expressionManager, final String expressionText) {
        this.valueExpression = valueExpression;
        this.expressionManager = expressionManager;
        this.expressionText = expressionText;
    }
    
    @Override
    public Object getValue(final VariableScope variableScope) {
        return this.getValue(variableScope, null);
    }
    
    @Override
    public Object getValue(final VariableScope variableScope, final BaseDelegateExecution contextExecution) {
        final ELContext elContext = this.expressionManager.getElContext(variableScope);
        try {
            final ExpressionGetInvocation invocation = new ExpressionGetInvocation(this.valueExpression, elContext, contextExecution);
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
            return invocation.getInvocationResult();
        }
        catch (PropertyNotFoundException pnfe) {
            throw new ProcessEngineException("Unknown property used in expression: " + this.expressionText + ". Cause: " + pnfe.getMessage(), pnfe);
        }
        catch (MethodNotFoundException mnfe) {
            throw new ProcessEngineException("Unknown method used in expression: " + this.expressionText + ". Cause: " + mnfe.getMessage(), mnfe);
        }
        catch (ELException ele) {
            throw new ProcessEngineException("Error while evaluating expression: " + this.expressionText + ". Cause: " + ele.getMessage(), ele);
        }
        catch (Exception e) {
            throw new ProcessEngineException("Error while evaluating expression: " + this.expressionText + ". Cause: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void setValue(final Object value, final VariableScope variableScope) {
        this.setValue(value, variableScope, null);
    }
    
    @Override
    public void setValue(final Object value, final VariableScope variableScope, final BaseDelegateExecution contextExecution) {
        final ELContext elContext = this.expressionManager.getElContext(variableScope);
        try {
            final ExpressionSetInvocation invocation = new ExpressionSetInvocation(this.valueExpression, elContext, value, contextExecution);
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
        }
        catch (Exception e) {
            throw new ProcessEngineException("Error while evaluating expression: " + this.expressionText + ". Cause: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String toString() {
        if (this.valueExpression != null) {
            return this.valueExpression.getExpressionString();
        }
        return super.toString();
    }
    
    @Override
    public boolean isLiteralText() {
        return this.valueExpression.isLiteralText();
    }
    
    @Override
    public String getExpressionText() {
        return this.expressionText;
    }
}
