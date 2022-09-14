// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.reporter;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.IsTelemetryEnabledCmd;
import java.util.TimerTask;
import org.zik.bpm.engine.impl.metrics.MetricsRegistry;
import org.zik.bpm.engine.impl.telemetry.TelemetryRegistry;
import org.camunda.connect.spi.ConnectorRequest;
import org.camunda.connect.spi.Connector;
import org.zik.bpm.engine.impl.telemetry.dto.TelemetryDataImpl;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Timer;
import org.zik.bpm.engine.impl.telemetry.TelemetryLogger;

public class TelemetryReporter
{
    protected static final TelemetryLogger LOG;
    protected long reportingIntervalInSeconds;
    public static long DEFAULT_INIT_REPORT_DELAY_SECONDS;
    public static long EXTENDED_INIT_REPORT_DELAY_SECONDS;
    protected TelemetrySendingTask telemetrySendingTask;
    protected Timer timer;
    protected CommandExecutor commandExecutor;
    protected String telemetryEndpoint;
    protected int telemetryRequestRetries;
    protected TelemetryDataImpl data;
    protected Connector<? extends ConnectorRequest<?>> httpConnector;
    protected TelemetryRegistry telemetryRegistry;
    protected MetricsRegistry metricsRegistry;
    protected int telemetryRequestTimeout;
    
    public TelemetryReporter(final CommandExecutor commandExecutor, final String telemetryEndpoint, final int telemetryRequestRetries, final long telemetryReportingPeriod, final TelemetryDataImpl data, final Connector<? extends ConnectorRequest<?>> httpConnector, final TelemetryRegistry telemetryRegistry, final MetricsRegistry metricsRegistry, final int telemetryRequestTimeout) {
        this.commandExecutor = commandExecutor;
        this.telemetryEndpoint = telemetryEndpoint;
        this.telemetryRequestRetries = telemetryRequestRetries;
        this.reportingIntervalInSeconds = telemetryReportingPeriod;
        this.data = data;
        this.httpConnector = httpConnector;
        this.telemetryRegistry = telemetryRegistry;
        this.metricsRegistry = metricsRegistry;
        this.telemetryRequestTimeout = telemetryRequestTimeout;
        this.initTelemetrySendingTask();
    }
    
    protected void initTelemetrySendingTask() {
        this.telemetrySendingTask = new TelemetrySendingTask(this.commandExecutor, this.telemetryEndpoint, this.telemetryRequestRetries, this.data, this.httpConnector, this.telemetryRegistry, this.metricsRegistry, this.telemetryRequestTimeout);
    }
    
    public synchronized void start() {
        if (!this.isScheduled()) {
            this.initTelemetrySendingTask();
            this.timer = new Timer("Camunda BPM Runtime Telemetry Reporter", true);
            final long reportingIntervalInMillis = this.reportingIntervalInSeconds * 1000L;
            final long initialReportingDelay = this.getInitialReportingDelaySeconds() * 1000L;
            try {
                this.timer.scheduleAtFixedRate(this.telemetrySendingTask, initialReportingDelay, reportingIntervalInMillis);
            }
            catch (Exception e) {
                this.timer = null;
                throw TelemetryReporter.LOG.schedulingTaskFails(e);
            }
        }
    }
    
    public synchronized void reschedule() {
        this.stop(false);
        this.start();
    }
    
    public synchronized void stop() {
        this.stop(true);
    }
    
    public synchronized void stop(final boolean report) {
        if (this.isScheduled()) {
            this.timer.cancel();
            this.timer = null;
            if (report) {
                this.reportNow();
            }
        }
    }
    
    public void reportNow() {
        if (this.telemetrySendingTask != null) {
            this.telemetrySendingTask.run();
        }
    }
    
    public boolean isScheduled() {
        return this.timer != null;
    }
    
    public long getReportingIntervalInSeconds() {
        return this.reportingIntervalInSeconds;
    }
    
    public TelemetrySendingTask getTelemetrySendingTask() {
        return this.telemetrySendingTask;
    }
    
    public void setTelemetrySendingTask(final TelemetrySendingTask telemetrySendingTask) {
        this.telemetrySendingTask = telemetrySendingTask;
    }
    
    public String getTelemetryEndpoint() {
        return this.telemetryEndpoint;
    }
    
    public Connector<? extends ConnectorRequest<?>> getHttpConnector() {
        return this.httpConnector;
    }
    
    public long getInitialReportingDelaySeconds() {
        final Boolean enabled = this.commandExecutor.execute((Command<Boolean>)new IsTelemetryEnabledCmd());
        return (enabled == null) ? TelemetryReporter.EXTENDED_INIT_REPORT_DELAY_SECONDS : TelemetryReporter.DEFAULT_INIT_REPORT_DELAY_SECONDS;
    }
    
    static {
        LOG = ProcessEngineLogger.TELEMETRY_LOGGER;
        TelemetryReporter.DEFAULT_INIT_REPORT_DELAY_SECONDS = 300L;
        TelemetryReporter.EXTENDED_INIT_REPORT_DELAY_SECONDS = 10800L;
    }
}
