// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.VariableScope;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.delegate.Expression;

public class ServiceTaskExpressionActivityBehavior extends TaskActivityBehavior
{
    protected Expression expression;
    protected String resultVariable;
    
    public ServiceTaskExpressionActivityBehavior(final Expression expression, final String resultVariable) {
        this.expression = expression;
        this.resultVariable = resultVariable;
    }
    
    public void performExecution(final ActivityExecution execution) throws Exception {
        this.executeWithErrorPropagation(execution, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final Object value = ServiceTaskExpressionActivityBehavior.this.expression.getValue(execution);
                if (ServiceTaskExpressionActivityBehavior.this.resultVariable != null) {
                    execution.setVariable(ServiceTaskExpressionActivityBehavior.this.resultVariable, value);
                }
                ServiceTaskExpressionActivityBehavior.this.leave(execution);
                return null;
            }
        });
    }
}
