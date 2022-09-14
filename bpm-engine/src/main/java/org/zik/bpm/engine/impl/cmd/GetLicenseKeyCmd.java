// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.PropertyEntity;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import java.nio.charset.StandardCharsets;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class GetLicenseKeyCmd extends LicenseCmd implements Command<String>
{
    @Override
    public String execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkReadLicenseKey);
        final ResourceEntity licenseResource = commandContext.getResourceManager().findLicenseKeyResource();
        if (licenseResource != null) {
            return new String(licenseResource.getBytes(), StandardCharsets.UTF_8);
        }
        final PropertyEntity licenseProperty = commandContext.getPropertyManager().findPropertyById("camunda-license-key");
        if (licenseProperty != null) {
            return licenseProperty.getValue();
        }
        return null;
    }
}
