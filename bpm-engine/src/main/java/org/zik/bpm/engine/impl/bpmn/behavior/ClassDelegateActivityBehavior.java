// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.JavaDelegate;
import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.SignallableActivityBehavior;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.application.InvocationContext;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;

public class ClassDelegateActivityBehavior extends AbstractBpmnActivityBehavior
{
    protected static final BpmnBehaviorLogger LOG;
    protected String className;
    protected List<FieldDeclaration> fieldDeclarations;
    
    public ClassDelegateActivityBehavior(final String className, final List<FieldDeclaration> fieldDeclarations) {
        this.className = className;
        this.fieldDeclarations = fieldDeclarations;
    }
    
    public ClassDelegateActivityBehavior(final Class<?> clazz, final List<FieldDeclaration> fieldDeclarations) {
        this(clazz.getName(), fieldDeclarations);
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        this.executeWithErrorPropagation(execution, new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                ClassDelegateActivityBehavior.this.getActivityBehaviorInstance(execution).execute(execution);
                return null;
            }
        });
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        final ProcessApplicationReference targetProcessApplication = ProcessApplicationContextUtil.getTargetProcessApplication((ExecutionEntity)execution);
        if (ProcessApplicationContextUtil.requiresContextSwitch(targetProcessApplication)) {
            Context.executeWithinProcessApplication((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    ClassDelegateActivityBehavior.this.signal(execution, signalName, signalData);
                    return null;
                }
            }, targetProcessApplication, new InvocationContext(execution));
        }
        else {
            this.doSignal(execution, signalName, signalData);
        }
    }
    
    protected void doSignal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        final ActivityBehavior activityBehaviorInstance = this.getActivityBehaviorInstance(execution);
        if (activityBehaviorInstance instanceof CustomActivityBehavior) {
            final CustomActivityBehavior behavior = (CustomActivityBehavior)activityBehaviorInstance;
            final ActivityBehavior delegate = behavior.getDelegateActivityBehavior();
            if (!(delegate instanceof SignallableActivityBehavior)) {
                throw ClassDelegateActivityBehavior.LOG.incorrectlyUsedSignalException(SignallableActivityBehavior.class.getName());
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
    
    protected ActivityBehavior getActivityBehaviorInstance(final ActivityExecution execution) {
        final Object delegateInstance = ClassDelegateUtil.instantiateDelegate(this.className, this.fieldDeclarations);
        if (delegateInstance instanceof ActivityBehavior) {
            return new CustomActivityBehavior((ActivityBehavior)delegateInstance);
        }
        if (delegateInstance instanceof JavaDelegate) {
            return new ServiceTaskJavaDelegateActivityBehavior((JavaDelegate)delegateInstance);
        }
        throw ClassDelegateActivityBehavior.LOG.missingDelegateParentClassException(delegateInstance.getClass().getName(), JavaDelegate.class.getName(), ActivityBehavior.class.getName());
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
