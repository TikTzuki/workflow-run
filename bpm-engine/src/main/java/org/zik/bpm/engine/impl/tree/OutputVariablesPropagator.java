// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.tree;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.pvm.delegate.SubProcessActivityBehavior;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class OutputVariablesPropagator implements TreeVisitor<ActivityExecution>
{
    @Override
    public void visit(final ActivityExecution execution) {
        if (this.isProcessInstanceOfSubprocess(execution)) {
            final PvmExecutionImpl superExecution = (PvmExecutionImpl)execution.getSuperExecution();
            final ActivityImpl activity = superExecution.getActivity();
            final SubProcessActivityBehavior subProcessActivityBehavior = (SubProcessActivityBehavior)activity.getActivityBehavior();
            subProcessActivityBehavior.passOutputVariables(superExecution, execution);
        }
    }
    
    protected boolean isProcessInstanceOfSubprocess(final ActivityExecution execution) {
        return execution.isProcessInstanceExecution() && execution.getSuperExecution() != null;
    }
}
