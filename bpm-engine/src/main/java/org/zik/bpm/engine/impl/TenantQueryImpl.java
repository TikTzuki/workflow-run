// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.identity.Tenant;
import org.zik.bpm.engine.identity.TenantQuery;

public abstract class TenantQueryImpl extends AbstractQuery<TenantQuery, Tenant> implements TenantQuery
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String[] ids;
    protected String name;
    protected String nameLike;
    protected String userId;
    protected String groupId;
    protected boolean includingGroups;
    
    public TenantQueryImpl() {
        this.includingGroups = false;
    }
    
    public TenantQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.includingGroups = false;
    }
    
    @Override
    public TenantQuery tenantId(final String id) {
        EnsureUtil.ensureNotNull("tenant ud", (Object)id);
        this.id = id;
        return this;
    }
    
    @Override
    public TenantQuery tenantIdIn(final String... ids) {
        EnsureUtil.ensureNotNull("tenant ids", (Object[])ids);
        this.ids = ids;
        return this;
    }
    
    @Override
    public TenantQuery tenantName(final String name) {
        EnsureUtil.ensureNotNull("tenant name", (Object)name);
        this.name = name;
        return this;
    }
    
    @Override
    public TenantQuery tenantNameLike(final String nameLike) {
        EnsureUtil.ensureNotNull("tenant name like", (Object)nameLike);
        this.nameLike = nameLike;
        return this;
    }
    
    @Override
    public TenantQuery userMember(final String userId) {
        EnsureUtil.ensureNotNull("user id", (Object)userId);
        this.userId = userId;
        return this;
    }
    
    @Override
    public TenantQuery groupMember(final String groupId) {
        EnsureUtil.ensureNotNull("group id", (Object)groupId);
        this.groupId = groupId;
        return this;
    }
    
    @Override
    public TenantQuery includingGroupsOfUser(final boolean includingGroups) {
        this.includingGroups = includingGroups;
        return this;
    }
    
    @Override
    public TenantQuery orderByTenantId() {
        return ((AbstractQuery<TenantQuery, U>)this).orderBy(TenantQueryProperty.GROUP_ID);
    }
    
    @Override
    public TenantQuery orderByTenantName() {
        return ((AbstractQuery<TenantQuery, U>)this).orderBy(TenantQueryProperty.NAME);
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getNameLike() {
        return this.nameLike;
    }
    
    public String[] getIds() {
        return this.ids;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public String getGroupId() {
        return this.groupId;
    }
    
    public boolean isIncludingGroups() {
        return this.includingGroups;
    }
}
