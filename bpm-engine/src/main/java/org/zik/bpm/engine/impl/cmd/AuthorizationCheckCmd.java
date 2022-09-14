// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Objects;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import java.util.List;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.interceptor.Command;

public class AuthorizationCheckCmd implements Command<Boolean>
{
    protected static final EnginePersistenceLogger LOG;
    protected String userId;
    protected List<String> groupIds;
    protected Permission permission;
    protected Resource resource;
    protected String resourceId;
    
    public AuthorizationCheckCmd(final String userId, final List<String> groupIds, final Permission permission, final Resource resource, final String resourceId) {
        this.userId = userId;
        this.groupIds = groupIds;
        this.permission = permission;
        this.resource = resource;
        this.resourceId = resourceId;
        this.validate(userId, groupIds, permission, resource);
    }
    
    @Override
    public Boolean execute(final CommandContext commandContext) {
        final AuthorizationManager authorizationManager = commandContext.getAuthorizationManager();
        if (authorizationManager.isPermissionDisabled(this.permission)) {
            throw AuthorizationCheckCmd.LOG.disabledPermissionException(this.permission.getName());
        }
        if (this.isHistoricInstancePermissionsDisabled(commandContext) && this.isHistoricInstanceResource()) {
            throw AuthorizationCheckCmd.LOG.disabledHistoricInstancePermissionsException();
        }
        return authorizationManager.isAuthorized(this.userId, this.groupIds, this.permission, this.resource, this.resourceId);
    }
    
    protected void validate(final String userId, final List<String> groupIds, final Permission permission, final Resource resource) {
        EnsureUtil.ensureAtLeastOneNotNull("Authorization must have a 'userId' or/and a 'groupId'.", userId, groupIds);
        EnsureUtil.ensureNotNull("Invalid permission for an authorization", "authorization.getResource()", permission);
        EnsureUtil.ensureNotNull("Invalid resource for an authorization", "authorization.getResource()", resource);
    }
    
    protected boolean isHistoricInstancePermissionsDisabled(final CommandContext commandContext) {
        return !commandContext.getProcessEngineConfiguration().isEnableHistoricInstancePermissions();
    }
    
    protected boolean isHistoricInstanceResource() {
        return Objects.equals(Resources.HISTORIC_TASK, this.resource) || Objects.equals(Resources.HISTORIC_PROCESS_INSTANCE, this.resource);
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
