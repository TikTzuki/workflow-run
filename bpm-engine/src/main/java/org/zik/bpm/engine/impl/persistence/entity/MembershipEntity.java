// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.DbEntity;
import java.io.Serializable;

public class MembershipEntity implements Serializable, DbEntity
{
    private static final long serialVersionUID = 1L;
    protected UserEntity user;
    protected GroupEntity group;
    protected String id;
    
    @Override
    public Object getPersistentState() {
        return MembershipEntity.class;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    public UserEntity getUser() {
        return this.user;
    }
    
    public void setUser(final UserEntity user) {
        this.user = user;
    }
    
    public GroupEntity getGroup() {
        return this.group;
    }
    
    public void setGroup(final GroupEntity group) {
        this.group = group;
    }
    
    public String getUserId() {
        return this.user.getId();
    }
    
    public String getGroupId() {
        return this.group.getId();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[user=" + this.user + ", group=" + this.group + "]";
    }
}
