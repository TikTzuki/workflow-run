// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class CreateTenantUserMembershipCmd extends AbstractWritableIdentityServiceCmd<Void> implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final String tenantId;
    protected final String userId;
    
    public CreateTenantUserMembershipCmd(final String tenantId, final String userId) {
        this.tenantId = tenantId;
        this.userId = userId;
    }
    
    @Override
    protected Void executeCmd(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("tenantId", (Object)this.tenantId);
        EnsureUtil.ensureNotNull("userId", (Object)this.userId);
        final IdentityOperationResult operationResult = commandContext.getWritableIdentityProvider().createTenantUserMembership(this.tenantId, this.userId);
        commandContext.getOperationLogManager().logMembershipOperation(operationResult, this.userId, null, this.tenantId);
        return null;
    }
}
