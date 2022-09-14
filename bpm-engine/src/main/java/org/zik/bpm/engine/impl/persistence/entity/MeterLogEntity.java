// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.DbEntity;

public class MeterLogEntity implements DbEntity, HasDbReferences, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected Date timestamp;
    protected Long milliseconds;
    protected String name;
    protected String reporter;
    protected long value;
    
    public MeterLogEntity(final String name, final long value, final Date timestamp) {
        this(name, null, value, timestamp);
    }
    
    public MeterLogEntity(final String name, final String reporter, final long value, final Date timestamp) {
        this.name = name;
        this.reporter = reporter;
        this.value = value;
        this.timestamp = timestamp;
        this.milliseconds = timestamp.getTime();
    }
    
    public MeterLogEntity() {
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public Long getMilliseconds() {
        return this.milliseconds;
    }
    
    public void setMilliseconds(final Long milliseconds) {
        this.milliseconds = milliseconds;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public long getValue() {
        return this.value;
    }
    
    public void setValue(final long value) {
        this.value = value;
    }
    
    public String getReporter() {
        return this.reporter;
    }
    
    public void setReporter(final String reporter) {
        this.reporter = reporter;
    }
    
    @Override
    public Object getPersistentState() {
        return MeterLogEntity.class;
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        return referenceIdAndClass;
    }
}
