// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.io.IOException;
import java.sql.Statement;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Closeable;
import org.zik.bpm.engine.impl.util.IoUtil;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import java.sql.PreparedStatement;
import org.zik.bpm.engine.impl.context.Context;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Map;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperationType;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbBulkOperation;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbEntityOperation;
import org.zik.bpm.engine.impl.util.DatabaseUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Iterator;
import java.util.List;
import java.sql.Connection;
import org.apache.ibatis.session.SqlSessionFactory;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import java.util.Objects;
import org.apache.ibatis.session.SqlSession;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.AbstractPersistenceSession;

public abstract class DbSqlSession extends AbstractPersistenceSession
{
    protected static final EnginePersistenceLogger LOG;
    public static final String[] JDBC_METADATA_TABLE_TYPES;
    public static final String[] PG_JDBC_METADATA_TABLE_TYPES;
    protected SqlSession sqlSession;
    protected DbSqlSessionFactory dbSqlSessionFactory;
    protected String connectionMetadataDefaultCatalog;
    protected String connectionMetadataDefaultSchema;
    
    public DbSqlSession(final DbSqlSessionFactory dbSqlSessionFactory) {
        this.connectionMetadataDefaultCatalog = null;
        this.connectionMetadataDefaultSchema = null;
        this.dbSqlSessionFactory = dbSqlSessionFactory;
        final SqlSessionFactory sqlSessionFactory2;
        final SqlSessionFactory sqlSessionFactory = sqlSessionFactory2 = dbSqlSessionFactory.getSqlSessionFactory();
        Objects.requireNonNull(sqlSessionFactory2);
        this.sqlSession = ExceptionUtil.doWithExceptionWrapper(sqlSessionFactory2::openSession);
    }
    
    public DbSqlSession(final DbSqlSessionFactory dbSqlSessionFactory, final Connection connection, final String catalog, final String schema) {
        this.connectionMetadataDefaultCatalog = null;
        this.connectionMetadataDefaultSchema = null;
        this.dbSqlSessionFactory = dbSqlSessionFactory;
        final SqlSessionFactory sqlSessionFactory = dbSqlSessionFactory.getSqlSessionFactory();
        this.sqlSession = ExceptionUtil.doWithExceptionWrapper(() -> sqlSessionFactory.openSession(connection));
        this.connectionMetadataDefaultCatalog = catalog;
        this.connectionMetadataDefaultSchema = schema;
    }
    
    @Override
    public List<?> selectList(String statement, final Object parameter) {
        statement = this.dbSqlSessionFactory.mapStatement(statement);
        final List<Object> resultList = this.executeSelectList(statement, parameter);
        for (final Object object : resultList) {
            this.fireEntityLoaded(object);
        }
        return resultList;
    }
    
    public List<Object> executeSelectList(final String statement, final Object parameter) {
        return ExceptionUtil.doWithExceptionWrapper(() -> this.sqlSession.selectList(statement, parameter));
    }
    
    @Override
    public <T extends DbEntity> T selectById(final Class<T> type, final String id) {
        final String selectStatement = this.dbSqlSessionFactory.getSelectStatement(type);
        final String mappedSelectStatement = this.dbSqlSessionFactory.mapStatement(selectStatement);
        EnsureUtil.ensureNotNull("no select statement for " + type + " in the ibatis mapping files", "selectStatement", selectStatement);
        final Object result = ExceptionUtil.doWithExceptionWrapper(() -> this.sqlSession.selectOne(mappedSelectStatement, (Object)id));
        this.fireEntityLoaded(result);
        return (T)result;
    }
    
    @Override
    public Object selectOne(final String statement, final Object parameter) {
        final String mappedStatement = this.dbSqlSessionFactory.mapStatement(statement);
        final Object result = ExceptionUtil.doWithExceptionWrapper(() -> this.sqlSession.selectOne(mappedStatement, parameter));
        this.fireEntityLoaded(result);
        return result;
    }
    
    @Override
    public void lock(final String statement, final Object parameter) {
        if (!DatabaseUtil.checkDatabaseType("cockroachdb", "h2")) {
            final String mappedStatement = this.dbSqlSessionFactory.mapStatement(statement);
            this.executeSelectForUpdate(mappedStatement, parameter);
        }
        else {
            DbSqlSession.LOG.debugDisabledPessimisticLocks();
        }
    }
    
    protected abstract void executeSelectForUpdate(final String p0, final Object p1);
    
    protected void entityUpdatePerformed(final DbEntityOperation operation, final int rowsAffected, final Exception failure) {
        if (failure != null) {
            this.configureFailedDbEntityOperation(operation, failure);
        }
        else {
            final DbEntity dbEntity = operation.getEntity();
            if (dbEntity instanceof HasDbRevision) {
                if (rowsAffected != 1) {
                    operation.setState(DbOperation.State.FAILED_CONCURRENT_MODIFICATION);
                }
                else {
                    final HasDbRevision versionedObject = (HasDbRevision)dbEntity;
                    versionedObject.setRevision(versionedObject.getRevisionNext());
                    operation.setState(DbOperation.State.APPLIED);
                }
            }
            else {
                operation.setState(DbOperation.State.APPLIED);
            }
        }
    }
    
    protected void bulkUpdatePerformed(final DbBulkOperation operation, final int rowsAffected, final Exception failure) {
        this.bulkOperationPerformed(operation, rowsAffected, failure);
    }
    
    protected void bulkDeletePerformed(final DbBulkOperation operation, final int rowsAffected, final Exception failure) {
        this.bulkOperationPerformed(operation, rowsAffected, failure);
    }
    
    protected void bulkOperationPerformed(final DbBulkOperation operation, final int rowsAffected, final Exception failure) {
        if (failure != null) {
            operation.setFailure(failure);
            DbOperation.State failedState = DbOperation.State.FAILED_ERROR;
            if (isCrdbConcurrencyConflict(failure)) {
                failedState = DbOperation.State.FAILED_CONCURRENT_MODIFICATION_CRDB;
            }
            operation.setState(failedState);
        }
        else {
            operation.setRowsAffected(rowsAffected);
            operation.setState(DbOperation.State.APPLIED);
        }
    }
    
    protected void entityDeletePerformed(final DbEntityOperation operation, final int rowsAffected, final Exception failure) {
        if (failure != null) {
            this.configureFailedDbEntityOperation(operation, failure);
        }
        else {
            operation.setRowsAffected(rowsAffected);
            final DbEntity dbEntity = operation.getEntity();
            if (dbEntity instanceof HasDbRevision && rowsAffected == 0) {
                operation.setState(DbOperation.State.FAILED_CONCURRENT_MODIFICATION);
            }
            else {
                operation.setState(DbOperation.State.APPLIED);
            }
        }
    }
    
    protected void configureFailedDbEntityOperation(final DbEntityOperation operation, final Exception failure) {
        operation.setRowsAffected(0);
        operation.setFailure(failure);
        final DbOperationType operationType = operation.getOperationType();
        final DbOperation dependencyOperation = operation.getDependentOperation();
        DbOperation.State failedState;
        if (isCrdbConcurrencyConflict(failure)) {
            failedState = DbOperation.State.FAILED_CONCURRENT_MODIFICATION_CRDB;
        }
        else if (this.isConcurrentModificationException(operation, failure)) {
            failedState = DbOperation.State.FAILED_CONCURRENT_MODIFICATION;
        }
        else if (DbOperationType.DELETE.equals(operationType) && dependencyOperation != null && dependencyOperation.getState() != null && dependencyOperation.getState() != DbOperation.State.APPLIED) {
            DbSqlSession.LOG.ignoreFailureDuePreconditionNotMet(operation, "Parent database operation failed", dependencyOperation);
            failedState = DbOperation.State.NOT_APPLIED;
        }
        else {
            failedState = DbOperation.State.FAILED_ERROR;
        }
        operation.setState(failedState);
    }
    
    protected boolean isConcurrentModificationException(final DbOperation failedOperation, final Exception cause) {
        final boolean isConstraintViolation = ExceptionUtil.checkForeignKeyConstraintViolation(cause);
        final boolean isVariableIntegrityViolation = ExceptionUtil.checkVariableIntegrityViolation(cause);
        if (isVariableIntegrityViolation) {
            return true;
        }
        if (isConstraintViolation && failedOperation instanceof DbEntityOperation && ((DbEntityOperation)failedOperation).getEntity() instanceof HasDbReferences && (failedOperation.getOperationType().equals(DbOperationType.INSERT) || failedOperation.getOperationType().equals(DbOperationType.UPDATE))) {
            final DbEntity entity = ((DbEntityOperation)failedOperation).getEntity();
            for (final Map.Entry<String, Class> reference : ((HasDbReferences)entity).getReferencedEntitiesIdAndClass().entrySet()) {
                final DbEntity referencedEntity = this.selectById(reference.getValue(), reference.getKey());
                if (referencedEntity == null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isCrdbConcurrencyConflict(final Throwable cause) {
        if (DatabaseUtil.checkDatabaseType("cockroachdb")) {
            final boolean isCrdbTxRetryException = ExceptionUtil.checkCrdbTransactionRetryException(cause);
            if (isCrdbTxRetryException) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isCrdbConcurrencyConflictOnCommit(final Throwable cause, final ProcessEngineConfigurationImpl configuration) {
        if (DatabaseUtil.checkDatabaseType(configuration, "cockroachdb")) {
            final List<Throwable> causes = new ArrayList<Throwable>(Arrays.asList(cause.getSuppressed()));
            causes.add(cause);
            for (final Throwable throwable : causes) {
                if (ExceptionUtil.checkCrdbTransactionRetryException(throwable)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    protected void insertEntity(final DbEntityOperation operation) {
        final DbEntity dbEntity = operation.getEntity();
        String insertStatement = this.dbSqlSessionFactory.getInsertStatement(dbEntity);
        insertStatement = this.dbSqlSessionFactory.mapStatement(insertStatement);
        EnsureUtil.ensureNotNull("no insert statement for " + dbEntity.getClass() + " in the ibatis mapping files", "insertStatement", insertStatement);
        this.executeInsertEntity(insertStatement, dbEntity);
    }
    
    protected void executeInsertEntity(final String insertStatement, final Object parameter) {
        DbSqlSession.LOG.executeDatabaseOperation("INSERT", parameter);
        try {
            this.sqlSession.insert(insertStatement, parameter);
        }
        catch (Exception e) {
            throw e;
        }
    }
    
    protected void entityInsertPerformed(final DbEntityOperation operation, final int rowsAffected, final Exception failure) {
        final DbEntity entity = operation.getEntity();
        if (failure != null) {
            this.configureFailedDbEntityOperation(operation, failure);
        }
        else {
            if (entity instanceof HasDbRevision) {
                final HasDbRevision versionedObject = (HasDbRevision)entity;
                versionedObject.setRevision(1);
            }
            operation.setState(DbOperation.State.APPLIED);
        }
    }
    
    protected int executeDelete(final String deleteStatement, final Object parameter) {
        final String mappedDeleteStatement = this.dbSqlSessionFactory.mapStatement(deleteStatement);
        try {
            return this.sqlSession.delete(mappedDeleteStatement, parameter);
        }
        catch (Exception e) {
            throw e;
        }
    }
    
    public int executeUpdate(final String updateStatement, final Object parameter) {
        final String mappedUpdateStatement = this.dbSqlSessionFactory.mapStatement(updateStatement);
        try {
            return this.sqlSession.update(mappedUpdateStatement, parameter);
        }
        catch (Exception e) {
            throw e;
        }
    }
    
    public int update(final String updateStatement, final Object parameter) {
        return ExceptionUtil.doWithExceptionWrapper(() -> this.sqlSession.update(updateStatement, parameter));
    }
    
    @Override
    public int executeNonEmptyUpdateStmt(final String updateStmt, final Object parameter) {
        final String mappedUpdateStmt = this.dbSqlSessionFactory.mapStatement(updateStmt);
        final Configuration configuration;
        final String s;
        final MappedStatement mappedStatement;
        final BoundSql boundSql;
        final String sql;
        final boolean isMappedStmtEmpty = ExceptionUtil.doWithExceptionWrapper(() -> {
            configuration = this.sqlSession.getConfiguration();
            mappedStatement = configuration.getMappedStatement(s);
            boundSql = mappedStatement.getBoundSql(parameter);
            sql = boundSql.getSql();
            return Boolean.valueOf(sql.isEmpty());
        });
        if (isMappedStmtEmpty) {
            return 0;
        }
        return this.update(mappedUpdateStmt, parameter);
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public void flushOperations() {
        ExceptionUtil.doWithExceptionWrapper(this::flushBatchOperations);
    }
    
    public List<BatchResult> flushBatchOperations() {
        try {
            return (List<BatchResult>)this.sqlSession.flushStatements();
        }
        catch (RuntimeException ex) {
            throw ex;
        }
    }
    
    @Override
    public void close() {
        ExceptionUtil.doWithExceptionWrapper(() -> {
            this.sqlSession.close();
            return null;
        });
    }
    
    @Override
    public void commit() {
        ExceptionUtil.doWithExceptionWrapper(() -> {
            this.sqlSession.commit();
            return null;
        });
    }
    
    @Override
    public void rollback() {
        ExceptionUtil.doWithExceptionWrapper(() -> {
            this.sqlSession.rollback();
            return null;
        });
    }
    
    @Override
    public void dbSchemaCheckVersion() {
        try {
            final String dbVersion = this.getDbVersion();
            if (!"fox".equals(dbVersion)) {
                throw DbSqlSession.LOG.wrongDbVersionException("fox", dbVersion);
            }
            final List<String> missingComponents = new ArrayList<String>();
            if (!this.isEngineTablePresent()) {
                missingComponents.add("engine");
            }
            if (this.dbSqlSessionFactory.isDbHistoryUsed() && !this.isHistoryTablePresent()) {
                missingComponents.add("history");
            }
            if (this.dbSqlSessionFactory.isDbIdentityUsed() && !this.isIdentityTablePresent()) {
                missingComponents.add("identity");
            }
            if (this.dbSqlSessionFactory.isCmmnEnabled() && !this.isCmmnTablePresent()) {
                missingComponents.add("case.engine");
            }
            if (this.dbSqlSessionFactory.isDmnEnabled() && !this.isDmnTablePresent()) {
                missingComponents.add("decision.engine");
            }
            if (!missingComponents.isEmpty()) {
                throw DbSqlSession.LOG.missingTableException(missingComponents);
            }
        }
        catch (Exception e) {
            if (this.isMissingTablesException(e)) {
                throw DbSqlSession.LOG.missingActivitiTablesException();
            }
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            }
            throw DbSqlSession.LOG.unableToFetchDbSchemaVersion(e);
        }
    }
    
    @Override
    protected String getDbVersion() {
        final String selectSchemaVersionStatement = this.dbSqlSessionFactory.mapStatement("selectDbSchemaVersion");
        return ExceptionUtil.doWithExceptionWrapper(() -> (String)this.sqlSession.selectOne(selectSchemaVersionStatement));
    }
    
    @Override
    protected void dbSchemaCreateIdentity() {
        this.executeMandatorySchemaResource("create", "identity");
    }
    
    @Override
    protected void dbSchemaCreateHistory() {
        this.executeMandatorySchemaResource("create", "history");
    }
    
    @Override
    protected void dbSchemaCreateEngine() {
        this.executeMandatorySchemaResource("create", "engine");
    }
    
    @Override
    protected void dbSchemaCreateCmmn() {
        this.executeMandatorySchemaResource("create", "case.engine");
    }
    
    @Override
    protected void dbSchemaCreateCmmnHistory() {
        this.executeMandatorySchemaResource("create", "case.history");
    }
    
    @Override
    protected void dbSchemaCreateDmn() {
        this.executeMandatorySchemaResource("create", "decision.engine");
    }
    
    @Override
    protected void dbSchemaCreateDmnHistory() {
        this.executeMandatorySchemaResource("create", "decision.history");
    }
    
    @Override
    protected void dbSchemaDropIdentity() {
        this.executeMandatorySchemaResource("drop", "identity");
    }
    
    @Override
    protected void dbSchemaDropHistory() {
        this.executeMandatorySchemaResource("drop", "history");
    }
    
    @Override
    protected void dbSchemaDropEngine() {
        this.executeMandatorySchemaResource("drop", "engine");
    }
    
    @Override
    protected void dbSchemaDropCmmn() {
        this.executeMandatorySchemaResource("drop", "case.engine");
    }
    
    @Override
    protected void dbSchemaDropCmmnHistory() {
        this.executeMandatorySchemaResource("drop", "case.history");
    }
    
    @Override
    protected void dbSchemaDropDmn() {
        this.executeMandatorySchemaResource("drop", "decision.engine");
    }
    
    @Override
    protected void dbSchemaDropDmnHistory() {
        this.executeMandatorySchemaResource("drop", "decision.history");
    }
    
    public void executeMandatorySchemaResource(final String operation, final String component) {
        this.executeSchemaResource(operation, component, this.getResourceForDbOperation(operation, operation, component), false);
    }
    
    @Override
    public boolean isEngineTablePresent() {
        return this.isTablePresent("ACT_RU_EXECUTION");
    }
    
    @Override
    public boolean isHistoryTablePresent() {
        return this.isTablePresent("ACT_HI_PROCINST");
    }
    
    @Override
    public boolean isIdentityTablePresent() {
        return this.isTablePresent("ACT_ID_USER");
    }
    
    @Override
    public boolean isCmmnTablePresent() {
        return this.isTablePresent("ACT_RE_CASE_DEF");
    }
    
    @Override
    public boolean isCmmnHistoryTablePresent() {
        return this.isTablePresent("ACT_HI_CASEINST");
    }
    
    @Override
    public boolean isDmnTablePresent() {
        return this.isTablePresent("ACT_RE_DECISION_DEF");
    }
    
    @Override
    public boolean isDmnHistoryTablePresent() {
        return this.isTablePresent("ACT_HI_DECINST");
    }
    
    public boolean isTablePresent(String tableName) {
        tableName = this.prependDatabaseTablePrefix(tableName);
        Connection connection = null;
        try {
            connection = ExceptionUtil.doWithExceptionWrapper(() -> this.sqlSession.getConnection());
            final DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet tables = null;
            String schema = this.connectionMetadataDefaultSchema;
            if (this.dbSqlSessionFactory.getDatabaseSchema() != null) {
                schema = this.dbSqlSessionFactory.getDatabaseSchema();
            }
            if (DatabaseUtil.checkDatabaseType("postgres", "cockroachdb")) {
                tableName = tableName.toLowerCase();
            }
            try {
                tables = databaseMetaData.getTables(this.connectionMetadataDefaultCatalog, schema, tableName, this.getTableTypes());
                return tables.next();
            }
            finally {
                if (tables != null) {
                    tables.close();
                }
            }
        }
        catch (Exception e) {
            throw DbSqlSession.LOG.checkDatabaseTableException(e);
        }
    }
    
    @Override
    public List<String> getTableNamesPresent() {
        List<String> tableNames = new ArrayList<String>();
        try {
            ResultSet tablesRs = null;
            try {
                if ("oracle".equals(this.getDbSqlSessionFactory().getDatabaseType())) {
                    tableNames = this.getTablesPresentInOracleDatabase();
                }
                else {
                    final Connection connection = this.getSqlSession().getConnection();
                    final String databaseTablePrefix = this.getDbSqlSessionFactory().getDatabaseTablePrefix();
                    String schema = this.getDbSqlSessionFactory().getDatabaseSchema();
                    String tableNameFilter = this.prependDatabaseTablePrefix("ACT_%");
                    if (DatabaseUtil.checkDatabaseType("postgres", "cockroachdb")) {
                        schema = ((schema == null) ? schema : schema.toLowerCase());
                        tableNameFilter = tableNameFilter.toLowerCase();
                    }
                    final DatabaseMetaData databaseMetaData = connection.getMetaData();
                    tablesRs = databaseMetaData.getTables(null, schema, tableNameFilter, this.getTableTypes());
                    while (tablesRs.next()) {
                        String tableName = tablesRs.getString("TABLE_NAME");
                        if (!databaseTablePrefix.isEmpty()) {
                            tableName = databaseTablePrefix + tableName;
                        }
                        tableName = tableName.toUpperCase();
                        tableNames.add(tableName);
                    }
                    DbSqlSession.LOG.fetchDatabaseTables("jdbc metadata", tableNames);
                }
            }
            catch (SQLException se) {
                throw se;
            }
            finally {
                if (tablesRs != null) {
                    tablesRs.close();
                }
            }
        }
        catch (Exception e) {
            throw DbSqlSession.LOG.getDatabaseTableNameException(e);
        }
        return tableNames;
    }
    
    protected List<String> getTablesPresentInOracleDatabase() throws SQLException {
        final List<String> tableNames = new ArrayList<String>();
        Connection connection = null;
        PreparedStatement prepStat = null;
        ResultSet tablesRs = null;
        final String selectTableNamesFromOracle = "SELECT table_name FROM all_tables WHERE table_name LIKE ?";
        final String databaseTablePrefix = this.getDbSqlSessionFactory().getDatabaseTablePrefix();
        try {
            connection = Context.getProcessEngineConfiguration().getDataSource().getConnection();
            prepStat = connection.prepareStatement(selectTableNamesFromOracle);
            prepStat.setString(1, databaseTablePrefix + "ACT_%");
            tablesRs = prepStat.executeQuery();
            while (tablesRs.next()) {
                String tableName = tablesRs.getString("TABLE_NAME");
                tableName = tableName.toUpperCase();
                tableNames.add(tableName);
            }
            DbSqlSession.LOG.fetchDatabaseTables("oracle all_tables", tableNames);
        }
        finally {
            if (tablesRs != null) {
                tablesRs.close();
            }
            if (prepStat != null) {
                prepStat.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return tableNames;
    }
    
    public String prependDatabaseTablePrefix(final String tableName) {
        String prefixWithoutSchema = this.dbSqlSessionFactory.getDatabaseTablePrefix();
        final String schema = this.dbSqlSessionFactory.getDatabaseSchema();
        if (prefixWithoutSchema == null) {
            return tableName;
        }
        if (schema == null) {
            return prefixWithoutSchema + tableName;
        }
        if (prefixWithoutSchema.startsWith(schema + ".")) {
            prefixWithoutSchema = prefixWithoutSchema.substring(schema.length() + 1);
        }
        return prefixWithoutSchema + tableName;
    }
    
    public String getResourceForDbOperation(final String directory, final String operation, final String component) {
        final String databaseType = this.dbSqlSessionFactory.getDatabaseType();
        return "org/camunda/bpm/engine/db/" + directory + "/activiti." + databaseType + "." + operation + "." + component + ".sql";
    }
    
    public void executeSchemaResource(final String operation, final String component, final String resourceName, final boolean isOptional) {
        InputStream inputStream = null;
        try {
            inputStream = ReflectUtil.getResourceAsStream(resourceName);
            if (inputStream == null) {
                if (!isOptional) {
                    throw DbSqlSession.LOG.missingSchemaResourceException(resourceName, operation);
                }
                DbSqlSession.LOG.missingSchemaResource(resourceName, operation);
            }
            else {
                this.executeSchemaResource(operation, component, resourceName, inputStream);
            }
        }
        finally {
            IoUtil.closeSilently(inputStream);
        }
    }
    
    public void executeSchemaResource(final String schemaFileResourceName) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(schemaFileResourceName));
            this.executeSchemaResource("schema operation", "process engine", schemaFileResourceName, inputStream);
        }
        catch (FileNotFoundException e) {
            throw DbSqlSession.LOG.missingSchemaResourceFileException(schemaFileResourceName, e);
        }
        finally {
            IoUtil.closeSilently(inputStream);
        }
    }
    
    private void executeSchemaResource(final String operation, final String component, final String resourceName, final InputStream inputStream) {
        String sqlStatement = null;
        String exceptionSqlStatement = null;
        try {
            final Connection connection = ExceptionUtil.doWithExceptionWrapper(() -> this.sqlSession.getConnection());
            Exception exception = null;
            final byte[] bytes = IoUtil.readInputStream(inputStream, resourceName);
            final String ddlStatements = new String(bytes);
            final BufferedReader reader = new BufferedReader(new StringReader(ddlStatements));
            String line = this.readNextTrimmedLine(reader);
            final List<String> logLines = new ArrayList<String>();
            while (line != null) {
                if (line.startsWith("# ")) {
                    logLines.add(line.substring(2));
                }
                else if (line.startsWith("-- ")) {
                    logLines.add(line.substring(3));
                }
                else if (line.length() > 0) {
                    if (line.endsWith(";")) {
                        sqlStatement = this.addSqlStatementPiece(sqlStatement, line.substring(0, line.length() - 1));
                        try {
                            final Statement jdbcStatement = connection.createStatement();
                            logLines.add(sqlStatement);
                            jdbcStatement.execute(sqlStatement);
                            jdbcStatement.close();
                        }
                        catch (Exception e) {
                            if (exception == null) {
                                exception = e;
                                exceptionSqlStatement = sqlStatement;
                            }
                            DbSqlSession.LOG.failedDatabaseOperation(operation, sqlStatement, e);
                        }
                        finally {
                            sqlStatement = null;
                        }
                    }
                    else {
                        sqlStatement = this.addSqlStatementPiece(sqlStatement, line);
                    }
                }
                line = this.readNextTrimmedLine(reader);
            }
            DbSqlSession.LOG.performingDatabaseOperation(operation, component, resourceName);
            DbSqlSession.LOG.executingDDL(logLines);
            if (exception != null) {
                throw exception;
            }
            DbSqlSession.LOG.successfulDatabaseOperation(operation, component);
        }
        catch (Exception e2) {
            throw DbSqlSession.LOG.performDatabaseOperationException(operation, exceptionSqlStatement, e2);
        }
    }
    
    protected String addSqlStatementPiece(final String sqlStatement, final String line) {
        if (sqlStatement == null) {
            return line;
        }
        return sqlStatement + " \n" + line;
    }
    
    protected String readNextTrimmedLine(final BufferedReader reader) throws IOException {
        String line = reader.readLine();
        if (line != null) {
            line = line.trim();
        }
        return line;
    }
    
    protected boolean isMissingTablesException(final Exception e) {
        final Throwable cause = e.getCause();
        if (cause != null) {
            final String exceptionMessage = cause.getMessage();
            if (cause.getMessage() != null) {
                return (exceptionMessage.contains("Table") && exceptionMessage.contains("not found")) || ((exceptionMessage.contains("Table") || exceptionMessage.contains("table")) && exceptionMessage.contains("doesn't exist")) || ((exceptionMessage.contains("relation") || exceptionMessage.contains("table")) && exceptionMessage.contains("does not exist"));
            }
        }
        return false;
    }
    
    protected String[] getTableTypes() {
        if (DatabaseUtil.checkDatabaseType("postgres")) {
            return DbSqlSession.PG_JDBC_METADATA_TABLE_TYPES;
        }
        return DbSqlSession.JDBC_METADATA_TABLE_TYPES;
    }
    
    public SqlSession getSqlSession() {
        return this.sqlSession;
    }
    
    public DbSqlSessionFactory getDbSqlSessionFactory() {
        return this.dbSqlSessionFactory;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
        JDBC_METADATA_TABLE_TYPES = new String[] { "TABLE" };
        PG_JDBC_METADATA_TABLE_TYPES = new String[] { "TABLE", "PARTITIONED TABLE" };
    }
}
