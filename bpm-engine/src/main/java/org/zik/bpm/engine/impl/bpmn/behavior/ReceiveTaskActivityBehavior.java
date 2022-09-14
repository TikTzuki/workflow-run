// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class ReceiveTaskActivityBehavior extends TaskActivityBehavior
{
    public void performExecution(final ActivityExecution execution) throws Exception {
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object data) throws Exception {
        this.leave(execution);
    }
}
