// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class TransactionLogger extends ProcessEngineLogger
{
    public ProcessEngineException exceptionWhileInteractingWithTransaction(final String operation, final Throwable e) {
        throw new ProcessEngineException(this.exceptionMessage("001", "{} while {}", new Object[] { e.getClass().getSimpleName(), operation }), e);
    }
    
    public void debugTransactionOperation(final String string) {
        this.logDebug("002", string, new Object[0]);
    }
    
    public void exceptionWhileFiringEvent(final TransactionState state, final Throwable exception) {
        this.logError("003", "Exception while firing event {}: {}", new Object[] { state, exception.getMessage(), exception });
    }
    
    public void debugFiringEventRolledBack() {
        this.logDebug("004", "Firing event rolled back", new Object[0]);
    }
}
