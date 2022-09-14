// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.delegate;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;

public class ExpressionGetInvocation extends DelegateInvocation
{
    protected final ValueExpression valueExpression;
    protected final ELContext elContext;
    
    public ExpressionGetInvocation(final ValueExpression valueExpression, final ELContext elContext, final BaseDelegateExecution contextExecution) {
        super(contextExecution, null);
        this.valueExpression = valueExpression;
        this.elContext = elContext;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.invocationResult = this.valueExpression.getValue(this.elContext);
    }
}
