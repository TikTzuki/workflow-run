// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.telemetry.reporter.TelemetryReporter;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.telemetry.dto.TelemetryDataImpl;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetTelemetryDataCmd implements Command<TelemetryDataImpl>
{
    ProcessEngineConfigurationImpl configuration;
    
    @Override
    public TelemetryDataImpl execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkReadTelemetryData);
        this.configuration = commandContext.getProcessEngineConfiguration();
        final TelemetryReporter telemetryReporter = this.configuration.getTelemetryReporter();
        if (telemetryReporter != null) {
            return telemetryReporter.getTelemetrySendingTask().updateAndSendData(false, false);
        }
        throw ProcessEngineLogger.TELEMETRY_LOGGER.exceptionWhileRetrievingTelemetryDataRegistryNull();
    }
}
