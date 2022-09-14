// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Map;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.history.event.HistoricIdentityLinkLogEventEntity;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricIdentityLinkLog;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricIdentityLinkLogQueryImpl;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricIdentityLinkLogManager extends AbstractHistoricManager
{
    public long findHistoricIdentityLinkLogCountByQueryCriteria(final HistoricIdentityLinkLogQueryImpl query) {
        this.configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectHistoricIdentityLinkCountByQueryCriteria", query);
    }
    
    public List<HistoricIdentityLinkLog> findHistoricIdentityLinkLogByQueryCriteria(final HistoricIdentityLinkLogQueryImpl query, final Page page) {
        this.configureQuery(query);
        return (List<HistoricIdentityLinkLog>)this.getDbEntityManager().selectList("selectHistoricIdentityLinkByQueryCriteria", query, page);
    }
    
    public void addRemovalTimeToIdentityLinkLogByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricIdentityLinkLogEventEntity.class, "updateIdentityLinkLogByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToIdentityLinkLogByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricIdentityLinkLogEventEntity.class, "updateIdentityLinkLogByProcessInstanceId", parameters);
    }
    
    public void deleteHistoricIdentityLinksLogByProcessDefinitionId(final String processDefId) {
        if (this.isHistoryEventProduced()) {
            this.getDbEntityManager().delete(HistoricIdentityLinkLogEntity.class, "deleteHistoricIdentityLinksByProcessDefinitionId", processDefId);
        }
    }
    
    public void deleteHistoricIdentityLinksLogByTaskId(final String taskId) {
        if (this.isHistoryEventProduced()) {
            this.getDbEntityManager().delete(HistoricIdentityLinkLogEntity.class, "deleteHistoricIdentityLinksByTaskId", taskId);
        }
    }
    
    public void deleteHistoricIdentityLinksLogByTaskProcessInstanceIds(final List<String> processInstanceIds) {
        this.getDbEntityManager().deletePreserveOrder(HistoricIdentityLinkLogEntity.class, "deleteHistoricIdentityLinksByTaskProcessInstanceIds", processInstanceIds);
    }
    
    public void deleteHistoricIdentityLinksLogByTaskCaseInstanceIds(final List<String> caseInstanceIds) {
        this.getDbEntityManager().deletePreserveOrder(HistoricIdentityLinkLogEntity.class, "deleteHistoricIdentityLinksByTaskCaseInstanceIds", caseInstanceIds);
    }
    
    public DbOperation deleteHistoricIdentityLinkLogByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(HistoricIdentityLinkLogEntity.class, "deleteHistoricIdentityLinkLogByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
    
    protected void configureQuery(final HistoricIdentityLinkLogQueryImpl query) {
        this.getAuthorizationManager().configureHistoricIdentityLinkQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected boolean isHistoryEventProduced() {
        final HistoryLevel historyLevel = Context.getProcessEngineConfiguration().getHistoryLevel();
        return historyLevel.isHistoryEventProduced(HistoryEventTypes.IDENTITY_LINK_ADD, null) || historyLevel.isHistoryEventProduced(HistoryEventTypes.IDENTITY_LINK_DELETE, null);
    }
}
