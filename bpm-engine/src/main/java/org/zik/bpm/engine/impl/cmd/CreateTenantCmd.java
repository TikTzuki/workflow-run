// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.identity.Tenant;

public class CreateTenantCmd extends AbstractWritableIdentityServiceCmd<Tenant> implements Command<Tenant>, Serializable
{
    private static final long serialVersionUID = 1L;
    protected final String tenantId;
    
    public CreateTenantCmd(final String tenantId) {
        EnsureUtil.ensureNotNull("tenantId", (Object)tenantId);
        this.tenantId = tenantId;
    }
    
    @Override
    protected Tenant executeCmd(final CommandContext commandContext) {
        return commandContext.getWritableIdentityProvider().createNewTenant(this.tenantId);
    }
}
