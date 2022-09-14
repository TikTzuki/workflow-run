// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.metrics;

import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class MetricsLogger extends ProcessEngineLogger
{
    public void couldNotCollectAndLogMetrics(final Exception e) {
        this.logWarn("001", "Could not collect and log metrics", new Object[] { e });
    }
}
