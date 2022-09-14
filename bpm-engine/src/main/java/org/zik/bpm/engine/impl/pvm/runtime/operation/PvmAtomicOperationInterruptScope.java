// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public abstract class PvmAtomicOperationInterruptScope implements PvmAtomicOperation
{
    @Override
    public void execute(final PvmExecutionImpl execution) {
        final PvmActivity interruptingActivity = this.getInterruptingActivity(execution);
        final PvmExecutionImpl scopeExecution = execution.isScope() ? execution : execution.getParent();
        if (scopeExecution != execution) {
            execution.remove();
        }
        scopeExecution.interrupt("Interrupting activity " + interruptingActivity + " executed.");
        scopeExecution.setActivity(interruptingActivity);
        scopeExecution.setActive(true);
        scopeExecution.setTransition(execution.getTransition());
        this.scopeInterrupted(scopeExecution);
    }
    
    protected abstract void scopeInterrupted(final PvmExecutionImpl p0);
    
    protected abstract PvmActivity getInterruptingActivity(final PvmExecutionImpl p0);
    
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        return false;
    }
    
    @Override
    public boolean isAsyncCapable() {
        return false;
    }
}
