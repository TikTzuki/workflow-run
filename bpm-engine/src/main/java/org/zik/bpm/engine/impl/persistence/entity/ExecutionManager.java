// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
import org.zik.bpm.engine.runtime.Execution;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.impl.ProcessInstanceQueryImpl;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.ExecutionQueryImpl;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class ExecutionManager extends AbstractManager
{
    protected static final EnginePersistenceLogger LOG;
    
    public void insertExecution(final ExecutionEntity execution) {
        this.getDbEntityManager().insert(execution);
        this.createDefaultAuthorizations(execution);
    }
    
    public void deleteExecution(final ExecutionEntity execution) {
        this.getDbEntityManager().delete(execution);
        if (execution.isProcessInstanceExecution()) {
            this.deleteAuthorizations(Resources.PROCESS_INSTANCE, execution.getProcessInstanceId());
        }
    }
    
    public void deleteProcessInstancesByProcessDefinition(final String processDefinitionId, final String deleteReason, final boolean cascade, final boolean skipCustomListeners, final boolean skipIoMappings) {
        final List<String> processInstanceIds = (List<String>)this.getDbEntityManager().selectList("selectProcessInstanceIdsByProcessDefinitionId", processDefinitionId);
        for (final String processInstanceId : processInstanceIds) {
            this.deleteProcessInstance(processInstanceId, deleteReason, cascade, skipCustomListeners, false, skipIoMappings, false);
        }
        if (cascade) {
            this.getHistoricProcessInstanceManager().deleteHistoricProcessInstanceByProcessDefinitionId(processDefinitionId);
        }
    }
    
    public void deleteProcessInstance(final String processInstanceId, final String deleteReason) {
        this.deleteProcessInstance(processInstanceId, deleteReason, false, false);
    }
    
    public void deleteProcessInstance(final String processInstanceId, final String deleteReason, final boolean cascade, final boolean skipCustomListeners) {
        this.deleteProcessInstance(processInstanceId, deleteReason, cascade, skipCustomListeners, false, false, false);
    }
    
    public void deleteProcessInstance(final String processInstanceId, final String deleteReason, final boolean cascade, final boolean skipCustomListeners, final boolean externallyTerminated, final boolean skipIoMappings, final boolean skipSubprocesses) {
        final ExecutionEntity execution = this.findExecutionById(processInstanceId);
        if (execution == null) {
            throw ExecutionManager.LOG.requestedProcessInstanceNotFoundException(processInstanceId);
        }
        this.getTaskManager().deleteTasksByProcessInstanceId(processInstanceId, deleteReason, cascade, skipCustomListeners);
        execution.deleteCascade(deleteReason, skipCustomListeners, skipIoMappings, externallyTerminated, skipSubprocesses);
        if (cascade) {
            this.getHistoricProcessInstanceManager().deleteHistoricProcessInstanceByIds(Arrays.asList(processInstanceId));
        }
    }
    
    public ExecutionEntity findSubProcessInstanceBySuperExecutionId(final String superExecutionId) {
        return (ExecutionEntity)this.getDbEntityManager().selectOne("selectSubProcessInstanceBySuperExecutionId", superExecutionId);
    }
    
    public ExecutionEntity findSubProcessInstanceBySuperCaseExecutionId(final String superCaseExecutionId) {
        return (ExecutionEntity)this.getDbEntityManager().selectOne("selectSubProcessInstanceBySuperCaseExecutionId", superCaseExecutionId);
    }
    
    public List<ExecutionEntity> findChildExecutionsByParentExecutionId(final String parentExecutionId) {
        return (List<ExecutionEntity>)this.getDbEntityManager().selectList("selectExecutionsByParentExecutionId", parentExecutionId);
    }
    
    public List<ExecutionEntity> findExecutionsByProcessInstanceId(final String processInstanceId) {
        return (List<ExecutionEntity>)this.getDbEntityManager().selectList("selectExecutionsByProcessInstanceId", processInstanceId);
    }
    
    public ExecutionEntity findExecutionById(final String executionId) {
        return this.getDbEntityManager().selectById(ExecutionEntity.class, executionId);
    }
    
    public long findExecutionCountByQueryCriteria(final ExecutionQueryImpl executionQuery) {
        this.configureQuery(executionQuery);
        return (long)this.getDbEntityManager().selectOne("selectExecutionCountByQueryCriteria", executionQuery);
    }
    
    public List<ExecutionEntity> findExecutionsByQueryCriteria(final ExecutionQueryImpl executionQuery, final Page page) {
        this.configureQuery(executionQuery);
        return (List<ExecutionEntity>)this.getDbEntityManager().selectList("selectExecutionsByQueryCriteria", executionQuery, page);
    }
    
    public long findProcessInstanceCountByQueryCriteria(final ProcessInstanceQueryImpl processInstanceQuery) {
        this.configureQuery(processInstanceQuery);
        return (long)this.getDbEntityManager().selectOne("selectProcessInstanceCountByQueryCriteria", processInstanceQuery);
    }
    
    public List<ProcessInstance> findProcessInstancesByQueryCriteria(final ProcessInstanceQueryImpl processInstanceQuery, final Page page) {
        this.configureQuery(processInstanceQuery);
        return (List<ProcessInstance>)this.getDbEntityManager().selectList("selectProcessInstanceByQueryCriteria", processInstanceQuery, page);
    }
    
    public List<String> findProcessInstancesIdsByQueryCriteria(final ProcessInstanceQueryImpl processInstanceQuery) {
        this.configureQuery(processInstanceQuery);
        return (List<String>)this.getDbEntityManager().selectList("selectProcessInstanceIdsByQueryCriteria", processInstanceQuery);
    }
    
    public List<ImmutablePair<String, String>> findDeploymentIdMappingsByQueryCriteria(final ProcessInstanceQueryImpl processInstanceQuery) {
        this.configureQuery(processInstanceQuery);
        return (List<ImmutablePair<String, String>>)this.getDbEntityManager().selectList("selectProcessInstanceDeploymentIdMappingsByQueryCriteria", processInstanceQuery);
    }
    
    public List<ExecutionEntity> findEventScopeExecutionsByActivityId(final String activityRef, final String parentExecutionId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("activityId", activityRef);
        parameters.put("parentExecutionId", parentExecutionId);
        return (List<ExecutionEntity>)this.getDbEntityManager().selectList("selectExecutionsByParentExecutionId", parameters);
    }
    
    public List<Execution> findExecutionsByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<Execution>)this.getDbEntityManager().selectListWithRawParameter("selectExecutionByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public List<ProcessInstance> findProcessInstanceByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<ProcessInstance>)this.getDbEntityManager().selectListWithRawParameter("selectExecutionByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public long findExecutionCountByNativeQuery(final Map<String, Object> parameterMap) {
        return (long)this.getDbEntityManager().selectOne("selectExecutionCountByNativeQuery", parameterMap);
    }
    
    public void updateExecutionSuspensionStateByProcessDefinitionId(final String processDefinitionId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionId", processDefinitionId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(ExecutionEntity.class, "updateExecutionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateExecutionSuspensionStateByProcessInstanceId(final String processInstanceId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(ExecutionEntity.class, "updateExecutionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateExecutionSuspensionStateByProcessDefinitionKey(final String processDefinitionKey, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isTenantIdSet", false);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(ExecutionEntity.class, "updateExecutionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateExecutionSuspensionStateByProcessDefinitionKeyAndTenantId(final String processDefinitionKey, final String tenantId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isTenantIdSet", true);
        parameters.put("tenantId", tenantId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(ExecutionEntity.class, "updateExecutionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    protected void createDefaultAuthorizations(final ExecutionEntity execution) {
        if (execution.isProcessInstanceExecution() && this.isAuthorizationEnabled()) {
            final ResourceAuthorizationProvider provider = this.getResourceAuthorizationProvider();
            final AuthorizationEntity[] authorizations = provider.newProcessInstance(execution);
            this.saveDefaultAuthorizations(authorizations);
        }
    }
    
    protected void configureQuery(final AbstractQuery<?, ?> query) {
        this.getAuthorizationManager().configureExecutionQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
