// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class NoneEndEventActivityBehavior extends FlowNodeActivityBehavior
{
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        execution.end(true);
    }
}
