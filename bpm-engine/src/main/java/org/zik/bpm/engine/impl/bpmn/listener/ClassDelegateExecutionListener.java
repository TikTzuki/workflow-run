// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.listener;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.bpmn.behavior.ServiceTaskJavaDelegateActivityBehavior;
import org.zik.bpm.engine.delegate.JavaDelegate;
import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.bpmn.delegate.ExecutionListenerInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;
import org.zik.bpm.engine.impl.bpmn.behavior.BpmnBehaviorLogger;
import org.zik.bpm.engine.delegate.ExecutionListener;
import org.zik.bpm.engine.impl.delegate.ClassDelegate;

public class ClassDelegateExecutionListener extends ClassDelegate implements ExecutionListener
{
    protected static final BpmnBehaviorLogger LOG;
    
    public ClassDelegateExecutionListener(final String className, final List<FieldDeclaration> fieldDeclarations) {
        super(className, fieldDeclarations);
    }
    
    public ClassDelegateExecutionListener(final Class<?> clazz, final List<FieldDeclaration> fieldDeclarations) {
        super(clazz, fieldDeclarations);
    }
    
    @Override
    public void notify(final DelegateExecution execution) throws Exception {
        final ExecutionListener executionListenerInstance = this.getExecutionListenerInstance();
        Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new ExecutionListenerInvocation(executionListenerInstance, execution));
    }
    
    protected ExecutionListener getExecutionListenerInstance() {
        final Object delegateInstance = ClassDelegateUtil.instantiateDelegate(this.className, this.fieldDeclarations);
        if (delegateInstance instanceof ExecutionListener) {
            return (ExecutionListener)delegateInstance;
        }
        if (delegateInstance instanceof JavaDelegate) {
            return new ServiceTaskJavaDelegateActivityBehavior((JavaDelegate)delegateInstance);
        }
        throw ClassDelegateExecutionListener.LOG.missingDelegateParentClassException(delegateInstance.getClass().getName(), ExecutionListener.class.getName(), JavaDelegate.class.getName());
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
