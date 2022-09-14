// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Collections;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.runtime.VariableInstance;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.VariableInstanceQueryImpl;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class VariableInstanceManager extends AbstractManager
{
    public List<VariableInstanceEntity> findVariableInstancesByTaskId(final String taskId) {
        return this.findVariableInstancesByTaskIdAndVariableNames(taskId, null);
    }
    
    public List<VariableInstanceEntity> findVariableInstancesByTaskIdAndVariableNames(final String taskId, final Collection<String> variableNames) {
        final Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("taskId", taskId);
        parameter.put("variableNames", variableNames);
        return (List<VariableInstanceEntity>)this.getDbEntityManager().selectList("selectVariablesByTaskId", parameter);
    }
    
    public List<VariableInstanceEntity> findVariableInstancesByExecutionId(final String executionId) {
        return this.findVariableInstancesByExecutionIdAndVariableNames(executionId, null);
    }
    
    public List<VariableInstanceEntity> findVariableInstancesByExecutionIdAndVariableNames(final String executionId, final Collection<String> variableNames) {
        final Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("executionId", executionId);
        parameter.put("variableNames", variableNames);
        return (List<VariableInstanceEntity>)this.getDbEntityManager().selectList("selectVariablesByExecutionId", parameter);
    }
    
    public List<VariableInstanceEntity> findVariableInstancesByProcessInstanceId(final String processInstanceId) {
        return (List<VariableInstanceEntity>)this.getDbEntityManager().selectList("selectVariablesByProcessInstanceId", processInstanceId);
    }
    
    public List<VariableInstanceEntity> findVariableInstancesByCaseExecutionId(final String caseExecutionId) {
        return this.findVariableInstancesByCaseExecutionIdAndVariableNames(caseExecutionId, null);
    }
    
    public List<VariableInstanceEntity> findVariableInstancesByCaseExecutionIdAndVariableNames(final String caseExecutionId, final Collection<String> variableNames) {
        final Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("caseExecutionId", caseExecutionId);
        parameter.put("variableNames", variableNames);
        return (List<VariableInstanceEntity>)this.getDbEntityManager().selectList("selectVariablesByCaseExecutionId", parameter);
    }
    
    public void deleteVariableInstanceByTask(final TaskEntity task) {
        final List<VariableInstanceEntity> variableInstances = task.variableStore.getVariables();
        for (final VariableInstanceEntity variableInstance : variableInstances) {
            variableInstance.delete();
        }
    }
    
    public long findVariableInstanceCountByQueryCriteria(final VariableInstanceQueryImpl variableInstanceQuery) {
        this.configureQuery(variableInstanceQuery);
        return (long)this.getDbEntityManager().selectOne("selectVariableInstanceCountByQueryCriteria", variableInstanceQuery);
    }
    
    public List<VariableInstance> findVariableInstanceByQueryCriteria(final VariableInstanceQueryImpl variableInstanceQuery, final Page page) {
        this.configureQuery(variableInstanceQuery);
        return (List<VariableInstance>)this.getDbEntityManager().selectList("selectVariableInstanceByQueryCriteria", variableInstanceQuery, page);
    }
    
    protected void configureQuery(final VariableInstanceQueryImpl query) {
        this.getAuthorizationManager().configureVariableInstanceQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    public List<VariableInstanceEntity> findVariableInstancesByBatchId(final String batchId) {
        final Map<String, String> parameters = Collections.singletonMap("batchId", batchId);
        return (List<VariableInstanceEntity>)this.getDbEntityManager().selectList("selectVariableInstancesByBatchId", parameters);
    }
}
