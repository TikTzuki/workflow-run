// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.delegate;

import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.SignallableActivityBehavior;
import org.zik.bpm.engine.impl.delegate.DelegateInvocation;

public class ActivityBehaviorSignalInvocation extends DelegateInvocation
{
    protected SignallableActivityBehavior behaviorInstance;
    protected ActivityExecution execution;
    protected String signalName;
    protected Object signalData;
    
    public ActivityBehaviorSignalInvocation(final SignallableActivityBehavior behaviorInstance, final ActivityExecution execution, final String signalName, final Object signalData) {
        super(execution, null);
        this.behaviorInstance = behaviorInstance;
        this.execution = execution;
        this.signalName = signalName;
        this.signalData = signalData;
    }
    
    @Override
    protected void invoke() throws Exception {
        this.behaviorInstance.signal(this.execution, this.signalName, this.signalData);
    }
}
