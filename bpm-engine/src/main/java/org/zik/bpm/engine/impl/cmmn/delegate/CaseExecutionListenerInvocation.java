// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.delegate;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.delegate.CaseExecutionListener;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class CaseExecutionListenerInvocation extends DelegateInvocation
{
    protected final CaseExecutionListener listenerInstance;
    protected final DelegateCaseExecution caseExecution;
    
    public CaseExecutionListenerInvocation(final CaseExecutionListener listenerInstance, final DelegateCaseExecution caseExecution) {
        super(caseExecution, null);
        this.listenerInstance = listenerInstance;
        this.caseExecution = caseExecution;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.listenerInstance.notify(this.caseExecution);
    }
}
