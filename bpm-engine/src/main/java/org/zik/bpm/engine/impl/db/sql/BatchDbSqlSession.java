// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbBulkOperation;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbEntityOperation;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperationType;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.sql.BatchUpdateException;
import org.apache.ibatis.executor.BatchExecutorException;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import java.util.ArrayList;
import org.apache.ibatis.executor.BatchResult;
import java.util.Iterator;
import org.zik.bpm.engine.impl.db.FlushResult;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.List;
import java.sql.Connection;

public class BatchDbSqlSession extends DbSqlSession
{
    public BatchDbSqlSession(final DbSqlSessionFactory dbSqlSessionFactory) {
        super(dbSqlSessionFactory);
    }
    
    public BatchDbSqlSession(final DbSqlSessionFactory dbSqlSessionFactory, final Connection connection, final String catalog, final String schema) {
        super(dbSqlSessionFactory, connection, catalog, schema);
    }
    
    @Override
    public FlushResult executeDbOperations(final List<DbOperation> operations) {
        for (final DbOperation operation : operations) {
            try {
                this.executeDbOperation(operation);
            }
            catch (Exception ex) {
                throw ex;
            }
        }
        List<BatchResult> batchResults;
        try {
            batchResults = this.flushBatchOperations();
        }
        catch (RuntimeException e) {
            return this.postProcessBatchFailure(operations, e);
        }
        return this.postProcessBatchSuccess(operations, batchResults);
    }
    
    protected FlushResult postProcessBatchSuccess(final List<DbOperation> operations, final List<BatchResult> batchResults) {
        final Iterator<DbOperation> operationsIt = operations.iterator();
        final List<DbOperation> failedOperations = new ArrayList<DbOperation>();
        for (final BatchResult successfulBatch : batchResults) {
            this.postProcessJdbcBatchResult(operationsIt, successfulBatch.getUpdateCounts(), null, failedOperations);
        }
        if (operationsIt.hasNext()) {
            throw BatchDbSqlSession.LOG.wrongBatchResultsSizeException(operations);
        }
        return FlushResult.withFailures(failedOperations);
    }
    
    protected FlushResult postProcessBatchFailure(final List<DbOperation> operations, final RuntimeException exception) {
        final BatchExecutorException batchExecutorException = ExceptionUtil.findBatchExecutorException(exception);
        if (batchExecutorException == null) {
            throw exception;
        }
        final List<BatchResult> successfulBatches = (List<BatchResult>)batchExecutorException.getSuccessfulBatchResults();
        final BatchUpdateException cause = batchExecutorException.getBatchUpdateException();
        final Iterator<DbOperation> operationsIt = operations.iterator();
        final List<DbOperation> failedOperations = new ArrayList<DbOperation>();
        for (final BatchResult successfulBatch : successfulBatches) {
            this.postProcessJdbcBatchResult(operationsIt, successfulBatch.getUpdateCounts(), null, failedOperations);
        }
        final int[] failedBatchUpdateCounts = cause.getUpdateCounts();
        this.postProcessJdbcBatchResult(operationsIt, failedBatchUpdateCounts, exception, failedOperations);
        final List<DbOperation> remainingOperations = CollectionUtil.collectInList(operationsIt);
        return FlushResult.withFailuresAndRemaining(failedOperations, remainingOperations);
    }
    
    protected void postProcessJdbcBatchResult(final Iterator<DbOperation> operationsIt, final int[] statementResults, final Exception failure, final List<DbOperation> failedOperations) {
        boolean failureHandled = false;
        for (final int statementResult : statementResults) {
            EnsureUtil.ensureTrue("More batch results than scheduled operations detected. This indicates a bug", operationsIt.hasNext());
            final DbOperation operation = operationsIt.next();
            if (statementResult == -2) {
                if (this.requiresAffectedRows(operation.getOperationType())) {
                    throw BatchDbSqlSession.LOG.batchingNotSupported(operation);
                }
                this.postProcessOperationPerformed(operation, 1, null);
            }
            else if (statementResult == -3) {
                this.postProcessOperationPerformed(operation, 0, failure);
                failureHandled = true;
            }
            else {
                this.postProcessOperationPerformed(operation, statementResult, null);
            }
            if (operation.isFailed()) {
                failedOperations.add(operation);
            }
        }
        if (failure != null && !failureHandled) {
            EnsureUtil.ensureTrue("More batch results than scheduled operations detected. This indicates a bug", operationsIt.hasNext());
            final DbOperation failedOperation = operationsIt.next();
            this.postProcessOperationPerformed(failedOperation, 0, failure);
            if (failedOperation.isFailed()) {
                failedOperations.add(failedOperation);
            }
        }
    }
    
    protected boolean requiresAffectedRows(final DbOperationType operationType) {
        return operationType != DbOperationType.INSERT;
    }
    
    protected void postProcessOperationPerformed(final DbOperation operation, final int rowsAffected, final Exception failure) {
        switch (operation.getOperationType()) {
            case INSERT: {
                this.entityInsertPerformed((DbEntityOperation)operation, rowsAffected, failure);
                break;
            }
            case DELETE: {
                this.entityDeletePerformed((DbEntityOperation)operation, rowsAffected, failure);
                break;
            }
            case DELETE_BULK: {
                this.bulkDeletePerformed((DbBulkOperation)operation, rowsAffected, failure);
                break;
            }
            case UPDATE: {
                this.entityUpdatePerformed((DbEntityOperation)operation, rowsAffected, failure);
                break;
            }
            case UPDATE_BULK: {
                this.bulkUpdatePerformed((DbBulkOperation)operation, rowsAffected, failure);
                break;
            }
        }
    }
    
    @Override
    protected void updateEntity(final DbEntityOperation operation) {
        final DbEntity dbEntity = operation.getEntity();
        final String updateStatement = this.dbSqlSessionFactory.getUpdateStatement(dbEntity);
        EnsureUtil.ensureNotNull("no update statement for " + dbEntity.getClass() + " in the ibatis mapping files", "updateStatement", updateStatement);
        BatchDbSqlSession.LOG.executeDatabaseOperation("UPDATE", dbEntity);
        this.executeUpdate(updateStatement, dbEntity);
    }
    
    @Override
    protected void updateBulk(final DbBulkOperation operation) {
        final String statement = operation.getStatement();
        final Object parameter = operation.getParameter();
        BatchDbSqlSession.LOG.executeDatabaseBulkOperation("UPDATE", statement, parameter);
        this.executeUpdate(statement, parameter);
    }
    
    @Override
    protected void deleteBulk(final DbBulkOperation operation) {
        final String statement = operation.getStatement();
        final Object parameter = operation.getParameter();
        BatchDbSqlSession.LOG.executeDatabaseBulkOperation("DELETE", statement, parameter);
        this.executeDelete(statement, parameter);
    }
    
    @Override
    protected void deleteEntity(final DbEntityOperation operation) {
        final DbEntity dbEntity = operation.getEntity();
        final String deleteStatement = this.dbSqlSessionFactory.getDeleteStatement(dbEntity.getClass());
        EnsureUtil.ensureNotNull("no delete statement for " + dbEntity.getClass() + " in the ibatis mapping files", "deleteStatement", deleteStatement);
        BatchDbSqlSession.LOG.executeDatabaseOperation("DELETE", dbEntity);
        this.executeDelete(deleteStatement, dbEntity);
    }
    
    @Override
    protected void executeSelectForUpdate(final String statement, final Object parameter) {
        this.executeSelectList(statement, parameter);
    }
}
