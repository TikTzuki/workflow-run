// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.AuthorizationQueryImpl;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteAuthorizationCmd implements Command<Void>
{
    protected String authorizationId;
    
    public DeleteAuthorizationCmd(final String authorizationId) {
        this.authorizationId = authorizationId;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final AuthorizationManager authorizationManager = commandContext.getAuthorizationManager();
        final AuthorizationEntity authorization = ((Query<T, AuthorizationEntity>)new AuthorizationQueryImpl().authorizationId(this.authorizationId)).singleResult();
        EnsureUtil.ensureNotNull("Authorization for Id '" + this.authorizationId + "' does not exist", "authorization", authorization);
        authorizationManager.delete(authorization);
        commandContext.getOperationLogManager().logAuthorizationOperation("Delete", authorization, null);
        return null;
    }
}
