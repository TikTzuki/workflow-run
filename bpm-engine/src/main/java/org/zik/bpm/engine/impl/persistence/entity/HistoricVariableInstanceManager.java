// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricVariableInstanceQuery;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricVariableInstanceQueryImpl;
import java.util.Iterator;
import org.zik.bpm.engine.history.HistoricVariableInstance;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricVariableInstanceManager extends AbstractHistoricManager
{
    public void deleteHistoricVariableInstanceByVariableInstanceId(final String historicVariableInstanceId) {
        if (this.isHistoryEnabled()) {
            final HistoricVariableInstanceEntity historicVariableInstance = this.findHistoricVariableInstanceByVariableInstanceId(historicVariableInstanceId);
            if (historicVariableInstance != null) {
                historicVariableInstance.delete();
            }
        }
    }
    
    public void deleteHistoricVariableInstanceByProcessInstanceIds(final List<String> historicProcessInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceIds", historicProcessInstanceIds);
        this.deleteHistoricVariableInstances(parameters);
    }
    
    public void deleteHistoricVariableInstancesByTaskProcessInstanceIds(final List<String> historicProcessInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("taskProcessInstanceIds", historicProcessInstanceIds);
        this.deleteHistoricVariableInstances(parameters);
    }
    
    public void deleteHistoricVariableInstanceByCaseInstanceId(final String historicCaseInstanceId) {
        this.deleteHistoricVariableInstancesByProcessCaseInstanceId(null, historicCaseInstanceId);
    }
    
    public void deleteHistoricVariableInstancesByCaseInstanceIds(final List<String> historicCaseInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("caseInstanceIds", historicCaseInstanceIds);
        this.deleteHistoricVariableInstances(parameters);
    }
    
    protected void deleteHistoricVariableInstances(final Map<String, Object> parameters) {
        this.getDbEntityManager().deletePreserveOrder(ByteArrayEntity.class, "deleteHistoricVariableInstanceByteArraysByIds", parameters);
        this.getDbEntityManager().deletePreserveOrder(HistoricVariableInstanceEntity.class, "deleteHistoricVariableInstanceByIds", parameters);
    }
    
    protected void deleteHistoricVariableInstancesByProcessCaseInstanceId(final String historicProcessInstanceId, final String historicCaseInstanceId) {
        EnsureUtil.ensureOnlyOneNotNull("Only the process instance or case instance id should be set", historicProcessInstanceId, historicCaseInstanceId);
        if (this.isHistoryEnabled()) {
            List<HistoricVariableInstance> historicVariableInstances;
            if (historicProcessInstanceId != null) {
                historicVariableInstances = this.findHistoricVariableInstancesByProcessInstanceId(historicProcessInstanceId);
            }
            else {
                historicVariableInstances = this.findHistoricVariableInstancesByCaseInstanceId(historicCaseInstanceId);
            }
            for (final HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
                ((HistoricVariableInstanceEntity)historicVariableInstance).delete();
            }
            final List<HistoricVariableInstanceEntity> cachedHistoricVariableInstances = this.getDbEntityManager().getCachedEntitiesByType(HistoricVariableInstanceEntity.class);
            for (final HistoricVariableInstanceEntity historicVariableInstance2 : cachedHistoricVariableInstances) {
                if ((historicProcessInstanceId != null && historicProcessInstanceId.equals(historicVariableInstance2.getProcessInstanceId())) || (historicCaseInstanceId != null && historicCaseInstanceId.equals(historicVariableInstance2.getCaseInstanceId()))) {
                    historicVariableInstance2.delete();
                }
            }
        }
    }
    
    public List<HistoricVariableInstance> findHistoricVariableInstancesByProcessInstanceId(final String processInstanceId) {
        return (List<HistoricVariableInstance>)this.getDbEntityManager().selectList("selectHistoricVariablesByProcessInstanceId", processInstanceId);
    }
    
    public List<HistoricVariableInstance> findHistoricVariableInstancesByCaseInstanceId(final String caseInstanceId) {
        return (List<HistoricVariableInstance>)this.getDbEntityManager().selectList("selectHistoricVariablesByCaseInstanceId", caseInstanceId);
    }
    
    public long findHistoricVariableInstanceCountByQueryCriteria(final HistoricVariableInstanceQueryImpl historicProcessVariableQuery) {
        this.configureQuery(historicProcessVariableQuery);
        return (long)this.getDbEntityManager().selectOne("selectHistoricVariableInstanceCountByQueryCriteria", historicProcessVariableQuery);
    }
    
    public List<HistoricVariableInstance> findHistoricVariableInstancesByQueryCriteria(final HistoricVariableInstanceQueryImpl historicProcessVariableQuery, final Page page) {
        this.configureQuery(historicProcessVariableQuery);
        return (List<HistoricVariableInstance>)this.getDbEntityManager().selectList("selectHistoricVariableInstanceByQueryCriteria", historicProcessVariableQuery, page);
    }
    
    public HistoricVariableInstanceEntity findHistoricVariableInstanceByVariableInstanceId(final String variableInstanceId) {
        return (HistoricVariableInstanceEntity)this.getDbEntityManager().selectOne("selectHistoricVariableInstanceByVariableInstanceId", variableInstanceId);
    }
    
    public void deleteHistoricVariableInstancesByTaskId(final String taskId) {
        if (this.isHistoryEnabled()) {
            final HistoricVariableInstanceQuery historicProcessVariableQuery = new HistoricVariableInstanceQueryImpl().taskIdIn(taskId);
            final List<HistoricVariableInstance> historicProcessVariables = ((Query<T, HistoricVariableInstance>)historicProcessVariableQuery).list();
            for (final HistoricVariableInstance historicProcessVariable : historicProcessVariables) {
                ((HistoricVariableInstanceEntity)historicProcessVariable).delete();
            }
        }
    }
    
    public void addRemovalTimeToVariableInstancesByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricVariableInstanceEntity.class, "updateHistoricVariableInstancesByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToVariableInstancesByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricVariableInstanceEntity.class, "updateHistoricVariableInstancesByProcessInstanceId", parameters);
    }
    
    public List<HistoricVariableInstance> findHistoricVariableInstancesByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<HistoricVariableInstance>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricVariableInstanceByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public long findHistoricVariableInstanceCountByNativeQuery(final Map<String, Object> parameterMap) {
        return (long)this.getDbEntityManager().selectOne("selectHistoricVariableInstanceCountByNativeQuery", parameterMap);
    }
    
    protected void configureQuery(final HistoricVariableInstanceQueryImpl query) {
        this.getAuthorizationManager().configureHistoricVariableInstanceQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    public DbOperation deleteHistoricVariableInstancesByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(HistoricVariableInstanceEntity.class, "deleteHistoricVariableInstancesByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
}
