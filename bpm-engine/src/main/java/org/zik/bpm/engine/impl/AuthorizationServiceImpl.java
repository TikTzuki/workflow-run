// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cmd.AuthorizationCheckCmd;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import java.util.List;
import org.zik.bpm.engine.impl.cmd.DeleteAuthorizationCmd;
import org.zik.bpm.engine.impl.cmd.SaveAuthorizationCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.CreateAuthorizationCommand;
import org.zik.bpm.engine.authorization.Authorization;
import org.zik.bpm.engine.authorization.AuthorizationQuery;
import org.zik.bpm.engine.AuthorizationService;

public class AuthorizationServiceImpl extends ServiceImpl implements AuthorizationService
{
    @Override
    public AuthorizationQuery createAuthorizationQuery() {
        return new AuthorizationQueryImpl(this.commandExecutor);
    }
    
    @Override
    public Authorization createNewAuthorization(final int type) {
        return this.commandExecutor.execute((Command<Authorization>)new CreateAuthorizationCommand(type));
    }
    
    @Override
    public Authorization saveAuthorization(final Authorization authorization) {
        return this.commandExecutor.execute((Command<Authorization>)new SaveAuthorizationCmd(authorization));
    }
    
    @Override
    public void deleteAuthorization(final String authorizationId) {
        this.commandExecutor.execute((Command<Object>)new DeleteAuthorizationCmd(authorizationId));
    }
    
    @Override
    public boolean isUserAuthorized(final String userId, final List<String> groupIds, final Permission permission, final Resource resource) {
        return this.commandExecutor.execute((Command<Boolean>)new AuthorizationCheckCmd(userId, groupIds, permission, resource, null));
    }
    
    @Override
    public boolean isUserAuthorized(final String userId, final List<String> groupIds, final Permission permission, final Resource resource, final String resourceId) {
        return this.commandExecutor.execute((Command<Boolean>)new AuthorizationCheckCmd(userId, groupIds, permission, resource, resourceId));
    }
}
