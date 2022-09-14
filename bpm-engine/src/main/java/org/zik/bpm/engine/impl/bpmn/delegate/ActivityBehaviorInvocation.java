// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.delegate;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityBehavior;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class ActivityBehaviorInvocation extends DelegateInvocation
{
    protected final ActivityBehavior behaviorInstance;
    protected final ActivityExecution execution;
    
    public ActivityBehaviorInvocation(final ActivityBehavior behaviorInstance, final ActivityExecution execution) {
        super(execution, null);
        this.behaviorInstance = behaviorInstance;
        this.execution = execution;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.behaviorInstance.execute(this.execution);
    }
}
