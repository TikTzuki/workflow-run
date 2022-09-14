// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteGroupCmd extends AbstractWritableIdentityServiceCmd<Void> implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    String groupId;
    
    public DeleteGroupCmd(final String groupId) {
        this.groupId = groupId;
    }
    
    @Override
    protected Void executeCmd(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("groupId", (Object)this.groupId);
        final IdentityOperationResult operationResult = commandContext.getWritableIdentityProvider().deleteGroup(this.groupId);
        commandContext.getOperationLogManager().logGroupOperation(operationResult, this.groupId);
        return null;
    }
}
