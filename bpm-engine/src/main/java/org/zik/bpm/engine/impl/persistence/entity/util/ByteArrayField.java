// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity.util;

import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Date;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.impl.persistence.entity.Nameable;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;

public class ByteArrayField
{
    protected ByteArrayEntity byteArrayValue;
    protected String byteArrayId;
    protected final Nameable nameProvider;
    protected ResourceType type;
    protected String rootProcessInstanceId;
    protected Date removalTime;
    
    public ByteArrayField(final Nameable nameProvider, final ResourceType type, final String rootProcessInstanceId, final Date removalTime) {
        this(nameProvider, type);
        this.removalTime = removalTime;
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    public ByteArrayField(final Nameable nameProvider, final ResourceType type) {
        this.nameProvider = nameProvider;
        this.type = type;
    }
    
    public String getByteArrayId() {
        return this.byteArrayId;
    }
    
    public void setByteArrayId(final String byteArrayId) {
        this.byteArrayId = byteArrayId;
        this.byteArrayValue = null;
    }
    
    public byte[] getByteArrayValue() {
        this.getByteArrayEntity();
        if (this.byteArrayValue != null) {
            return this.byteArrayValue.getBytes();
        }
        return null;
    }
    
    protected ByteArrayEntity getByteArrayEntity() {
        if (this.byteArrayValue == null && this.byteArrayId != null && Context.getCommandContext() != null) {
            return this.byteArrayValue = Context.getCommandContext().getDbEntityManager().selectById(ByteArrayEntity.class, this.byteArrayId);
        }
        return this.byteArrayValue;
    }
    
    public void setByteArrayValue(final byte[] bytes) {
        this.setByteArrayValue(bytes, false);
    }
    
    public void setByteArrayValue(final byte[] bytes, final boolean isTransient) {
        if (bytes != null) {
            if (this.byteArrayId != null && this.getByteArrayEntity() != null) {
                this.byteArrayValue.setBytes(bytes);
            }
            else {
                this.deleteByteArrayValue();
                this.byteArrayValue = new ByteArrayEntity(this.nameProvider.getName(), bytes, this.type, this.rootProcessInstanceId, this.removalTime);
                if (!isTransient) {
                    Context.getCommandContext().getByteArrayManager().insertByteArray(this.byteArrayValue);
                    this.byteArrayId = this.byteArrayValue.getId();
                }
            }
        }
        else {
            this.deleteByteArrayValue();
        }
    }
    
    public void deleteByteArrayValue() {
        if (this.byteArrayId != null) {
            this.getByteArrayEntity();
            if (this.byteArrayValue != null) {
                Context.getCommandContext().getDbEntityManager().delete(this.byteArrayValue);
            }
            this.byteArrayId = null;
        }
    }
    
    public void setByteArrayValue(final ByteArrayEntity byteArrayValue) {
        this.byteArrayValue = byteArrayValue;
    }
    
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    public void setRemovalTime(final Date removalTime) {
        this.removalTime = removalTime;
    }
}
