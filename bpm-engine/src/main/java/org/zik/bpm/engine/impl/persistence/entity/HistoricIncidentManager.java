// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Map;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.history.event.HistoricIncidentEventEntity;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricIncident;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricIncidentQueryImpl;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricIncidentManager extends AbstractHistoricManager
{
    public long findHistoricIncidentCountByQueryCriteria(final HistoricIncidentQueryImpl query) {
        this.configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectHistoricIncidentCountByQueryCriteria", query);
    }
    
    public HistoricIncidentEntity findHistoricIncidentById(final String id) {
        return (HistoricIncidentEntity)this.getDbEntityManager().selectOne("selectHistoricIncidentById", id);
    }
    
    public List<HistoricIncident> findHistoricIncidentByQueryCriteria(final HistoricIncidentQueryImpl query, final Page page) {
        this.configureQuery(query);
        return (List<HistoricIncident>)this.getDbEntityManager().selectList("selectHistoricIncidentByQueryCriteria", query, page);
    }
    
    public void addRemovalTimeToIncidentsByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricIncidentEventEntity.class, "updateHistoricIncidentsByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToIncidentsByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricIncidentEventEntity.class, "updateHistoricIncidentsByProcessInstanceId", parameters);
    }
    
    public void deleteHistoricIncidentsByProcessInstanceIds(final List<String> processInstanceIds) {
        this.getDbEntityManager().deletePreserveOrder(HistoricIncidentEntity.class, "deleteHistoricIncidentsByProcessInstanceIds", processInstanceIds);
    }
    
    public void deleteHistoricIncidentsByProcessDefinitionId(final String processDefinitionId) {
        if (this.isHistoryEventProduced()) {
            this.getDbEntityManager().delete(HistoricIncidentEntity.class, "deleteHistoricIncidentsByProcessDefinitionId", processDefinitionId);
        }
    }
    
    public void deleteHistoricIncidentsByJobDefinitionId(final String jobDefinitionId) {
        if (this.isHistoryEventProduced()) {
            this.getDbEntityManager().delete(HistoricIncidentEntity.class, "deleteHistoricIncidentsByJobDefinitionId", jobDefinitionId);
        }
    }
    
    public void deleteHistoricIncidentsByBatchId(final List<String> historicBatchIds) {
        if (this.isHistoryEventProduced()) {
            this.getDbEntityManager().delete(HistoricIncidentEntity.class, "deleteHistoricIncidentsByBatchIds", historicBatchIds);
        }
    }
    
    protected void configureQuery(final HistoricIncidentQueryImpl query) {
        this.getAuthorizationManager().configureHistoricIncidentQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected boolean isHistoryEventProduced() {
        final HistoryLevel historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        return historyLevel.isHistoryEventProduced(HistoryEventTypes.INCIDENT_CREATE, null) || historyLevel.isHistoryEventProduced(HistoryEventTypes.INCIDENT_DELETE, null) || historyLevel.isHistoryEventProduced(HistoryEventTypes.INCIDENT_MIGRATE, null) || historyLevel.isHistoryEventProduced(HistoryEventTypes.INCIDENT_RESOLVE, null);
    }
    
    public DbOperation deleteHistoricIncidentsByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(HistoricIncidentEntity.class, "deleteHistoricIncidentsByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
    
    public void addRemovalTimeToHistoricIncidentsByBatchId(final String batchId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("batchId", batchId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricIncidentEntity.class, "updateHistoricIncidentsByBatchId", parameters);
    }
}
