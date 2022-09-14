// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.delegate;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;

public abstract class DelegateInvocation
{
    protected Object invocationResult;
    protected BaseDelegateExecution contextExecution;
    protected ResourceDefinitionEntity contextResource;
    
    public DelegateInvocation(final BaseDelegateExecution contextExecution, final ResourceDefinitionEntity contextResource) {
        this.contextExecution = contextExecution;
        this.contextResource = contextResource;
    }
    
    public void proceed() throws Exception {
        this.invoke();
    }
    
    protected abstract void invoke() throws Exception;
    
    public Object getInvocationResult() {
        return this.invocationResult;
    }
    
    public BaseDelegateExecution getContextExecution() {
        return this.contextExecution;
    }
    
    public ResourceDefinitionEntity getContextResource() {
        return this.contextResource;
    }
}
