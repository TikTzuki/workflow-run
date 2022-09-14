// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class IntermediateCatchEventActivityBehavior extends AbstractBpmnActivityBehavior
{
    protected boolean isAfterEventBasedGateway;
    
    public IntermediateCatchEventActivityBehavior(final boolean isAfterEventBasedGateway) {
        this.isAfterEventBasedGateway = isAfterEventBasedGateway;
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        if (this.isAfterEventBasedGateway) {
            this.leave(execution);
        }
    }
    
    public boolean isAfterEventBasedGateway() {
        return this.isAfterEventBasedGateway;
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        this.leave(execution);
    }
}
