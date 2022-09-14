// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.identity.IdentityOperationResult;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.identity.Tenant;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class SaveTenantCmd extends AbstractWritableIdentityServiceCmd<Void> implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected Tenant tenant;
    
    public SaveTenantCmd(final Tenant tenant) {
        this.tenant = tenant;
    }
    
    @Override
    protected Void executeCmd(final CommandContext commandContext) {
        EnsureUtil.ensureNotNull("tenant", this.tenant);
        EnsureUtil.ensureWhitelistedResourceId(commandContext, "Tenant", this.tenant.getId());
        final IdentityOperationResult operationResult = commandContext.getWritableIdentityProvider().saveTenant(this.tenant);
        commandContext.getOperationLogManager().logTenantOperation(operationResult, this.tenant.getId());
        return null;
    }
}
