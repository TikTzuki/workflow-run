// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface HistoricIncidentQuery extends Query<HistoricIncidentQuery, HistoricIncident>
{
    HistoricIncidentQuery incidentId(final String p0);
    
    HistoricIncidentQuery incidentType(final String p0);
    
    HistoricIncidentQuery incidentMessage(final String p0);
    
    HistoricIncidentQuery incidentMessageLike(final String p0);
    
    HistoricIncidentQuery processDefinitionId(final String p0);
    
    HistoricIncidentQuery processDefinitionKey(final String p0);
    
    HistoricIncidentQuery processDefinitionKeyIn(final String... p0);
    
    HistoricIncidentQuery processInstanceId(final String p0);
    
    HistoricIncidentQuery executionId(final String p0);
    
    HistoricIncidentQuery createTimeBefore(final Date p0);
    
    HistoricIncidentQuery createTimeAfter(final Date p0);
    
    HistoricIncidentQuery endTimeBefore(final Date p0);
    
    HistoricIncidentQuery endTimeAfter(final Date p0);
    
    HistoricIncidentQuery activityId(final String p0);
    
    HistoricIncidentQuery failedActivityId(final String p0);
    
    HistoricIncidentQuery causeIncidentId(final String p0);
    
    HistoricIncidentQuery rootCauseIncidentId(final String p0);
    
    HistoricIncidentQuery tenantIdIn(final String... p0);
    
    HistoricIncidentQuery withoutTenantId();
    
    HistoricIncidentQuery configuration(final String p0);
    
    HistoricIncidentQuery historyConfiguration(final String p0);
    
    HistoricIncidentQuery jobDefinitionIdIn(final String... p0);
    
    HistoricIncidentQuery open();
    
    HistoricIncidentQuery resolved();
    
    HistoricIncidentQuery deleted();
    
    HistoricIncidentQuery orderByIncidentId();
    
    HistoricIncidentQuery orderByIncidentMessage();
    
    HistoricIncidentQuery orderByCreateTime();
    
    HistoricIncidentQuery orderByEndTime();
    
    HistoricIncidentQuery orderByIncidentType();
    
    HistoricIncidentQuery orderByExecutionId();
    
    HistoricIncidentQuery orderByActivityId();
    
    HistoricIncidentQuery orderByProcessInstanceId();
    
    HistoricIncidentQuery orderByProcessDefinitionId();
    
    HistoricIncidentQuery orderByProcessDefinitionKey();
    
    HistoricIncidentQuery orderByCauseIncidentId();
    
    HistoricIncidentQuery orderByRootCauseIncidentId();
    
    HistoricIncidentQuery orderByConfiguration();
    
    HistoricIncidentQuery orderByHistoryConfiguration();
    
    HistoricIncidentQuery orderByIncidentState();
    
    HistoricIncidentQuery orderByTenantId();
}
