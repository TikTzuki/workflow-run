// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.io.Serializable;
import org.zik.bpm.engine.identity.Tenant;

public class TenantEntity implements Tenant, Serializable, DbEntity, HasDbRevision
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String name;
    protected int revision;
    
    public TenantEntity() {
    }
    
    public TenantEntity(final String id) {
        this.id = id;
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("name", this.name);
        return persistentState;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public String toString() {
        return "TenantEntity [id=" + this.id + ", name=" + this.name + ", revision=" + this.revision + "]";
    }
}
