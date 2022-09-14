// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.delegate;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.javax.el.ELContext;
import org.zik.bpm.engine.impl.javax.el.ValueExpression;

public class ExpressionSetInvocation extends DelegateInvocation
{
    protected final ValueExpression valueExpression;
    protected final Object value;
    protected ELContext elContext;
    
    public ExpressionSetInvocation(final ValueExpression valueExpression, final ELContext elContext, final Object value, final BaseDelegateExecution contextExecution) {
        super(contextExecution, null);
        this.valueExpression = valueExpression;
        this.value = value;
        this.elContext = elContext;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.valueExpression.setValue(this.elContext, this.value);
    }
}
