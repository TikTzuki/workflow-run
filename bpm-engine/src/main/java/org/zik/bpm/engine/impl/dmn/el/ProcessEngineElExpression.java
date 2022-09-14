// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.el;

import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.camunda.bpm.engine.variable.context.VariableContext;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;
import org.camunda.bpm.dmn.engine.impl.spi.el.ElExpression;

public class ProcessEngineElExpression implements ElExpression
{
    protected final ValueExpression valueExpression;
    
    public ProcessEngineElExpression(final ValueExpression expression) {
        this.valueExpression = expression;
    }
    
    public Object getValue(final VariableContext variableContext) {
        if (Context.getCommandContext() == null) {
            throw new ProcessEngineException("Expression can only be evaluated inside the context of the process engine");
        }
        final ELContext context = Context.getProcessEngineConfiguration().getExpressionManager().createElContext(variableContext);
        return this.valueExpression.getValue(context);
    }
}
