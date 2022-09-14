// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.authorization;

import org.zik.bpm.engine.query.Query;

public interface AuthorizationQuery extends Query<AuthorizationQuery, Authorization>
{
    AuthorizationQuery authorizationId(final String p0);
    
    AuthorizationQuery authorizationType(final Integer p0);
    
    AuthorizationQuery userIdIn(final String... p0);
    
    AuthorizationQuery groupIdIn(final String... p0);
    
    AuthorizationQuery resourceType(final Resource p0);
    
    AuthorizationQuery resourceType(final int p0);
    
    AuthorizationQuery resourceId(final String p0);
    
    AuthorizationQuery hasPermission(final Permission p0);
    
    AuthorizationQuery orderByResourceType();
    
    AuthorizationQuery orderByResourceId();
}
