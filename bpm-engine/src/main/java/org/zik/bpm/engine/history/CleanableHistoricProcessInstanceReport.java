// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.query.Query;

public interface CleanableHistoricProcessInstanceReport extends Query<CleanableHistoricProcessInstanceReport, CleanableHistoricProcessInstanceReportResult>
{
    CleanableHistoricProcessInstanceReport processDefinitionIdIn(final String... p0);
    
    CleanableHistoricProcessInstanceReport processDefinitionKeyIn(final String... p0);
    
    CleanableHistoricProcessInstanceReport tenantIdIn(final String... p0);
    
    CleanableHistoricProcessInstanceReport withoutTenantId();
    
    CleanableHistoricProcessInstanceReport compact();
    
    CleanableHistoricProcessInstanceReport orderByFinished();
}
