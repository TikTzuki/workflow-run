// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.util;

import org.zik.bpm.engine.impl.metrics.MetricsRegistry;
import org.zik.bpm.engine.impl.telemetry.TelemetryRegistry;

public class TelemetryUtil
{
    public static void toggleLocalTelemetry(final boolean telemetryActivated, final TelemetryRegistry telemetryRegistry, final MetricsRegistry metricsRegistry) {
        final boolean previouslyActivated = telemetryRegistry.setTelemetryLocallyActivated(telemetryActivated);
        if (!previouslyActivated && telemetryActivated) {
            if (telemetryRegistry != null) {
                telemetryRegistry.clearCommandCounts();
            }
            if (metricsRegistry != null) {
                metricsRegistry.clearTelemetryMetrics();
            }
        }
    }
}
