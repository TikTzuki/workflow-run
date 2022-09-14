// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.variable.listener;

import org.zik.bpm.engine.delegate.DelegateVariableInstance;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateCaseVariableInstance;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;
import org.zik.bpm.engine.delegate.CaseVariableListener;
import org.zik.bpm.engine.impl.delegate.ClassDelegate;

public class ClassDelegateCaseVariableListener extends ClassDelegate implements CaseVariableListener
{
    public ClassDelegateCaseVariableListener(final String className, final List<FieldDeclaration> fieldDeclarations) {
        super(className, fieldDeclarations);
    }
    
    public ClassDelegateCaseVariableListener(final Class<?> clazz, final List<FieldDeclaration> fieldDeclarations) {
        super(clazz, fieldDeclarations);
    }
    
    @Override
    public void notify(final DelegateCaseVariableInstance variableInstance) throws Exception {
        final CaseVariableListener variableListenerInstance = this.getVariableListenerInstance();
        Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new CaseVariableListenerInvocation(variableListenerInstance, variableInstance));
    }
    
    protected CaseVariableListener getVariableListenerInstance() {
        final Object delegateInstance = ClassDelegateUtil.instantiateDelegate(this.className, this.fieldDeclarations);
        if (delegateInstance instanceof CaseVariableListener) {
            return (CaseVariableListener)delegateInstance;
        }
        throw new ProcessEngineException(delegateInstance.getClass().getName() + " doesn't implement " + CaseVariableListener.class);
    }
}
