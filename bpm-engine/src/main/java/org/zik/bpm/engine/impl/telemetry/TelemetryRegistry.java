// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry;

import java.util.HashSet;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Set;
import org.zik.bpm.engine.impl.telemetry.dto.LicenseKeyDataImpl;
import org.zik.bpm.engine.impl.telemetry.dto.ApplicationServerImpl;
import java.util.Map;

public class TelemetryRegistry
{
    protected Map<String, CommandCounter> commands;
    protected ApplicationServerImpl applicationServer;
    protected LicenseKeyDataImpl licenseKey;
    protected String camundaIntegration;
    protected Set<String> webapps;
    protected AtomicBoolean telemetryLocallyActivated;
    
    public TelemetryRegistry() {
        this.commands = new HashMap<String, CommandCounter>();
        this.webapps = new HashSet<String>();
        this.telemetryLocallyActivated = new AtomicBoolean(false);
    }
    
    public synchronized ApplicationServerImpl getApplicationServer() {
        if (this.applicationServer == null) {
            this.applicationServer = PlatformTelemetryRegistry.getApplicationServer();
        }
        return this.applicationServer;
    }
    
    public synchronized void setApplicationServer(final String applicationServerVersion) {
        this.applicationServer = new ApplicationServerImpl(applicationServerVersion);
    }
    
    public Map<String, CommandCounter> getCommands() {
        return this.commands;
    }
    
    public String getCamundaIntegration() {
        return this.camundaIntegration;
    }
    
    public void setCamundaIntegration(final String camundaIntegration) {
        this.camundaIntegration = camundaIntegration;
    }
    
    public LicenseKeyDataImpl getLicenseKey() {
        return this.licenseKey;
    }
    
    public void setLicenseKey(final LicenseKeyDataImpl licenseKey) {
        this.licenseKey = licenseKey;
    }
    
    public synchronized Set<String> getWebapps() {
        return this.webapps;
    }
    
    public synchronized void setWebapps(final Set<String> webapps) {
        this.webapps = webapps;
    }
    
    public void markOccurrence(final String name) {
        this.markOccurrence(name, 1L);
    }
    
    public void markOccurrence(final String name, final long times) {
        CommandCounter counter = this.commands.get(name);
        if (counter == null) {
            synchronized (this.commands) {
                if (counter == null) {
                    counter = new CommandCounter(name);
                    this.commands.put(name, counter);
                }
            }
        }
        counter.mark(times);
    }
    
    public synchronized void addWebapp(final String webapp) {
        if (!this.webapps.contains(webapp)) {
            this.webapps.add(webapp);
        }
    }
    
    public boolean isTelemetryLocallyActivated() {
        return this.telemetryLocallyActivated.get();
    }
    
    public boolean setTelemetryLocallyActivated(final boolean activated) {
        return this.telemetryLocallyActivated.getAndSet(activated);
    }
    
    public void clearCommandCounts() {
        this.commands.clear();
    }
    
    public void clear() {
        this.commands.clear();
        this.licenseKey = null;
        this.applicationServer = null;
        this.webapps.clear();
    }
}
