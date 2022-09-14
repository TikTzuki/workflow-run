// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry.reporter;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.HashSet;
import org.zik.bpm.engine.impl.telemetry.dto.ProductImpl;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.telemetry.TelemetryData;
import org.zik.bpm.engine.impl.metrics.util.MetricsUtil;
import org.zik.bpm.engine.impl.telemetry.dto.MetricImpl;
import org.zik.bpm.engine.impl.metrics.Meter;
import org.zik.bpm.engine.impl.telemetry.dto.CommandImpl;
import org.zik.bpm.engine.impl.telemetry.CommandCounter;
import java.util.HashMap;
import java.util.Iterator;
import org.zik.bpm.engine.telemetry.Metric;
import org.camunda.connect.spi.CloseableConnectorResponse;
import java.util.Map;
import org.zik.bpm.engine.impl.util.ConnectUtil;
import org.zik.bpm.engine.impl.util.JsonUtil;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.IsTelemetryEnabledCmd;
import org.zik.bpm.engine.impl.telemetry.dto.ApplicationServerImpl;
import org.zik.bpm.engine.impl.telemetry.dto.InternalsImpl;
import org.zik.bpm.engine.impl.util.TelemetryUtil;
import org.zik.bpm.engine.impl.metrics.MetricsRegistry;
import org.zik.bpm.engine.impl.telemetry.TelemetryRegistry;
import org.camunda.connect.spi.ConnectorRequest;
import org.camunda.connect.spi.Connector;
import org.zik.bpm.engine.impl.telemetry.dto.TelemetryDataImpl;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.telemetry.TelemetryLogger;
import java.util.Set;
import java.util.TimerTask;

public class TelemetrySendingTask extends TimerTask
{
    protected static final Set<String> METRICS_TO_REPORT;
    protected static final TelemetryLogger LOG;
    protected static final String UUID4_PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
    protected CommandExecutor commandExecutor;
    protected String telemetryEndpoint;
    protected TelemetryDataImpl staticData;
    protected Connector<? extends ConnectorRequest<?>> httpConnector;
    protected int telemetryRequestRetries;
    protected TelemetryRegistry telemetryRegistry;
    protected MetricsRegistry metricsRegistry;
    protected int telemetryRequestTimeout;
    
    public TelemetrySendingTask(final CommandExecutor commandExecutor, final String telemetryEndpoint, final int telemetryRequestRetries, final TelemetryDataImpl data, final Connector<? extends ConnectorRequest<?>> httpConnector, final TelemetryRegistry telemetryRegistry, final MetricsRegistry metricsRegistry, final int telemetryRequestTimeout) {
        this.commandExecutor = commandExecutor;
        this.telemetryEndpoint = telemetryEndpoint;
        this.telemetryRequestRetries = telemetryRequestRetries;
        this.staticData = data;
        this.httpConnector = httpConnector;
        this.telemetryRegistry = telemetryRegistry;
        this.metricsRegistry = metricsRegistry;
        this.telemetryRequestTimeout = telemetryRequestTimeout;
    }
    
    @Override
    public void run() {
        TelemetrySendingTask.LOG.startTelemetrySendingTask();
        if (!this.isTelemetryEnabled()) {
            TelemetrySendingTask.LOG.telemetryDisabled();
            return;
        }
        TelemetryUtil.toggleLocalTelemetry(true, this.telemetryRegistry, this.metricsRegistry);
        this.performDataSend(() -> this.updateAndSendData(true, true));
    }
    
    public TelemetryDataImpl updateAndSendData(final boolean sendData, final boolean addLegacyNames) {
        this.updateStaticData();
        final InternalsImpl dynamicData = this.resolveDynamicData(sendData, addLegacyNames);
        final TelemetryDataImpl mergedData = new TelemetryDataImpl(this.staticData);
        mergedData.mergeInternals(dynamicData);
        if (sendData) {
            try {
                this.sendData(mergedData);
            }
            catch (Exception e) {
                this.restoreDynamicData(dynamicData);
                throw e;
            }
        }
        return mergedData;
    }
    
    protected void updateStaticData() {
        final InternalsImpl internals = this.staticData.getProduct().getInternals();
        if (internals.getApplicationServer() == null) {
            final ApplicationServerImpl applicationServer = this.telemetryRegistry.getApplicationServer();
            internals.setApplicationServer(applicationServer);
        }
        if (internals.isTelemetryEnabled() == null) {
            internals.setTelemetryEnabled(true);
        }
        internals.setLicenseKey(this.telemetryRegistry.getLicenseKey());
        internals.setWebapps(this.telemetryRegistry.getWebapps());
    }
    
    protected boolean isTelemetryEnabled() {
        final Boolean telemetryEnabled = this.commandExecutor.execute((Command<Boolean>)new IsTelemetryEnabledCmd());
        return telemetryEnabled != null && telemetryEnabled;
    }
    
    protected void sendData(final TelemetryDataImpl dataToSend) {
        final String telemetryData = JsonUtil.asString(dataToSend);
        Map<String, Object> requestParams = ConnectUtil.assembleRequestParameters("POST", this.telemetryEndpoint, "application/json", telemetryData);
        requestParams = ConnectUtil.addRequestTimeoutConfiguration(requestParams, this.telemetryRequestTimeout);
        final ConnectorRequest<?> request = (ConnectorRequest<?>)this.httpConnector.createRequest();
        request.setRequestParameters((Map)requestParams);
        TelemetrySendingTask.LOG.sendingTelemetryData(telemetryData);
        final CloseableConnectorResponse response = (CloseableConnectorResponse)request.execute();
        if (response == null) {
            TelemetrySendingTask.LOG.unexpectedResponseWhileSendingTelemetryData();
        }
        else {
            final int responseCode = (int)response.getResponseParameter("statusCode");
            if (!this.isSuccessStatusCode(responseCode)) {
                throw TelemetrySendingTask.LOG.unexpectedResponseWhileSendingTelemetryData(responseCode);
            }
            if (responseCode != 202) {
                TelemetrySendingTask.LOG.unexpectedResponseSuccessCode(responseCode);
            }
            TelemetrySendingTask.LOG.telemetrySentSuccessfully();
        }
    }
    
    protected boolean isSuccessStatusCode(final int statusCode) {
        return statusCode / 100 == 2;
    }
    
    protected void restoreDynamicData(final InternalsImpl internals) {
        final Map<String, org.zik.bpm.engine.telemetry.Command> commands = internals.getCommands();
        for (final Map.Entry<String, org.zik.bpm.engine.telemetry.Command> entry : commands.entrySet()) {
            this.telemetryRegistry.markOccurrence(entry.getKey(), entry.getValue().getCount());
        }
        if (this.metricsRegistry != null) {
            final Map<String, Metric> metrics = internals.getMetrics();
            for (final String metricToReport : TelemetrySendingTask.METRICS_TO_REPORT) {
                final Metric metricValue = metrics.get(metricToReport);
                this.metricsRegistry.markTelemetryOccurrence(metricToReport, metricValue.getCount());
            }
        }
    }
    
    protected InternalsImpl resolveDynamicData(final boolean reset, final boolean addLegacyNames) {
        final InternalsImpl result = new InternalsImpl();
        final Map<String, Metric> metrics = this.calculateMetrics(reset, addLegacyNames);
        result.setMetrics(metrics);
        final Map<String, org.zik.bpm.engine.telemetry.Command> commands = this.fetchAndResetCommandCounts(reset);
        result.setCommands(commands);
        return result;
    }
    
    protected Map<String, org.zik.bpm.engine.telemetry.Command> fetchAndResetCommandCounts(final boolean reset) {
        final Map<String, org.zik.bpm.engine.telemetry.Command> commandsToReport = new HashMap<String, org.zik.bpm.engine.telemetry.Command>();
        final Map<String, CommandCounter> originalCounts = this.telemetryRegistry.getCommands();
        synchronized (originalCounts) {
            for (final Map.Entry<String, CommandCounter> counter : originalCounts.entrySet()) {
                final long occurrences = counter.getValue().get(reset);
                commandsToReport.put(counter.getKey(), new CommandImpl(occurrences));
            }
        }
        return commandsToReport;
    }
    
    protected Map<String, Metric> calculateMetrics(final boolean reset, final boolean addLegacyNames) {
        final Map<String, Metric> metrics = new HashMap<String, Metric>();
        if (this.metricsRegistry != null) {
            final Map<String, Meter> telemetryMeters = this.metricsRegistry.getTelemetryMeters();
            for (final String metricToReport : TelemetrySendingTask.METRICS_TO_REPORT) {
                final long value = telemetryMeters.get(metricToReport).get(reset);
                if (addLegacyNames) {
                    metrics.put(metricToReport, new MetricImpl(value));
                }
                metrics.put(MetricsUtil.resolvePublicName(metricToReport), new MetricImpl(value));
            }
        }
        return metrics;
    }
    
    protected void performDataSend(final Runnable runnable) {
        if (this.validateData(this.staticData)) {
            int triesLeft = this.telemetryRequestRetries + 1;
            boolean requestSuccessful = false;
            do {
                try {
                    --triesLeft;
                    runnable.run();
                    requestSuccessful = true;
                }
                catch (Exception e) {
                    TelemetrySendingTask.LOG.exceptionWhileSendingTelemetryData(e);
                }
            } while (!requestSuccessful && triesLeft > 0);
        }
        else {
            TelemetrySendingTask.LOG.sendingTelemetryDataFails(this.staticData);
        }
    }
    
    protected Boolean validateData(final TelemetryDataImpl dataToSend) {
        final ProductImpl product = dataToSend.getProduct();
        final String installationId = dataToSend.getInstallation();
        final String edition = product.getEdition();
        final String version = product.getVersion();
        final String name = product.getName();
        boolean validProductData = StringUtil.hasText(name) && StringUtil.hasText(version) && StringUtil.hasText(edition) && StringUtil.hasText(installationId);
        if (validProductData) {
            validProductData = (validProductData && installationId.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}"));
        }
        return validProductData;
    }
    
    static {
        METRICS_TO_REPORT = new HashSet<String>();
        LOG = ProcessEngineLogger.TELEMETRY_LOGGER;
        TelemetrySendingTask.METRICS_TO_REPORT.add("root-process-instance-start");
        TelemetrySendingTask.METRICS_TO_REPORT.add("executed-decision-instances");
        TelemetrySendingTask.METRICS_TO_REPORT.add("executed-decision-elements");
        TelemetrySendingTask.METRICS_TO_REPORT.add("activity-instance-start");
    }
}
