// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface IncidentQuery extends Query<IncidentQuery, Incident>
{
    IncidentQuery incidentId(final String p0);
    
    IncidentQuery incidentType(final String p0);
    
    IncidentQuery incidentMessage(final String p0);
    
    IncidentQuery incidentMessageLike(final String p0);
    
    IncidentQuery processDefinitionId(final String p0);
    
    IncidentQuery processDefinitionKeyIn(final String... p0);
    
    IncidentQuery processInstanceId(final String p0);
    
    IncidentQuery executionId(final String p0);
    
    IncidentQuery incidentTimestampBefore(final Date p0);
    
    IncidentQuery incidentTimestampAfter(final Date p0);
    
    IncidentQuery activityId(final String p0);
    
    IncidentQuery failedActivityId(final String p0);
    
    IncidentQuery causeIncidentId(final String p0);
    
    IncidentQuery rootCauseIncidentId(final String p0);
    
    IncidentQuery configuration(final String p0);
    
    IncidentQuery tenantIdIn(final String... p0);
    
    IncidentQuery jobDefinitionIdIn(final String... p0);
    
    IncidentQuery orderByIncidentId();
    
    IncidentQuery orderByIncidentTimestamp();
    
    IncidentQuery orderByIncidentMessage();
    
    IncidentQuery orderByIncidentType();
    
    IncidentQuery orderByExecutionId();
    
    IncidentQuery orderByActivityId();
    
    IncidentQuery orderByProcessInstanceId();
    
    IncidentQuery orderByProcessDefinitionId();
    
    IncidentQuery orderByCauseIncidentId();
    
    IncidentQuery orderByRootCauseIncidentId();
    
    IncidentQuery orderByConfiguration();
    
    IncidentQuery orderByTenantId();
}
