// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.batch;

import org.zik.bpm.engine.query.Query;

public interface BatchStatisticsQuery extends Query<BatchStatisticsQuery, BatchStatistics>
{
    BatchStatisticsQuery batchId(final String p0);
    
    BatchStatisticsQuery type(final String p0);
    
    BatchStatisticsQuery tenantIdIn(final String... p0);
    
    BatchStatisticsQuery withoutTenantId();
    
    BatchStatisticsQuery active();
    
    BatchStatisticsQuery suspended();
    
    BatchStatisticsQuery orderById();
    
    BatchStatisticsQuery orderByTenantId();
}
