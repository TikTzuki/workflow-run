// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.identity.db.DbReadOnlyIdentityServiceProvider;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.identity.User;
import org.zik.bpm.engine.identity.NativeUserQuery;

public class NativeUserQueryImpl extends AbstractNativeQuery<NativeUserQuery, User> implements NativeUserQuery
{
    private static final long serialVersionUID = 1L;
    
    public NativeUserQueryImpl(final CommandContext commandContext) {
        super(commandContext);
    }
    
    public NativeUserQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public List<User> executeList(final CommandContext commandContext, final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        final DbReadOnlyIdentityServiceProvider identityProvider = this.getIdentityProvider(commandContext);
        return identityProvider.findUserByNativeQuery(parameterMap, firstResult, maxResults);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext, final Map<String, Object> parameterMap) {
        final DbReadOnlyIdentityServiceProvider identityProvider = this.getIdentityProvider(commandContext);
        return identityProvider.findUserCountByNativeQuery(parameterMap);
    }
    
    private DbReadOnlyIdentityServiceProvider getIdentityProvider(final CommandContext commandContext) {
        return (DbReadOnlyIdentityServiceProvider)commandContext.getReadOnlyIdentityProvider();
    }
}
