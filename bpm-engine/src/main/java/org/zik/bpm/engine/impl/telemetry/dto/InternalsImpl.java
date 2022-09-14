// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.dto;

import org.zik.bpm.engine.telemetry.Database;
import org.zik.bpm.engine.telemetry.ApplicationServer;
import org.zik.bpm.engine.telemetry.LicenseKeyData;
import org.zik.bpm.engine.telemetry.Jdk;
import java.util.Collection;
import java.util.HashSet;
import java.util.HashMap;
import org.zik.bpm.engine.telemetry.Metric;
import java.util.Set;
import org.zik.bpm.engine.telemetry.Command;
import java.util.Map;
import com.google.gson.annotations.SerializedName;
import org.zik.bpm.engine.telemetry.Internals;

public class InternalsImpl implements Internals
{
    public static final String SERIALIZED_APPLICATION_SERVER = "application-server";
    public static final String SERIALIZED_CAMUNDA_INTEGRATION = "camunda-integration";
    public static final String SERIALIZED_LICENSE_KEY = "license-key";
    public static final String SERIALIZED_TELEMETRY_ENABLED = "telemetry-enabled";
    protected DatabaseImpl database;
    @SerializedName("application-server")
    protected ApplicationServerImpl applicationServer;
    @SerializedName("license-key")
    protected LicenseKeyDataImpl licenseKey;
    protected Map<String, Command> commands;
    @SerializedName("camunda-integration")
    protected Set<String> camundaIntegration;
    protected Map<String, Metric> metrics;
    protected Set<String> webapps;
    protected JdkImpl jdk;
    @SerializedName("telemetry-enabled")
    protected Boolean telemetryEnabled;
    
    public InternalsImpl() {
        this(null, null, null, null);
    }
    
    public InternalsImpl(final DatabaseImpl database, final ApplicationServerImpl server, final LicenseKeyDataImpl licenseKey, final JdkImpl jdk) {
        this.database = database;
        this.applicationServer = server;
        this.licenseKey = licenseKey;
        this.commands = new HashMap<String, Command>();
        this.jdk = jdk;
        this.camundaIntegration = new HashSet<String>();
    }
    
    public InternalsImpl(final InternalsImpl internals) {
        this(internals.database, internals.applicationServer, internals.licenseKey, internals.jdk);
        this.camundaIntegration = ((internals.camundaIntegration == null) ? null : new HashSet<String>(internals.getCamundaIntegration()));
        this.commands = new HashMap<String, Command>(internals.getCommands());
        this.metrics = ((internals.metrics == null) ? null : new HashMap<String, Metric>(internals.getMetrics()));
        this.telemetryEnabled = internals.telemetryEnabled;
        this.webapps = internals.webapps;
    }
    
    @Override
    public DatabaseImpl getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final DatabaseImpl database) {
        this.database = database;
    }
    
    @Override
    public ApplicationServerImpl getApplicationServer() {
        return this.applicationServer;
    }
    
    public void setApplicationServer(final ApplicationServerImpl applicationServer) {
        this.applicationServer = applicationServer;
    }
    
    @Override
    public Map<String, Command> getCommands() {
        return this.commands;
    }
    
    public void setCommands(final Map<String, Command> commands) {
        this.commands = commands;
    }
    
    public void putCommand(final String commandName, final int count) {
        if (this.commands == null) {
            this.commands = new HashMap<String, Command>();
        }
        this.commands.put(commandName, new CommandImpl(count));
    }
    
    @Override
    public Map<String, Metric> getMetrics() {
        return this.metrics;
    }
    
    public void setMetrics(final Map<String, Metric> metrics) {
        this.metrics = metrics;
    }
    
    public void putMetric(final String metricName, final int count) {
        if (this.metrics == null) {
            this.metrics = new HashMap<String, Metric>();
        }
        this.metrics.put(metricName, new MetricImpl(count));
    }
    
    public void mergeDynamicData(final InternalsImpl other) {
        this.commands = other.commands;
        this.metrics = other.metrics;
    }
    
    @Override
    public JdkImpl getJdk() {
        return this.jdk;
    }
    
    public void setJdk(final JdkImpl jdk) {
        this.jdk = jdk;
    }
    
    @Override
    public Set<String> getCamundaIntegration() {
        return this.camundaIntegration;
    }
    
    public void setCamundaIntegration(final Set<String> camundaIntegration) {
        this.camundaIntegration = camundaIntegration;
    }
    
    @Override
    public LicenseKeyDataImpl getLicenseKey() {
        return this.licenseKey;
    }
    
    public void setLicenseKey(final LicenseKeyDataImpl licenseKey) {
        this.licenseKey = licenseKey;
    }
    
    public Boolean isTelemetryEnabled() {
        return this.telemetryEnabled;
    }
    
    public void setTelemetryEnabled(final Boolean telemetryEnabled) {
        this.telemetryEnabled = telemetryEnabled;
    }
    
    @Override
    public Set<String> getWebapps() {
        return this.webapps;
    }
    
    public void setWebapps(final Set<String> webapps) {
        this.webapps = webapps;
    }
}
