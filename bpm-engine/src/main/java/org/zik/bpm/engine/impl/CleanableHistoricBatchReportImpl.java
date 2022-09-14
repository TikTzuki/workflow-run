// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.cfg.CommandChecker;
import java.util.Iterator;
import java.util.Set;
import org.zik.bpm.engine.impl.batch.BatchJobHandler;
import org.zik.bpm.engine.impl.context.Context;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import org.zik.bpm.engine.history.CleanableHistoricBatchReportResult;
import org.zik.bpm.engine.history.CleanableHistoricBatchReport;

public class CleanableHistoricBatchReportImpl extends AbstractQuery<CleanableHistoricBatchReport, CleanableHistoricBatchReportResult> implements CleanableHistoricBatchReport
{
    private static final long serialVersionUID = 1L;
    protected Date currentTimestamp;
    protected boolean isHistoryCleanupStrategyRemovalTimeBased;
    
    public CleanableHistoricBatchReportImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public CleanableHistoricBatchReport orderByFinishedBatchOperation() {
        this.orderBy(CleanableHistoricInstanceReportProperty.FINISHED_AMOUNT);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.provideHistoryCleanupStrategy(commandContext);
        this.checkQueryOk();
        this.checkPermissions(commandContext);
        final Map<String, Integer> batchOperationsForHistoryCleanup = commandContext.getProcessEngineConfiguration().getParsedBatchOperationsForHistoryCleanup();
        if (this.isHistoryCleanupStrategyRemovalTimeBased()) {
            this.addBatchOperationsWithoutTTL(batchOperationsForHistoryCleanup);
        }
        return commandContext.getHistoricBatchManager().findCleanableHistoricBatchesReportCountByCriteria(this, batchOperationsForHistoryCleanup);
    }
    
    @Override
    public List<CleanableHistoricBatchReportResult> executeList(final CommandContext commandContext, final Page page) {
        this.provideHistoryCleanupStrategy(commandContext);
        this.checkQueryOk();
        this.checkPermissions(commandContext);
        final Map<String, Integer> batchOperationsForHistoryCleanup = commandContext.getProcessEngineConfiguration().getParsedBatchOperationsForHistoryCleanup();
        if (this.isHistoryCleanupStrategyRemovalTimeBased()) {
            this.addBatchOperationsWithoutTTL(batchOperationsForHistoryCleanup);
        }
        return commandContext.getHistoricBatchManager().findCleanableHistoricBatchesReportByCriteria(this, page, batchOperationsForHistoryCleanup);
    }
    
    protected void addBatchOperationsWithoutTTL(final Map<String, Integer> batchOperations) {
        final Map<String, BatchJobHandler<?>> batchJobHandlers = Context.getProcessEngineConfiguration().getBatchHandlers();
        Set<String> batchOperationKeys = null;
        if (batchJobHandlers != null) {
            batchOperationKeys = batchJobHandlers.keySet();
        }
        if (batchOperationKeys != null) {
            for (final String batchOperation : batchOperationKeys) {
                final Integer ttl = batchOperations.get(batchOperation);
                batchOperations.put(batchOperation, ttl);
            }
        }
    }
    
    public Date getCurrentTimestamp() {
        return this.currentTimestamp;
    }
    
    public void setCurrentTimestamp(final Date currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }
    
    private void checkPermissions(final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkReadHistoricBatch();
        }
    }
    
    protected void provideHistoryCleanupStrategy(final CommandContext commandContext) {
        final String historyCleanupStrategy = commandContext.getProcessEngineConfiguration().getHistoryCleanupStrategy();
        this.isHistoryCleanupStrategyRemovalTimeBased = "removalTimeBased".equals(historyCleanupStrategy);
    }
    
    public boolean isHistoryCleanupStrategyRemovalTimeBased() {
        return this.isHistoryCleanupStrategyRemovalTimeBased;
    }
}
