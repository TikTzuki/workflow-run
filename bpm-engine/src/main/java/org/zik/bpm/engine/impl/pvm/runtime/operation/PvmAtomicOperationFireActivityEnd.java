// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationFireActivityEnd extends AbstractPvmEventAtomicOperation
{
    @Override
    public String getCanonicalName() {
        return "fire-activity-end";
    }
    
    @Override
    protected ScopeImpl getScope(final PvmExecutionImpl execution) {
        return execution.getActivity();
    }
    
    @Override
    protected String getEventName() {
        return "end";
    }
    
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
    }
    
    @Override
    protected boolean isSkipNotifyListeners(final PvmExecutionImpl execution) {
        return execution.hasFailedOnEndListeners();
    }
}
