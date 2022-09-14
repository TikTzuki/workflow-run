// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.query.Query;

public interface CleanableHistoricDecisionInstanceReport extends Query<CleanableHistoricDecisionInstanceReport, CleanableHistoricDecisionInstanceReportResult>
{
    CleanableHistoricDecisionInstanceReport decisionDefinitionIdIn(final String... p0);
    
    CleanableHistoricDecisionInstanceReport decisionDefinitionKeyIn(final String... p0);
    
    CleanableHistoricDecisionInstanceReport tenantIdIn(final String... p0);
    
    CleanableHistoricDecisionInstanceReport withoutTenantId();
    
    CleanableHistoricDecisionInstanceReport compact();
    
    CleanableHistoricDecisionInstanceReport orderByFinished();
}
