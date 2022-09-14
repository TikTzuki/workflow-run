// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity.db;

import org.zik.bpm.engine.identity.User;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.UserQueryImpl;

public class DbUserQueryImpl extends UserQueryImpl
{
    private static final long serialVersionUID = 1L;
    
    public DbUserQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    public DbUserQueryImpl() {
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        final DbReadOnlyIdentityServiceProvider identityProvider = this.getIdentityProvider(commandContext);
        return identityProvider.findUserCountByQueryCriteria(this);
    }
    
    @Override
    public List<User> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        final DbReadOnlyIdentityServiceProvider identityProvider = this.getIdentityProvider(commandContext);
        return identityProvider.findUserByQueryCriteria(this);
    }
    
    private DbReadOnlyIdentityServiceProvider getIdentityProvider(final CommandContext commandContext) {
        return (DbReadOnlyIdentityServiceProvider)commandContext.getReadOnlyIdentityProvider();
    }
}
