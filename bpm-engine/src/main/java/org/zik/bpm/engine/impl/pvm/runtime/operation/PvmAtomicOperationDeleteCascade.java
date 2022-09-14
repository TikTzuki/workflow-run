// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationDeleteCascade implements PvmAtomicOperation
{
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        return false;
    }
    
    @Override
    public boolean isAsyncCapable() {
        return false;
    }
    
    @Override
    public void execute(final PvmExecutionImpl execution) {
        PvmExecutionImpl nextLeaf;
        do {
            nextLeaf = this.findNextLeaf(execution);
            if (nextLeaf.isDeleteRoot() && nextLeaf.isRemoved()) {
                return;
            }
            final PvmExecutionImpl deleteRoot = this.getDeleteRoot(execution);
            if (deleteRoot != null) {
                nextLeaf.setSkipCustomListeners(deleteRoot.isSkipCustomListeners());
                nextLeaf.setSkipIoMappings(deleteRoot.isSkipIoMappings());
                nextLeaf.setExternallyTerminated(deleteRoot.isExternallyTerminated());
            }
            final PvmExecutionImpl subProcessInstance = nextLeaf.getSubProcessInstance();
            if (subProcessInstance != null) {
                if (deleteRoot.isSkipSubprocesses()) {
                    subProcessInstance.setSuperExecution(null);
                }
                else {
                    subProcessInstance.deleteCascade(execution.getDeleteReason(), nextLeaf.isSkipCustomListeners(), nextLeaf.isSkipIoMappings(), nextLeaf.isExternallyTerminated(), nextLeaf.isSkipSubprocesses());
                }
            }
            nextLeaf.performOperation((CoreAtomicOperation<CoreExecution>)PvmAtomicOperationDeleteCascade.DELETE_CASCADE_FIRE_ACTIVITY_END);
        } while (!nextLeaf.isDeleteRoot());
    }
    
    protected PvmExecutionImpl findNextLeaf(final PvmExecutionImpl execution) {
        if (execution.hasChildren()) {
            return this.findNextLeaf((PvmExecutionImpl)execution.getExecutions().get(0));
        }
        return execution;
    }
    
    protected PvmExecutionImpl getDeleteRoot(final PvmExecutionImpl execution) {
        if (execution == null) {
            return null;
        }
        if (execution.isDeleteRoot()) {
            return execution;
        }
        return this.getDeleteRoot(execution.getParent());
    }
    
    @Override
    public String getCanonicalName() {
        return "delete-cascade";
    }
}
