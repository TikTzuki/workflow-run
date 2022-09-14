// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry;

import org.zik.bpm.engine.impl.telemetry.dto.ApplicationServerImpl;

public class PlatformTelemetryRegistry
{
    protected static ApplicationServerImpl applicationServer;
    
    public static synchronized ApplicationServerImpl getApplicationServer() {
        return PlatformTelemetryRegistry.applicationServer;
    }
    
    public static synchronized void setApplicationServer(final String applicationServerVersion) {
        if (PlatformTelemetryRegistry.applicationServer == null) {
            PlatformTelemetryRegistry.applicationServer = new ApplicationServerImpl(applicationServerVersion);
        }
    }
    
    public static synchronized void clear() {
        PlatformTelemetryRegistry.applicationServer = null;
    }
}
