// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity.db;

import org.zik.bpm.engine.identity.Group;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.GroupQueryImpl;

public class DbGroupQueryImpl extends GroupQueryImpl
{
    private static final long serialVersionUID = 1L;
    
    public DbGroupQueryImpl() {
    }
    
    public DbGroupQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        final DbReadOnlyIdentityServiceProvider identityProvider = this.getIdentityProvider(commandContext);
        return identityProvider.findGroupCountByQueryCriteria(this);
    }
    
    @Override
    public List<Group> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        final DbReadOnlyIdentityServiceProvider identityProvider = this.getIdentityProvider(commandContext);
        return identityProvider.findGroupByQueryCriteria(this);
    }
    
    protected DbReadOnlyIdentityServiceProvider getIdentityProvider(final CommandContext commandContext) {
        return (DbReadOnlyIdentityServiceProvider)commandContext.getReadOnlyIdentityProvider();
    }
}
