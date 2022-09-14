// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.task.listener;

import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.task.delegate.TaskListenerInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.delegate.TaskListener;

public class DelegateExpressionTaskListener implements TaskListener
{
    protected Expression expression;
    private final List<FieldDeclaration> fieldDeclarations;
    
    public DelegateExpressionTaskListener(final Expression expression, final List<FieldDeclaration> fieldDeclarations) {
        this.expression = expression;
        this.fieldDeclarations = fieldDeclarations;
    }
    
    @Override
    public void notify(final DelegateTask delegateTask) {
        VariableScope variableScope = delegateTask.getExecution();
        if (variableScope == null) {
            variableScope = delegateTask.getCaseExecution();
        }
        final Object delegate = this.expression.getValue(variableScope);
        ClassDelegateUtil.applyFieldDeclaration(this.fieldDeclarations, delegate);
        if (delegate instanceof TaskListener) {
            try {
                Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new TaskListenerInvocation((TaskListener)delegate, delegateTask));
                return;
            }
            catch (Exception e) {
                throw new ProcessEngineException("Exception while invoking TaskListener: " + e.getMessage(), e);
            }
            throw new ProcessEngineException("Delegate expression " + this.expression + " did not resolve to an implementation of " + TaskListener.class);
        }
        throw new ProcessEngineException("Delegate expression " + this.expression + " did not resolve to an implementation of " + TaskListener.class);
    }
    
    public String getExpressionText() {
        return this.expression.getExpressionText();
    }
    
    public List<FieldDeclaration> getFieldDeclarations() {
        return this.fieldDeclarations;
    }
}
