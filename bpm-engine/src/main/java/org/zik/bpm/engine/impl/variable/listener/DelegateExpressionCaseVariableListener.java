// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.listener;

import org.zik.bpm.engine.delegate.DelegateVariableInstance;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateCaseVariableInstance;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.delegate.CaseVariableListener;

public class DelegateExpressionCaseVariableListener implements CaseVariableListener
{
    protected Expression expression;
    private final List<FieldDeclaration> fieldDeclarations;
    
    public DelegateExpressionCaseVariableListener(final Expression expression, final List<FieldDeclaration> fieldDeclarations) {
        this.expression = expression;
        this.fieldDeclarations = fieldDeclarations;
    }
    
    @Override
    public void notify(final DelegateCaseVariableInstance variableInstance) throws Exception {
        final Object delegate = this.expression.getValue(variableInstance.getSourceExecution());
        ClassDelegateUtil.applyFieldDeclaration(this.fieldDeclarations, delegate);
        if (delegate instanceof CaseVariableListener) {
            final CaseVariableListener listenerInstance = (CaseVariableListener)delegate;
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new CaseVariableListenerInvocation(listenerInstance, variableInstance));
            return;
        }
        throw new ProcessEngineException("Delegate expression " + this.expression + " did not resolve to an implementation of " + CaseVariableListener.class);
    }
    
    public String getExpressionText() {
        return this.expression.getExpressionText();
    }
    
    public List<FieldDeclaration> getFieldDeclarations() {
        return this.fieldDeclarations;
    }
}
