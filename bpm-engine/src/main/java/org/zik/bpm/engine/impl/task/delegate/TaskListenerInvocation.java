// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.task.delegate;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.delegate.TaskListener;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class TaskListenerInvocation extends DelegateInvocation
{
    protected final TaskListener taskListenerInstance;
    protected final DelegateTask delegateTask;
    
    public TaskListenerInvocation(final TaskListener executionListenerInstance, final DelegateTask delegateTask) {
        this(executionListenerInstance, delegateTask, null);
    }
    
    public TaskListenerInvocation(final TaskListener taskListenerInstance, final DelegateTask delegateTask, final BaseDelegateExecution contextExecution) {
        super(contextExecution, null);
        this.taskListenerInstance = taskListenerInstance;
        this.delegateTask = delegateTask;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.taskListenerInstance.notify(this.delegateTask);
    }
}
