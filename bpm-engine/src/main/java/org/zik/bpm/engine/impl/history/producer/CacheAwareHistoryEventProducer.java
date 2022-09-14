// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.producer;

import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.impl.history.event.HistoricIncidentEventEntity;
import org.zik.bpm.engine.runtime.Incident;
import org.zik.bpm.engine.impl.history.event.HistoricTaskInstanceEventEntity;
import org.zik.bpm.engine.delegate.DelegateTask;
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;
import org.zik.bpm.engine.impl.history.event.HistoricActivityInstanceEventEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public class CacheAwareHistoryEventProducer extends DefaultHistoryEventProducer
{
    @Override
    protected HistoricActivityInstanceEventEntity loadActivityInstanceEventEntity(final ExecutionEntity execution) {
        final String activityInstanceId = execution.getActivityInstanceId();
        final HistoricActivityInstanceEventEntity cachedEntity = this.findInCache(HistoricActivityInstanceEventEntity.class, activityInstanceId);
        if (cachedEntity != null) {
            return cachedEntity;
        }
        return this.newActivityInstanceEventEntity(execution);
    }
    
    @Override
    protected HistoricProcessInstanceEventEntity loadProcessInstanceEventEntity(final ExecutionEntity execution) {
        final String processInstanceId = execution.getProcessInstanceId();
        final HistoricProcessInstanceEventEntity cachedEntity = this.findInCache(HistoricProcessInstanceEventEntity.class, processInstanceId);
        if (cachedEntity != null) {
            return cachedEntity;
        }
        return this.newProcessInstanceEventEntity(execution);
    }
    
    @Override
    protected HistoricTaskInstanceEventEntity loadTaskInstanceEvent(final DelegateTask task) {
        final String taskId = task.getId();
        final HistoricTaskInstanceEventEntity cachedEntity = this.findInCache(HistoricTaskInstanceEventEntity.class, taskId);
        if (cachedEntity != null) {
            return cachedEntity;
        }
        return this.newTaskInstanceEventEntity(task);
    }
    
    @Override
    protected HistoricIncidentEventEntity loadIncidentEvent(final Incident incident) {
        final String incidentId = incident.getId();
        final HistoricIncidentEventEntity cachedEntity = this.findInCache(HistoricIncidentEventEntity.class, incidentId);
        if (cachedEntity != null) {
            return cachedEntity;
        }
        return this.newIncidentEventEntity(incident);
    }
    
    @Override
    protected HistoricBatchEntity loadBatchEntity(final BatchEntity batch) {
        final String batchId = batch.getId();
        final HistoricBatchEntity cachedEntity = this.findInCache(HistoricBatchEntity.class, batchId);
        if (cachedEntity != null) {
            return cachedEntity;
        }
        return this.newBatchEventEntity(batch);
    }
    
    protected <T extends HistoryEvent> T findInCache(final Class<T> type, final String id) {
        return Context.getCommandContext().getDbEntityManager().getCachedEntity(type, id);
    }
}
