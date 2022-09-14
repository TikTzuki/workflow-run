// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.optimize;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class OptimizeHistoricUserOperationsLogQueryCmd implements Command<List<UserOperationLogEntry>>
{
    protected Date occurredAfter;
    protected Date occurredAt;
    protected int maxResults;
    
    public OptimizeHistoricUserOperationsLogQueryCmd(final Date occurredAfter, final Date occurredAt, final int maxResults) {
        this.occurredAfter = occurredAfter;
        this.occurredAt = occurredAt;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<UserOperationLogEntry> execute(final CommandContext commandContext) {
        return commandContext.getOptimizeManager().getHistoricUserOperationLogs(this.occurredAfter, this.occurredAt, this.maxResults);
    }
}
