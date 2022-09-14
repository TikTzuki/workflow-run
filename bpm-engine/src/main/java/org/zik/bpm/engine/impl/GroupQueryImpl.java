// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.identity.Group;
import org.zik.bpm.engine.identity.GroupQuery;

public abstract class GroupQueryImpl extends AbstractQuery<GroupQuery, Group> implements GroupQuery
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String[] ids;
    protected String name;
    protected String nameLike;
    protected String type;
    protected String userId;
    protected String procDefId;
    protected String tenantId;
    
    public GroupQueryImpl() {
    }
    
    public GroupQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public GroupQuery groupId(final String id) {
        EnsureUtil.ensureNotNull("Provided id", (Object)id);
        this.id = id;
        return this;
    }
    
    @Override
    public GroupQuery groupIdIn(final String... ids) {
        EnsureUtil.ensureNotNull("Provided ids", (Object[])ids);
        this.ids = ids;
        return this;
    }
    
    @Override
    public GroupQuery groupName(final String name) {
        EnsureUtil.ensureNotNull("Provided name", (Object)name);
        this.name = name;
        return this;
    }
    
    @Override
    public GroupQuery groupNameLike(final String nameLike) {
        EnsureUtil.ensureNotNull("Provided nameLike", (Object)nameLike);
        this.nameLike = nameLike;
        return this;
    }
    
    @Override
    public GroupQuery groupType(final String type) {
        EnsureUtil.ensureNotNull("Provided type", (Object)type);
        this.type = type;
        return this;
    }
    
    @Override
    public GroupQuery groupMember(final String userId) {
        EnsureUtil.ensureNotNull("Provided userId", (Object)userId);
        this.userId = userId;
        return this;
    }
    
    @Override
    public GroupQuery potentialStarter(final String procDefId) {
        EnsureUtil.ensureNotNull("Provided processDefinitionId", (Object)procDefId);
        this.procDefId = procDefId;
        return this;
    }
    
    @Override
    public GroupQuery memberOfTenant(final String tenantId) {
        EnsureUtil.ensureNotNull("Provided tenantId", (Object)tenantId);
        this.tenantId = tenantId;
        return this;
    }
    
    @Override
    public GroupQuery orderByGroupId() {
        return ((AbstractQuery<GroupQuery, U>)this).orderBy(GroupQueryProperty.GROUP_ID);
    }
    
    @Override
    public GroupQuery orderByGroupName() {
        return ((AbstractQuery<GroupQuery, U>)this).orderBy(GroupQueryProperty.NAME);
    }
    
    @Override
    public GroupQuery orderByGroupType() {
        return ((AbstractQuery<GroupQuery, U>)this).orderBy(GroupQueryProperty.TYPE);
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
    
    public String getType() {
        return this.type;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public String[] getIds() {
        return this.ids;
    }
}
