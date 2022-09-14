// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.form.TaskFormData;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;

public class DelegateTaskFormHandler extends DelegateFormHandler implements TaskFormHandler
{
    public DelegateTaskFormHandler(final TaskFormHandler formHandler, final DeploymentEntity deployment) {
        super(formHandler, deployment.getId());
    }
    
    @Override
    public TaskFormData createTaskForm(final TaskEntity task) {
        return this.performContextSwitch((Callable<TaskFormData>)new Callable<TaskFormData>() {
            @Override
            public TaskFormData call() throws Exception {
                final CreateTaskFormInvocation invocation = new CreateTaskFormInvocation((TaskFormHandler)DelegateTaskFormHandler.this.formHandler, task);
                Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
                return (TaskFormData)invocation.getInvocationResult();
            }
        });
    }
    
    @Override
    public FormHandler getFormHandler() {
        return this.formHandler;
    }
}
