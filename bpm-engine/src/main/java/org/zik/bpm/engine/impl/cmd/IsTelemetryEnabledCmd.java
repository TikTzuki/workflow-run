// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.persistence.entity.PropertyEntity;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.telemetry.TelemetryLogger;
import org.zik.bpm.engine.impl.interceptor.Command;

public class IsTelemetryEnabledCmd implements Command<Boolean>
{
    protected static final TelemetryLogger LOG;
    
    @Override
    public Boolean execute(final CommandContext commandContext) {
        final AuthorizationManager authorizationManager = commandContext.getAuthorizationManager();
        authorizationManager.checkCamundaAdminOrPermission(CommandChecker::checkReadTelemetryCollectionStatusData);
        final PropertyEntity telemetryProperty = commandContext.getPropertyManager().findPropertyById("camunda.telemetry.enabled");
        if (telemetryProperty == null) {
            IsTelemetryEnabledCmd.LOG.databaseTelemetryPropertyMissingInfo();
            return null;
        }
        if (telemetryProperty.getValue().toLowerCase().equals("null")) {
            return null;
        }
        return Boolean.parseBoolean(telemetryProperty.getValue());
    }
    
    static {
        LOG = ProcessEngineLogger.TELEMETRY_LOGGER;
    }
}
