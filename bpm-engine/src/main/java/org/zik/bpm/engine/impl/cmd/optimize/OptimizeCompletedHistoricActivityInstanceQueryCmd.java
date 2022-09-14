// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.optimize;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricActivityInstance;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class OptimizeCompletedHistoricActivityInstanceQueryCmd implements Command<List<HistoricActivityInstance>>
{
    protected Date finishedAfter;
    protected Date finishedAt;
    protected int maxResults;
    
    public OptimizeCompletedHistoricActivityInstanceQueryCmd(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        this.finishedAfter = finishedAfter;
        this.finishedAt = finishedAt;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<HistoricActivityInstance> execute(final CommandContext commandContext) {
        return commandContext.getOptimizeManager().getCompletedHistoricActivityInstances(this.finishedAfter, this.finishedAt, this.maxResults);
    }
}
