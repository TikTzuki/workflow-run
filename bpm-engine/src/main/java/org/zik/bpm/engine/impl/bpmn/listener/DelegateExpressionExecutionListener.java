// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.listener;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.bpmn.delegate.JavaDelegateInvocation;
import org.zik.bpm.engine.delegate.JavaDelegate;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.bpmn.delegate.ExecutionListenerInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.bpmn.behavior.BpmnBehaviorLogger;
import org.zik.bpm.engine.delegate.ExecutionListener;

public class DelegateExpressionExecutionListener implements ExecutionListener
{
    protected static final BpmnBehaviorLogger LOG;
    protected Expression expression;
    private final List<FieldDeclaration> fieldDeclarations;
    
    public DelegateExpressionExecutionListener(final Expression expression, final List<FieldDeclaration> fieldDeclarations) {
        this.expression = expression;
        this.fieldDeclarations = fieldDeclarations;
    }
    
    @Override
    public void notify(final DelegateExecution execution) throws Exception {
        final Object delegate = this.expression.getValue(execution);
        ClassDelegateUtil.applyFieldDeclaration(this.fieldDeclarations, delegate);
        if (delegate instanceof ExecutionListener) {
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new ExecutionListenerInvocation((ExecutionListener)delegate, execution));
        }
        else {
            if (!(delegate instanceof JavaDelegate)) {
                throw DelegateExpressionExecutionListener.LOG.resolveDelegateExpressionException(this.expression, ExecutionListener.class, JavaDelegate.class);
            }
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new JavaDelegateInvocation((JavaDelegate)delegate, execution));
        }
    }
    
    public String getExpressionText() {
        return this.expression.getExpressionText();
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
