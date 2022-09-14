// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.query.Query;

public interface HistoricJobLogQuery extends Query<HistoricJobLogQuery, HistoricJobLog>
{
    HistoricJobLogQuery logId(final String p0);
    
    HistoricJobLogQuery jobId(final String p0);
    
    HistoricJobLogQuery jobExceptionMessage(final String p0);
    
    HistoricJobLogQuery jobDefinitionId(final String p0);
    
    HistoricJobLogQuery jobDefinitionType(final String p0);
    
    HistoricJobLogQuery jobDefinitionConfiguration(final String p0);
    
    HistoricJobLogQuery activityIdIn(final String... p0);
    
    HistoricJobLogQuery failedActivityIdIn(final String... p0);
    
    HistoricJobLogQuery executionIdIn(final String... p0);
    
    HistoricJobLogQuery processInstanceId(final String p0);
    
    HistoricJobLogQuery processDefinitionId(final String p0);
    
    HistoricJobLogQuery processDefinitionKey(final String p0);
    
    HistoricJobLogQuery deploymentId(final String p0);
    
    HistoricJobLogQuery tenantIdIn(final String... p0);
    
    HistoricJobLogQuery withoutTenantId();
    
    HistoricJobLogQuery hostname(final String p0);
    
    HistoricJobLogQuery jobPriorityHigherThanOrEquals(final long p0);
    
    HistoricJobLogQuery jobPriorityLowerThanOrEquals(final long p0);
    
    HistoricJobLogQuery creationLog();
    
    HistoricJobLogQuery failureLog();
    
    HistoricJobLogQuery successLog();
    
    HistoricJobLogQuery deletionLog();
    
    HistoricJobLogQuery orderByTimestamp();
    
    HistoricJobLogQuery orderByJobId();
    
    HistoricJobLogQuery orderByJobDueDate();
    
    HistoricJobLogQuery orderByJobRetries();
    
    HistoricJobLogQuery orderByJobPriority();
    
    HistoricJobLogQuery orderByJobDefinitionId();
    
    HistoricJobLogQuery orderByActivityId();
    
    HistoricJobLogQuery orderByExecutionId();
    
    HistoricJobLogQuery orderByProcessInstanceId();
    
    HistoricJobLogQuery orderByProcessDefinitionId();
    
    HistoricJobLogQuery orderByProcessDefinitionKey();
    
    HistoricJobLogQuery orderByDeploymentId();
    
    HistoricJobLogQuery orderPartiallyByOccurrence();
    
    HistoricJobLogQuery orderByTenantId();
    
    HistoricJobLogQuery orderByHostname();
}
