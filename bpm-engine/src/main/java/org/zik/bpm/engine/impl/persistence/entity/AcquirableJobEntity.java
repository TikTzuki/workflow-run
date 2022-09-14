// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;

public class AcquirableJobEntity implements DbEntity, HasDbRevision
{
    public static final boolean DEFAULT_EXCLUSIVE = true;
    protected String id;
    protected int revision;
    protected String lockOwner;
    protected Date lockExpirationTime;
    protected Date duedate;
    protected String processInstanceId;
    protected boolean isExclusive;
    
    public AcquirableJobEntity() {
        this.lockOwner = null;
        this.lockExpirationTime = null;
        this.processInstanceId = null;
        this.isExclusive = true;
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("lockOwner", this.lockOwner);
        persistentState.put("lockExpirationTime", this.lockExpirationTime);
        persistentState.put("duedate", this.duedate);
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
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    public Date getDuedate() {
        return this.duedate;
    }
    
    public void setDuedate(final Date duedate) {
        this.duedate = duedate;
    }
    
    public String getLockOwner() {
        return this.lockOwner;
    }
    
    public void setLockOwner(final String lockOwner) {
        this.lockOwner = lockOwner;
    }
    
    public Date getLockExpirationTime() {
        return this.lockExpirationTime;
    }
    
    public void setLockExpirationTime(final Date lockExpirationTime) {
        this.lockExpirationTime = lockExpirationTime;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public boolean isExclusive() {
        return this.isExclusive;
    }
    
    public void setExclusive(final boolean isExclusive) {
        this.isExclusive = isExclusive;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final AcquirableJobEntity other = (AcquirableJobEntity)obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", revision=" + this.revision + ", lockOwner=" + this.lockOwner + ", lockExpirationTime=" + this.lockExpirationTime + ", duedate=" + this.duedate + ", processInstanceId=" + this.processInstanceId + ", isExclusive=" + this.isExclusive + "]";
    }
}
