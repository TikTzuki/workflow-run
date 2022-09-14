// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class ContextLogger extends ProcessEngineLogger
{
    public void debugExecutingAtomicOperation(final CoreAtomicOperation<?> executionOperation, final CoreExecution execution) {
        this.logDebug("001", "Executing atomic operation {} on {}", new Object[] { executionOperation, execution });
    }
    
    public void debugException(final Throwable throwable) {
        this.logDebug("002", "Exception while closing command context: {}", new Object[] { throwable.getMessage(), throwable });
    }
    
    public void infoException(final Throwable throwable) {
        this.logInfo("003", "Exception while closing command context: {}", new Object[] { throwable.getMessage(), throwable });
    }
    
    public void errorException(final Throwable throwable) {
        this.logError("004", "Exception while closing command context: {}", new Object[] { throwable.getMessage(), throwable });
    }
    
    public void exceptionWhileInvokingOnCommandFailed(final Throwable t) {
        this.logError("005", "Exception while invoking onCommandFailed()", new Object[] { t });
    }
    
    public void bpmnStackTrace(final String string) {
        this.logDebug("006", string, new Object[0]);
    }
}
