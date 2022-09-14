// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.plugin;

import org.zik.bpm.engine.impl.ProcessEngineLogger;

public class AdministratorAuthorizationPluginLogger extends ProcessEngineLogger
{
    public void grantGroupPermissions(final String administratorGroupName, final String resourceName) {
        this.logInfo("001", "GRANT group {} ALL permissions on resource {}.", new Object[] { administratorGroupName, resourceName });
    }
    
    public void grantUserPermissions(final String administratorUserName, final String resourceName) {
        this.logInfo("002", "GRANT user {} ALL permissions on resource {}.", new Object[] { administratorUserName, resourceName });
    }
}
