// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.identity;

import org.zik.bpm.engine.query.Query;

public interface TenantQuery extends Query<TenantQuery, Tenant>
{
    TenantQuery tenantId(final String p0);
    
    TenantQuery tenantIdIn(final String... p0);
    
    TenantQuery tenantName(final String p0);
    
    TenantQuery tenantNameLike(final String p0);
    
    TenantQuery userMember(final String p0);
    
    TenantQuery groupMember(final String p0);
    
    TenantQuery includingGroupsOfUser(final boolean p0);
    
    TenantQuery orderByTenantId();
    
    TenantQuery orderByTenantName();
}
