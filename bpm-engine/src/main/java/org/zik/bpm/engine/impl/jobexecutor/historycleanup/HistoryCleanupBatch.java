// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import java.util.HashMap;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Collections;
import java.util.List;

public class HistoryCleanupBatch extends HistoryCleanupHandler
{
    protected List<String> historicProcessInstanceIds;
    protected List<String> historicDecisionInstanceIds;
    protected List<String> historicCaseInstanceIds;
    protected List<String> historicBatchIds;
    protected List<String> taskMetricIds;
    
    public HistoryCleanupBatch() {
        this.historicProcessInstanceIds = Collections.emptyList();
        this.historicDecisionInstanceIds = Collections.emptyList();
        this.historicCaseInstanceIds = Collections.emptyList();
        this.historicBatchIds = Collections.emptyList();
        this.taskMetricIds = Collections.emptyList();
    }
    
    public List<String> getHistoricProcessInstanceIds() {
        return this.historicProcessInstanceIds;
    }
    
    public void setHistoricProcessInstanceIds(final List<String> historicProcessInstanceIds) {
        this.historicProcessInstanceIds = historicProcessInstanceIds;
    }
    
    public List<String> getHistoricDecisionInstanceIds() {
        return this.historicDecisionInstanceIds;
    }
    
    public void setHistoricDecisionInstanceIds(final List<String> historicDecisionInstanceIds) {
        this.historicDecisionInstanceIds = historicDecisionInstanceIds;
    }
    
    public List<String> getHistoricCaseInstanceIds() {
        return this.historicCaseInstanceIds;
    }
    
    public void setHistoricCaseInstanceIds(final List<String> historicCaseInstanceIds) {
        this.historicCaseInstanceIds = historicCaseInstanceIds;
    }
    
    public List<String> getHistoricBatchIds() {
        return this.historicBatchIds;
    }
    
    public void setHistoricBatchIds(final List<String> historicBatchIds) {
        this.historicBatchIds = historicBatchIds;
    }
    
    public List<String> getTaskMetricIds() {
        return this.taskMetricIds;
    }
    
    public void setTaskMetricIds(final List<String> taskMetricIds) {
        this.taskMetricIds = taskMetricIds;
    }
    
    public int size() {
        return this.historicProcessInstanceIds.size() + this.historicDecisionInstanceIds.size() + this.historicCaseInstanceIds.size() + this.historicBatchIds.size() + this.taskMetricIds.size();
    }
    
    public void performCleanup() {
        final CommandContext commandContext = Context.getCommandContext();
        HistoryCleanupHelper.prepareNextBatch(this, commandContext);
        if (this.size() > 0) {
            if (this.historicProcessInstanceIds.size() > 0) {
                commandContext.getHistoricProcessInstanceManager().deleteHistoricProcessInstanceByIds(this.historicProcessInstanceIds);
            }
            if (this.historicDecisionInstanceIds.size() > 0) {
                commandContext.getHistoricDecisionInstanceManager().deleteHistoricDecisionInstanceByIds(this.historicDecisionInstanceIds);
            }
            if (this.historicCaseInstanceIds.size() > 0) {
                commandContext.getHistoricCaseInstanceManager().deleteHistoricCaseInstancesByIds(this.historicCaseInstanceIds);
            }
            if (this.historicBatchIds.size() > 0) {
                commandContext.getHistoricBatchManager().deleteHistoricBatchesByIds(this.historicBatchIds);
            }
            if (this.taskMetricIds.size() > 0) {
                commandContext.getMeterLogManager().deleteTaskMetricsById(this.taskMetricIds);
            }
        }
    }
    
    protected Map<String, Long> reportMetrics() {
        final Map<String, Long> reports = new HashMap<String, Long>();
        if (this.historicProcessInstanceIds.size() > 0) {
            reports.put("history-cleanup-removed-process-instances", (long)this.historicProcessInstanceIds.size());
        }
        if (this.historicDecisionInstanceIds.size() > 0) {
            reports.put("history-cleanup-removed-decision-instances", (long)this.historicDecisionInstanceIds.size());
        }
        if (this.historicCaseInstanceIds.size() > 0) {
            reports.put("history-cleanup-removed-case-instances", (long)this.historicCaseInstanceIds.size());
        }
        if (this.historicBatchIds.size() > 0) {
            reports.put("history-cleanup-removed-batch-operations", (long)this.historicBatchIds.size());
        }
        if (this.taskMetricIds.size() > 0) {
            reports.put("history-cleanup-removed-task-metrics", (long)this.taskMetricIds.size());
        }
        return reports;
    }
    
    @Override
    boolean shouldRescheduleNow() {
        return this.size() >= this.getBatchSizeThreshold();
    }
    
    public Integer getBatchSizeThreshold() {
        return Context.getProcessEngineConfiguration().getHistoryCleanupBatchThreshold();
    }
}
