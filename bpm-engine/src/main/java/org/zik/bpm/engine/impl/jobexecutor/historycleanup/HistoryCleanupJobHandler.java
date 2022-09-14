// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import com.google.gson.JsonObject;
import org.zik.bpm.engine.impl.util.JsonUtil;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.jobexecutor.JobHandler;

public class HistoryCleanupJobHandler implements JobHandler<HistoryCleanupJobHandlerConfiguration>
{
    public static final String TYPE = "history-cleanup";
    
    @Override
    public void execute(final HistoryCleanupJobHandlerConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final HistoryCleanupHandler cleanupHandler = this.initCleanupHandler(configuration, commandContext);
        if (configuration.isImmediatelyDue() || this.isWithinBatchWindow(commandContext)) {
            cleanupHandler.performCleanup();
        }
        commandContext.getTransactionContext().addTransactionListener(TransactionState.COMMITTED, cleanupHandler);
    }
    
    protected HistoryCleanupHandler initCleanupHandler(final HistoryCleanupJobHandlerConfiguration configuration, final CommandContext commandContext) {
        HistoryCleanupHandler cleanupHandler = null;
        if (this.isHistoryCleanupStrategyRemovalTimeBased(commandContext)) {
            cleanupHandler = new HistoryCleanupRemovalTime();
        }
        else {
            cleanupHandler = new HistoryCleanupBatch();
        }
        final CommandExecutor commandExecutor = commandContext.getProcessEngineConfiguration().getCommandExecutorTxRequiresNew();
        final String jobId = commandContext.getCurrentJob().getId();
        return cleanupHandler.setConfiguration(configuration).setCommandExecutor(commandExecutor).setJobId(jobId);
    }
    
    protected boolean isHistoryCleanupStrategyRemovalTimeBased(final CommandContext commandContext) {
        final String historyRemovalTimeStrategy = commandContext.getProcessEngineConfiguration().getHistoryCleanupStrategy();
        return "removalTimeBased".equals(historyRemovalTimeStrategy);
    }
    
    protected boolean isWithinBatchWindow(final CommandContext commandContext) {
        return HistoryCleanupHelper.isWithinBatchWindow(ClockUtil.getCurrentTime(), commandContext.getProcessEngineConfiguration());
    }
    
    @Override
    public HistoryCleanupJobHandlerConfiguration newConfiguration(final String canonicalString) {
        final JsonObject jsonObject = JsonUtil.asObject(canonicalString);
        return HistoryCleanupJobHandlerConfiguration.fromJson(jsonObject);
    }
    
    @Override
    public String getType() {
        return "history-cleanup";
    }
    
    @Override
    public void onDelete(final HistoryCleanupJobHandlerConfiguration configuration, final JobEntity jobEntity) {
    }
}
