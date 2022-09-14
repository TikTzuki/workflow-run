// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteUserCmd extends AbstractWritableIdentityServiceCmd<Void> implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    String userId;
    
    public DeleteUserCmd(final String userId) {
        this.userId = userId;
    }
    
    @Override
    protected Void executeCmd(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("userId", (Object)this.userId);
        new DeleteUserPictureCmd(this.userId).execute(commandContext);
        commandContext.getIdentityInfoManager().deleteUserInfoByUserId(this.userId);
        final IdentityOperationResult operationResult = commandContext.getWritableIdentityProvider().deleteUser(this.userId);
        commandContext.getOperationLogManager().logUserOperation(operationResult, this.userId);
        return null;
    }
}
