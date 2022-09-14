// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.form.handler;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class CreateStartFormInvocation extends DelegateInvocation
{
    protected StartFormHandler startFormHandler;
    protected ProcessDefinitionEntity definition;
    
    public CreateStartFormInvocation(final StartFormHandler startFormHandler, final ProcessDefinitionEntity definition) {
        super(null, definition);
        this.startFormHandler = startFormHandler;
        this.definition = definition;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.invocationResult = this.startFormHandler.createStartFormData(this.definition);
    }
}
