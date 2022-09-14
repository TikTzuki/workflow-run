// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyManager;
import org.zik.bpm.engine.impl.persistence.entity.ResourceManager;
import org.zik.bpm.engine.impl.telemetry.dto.LicenseKeyDataImpl;
import org.zik.bpm.engine.impl.ManagementServiceImpl;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteLicenseKeyCmd extends LicenseCmd implements Command<Object>
{
    boolean deleteProperty;
    boolean updateTelemetry;
    
    public DeleteLicenseKeyCmd(final boolean deleteProperty) {
        this(deleteProperty, true);
    }
    
    public DeleteLicenseKeyCmd(final boolean deleteProperty, final boolean updateTelemetry) {
        this.deleteProperty = deleteProperty;
        this.updateTelemetry = updateTelemetry;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkDeleteLicenseKey);
        final ResourceManager resourceManager = commandContext.getResourceManager();
        final PropertyManager propertyManager = commandContext.getPropertyManager();
        final PropertyEntity licenseProperty = propertyManager.findPropertyById("camunda-license-key-id");
        final ResourceEntity licenseKey = resourceManager.findLicenseKeyResource();
        if (licenseKey != null) {
            resourceManager.delete(licenseKey);
        }
        new DeletePropertyCmd("camunda-license-key").execute(commandContext);
        if (this.deleteProperty) {
            new DeletePropertyCmd("camunda-license-key-id").execute(commandContext);
        }
        if (this.updateTelemetry) {
            ((ManagementServiceImpl)commandContext.getProcessEngineConfiguration().getManagementService()).setLicenseKeyForTelemetry(null);
        }
        return null;
    }
}
