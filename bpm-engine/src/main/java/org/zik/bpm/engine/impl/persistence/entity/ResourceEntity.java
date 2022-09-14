// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Date;
import org.zik.bpm.engine.repository.Resource;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.io.Serializable;

public class ResourceEntity implements Serializable, DbEntity, Resource
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String name;
    protected byte[] bytes;
    protected String deploymentId;
    protected boolean generated;
    protected String tenantId;
    protected Integer type;
    protected Date createTime;
    
    public ResourceEntity() {
        this.generated = false;
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
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public byte[] getBytes() {
        return this.bytes;
    }
    
    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }
    
    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    @Override
    public Object getPersistentState() {
        return ResourceEntity.class;
    }
    
    public void setGenerated(final boolean generated) {
        this.generated = generated;
    }
    
    public boolean isGenerated() {
        return this.generated;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", name=" + this.name + ", deploymentId=" + this.deploymentId + ", generated=" + this.generated + ", tenantId=" + this.tenantId + ", type=" + this.type + ", createTime=" + this.createTime + "]";
    }
}
