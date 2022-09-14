// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import org.zik.bpm.engine.query.Query;

public interface DeploymentStatisticsQuery extends Query<DeploymentStatisticsQuery, DeploymentStatistics>
{
    DeploymentStatisticsQuery includeFailedJobs();
    
    DeploymentStatisticsQuery includeIncidents();
    
    DeploymentStatisticsQuery includeIncidentsForType(final String p0);
}
