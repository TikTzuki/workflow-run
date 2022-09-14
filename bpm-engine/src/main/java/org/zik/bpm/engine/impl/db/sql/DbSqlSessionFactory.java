// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.db.sql;

import java.util.Iterator;
import java.util.Arrays;
import java.util.HashMap;
import org.zik.bpm.engine.impl.util.ClassNameUtil;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.sql.Connection;
import org.zik.bpm.engine.impl.interceptor.Session;
import java.util.concurrent.ConcurrentHashMap;
import org.zik.bpm.engine.impl.cfg.IdGenerator;
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.SessionFactory;

public class DbSqlSessionFactory implements SessionFactory
{
    public static final String MSSQL = "mssql";
    public static final String DB2 = "db2";
    public static final String ORACLE = "oracle";
    public static final String H2 = "h2";
    public static final String MYSQL = "mysql";
    public static final String POSTGRES = "postgres";
    public static final String MARIADB = "mariadb";
    public static final String CRDB = "cockroachdb";
    public static final String[] SUPPORTED_DATABASES;
    protected static final Map<String, Map<String, String>> databaseSpecificStatements;
    public static final Map<String, String> databaseSpecificLimitBeforeStatements;
    public static final Map<String, String> databaseSpecificLimitAfterStatements;
    public static final Map<String, String> databaseSpecificLimitBeforeWithoutOffsetStatements;
    public static final Map<String, String> databaseSpecificLimitAfterWithoutOffsetStatements;
    public static final Map<String, String> databaseSpecificInnerLimitAfterStatements;
    public static final Map<String, String> databaseSpecificLimitBetweenStatements;
    public static final Map<String, String> databaseSpecificLimitBetweenFilterStatements;
    public static final Map<String, String> databaseSpecificLimitBetweenAcquisitionStatements;
    public static final Map<String, String> databaseSpecificCountDistinctBeforeStart;
    public static final Map<String, String> databaseSpecificCountDistinctBeforeEnd;
    public static final Map<String, String> databaseSpecificCountDistinctAfterEnd;
    public static final Map<String, String> optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements;
    public static final Map<String, String> optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements;
    public static final Map<String, String> databaseSpecificEscapeChar;
    public static final Map<String, String> databaseSpecificOrderByStatements;
    public static final Map<String, String> databaseSpecificLimitBeforeNativeQueryStatements;
    public static final Map<String, String> databaseSpecificBitAnd1;
    public static final Map<String, String> databaseSpecificBitAnd2;
    public static final Map<String, String> databaseSpecificBitAnd3;
    public static final Map<String, String> databaseSpecificDatepart1;
    public static final Map<String, String> databaseSpecificDatepart2;
    public static final Map<String, String> databaseSpecificDatepart3;
    public static final Map<String, String> databaseSpecificDummyTable;
    public static final Map<String, String> databaseSpecificIfNull;
    public static final Map<String, String> databaseSpecificTrueConstant;
    public static final Map<String, String> databaseSpecificFalseConstant;
    public static final Map<String, String> databaseSpecificDistinct;
    public static final Map<String, String> databaseSpecificNumericCast;
    public static final Map<String, Map<String, String>> dbSpecificConstants;
    public static final Map<String, String> databaseSpecificDaysComparator;
    public static final Map<String, String> databaseSpecificCollationForCaseSensitivity;
    public static final Map<String, String> databaseSpecificAuthJoinStart;
    public static final Map<String, String> databaseSpecificAuthJoinEnd;
    public static final Map<String, String> databaseSpecificAuthJoinSeparator;
    public static final Map<String, String> databaseSpecificAuth1JoinStart;
    public static final Map<String, String> databaseSpecificAuth1JoinEnd;
    public static final Map<String, String> databaseSpecificAuth1JoinSeparator;
    public static final int MAXIMUM_NUMBER_PARAMS = 2000;
    protected String databaseType;
    protected String databaseTablePrefix;
    protected String databaseSchema;
    protected SqlSessionFactory sqlSessionFactory;
    protected IdGenerator idGenerator;
    protected Map<String, String> statementMappings;
    protected Map<Class<?>, String> insertStatements;
    protected Map<Class<?>, String> updateStatements;
    protected Map<Class<?>, String> deleteStatements;
    protected Map<Class<?>, String> selectStatements;
    protected boolean isDbIdentityUsed;
    protected boolean isDbHistoryUsed;
    protected boolean cmmnEnabled;
    protected boolean dmnEnabled;
    protected boolean jdbcBatchProcessing;
    
    public DbSqlSessionFactory(final boolean jdbcBatchProcessing) {
        this.databaseTablePrefix = "";
        this.insertStatements = new ConcurrentHashMap<Class<?>, String>();
        this.updateStatements = new ConcurrentHashMap<Class<?>, String>();
        this.deleteStatements = new ConcurrentHashMap<Class<?>, String>();
        this.selectStatements = new ConcurrentHashMap<Class<?>, String>();
        this.isDbIdentityUsed = true;
        this.isDbHistoryUsed = true;
        this.cmmnEnabled = true;
        this.dmnEnabled = true;
        this.jdbcBatchProcessing = jdbcBatchProcessing;
    }
    
    @Override
    public Class<?> getSessionType() {
        return DbSqlSession.class;
    }
    
    @Override
    public Session openSession() {
        return this.jdbcBatchProcessing ? new BatchDbSqlSession(this) : new SimpleDbSqlSession(this);
    }
    
    public DbSqlSession openSession(final Connection connection, final String catalog, final String schema) {
        return this.jdbcBatchProcessing ? new BatchDbSqlSession(this, connection, catalog, schema) : new SimpleDbSqlSession(this, connection, catalog, schema);
    }
    
    public String getInsertStatement(final DbEntity object) {
        return this.getStatement(object.getClass(), this.insertStatements, "insert");
    }
    
    public String getUpdateStatement(final DbEntity object) {
        return this.getStatement(object.getClass(), this.updateStatements, "update");
    }
    
    public String getDeleteStatement(final Class<?> persistentObjectClass) {
        return this.getStatement(persistentObjectClass, this.deleteStatements, "delete");
    }
    
    public String getSelectStatement(final Class<?> persistentObjectClass) {
        return this.getStatement(persistentObjectClass, this.selectStatements, "select");
    }
    
    private String getStatement(final Class<?> persistentObjectClass, final Map<Class<?>, String> cachedStatements, final String prefix) {
        String statement = cachedStatements.get(persistentObjectClass);
        if (statement != null) {
            return statement;
        }
        statement = prefix + ClassNameUtil.getClassNameWithoutPackage(persistentObjectClass);
        statement = statement.substring(0, statement.length() - 6);
        cachedStatements.put(persistentObjectClass, statement);
        return statement;
    }
    
    protected static void addDatabaseSpecificStatement(final String databaseType, final String activitiStatement, final String ibatisStatement) {
        Map<String, String> specificStatements = DbSqlSessionFactory.databaseSpecificStatements.get(databaseType);
        if (specificStatements == null) {
            specificStatements = new HashMap<String, String>();
            DbSqlSessionFactory.databaseSpecificStatements.put(databaseType, specificStatements);
        }
        specificStatements.put(activitiStatement, ibatisStatement);
    }
    
    public String mapStatement(final String statement) {
        if (this.statementMappings == null) {
            return statement;
        }
        final String mappedStatement = this.statementMappings.get(statement);
        return (mappedStatement != null) ? mappedStatement : statement;
    }
    
    public void setDatabaseType(final String databaseType) {
        this.databaseType = databaseType;
        this.statementMappings = DbSqlSessionFactory.databaseSpecificStatements.get(databaseType);
    }
    
    public SqlSessionFactory getSqlSessionFactory() {
        return this.sqlSessionFactory;
    }
    
    public void setSqlSessionFactory(final SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }
    
    public IdGenerator getIdGenerator() {
        return this.idGenerator;
    }
    
    public void setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }
    
    public String getDatabaseType() {
        return this.databaseType;
    }
    
    public Map<String, String> getStatementMappings() {
        return this.statementMappings;
    }
    
    public void setStatementMappings(final Map<String, String> statementMappings) {
        this.statementMappings = statementMappings;
    }
    
    public Map<Class<?>, String> getInsertStatements() {
        return this.insertStatements;
    }
    
    public void setInsertStatements(final Map<Class<?>, String> insertStatements) {
        this.insertStatements = insertStatements;
    }
    
    public Map<Class<?>, String> getUpdateStatements() {
        return this.updateStatements;
    }
    
    public void setUpdateStatements(final Map<Class<?>, String> updateStatements) {
        this.updateStatements = updateStatements;
    }
    
    public Map<Class<?>, String> getDeleteStatements() {
        return this.deleteStatements;
    }
    
    public void setDeleteStatements(final Map<Class<?>, String> deleteStatements) {
        this.deleteStatements = deleteStatements;
    }
    
    public Map<Class<?>, String> getSelectStatements() {
        return this.selectStatements;
    }
    
    public void setSelectStatements(final Map<Class<?>, String> selectStatements) {
        this.selectStatements = selectStatements;
    }
    
    public boolean isDbIdentityUsed() {
        return this.isDbIdentityUsed;
    }
    
    public void setDbIdentityUsed(final boolean isDbIdentityUsed) {
        this.isDbIdentityUsed = isDbIdentityUsed;
    }
    
    public boolean isDbHistoryUsed() {
        return this.isDbHistoryUsed;
    }
    
    public void setDbHistoryUsed(final boolean isDbHistoryUsed) {
        this.isDbHistoryUsed = isDbHistoryUsed;
    }
    
    public boolean isCmmnEnabled() {
        return this.cmmnEnabled;
    }
    
    public void setCmmnEnabled(final boolean cmmnEnabled) {
        this.cmmnEnabled = cmmnEnabled;
    }
    
    public boolean isDmnEnabled() {
        return this.dmnEnabled;
    }
    
    public void setDmnEnabled(final boolean dmnEnabled) {
        this.dmnEnabled = dmnEnabled;
    }
    
    public void setDatabaseTablePrefix(final String databaseTablePrefix) {
        this.databaseTablePrefix = databaseTablePrefix;
    }
    
    public String getDatabaseTablePrefix() {
        return this.databaseTablePrefix;
    }
    
    public String getDatabaseSchema() {
        return this.databaseSchema;
    }
    
    public void setDatabaseSchema(final String databaseSchema) {
        this.databaseSchema = databaseSchema;
    }
    
    static {
        SUPPORTED_DATABASES = new String[] { "mssql", "db2", "oracle", "h2", "mysql", "postgres", "mariadb", "cockroachdb" };
        databaseSpecificStatements = new HashMap<String, Map<String, String>>();
        databaseSpecificLimitBeforeStatements = new HashMap<String, String>();
        databaseSpecificLimitAfterStatements = new HashMap<String, String>();
        databaseSpecificLimitBeforeWithoutOffsetStatements = new HashMap<String, String>();
        databaseSpecificLimitAfterWithoutOffsetStatements = new HashMap<String, String>();
        databaseSpecificInnerLimitAfterStatements = new HashMap<String, String>();
        databaseSpecificLimitBetweenStatements = new HashMap<String, String>();
        databaseSpecificLimitBetweenFilterStatements = new HashMap<String, String>();
        databaseSpecificLimitBetweenAcquisitionStatements = new HashMap<String, String>();
        databaseSpecificCountDistinctBeforeStart = new HashMap<String, String>();
        databaseSpecificCountDistinctBeforeEnd = new HashMap<String, String>();
        databaseSpecificCountDistinctAfterEnd = new HashMap<String, String>();
        optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements = new HashMap<String, String>();
        optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements = new HashMap<String, String>();
        databaseSpecificEscapeChar = new HashMap<String, String>();
        databaseSpecificOrderByStatements = new HashMap<String, String>();
        databaseSpecificLimitBeforeNativeQueryStatements = new HashMap<String, String>();
        databaseSpecificBitAnd1 = new HashMap<String, String>();
        databaseSpecificBitAnd2 = new HashMap<String, String>();
        databaseSpecificBitAnd3 = new HashMap<String, String>();
        databaseSpecificDatepart1 = new HashMap<String, String>();
        databaseSpecificDatepart2 = new HashMap<String, String>();
        databaseSpecificDatepart3 = new HashMap<String, String>();
        databaseSpecificDummyTable = new HashMap<String, String>();
        databaseSpecificIfNull = new HashMap<String, String>();
        databaseSpecificTrueConstant = new HashMap<String, String>();
        databaseSpecificFalseConstant = new HashMap<String, String>();
        databaseSpecificDistinct = new HashMap<String, String>();
        databaseSpecificNumericCast = new HashMap<String, String>();
        dbSpecificConstants = new HashMap<String, Map<String, String>>();
        databaseSpecificDaysComparator = new HashMap<String, String>();
        databaseSpecificCollationForCaseSensitivity = new HashMap<String, String>();
        databaseSpecificAuthJoinStart = new HashMap<String, String>();
        databaseSpecificAuthJoinEnd = new HashMap<String, String>();
        databaseSpecificAuthJoinSeparator = new HashMap<String, String>();
        databaseSpecificAuth1JoinStart = new HashMap<String, String>();
        databaseSpecificAuth1JoinEnd = new HashMap<String, String>();
        databaseSpecificAuth1JoinSeparator = new HashMap<String, String>();
        final String defaultOrderBy = "order by ${internalOrderBy}";
        final String defaultEscapeChar = "'\\'";
        final String defaultDistinctCountBeforeStart = "select count(distinct";
        final String defaultDistinctCountBeforeEnd = ")";
        final String defaultDistinctCountAfterEnd = "";
        final String defaultAuthOnStart = "IN (";
        final String defaultAuthOnEnd = ")";
        final String defaultAuthOnSeparator = ",";
        DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.put("h2", "");
        DbSqlSessionFactory.optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements.put("h2", "");
        DbSqlSessionFactory.databaseSpecificLimitAfterStatements.put("h2", "LIMIT #{maxResults} OFFSET #{firstResult}");
        DbSqlSessionFactory.optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements.put("h2", "LIMIT #{maxResults}");
        DbSqlSessionFactory.databaseSpecificLimitBeforeWithoutOffsetStatements.put("h2", "");
        DbSqlSessionFactory.databaseSpecificLimitAfterWithoutOffsetStatements.put("h2", "LIMIT #{maxResults}");
        DbSqlSessionFactory.databaseSpecificInnerLimitAfterStatements.put("h2", DbSqlSessionFactory.databaseSpecificLimitAfterStatements.get("h2"));
        DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.put("h2", "");
        DbSqlSessionFactory.databaseSpecificLimitBetweenFilterStatements.put("h2", "");
        DbSqlSessionFactory.databaseSpecificLimitBetweenAcquisitionStatements.put("h2", "");
        DbSqlSessionFactory.databaseSpecificOrderByStatements.put("h2", defaultOrderBy);
        DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.put("h2", "");
        DbSqlSessionFactory.databaseSpecificDistinct.put("h2", "distinct");
        DbSqlSessionFactory.databaseSpecificNumericCast.put("h2", "");
        DbSqlSessionFactory.databaseSpecificCountDistinctBeforeStart.put("h2", defaultDistinctCountBeforeStart);
        DbSqlSessionFactory.databaseSpecificCountDistinctBeforeEnd.put("h2", defaultDistinctCountBeforeEnd);
        DbSqlSessionFactory.databaseSpecificCountDistinctAfterEnd.put("h2", defaultDistinctCountAfterEnd);
        DbSqlSessionFactory.databaseSpecificEscapeChar.put("h2", defaultEscapeChar);
        DbSqlSessionFactory.databaseSpecificBitAnd1.put("h2", "BITAND(");
        DbSqlSessionFactory.databaseSpecificBitAnd2.put("h2", ",CAST(");
        DbSqlSessionFactory.databaseSpecificBitAnd3.put("h2", " AS BIGINT))");
        DbSqlSessionFactory.databaseSpecificDatepart1.put("h2", "");
        DbSqlSessionFactory.databaseSpecificDatepart2.put("h2", "(");
        DbSqlSessionFactory.databaseSpecificDatepart3.put("h2", ")");
        DbSqlSessionFactory.databaseSpecificDummyTable.put("h2", "");
        DbSqlSessionFactory.databaseSpecificTrueConstant.put("h2", "true");
        DbSqlSessionFactory.databaseSpecificFalseConstant.put("h2", "false");
        DbSqlSessionFactory.databaseSpecificIfNull.put("h2", "IFNULL");
        DbSqlSessionFactory.databaseSpecificDaysComparator.put("h2", "DATEDIFF(DAY, ${date}, #{currentTimestamp}) >= ${days}");
        DbSqlSessionFactory.databaseSpecificCollationForCaseSensitivity.put("h2", "");
        DbSqlSessionFactory.databaseSpecificAuthJoinStart.put("h2", defaultAuthOnStart);
        DbSqlSessionFactory.databaseSpecificAuthJoinEnd.put("h2", defaultAuthOnEnd);
        DbSqlSessionFactory.databaseSpecificAuthJoinSeparator.put("h2", defaultAuthOnSeparator);
        DbSqlSessionFactory.databaseSpecificAuth1JoinStart.put("h2", defaultAuthOnStart);
        DbSqlSessionFactory.databaseSpecificAuth1JoinEnd.put("h2", defaultAuthOnEnd);
        DbSqlSessionFactory.databaseSpecificAuth1JoinSeparator.put("h2", defaultAuthOnSeparator);
        HashMap<String, String> constants = new HashMap<String, String>();
        constants.put("constant.event", "'event'");
        constants.put("constant.op_message", "NEW_VALUE_ || '_|_' || PROPERTY_");
        constants.put("constant_for_update", "for update");
        constants.put("constant.datepart.quarter", "QUARTER");
        constants.put("constant.datepart.month", "MONTH");
        constants.put("constant.datepart.minute", "MINUTE");
        constants.put("constant.null.startTime", "null START_TIME_");
        constants.put("constant.varchar.cast", "'${key}'");
        constants.put("constant.integer.cast", "NULL");
        constants.put("constant.null.reporter", "NULL AS REPORTER_");
        DbSqlSessionFactory.dbSpecificConstants.put("h2", constants);
        for (final String mysqlLikeDatabase : Arrays.asList("mysql", "mariadb")) {
            DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificLimitAfterStatements.put(mysqlLikeDatabase, "LIMIT #{maxResults} OFFSET #{firstResult}");
            DbSqlSessionFactory.optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements.put(mysqlLikeDatabase, "LIMIT #{maxResults}");
            DbSqlSessionFactory.databaseSpecificLimitBeforeWithoutOffsetStatements.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificLimitAfterWithoutOffsetStatements.put(mysqlLikeDatabase, "LIMIT #{maxResults}");
            DbSqlSessionFactory.databaseSpecificInnerLimitAfterStatements.put(mysqlLikeDatabase, DbSqlSessionFactory.databaseSpecificLimitAfterStatements.get(mysqlLikeDatabase));
            DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificLimitBetweenFilterStatements.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificLimitBetweenAcquisitionStatements.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificOrderByStatements.put(mysqlLikeDatabase, defaultOrderBy);
            DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificDistinct.put(mysqlLikeDatabase, "distinct");
            DbSqlSessionFactory.databaseSpecificNumericCast.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificCountDistinctBeforeStart.put(mysqlLikeDatabase, defaultDistinctCountBeforeStart);
            DbSqlSessionFactory.databaseSpecificCountDistinctBeforeEnd.put(mysqlLikeDatabase, defaultDistinctCountBeforeEnd);
            DbSqlSessionFactory.databaseSpecificCountDistinctAfterEnd.put(mysqlLikeDatabase, defaultDistinctCountAfterEnd);
            DbSqlSessionFactory.databaseSpecificEscapeChar.put(mysqlLikeDatabase, "'\\\\'");
            DbSqlSessionFactory.databaseSpecificBitAnd1.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificBitAnd2.put(mysqlLikeDatabase, " & ");
            DbSqlSessionFactory.databaseSpecificBitAnd3.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificDatepart1.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificDatepart2.put(mysqlLikeDatabase, "(");
            DbSqlSessionFactory.databaseSpecificDatepart3.put(mysqlLikeDatabase, ")");
            DbSqlSessionFactory.databaseSpecificDummyTable.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificTrueConstant.put(mysqlLikeDatabase, "1");
            DbSqlSessionFactory.databaseSpecificFalseConstant.put(mysqlLikeDatabase, "0");
            DbSqlSessionFactory.databaseSpecificIfNull.put(mysqlLikeDatabase, "IFNULL");
            DbSqlSessionFactory.databaseSpecificDaysComparator.put(mysqlLikeDatabase, "DATEDIFF(#{currentTimestamp}, ${date}) >= ${days}");
            DbSqlSessionFactory.databaseSpecificCollationForCaseSensitivity.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificAuthJoinStart.put(mysqlLikeDatabase, "=");
            DbSqlSessionFactory.databaseSpecificAuthJoinEnd.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificAuthJoinSeparator.put(mysqlLikeDatabase, "OR AUTH.RESOURCE_ID_ =");
            DbSqlSessionFactory.databaseSpecificAuth1JoinStart.put(mysqlLikeDatabase, "=");
            DbSqlSessionFactory.databaseSpecificAuth1JoinEnd.put(mysqlLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificAuth1JoinSeparator.put(mysqlLikeDatabase, "OR AUTH1.RESOURCE_ID_ =");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "toggleForeignKey", "toggleForeignKey_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "selectDeploymentsByQueryCriteria", "selectDeploymentsByQueryCriteria_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "selectDeploymentCountByQueryCriteria", "selectDeploymentCountByQueryCriteria_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteExceptionByteArraysByIds", "deleteExceptionByteArraysByIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteErrorDetailsByteArraysByIds", "deleteErrorDetailsByteArraysByIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteHistoricDetailsByIds", "deleteHistoricDetailsByIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteHistoricDetailByteArraysByIds", "deleteHistoricDetailByteArraysByIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteHistoricIdentityLinksByTaskProcessInstanceIds", "deleteHistoricIdentityLinksByTaskProcessInstanceIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteHistoricIdentityLinksByTaskCaseInstanceIds", "deleteHistoricIdentityLinksByTaskCaseInstanceIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteHistoricDecisionInputInstanceByteArraysByDecisionInstanceIds", "deleteHistoricDecisionInputInstanceByteArraysByDecisionInstanceIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteHistoricDecisionOutputInstanceByteArraysByDecisionInstanceIds", "deleteHistoricDecisionOutputInstanceByteArraysByDecisionInstanceIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteHistoricVariableInstanceByIds", "deleteHistoricVariableInstanceByIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteHistoricVariableInstanceByteArraysByIds", "deleteHistoricVariableInstanceByteArraysByIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteCommentsByIds", "deleteCommentsByIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteAttachmentByteArraysByIds", "deleteAttachmentByteArraysByIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteAttachmentByIds", "deleteAttachmentByIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "deleteHistoricIncidentsByBatchIds", "deleteHistoricIncidentsByBatchIds_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateUserOperationLogByRootProcessInstanceId", "updateUserOperationLogByRootProcessInstanceId_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateExternalTaskLogByRootProcessInstanceId", "updateExternalTaskLogByRootProcessInstanceId_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateHistoricIncidentsByRootProcessInstanceId", "updateHistoricIncidentsByRootProcessInstanceId_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateHistoricIncidentsByBatchId", "updateHistoricIncidentsByBatchId_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateIdentityLinkLogByRootProcessInstanceId", "updateIdentityLinkLogByRootProcessInstanceId_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateUserOperationLogByProcessInstanceId", "updateUserOperationLogByProcessInstanceId_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateExternalTaskLogByProcessInstanceId", "updateExternalTaskLogByProcessInstanceId_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateHistoricIncidentsByProcessInstanceId", "updateHistoricIncidentsByProcessInstanceId_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateIdentityLinkLogByProcessInstanceId", "updateIdentityLinkLogByProcessInstanceId_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateOperationLogAnnotationByOperationId", "updateOperationLogAnnotationByOperationId_mysql");
            addDatabaseSpecificStatement(mysqlLikeDatabase, "updateByteArraysByBatchId", "updateByteArraysByBatchId_mysql");
            constants = new HashMap<String, String>();
            constants.put("constant.event", "'event'");
            constants.put("constant.op_message", "CONCAT(NEW_VALUE_, '_|_', PROPERTY_)");
            constants.put("constant_for_update", "for update");
            constants.put("constant.datepart.quarter", "QUARTER");
            constants.put("constant.datepart.month", "MONTH");
            constants.put("constant.datepart.minute", "MINUTE");
            constants.put("constant.null.startTime", "null START_TIME_");
            constants.put("constant.varchar.cast", "'${key}'");
            constants.put("constant.integer.cast", "NULL");
            constants.put("constant.null.reporter", "NULL AS REPORTER_");
            DbSqlSessionFactory.dbSpecificConstants.put(mysqlLikeDatabase, constants);
        }
        for (final String postgresLikeDatabase : Arrays.asList("postgres", "cockroachdb")) {
            DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificLimitAfterStatements.put(postgresLikeDatabase, "LIMIT #{maxResults} OFFSET #{firstResult}");
            DbSqlSessionFactory.optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements.put(postgresLikeDatabase, "LIMIT #{maxResults}");
            DbSqlSessionFactory.databaseSpecificLimitBeforeWithoutOffsetStatements.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificLimitAfterWithoutOffsetStatements.put(postgresLikeDatabase, "LIMIT #{maxResults}");
            DbSqlSessionFactory.databaseSpecificInnerLimitAfterStatements.put(postgresLikeDatabase, DbSqlSessionFactory.databaseSpecificLimitAfterStatements.get("postgres"));
            DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificLimitBetweenFilterStatements.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificLimitBetweenAcquisitionStatements.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificOrderByStatements.put(postgresLikeDatabase, defaultOrderBy);
            DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificDistinct.put(postgresLikeDatabase, "distinct");
            DbSqlSessionFactory.databaseSpecificCountDistinctBeforeStart.put(postgresLikeDatabase, "SELECT COUNT(*) FROM (SELECT DISTINCT");
            DbSqlSessionFactory.databaseSpecificCountDistinctBeforeEnd.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificCountDistinctAfterEnd.put(postgresLikeDatabase, ") countDistinct");
            DbSqlSessionFactory.databaseSpecificEscapeChar.put(postgresLikeDatabase, defaultEscapeChar);
            DbSqlSessionFactory.databaseSpecificBitAnd1.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificBitAnd2.put(postgresLikeDatabase, " & ");
            DbSqlSessionFactory.databaseSpecificBitAnd3.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificDatepart1.put(postgresLikeDatabase, "extract(");
            DbSqlSessionFactory.databaseSpecificDatepart2.put(postgresLikeDatabase, " from ");
            DbSqlSessionFactory.databaseSpecificDatepart3.put(postgresLikeDatabase, ")");
            DbSqlSessionFactory.databaseSpecificDummyTable.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificTrueConstant.put(postgresLikeDatabase, "true");
            DbSqlSessionFactory.databaseSpecificFalseConstant.put(postgresLikeDatabase, "false");
            DbSqlSessionFactory.databaseSpecificIfNull.put(postgresLikeDatabase, "COALESCE");
            DbSqlSessionFactory.databaseSpecificCollationForCaseSensitivity.put(postgresLikeDatabase, "");
            DbSqlSessionFactory.databaseSpecificAuthJoinStart.put(postgresLikeDatabase, defaultAuthOnStart);
            DbSqlSessionFactory.databaseSpecificAuthJoinEnd.put(postgresLikeDatabase, defaultAuthOnEnd);
            DbSqlSessionFactory.databaseSpecificAuthJoinSeparator.put(postgresLikeDatabase, defaultAuthOnSeparator);
            DbSqlSessionFactory.databaseSpecificAuth1JoinStart.put(postgresLikeDatabase, defaultAuthOnStart);
            DbSqlSessionFactory.databaseSpecificAuth1JoinEnd.put(postgresLikeDatabase, defaultAuthOnEnd);
            DbSqlSessionFactory.databaseSpecificAuth1JoinSeparator.put(postgresLikeDatabase, defaultAuthOnSeparator);
            addDatabaseSpecificStatement(postgresLikeDatabase, "insertByteArray", "insertByteArray_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "updateByteArray", "updateByteArray_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectByteArray", "selectByteArray_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectByteArrays", "selectByteArrays_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectResourceByDeploymentIdAndResourceName", "selectResourceByDeploymentIdAndResourceName_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectResourceByDeploymentIdAndResourceNames", "selectResourceByDeploymentIdAndResourceNames_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectResourceByDeploymentIdAndResourceId", "selectResourceByDeploymentIdAndResourceId_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectResourceByDeploymentIdAndResourceIds", "selectResourceByDeploymentIdAndResourceIds_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectResourcesByDeploymentId", "selectResourcesByDeploymentId_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectResourceById", "selectResourceById_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectLatestResourcesByDeploymentName", "selectLatestResourcesByDeploymentName_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "insertIdentityInfo", "insertIdentityInfo_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "updateIdentityInfo", "updateIdentityInfo_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectIdentityInfoById", "selectIdentityInfoById_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectIdentityInfoByUserIdAndKey", "selectIdentityInfoByUserIdAndKey_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectIdentityInfoByUserId", "selectIdentityInfoByUserId_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectIdentityInfoDetails", "selectIdentityInfoDetails_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "insertComment", "insertComment_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectCommentsByTaskId", "selectCommentsByTaskId_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectCommentsByProcessInstanceId", "selectCommentsByProcessInstanceId_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectCommentByTaskIdAndCommentId", "selectCommentByTaskIdAndCommentId_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectEventsByTaskId", "selectEventsByTaskId_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectFilterByQueryCriteria", "selectFilterByQueryCriteria_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "selectFilter", "selectFilter_postgres");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteAttachmentsByRemovalTime", "deleteAttachmentsByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteCommentsByRemovalTime", "deleteCommentsByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricActivityInstancesByRemovalTime", "deleteHistoricActivityInstancesByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricDecisionInputInstancesByRemovalTime", "deleteHistoricDecisionInputInstancesByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricDecisionInstancesByRemovalTime", "deleteHistoricDecisionInstancesByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricDecisionOutputInstancesByRemovalTime", "deleteHistoricDecisionOutputInstancesByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricDetailsByRemovalTime", "deleteHistoricDetailsByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteExternalTaskLogByRemovalTime", "deleteExternalTaskLogByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricIdentityLinkLogByRemovalTime", "deleteHistoricIdentityLinkLogByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricIncidentsByRemovalTime", "deleteHistoricIncidentsByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteJobLogByRemovalTime", "deleteJobLogByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricProcessInstancesByRemovalTime", "deleteHistoricProcessInstancesByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricTaskInstancesByRemovalTime", "deleteHistoricTaskInstancesByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricVariableInstancesByRemovalTime", "deleteHistoricVariableInstancesByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteUserOperationLogByRemovalTime", "deleteUserOperationLogByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteByteArraysByRemovalTime", "deleteByteArraysByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteHistoricBatchesByRemovalTime", "deleteHistoricBatchesByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteAuthorizationsByRemovalTime", "deleteAuthorizationsByRemovalTime_postgres_or_db2");
            addDatabaseSpecificStatement(postgresLikeDatabase, "deleteTaskMetricsByRemovalTime", "deleteTaskMetricsByRemovalTime_postgres_or_db2");
            constants = new HashMap<String, String>();
            constants.put("constant.event", "'event'");
            constants.put("constant.op_message", "NEW_VALUE_ || '_|_' || PROPERTY_");
            constants.put("constant_for_update", "for update");
            constants.put("constant.datepart.quarter", "QUARTER");
            constants.put("constant.datepart.month", "MONTH");
            constants.put("constant.datepart.minute", "MINUTE");
            constants.put("constant.null.startTime", "null START_TIME_");
            constants.put("constant.varchar.cast", "cast('${key}' as varchar(64))");
            constants.put("constant.integer.cast", "cast(NULL as integer)");
            constants.put("constant.null.reporter", "CAST(NULL AS VARCHAR) AS REPORTER_");
            DbSqlSessionFactory.dbSpecificConstants.put(postgresLikeDatabase, constants);
        }
        DbSqlSessionFactory.databaseSpecificDaysComparator.put("postgres", "EXTRACT (DAY FROM #{currentTimestamp} - ${date}) >= ${days}");
        DbSqlSessionFactory.databaseSpecificNumericCast.put("postgres", "");
        DbSqlSessionFactory.databaseSpecificDaysComparator.put("cockroachdb", "CAST( EXTRACT (HOUR FROM #{currentTimestamp} - ${date}) / 24 AS INT ) >= ${days}");
        DbSqlSessionFactory.databaseSpecificNumericCast.put("cockroachdb", "::NUMERIC");
        DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.put("oracle", "select * from ( select a.*, ROWNUM rnum from (");
        DbSqlSessionFactory.optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements.put("oracle", "select * from ( select a.*, ROWNUM rnum from (");
        DbSqlSessionFactory.databaseSpecificLimitAfterStatements.put("oracle", "  ) a where ROWNUM < #{lastRow}) where rnum  >= #{firstRow}");
        DbSqlSessionFactory.optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements.put("oracle", "  ) a where ROWNUM <= #{maxResults})");
        DbSqlSessionFactory.databaseSpecificLimitBeforeWithoutOffsetStatements.put("oracle", "");
        DbSqlSessionFactory.databaseSpecificLimitAfterWithoutOffsetStatements.put("oracle", "AND ROWNUM <= #{maxResults}");
        DbSqlSessionFactory.databaseSpecificInnerLimitAfterStatements.put("oracle", DbSqlSessionFactory.databaseSpecificLimitAfterStatements.get("oracle"));
        DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.put("oracle", "");
        DbSqlSessionFactory.databaseSpecificLimitBetweenFilterStatements.put("oracle", "");
        DbSqlSessionFactory.databaseSpecificLimitBetweenAcquisitionStatements.put("oracle", "");
        DbSqlSessionFactory.databaseSpecificOrderByStatements.put("oracle", defaultOrderBy);
        DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.put("oracle", "");
        DbSqlSessionFactory.databaseSpecificDistinct.put("oracle", "distinct");
        DbSqlSessionFactory.databaseSpecificNumericCast.put("oracle", "");
        DbSqlSessionFactory.databaseSpecificCountDistinctBeforeStart.put("oracle", defaultDistinctCountBeforeStart);
        DbSqlSessionFactory.databaseSpecificCountDistinctBeforeEnd.put("oracle", defaultDistinctCountBeforeEnd);
        DbSqlSessionFactory.databaseSpecificCountDistinctAfterEnd.put("oracle", defaultDistinctCountAfterEnd);
        DbSqlSessionFactory.databaseSpecificEscapeChar.put("oracle", defaultEscapeChar);
        DbSqlSessionFactory.databaseSpecificDummyTable.put("oracle", "FROM DUAL");
        DbSqlSessionFactory.databaseSpecificBitAnd1.put("oracle", "BITAND(");
        DbSqlSessionFactory.databaseSpecificBitAnd2.put("oracle", ",");
        DbSqlSessionFactory.databaseSpecificBitAnd3.put("oracle", ")");
        DbSqlSessionFactory.databaseSpecificDatepart1.put("oracle", "to_number(to_char(");
        DbSqlSessionFactory.databaseSpecificDatepart2.put("oracle", ",");
        DbSqlSessionFactory.databaseSpecificDatepart3.put("oracle", "))");
        DbSqlSessionFactory.databaseSpecificTrueConstant.put("oracle", "1");
        DbSqlSessionFactory.databaseSpecificFalseConstant.put("oracle", "0");
        DbSqlSessionFactory.databaseSpecificIfNull.put("oracle", "NVL");
        DbSqlSessionFactory.databaseSpecificDaysComparator.put("oracle", "${date} <= #{currentTimestamp} - ${days}");
        DbSqlSessionFactory.databaseSpecificCollationForCaseSensitivity.put("oracle", "");
        DbSqlSessionFactory.databaseSpecificAuthJoinStart.put("oracle", defaultAuthOnStart);
        DbSqlSessionFactory.databaseSpecificAuthJoinEnd.put("oracle", defaultAuthOnEnd);
        DbSqlSessionFactory.databaseSpecificAuthJoinSeparator.put("oracle", defaultAuthOnSeparator);
        DbSqlSessionFactory.databaseSpecificAuth1JoinStart.put("oracle", defaultAuthOnStart);
        DbSqlSessionFactory.databaseSpecificAuth1JoinEnd.put("oracle", defaultAuthOnEnd);
        DbSqlSessionFactory.databaseSpecificAuth1JoinSeparator.put("oracle", defaultAuthOnSeparator);
        addDatabaseSpecificStatement("oracle", "selectHistoricProcessInstanceDurationReport", "selectHistoricProcessInstanceDurationReport_oracle");
        addDatabaseSpecificStatement("oracle", "selectHistoricTaskInstanceDurationReport", "selectHistoricTaskInstanceDurationReport_oracle");
        addDatabaseSpecificStatement("oracle", "selectHistoricTaskInstanceCountByTaskNameReport", "selectHistoricTaskInstanceCountByTaskNameReport_oracle");
        addDatabaseSpecificStatement("oracle", "selectFilterByQueryCriteria", "selectFilterByQueryCriteria_oracleDb2");
        addDatabaseSpecificStatement("oracle", "selectHistoricProcessInstanceIdsForCleanup", "selectHistoricProcessInstanceIdsForCleanup_oracle");
        addDatabaseSpecificStatement("oracle", "selectHistoricDecisionInstanceIdsForCleanup", "selectHistoricDecisionInstanceIdsForCleanup_oracle");
        addDatabaseSpecificStatement("oracle", "selectHistoricCaseInstanceIdsForCleanup", "selectHistoricCaseInstanceIdsForCleanup_oracle");
        addDatabaseSpecificStatement("oracle", "selectHistoricBatchIdsForCleanup", "selectHistoricBatchIdsForCleanup_oracle");
        addDatabaseSpecificStatement("oracle", "selectTaskMetricIdsForCleanup", "selectTaskMetricIdsForCleanup_oracle");
        addDatabaseSpecificStatement("oracle", "deleteAttachmentsByRemovalTime", "deleteAttachmentsByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteCommentsByRemovalTime", "deleteCommentsByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricActivityInstancesByRemovalTime", "deleteHistoricActivityInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricDecisionInputInstancesByRemovalTime", "deleteHistoricDecisionInputInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricDecisionInstancesByRemovalTime", "deleteHistoricDecisionInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricDecisionOutputInstancesByRemovalTime", "deleteHistoricDecisionOutputInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricDetailsByRemovalTime", "deleteHistoricDetailsByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteExternalTaskLogByRemovalTime", "deleteExternalTaskLogByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricIdentityLinkLogByRemovalTime", "deleteHistoricIdentityLinkLogByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricIncidentsByRemovalTime", "deleteHistoricIncidentsByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteJobLogByRemovalTime", "deleteJobLogByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricProcessInstancesByRemovalTime", "deleteHistoricProcessInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricTaskInstancesByRemovalTime", "deleteHistoricTaskInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricVariableInstancesByRemovalTime", "deleteHistoricVariableInstancesByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteUserOperationLogByRemovalTime", "deleteUserOperationLogByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteByteArraysByRemovalTime", "deleteByteArraysByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteHistoricBatchesByRemovalTime", "deleteHistoricBatchesByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteAuthorizationsByRemovalTime", "deleteAuthorizationsByRemovalTime_oracle");
        addDatabaseSpecificStatement("oracle", "deleteTaskMetricsByRemovalTime", "deleteTaskMetricsByRemovalTime_oracle");
        constants = new HashMap<String, String>();
        constants.put("constant.event", "cast('event' as nvarchar2(255))");
        constants.put("constant.op_message", "NEW_VALUE_ || '_|_' || PROPERTY_");
        constants.put("constant_for_update", "for update");
        constants.put("constant.datepart.quarter", "'Q'");
        constants.put("constant.datepart.month", "'MM'");
        constants.put("constant.datepart.minute", "'MI'");
        constants.put("constant.null.startTime", "null START_TIME_");
        constants.put("constant.varchar.cast", "'${key}'");
        constants.put("constant.integer.cast", "NULL");
        constants.put("constant.null.reporter", "NULL AS REPORTER_");
        DbSqlSessionFactory.dbSpecificConstants.put("oracle", constants);
        DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.put("db2", "SELECT SUB.* FROM (");
        DbSqlSessionFactory.optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements.put("db2", "");
        DbSqlSessionFactory.databaseSpecificInnerLimitAfterStatements.put("db2", ")RES ) SUB WHERE SUB.rnk >= #{firstRow} AND SUB.rnk < #{lastRow}");
        DbSqlSessionFactory.databaseSpecificLimitAfterStatements.put("db2", DbSqlSessionFactory.databaseSpecificInnerLimitAfterStatements.get("db2") + " ORDER BY SUB.rnk");
        DbSqlSessionFactory.optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements.put("db2", "FETCH FIRST ${maxResults} ROWS ONLY");
        final String db2LimitBetweenWithoutColumns = ", row_number() over (ORDER BY ${internalOrderBy}) rnk FROM ( select distinct ";
        DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.put("db2", db2LimitBetweenWithoutColumns + "RES.* ");
        DbSqlSessionFactory.databaseSpecificLimitBetweenFilterStatements.put("db2", db2LimitBetweenWithoutColumns + "RES.ID_, RES.REV_, RES.RESOURCE_TYPE_, RES.NAME_, RES.OWNER_ ");
        DbSqlSessionFactory.databaseSpecificLimitBetweenAcquisitionStatements.put("db2", db2LimitBetweenWithoutColumns + "RES.ID_, RES.REV_, RES.TYPE_, RES.LOCK_EXP_TIME_, RES.LOCK_OWNER_, RES.EXCLUSIVE_, RES.PROCESS_INSTANCE_ID_, RES.DUEDATE_, RES.PRIORITY_ ");
        DbSqlSessionFactory.databaseSpecificLimitBeforeWithoutOffsetStatements.put("db2", "");
        DbSqlSessionFactory.databaseSpecificLimitAfterWithoutOffsetStatements.put("db2", "FETCH FIRST ${maxResults} ROWS ONLY");
        DbSqlSessionFactory.databaseSpecificOrderByStatements.put("db2", defaultOrderBy);
        DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.put("db2", "SELECT SUB.* FROM ( select RES.* , row_number() over (ORDER BY ${internalOrderBy}) rnk FROM (");
        DbSqlSessionFactory.databaseSpecificDistinct.put("db2", "");
        DbSqlSessionFactory.databaseSpecificNumericCast.put("db2", "");
        DbSqlSessionFactory.databaseSpecificCountDistinctBeforeStart.put("db2", defaultDistinctCountBeforeStart);
        DbSqlSessionFactory.databaseSpecificCountDistinctBeforeEnd.put("db2", defaultDistinctCountBeforeEnd);
        DbSqlSessionFactory.databaseSpecificCountDistinctAfterEnd.put("db2", defaultDistinctCountAfterEnd);
        DbSqlSessionFactory.databaseSpecificEscapeChar.put("db2", defaultEscapeChar);
        DbSqlSessionFactory.databaseSpecificBitAnd1.put("db2", "BITAND(");
        DbSqlSessionFactory.databaseSpecificBitAnd2.put("db2", ", CAST(");
        DbSqlSessionFactory.databaseSpecificBitAnd3.put("db2", " AS Integer))");
        DbSqlSessionFactory.databaseSpecificDatepart1.put("db2", "");
        DbSqlSessionFactory.databaseSpecificDatepart2.put("db2", "(");
        DbSqlSessionFactory.databaseSpecificDatepart3.put("db2", ")");
        DbSqlSessionFactory.databaseSpecificDummyTable.put("db2", "FROM SYSIBM.SYSDUMMY1");
        DbSqlSessionFactory.databaseSpecificTrueConstant.put("db2", "1");
        DbSqlSessionFactory.databaseSpecificFalseConstant.put("db2", "0");
        DbSqlSessionFactory.databaseSpecificIfNull.put("db2", "NVL");
        DbSqlSessionFactory.databaseSpecificDaysComparator.put("db2", "${date} + ${days} DAYS <= #{currentTimestamp}");
        DbSqlSessionFactory.databaseSpecificCollationForCaseSensitivity.put("db2", "");
        DbSqlSessionFactory.databaseSpecificAuthJoinStart.put("db2", defaultAuthOnStart);
        DbSqlSessionFactory.databaseSpecificAuthJoinEnd.put("db2", defaultAuthOnEnd);
        DbSqlSessionFactory.databaseSpecificAuthJoinSeparator.put("db2", defaultAuthOnSeparator);
        DbSqlSessionFactory.databaseSpecificAuth1JoinStart.put("db2", defaultAuthOnStart);
        DbSqlSessionFactory.databaseSpecificAuth1JoinEnd.put("db2", defaultAuthOnEnd);
        DbSqlSessionFactory.databaseSpecificAuth1JoinSeparator.put("db2", defaultAuthOnSeparator);
        addDatabaseSpecificStatement("db2", "selectMeterLogAggregatedByTimeInterval", "selectMeterLogAggregatedByTimeInterval_db2_or_mssql");
        addDatabaseSpecificStatement("db2", "selectExecutionByNativeQuery", "selectExecutionByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("db2", "selectHistoricActivityInstanceByNativeQuery", "selectHistoricActivityInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("db2", "selectHistoricCaseActivityInstanceByNativeQuery", "selectHistoricCaseActivityInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("db2", "selectHistoricProcessInstanceByNativeQuery", "selectHistoricProcessInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("db2", "selectHistoricCaseInstanceByNativeQuery", "selectHistoricCaseInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("db2", "selectHistoricTaskInstanceByNativeQuery", "selectHistoricTaskInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("db2", "selectHistoricVariableInstanceByNativeQuery", "selectHistoricVariableInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("db2", "selectTaskByNativeQuery", "selectTaskByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("db2", "selectUserByNativeQuery", "selectUserByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("db2", "selectHistoricDecisionInstancesByNativeQuery", "selectHistoricDecisionInstancesByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("db2", "selectFilterByQueryCriteria", "selectFilterByQueryCriteria_oracleDb2");
        addDatabaseSpecificStatement("db2", "deleteAttachmentsByRemovalTime", "deleteAttachmentsByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteCommentsByRemovalTime", "deleteCommentsByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricActivityInstancesByRemovalTime", "deleteHistoricActivityInstancesByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricDecisionInputInstancesByRemovalTime", "deleteHistoricDecisionInputInstancesByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricDecisionInstancesByRemovalTime", "deleteHistoricDecisionInstancesByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricDecisionOutputInstancesByRemovalTime", "deleteHistoricDecisionOutputInstancesByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricDetailsByRemovalTime", "deleteHistoricDetailsByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteExternalTaskLogByRemovalTime", "deleteExternalTaskLogByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricIdentityLinkLogByRemovalTime", "deleteHistoricIdentityLinkLogByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricIncidentsByRemovalTime", "deleteHistoricIncidentsByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteJobLogByRemovalTime", "deleteJobLogByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricProcessInstancesByRemovalTime", "deleteHistoricProcessInstancesByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricTaskInstancesByRemovalTime", "deleteHistoricTaskInstancesByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricVariableInstancesByRemovalTime", "deleteHistoricVariableInstancesByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteUserOperationLogByRemovalTime", "deleteUserOperationLogByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteByteArraysByRemovalTime", "deleteByteArraysByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteHistoricBatchesByRemovalTime", "deleteHistoricBatchesByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteAuthorizationsByRemovalTime", "deleteAuthorizationsByRemovalTime_postgres_or_db2");
        addDatabaseSpecificStatement("db2", "deleteTaskMetricsByRemovalTime", "deleteTaskMetricsByRemovalTime_postgres_or_db2");
        constants = new HashMap<String, String>();
        constants.put("constant.event", "'event'");
        constants.put("constant.op_message", "CAST(CONCAT(CONCAT(COALESCE(NEW_VALUE_,''), '_|_'), COALESCE(PROPERTY_,'')) as varchar(255))");
        constants.put("constant_for_update", "for read only with rs use and keep update locks");
        constants.put("constant.datepart.quarter", "QUARTER");
        constants.put("constant.datepart.month", "MONTH");
        constants.put("constant.datepart.minute", "MINUTE");
        constants.put("constant.null.startTime", "CAST(NULL as timestamp) as START_TIME_");
        constants.put("constant.varchar.cast", "cast('${key}' as varchar(64))");
        constants.put("constant.integer.cast", "cast(NULL as integer)");
        constants.put("constant.null.reporter", "CAST(NULL AS VARCHAR(255)) AS REPORTER_");
        DbSqlSessionFactory.dbSpecificConstants.put("db2", constants);
        DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.put("mssql", "SELECT SUB.* FROM (");
        DbSqlSessionFactory.optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements.put("mssql", "");
        DbSqlSessionFactory.databaseSpecificInnerLimitAfterStatements.put("mssql", ")RES ) SUB WHERE SUB.rnk >= #{firstRow} AND SUB.rnk < #{lastRow}");
        DbSqlSessionFactory.databaseSpecificLimitAfterStatements.put("mssql", DbSqlSessionFactory.databaseSpecificInnerLimitAfterStatements.get("mssql") + " ORDER BY SUB.rnk");
        DbSqlSessionFactory.optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements.put("mssql", "");
        final String mssqlLimitBetweenWithoutColumns = ", row_number() over (ORDER BY ${internalOrderBy}) rnk FROM ( select distinct ";
        DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.put("mssql", mssqlLimitBetweenWithoutColumns + "RES.* ");
        DbSqlSessionFactory.databaseSpecificLimitBetweenFilterStatements.put("mssql", "");
        DbSqlSessionFactory.databaseSpecificLimitBetweenAcquisitionStatements.put("mssql", mssqlLimitBetweenWithoutColumns + "RES.ID_, RES.REV_, RES.TYPE_, RES.LOCK_EXP_TIME_, RES.LOCK_OWNER_, RES.EXCLUSIVE_, RES.PROCESS_INSTANCE_ID_, RES.DUEDATE_, RES.PRIORITY_ ");
        DbSqlSessionFactory.databaseSpecificLimitBeforeWithoutOffsetStatements.put("mssql", "TOP (#{maxResults})");
        DbSqlSessionFactory.databaseSpecificLimitAfterWithoutOffsetStatements.put("mssql", "");
        DbSqlSessionFactory.databaseSpecificOrderByStatements.put("mssql", "");
        DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.put("mssql", "SELECT SUB.* FROM ( select RES.* , row_number() over (ORDER BY ${internalOrderBy}) rnk FROM (");
        DbSqlSessionFactory.databaseSpecificDistinct.put("mssql", "");
        DbSqlSessionFactory.databaseSpecificNumericCast.put("mssql", "");
        DbSqlSessionFactory.databaseSpecificCountDistinctBeforeStart.put("mssql", defaultDistinctCountBeforeStart);
        DbSqlSessionFactory.databaseSpecificCountDistinctBeforeEnd.put("mssql", defaultDistinctCountBeforeEnd);
        DbSqlSessionFactory.databaseSpecificCountDistinctAfterEnd.put("mssql", defaultDistinctCountAfterEnd);
        DbSqlSessionFactory.databaseSpecificEscapeChar.put("mssql", defaultEscapeChar);
        DbSqlSessionFactory.databaseSpecificBitAnd1.put("mssql", "");
        DbSqlSessionFactory.databaseSpecificBitAnd2.put("mssql", " &");
        DbSqlSessionFactory.databaseSpecificBitAnd3.put("mssql", "");
        DbSqlSessionFactory.databaseSpecificDatepart1.put("mssql", "datepart(");
        DbSqlSessionFactory.databaseSpecificDatepart2.put("mssql", ",");
        DbSqlSessionFactory.databaseSpecificDatepart3.put("mssql", ")");
        DbSqlSessionFactory.databaseSpecificDummyTable.put("mssql", "");
        DbSqlSessionFactory.databaseSpecificTrueConstant.put("mssql", "1");
        DbSqlSessionFactory.databaseSpecificFalseConstant.put("mssql", "0");
        DbSqlSessionFactory.databaseSpecificIfNull.put("mssql", "ISNULL");
        DbSqlSessionFactory.databaseSpecificDaysComparator.put("mssql", "DATEDIFF(DAY, ${date}, #{currentTimestamp}) >= ${days}");
        DbSqlSessionFactory.databaseSpecificCollationForCaseSensitivity.put("mssql", "COLLATE Latin1_General_CS_AS");
        DbSqlSessionFactory.databaseSpecificAuthJoinStart.put("mssql", defaultAuthOnStart);
        DbSqlSessionFactory.databaseSpecificAuthJoinEnd.put("mssql", defaultAuthOnEnd);
        DbSqlSessionFactory.databaseSpecificAuthJoinSeparator.put("mssql", defaultAuthOnSeparator);
        DbSqlSessionFactory.databaseSpecificAuth1JoinStart.put("mssql", defaultAuthOnStart);
        DbSqlSessionFactory.databaseSpecificAuth1JoinEnd.put("mssql", defaultAuthOnEnd);
        DbSqlSessionFactory.databaseSpecificAuth1JoinSeparator.put("mssql", defaultAuthOnSeparator);
        addDatabaseSpecificStatement("mssql", "selectMeterLogAggregatedByTimeInterval", "selectMeterLogAggregatedByTimeInterval_db2_or_mssql");
        addDatabaseSpecificStatement("mssql", "selectExecutionByNativeQuery", "selectExecutionByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("mssql", "selectHistoricActivityInstanceByNativeQuery", "selectHistoricActivityInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("mssql", "selectHistoricCaseActivityInstanceByNativeQuery", "selectHistoricCaseActivityInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("mssql", "selectHistoricProcessInstanceByNativeQuery", "selectHistoricProcessInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("mssql", "selectHistoricCaseInstanceByNativeQuery", "selectHistoricCaseInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("mssql", "selectHistoricTaskInstanceByNativeQuery", "selectHistoricTaskInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("mssql", "selectHistoricVariableInstanceByNativeQuery", "selectHistoricVariableInstanceByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("mssql", "selectTaskByNativeQuery", "selectTaskByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("mssql", "selectUserByNativeQuery", "selectUserByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("mssql", "lockDeploymentLockProperty", "lockDeploymentLockProperty_mssql");
        addDatabaseSpecificStatement("mssql", "lockHistoryCleanupJobLockProperty", "lockHistoryCleanupJobLockProperty_mssql");
        addDatabaseSpecificStatement("mssql", "lockStartupLockProperty", "lockStartupLockProperty_mssql");
        addDatabaseSpecificStatement("mssql", "lockTelemetryLockProperty", "lockTelemetryLockProperty_mssql");
        addDatabaseSpecificStatement("mssql", "lockInstallationIdLockProperty", "lockInstallationIdLockProperty_mssql");
        addDatabaseSpecificStatement("mssql", "selectEventSubscriptionsByNameAndExecution", "selectEventSubscriptionsByNameAndExecution_mssql");
        addDatabaseSpecificStatement("mssql", "selectEventSubscriptionsByExecutionAndType", "selectEventSubscriptionsByExecutionAndType_mssql");
        addDatabaseSpecificStatement("mssql", "selectHistoricDecisionInstancesByNativeQuery", "selectHistoricDecisionInstancesByNativeQuery_mssql_or_db2");
        addDatabaseSpecificStatement("mssql", "deleteByteArraysByRemovalTime", "deleteByteArraysByRemovalTime_mssql");
        addDatabaseSpecificStatement("mssql", "updateAttachmentsByRootProcessInstanceId", "updateAttachmentsByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateAttachmentsByProcessInstanceId", "updateAttachmentsByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateAuthorizationsByRootProcessInstanceId", "updateAuthorizationsByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateAuthorizationsByProcessInstanceId", "updateAuthorizationsByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateCommentsByRootProcessInstanceId", "updateCommentsByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateCommentsByProcessInstanceId", "updateCommentsByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricActivityInstancesByRootProcessInstanceId", "updateHistoricActivityInstancesByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricActivityInstancesByProcessInstanceId", "updateHistoricActivityInstancesByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricBatch", "updateHistoricBatch_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricBatchRemovalTimeById", "updateHistoricBatchRemovalTimeById_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionInputInstancesByRootProcessInstanceId", "updateHistoricDecisionInputInstancesByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionInputInstancesByProcessInstanceId", "updateHistoricDecisionInputInstancesByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionInputInstancesByRootDecisionInstanceId", "updateHistoricDecisionInputInstancesByRootDecisionInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionInputInstancesByDecisionInstanceId", "updateHistoricDecisionInputInstancesByDecisionInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionInstancesByRootProcessInstanceId", "updateHistoricDecisionInstancesByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionInstancesByProcessInstanceId", "updateHistoricDecisionInstancesByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionInstancesByRootDecisionInstanceId", "updateHistoricDecisionInstancesByRootDecisionInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionInstancesByDecisionInstanceId", "updateHistoricDecisionInstancesByDecisionInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionOutputInstancesByRootProcessInstanceId", "updateHistoricDecisionOutputInstancesByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionOutputInstancesByProcessInstanceId", "updateHistoricDecisionOutputInstancesByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionOutputInstancesByRootDecisionInstanceId", "updateHistoricDecisionOutputInstancesByRootDecisionInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDecisionOutputInstancesByDecisionInstanceId", "updateHistoricDecisionOutputInstancesByDecisionInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDetailsByRootProcessInstanceId", "updateHistoricDetailsByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricDetailsByProcessInstanceId", "updateHistoricDetailsByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateExternalTaskLogByRootProcessInstanceId", "updateExternalTaskLogByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateExternalTaskLogByProcessInstanceId", "updateExternalTaskLogByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateIdentityLinkLogByRootProcessInstanceId", "updateIdentityLinkLogByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateIdentityLinkLogByProcessInstanceId", "updateIdentityLinkLogByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricIncidentsByRootProcessInstanceId", "updateHistoricIncidentsByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricIncidentsByProcessInstanceId", "updateHistoricIncidentsByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricIncidentsByBatchId", "updateHistoricIncidentsByBatchId_mssql");
        addDatabaseSpecificStatement("mssql", "updateJobLogByRootProcessInstanceId", "updateJobLogByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateJobLogByProcessInstanceId", "updateJobLogByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateJobLogByBatchId", "updateJobLogByBatchId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricProcessInstanceEventsByRootProcessInstanceId", "updateHistoricProcessInstanceEventsByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricProcessInstanceByProcessInstanceId", "updateHistoricProcessInstanceByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricTaskInstancesByRootProcessInstanceId", "updateHistoricTaskInstancesByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricTaskInstancesByProcessInstanceId", "updateHistoricTaskInstancesByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricVariableInstancesByRootProcessInstanceId", "updateHistoricVariableInstancesByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateHistoricVariableInstancesByProcessInstanceId", "updateHistoricVariableInstancesByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateUserOperationLogByRootProcessInstanceId", "updateUserOperationLogByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateUserOperationLogByProcessInstanceId", "updateUserOperationLogByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateByteArraysByRootProcessInstanceId", "updateByteArraysByRootProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateVariableByteArraysByProcessInstanceId", "updateVariableByteArraysByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateDecisionInputsByteArraysByProcessInstanceId", "updateDecisionInputsByteArraysByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateDecisionOutputsByteArraysByProcessInstanceId", "updateDecisionOutputsByteArraysByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateJobLogByteArraysByProcessInstanceId", "updateJobLogByteArraysByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateExternalTaskLogByteArraysByProcessInstanceId", "updateExternalTaskLogByteArraysByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateAttachmentByteArraysByProcessInstanceId", "updateAttachmentByteArraysByProcessInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateDecisionInputByteArraysByRootDecisionInstanceId", "updateDecisionInputByteArraysByRootDecisionInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateDecisionOutputByteArraysByRootDecisionInstanceId", "updateDecisionOutputByteArraysByRootDecisionInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateDecisionInputByteArraysByDecisionInstanceId", "updateDecisionInputByteArraysByDecisionInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateDecisionOutputByteArraysByDecisionInstanceId", "updateDecisionOutputByteArraysByDecisionInstanceId_mssql");
        addDatabaseSpecificStatement("mssql", "updateByteArraysByBatchId", "updateByteArraysByBatchId_mssql");
        constants = new HashMap<String, String>();
        constants.put("constant.event", "'event'");
        constants.put("constant.op_message", "NEW_VALUE_ + '_|_' + PROPERTY_");
        constants.put("constant.datepart.quarter", "QUARTER");
        constants.put("constant.datepart.month", "MONTH");
        constants.put("constant.datepart.minute", "MINUTE");
        constants.put("constant.null.startTime", "CAST(NULL AS datetime2) AS START_TIME_");
        constants.put("constant.varchar.cast", "'${key}'");
        constants.put("constant.integer.cast", "NULL");
        constants.put("constant.null.reporter", "NULL AS REPORTER_");
        DbSqlSessionFactory.dbSpecificConstants.put("mssql", constants);
    }
}
