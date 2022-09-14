// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.repository.ResourceType;
import java.util.Date;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.io.Serializable;

public class ByteArrayEntity implements Serializable, DbEntity, HasDbRevision
{
    private static final long serialVersionUID = 1L;
    private static final Object PERSISTENTSTATE_NULL;
    protected String id;
    protected int revision;
    protected String name;
    protected byte[] bytes;
    protected String deploymentId;
    protected String tenantId;
    protected Integer type;
    protected Date createTime;
    protected String rootProcessInstanceId;
    protected Date removalTime;
    
    public ByteArrayEntity() {
    }
    
    public ByteArrayEntity(final String name, final byte[] bytes, final ResourceType type, final String rootProcessInstanceId, final Date removalTime) {
        this(name, bytes, type);
        this.rootProcessInstanceId = rootProcessInstanceId;
        this.removalTime = removalTime;
    }
    
    public ByteArrayEntity(final String name, final byte[] bytes, final ResourceType type) {
        this(name, bytes);
        this.type = type.getValue();
    }
    
    public ByteArrayEntity(final String name, final byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }
    
    public ByteArrayEntity(final byte[] bytes, final ResourceType type) {
        this.bytes = bytes;
        this.type = type.getValue();
    }
    
    public byte[] getBytes() {
        return this.bytes;
    }
    
    @Override
    public Object getPersistentState() {
        return (this.bytes != null) ? this.bytes : ByteArrayEntity.PERSISTENTSTATE_NULL;
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
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
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
    
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    public Date getRemovalTime() {
        return this.removalTime;
    }
    
    public void setRemovalTime(final Date removalTime) {
        this.removalTime = removalTime;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", revision=" + this.revision + ", name=" + this.name + ", deploymentId=" + this.deploymentId + ", tenantId=" + this.tenantId + ", type=" + this.type + ", createTime=" + this.createTime + ", rootProcessInstanceId=" + this.rootProcessInstanceId + ", removalTime=" + this.removalTime + "]";
    }
    
    static {
        PERSISTENTSTATE_NULL = new Object();
    }
}
