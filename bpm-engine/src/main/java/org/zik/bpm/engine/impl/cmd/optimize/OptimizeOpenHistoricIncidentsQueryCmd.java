// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd.optimize;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.Date;
import org.zik.bpm.engine.impl.persistence.entity.HistoricIncidentEntity;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.Command;

public class OptimizeOpenHistoricIncidentsQueryCmd implements Command<List<HistoricIncidentEntity>>
{
    protected Date createdAfter;
    protected Date createdAt;
    protected int maxResults;
    
    public OptimizeOpenHistoricIncidentsQueryCmd(final Date createdAfter, final Date createdAt, final int maxResults) {
        this.createdAfter = createdAfter;
        this.createdAt = createdAt;
        this.maxResults = maxResults;
    }
    
    @Override
    public List<HistoricIncidentEntity> execute(final CommandContext commandContext) {
        return commandContext.getOptimizeManager().getOpenHistoricIncidents(this.createdAfter, this.createdAt, this.maxResults);
    }
}
