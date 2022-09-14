// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.query.Query;

public interface CleanableHistoricCaseInstanceReport extends Query<CleanableHistoricCaseInstanceReport, CleanableHistoricCaseInstanceReportResult>
{
    CleanableHistoricCaseInstanceReport caseDefinitionIdIn(final String... p0);
    
    CleanableHistoricCaseInstanceReport caseDefinitionKeyIn(final String... p0);
    
    CleanableHistoricCaseInstanceReport tenantIdIn(final String... p0);
    
    CleanableHistoricCaseInstanceReport withoutTenantId();
    
    CleanableHistoricCaseInstanceReport compact();
    
    CleanableHistoricCaseInstanceReport orderByFinished();
}
