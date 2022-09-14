// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.incident;

import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class IncidentLogger extends ProcessEngineLogger
{
    public void executionNotFound(final String executionId) {
        this.logWarn("001", "The execution with id {} does not exist", new Object[] { executionId });
    }
}
