// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationActivityStartConcurrent extends PvmAtomicOperationCreateConcurrentExecution
{
    @Override
    protected void concurrentExecutionCreated(final PvmExecutionImpl propagatingExecution) {
        propagatingExecution.setActivityInstanceId(null);
        propagatingExecution.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationActivityStartConcurrent.ACTIVITY_START_CREATE_SCOPE);
    }
    
    @Override
    public String getCanonicalName() {
        return "activity-start-concurrent";
    }
    
    @Override
    public boolean isAsyncCapable() {
        return false;
    }
}
