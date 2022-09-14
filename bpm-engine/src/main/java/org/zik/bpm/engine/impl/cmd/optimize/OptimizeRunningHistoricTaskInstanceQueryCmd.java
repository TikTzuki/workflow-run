// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.optimize;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricTaskInstance;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class OptimizeRunningHistoricTaskInstanceQueryCmd implements Command<List<HistoricTaskInstance>>
{
    protected Date startedAfter;
    protected Date startedAt;
    protected int maxResults;
    
    public OptimizeRunningHistoricTaskInstanceQueryCmd(final Date startedAfter, final Date startedAt, final int maxResults) {
        this.startedAfter = startedAfter;
        this.startedAt = startedAt;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<HistoricTaskInstance> execute(final CommandContext commandContext) {
        return commandContext.getOptimizeManager().getRunningHistoricTaskInstances(this.startedAfter, this.startedAt, this.maxResults);
    }
}
