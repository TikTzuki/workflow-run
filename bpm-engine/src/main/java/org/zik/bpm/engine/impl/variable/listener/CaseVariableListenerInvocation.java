// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.listener;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.delegate.DelegateCaseVariableInstance;
import org.zik.bpm.engine.delegate.CaseVariableListener;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class CaseVariableListenerInvocation extends DelegateInvocation
{
    protected CaseVariableListener variableListenerInstance;
    protected DelegateCaseVariableInstance variableInstance;
    
    public CaseVariableListenerInvocation(final CaseVariableListener variableListenerInstance, final DelegateCaseVariableInstance variableInstance) {
        this(variableListenerInstance, variableInstance, null);
    }
    
    public CaseVariableListenerInvocation(final CaseVariableListener variableListenerInstance, final DelegateCaseVariableInstance variableInstance, final BaseDelegateExecution contextExecution) {
        super(contextExecution, null);
        this.variableListenerInstance = variableListenerInstance;
        this.variableInstance = variableInstance;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.variableListenerInstance.notify(this.variableInstance);
    }
}
