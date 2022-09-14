// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.CleanableHistoricCaseInstanceReportResult;
import org.zik.bpm.engine.impl.CleanableHistoricCaseInstanceReportImpl;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricCaseInstance;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricCaseInstanceQueryImpl;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.List;
import org.zik.bpm.engine.impl.history.event.HistoricCaseInstanceEventEntity;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricCaseInstanceManager extends AbstractHistoricManager
{
    public HistoricCaseInstanceEntity findHistoricCaseInstance(final String caseInstanceId) {
        if (this.isHistoryEnabled()) {
            return this.getDbEntityManager().selectById(HistoricCaseInstanceEntity.class, caseInstanceId);
        }
        return null;
    }
    
    public HistoricCaseInstanceEventEntity findHistoricCaseInstanceEvent(final String eventId) {
        if (this.isHistoryEnabled()) {
            return this.getDbEntityManager().selectById(HistoricCaseInstanceEventEntity.class, eventId);
        }
        return null;
    }
    
    public void deleteHistoricCaseInstanceByCaseDefinitionId(final String caseDefinitionId) {
        if (this.isHistoryEnabled()) {
            final List<String> historicCaseInstanceIds = (List<String>)this.getDbEntityManager().selectList("selectHistoricCaseInstanceIdsByCaseDefinitionId", caseDefinitionId);
            if (historicCaseInstanceIds != null && !historicCaseInstanceIds.isEmpty()) {
                this.deleteHistoricCaseInstancesByIds(historicCaseInstanceIds);
            }
        }
    }
    
    public void deleteHistoricCaseInstancesByIds(final List<String> historicCaseInstanceIds) {
        if (this.isHistoryEnabled()) {
            this.getHistoricDetailManager().deleteHistoricDetailsByCaseInstanceIds(historicCaseInstanceIds);
            this.getHistoricVariableInstanceManager().deleteHistoricVariableInstancesByCaseInstanceIds(historicCaseInstanceIds);
            this.getHistoricCaseActivityInstanceManager().deleteHistoricCaseActivityInstancesByCaseInstanceIds(historicCaseInstanceIds);
            this.getHistoricTaskInstanceManager().deleteHistoricTaskInstancesByCaseInstanceIds(historicCaseInstanceIds);
            this.getDbEntityManager().delete(HistoricCaseInstanceEntity.class, "deleteHistoricCaseInstancesByIds", historicCaseInstanceIds);
        }
    }
    
    public long findHistoricCaseInstanceCountByQueryCriteria(final HistoricCaseInstanceQueryImpl historicCaseInstanceQuery) {
        if (this.isHistoryEnabled()) {
            this.configureHistoricCaseInstanceQuery(historicCaseInstanceQuery);
            return (long)this.getDbEntityManager().selectOne("selectHistoricCaseInstanceCountByQueryCriteria", historicCaseInstanceQuery);
        }
        return 0L;
    }
    
    public List<HistoricCaseInstance> findHistoricCaseInstancesByQueryCriteria(final HistoricCaseInstanceQueryImpl historicCaseInstanceQuery, final Page page) {
        if (this.isHistoryEnabled()) {
            this.configureHistoricCaseInstanceQuery(historicCaseInstanceQuery);
            return (List<HistoricCaseInstance>)this.getDbEntityManager().selectList("selectHistoricCaseInstancesByQueryCriteria", historicCaseInstanceQuery, page);
        }
        return (List<HistoricCaseInstance>)Collections.EMPTY_LIST;
    }
    
    public List<HistoricCaseInstance> findHistoricCaseInstancesByNativeQuery(final Map<String, Object> parameterMap, final int firstResult, final int maxResults) {
        return (List<HistoricCaseInstance>)this.getDbEntityManager().selectListWithRawParameter("selectHistoricCaseInstanceByNativeQuery", parameterMap, firstResult, maxResults);
    }
    
    public long findHistoricCaseInstanceCountByNativeQuery(final Map<String, Object> parameterMap) {
        return (long)this.getDbEntityManager().selectOne("selectHistoricCaseInstanceCountByNativeQuery", parameterMap);
    }
    
    protected void configureHistoricCaseInstanceQuery(final HistoricCaseInstanceQueryImpl query) {
        this.getTenantManager().configureQuery(query);
    }
    
    public List<String> findHistoricCaseInstanceIdsForCleanup(final int batchSize, final int minuteFrom, final int minuteTo) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("currentTimestamp", ClockUtil.getCurrentTime());
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        final ListQueryParameterObject parameterObject = new ListQueryParameterObject(parameters, 0, batchSize);
        return (List<String>)this.getDbEntityManager().selectList("selectHistoricCaseInstanceIdsForCleanup", parameterObject);
    }
    
    public List<CleanableHistoricCaseInstanceReportResult> findCleanableHistoricCaseInstancesReportByCriteria(final CleanableHistoricCaseInstanceReportImpl query, final Page page) {
        query.setCurrentTimestamp(ClockUtil.getCurrentTime());
        this.getTenantManager().configureQuery(query);
        return (List<CleanableHistoricCaseInstanceReportResult>)this.getDbEntityManager().selectList("selectFinishedCaseInstancesReportEntities", query, page);
    }
    
    public long findCleanableHistoricCaseInstancesReportCountByCriteria(final CleanableHistoricCaseInstanceReportImpl query) {
        query.setCurrentTimestamp(ClockUtil.getCurrentTime());
        this.getTenantManager().configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectFinishedCaseInstancesReportEntitiesCount", query);
    }
}
