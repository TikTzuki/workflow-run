// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmmn.listener;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.util.ClassDelegateUtil;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.cmmn.delegate.CaseExecutionListenerInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateCaseExecution;
import org.zik.bpm.engine.impl.bpmn.parser.FieldDeclaration;
import java.util.List;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnBehaviorLogger;
import org.zik.bpm.engine.delegate.CaseExecutionListener;
import org.zik.bpm.engine.impl.delegate.ClassDelegate;

public class ClassDelegateCaseExecutionListener extends ClassDelegate implements CaseExecutionListener
{
    protected static final CmmnBehaviorLogger LOG;
    
    public ClassDelegateCaseExecutionListener(final String className, final List<FieldDeclaration> fieldDeclarations) {
        super(className, fieldDeclarations);
    }
    
    public ClassDelegateCaseExecutionListener(final Class<?> clazz, final List<FieldDeclaration> fieldDeclarations) {
        super(clazz, fieldDeclarations);
    }
    
    @Override
    public void notify(final DelegateCaseExecution caseExecution) throws Exception {
        final CaseExecutionListener listenerInstance = this.getListenerInstance();
        Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new CaseExecutionListenerInvocation(listenerInstance, caseExecution));
    }
    
    protected CaseExecutionListener getListenerInstance() {
        final Object delegateInstance = ClassDelegateUtil.instantiateDelegate(this.className, this.fieldDeclarations);
        if (delegateInstance instanceof CaseExecutionListener) {
            return (CaseExecutionListener)delegateInstance;
        }
        throw ClassDelegateCaseExecutionListener.LOG.missingDelegateParentClassException(delegateInstance.getClass().getName(), CaseExecutionListener.class.getName());
    }
    
    static {
        LOG = ProcessEngineLogger.CMNN_BEHAVIOR_LOGGER;
    }
}
