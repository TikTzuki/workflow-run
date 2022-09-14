// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.persistence.entity.ResourceManager;
import org.zik.bpm.engine.impl.telemetry.dto.LicenseKeyDataImpl;
import org.zik.bpm.engine.impl.ManagementServiceImpl;
import java.nio.charset.StandardCharsets;
import org.zik.bpm.engine.impl.persistence.entity.ResourceEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SetLicenseKeyCmd extends LicenseCmd implements Command<Object>
{
    protected String licenseKey;
    
    public SetLicenseKeyCmd(final String licenseKey) {
        this.licenseKey = licenseKey;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("licenseKey", (Object)this.licenseKey);
        commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkSetLicenseKey);
        final ResourceManager resourceManager = commandContext.getResourceManager();
        ResourceEntity key = resourceManager.findLicenseKeyResource();
        if (key != null) {
            new DeleteLicenseKeyCmd(false, false).execute(commandContext);
        }
        key = new ResourceEntity();
        key.setName("camunda-license-key");
        key.setBytes(this.licenseKey.getBytes(StandardCharsets.UTF_8));
        resourceManager.insertResource(key);
        commandContext.runWithoutAuthorization((Command<Object>)new SetPropertyCmd("camunda-license-key-id", key.getId()));
        commandContext.runWithoutAuthorization((Command<Object>)new DeletePropertyCmd("camunda-license-key"));
        final ManagementServiceImpl managementService = (ManagementServiceImpl)commandContext.getProcessEngineConfiguration().getManagementService();
        final LicenseKeyDataImpl currentLicenseData = managementService.getLicenseKeyFromTelemetry();
        final LicenseKeyDataImpl licenseKeyData = LicenseKeyDataImpl.fromRawString(this.licenseKey);
        if (currentLicenseData == null || !licenseKeyData.getRaw().equals(currentLicenseData.getRaw())) {
            managementService.setLicenseKeyForTelemetry(licenseKeyData);
        }
        return null;
    }
}
