// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.telemetry.reporter.TelemetryReporter;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.util.TelemetryUtil;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.telemetry.TelemetryLogger;
import org.zik.bpm.engine.impl.interceptor.Command;

public class TelemetryConfigureCmd implements Command<Void>
{
    protected static final TelemetryLogger LOG;
    protected static final String TELEMETRY_PROPERTY = "camunda.telemetry.enabled";
    protected boolean telemetryEnabled;
    
    public TelemetryConfigureCmd(final boolean telemetryEnabled) {
        this.telemetryEnabled = telemetryEnabled;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final AuthorizationManager authorizationManager = commandContext.getAuthorizationManager();
        authorizationManager.checkCamundaAdminOrPermission(CommandChecker::checkConfigureTelemetry);
        commandContext.runWithoutAuthorization(() -> {
            this.toggleTelemetry(commandContext);
            return null;
        });
        return null;
    }
    
    protected void toggleTelemetry(final CommandContext commandContext) {
        final Boolean currentValue = new IsTelemetryEnabledCmd().execute(commandContext);
        new SetPropertyCmd("camunda.telemetry.enabled", Boolean.toString(this.telemetryEnabled)).execute(commandContext);
        final ProcessEngineConfigurationImpl processEngineConfiguration = commandContext.getProcessEngineConfiguration();
        final boolean isReportedActivated = processEngineConfiguration.isTelemetryReporterActivate();
        final TelemetryReporter telemetryReporter = processEngineConfiguration.getTelemetryReporter();
        if ((currentValue == null || (!currentValue && this.telemetryEnabled)) && isReportedActivated) {
            telemetryReporter.reschedule();
        }
        TelemetryUtil.toggleLocalTelemetry(this.telemetryEnabled, processEngineConfiguration.getTelemetryRegistry(), processEngineConfiguration.getMetricsRegistry());
    }
    
    static {
        LOG = ProcessEngineLogger.TELEMETRY_LOGGER;
    }
}
