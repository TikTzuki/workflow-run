// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteTenantCmd extends AbstractWritableIdentityServiceCmd<Void> implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final String tenantId;
    
    public DeleteTenantCmd(final String tenantId) {
        EnsureUtil.ensureNotNull("tenantId", (Object)tenantId);
        this.tenantId = tenantId;
    }
    
    @Override
    protected Void executeCmd(final CommandContext commandContext) {
        final IdentityOperationResult operationResult = commandContext.getWritableIdentityProvider().deleteTenant(this.tenantId);
        commandContext.getOperationLogManager().logTenantOperation(operationResult, this.tenantId);
        return null;
    }
}
