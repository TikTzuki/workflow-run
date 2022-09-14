// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Date;
import java.util.Iterator;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricDetail;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricDetailQueryImpl;
import org.zik.bpm.engine.impl.history.event.HistoricDetailEventEntity;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricDetailManager extends AbstractHistoricManager
{
    public void deleteHistoricDetailsByProcessInstanceIds(final List<String> historicProcessInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceIds", historicProcessInstanceIds);
        this.deleteHistoricDetails(parameters);
    }
    
    public void deleteHistoricDetailsByTaskProcessInstanceIds(final List<String> historicProcessInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("taskProcessInstanceIds", historicProcessInstanceIds);
        this.deleteHistoricDetails(parameters);
    }
    
    public void deleteHistoricDetailsByCaseInstanceIds(final List<String> historicCaseInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("caseInstanceIds", historicCaseInstanceIds);
        this.deleteHistoricDetails(parameters);
    }
    
    public void deleteHistoricDetailsByTaskCaseInstanceIds(final List<String> historicCaseInstanceIds) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("taskCaseInstanceIds", historicCaseInstanceIds);
        this.deleteHistoricDetails(parameters);
    }
    
    public void deleteHistoricDetailsByVariableInstanceId(final String historicVariableInstanceId) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("variableInstanceId", historicVariableInstanceId);
        this.deleteHistoricDetails(parameters);
    }
    
    public void deleteHistoricDetails(final Map<String, Object> parameters) {
        this.getDbEntityManager().deletePreserveOrder(ByteArrayEntity.class, "deleteHistoricDetailByteArraysByIds", parameters);
        this.getDbEntityManager().deletePreserveOrder(HistoricDetailEventEntity.class, "deleteHistoricDetailsByIds", parameters);
    }
    
    public long findHistoricDetailCountByQueryCriteria(final HistoricDetailQueryImpl historicVariableUpdateQuery) {
        this.configureQuery(historicVariableUpdateQuery);
        return (long)this.getDbEntityManager().selectOne("selectHistoricDetailCountByQueryCriteria", historicVariableUpdateQuery);
    }
    
    public List<HistoricDetail> findHistoricDetailsByQueryCriteria(final HistoricDetailQueryImpl historicVariableUpdateQuery, final Page page) {
        this.configureQuery(historicVariableUpdateQuery);
        return (List<HistoricDetail>)this.getDbEntityManager().selectList("selectHistoricDetailsByQueryCriteria", historicVariableUpdateQuery, page);
    }
    
    public void deleteHistoricDetailsByTaskId(final String taskId) {
        if (this.isHistoryEnabled()) {
            final List<HistoricDetail> historicDetails = this.findHistoricDetailsByTaskId(taskId);
            for (final HistoricDetail historicDetail : historicDetails) {
                ((HistoricDetailEventEntity)historicDetail).delete();
            }
            final List<HistoricDetailEventEntity> cachedHistoricDetails = this.getDbEntityManager().getCachedEntitiesByType(HistoricDetailEventEntity.class);
            for (final HistoricDetailEventEntity historicDetail2 : cachedHistoricDetails) {
                if (taskId.equals(historicDetail2.getTaskId())) {
                    historicDetail2.delete();
                }
            }
        }
    }
    
    public List<HistoricDetail> findHistoricDetailsByTaskId(final String taskId) {
        return (List<HistoricDetail>)this.getDbEntityManager().selectList("selectHistoricDetailsByTaskId", taskId);
    }
    
    protected void configureQuery(final HistoricDetailQueryImpl query) {
        this.getAuthorizationManager().configureHistoricDetailQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    public void addRemovalTimeToDetailsByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricDetailEventEntity.class, "updateHistoricDetailsByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToDetailsByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricDetailEventEntity.class, "updateHistoricDetailsByProcessInstanceId", parameters);
    }
    
    public DbOperation deleteHistoricDetailsByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(HistoricDetailEventEntity.class, "deleteHistoricDetailsByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
}
