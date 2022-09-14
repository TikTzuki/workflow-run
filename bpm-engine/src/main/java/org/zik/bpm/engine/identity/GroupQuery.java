// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.identity;

import org.zik.bpm.engine.query.Query;

public interface GroupQuery extends Query<GroupQuery, Group>
{
    GroupQuery groupId(final String p0);
    
    GroupQuery groupIdIn(final String... p0);
    
    GroupQuery groupName(final String p0);
    
    GroupQuery groupNameLike(final String p0);
    
    GroupQuery groupType(final String p0);
    
    GroupQuery groupMember(final String p0);
    
    GroupQuery potentialStarter(final String p0);
    
    GroupQuery memberOfTenant(final String p0);
    
    GroupQuery orderByGroupId();
    
    GroupQuery orderByGroupName();
    
    GroupQuery orderByGroupType();
}
