// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.telemetry;

import org.zik.bpm.engine.telemetry.Product;
import org.zik.bpm.engine.telemetry.TelemetryData;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class TelemetryLogger extends ProcessEngineLogger
{
    public void startTelemetrySendingTask() {
        this.logDebug("001", "Start telemetry sending task.", new Object[0]);
    }
    
    public void exceptionWhileSendingTelemetryData(final Exception e) {
        this.logWarn("002", "Could not send telemetry data. Reason: {} with message '{}'. Set this logger to DEBUG/FINE for the full stacktrace.", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        this.logDebug("003", "{} occurred while sending telemetry data.", new Object[] { e.getClass().getCanonicalName(), e });
    }
    
    public ProcessEngineException unexpectedResponseWhileSendingTelemetryData(final int responseCode) {
        return new ProcessEngineException(this.exceptionMessage("004", "Unexpected response code {} when sending telemetry data", new Object[] { responseCode }));
    }
    
    public void unexpectedResponseWhileSendingTelemetryData() {
        this.logDebug("005", "Unexpected 'null' response while sending telemetry data.", new Object[0]);
    }
    
    public void sendingTelemetryData(final String data) {
        this.logDebug("006", "Sending telemetry data: {}", new Object[] { data });
    }
    
    public void databaseTelemetryPropertyMissingInfo(final boolean telemetryEnabled) {
        this.logInfo("007", "`camunda.telemetry.enabled` property is missing in the database, creating the property with value: {}", new Object[] { Boolean.toString(telemetryEnabled) });
    }
    
    public void databaseTelemetryPropertyMissingInfo() {
        this.logInfo("008", "`camunda.telemetry.enabled` property is missing in the database", new Object[0]);
    }
    
    public void telemetryDisabled() {
        this.logDebug("009", "Sending telemetry is disabled.", new Object[0]);
    }
    
    public ProcessEngineException schedulingTaskFails(final Exception e) {
        return new ProcessEngineException(this.exceptionMessage("010", "Cannot schedule the telemetry task.", new Object[0]), e);
    }
    
    public void schedulingTaskFailsOnEngineStart(final Exception e) {
        this.logWarn("013", "Could not start telemetry task. Reason: {} with message '{}'. Set this logger to DEBUG/FINE for the full stacktrace.", new Object[] { e.getClass().getSimpleName(), e.getMessage() });
        this.logDebug("014", "{} occurred while starting the telemetry task.", new Object[] { e.getClass().getCanonicalName(), e });
    }
    
    public void unableToConfigureHttpConnectorWarning() {
        this.logWarn("011", "The http connector used to send telemetry is `null`, telemetry data will not be sent.", new Object[0]);
    }
    
    public void unexpectedExceptionDuringHttpConnectorConfiguration(final Exception e) {
        this.logDebug("012", "'{}' exception occurred while configuring http connector with message: {}", new Object[] { e.getClass().getCanonicalName(), e.getMessage() });
    }
    
    public void unexpectedResponseSuccessCode(final int statusCode) {
        this.logDebug("015", "Telemetry request was sent, but received an unexpected response success code: {}", new Object[] { statusCode });
    }
    
    public void telemetrySentSuccessfully() {
        this.logDebug("016", "Telemetry request was successful.", new Object[0]);
    }
    
    public void sendingTelemetryDataFails(final TelemetryData productData) {
        final Product product = productData.getProduct();
        final String installationId = productData.getInstallation();
        this.logWarn("017", "Cannot send the telemetry data. Some of the data is invalid. Set this logger to DEBUG/FINE to see more details.", new Object[0]);
        this.logDebug("018", "Cannot send the telemetry task data. The following values must be non-empty Strings: '{}' (name), '{}' (version), '{}' (edition), '{}' (UUIDv4 installation id).", new Object[] { product.getName(), product.getVersion(), product.getEdition(), installationId });
    }
    
    public ProcessEngineException exceptionWhileRetrievingTelemetryDataRegistryNull() {
        return new ProcessEngineException(this.exceptionMessage("019", "Error while retrieving telemetry data. Telemetry registry was not initialized.", new Object[0]));
    }
}
