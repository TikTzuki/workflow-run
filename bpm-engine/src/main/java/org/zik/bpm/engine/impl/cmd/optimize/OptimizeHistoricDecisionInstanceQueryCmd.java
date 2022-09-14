// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.optimize;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricDecisionInstance;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class OptimizeHistoricDecisionInstanceQueryCmd implements Command<List<HistoricDecisionInstance>>
{
    protected Date evaluatedAfter;
    protected Date evaluatedAt;
    protected int maxResults;
    
    public OptimizeHistoricDecisionInstanceQueryCmd(final Date evaluatedAfter, final Date evaluatedAt, final int maxResults) {
        this.evaluatedAfter = evaluatedAfter;
        this.evaluatedAt = evaluatedAt;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<HistoricDecisionInstance> execute(final CommandContext commandContext) {
        return commandContext.getOptimizeManager().getHistoricDecisionInstances(this.evaluatedAfter, this.evaluatedAt, this.maxResults);
    }
}
