// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationTransitionNotifyListenerTake extends AbstractPvmAtomicOperationTransitionNotifyListenerTake
{
    @Override
    public boolean isAsync(final PvmExecutionImpl execution) {
        return execution.getActivity().isAsyncAfter();
    }
    
    @Override
    public String getCanonicalName() {
        return "transition-notify-listener-take";
    }
    
    @Override
    public boolean isAsyncCapable() {
        return true;
    }
    
    @Override
    public boolean shouldHandleFailureAsBpmnError() {
        return true;
    }
}
