// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface HistoricActivityStatisticsQuery extends Query<HistoricActivityStatisticsQuery, HistoricActivityStatistics>
{
    HistoricActivityStatisticsQuery includeFinished();
    
    HistoricActivityStatisticsQuery includeCanceled();
    
    HistoricActivityStatisticsQuery includeCompleteScope();
    
    HistoricActivityStatisticsQuery includeIncidents();
    
    HistoricActivityStatisticsQuery startedBefore(final Date p0);
    
    HistoricActivityStatisticsQuery startedAfter(final Date p0);
    
    HistoricActivityStatisticsQuery finishedBefore(final Date p0);
    
    HistoricActivityStatisticsQuery finishedAfter(final Date p0);
    
    HistoricActivityStatisticsQuery processInstanceIdIn(final String... p0);
    
    HistoricActivityStatisticsQuery orderByActivityId();
}
