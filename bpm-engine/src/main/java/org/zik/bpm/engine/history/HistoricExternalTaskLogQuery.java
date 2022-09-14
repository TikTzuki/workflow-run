// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.query.Query;

public interface HistoricExternalTaskLogQuery extends Query<HistoricExternalTaskLogQuery, HistoricExternalTaskLog>
{
    HistoricExternalTaskLogQuery logId(final String p0);
    
    HistoricExternalTaskLogQuery externalTaskId(final String p0);
    
    HistoricExternalTaskLogQuery topicName(final String p0);
    
    HistoricExternalTaskLogQuery workerId(final String p0);
    
    HistoricExternalTaskLogQuery errorMessage(final String p0);
    
    HistoricExternalTaskLogQuery activityIdIn(final String... p0);
    
    HistoricExternalTaskLogQuery activityInstanceIdIn(final String... p0);
    
    HistoricExternalTaskLogQuery executionIdIn(final String... p0);
    
    HistoricExternalTaskLogQuery processInstanceId(final String p0);
    
    HistoricExternalTaskLogQuery processDefinitionId(final String p0);
    
    HistoricExternalTaskLogQuery processDefinitionKey(final String p0);
    
    HistoricExternalTaskLogQuery tenantIdIn(final String... p0);
    
    HistoricExternalTaskLogQuery withoutTenantId();
    
    HistoricExternalTaskLogQuery priorityHigherThanOrEquals(final long p0);
    
    HistoricExternalTaskLogQuery priorityLowerThanOrEquals(final long p0);
    
    HistoricExternalTaskLogQuery creationLog();
    
    HistoricExternalTaskLogQuery failureLog();
    
    HistoricExternalTaskLogQuery successLog();
    
    HistoricExternalTaskLogQuery deletionLog();
    
    HistoricExternalTaskLogQuery orderByTimestamp();
    
    HistoricExternalTaskLogQuery orderByExternalTaskId();
    
    HistoricExternalTaskLogQuery orderByRetries();
    
    HistoricExternalTaskLogQuery orderByPriority();
    
    HistoricExternalTaskLogQuery orderByTopicName();
    
    HistoricExternalTaskLogQuery orderByWorkerId();
    
    HistoricExternalTaskLogQuery orderByActivityId();
    
    HistoricExternalTaskLogQuery orderByActivityInstanceId();
    
    HistoricExternalTaskLogQuery orderByExecutionId();
    
    HistoricExternalTaskLogQuery orderByProcessInstanceId();
    
    HistoricExternalTaskLogQuery orderByProcessDefinitionId();
    
    HistoricExternalTaskLogQuery orderByProcessDefinitionKey();
    
    HistoricExternalTaskLogQuery orderByTenantId();
}
