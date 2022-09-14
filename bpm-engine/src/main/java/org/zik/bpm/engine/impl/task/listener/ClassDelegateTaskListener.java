// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.task.listener;

import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.task.delegate.TaskListenerInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;
import org.zik.bpm.engine.delegate.TaskListener;
import org.zik.bpm.engine.impl.delegate.ClassDelegate;

public class ClassDelegateTaskListener extends ClassDelegate implements TaskListener
{
    public ClassDelegateTaskListener(final String className, final List<FieldDeclaration> fieldDeclarations) {
        super(className, fieldDeclarations);
    }
    
    public ClassDelegateTaskListener(final Class<?> clazz, final List<FieldDeclaration> fieldDeclarations) {
        super(clazz, fieldDeclarations);
    }
    
    @Override
    public void notify(final DelegateTask delegateTask) {
        final TaskListener taskListenerInstance = this.getTaskListenerInstance();
        try {
            Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new TaskListenerInvocation(taskListenerInstance, delegateTask));
        }
        catch (Exception e) {
            throw new ProcessEngineException("Exception while invoking TaskListener: " + e.getMessage(), e);
        }
    }
    
    protected TaskListener getTaskListenerInstance() {
        final Object delegateInstance = ClassDelegateUtil.instantiateDelegate(this.className, this.fieldDeclarations);
        if (delegateInstance instanceof TaskListener) {
            return (TaskListener)delegateInstance;
        }
        throw new ProcessEngineException(delegateInstance.getClass().getName() + " doesn't implement " + TaskListener.class);
    }
}
