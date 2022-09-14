// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.optimize;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.impl.persistence.entity.optimize.OptimizeHistoricIdentityLinkLogEntity;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class OptimizeHistoricIdentityLinkLogQueryCmd implements Command<List<OptimizeHistoricIdentityLinkLogEntity>>
{
    protected Date occurredAfter;
    protected Date occurredAt;
    protected int maxResults;
    
    public OptimizeHistoricIdentityLinkLogQueryCmd(final Date occurredAfter, final Date occurredAt, final int maxResults) {
        this.occurredAfter = occurredAfter;
        this.occurredAt = occurredAt;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<OptimizeHistoricIdentityLinkLogEntity> execute(final CommandContext commandContext) {
        return commandContext.getOptimizeManager().getHistoricIdentityLinkLogs(this.occurredAfter, this.occurredAt, this.maxResults);
    }
}
