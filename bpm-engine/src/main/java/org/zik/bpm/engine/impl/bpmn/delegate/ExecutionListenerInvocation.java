// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.delegate;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.delegate.ExecutionListener;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class ExecutionListenerInvocation extends DelegateInvocation
{
    protected final ExecutionListener executionListenerInstance;
    protected final DelegateExecution execution;
    
    public ExecutionListenerInvocation(final ExecutionListener executionListenerInstance, final DelegateExecution execution) {
        super(execution, null);
        this.executionListenerInstance = executionListenerInstance;
        this.execution = execution;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.executionListenerInstance.notify(this.execution);
    }
}
