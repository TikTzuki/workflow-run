// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class UnlockUserCmd implements Command<Object>, Serializable
{
    private static final long serialVersionUID = 1L;
    String userId;
    
    public UnlockUserCmd(final String userId) {
        this.userId = userId;
    }
    
    @Override
    public Object execute(final CommandContext commandContext) {
        commandContext.getAuthorizationManager().checkCamundaAdmin();
        final IdentityOperationResult operationResult = commandContext.getWritableIdentityProvider().unlockUser(this.userId);
        commandContext.getOperationLogManager().logUserOperation(operationResult, this.userId);
        return null;
    }
}
