// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import org.zik.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import java.util.Date;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricActivityInstance;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricActivityInstanceQueryImpl;
import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricActivityInstanceManager extends AbstractHistoricManager
{
    public void deleteHistoricActivityInstancesByProcessInstanceIds(final List<String> historicProcessInstanceIds) {
        this.getDbEntityManager().deletePreserveOrder(HistoricActivityInstanceEntity.class, "deleteHistoricActivityInstancesByProcessInstanceIds", historicProcessInstanceIds);
    }
    
    public void insertHistoricActivityInstance(final HistoricActivityInstanceEntity historicActivityInstance) {
        this.getDbEntityManager().insert(historicActivityInstance);
    }
    
    public HistoricActivityInstanceEntity findHistoricActivityInstance(final String activityId, final String processInstanceId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("activityId", activityId);
        parameters.put("processInstanceId", processInstanceId);
        return (HistoricActivityInstanceEntity)this.getDbEntityManager().selectOne("selectHistoricActivityInstance", parameters);
    }
    
    public long findHistoricActivityInstanceCountByQueryCriteria(final HistoricActivityInstanceQueryImpl historicActivityInstanceQuery) {
        this.configureQuery(historicActivityInstanceQuery);
        return (long)this.getDbEntityManager().selectOne("selectHistoricActivityInstanceCountByQueryCriteria", historicActivityInstanceQuery);
    }
    
    public List<HistoricActivityInstance> findHistoricActivityInstancesByQueryCriteria(final HistoricActivityInstanceQueryImpl historicActivityInstanceQuery, final Page page) {
        this.configureQuery(historicActivityInstanceQuery);
        return (List<HistoricActivityInstance>)this.getDbEntityManager().selectList("selectHistoricActivityInstancesByQueryCriteria", historicActivityInstanceQuery, page);
    }
    
    public List<HistoricActivityInstance> findHistoricActivityInstancesByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<HistoricActivityInstance>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricActivityInstanceByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public long findHistoricActivityInstanceCountByNativeQuery(final Map<String, Object> parameterMap) {
        return (long)this.getDbEntityManager().selectOne("selectHistoricActivityInstanceCountByNativeQuery", parameterMap);
    }
    
    protected void configureQuery(final HistoricActivityInstanceQueryImpl query) {
        this.getAuthorizationManager().configureHistoricActivityInstanceQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    public void addRemovalTimeToActivityInstancesByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricActivityInstanceEventEntity.class, "updateHistoricActivityInstancesByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToActivityInstancesByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricActivityInstanceEventEntity.class, "updateHistoricActivityInstancesByProcessInstanceId", parameters);
    }
    
    public DbOperation deleteHistoricActivityInstancesByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(HistoricActivityInstanceEntity.class, "deleteHistoricActivityInstancesByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
}
