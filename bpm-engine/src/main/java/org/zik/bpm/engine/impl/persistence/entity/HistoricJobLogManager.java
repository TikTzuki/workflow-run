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
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import java.util.Map;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.HashMap;
import java.util.Date;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricJobLogQueryImpl;
import org.zik.bpm.engine.history.HistoricJobLog;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.AbstractHistoricManager;

public class HistoricJobLogManager extends AbstractHistoricManager
{
    public HistoricJobLogEventEntity findHistoricJobLogById(final String historicJobLogId) {
        return (HistoricJobLogEventEntity)this.getDbEntityManager().selectOne("selectHistoricJobLog", historicJobLogId);
    }
    
    public List<HistoricJobLog> findHistoricJobLogsByDeploymentId(final String deploymentId) {
        return (List<HistoricJobLog>)this.getDbEntityManager().selectList("selectHistoricJobLogByDeploymentId", deploymentId);
    }
    
    public List<HistoricJobLog> findHistoricJobLogsByQueryCriteria(final HistoricJobLogQueryImpl query, final Page page) {
        this.configureQuery(query);
        return (List<HistoricJobLog>)this.getDbEntityManager().selectList("selectHistoricJobLogByQueryCriteria", query, page);
    }
    
    public long findHistoricJobLogsCountByQueryCriteria(final HistoricJobLogQueryImpl query) {
        this.configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectHistoricJobLogCountByQueryCriteria", query);
    }
    
    public void addRemovalTimeToJobLogByRootProcessInstanceId(final String rootProcessInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("rootProcessInstanceId", rootProcessInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricJobLogEventEntity.class, "updateJobLogByRootProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToJobLogByProcessInstanceId(final String processInstanceId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricJobLogEventEntity.class, "updateJobLogByProcessInstanceId", parameters);
    }
    
    public void addRemovalTimeToJobLogByBatchId(final String batchId, final Date removalTime) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("batchId", batchId);
        parameters.put("removalTime", removalTime);
        this.getDbEntityManager().updatePreserveOrder(HistoricJobLogEventEntity.class, "updateJobLogByBatchId", parameters);
        this.getDbEntityManager().updatePreserveOrder(ByteArrayEntity.class, "updateByteArraysByBatchId", parameters);
    }
    
    public void deleteHistoricJobLogById(final String id) {
        if (this.isHistoryEnabled()) {
            this.deleteExceptionByteArrayByParameterMap("id", id);
            this.getDbEntityManager().delete(HistoricJobLogEventEntity.class, "deleteHistoricJobLogById", id);
        }
    }
    
    public void deleteHistoricJobLogByJobId(final String jobId) {
        if (this.isHistoryEnabled()) {
            this.deleteExceptionByteArrayByParameterMap("jobId", jobId);
            this.getDbEntityManager().delete(HistoricJobLogEventEntity.class, "deleteHistoricJobLogByJobId", jobId);
        }
    }
    
    public void deleteHistoricJobLogsByProcessInstanceIds(final List<String> processInstanceIds) {
        this.deleteExceptionByteArrayByParameterMap("processInstanceIdIn", processInstanceIds.toArray());
        this.getDbEntityManager().deletePreserveOrder(HistoricJobLogEventEntity.class, "deleteHistoricJobLogByProcessInstanceIds", processInstanceIds);
    }
    
    public void deleteHistoricJobLogsByProcessDefinitionId(final String processDefinitionId) {
        if (this.isHistoryEnabled()) {
            this.deleteExceptionByteArrayByParameterMap("processDefinitionId", processDefinitionId);
            this.getDbEntityManager().delete(HistoricJobLogEventEntity.class, "deleteHistoricJobLogByProcessDefinitionId", processDefinitionId);
        }
    }
    
    public void deleteHistoricJobLogsByDeploymentId(final String deploymentId) {
        if (this.isHistoryEnabled()) {
            this.deleteExceptionByteArrayByParameterMap("deploymentId", deploymentId);
            this.getDbEntityManager().delete(HistoricJobLogEventEntity.class, "deleteHistoricJobLogByDeploymentId", deploymentId);
        }
    }
    
    public void deleteHistoricJobLogsByHandlerType(final String handlerType) {
        if (this.isHistoryEnabled()) {
            this.deleteExceptionByteArrayByParameterMap("handlerType", handlerType);
            this.getDbEntityManager().delete(HistoricJobLogEventEntity.class, "deleteHistoricJobLogByHandlerType", handlerType);
        }
    }
    
    public void deleteHistoricJobLogsByJobDefinitionId(final String jobDefinitionId) {
        if (this.isHistoryEnabled()) {
            this.deleteExceptionByteArrayByParameterMap("jobDefinitionId", jobDefinitionId);
            this.getDbEntityManager().delete(HistoricJobLogEventEntity.class, "deleteHistoricJobLogByJobDefinitionId", jobDefinitionId);
        }
    }
    
    public void deleteHistoricJobLogByBatchIds(final List<String> historicBatchIds) {
        if (this.isHistoryEnabled()) {
            this.deleteExceptionByteArrayByParameterMap("historicBatchIdIn", historicBatchIds);
            this.getDbEntityManager().delete(HistoricJobLogEventEntity.class, "deleteHistoricJobLogByBatchIds", historicBatchIds);
        }
    }
    
    public DbOperation deleteJobLogByRemovalTime(final Date removalTime, final int minuteFrom, final int minuteTo, final int batchSize) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("removalTime", removalTime);
        if (minuteTo - minuteFrom + 1 < 60) {
            parameters.put("minuteFrom", minuteFrom);
            parameters.put("minuteTo", minuteTo);
        }
        parameters.put("batchSize", batchSize);
        return this.getDbEntityManager().deletePreserveOrder(HistoricJobLogEventEntity.class, "deleteJobLogByRemovalTime", new ListQueryParameterObject(parameters, 0, batchSize));
    }
    
    protected void deleteExceptionByteArrayByParameterMap(final String key, final Object value) {
        EnsureUtil.ensureNotNull(key, value);
        final Map<String, Object> parameterMap = new HashMap<String, Object>();
        parameterMap.put(key, value);
        this.getDbEntityManager().delete(ByteArrayEntity.class, "deleteExceptionByteArraysByIds", parameterMap);
    }
    
    public void fireJobCreatedEvent(final Job job) {
        if (this.isHistoryEventProduced(HistoryEventTypes.JOB_CREATE, job)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricJobLogCreateEvt(job);
                }
            });
        }
    }
    
    public void fireJobFailedEvent(final Job job, final Throwable exception) {
        if (this.isHistoryEventProduced(HistoryEventTypes.JOB_FAIL, job)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricJobLogFailedEvt(job, exception);
                }
                
                @Override
                public void postHandleSingleHistoryEventCreated(final HistoryEvent event) {
                    ((JobEntity)job).setLastFailureLogId(event.getId());
                }
            });
        }
    }
    
    public void fireJobSuccessfulEvent(final Job job) {
        if (this.isHistoryEventProduced(HistoryEventTypes.JOB_SUCCESS, job)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricJobLogSuccessfulEvt(job);
                }
            });
        }
    }
    
    public void fireJobDeletedEvent(final Job job) {
        if (this.isHistoryEventProduced(HistoryEventTypes.JOB_DELETE, job)) {
            HistoryEventProcessor.processHistoryEvents(new HistoryEventProcessor.HistoryEventCreator() {
                @Override
                public HistoryEvent createHistoryEvent(final HistoryEventProducer producer) {
                    return producer.createHistoricJobLogDeleteEvt(job);
                }
            });
        }
    }
    
    protected boolean isHistoryEventProduced(final HistoryEventType eventType, final Job job) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final HistoryLevel historyLevel = configuration.getHistoryLevel();
        return historyLevel.isHistoryEventProduced(eventType, job);
    }
    
    protected void configureQuery(final HistoricJobLogQueryImpl query) {
        this.getAuthorizationManager().configureHistoricJobLogQuery(query);
        this.getTenantManager().configureQuery(query);
    }
}
