// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.HistoricDecisionInstance;
import org.zik.bpm.engine.history.HistoricCaseActivityInstance;
import org.zik.bpm.engine.history.HistoricCaseInstance;
import org.zik.bpm.engine.history.HistoricVariableInstance;
import org.zik.bpm.engine.history.HistoricTaskInstance;
import org.zik.bpm.engine.history.HistoricFormProperty;
import org.zik.bpm.engine.history.HistoricVariableUpdate;
import org.zik.bpm.engine.history.HistoricDetail;
import org.zik.bpm.engine.history.HistoricActivityInstance;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import org.zik.bpm.engine.filter.Filter;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.repository.Deployment;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.runtime.Execution;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.impl.history.event.UserOperationLogEntryEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInstanceEntity;
import org.zik.bpm.engine.impl.history.event.HistoricDetailEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoricExternalTaskLogEntity;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import org.zik.bpm.engine.impl.history.event.HistoricIncidentEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionOutputInstanceEntity;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInputInstanceEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionEntity;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseSentryPartEntity;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionEntity;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.zik.bpm.engine.impl.util.DatabaseUtil;
import org.zik.bpm.engine.management.TableMetaData;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.management.TablePage;
import org.zik.bpm.engine.impl.TablePageQueryImpl;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Map;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class TableDataManager extends AbstractManager
{
    protected static final EnginePersistenceLogger LOG;
    public static Map<Class<?>, String> apiTypeToTableNameMap;
    public static Map<Class<? extends DbEntity>, String> persistentObjectToTableNameMap;
    
    public Map<String, Long> getTableCount() {
        final Map<String, Long> tableCount = new HashMap<String, Long>();
        try {
            for (final String tableName : this.getDbEntityManager().getTableNamesPresentInDatabase()) {
                tableCount.put(tableName, this.getTableCount(tableName));
            }
            TableDataManager.LOG.countRowsPerProcessEngineTable(tableCount);
        }
        catch (Exception e) {
            throw TableDataManager.LOG.countTableRowsException(e);
        }
        return tableCount;
    }
    
    protected long getTableCount(final String tableName) {
        TableDataManager.LOG.selectTableCountForTable(tableName);
        final Long count = (Long)this.getDbEntityManager().selectOne("selectTableCount", Collections.singletonMap("tableName", tableName));
        return count;
    }
    
    public TablePage getTablePage(final TablePageQueryImpl tablePageQuery) {
        final TablePage tablePage = new TablePage();
        final List tableData = this.getDbEntityManager().selectList("selectTableData", tablePageQuery);
        tablePage.setTableName(tablePageQuery.getTableName());
        tablePage.setTotal(this.getTableCount(tablePageQuery.getTableName()));
        tablePage.setRows(tableData);
        tablePage.setFirstResult(tablePageQuery.getFirstResult());
        return tablePage;
    }
    
    public List<Class<? extends DbEntity>> getEntities(final String tableName) {
        final String databaseTablePrefix = this.getDbSqlSession().getDbSqlSessionFactory().getDatabaseTablePrefix();
        final List<Class<? extends DbEntity>> entities = new ArrayList<Class<? extends DbEntity>>();
        final Set<Class<? extends DbEntity>> entityClasses = TableDataManager.persistentObjectToTableNameMap.keySet();
        for (final Class<? extends DbEntity> entityClass : entityClasses) {
            final String entityTableName = TableDataManager.persistentObjectToTableNameMap.get(entityClass);
            if ((databaseTablePrefix + entityTableName).equals(tableName)) {
                entities.add(entityClass);
            }
        }
        return entities;
    }
    
    public String getTableName(final Class<?> entityClass, final boolean withPrefix) {
        final String databaseTablePrefix = this.getDbSqlSession().getDbSqlSessionFactory().getDatabaseTablePrefix();
        String tableName = null;
        if (DbEntity.class.isAssignableFrom(entityClass)) {
            tableName = TableDataManager.persistentObjectToTableNameMap.get(entityClass);
        }
        else {
            tableName = TableDataManager.apiTypeToTableNameMap.get(entityClass);
        }
        if (withPrefix) {
            return databaseTablePrefix + tableName;
        }
        return tableName;
    }
    
    public TableMetaData getTableMetaData(String tableName) {
        TableMetaData result = new TableMetaData();
        ResultSet resultSet = null;
        try {
            try {
                result.setTableName(tableName);
                final DatabaseMetaData metaData = this.getDbSqlSession().getSqlSession().getConnection().getMetaData();
                if (DatabaseUtil.checkDatabaseType("postgres", "cockroachdb")) {
                    tableName = tableName.toLowerCase();
                }
                final String databaseSchema = this.getDbSqlSession().getDbSqlSessionFactory().getDatabaseSchema();
                tableName = this.getDbSqlSession().prependDatabaseTablePrefix(tableName);
                resultSet = metaData.getColumns(null, databaseSchema, tableName, null);
                while (resultSet.next()) {
                    final String name = resultSet.getString("COLUMN_NAME").toUpperCase();
                    final String type = resultSet.getString("TYPE_NAME").toUpperCase();
                    result.addColumnMetaData(name, type);
                }
            }
            catch (SQLException se) {
                throw se;
            }
            finally {
                if (resultSet != null) {
                    resultSet.close();
                }
            }
        }
        catch (Exception e) {
            throw TableDataManager.LOG.retrieveMetadataException(e);
        }
        if (result.getColumnNames().size() == 0) {
            result = null;
        }
        return result;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
        TableDataManager.apiTypeToTableNameMap = new HashMap<Class<?>, String>();
        (TableDataManager.persistentObjectToTableNameMap = new HashMap<Class<? extends DbEntity>, String>()).put(TaskEntity.class, "ACT_RU_TASK");
        TableDataManager.persistentObjectToTableNameMap.put(ExternalTaskEntity.class, "ACT_RU_EXT_TASK");
        TableDataManager.persistentObjectToTableNameMap.put(ExecutionEntity.class, "ACT_RU_EXECUTION");
        TableDataManager.persistentObjectToTableNameMap.put(IdentityLinkEntity.class, "ACT_RU_IDENTITYLINK");
        TableDataManager.persistentObjectToTableNameMap.put(VariableInstanceEntity.class, "ACT_RU_VARIABLE");
        TableDataManager.persistentObjectToTableNameMap.put(JobEntity.class, "ACT_RU_JOB");
        TableDataManager.persistentObjectToTableNameMap.put(MessageEntity.class, "ACT_RU_JOB");
        TableDataManager.persistentObjectToTableNameMap.put(TimerEntity.class, "ACT_RU_JOB");
        TableDataManager.persistentObjectToTableNameMap.put(JobDefinitionEntity.class, "ACT_RU_JOBDEF");
        TableDataManager.persistentObjectToTableNameMap.put(BatchEntity.class, "ACT_RU_BATCH");
        TableDataManager.persistentObjectToTableNameMap.put(IncidentEntity.class, "ACT_RU_INCIDENT");
        TableDataManager.persistentObjectToTableNameMap.put(EventSubscriptionEntity.class, "ACT_RU_EVENT_SUBSCR");
        TableDataManager.persistentObjectToTableNameMap.put(MeterLogEntity.class, "ACT_RU_METER_LOG");
        TableDataManager.persistentObjectToTableNameMap.put(TaskMeterLogEntity.class, "ACT_RU_TASK_METER_LOG");
        TableDataManager.persistentObjectToTableNameMap.put(CamundaFormDefinitionEntity.class, "ACT_RE_CAMFORMDEF");
        TableDataManager.persistentObjectToTableNameMap.put(DeploymentEntity.class, "ACT_RE_DEPLOYMENT");
        TableDataManager.persistentObjectToTableNameMap.put(ProcessDefinitionEntity.class, "ACT_RE_PROCDEF");
        TableDataManager.persistentObjectToTableNameMap.put(CaseDefinitionEntity.class, "ACT_RE_CASE_DEF");
        TableDataManager.persistentObjectToTableNameMap.put(CaseExecutionEntity.class, "ACT_RU_CASE_EXECUTION");
        TableDataManager.persistentObjectToTableNameMap.put(CaseSentryPartEntity.class, "ACT_RU_CASE_SENTRY_PART");
        TableDataManager.persistentObjectToTableNameMap.put(DecisionRequirementsDefinitionEntity.class, "ACT_RE_DECISION_REQ_DEF");
        TableDataManager.persistentObjectToTableNameMap.put(DecisionDefinitionEntity.class, "ACT_RE_DECISION_DEF");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricDecisionInputInstanceEntity.class, "ACT_HI_DEC_IN");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricDecisionOutputInstanceEntity.class, "ACT_HI_DEC_OUT");
        TableDataManager.persistentObjectToTableNameMap.put(CommentEntity.class, "ACT_HI_COMMENT");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricActivityInstanceEntity.class, "ACT_HI_ACTINST");
        TableDataManager.persistentObjectToTableNameMap.put(AttachmentEntity.class, "ACT_HI_ATTACHMENT");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricProcessInstanceEntity.class, "ACT_HI_PROCINST");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricTaskInstanceEntity.class, "ACT_HI_TASKINST");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricJobLogEventEntity.class, "ACT_HI_JOB_LOG");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricIncidentEventEntity.class, "ACT_HI_INCIDENT");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricBatchEntity.class, "ACT_HI_BATCH");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricExternalTaskLogEntity.class, "ACT_HI_EXT_TASK_LOG");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricCaseInstanceEntity.class, "ACT_HI_CASEINST");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricCaseActivityInstanceEntity.class, "ACT_HI_CASEACTINST");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricIdentityLinkLogEntity.class, "ACT_HI_IDENTITYLINK");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricFormPropertyEntity.class, "ACT_HI_DETAIL");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricVariableInstanceEntity.class, "ACT_HI_VARINST");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricDetailEventEntity.class, "ACT_HI_DETAIL");
        TableDataManager.persistentObjectToTableNameMap.put(HistoricDecisionInstanceEntity.class, "ACT_HI_DECINST");
        TableDataManager.persistentObjectToTableNameMap.put(UserOperationLogEntryEventEntity.class, "ACT_HI_OP_LOG");
        TableDataManager.persistentObjectToTableNameMap.put(GroupEntity.class, "ACT_ID_GROUP");
        TableDataManager.persistentObjectToTableNameMap.put(MembershipEntity.class, "ACT_ID_MEMBERSHIP");
        TableDataManager.persistentObjectToTableNameMap.put(TenantEntity.class, "ACT_ID_TENANT");
        TableDataManager.persistentObjectToTableNameMap.put(TenantMembershipEntity.class, "ACT_ID_TENANT_MEMBER");
        TableDataManager.persistentObjectToTableNameMap.put(UserEntity.class, "ACT_ID_USER");
        TableDataManager.persistentObjectToTableNameMap.put(IdentityInfoEntity.class, "ACT_ID_INFO");
        TableDataManager.persistentObjectToTableNameMap.put(AuthorizationEntity.class, "ACT_RU_AUTHORIZATION");
        TableDataManager.persistentObjectToTableNameMap.put(PropertyEntity.class, "ACT_GE_PROPERTY");
        TableDataManager.persistentObjectToTableNameMap.put(ByteArrayEntity.class, "ACT_GE_BYTEARRAY");
        TableDataManager.persistentObjectToTableNameMap.put(ResourceEntity.class, "ACT_GE_BYTEARRAY");
        TableDataManager.persistentObjectToTableNameMap.put(SchemaLogEntryEntity.class, "ACT_GE_SCHEMA_LOG");
        TableDataManager.persistentObjectToTableNameMap.put(FilterEntity.class, "ACT_RU_FILTER");
        TableDataManager.apiTypeToTableNameMap.put(Task.class, "ACT_RU_TASK");
        TableDataManager.apiTypeToTableNameMap.put(Execution.class, "ACT_RU_EXECUTION");
        TableDataManager.apiTypeToTableNameMap.put(ProcessInstance.class, "ACT_RU_EXECUTION");
        TableDataManager.apiTypeToTableNameMap.put(ProcessDefinition.class, "ACT_RE_PROCDEF");
        TableDataManager.apiTypeToTableNameMap.put(Deployment.class, "ACT_RE_DEPLOYMENT");
        TableDataManager.apiTypeToTableNameMap.put(Job.class, "ACT_RU_JOB");
        TableDataManager.apiTypeToTableNameMap.put(Incident.class, "ACT_RU_INCIDENT");
        TableDataManager.apiTypeToTableNameMap.put(Filter.class, "ACT_RU_FILTER");
        TableDataManager.apiTypeToTableNameMap.put(HistoricProcessInstance.class, "ACT_HI_PROCINST");
        TableDataManager.apiTypeToTableNameMap.put(HistoricActivityInstance.class, "ACT_HI_ACTINST");
        TableDataManager.apiTypeToTableNameMap.put(HistoricDetail.class, "ACT_HI_DETAIL");
        TableDataManager.apiTypeToTableNameMap.put(HistoricVariableUpdate.class, "ACT_HI_DETAIL");
        TableDataManager.apiTypeToTableNameMap.put(HistoricFormProperty.class, "ACT_HI_DETAIL");
        TableDataManager.apiTypeToTableNameMap.put(HistoricTaskInstance.class, "ACT_HI_TASKINST");
        TableDataManager.apiTypeToTableNameMap.put(HistoricVariableInstance.class, "ACT_HI_VARINST");
        TableDataManager.apiTypeToTableNameMap.put(HistoricCaseInstance.class, "ACT_HI_CASEINST");
        TableDataManager.apiTypeToTableNameMap.put(HistoricCaseActivityInstance.class, "ACT_HI_CASEACTINST");
        TableDataManager.apiTypeToTableNameMap.put(HistoricDecisionInstance.class, "ACT_HI_DECINST");
    }
}
