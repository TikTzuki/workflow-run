// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface HistoricCaseActivityInstanceQuery extends Query<HistoricCaseActivityInstanceQuery, HistoricCaseActivityInstance>
{
    HistoricCaseActivityInstanceQuery caseActivityInstanceId(final String p0);
    
    HistoricCaseActivityInstanceQuery caseActivityInstanceIdIn(final String... p0);
    
    HistoricCaseActivityInstanceQuery caseExecutionId(final String p0);
    
    HistoricCaseActivityInstanceQuery caseInstanceId(final String p0);
    
    HistoricCaseActivityInstanceQuery caseDefinitionId(final String p0);
    
    HistoricCaseActivityInstanceQuery caseActivityId(final String p0);
    
    HistoricCaseActivityInstanceQuery caseActivityIdIn(final String... p0);
    
    HistoricCaseActivityInstanceQuery caseActivityName(final String p0);
    
    HistoricCaseActivityInstanceQuery caseActivityType(final String p0);
    
    HistoricCaseActivityInstanceQuery createdBefore(final Date p0);
    
    HistoricCaseActivityInstanceQuery createdAfter(final Date p0);
    
    HistoricCaseActivityInstanceQuery endedBefore(final Date p0);
    
    HistoricCaseActivityInstanceQuery endedAfter(final Date p0);
    
    HistoricCaseActivityInstanceQuery required();
    
    HistoricCaseActivityInstanceQuery ended();
    
    HistoricCaseActivityInstanceQuery notEnded();
    
    HistoricCaseActivityInstanceQuery available();
    
    HistoricCaseActivityInstanceQuery enabled();
    
    HistoricCaseActivityInstanceQuery disabled();
    
    HistoricCaseActivityInstanceQuery active();
    
    HistoricCaseActivityInstanceQuery completed();
    
    HistoricCaseActivityInstanceQuery terminated();
    
    HistoricCaseActivityInstanceQuery tenantIdIn(final String... p0);
    
    HistoricCaseActivityInstanceQuery withoutTenantId();
    
    HistoricCaseActivityInstanceQuery orderByHistoricCaseActivityInstanceId();
    
    HistoricCaseActivityInstanceQuery orderByCaseInstanceId();
    
    HistoricCaseActivityInstanceQuery orderByCaseExecutionId();
    
    HistoricCaseActivityInstanceQuery orderByCaseActivityId();
    
    HistoricCaseActivityInstanceQuery orderByCaseActivityName();
    
    HistoricCaseActivityInstanceQuery orderByCaseActivityType();
    
    HistoricCaseActivityInstanceQuery orderByHistoricCaseActivityInstanceCreateTime();
    
    HistoricCaseActivityInstanceQuery orderByHistoricCaseActivityInstanceEndTime();
    
    HistoricCaseActivityInstanceQuery orderByHistoricCaseActivityInstanceDuration();
    
    HistoricCaseActivityInstanceQuery orderByCaseDefinitionId();
    
    HistoricCaseActivityInstanceQuery orderByTenantId();
}
