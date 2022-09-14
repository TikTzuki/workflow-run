// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Date;
import org.zik.bpm.engine.history.CleanableHistoricBatchReportResult;
import org.zik.bpm.engine.impl.CleanableHistoricBatchReportImpl;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.query.QueryProperty;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import org.zik.bpm.engine.impl.Direction;
import org.zik.bpm.engine.impl.QueryPropertyImpl;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.batch.history.HistoricBatch;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchQueryImpl;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class HistoricBatchManager extends AbstractManager
{
    public long findBatchCountByQueryCriteria(final HistoricBatchQueryImpl historicBatchQuery) {
        this.configureQuery(historicBatchQuery);
        return (long)this.getDbEntityManager().selectOne("selectHistoricBatchCountByQueryCriteria", historicBatchQuery);
    }
    
    public List<HistoricBatch> findBatchesByQueryCriteria(final HistoricBatchQueryImpl historicBatchQuery, final Page page) {
        this.configureQuery(historicBatchQuery);
        return (List<HistoricBatch>)this.getDbEntityManager().selectList("selectHistoricBatchesByQueryCriteria", historicBatchQuery, page);
    }
    
    public HistoricBatchEntity findHistoricBatchById(final String batchId) {
        return this.getDbEntityManager().selectById(HistoricBatchEntity.class, batchId);
    }
    
    public HistoricBatchEntity findHistoricBatchByJobId(final String jobId) {
        return (HistoricBatchEntity)this.getDbEntityManager().selectOne("selectHistoricBatchByJobId", jobId);
    }
    
    public List<String> findHistoricBatchIdsForCleanup(final Integer batchSize, final Map<String, Integer> batchOperationsForHistoryCleanup, final int minuteFrom, final int minuteTo) {
        final Map<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("currentTimestamp", ClockUtil.getCurrentTime());
        queryParameters.put("map", batchOperationsForHistoryCleanup);
        if (minuteTo - minuteFrom + 1 < 60) {
            queryParameters.put("minuteFrom", minuteFrom);
            queryParameters.put("minuteTo", minuteTo);
        }
        final ListQueryParameterObject parameterObject = new ListQueryParameterObject(queryParameters, 0, batchSize);
        parameterObject.getOrderingProperties().add(new QueryOrderingProperty(new QueryPropertyImpl("END_TIME_"), Direction.ASCENDING));
        return (List<String>)this.getDbEntityManager().selectList("selectHistoricBatchIdsForCleanup", parameterObject);
    }
    
    public void deleteHistoricBatchById(final String id) {
        this.getDbEntityManager().delete(HistoricBatchEntity.class, "deleteHistoricBatchById", id);
    }
    
    public void deleteHistoricBatchesByIds(final List<String> historicBatchIds) {
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.getHistoricIncidentManager().deleteHistoricIncidentsByBatchId(historicBatchIds);
        commandContext.getHistoricJobLogManager().deleteHistoricJobLogByBatchIds(historicBatchIds);
        this.getDbEntityManager().deletePreserveOrder(HistoricBatchEntity.class, "deleteHistoricBatchByIds", historicBatchIds);
    }
    
    public void createHistoricBatch(final BatchEntity batch) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final HistoryLevel historyLevel = configuration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.BATCH_START, batch)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createBatchStartEvent(batch);
                }
            });
        }
    }
    
    public void completeHistoricBatch(final BatchEntity batch) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final HistoryLevel historyLevel = configuration.getHistoryLevel();
        if (historyLevel.isHistoryEventProduced(HistoryEventTypes.BATCH_END, batch)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createBatchEndEvent(batch);
                }
            });
        }
    }
    
    protected void configureQuery(final HistoricBatchQueryImpl query) {
        this.getAuthorizationManager().configureHistoricBatchQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    public List<CleanableHistoricBatchReportResult> findCleanableHistoricBatchesReportByCriteria(final CleanableHistoricBatchReportImpl query, final Page page, final Map<String, Integer> batchOperationsForHistoryCleanup) {
        query.setCurrentTimestamp(ClockUtil.getCurrentTime());
        query.setParameter(batchOperationsForHistoryCleanup);
        query.getOrderingProperties().add(new QueryOrderingProperty(new QueryPropertyImpl("TYPE_"), Direction.ASCENDING));
        if (batchOperationsForHistoryCleanup.isEmpty()) {
            return (List<CleanableHistoricBatchReportResult>)this.getDbEntityManager().selectList("selectOnlyFinishedBatchesReportEntities", query, page);
        }
        return (List<CleanableHistoricBatchReportResult>)this.getDbEntityManager().selectList("selectFinishedBatchesReportEntities", query, page);
    }
    
    public long findCleanableHistoricBatchesReportCountByCriteria(final CleanableHistoricBatchReportImpl query, final Map<String, Integer> batchOperationsForHistoryCleanup) {
        query.setCurrentTimestamp(ClockUtil.getCurrentTime());
        query.setParameter(batchOperationsForHistoryCleanup);
        if (batchOperationsForHistoryCleanup.isEmpty()) {
            return (long)this.getDbEntityManager().selectOne("selectOnlyFinishedBatchesReportEntitiesCount", query);
        }
        return (long)this.getDbEntityManager().selectOne("selectFinishedBatchesReportEntitiesCount", query);
    }
    
    public DbOperation deleteHistoricBatchesByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(HistoricBatchEntity.class, "deleteHistoricBatchesByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
    
    public void addRemovalTimeById(final String id, final Date removalTime) {
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.getHistoricIncidentManager().addRemovalTimeToHistoricIncidentsByBatchId(id, removalTime);
        commandContext.getHistoricJobLogManager().addRemovalTimeToJobLogByBatchId(id, removalTime);
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", id);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricBatchEntity.class, "updateHistoricBatchRemovalTimeById", parameters);
    }
}
