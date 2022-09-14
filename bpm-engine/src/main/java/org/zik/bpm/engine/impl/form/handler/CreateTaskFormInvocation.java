// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class CreateTaskFormInvocation extends DelegateInvocation
{
    protected TaskFormHandler taskFormHandler;
    protected TaskEntity task;
    
    public CreateTaskFormInvocation(final TaskFormHandler taskFormHandler, final TaskEntity task) {
        super(null, null);
        this.taskFormHandler = taskFormHandler;
        this.task = task;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.invocationResult = this.taskFormHandler.createTaskForm(this.task);
    }
}
