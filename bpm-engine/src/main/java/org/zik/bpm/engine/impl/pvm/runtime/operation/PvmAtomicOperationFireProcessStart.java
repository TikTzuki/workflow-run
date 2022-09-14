// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.model.CoreModelElement;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;

public class PvmAtomicOperationFireProcessStart extends AbstractPvmEventAtomicOperation
{
    @Override
    protected ScopeImpl getScope(final PvmExecutionImpl execution) {
        return execution.getProcessDefinition();
    }
    
    @Override
    protected String getEventName() {
        return "start";
    }
    
    @Override
    protected void eventNotificationsCompleted(final PvmExecutionImpl execution) {
    }
    
    @Override
    public String getCanonicalName() {
        return "fire-process-start";
    }
}
