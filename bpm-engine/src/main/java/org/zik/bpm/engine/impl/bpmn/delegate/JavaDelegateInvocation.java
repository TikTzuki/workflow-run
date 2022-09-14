// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.delegate;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.delegate.JavaDelegate;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class JavaDelegateInvocation extends DelegateInvocation
{
    protected final JavaDelegate delegateInstance;
    protected final DelegateExecution execution;
    
    public JavaDelegateInvocation(final JavaDelegate delegateInstance, final DelegateExecution execution) {
        super(execution, null);
        this.delegateInstance = delegateInstance;
        this.execution = execution;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.delegateInstance.execute(this.execution);
    }
}
