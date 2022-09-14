// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.pvm;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class PvmLogger extends ProcessEngineLogger
{
    public void notTakingTranistion(final PvmTransition outgoingTransition) {
        this.logDebug("001", "Not taking transition '{}', outgoing execution has ended.", new Object[] { outgoingTransition });
    }
    
    public void debugExecutesActivity(final PvmExecutionImpl execution, final ActivityImpl activity, final String name) {
        this.logDebug("002", "{} executed activity {}: {}", new Object[] { execution, activity, name });
    }
    
    public void debugLeavesActivityInstance(final PvmExecutionImpl execution, final String activityInstanceId) {
        this.logDebug("003", "Execution {} leaves activity instance {}", new Object[] { execution, activityInstanceId });
    }
    
    public void debugDestroyScope(final PvmExecutionImpl execution, final PvmExecutionImpl propagatingExecution) {
        this.logDebug("004", "Execution {} leaves parent scope {}", new Object[] { execution, propagatingExecution });
    }
    
    public void destroying(final PvmExecutionImpl pvmExecutionImpl) {
        this.logDebug("005", "Detroying scope {}", new Object[] { pvmExecutionImpl });
    }
    
    public void removingEventScope(final PvmExecutionImpl childExecution) {
        this.logDebug("006", "Removeing event scope {}", new Object[] { childExecution });
    }
    
    public void interruptingExecution(final String reason, final boolean skipCustomListeners) {
        this.logDebug("007", "Interrupting execution execution {}, {}", new Object[] { reason, skipCustomListeners });
    }
    
    public void debugEnterActivityInstance(final PvmExecutionImpl pvmExecutionImpl, final String parentActivityInstanceId) {
        this.logDebug("008", "Enter activity instance {} parent: {}", new Object[] { pvmExecutionImpl, parentActivityInstanceId });
    }
    
    public void exceptionWhileCompletingSupProcess(final PvmExecutionImpl execution, final Exception e) {
        this.logError("009", "Exception while completing subprocess of execution {}", new Object[] { execution, e });
    }
    
    public void createScope(final PvmExecutionImpl execution, final PvmExecutionImpl propagatingExecution) {
        this.logDebug("010", "Create scope: parent exection {} continues as  {}", new Object[] { execution, propagatingExecution });
    }
    
    public ProcessEngineException scopeNotFoundException(final String activityId, final String executionId) {
        return new ProcessEngineException(this.exceptionMessage("011", "Scope with specified activity Id {} and execution {} not found", new Object[] { activityId, executionId }));
    }
}
