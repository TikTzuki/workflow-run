// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.bpmn.delegate.ActivityBehaviorSignalInvocation;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;
import org.zik.bpm.engine.impl.bpmn.delegate.ActivityBehaviorInvocation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.SignallableActivityBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;

public class CustomActivityBehavior implements ActivityBehavior, SignallableActivityBehavior
{
    protected ActivityBehavior delegateActivityBehavior;
    
    public CustomActivityBehavior(final ActivityBehavior activityBehavior) {
        this.delegateActivityBehavior = activityBehavior;
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new ActivityBehaviorInvocation(this.delegateActivityBehavior, execution));
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalEvent, final Object signalData) throws Exception {
        Context.getProcessEngineConfiguration().getDelegateInterceptor().handleInvocation(new ActivityBehaviorSignalInvocation((SignallableActivityBehavior)this.delegateActivityBehavior, execution, signalEvent, signalData));
    }
    
    public ActivityBehavior getDelegateActivityBehavior() {
        return this.delegateActivityBehavior;
    }
}
