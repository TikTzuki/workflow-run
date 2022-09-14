// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import java.util.List;
import org.zik.bpm.engine.impl.pvm.runtime.InstantiationStack;
import org.zik.bpm.engine.impl.pvm.runtime.ScopeInstantiationContext;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationActivityInitStack implements PvmAtomicOperation
{
    protected PvmAtomicOperation operationOnScopeInitialization;
    
    public PvmAtomicOperationActivityInitStack(final PvmAtomicOperation operationOnScopeInitialization) {
        this.operationOnScopeInitialization = operationOnScopeInitialization;
    }
    
    @Override
    public String getCanonicalName() {
        return "activity-stack-init";
    }
    
    @Override
    public void execute(final PvmExecutionImpl execution) {
        final ScopeInstantiationContext executionStartContext = execution.getScopeInstantiationContext();
        final InstantiationStack instantiationStack = executionStartContext.getInstantiationStack();
        final List<PvmActivity> activityStack = instantiationStack.getActivities();
        final PvmActivity currentActivity = activityStack.remove(0);
        PvmExecutionImpl propagatingExecution = execution;
        if (currentActivity.isScope()) {
            propagatingExecution = execution.createExecution();
            execution.setActive(false);
            propagatingExecution.setActivity(currentActivity);
            propagatingExecution.initialize();
        }
        else {
            propagatingExecution.setActivity(currentActivity);
        }
        propagatingExecution.performOperation((CoreAtomicOperation<CoreExecution>)this.operationOnScopeInitialization);
    }
    
    @Override
    public boolean isAsync(final PvmExecutionImpl instance) {
        return false;
    }
    
    public PvmExecutionImpl getStartContextExecution(final PvmExecutionImpl execution) {
        return execution;
    }
    
    @Override
    public boolean isAsyncCapable() {
        return false;
    }
}
