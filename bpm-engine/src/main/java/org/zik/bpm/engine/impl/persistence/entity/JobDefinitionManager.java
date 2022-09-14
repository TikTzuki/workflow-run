// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.management.JobDefinition;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.JobDefinitionQueryImpl;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class JobDefinitionManager extends AbstractManager
{
    public JobDefinitionEntity findById(final String jobDefinitionId) {
        return this.getDbEntityManager().selectById(JobDefinitionEntity.class, jobDefinitionId);
    }
    
    public List<JobDefinitionEntity> findByProcessDefinitionId(final String processDefinitionId) {
        return (List<JobDefinitionEntity>)this.getDbEntityManager().selectList("selectJobDefinitionsByProcessDefinitionId", processDefinitionId);
    }
    
    public void deleteJobDefinitionsByProcessDefinitionId(final String id) {
        this.getDbEntityManager().delete(JobDefinitionEntity.class, "deleteJobDefinitionsByProcessDefinitionId", id);
    }
    
    public List<JobDefinition> findJobDefnitionByQueryCriteria(final JobDefinitionQueryImpl jobDefinitionQuery, final Page page) {
        this.configureQuery(jobDefinitionQuery);
        return (List<JobDefinition>)this.getDbEntityManager().selectList("selectJobDefinitionByQueryCriteria", jobDefinitionQuery, page);
    }
    
    public long findJobDefinitionCountByQueryCriteria(final JobDefinitionQueryImpl jobDefinitionQuery) {
        this.configureQuery(jobDefinitionQuery);
        return (long)this.getDbEntityManager().selectOne("selectJobDefinitionCountByQueryCriteria", jobDefinitionQuery);
    }
    
    public void updateJobDefinitionSuspensionStateById(final String jobDefinitionId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("jobDefinitionId", jobDefinitionId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(JobDefinitionEntity.class, "updateJobDefinitionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateJobDefinitionSuspensionStateByProcessDefinitionId(final String processDefinitionId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionId", processDefinitionId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(JobDefinitionEntity.class, "updateJobDefinitionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateJobDefinitionSuspensionStateByProcessDefinitionKey(final String processDefinitionKey, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isProcessDefinitionTenantIdSet", false);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(JobDefinitionEntity.class, "updateJobDefinitionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateJobDefinitionSuspensionStateByProcessDefinitionKeyAndTenantId(final String processDefinitionKey, final String processDefinitionTenantId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isProcessDefinitionTenantIdSet", true);
        parameters.put("processDefinitionTenantId", processDefinitionTenantId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(JobDefinitionEntity.class, "updateJobDefinitionSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    protected void configureQuery(final JobDefinitionQueryImpl query) {
        this.getAuthorizationManager().configureJobDefinitionQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
}
