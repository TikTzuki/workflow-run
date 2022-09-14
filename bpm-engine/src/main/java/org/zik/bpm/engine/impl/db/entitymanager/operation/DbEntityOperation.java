// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.operation;

import org.zik.bpm.engine.impl.util.ClassNameUtil;
import java.util.Set;
import org.zik.bpm.engine.impl.db.DbEntity;

public class DbEntityOperation extends DbOperation
{
    protected DbEntity entity;
    protected Set<String> flushRelevantEntityReferences;
    protected DbOperation dependentOperation;
    
    @Override
    public void recycle() {
        this.entity = null;
        super.recycle();
    }
    
    public DbEntity getEntity() {
        return this.entity;
    }
    
    public void setEntity(final DbEntity dbEntity) {
        this.entityType = dbEntity.getClass();
        this.entity = dbEntity;
    }
    
    public void setFlushRelevantEntityReferences(final Set<String> flushRelevantEntityReferences) {
        this.flushRelevantEntityReferences = flushRelevantEntityReferences;
    }
    
    public Set<String> getFlushRelevantEntityReferences() {
        return this.flushRelevantEntityReferences;
    }
    
    @Override
    public String toString() {
        return this.operationType + " " + ClassNameUtil.getClassNameWithoutPackage(this.entity) + "[" + this.entity.getId() + "]";
    }
    
    public void setDependency(final DbOperation owner) {
        this.dependentOperation = owner;
    }
    
    public DbOperation getDependentOperation() {
        return this.dependentOperation;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.entity == null) ? 0 : this.entity.hashCode());
        result = 31 * result + ((this.operationType == null) ? 0 : this.operationType.hashCode());
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
        final DbEntityOperation other = (DbEntityOperation)obj;
        if (this.entity == null) {
            if (other.entity != null) {
                return false;
            }
        }
        else if (!this.entity.equals(other.entity)) {
            return false;
        }
        return this.operationType == other.operationType;
    }
}
