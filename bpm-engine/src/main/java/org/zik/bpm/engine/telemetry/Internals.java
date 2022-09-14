// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.telemetry;

import java.util.Set;
import java.util.Map;

public interface Internals
{
    Database getDatabase();
    
    ApplicationServer getApplicationServer();
    
    LicenseKeyData getLicenseKey();
    
    Map<String, Command> getCommands();
    
    Map<String, Metric> getMetrics();
    
    Set<String> getCamundaIntegration();
    
    Set<String> getWebapps();
    
    Jdk getJdk();
}
