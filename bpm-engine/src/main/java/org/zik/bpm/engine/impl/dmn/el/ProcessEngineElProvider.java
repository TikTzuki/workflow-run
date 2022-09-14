// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.dmn.el;

import org.camunda.bpm.dmn.engine.impl.spi.el.ElExpression;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.dmn.engine.impl.spi.el.ElProvider;

public class ProcessEngineElProvider implements ElProvider
{
    protected final ExpressionManager expressionManager;
    
    public ProcessEngineElProvider(final ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
    }
    
    public ElExpression createExpression(final String expression) {
        return (ElExpression)new ProcessEngineElExpression(this.expressionManager.createValueExpression(expression));
    }
}
