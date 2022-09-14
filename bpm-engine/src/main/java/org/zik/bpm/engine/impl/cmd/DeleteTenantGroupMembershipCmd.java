// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class DeleteTenantGroupMembershipCmd extends AbstractWritableIdentityServiceCmd<Void> implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final String tenantId;
    protected final String groupId;
    
    public DeleteTenantGroupMembershipCmd(final String tenantId, final String groupId) {
        this.tenantId = tenantId;
        this.groupId = groupId;
    }
    
    @Override
    protected Void executeCmd(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("tenantId", (Object)this.tenantId);
        EnsureUtil.ensureNotNull("groupId", (Object)this.groupId);
        final IdentityOperationResult operationResult = commandContext.getWritableIdentityProvider().deleteTenantGroupMembership(this.tenantId, this.groupId);
        commandContext.getOperationLogManager().logMembershipOperation(operationResult, null, this.groupId, this.tenantId);
        return null;
    }
}
