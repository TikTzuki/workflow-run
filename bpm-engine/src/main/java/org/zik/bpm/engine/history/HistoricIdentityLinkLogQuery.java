// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface HistoricIdentityLinkLogQuery extends Query<HistoricIdentityLinkLogQuery, HistoricIdentityLinkLog>
{
    HistoricIdentityLinkLogQuery dateBefore(final Date p0);
    
    HistoricIdentityLinkLogQuery dateAfter(final Date p0);
    
    HistoricIdentityLinkLogQuery type(final String p0);
    
    HistoricIdentityLinkLogQuery userId(final String p0);
    
    HistoricIdentityLinkLogQuery groupId(final String p0);
    
    HistoricIdentityLinkLogQuery taskId(final String p0);
    
    HistoricIdentityLinkLogQuery processDefinitionId(final String p0);
    
    HistoricIdentityLinkLogQuery processDefinitionKey(final String p0);
    
    HistoricIdentityLinkLogQuery operationType(final String p0);
    
    HistoricIdentityLinkLogQuery assignerId(final String p0);
    
    HistoricIdentityLinkLogQuery tenantIdIn(final String... p0);
    
    HistoricIdentityLinkLogQuery withoutTenantId();
    
    HistoricIdentityLinkLogQuery orderByTime();
    
    HistoricIdentityLinkLogQuery orderByType();
    
    HistoricIdentityLinkLogQuery orderByUserId();
    
    HistoricIdentityLinkLogQuery orderByGroupId();
    
    HistoricIdentityLinkLogQuery orderByTaskId();
    
    HistoricIdentityLinkLogQuery orderByProcessDefinitionId();
    
    HistoricIdentityLinkLogQuery orderByProcessDefinitionKey();
    
    HistoricIdentityLinkLogQuery orderByOperationType();
    
    HistoricIdentityLinkLogQuery orderByAssignerId();
    
    HistoricIdentityLinkLogQuery orderByTenantId();
}
