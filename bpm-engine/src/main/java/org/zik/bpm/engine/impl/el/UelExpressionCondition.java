// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.javax.el.PropertyNotFoundException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.Condition;

public class UelExpressionCondition implements Condition
{
    protected Expression expression;
    
    public UelExpressionCondition(final Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public boolean evaluate(final DelegateExecution execution) {
        return this.evaluate(execution, execution);
    }
    
    @Override
    public boolean evaluate(final VariableScope scope, final DelegateExecution execution) {
        final Object result = this.expression.getValue(scope, execution);
        EnsureUtil.ensureNotNull("condition expression returns null", "result", result);
        EnsureUtil.ensureInstanceOf("condition expression returns non-Boolean", "result", result, Boolean.class);
        return (boolean)result;
    }
    
    @Override
    public boolean tryEvaluate(final VariableScope scope, final DelegateExecution execution) {
        boolean result = false;
        try {
            result = this.evaluate(scope, execution);
        }
        catch (ProcessEngineException pee) {
            if (!(pee.getCause() instanceof PropertyNotFoundException)) {
                throw pee;
            }
        }
        return result;
    }
}
