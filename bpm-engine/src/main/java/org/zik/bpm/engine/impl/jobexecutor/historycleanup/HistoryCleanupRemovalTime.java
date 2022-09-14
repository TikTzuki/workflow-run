// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.TaskMeterLogEntity;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInstanceEntity;
import org.zik.bpm.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.context.Context;
import java.util.HashMap;
import org.zik.bpm.engine.impl.db.entitymanager.operation.DbOperation;
import org.zik.bpm.engine.impl.db.DbEntity;
import java.util.Map;

public class HistoryCleanupRemovalTime extends HistoryCleanupHandler
{
    protected Map<Class<? extends DbEntity>, DbOperation> deleteOperations;
    
    public HistoryCleanupRemovalTime() {
        this.deleteOperations = new HashMap<Class<? extends DbEntity>, DbOperation>();
    }
    
    public void performCleanup() {
        this.deleteOperations.putAll(this.performProcessCleanup());
        if (this.isDmnEnabled()) {
            this.deleteOperations.putAll(this.performDmnCleanup());
        }
        final DbOperation batchCleanup = this.performBatchCleanup();
        this.deleteOperations.put(batchCleanup.getEntityType(), batchCleanup);
        if (this.getTaskMetricsTimeToLive() != null) {
            final DbOperation taskMetricsCleanup = this.performTaskMetricsCleanup();
            this.deleteOperations.put(taskMetricsCleanup.getEntityType(), taskMetricsCleanup);
        }
    }
    
    protected Map<Class<? extends DbEntity>, DbOperation> performDmnCleanup() {
        return Context.getCommandContext().getHistoricDecisionInstanceManager().deleteHistoricDecisionsByRemovalTime(ClockUtil.getCurrentTime(), this.configuration.getMinuteFrom(), this.configuration.getMinuteTo(), this.getBatchSize());
    }
    
    protected Map<Class<? extends DbEntity>, DbOperation> performProcessCleanup() {
        return Context.getCommandContext().getHistoricProcessInstanceManager().deleteHistoricProcessInstancesByRemovalTime(ClockUtil.getCurrentTime(), this.configuration.getMinuteFrom(), this.configuration.getMinuteTo(), this.getBatchSize());
    }
    
    protected DbOperation performBatchCleanup() {
        return Context.getCommandContext().getHistoricBatchManager().deleteHistoricBatchesByRemovalTime(ClockUtil.getCurrentTime(), this.configuration.getMinuteFrom(), this.configuration.getMinuteTo(), this.getBatchSize());
    }
    
    protected DbOperation performTaskMetricsCleanup() {
        return Context.getCommandContext().getMeterLogManager().deleteTaskMetricsByRemovalTime(ClockUtil.getCurrentTime(), this.getTaskMetricsTimeToLive(), this.configuration.getMinuteFrom(), this.configuration.getMinuteTo(), this.getBatchSize());
    }
    
    protected Map<String, Long> reportMetrics() {
        final Map<String, Long> reports = new HashMap<String, Long>();
        final DbOperation deleteOperationProcessInstance = this.deleteOperations.get(HistoricProcessInstanceEntity.class);
        if (deleteOperationProcessInstance != null) {
            reports.put("history-cleanup-removed-process-instances", (long)deleteOperationProcessInstance.getRowsAffected());
        }
        final DbOperation deleteOperationDecisionInstance = this.deleteOperations.get(HistoricDecisionInstanceEntity.class);
        if (deleteOperationDecisionInstance != null) {
            reports.put("history-cleanup-removed-decision-instances", (long)deleteOperationDecisionInstance.getRowsAffected());
        }
        final DbOperation deleteOperationBatch = this.deleteOperations.get(HistoricBatchEntity.class);
        if (deleteOperationBatch != null) {
            reports.put("history-cleanup-removed-batch-operations", (long)deleteOperationBatch.getRowsAffected());
        }
        final DbOperation deleteOperationTaskMetric = this.deleteOperations.get(TaskMeterLogEntity.class);
        if (deleteOperationTaskMetric != null) {
            reports.put("history-cleanup-removed-task-metrics", (long)deleteOperationTaskMetric.getRowsAffected());
        }
        return reports;
    }
    
    protected boolean isDmnEnabled() {
        return Context.getProcessEngineConfiguration().isDmnEnabled();
    }
    
    protected Integer getTaskMetricsTimeToLive() {
        return Context.getProcessEngineConfiguration().getParsedTaskMetricsTimeToLive();
    }
    
    protected boolean shouldRescheduleNow() {
        final int batchSize = this.getBatchSize();
        for (final DbOperation deleteOperation : this.deleteOperations.values()) {
            if (deleteOperation.getRowsAffected() == batchSize) {
                return true;
            }
        }
        return false;
    }
    
    public int getBatchSize() {
        return Context.getProcessEngineConfiguration().getHistoryCleanupBatchSize();
    }
}
