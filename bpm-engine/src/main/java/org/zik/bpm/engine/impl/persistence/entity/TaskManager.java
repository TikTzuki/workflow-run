// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.TaskQueryImpl;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.task.Task;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class TaskManager extends AbstractManager
{
    public void insertTask(final TaskEntity task) {
        this.getDbEntityManager().insert(task);
        this.createDefaultAuthorizations(task);
    }
    
    public void deleteTasksByProcessInstanceId(final String processInstanceId, final String deleteReason, final boolean cascade, final boolean skipCustomListeners) {
        final List<TaskEntity> tasks = ((AbstractQuery<T, TaskEntity>)this.getDbEntityManager().createTaskQuery().processInstanceId(processInstanceId)).list();
        final String reason = (deleteReason == null || deleteReason.length() == 0) ? "deleted" : deleteReason;
        for (final TaskEntity task : tasks) {
            task.delete(reason, cascade, skipCustomListeners);
        }
    }
    
    public void deleteTasksByCaseInstanceId(final String caseInstanceId, final String deleteReason, final boolean cascade) {
        final List<TaskEntity> tasks = ((Query<T, TaskEntity>)this.getDbEntityManager().createTaskQuery().caseInstanceId(caseInstanceId)).list();
        final String reason = (deleteReason == null || deleteReason.length() == 0) ? "deleted" : deleteReason;
        for (final TaskEntity task : tasks) {
            task.delete(reason, cascade, false);
        }
    }
    
    public void deleteTask(final TaskEntity task, final String deleteReason, final boolean cascade, final boolean skipCustomListeners) {
        if (!task.isDeleted()) {
            task.setDeleted(true);
            final CommandContext commandContext = Context.getCommandContext();
            final String taskId = task.getId();
            final List<Task> subTasks = this.findTasksByParentTaskId(taskId);
            for (final Task subTask : subTasks) {
                ((TaskEntity)subTask).delete(deleteReason, cascade, skipCustomListeners);
            }
            task.deleteIdentityLinks();
            commandContext.getVariableInstanceManager().deleteVariableInstanceByTask(task);
            if (cascade) {
                commandContext.getHistoricTaskInstanceManager().deleteHistoricTaskInstanceById(taskId);
            }
            else {
                commandContext.getHistoricTaskInstanceManager().markTaskInstanceEnded(taskId, deleteReason);
            }
            this.deleteAuthorizations(Resources.TASK, taskId);
            this.getDbEntityManager().delete(task);
        }
    }
    
    public TaskEntity findTaskById(final String id) {
        EnsureUtil.ensureNotNull("Invalid task id", "id", id);
        return this.getDbEntityManager().selectById(TaskEntity.class, id);
    }
    
    public List<TaskEntity> findTasksByExecutionId(final String executionId) {
        return (List<TaskEntity>)this.getDbEntityManager().selectList("selectTasksByExecutionId", executionId);
    }
    
    public TaskEntity findTaskByCaseExecutionId(final String caseExecutionId) {
        return (TaskEntity)this.getDbEntityManager().selectOne("selectTaskByCaseExecutionId", caseExecutionId);
    }
    
    public List<TaskEntity> findTasksByProcessInstanceId(final String processInstanceId) {
        return (List<TaskEntity>)this.getDbEntityManager().selectList("selectTasksByProcessInstanceId", processInstanceId);
    }
    
    @Deprecated
    public List<Task> findTasksByQueryCriteria(final TaskQueryImpl taskQuery, final Page page) {
        taskQuery.setFirstResult(page.getFirstResult());
        taskQuery.setMaxResults(page.getMaxResults());
        return this.findTasksByQueryCriteria(taskQuery);
    }
    
    public List<Task> findTasksByQueryCriteria(final TaskQueryImpl taskQuery) {
        this.configureQuery(taskQuery);
        return (List<Task>)this.getDbEntityManager().selectList("selectTaskByQueryCriteria", taskQuery);
    }
    
    public long findTaskCountByQueryCriteria(final TaskQueryImpl taskQuery) {
        this.configureQuery(taskQuery);
        return (long)this.getDbEntityManager().selectOne("selectTaskCountByQueryCriteria", taskQuery);
    }
    
    public List<Task> findTasksByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<Task>)this.getDbEntityManager().selectListWithRawParameter("selectTaskByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public long findTaskCountByNativeQuery(final Map<String, Object> parameterMap) {
        return (long)this.getDbEntityManager().selectOne("selectTaskCountByNativeQuery", parameterMap);
    }
    
    public List<Task> findTasksByParentTaskId(final String parentTaskId) {
        return (List<Task>)this.getDbEntityManager().selectList("selectTasksByParentTaskId", parentTaskId);
    }
    
    public void updateTaskSuspensionStateByProcessDefinitionId(final String processDefinitionId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionId", processDefinitionId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(TaskEntity.class, "updateTaskSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateTaskSuspensionStateByProcessInstanceId(final String processInstanceId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(TaskEntity.class, "updateTaskSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateTaskSuspensionStateByProcessDefinitionKey(final String processDefinitionKey, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isProcessDefinitionTenantIdSet", false);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(TaskEntity.class, "updateTaskSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateTaskSuspensionStateByProcessDefinitionKeyAndTenantId(final String processDefinitionKey, final String processDefinitionTenantId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isProcessDefinitionTenantIdSet", true);
        parameters.put("processDefinitionTenantId", processDefinitionTenantId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(TaskEntity.class, "updateTaskSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateTaskSuspensionStateByCaseExecutionId(final String caseExecutionId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("caseExecutionId", caseExecutionId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(TaskEntity.class, "updateTaskSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    protected void createDefaultAuthorizations(final TaskEntity task) {
        if (this.isAuthorizationEnabled()) {
            final ResourceAuthorizationProvider provider = this.getResourceAuthorizationProvider();
            final AuthorizationEntity[] authorizations = provider.newTask(task);
            this.saveDefaultAuthorizations(authorizations);
        }
    }
    
    protected void configureQuery(final TaskQueryImpl query) {
        this.getAuthorizationManager().configureTaskQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
}
