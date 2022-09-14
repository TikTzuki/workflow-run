// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.management.SchemaLogEntry;

public class SchemaLogEntryEntity implements SchemaLogEntry, DbEntity, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected Date timestamp;
    protected String version;
    
    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String getVersion() {
        return this.version;
    }
    
    public void setVersion(final String version) {
        this.version = version;
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
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("id", this.id);
        persistentState.put("timestamp", this.timestamp);
        persistentState.put("version", this.version);
        return persistentState;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", timestamp=" + this.timestamp + ", version=" + this.version + "]";
    }
}
