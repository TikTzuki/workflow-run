// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.identity;

import org.zik.bpm.engine.query.Query;

public interface UserQuery extends Query<UserQuery, User>
{
    UserQuery userId(final String p0);
    
    UserQuery userIdIn(final String... p0);
    
    UserQuery userFirstName(final String p0);
    
    UserQuery userFirstNameLike(final String p0);
    
    UserQuery userLastName(final String p0);
    
    UserQuery userLastNameLike(final String p0);
    
    UserQuery userEmail(final String p0);
    
    UserQuery userEmailLike(final String p0);
    
    UserQuery memberOfGroup(final String p0);
    
    UserQuery potentialStarter(final String p0);
    
    UserQuery memberOfTenant(final String p0);
    
    UserQuery orderByUserId();
    
    UserQuery orderByUserFirstName();
    
    UserQuery orderByUserLastName();
    
    UserQuery orderByUserEmail();
}
