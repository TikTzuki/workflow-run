// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.plugin;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.AuthorizationService;
import org.zik.bpm.engine.authorization.Authorization;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.ProcessEngine;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;

public class AdministratorAuthorizationPlugin extends AbstractProcessEnginePlugin
{
    private static final AdministratorAuthorizationPluginLogger LOG;
    protected String administratorGroupName;
    protected String administratorUserName;
    protected boolean authorizationEnabled;
    
    @Override
    public void postInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.authorizationEnabled = processEngineConfiguration.isAuthorizationEnabled();
        if (this.administratorGroupName != null && this.administratorGroupName.length() > 0) {
            processEngineConfiguration.getAdminGroups().add(this.administratorGroupName);
        }
        if (this.administratorUserName != null && this.administratorUserName.length() > 0) {
            processEngineConfiguration.getAdminUsers().add(this.administratorUserName);
        }
    }
    
    @Override
    public void postProcessEngineBuild(final ProcessEngine processEngine) {
        if (!this.authorizationEnabled) {
            return;
        }
        final AuthorizationService authorizationService = processEngine.getAuthorizationService();
        if (this.administratorGroupName != null && this.administratorGroupName.length() > 0) {
            for (final Resource resource : Resources.values()) {
                if (authorizationService.createAuthorizationQuery().groupIdIn(this.administratorGroupName).resourceType(resource).resourceId("*").count() == 0L) {
                    final AuthorizationEntity adminGroupAuth = new AuthorizationEntity(1);
                    adminGroupAuth.setGroupId(this.administratorGroupName);
                    adminGroupAuth.setResource(resource);
                    adminGroupAuth.setResourceId("*");
                    adminGroupAuth.addPermission(Permissions.ALL);
                    authorizationService.saveAuthorization(adminGroupAuth);
                    AdministratorAuthorizationPlugin.LOG.grantGroupPermissions(this.administratorGroupName, resource.resourceName());
                }
            }
        }
        if (this.administratorUserName != null && this.administratorUserName.length() > 0) {
            for (final Resource resource : Resources.values()) {
                if (authorizationService.createAuthorizationQuery().userIdIn(this.administratorUserName).resourceType(resource).resourceId("*").count() == 0L) {
                    final AuthorizationEntity adminUserAuth = new AuthorizationEntity(1);
                    adminUserAuth.setUserId(this.administratorUserName);
                    adminUserAuth.setResource(resource);
                    adminUserAuth.setResourceId("*");
                    adminUserAuth.addPermission(Permissions.ALL);
                    authorizationService.saveAuthorization(adminUserAuth);
                    AdministratorAuthorizationPlugin.LOG.grantUserPermissions(this.administratorUserName, resource.resourceName());
                }
            }
        }
    }
    
    public String getAdministratorGroupName() {
        return this.administratorGroupName;
    }
    
    public void setAdministratorGroupName(final String administratorGroupName) {
        this.administratorGroupName = administratorGroupName;
    }
    
    public String getAdministratorUserName() {
        return this.administratorUserName;
    }
    
    public void setAdministratorUserName(final String administratorUserName) {
        this.administratorUserName = administratorUserName;
    }
    
    static {
        LOG = ProcessEngineLogger.ADMIN_PLUGIN_LOGGER;
    }
}
