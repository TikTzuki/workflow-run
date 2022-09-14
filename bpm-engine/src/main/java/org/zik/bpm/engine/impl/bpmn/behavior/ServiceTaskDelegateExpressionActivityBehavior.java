// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.bpmn.delegate.JavaDelegateInvocation;
import org.zik.bpm.engine.delegate.JavaDelegate;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.bpmn.delegate.ActivityBehaviorInvocation;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.SignallableActivityBehavior;
import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.application.InvocationContext;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;
import org.zik.bpm.engine.delegate.Expression;

public class ServiceTaskDelegateExpressionActivityBehavior extends TaskActivityBehavior
{
    protected static final BpmnBehaviorLogger LOG;
    protected Expression expression;
    private final List<FieldDeclaration> fieldDeclarations;
    
    public ServiceTaskDelegateExpressionActivityBehavior(final Expression expression, final List<FieldDeclaration> fieldDeclarations) {
        this.expression = expression;
        this.fieldDeclarations = fieldDeclarations;
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        final ProcessApplicationReference targetProcessApplication = ProcessApplicationContextUtil.getTargetProcessApplication((ExecutionEntity)execution);
        if (ProcessApplicationContextUtil.requiresContextSwitch(targetProcessApplication)) {
            Context.executeWithinProcessApplication((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    ServiceTaskDelegateExpressionActivityBehavior.this.signal(execution, signalName, signalData);
                    return null;
                }
            }, targetProcessApplication, new InvocationContext(execution));
        }
        else {
            this.doSignal(execution, signalName, signalData);
        }
    }
    
    public void doSignal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        final Object delegate = this.expression.getValue(execution);
        ClassDelegateUtil.applyFieldDeclaration(this.fieldDeclarations, delegate);
        final ActivityBehavior activityBehaviorInstance = this.getActivityBehaviorInstance(execution, delegate);
        if (activityBehaviorInstance instanceof CustomActivityBehavior) {
            final CustomActivityBehavior behavior = (CustomActivityBehavior)activityBehaviorInstance;
            final ActivityBehavior delegateActivityBehavior = behavior.getDelegateActivityBehavior();
            if (!(delegateActivityBehavior instanceof SignallableActivityBehavior)) {
                return;
            }
        }
        this.executeWithErrorPropagation(execution, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ((SignallableActivityBehavior)activityBehaviorInstance).signal(execution, signalName, signalData);
                return null;
            }
        });
    }
    
    public void performExecution(final ActivityExecution execution) throws Exception {
        final Callable<Void> callable = new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final Object delegate = ServiceTaskDelegateExpressionActivityBehavior.this.expression.getValue(execution);
                ClassDelegateUtil.applyFieldDeclaration(ServiceTaskDelegateExpressionActivityBehavior.this.fieldDeclarations, delegate);
                if (delegate instanceof ActivityBehavior) {
                    Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new ActivityBehaviorInvocation((ActivityBehavior)delegate, execution));
                }
                else {
                    if (!(delegate instanceof JavaDelegate)) {
                        throw ServiceTaskDelegateExpressionActivityBehavior.LOG.resolveDelegateExpressionException(ServiceTaskDelegateExpressionActivityBehavior.this.expression, ActivityBehavior.class, JavaDelegate.class);
                    }
                    Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new JavaDelegateInvocation((JavaDelegate)delegate, execution));
                    ServiceTaskDelegateExpressionActivityBehavior.this.leave(execution);
                }
                return null;
            }
        };
        this.executeWithErrorPropagation(execution, callable);
    }
    
    protected ActivityBehavior getActivityBehaviorInstance(final ActivityExecution execution, final Object delegateInstance) {
        if (delegateInstance instanceof ActivityBehavior) {
            return new CustomActivityBehavior((ActivityBehavior)delegateInstance);
        }
        if (delegateInstance instanceof JavaDelegate) {
            return new ServiceTaskJavaDelegateActivityBehavior((JavaDelegate)delegateInstance);
        }
        throw ServiceTaskDelegateExpressionActivityBehavior.LOG.missingDelegateParentClassException(delegateInstance.getClass().getName(), JavaDelegate.class.getName(), ActivityBehavior.class.getName());
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
