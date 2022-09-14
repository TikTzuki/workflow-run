// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import org.zik.bpm.engine.query.Query;

public interface ActivityStatisticsQuery extends Query<ActivityStatisticsQuery, ActivityStatistics>
{
    ActivityStatisticsQuery includeFailedJobs();
    
    ActivityStatisticsQuery includeIncidents();
    
    ActivityStatisticsQuery includeIncidentsForType(final String p0);
}
