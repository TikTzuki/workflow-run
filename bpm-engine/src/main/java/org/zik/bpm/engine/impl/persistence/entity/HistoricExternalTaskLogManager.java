// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.history.producer.HistoryEventProducer;
import org.zik.bpm.engine.impl.history.event.HistoryEventProcessor;
import org.zik.bpm.engine.impl.history.event.HistoryEventType;
import org.zik.bpm.engine.impl.history.event.HistoryEventTypes;
import org.zik.bpm.engine.externaltask.ExternalTask;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Map;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricExternalTaskLog;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricExternalTaskLogQueryImpl;
import org.zik.bpm.engine.impl.history.event.HistoricExternalTaskLogEntity;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class HistoricExternalTaskLogManager extends AbstractManager
{
    public HistoricExternalTaskLogEntity findHistoricExternalTaskLogById(final String HistoricExternalTaskLogId) {
        return (HistoricExternalTaskLogEntity)this.getDbEntityManager().selectOne("selectHistoricExternalTaskLog", HistoricExternalTaskLogId);
    }
    
    public List<HistoricExternalTaskLog> findHistoricExternalTaskLogsByQueryCriteria(final HistoricExternalTaskLogQueryImpl query, final Page page) {
        this.configureQuery(query);
        return (List<HistoricExternalTaskLog>)this.getDbEntityManager().selectList("selectHistoricExternalTaskLogByQueryCriteria", query, page);
    }
    
    public long findHistoricExternalTaskLogsCountByQueryCriteria(final HistoricExternalTaskLogQueryImpl query) {
        this.configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectHistoricExternalTaskLogCountByQueryCriteria", query);
    }
    
    public void addRemovalTimeToExternalTaskLogByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricExternalTaskLogEntity.class, "updateExternalTaskLogByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToExternalTaskLogByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricExternalTaskLogEntity.class, "updateExternalTaskLogByProcessInstanceId", parameters);
    }
    
    public void deleteHistoricExternalTaskLogsByProcessInstanceIds(final List<String> processInstanceIds) {
        this.deleteExceptionByteArrayByParameterMap("processInstanceIdIn", processInstanceIds.toArray());
        this.getDbEntityManager().deletePreserveOrder(HistoricExternalTaskLogEntity.class, "deleteHistoricExternalTaskLogByProcessInstanceIds", processInstanceIds);
    }
    
    public DbOperation deleteExternalTaskLogByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(HistoricExternalTaskLogEntity.class, "deleteExternalTaskLogByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
    
    protected void deleteExceptionByteArrayByParameterMap(final String key, final Object value) {
        EnsureUtil.ensureNotNull(key, value);
        final Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put(key, value);
        this.getDbEntityManager().delete(ByteArrayEntity.class, "deleteErrorDetailsByteArraysByIds", parameterMap);
    }
    
    public void fireExternalTaskCreatedEvent(final ExternalTask externalTask) {
        if (this.isHistoryEventProduced(HistoryEventTypes.EXTERNAL_TASK_CREATE, externalTask)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricExternalTaskLogCreatedEvt(externalTask);
                }
            });
        }
    }
    
    public void fireExternalTaskFailedEvent(final ExternalTask externalTask) {
        if (this.isHistoryEventProduced(HistoryEventTypes.EXTERNAL_TASK_FAIL, externalTask)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricExternalTaskLogFailedEvt(externalTask);
                }
                
                @Override
                public void postHandleSingleHistoryEventCreated(final HistoryEvent event) {
                    ((ExternalTaskEntity)externalTask).setLastFailureLogId(event.getId());
                }
            });
        }
    }
    
    public void fireExternalTaskSuccessfulEvent(final ExternalTask externalTask) {
        if (this.isHistoryEventProduced(HistoryEventTypes.EXTERNAL_TASK_SUCCESS, externalTask)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricExternalTaskLogSuccessfulEvt(externalTask);
                }
            });
        }
    }
    
    public void fireExternalTaskDeletedEvent(final ExternalTask externalTask) {
        if (this.isHistoryEventProduced(HistoryEventTypes.EXTERNAL_TASK_DELETE, externalTask)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricExternalTaskLogDeletedEvt(externalTask);
                }
            });
        }
    }
    
    protected boolean isHistoryEventProduced(final HistoryEventType eventType, final ExternalTask externalTask) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final HistoryLevel historyLevel = configuration.getHistoryLevel();
        return historyLevel.isHistoryEventProduced(eventType, externalTask);
    }
    
    protected void configureQuery(final HistoricExternalTaskLogQueryImpl query) {
        this.getAuthorizationManager().configureHistoricExternalTaskLogQuery(query);
        this.getTenantManager().configureQuery(query);
    }
}
