// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.metrics.util.MetricsUtil;
import java.util.Date;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.management.MetricIntervalValue;

public class MetricIntervalEntity implements MetricIntervalValue, DbEntity, Serializable
{
    protected Date timestamp;
    protected String name;
    protected String reporter;
    protected long value;
    
    public MetricIntervalEntity(final Date timestamp, final String name, final String reporter) {
        this.timestamp = timestamp;
        this.name = name;
        this.reporter = reporter;
    }
    
    public MetricIntervalEntity(final Long timestamp, final String name, final String reporter) {
        this.timestamp = new Date(timestamp);
        this.name = name;
        this.reporter = reporter;
    }
    
    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setTimestamp(final long timestamp) {
        this.timestamp = new Date(timestamp);
    }
    
    @Override
    public String getName() {
        return MetricsUtil.resolvePublicName(this.name);
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public String getReporter() {
        return this.reporter;
    }
    
    public void setReporter(final String reporter) {
        this.reporter = reporter;
    }
    
    @Override
    public long getValue() {
        return this.value;
    }
    
    public void setValue(final long value) {
        this.value = value;
    }
    
    @Override
    public String getId() {
        return this.name + this.reporter + this.timestamp.toString();
    }
    
    @Override
    public void setId(final String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Object getPersistentState() {
        return MetricIntervalEntity.class;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + ((this.timestamp != null) ? this.timestamp.hashCode() : 0);
        hash = 67 * hash + ((this.name != null) ? this.name.hashCode() : 0);
        hash = 67 * hash + ((this.reporter != null) ? this.reporter.hashCode() : 0);
        return hash;
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
        final MetricIntervalEntity other = (MetricIntervalEntity)obj;
        Label_0064: {
            if (this.name == null) {
                if (other.name == null) {
                    break Label_0064;
                }
            }
            else if (this.name.equals(other.name)) {
                break Label_0064;
            }
            return false;
        }
        if (this.reporter == null) {
            if (other.reporter == null) {
                return this.timestamp == other.timestamp || (this.timestamp != null && this.timestamp.equals(other.timestamp));
            }
        }
        else if (this.reporter.equals(other.reporter)) {
            return this.timestamp == other.timestamp || (this.timestamp != null && this.timestamp.equals(other.timestamp));
        }
        return false;
    }
}
