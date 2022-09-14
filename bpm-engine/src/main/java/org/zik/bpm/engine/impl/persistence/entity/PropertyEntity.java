// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import java.io.Serializable;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;

public class PropertyEntity implements DbEntity, HasDbRevision, Serializable
{
    protected static final EnginePersistenceLogger LOG;
    private static final long serialVersionUID = 1L;
    String name;
    int revision;
    String value;
    
    public PropertyEntity() {
    }
    
    public PropertyEntity(final String name, final String value) {
        this.name = name;
        this.value = value;
    }
    
    public String getName() {
        return this.name;
    }
    
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
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public String getId() {
        return this.name;
    }
    
    @Override
    public Object getPersistentState() {
        return this.value;
    }
    
    @Override
    public void setId(final String id) {
        throw PropertyEntity.LOG.notAllowedIdException(id);
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[name=" + this.name + ", revision=" + this.revision + ", value=" + this.value + "]";
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
