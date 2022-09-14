// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.cache;

import java.util.Collections;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import java.util.Set;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.entitymanager.Recyclable;

public class CachedDbEntity implements Recyclable
{
    protected DbEntity dbEntity;
    protected Object copy;
    protected DbEntityState entityState;
    protected Set<String> flushRelevantEntityReferences;
    
    public CachedDbEntity() {
        this.flushRelevantEntityReferences = null;
    }
    
    @Override
    public void recycle() {
        this.dbEntity = null;
        this.copy = null;
        this.entityState = null;
    }
    
    public boolean isDirty() {
        return !this.dbEntity.getPersistentState().equals(this.copy);
    }
    
    public void forceSetDirty() {
        this.copy = -1;
    }
    
    public void makeCopy() {
        this.copy = this.dbEntity.getPersistentState();
    }
    
    @Override
    public String toString() {
        return this.entityState + " " + this.dbEntity.getClass().getSimpleName() + "[" + this.dbEntity.getId() + "]";
    }
    
    public void determineEntityReferences() {
        if (this.dbEntity instanceof HasDbReferences) {
            this.flushRelevantEntityReferences = ((HasDbReferences)this.dbEntity).getReferencedEntityIds();
        }
        else {
            this.flushRelevantEntityReferences = Collections.emptySet();
        }
    }
    
    public boolean areFlushRelevantReferencesDetermined() {
        return this.flushRelevantEntityReferences != null;
    }
    
    public Set<String> getFlushRelevantEntityReferences() {
        return this.flushRelevantEntityReferences;
    }
    
    public DbEntity getEntity() {
        return this.dbEntity;
    }
    
    public void setEntity(final DbEntity dbEntity) {
        this.dbEntity = dbEntity;
    }
    
    public DbEntityState getEntityState() {
        return this.entityState;
    }
    
    public void setEntityState(final DbEntityState entityState) {
        this.entityState = entityState;
    }
    
    public Class<? extends DbEntity> getEntityType() {
        return this.dbEntity.getClass();
    }
}
