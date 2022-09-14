// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteMembershipCmd extends AbstractWritableIdentityServiceCmd<Void> implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    String userId;
    String groupId;
    
    public DeleteMembershipCmd(final String userId, final String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
    
    @Override
    protected Void executeCmd(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("userId", (Object)this.userId);
        EnsureUtil.ensureNotNull("groupId", (Object)this.groupId);
        final IdentityOperationResult operationResult = commandContext.getWritableIdentityProvider().deleteMembership(this.userId, this.groupId);
        commandContext.getOperationLogManager().logMembershipOperation(operationResult, this.userId, this.groupId, null);
        return null;
    }
}
