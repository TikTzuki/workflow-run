// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.batch;

import org.zik.bpm.engine.query.Query;

public interface BatchQuery extends Query<BatchQuery, Batch>
{
    BatchQuery batchId(final String p0);
    
    BatchQuery type(final String p0);
    
    BatchQuery tenantIdIn(final String... p0);
    
    BatchQuery withoutTenantId();
    
    BatchQuery active();
    
    BatchQuery suspended();
    
    BatchQuery orderById();
    
    BatchQuery orderByTenantId();
}
