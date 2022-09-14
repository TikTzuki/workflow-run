// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history;

import java.util.Calendar;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchEntity;
import org.zik.bpm.engine.repository.DecisionDefinition;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInstanceEntity;
import java.util.Date;
import org.zik.bpm.engine.repository.ProcessDefinition;
import org.zik.bpm.engine.impl.history.event.HistoricProcessInstanceEventEntity;

public class DefaultHistoryRemovalTimeProvider implements HistoryRemovalTimeProvider
{
    @Override
    public Date calculateRemovalTime(final HistoricProcessInstanceEventEntity historicRootProcessInstance, final ProcessDefinition processDefinition) {
        final Integer historyTimeToLive = processDefinition.getHistoryTimeToLive();
        if (historyTimeToLive != null) {
            if (this.isProcessInstanceRunning(historicRootProcessInstance)) {
                final Date startTime = historicRootProcessInstance.getStartTime();
                return determineRemovalTime(startTime, historyTimeToLive);
            }
            if (this.isProcessInstanceEnded(historicRootProcessInstance)) {
                final Date endTime = historicRootProcessInstance.getEndTime();
                return determineRemovalTime(endTime, historyTimeToLive);
            }
        }
        return null;
    }
    
    @Override
    public Date calculateRemovalTime(final HistoricDecisionInstanceEntity historicRootDecisionInstance, final DecisionDefinition decisionDefinition) {
        final Integer historyTimeToLive = decisionDefinition.getHistoryTimeToLive();
        if (historyTimeToLive != null) {
            final Date evaluationTime = historicRootDecisionInstance.getEvaluationTime();
            return determineRemovalTime(evaluationTime, historyTimeToLive);
        }
        return null;
    }
    
    @Override
    public Date calculateRemovalTime(final HistoricBatchEntity historicBatch) {
        final String batchOperation = historicBatch.getType();
        if (batchOperation != null) {
            final Integer historyTimeToLive = this.getTTLByBatchOperation(batchOperation);
            if (historyTimeToLive != null) {
                if (this.isBatchRunning(historicBatch)) {
                    final Date startTime = historicBatch.getStartTime();
                    return determineRemovalTime(startTime, historyTimeToLive);
                }
                if (this.isBatchEnded(historicBatch)) {
                    final Date endTime = historicBatch.getEndTime();
                    return determineRemovalTime(endTime, historyTimeToLive);
                }
            }
        }
        return null;
    }
    
    protected boolean isBatchRunning(final HistoricBatchEntity historicBatch) {
        return historicBatch.getEndTime() == null;
    }
    
    protected boolean isBatchEnded(final HistoricBatchEntity historicBatch) {
        return historicBatch.getEndTime() != null;
    }
    
    protected Integer getTTLByBatchOperation(final String batchOperation) {
        return Context.getCommandContext().getProcessEngineConfiguration().getParsedBatchOperationsForHistoryCleanup().get(batchOperation);
    }
    
    protected boolean isProcessInstanceRunning(final HistoricProcessInstanceEventEntity historicProcessInstance) {
        return historicProcessInstance.getEndTime() == null;
    }
    
    protected boolean isProcessInstanceEnded(final HistoricProcessInstanceEventEntity historicProcessInstance) {
        return historicProcessInstance.getEndTime() != null;
    }
    
    public static Date determineRemovalTime(final Date initTime, final Integer timeToLive) {
        final Calendar removalTime = Calendar.getInstance();
        removalTime.setTime(initTime);
        removalTime.add(5, timeToLive);
        return removalTime.getTime();
    }
}
