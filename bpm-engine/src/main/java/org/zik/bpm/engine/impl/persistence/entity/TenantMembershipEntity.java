// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.DbEntity;
import java.io.Serializable;

public class TenantMembershipEntity implements Serializable, DbEntity
{
    private static final long serialVersionUID = 1L;
    protected TenantEntity tenant;
    protected UserEntity user;
    protected GroupEntity group;
    protected String id;
    
    @Override
    public Object getPersistentState() {
        return TenantMembershipEntity.class;
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
    
    public String getTenantId() {
        return this.tenant.getId();
    }
    
    public String getUserId() {
        if (this.user != null) {
            return this.user.getId();
        }
        return null;
    }
    
    public String getGroupId() {
        if (this.group != null) {
            return this.group.getId();
        }
        return null;
    }
    
    public TenantEntity getTenant() {
        return this.tenant;
    }
    
    public void setTenant(final TenantEntity tenant) {
        this.tenant = tenant;
    }
    
    @Override
    public String toString() {
        return "TenantMembershipEntity [id=" + this.id + ", tenant=" + this.tenant + ", user=" + this.user + ", group=" + this.group + "]";
    }
}
