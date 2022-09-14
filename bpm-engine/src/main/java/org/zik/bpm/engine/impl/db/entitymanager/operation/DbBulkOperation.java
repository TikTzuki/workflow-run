// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.entitymanager.operation;

import org.zik.bpm.engine.impl.db.DbEntity;

public class DbBulkOperation extends DbOperation
{
    protected String statement;
    protected Object parameter;
    
    public DbBulkOperation() {
    }
    
    public DbBulkOperation(final DbOperationType operationType, final Class<? extends DbEntity> entityType, final String statement, final Object parameter) {
        this.operationType = operationType;
        this.entityType = entityType;
        this.statement = statement;
        this.parameter = parameter;
    }
    
    @Override
    public void recycle() {
        this.statement = null;
        this.parameter = null;
        super.recycle();
    }
    
    public Object getParameter() {
        return this.parameter;
    }
    
    public void setParameter(final Object parameter) {
        this.parameter = parameter;
    }
    
    public String getStatement() {
        return this.statement;
    }
    
    public void setStatement(final String statement) {
        this.statement = statement;
    }
    
    @Override
    public String toString() {
        return this.operationType + " " + this.statement + " " + this.parameter;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.parameter == null) ? 0 : this.parameter.hashCode());
        result = 31 * result + ((this.statement == null) ? 0 : this.statement.hashCode());
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
        final DbBulkOperation other = (DbBulkOperation)obj;
        if (this.parameter == null) {
            if (other.parameter != null) {
                return false;
            }
        }
        else if (!this.parameter.equals(other.parameter)) {
            return false;
        }
        if (this.statement == null) {
            if (other.statement != null) {
                return false;
            }
        }
        else if (!this.statement.equals(other.statement)) {
            return false;
        }
        return true;
    }
}
