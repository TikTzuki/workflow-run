// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor.historycleanup;

import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cfg.TransactionListener;

public abstract class HistoryCleanupHandler implements TransactionListener
{
    public static final int MAX_BATCH_SIZE = 500;
    protected HistoryCleanupJobHandlerConfiguration configuration;
    protected String jobId;
    protected CommandExecutor commandExecutor;
    
    @Override
    public void execute(final CommandContext commandContext) {
        this.commandExecutor.execute((Command<Object>)new HistoryCleanupHandlerCmd());
    }
    
    abstract void performCleanup();
    
    abstract Map<String, Long> reportMetrics();
    
    abstract boolean shouldRescheduleNow();
    
    public HistoryCleanupJobHandlerConfiguration getConfiguration() {
        return this.configuration;
    }
    
    public HistoryCleanupHandler setConfiguration(final HistoryCleanupJobHandlerConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }
    
    public HistoryCleanupHandler setJobId(final String jobId) {
        this.jobId = jobId;
        return this;
    }
    
    public HistoryCleanupHandler setCommandExecutor(final CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        return this;
    }
    
    protected class HistoryCleanupHandlerCmd implements Command<Void>
    {
        @Override
        public Void execute(final CommandContext commandContext) {
            final Map<String, Long> report = HistoryCleanupHandler.this.reportMetrics();
            final boolean isRescheduleNow = HistoryCleanupHandler.this.shouldRescheduleNow();
            new HistoryCleanupSchedulerCmd(isRescheduleNow, report, HistoryCleanupHandler.this.configuration, HistoryCleanupHandler.this.jobId).execute(commandContext);
            return null;
        }
    }
}
