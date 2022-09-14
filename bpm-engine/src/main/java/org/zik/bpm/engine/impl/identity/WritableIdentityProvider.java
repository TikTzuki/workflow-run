// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.identity;

import org.zik.bpm.engine.identity.Tenant;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.identity.User;
import org.zik.bpm.engine.impl.interceptor.Session;

public interface WritableIdentityProvider extends Session
{
    User createNewUser(final String p0);
    
    IdentityOperationResult saveUser(final User p0);
    
    IdentityOperationResult deleteUser(final String p0);
    
    IdentityOperationResult unlockUser(final String p0);
    
    Group createNewGroup(final String p0);
    
    IdentityOperationResult saveGroup(final Group p0);
    
    IdentityOperationResult deleteGroup(final String p0);
    
    Tenant createNewTenant(final String p0);
    
    IdentityOperationResult saveTenant(final Tenant p0);
    
    IdentityOperationResult deleteTenant(final String p0);
    
    IdentityOperationResult createMembership(final String p0, final String p1);
    
    IdentityOperationResult deleteMembership(final String p0, final String p1);
    
    IdentityOperationResult createTenantUserMembership(final String p0, final String p1);
    
    IdentityOperationResult createTenantGroupMembership(final String p0, final String p1);
    
    IdentityOperationResult deleteTenantUserMembership(final String p0, final String p1);
    
    IdentityOperationResult deleteTenantGroupMembership(final String p0, final String p1);
}
