// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.batch.history;

import org.zik.bpm.engine.query.Query;

public interface HistoricBatchQuery extends Query<HistoricBatchQuery, HistoricBatch>
{
    HistoricBatchQuery batchId(final String p0);
    
    HistoricBatchQuery type(final String p0);
    
    HistoricBatchQuery completed(final boolean p0);
    
    HistoricBatchQuery tenantIdIn(final String... p0);
    
    HistoricBatchQuery withoutTenantId();
    
    HistoricBatchQuery orderById();
    
    HistoricBatchQuery orderByStartTime();
    
    HistoricBatchQuery orderByEndTime();
    
    HistoricBatchQuery orderByTenantId();
}
