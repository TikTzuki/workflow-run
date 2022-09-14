// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm.runtime.operation;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.pvm.PvmLogger;

public abstract class PvmAtomicOperationCreateScope implements PvmAtomicOperation
{
    private static final PvmLogger LOG;
    
    @Override
    public void execute(final PvmExecutionImpl execution) {
        execution.setActivityInstanceId(execution.getParentActivityInstanceId());
        PvmExecutionImpl propagatingExecution = null;
        final PvmActivity activity = execution.getActivity();
        if (activity.isScope()) {
            propagatingExecution = execution.createExecution();
            propagatingExecution.setActivity(activity);
            propagatingExecution.setTransition(execution.getTransition());
            execution.setTransition(null);
            execution.setActive(false);
            execution.setActivity(null);
            PvmAtomicOperationCreateScope.LOG.createScope(execution, propagatingExecution);
            propagatingExecution.initialize();
        }
        else {
            propagatingExecution = execution;
        }
        this.scopeCreated(propagatingExecution);
    }
    
    protected abstract void scopeCreated(final PvmExecutionImpl p0);
    
    static {
        LOG = PvmLogger.PVM_LOGGER;
    }
}
