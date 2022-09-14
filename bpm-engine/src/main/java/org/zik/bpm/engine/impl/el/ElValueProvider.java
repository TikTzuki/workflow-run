// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.el;

import org.camunda.commons.utils.EnsureUtil;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;

public class ElValueProvider implements ParameterValueProvider, Comparable<ElValueProvider>
{
    protected Expression expression;
    
    public ElValueProvider(final Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public Object getValue(final VariableScope variableScope) {
        EnsureUtil.ensureNotNull("variableScope", (Object)variableScope);
        return this.expression.getValue(variableScope);
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    public void setExpression(final Expression expression) {
        this.expression = expression;
    }
    
    @Override
    public int compareTo(final ElValueProvider o) {
        return this.expression.getExpressionText().compareTo(o.getExpression().getExpressionText());
    }
    
    @Override
    public boolean isDynamic() {
        return !this.getExpression().isLiteralText();
    }
}
