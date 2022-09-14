// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.optimize;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class OptimizeCompletedHistoricProcessInstanceQueryCmd implements Command<List<HistoricProcessInstance>>
{
    protected Date finishedAfter;
    protected Date finishedAt;
    protected int maxResults;
    
    public OptimizeCompletedHistoricProcessInstanceQueryCmd(final Date finishedAfter, final Date finishedAt, final int maxResults) {
        this.finishedAfter = finishedAfter;
        this.finishedAt = finishedAt;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<HistoricProcessInstance> execute(final CommandContext commandContext) {
        return commandContext.getOptimizeManager().getCompletedHistoricProcessInstances(this.finishedAfter, this.finishedAt, this.maxResults);
    }
}
