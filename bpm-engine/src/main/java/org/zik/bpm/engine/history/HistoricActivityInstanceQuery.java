// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface HistoricActivityInstanceQuery extends Query<HistoricActivityInstanceQuery, HistoricActivityInstance>
{
    HistoricActivityInstanceQuery activityInstanceId(final String p0);
    
    HistoricActivityInstanceQuery processInstanceId(final String p0);
    
    HistoricActivityInstanceQuery processDefinitionId(final String p0);
    
    HistoricActivityInstanceQuery executionId(final String p0);
    
    HistoricActivityInstanceQuery activityId(final String p0);
    
    HistoricActivityInstanceQuery activityName(final String p0);
    
    HistoricActivityInstanceQuery activityNameLike(final String p0);
    
    HistoricActivityInstanceQuery activityType(final String p0);
    
    HistoricActivityInstanceQuery taskAssignee(final String p0);
    
    HistoricActivityInstanceQuery finished();
    
    HistoricActivityInstanceQuery unfinished();
    
    HistoricActivityInstanceQuery completeScope();
    
    HistoricActivityInstanceQuery canceled();
    
    HistoricActivityInstanceQuery startedBefore(final Date p0);
    
    HistoricActivityInstanceQuery startedAfter(final Date p0);
    
    HistoricActivityInstanceQuery finishedBefore(final Date p0);
    
    HistoricActivityInstanceQuery finishedAfter(final Date p0);
    
    HistoricActivityInstanceQuery orderByHistoricActivityInstanceId();
    
    HistoricActivityInstanceQuery orderByProcessInstanceId();
    
    HistoricActivityInstanceQuery orderByExecutionId();
    
    HistoricActivityInstanceQuery orderByActivityId();
    
    HistoricActivityInstanceQuery orderByActivityName();
    
    HistoricActivityInstanceQuery orderByActivityType();
    
    HistoricActivityInstanceQuery orderByHistoricActivityInstanceStartTime();
    
    HistoricActivityInstanceQuery orderByHistoricActivityInstanceEndTime();
    
    HistoricActivityInstanceQuery orderByHistoricActivityInstanceDuration();
    
    HistoricActivityInstanceQuery orderByProcessDefinitionId();
    
    HistoricActivityInstanceQuery orderPartiallyByOccurrence();
    
    HistoricActivityInstanceQuery tenantIdIn(final String... p0);
    
    HistoricActivityInstanceQuery withoutTenantId();
    
    HistoricActivityInstanceQuery orderByTenantId();
}
