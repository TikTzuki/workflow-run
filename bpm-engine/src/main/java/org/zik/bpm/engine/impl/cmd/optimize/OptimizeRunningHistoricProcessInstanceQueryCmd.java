// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.optimize;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricProcessInstance;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class OptimizeRunningHistoricProcessInstanceQueryCmd implements Command<List<HistoricProcessInstance>>
{
    protected Date startedAfter;
    protected Date startedAt;
    protected int maxResults;
    
    public OptimizeRunningHistoricProcessInstanceQueryCmd(final Date startedAfter, final Date startedAt, final int maxResults) {
        this.startedAfter = startedAfter;
        this.startedAt = startedAt;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<HistoricProcessInstance> execute(final CommandContext commandContext) {
        return commandContext.getOptimizeManager().getRunningHistoricProcessInstances(this.startedAfter, this.startedAt, this.maxResults);
    }
}
