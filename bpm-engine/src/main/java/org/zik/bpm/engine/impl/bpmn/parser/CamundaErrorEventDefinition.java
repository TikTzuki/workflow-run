// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.parser;

import org.zik.bpm.engine.impl.el.Expression;

public class CamundaErrorEventDefinition extends ErrorEventDefinition
{
    static final long serialVersionUID = 1L;
    Expression expression;
    
    public CamundaErrorEventDefinition(final String handlerActivityId, final Expression expression) {
        super(handlerActivityId);
        this.expression = expression;
    }
    
    @Override
    public String getErrorCode() {
        return this.errorCode;
    }
    
    @Override
    public void setErrorCode(final String errorCode) {
        this.errorCode = errorCode;
    }
    
    public Expression getExpression() {
        return this.expression;
    }
    
    public void setExpression(final Expression expression) {
        this.expression = expression;
    }
}
