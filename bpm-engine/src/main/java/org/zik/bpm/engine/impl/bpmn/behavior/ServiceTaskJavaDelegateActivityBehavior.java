// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.bpmn.delegate.JavaDelegateInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.delegate.JavaDelegate;
import org.zik.bpm.engine.delegate.ExecutionListener;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;

public class ServiceTaskJavaDelegateActivityBehavior extends TaskActivityBehavior implements ActivityBehavior, ExecutionListener
{
    protected JavaDelegate javaDelegate;
    
    protected ServiceTaskJavaDelegateActivityBehavior() {
    }
    
    public ServiceTaskJavaDelegateActivityBehavior(final JavaDelegate javaDelegate) {
        this.javaDelegate = javaDelegate;
    }
    
    public void performExecution(final ActivityExecution execution) throws Exception {
        this.execute((DelegateExecution)execution);
        this.leave(execution);
    }
    
    @Override
    public void notify(final DelegateExecution execution) throws Exception {
        this.execute(execution);
    }
    
    public void execute(final DelegateExecution execution) throws Exception {
        Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new JavaDelegateInvocation(this.javaDelegate, execution));
    }
}
