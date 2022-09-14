// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.listener;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.cmmn.delegate.CaseExecutionListenerInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.delegate.CaseExecutionListener;

public class DelegateExpressionCaseExecutionListener implements CaseExecutionListener
{
    protected Expression expression;
    private final List<FieldDeclaration> fieldDeclarations;
    
    public DelegateExpressionCaseExecutionListener(final Expression expression, final List<FieldDeclaration> fieldDeclarations) {
        this.expression = expression;
        this.fieldDeclarations = fieldDeclarations;
    }
    
    @Override
    public void notify(final DelegateCaseExecution caseExecution) throws Exception {
        final Object delegate = this.expression.getValue(caseExecution);
        ClassDelegateUtil.applyFieldDeclaration(this.fieldDeclarations, delegate);
        if (delegate instanceof CaseExecutionListener) {
            final CaseExecutionListener listenerInstance = (CaseExecutionListener)delegate;
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new CaseExecutionListenerInvocation(listenerInstance, caseExecution));
            return;
        }
        throw new ProcessEngineException("Delegate expression " + this.expression + " did not resolve to an implementation of " + CaseExecutionListener.class);
    }
    
    public String getExpressionText() {
        return this.expression.getExpressionText();
    }
    
    public List<FieldDeclaration> getFieldDeclarations() {
        return this.fieldDeclarations;
    }
}
