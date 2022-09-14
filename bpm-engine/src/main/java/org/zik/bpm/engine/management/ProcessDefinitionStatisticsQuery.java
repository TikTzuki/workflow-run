// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import org.zik.bpm.engine.query.Query;

public interface ProcessDefinitionStatisticsQuery extends Query<ProcessDefinitionStatisticsQuery, ProcessDefinitionStatistics>
{
    ProcessDefinitionStatisticsQuery includeFailedJobs();
    
    ProcessDefinitionStatisticsQuery includeIncidents();
    
    ProcessDefinitionStatisticsQuery includeRootIncidents();
    
    ProcessDefinitionStatisticsQuery includeIncidentsForType(final String p0);
}
