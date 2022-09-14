// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.runtime.LegacyBehavior;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class CancelBoundaryEventActivityBehavior extends BoundaryEventActivityBehavior
{
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        if (LegacyBehavior.signalCancelBoundaryEvent(signalName)) {
            if (!execution.hasChildren()) {
                this.leave(execution);
            }
            else {
                ((ExecutionEntity)execution).forceUpdate();
            }
        }
        else {
            super.signal(execution, signalName, signalData);
        }
    }
}
