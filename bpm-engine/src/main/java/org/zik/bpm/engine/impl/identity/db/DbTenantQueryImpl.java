// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity.db;

import org.zik.bpm.engine.identity.Tenant;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.TenantQueryImpl;

public class DbTenantQueryImpl extends TenantQueryImpl
{
    private static final long serialVersionUID = 1L;
    
    public DbTenantQueryImpl() {
    }
    
    public DbTenantQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        final DbReadOnlyIdentityServiceProvider identityProvider = this.getIdentityProvider(commandContext);
        return identityProvider.findTenantCountByQueryCriteria(this);
    }
    
    @Override
    public List<Tenant> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        final DbReadOnlyIdentityServiceProvider identityProvider = this.getIdentityProvider(commandContext);
        return identityProvider.findTenantByQueryCriteria(this);
    }
    
    protected DbReadOnlyIdentityServiceProvider getIdentityProvider(final CommandContext commandContext) {
        return (DbReadOnlyIdentityServiceProvider)commandContext.getReadOnlyIdentityProvider();
    }
}
