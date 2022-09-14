// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public abstract class PvmAtomicOperationCreateConcurrentExecution implements PvmAtomicOperation
{
    @Override
    public void execute(final PvmExecutionImpl execution) {
        final PvmActivity activityToStart = execution.getNextActivity();
        execution.setNextActivity(null);
        final PvmExecutionImpl propagatingExecution = execution.createConcurrentExecution();
        propagatingExecution.setActivity(activityToStart);
        this.concurrentExecutionCreated(propagatingExecution);
    }
    
    protected abstract void concurrentExecutionCreated(final PvmExecutionImpl p0);
    
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        return false;
    }
}
