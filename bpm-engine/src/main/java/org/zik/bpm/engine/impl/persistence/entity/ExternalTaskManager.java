// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.Direction;
import org.zik.bpm.engine.impl.ExternalTaskQueryProperty;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import org.zik.bpm.engine.externaltask.ExternalTask;
import org.zik.bpm.engine.impl.ExternalTaskQueryImpl;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import java.util.Map;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.util.DatabaseUtil;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.HashMap;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.externaltask.TopicFetchInstruction;
import java.util.Collection;
import java.util.List;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class ExternalTaskManager extends AbstractManager
{
    public static QueryOrderingProperty EXT_TASK_PRIORITY_ORDERING_PROPERTY;
    
    public ExternalTaskEntity findExternalTaskById(final String id) {
        return this.getDbEntityManager().selectById(ExternalTaskEntity.class, id);
    }
    
    public void insert(final ExternalTaskEntity externalTask) {
        this.getDbEntityManager().insert(externalTask);
        this.fireExternalTaskAvailableEvent();
    }
    
    public void delete(final ExternalTaskEntity externalTask) {
        this.getDbEntityManager().delete(externalTask);
    }
    
    public List<ExternalTaskEntity> findExternalTasksByExecutionId(final String id) {
        return (List<ExternalTaskEntity>)this.getDbEntityManager().selectList("selectExternalTasksByExecutionId", id);
    }
    
    public List<ExternalTaskEntity> findExternalTasksByProcessInstanceId(final String processInstanceId) {
        return (List<ExternalTaskEntity>)this.getDbEntityManager().selectList("selectExternalTasksByProcessInstanceId", processInstanceId);
    }
    
    public List<ExternalTaskEntity> selectExternalTasksForTopics(final Collection<TopicFetchInstruction> queryFilters, final int maxResults, final boolean usePriority) {
        if (queryFilters.isEmpty()) {
            return new ArrayList<ExternalTaskEntity>();
        }
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("topics", queryFilters);
        parameters.put("now", ClockUtil.getCurrentTime());
        parameters.put("applyOrdering", usePriority);
        final List<QueryOrderingProperty> orderingProperties = new ArrayList<QueryOrderingProperty>();
        orderingProperties.add(ExternalTaskManager.EXT_TASK_PRIORITY_ORDERING_PROPERTY);
        parameters.put("orderingProperties", orderingProperties);
        parameters.put("usesPostgres", DatabaseUtil.checkDatabaseType("postgres", "cockroachdb"));
        final ListQueryParameterObject parameter = new ListQueryParameterObject(parameters, 0, maxResults);
        this.configureQuery(parameter);
        final DbEntityManager manager = this.getDbEntityManager();
        return (List<ExternalTaskEntity>)manager.selectList("selectExternalTasksForTopics", parameter);
    }
    
    public List<ExternalTask> findExternalTasksByQueryCriteria(final ExternalTaskQueryImpl externalTaskQuery) {
        this.configureQuery(externalTaskQuery);
        return (List<ExternalTask>)this.getDbEntityManager().selectList("selectExternalTaskByQueryCriteria", externalTaskQuery);
    }
    
    public List<String> findExternalTaskIdsByQueryCriteria(final ExternalTaskQueryImpl externalTaskQuery) {
        this.configureQuery(externalTaskQuery);
        return (List<String>)this.getDbEntityManager().selectList("selectExternalTaskIdsByQueryCriteria", externalTaskQuery);
    }
    
    public List<ImmutablePair<String, String>> findDeploymentIdMappingsByQueryCriteria(final ExternalTaskQueryImpl externalTaskQuery) {
        this.configureQuery(externalTaskQuery);
        return (List<ImmutablePair<String, String>>)this.getDbEntityManager().selectList("selectExternalTaskDeploymentIdMappingsByQueryCriteria", externalTaskQuery);
    }
    
    public long findExternalTaskCountByQueryCriteria(final ExternalTaskQueryImpl externalTaskQuery) {
        this.configureQuery(externalTaskQuery);
        return (long)this.getDbEntityManager().selectOne("selectExternalTaskCountByQueryCriteria", externalTaskQuery);
    }
    
    public List<String> selectTopicNamesByQuery(final ExternalTaskQueryImpl externalTaskQuery) {
        this.configureQuery(externalTaskQuery);
        return (List<String>)this.getDbEntityManager().selectList("selectTopicNamesByQuery", externalTaskQuery);
    }
    
    protected void updateExternalTaskSuspensionState(final String processInstanceId, final String processDefinitionId, final String processDefinitionKey, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("processDefinitionId", processDefinitionId);
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isProcessDefinitionTenantIdSet", false);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(ExternalTaskEntity.class, "updateExternalTaskSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateExternalTaskSuspensionStateByProcessInstanceId(final String processInstanceId, final SuspensionState suspensionState) {
        this.updateExternalTaskSuspensionState(processInstanceId, null, null, suspensionState);
    }
    
    public void updateExternalTaskSuspensionStateByProcessDefinitionId(final String processDefinitionId, final SuspensionState suspensionState) {
        this.updateExternalTaskSuspensionState(null, processDefinitionId, null, suspensionState);
    }
    
    public void updateExternalTaskSuspensionStateByProcessDefinitionKey(final String processDefinitionKey, final SuspensionState suspensionState) {
        this.updateExternalTaskSuspensionState(null, null, processDefinitionKey, suspensionState);
    }
    
    public void updateExternalTaskSuspensionStateByProcessDefinitionKeyAndTenantId(final String processDefinitionKey, final String processDefinitionTenantId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isProcessDefinitionTenantIdSet", true);
        parameters.put("processDefinitionTenantId", processDefinitionTenantId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(ExternalTaskEntity.class, "updateExternalTaskSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    protected void configureQuery(final ExternalTaskQueryImpl query) {
        this.getAuthorizationManager().configureExternalTaskQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected void configureQuery(final ListQueryParameterObject parameter) {
        this.getAuthorizationManager().configureExternalTaskFetch(parameter);
        this.getTenantManager().configureQuery(parameter);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
    
    public void fireExternalTaskAvailableEvent() {
        Context.getCommandContext().getTransactionContext().addTransactionListener(TransactionState.COMMITTED, new TransactionListener() {
            @Override
            public void execute(final CommandContext commandContext) {
                ProcessEngineImpl.EXT_TASK_CONDITIONS.signalAll();
            }
        });
    }
    
    static {
        ExternalTaskManager.EXT_TASK_PRIORITY_ORDERING_PROPERTY = new QueryOrderingProperty(ExternalTaskQueryProperty.PRIORITY, Direction.DESCENDING);
    }
}
