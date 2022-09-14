// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.form.StartFormData;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentEntity;

public class DelegateStartFormHandler extends DelegateFormHandler implements StartFormHandler
{
    public DelegateStartFormHandler(final StartFormHandler formHandler, final DeploymentEntity deployment) {
        super(formHandler, deployment.getId());
    }
    
    @Override
    public StartFormData createStartFormData(final ProcessDefinitionEntity processDefinition) {
        return this.performContextSwitch((Callable<StartFormData>)new Callable<StartFormData>() {
            @Override
            public StartFormData call() throws Exception {
                final CreateStartFormInvocation invocation = new CreateStartFormInvocation((StartFormHandler)DelegateStartFormHandler.this.formHandler, processDefinition);
                Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(invocation);
                return (StartFormData)invocation.getInvocationResult();
            }
        });
    }
    
    @Override
    public StartFormHandler getFormHandler() {
        return (StartFormHandler)this.formHandler;
    }
}
