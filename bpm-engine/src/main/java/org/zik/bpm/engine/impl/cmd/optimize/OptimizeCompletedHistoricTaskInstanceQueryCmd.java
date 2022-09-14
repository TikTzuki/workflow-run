// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.optimize;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricTaskInstance;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class OptimizeCompletedHistoricTaskInstanceQueryCmd implements Command<List<HistoricTaskInstance>>
{
    protected Date finishedAfter;
    protected Date finishedAt;
    protected int maxResults;
    
    public OptimizeCompletedHistoricTaskInstanceQueryCmd(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        this.finishedAfter = finishedAfter;
        this.finishedAt = finishedAt;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<HistoricTaskInstance> execute(final CommandContext commandContext) {
        return commandContext.getOptimizeManager().getCompletedHistoricTaskInstances(this.finishedAfter, this.finishedAt, this.maxResults);
    }
}
