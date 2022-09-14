// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.operation;

import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.entitymanager.Recyclable;

public abstract class DbOperation implements Recyclable
{
    protected DbOperationType operationType;
    protected int rowsAffected;
    protected Exception failure;
    protected State state;
    protected Class<? extends DbEntity> entityType;
    
    @Override
    public void recycle() {
        this.operationType = null;
        this.entityType = null;
    }
    
    public Class<? extends DbEntity> getEntityType() {
        return this.entityType;
    }
    
    public void setEntityType(final Class<? extends DbEntity> entityType) {
        this.entityType = entityType;
    }
    
    public DbOperationType getOperationType() {
        return this.operationType;
    }
    
    public void setOperationType(final DbOperationType operationType) {
        this.operationType = operationType;
    }
    
    public int getRowsAffected() {
        return this.rowsAffected;
    }
    
    public void setRowsAffected(final int rowsAffected) {
        this.rowsAffected = rowsAffected;
    }
    
    public boolean isFailed() {
        return this.state == State.FAILED_CONCURRENT_MODIFICATION || this.state == State.FAILED_CONCURRENT_MODIFICATION_CRDB || this.state == State.FAILED_ERROR;
    }
    
    public State getState() {
        return this.state;
    }
    
    public void setState(final State state) {
        this.state = state;
    }
    
    public Exception getFailure() {
        return this.failure;
    }
    
    public void setFailure(final Exception failure) {
        this.failure = failure;
    }
    
    public enum State
    {
        NOT_APPLIED, 
        APPLIED, 
        FAILED_ERROR, 
        FAILED_CONCURRENT_MODIFICATION, 
        FAILED_CONCURRENT_MODIFICATION_CRDB;
    }
}
