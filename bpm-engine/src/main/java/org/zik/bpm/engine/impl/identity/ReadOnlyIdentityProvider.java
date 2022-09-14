// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import org.zik.bpm.engine.identity.TenantQuery;
import org.zik.bpm.engine.identity.Tenant;
import org.zik.bpm.engine.identity.GroupQuery;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.identity.NativeUserQuery;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.identity.UserQuery;
import org.zik.bpm.engine.identity.User;
import org.zik.bpm.engine.impl.interceptor.Session;

public interface ReadOnlyIdentityProvider extends Session
{
    User findUserById(final String p0);
    
    UserQuery createUserQuery();
    
    UserQuery createUserQuery(final CommandContext p0);
    
    NativeUserQuery createNativeUserQuery();
    
    boolean checkPassword(final String p0, final String p1);
    
    Group findGroupById(final String p0);
    
    GroupQuery createGroupQuery();
    
    GroupQuery createGroupQuery(final CommandContext p0);
    
    Tenant findTenantById(final String p0);
    
    TenantQuery createTenantQuery();
    
    TenantQuery createTenantQuery(final CommandContext p0);
}
