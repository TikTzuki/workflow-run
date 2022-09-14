// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbBulkOperation;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbEntityOperation;
import java.util.Collections;
import org.zik.bpm.engine.impl.db.FlushResult;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.List;
import java.sql.Connection;

public class SimpleDbSqlSession extends DbSqlSession
{
    public SimpleDbSqlSession(final DbSqlSessionFactory dbSqlSessionFactory) {
        super(dbSqlSessionFactory);
    }
    
    public SimpleDbSqlSession(final DbSqlSessionFactory dbSqlSessionFactory, final Connection connection, final String catalog, final String schema) {
        super(dbSqlSessionFactory, connection, catalog, schema);
    }
    
    @Override
    protected void executeSelectForUpdate(final String statement, final Object parameter) {
        this.update(statement, parameter);
    }
    
    @Override
    public FlushResult executeDbOperations(final List<DbOperation> operations) {
        for (int i = 0; i < operations.size(); ++i) {
            final DbOperation operation = operations.get(i);
            this.executeDbOperation(operation);
            if (operation.isFailed()) {
                final List<DbOperation> remainingOperations = operations.subList(i + 1, operations.size());
                return FlushResult.withFailuresAndRemaining(Collections.singletonList(operation), remainingOperations);
            }
        }
        return FlushResult.allApplied();
    }
    
    @Override
    protected void insertEntity(final DbEntityOperation operation) {
        final DbEntity dbEntity = operation.getEntity();
        String insertStatement = this.dbSqlSessionFactory.getInsertStatement(dbEntity);
        insertStatement = this.dbSqlSessionFactory.mapStatement(insertStatement);
        EnsureUtil.ensureNotNull("no insert statement for " + dbEntity.getClass() + " in the ibatis mapping files", "insertStatement", insertStatement);
        try {
            this.executeInsertEntity(insertStatement, dbEntity);
            this.entityInsertPerformed(operation, 1, null);
        }
        catch (Exception e) {
            this.entityInsertPerformed(operation, 0, e);
        }
    }
    
    @Override
    protected void deleteEntity(final DbEntityOperation operation) {
        final DbEntity dbEntity = operation.getEntity();
        final String deleteStatement = this.dbSqlSessionFactory.getDeleteStatement(dbEntity.getClass());
        EnsureUtil.ensureNotNull("no delete statement for " + dbEntity.getClass() + " in the ibatis mapping files", "deleteStatement", deleteStatement);
        SimpleDbSqlSession.LOG.executeDatabaseOperation("DELETE", dbEntity);
        try {
            final int nrOfRowsDeleted = this.executeDelete(deleteStatement, dbEntity);
            this.entityDeletePerformed(operation, nrOfRowsDeleted, null);
        }
        catch (Exception e) {
            this.entityDeletePerformed(operation, 0, e);
        }
    }
    
    @Override
    protected void deleteBulk(final DbBulkOperation operation) {
        final String statement = operation.getStatement();
        final Object parameter = operation.getParameter();
        SimpleDbSqlSession.LOG.executeDatabaseBulkOperation("DELETE", statement, parameter);
        try {
            final int rowsAffected = this.executeDelete(statement, parameter);
            this.bulkDeletePerformed(operation, rowsAffected, null);
        }
        catch (Exception e) {
            this.bulkDeletePerformed(operation, 0, e);
        }
    }
    
    @Override
    protected void updateEntity(final DbEntityOperation operation) {
        final DbEntity dbEntity = operation.getEntity();
        final String updateStatement = this.dbSqlSessionFactory.getUpdateStatement(dbEntity);
        EnsureUtil.ensureNotNull("no update statement for " + dbEntity.getClass() + " in the ibatis mapping files", "updateStatement", updateStatement);
        SimpleDbSqlSession.LOG.executeDatabaseOperation("UPDATE", dbEntity);
        try {
            final int rowsAffected = this.executeUpdate(updateStatement, dbEntity);
            this.entityUpdatePerformed(operation, rowsAffected, null);
        }
        catch (Exception e) {
            this.entityUpdatePerformed(operation, 0, e);
        }
    }
    
    @Override
    protected void updateBulk(final DbBulkOperation operation) {
        final String statement = operation.getStatement();
        final Object parameter = operation.getParameter();
        SimpleDbSqlSession.LOG.executeDatabaseBulkOperation("UPDATE", statement, parameter);
        try {
            final int rowsAffected = this.executeUpdate(statement, parameter);
            this.bulkUpdatePerformed(operation, rowsAffected, null);
        }
        catch (Exception e) {
            this.bulkUpdatePerformed(operation, 0, e);
        }
    }
}
