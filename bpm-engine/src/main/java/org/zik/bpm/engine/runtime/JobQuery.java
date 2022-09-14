// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.Date;
import java.util.Set;
import org.zik.bpm.engine.query.Query;

public interface JobQuery extends Query<JobQuery, Job>
{
    JobQuery jobId(final String p0);
    
    JobQuery jobIds(final Set<String> p0);
    
    JobQuery jobDefinitionId(final String p0);
    
    JobQuery processInstanceId(final String p0);
    
    JobQuery processInstanceIds(final Set<String> p0);
    
    JobQuery processDefinitionId(final String p0);
    
    JobQuery processDefinitionKey(final String p0);
    
    JobQuery executionId(final String p0);
    
    JobQuery activityId(final String p0);
    
    JobQuery withRetriesLeft();
    
    JobQuery executable();
    
    JobQuery timers();
    
    JobQuery messages();
    
    JobQuery duedateLowerThan(final Date p0);
    
    JobQuery duedateHigherThan(final Date p0);
    
    @Deprecated
    JobQuery duedateLowerThen(final Date p0);
    
    @Deprecated
    JobQuery duedateLowerThenOrEquals(final Date p0);
    
    @Deprecated
    JobQuery duedateHigherThen(final Date p0);
    
    @Deprecated
    JobQuery duedateHigherThenOrEquals(final Date p0);
    
    JobQuery createdBefore(final Date p0);
    
    JobQuery createdAfter(final Date p0);
    
    JobQuery priorityHigherThanOrEquals(final long p0);
    
    JobQuery priorityLowerThanOrEquals(final long p0);
    
    JobQuery withException();
    
    JobQuery exceptionMessage(final String p0);
    
    JobQuery failedActivityId(final String p0);
    
    JobQuery noRetriesLeft();
    
    JobQuery active();
    
    JobQuery suspended();
    
    JobQuery tenantIdIn(final String... p0);
    
    JobQuery withoutTenantId();
    
    JobQuery includeJobsWithoutTenantId();
    
    JobQuery orderByJobId();
    
    JobQuery orderByJobDuedate();
    
    JobQuery orderByJobRetries();
    
    JobQuery orderByJobPriority();
    
    JobQuery orderByProcessInstanceId();
    
    JobQuery orderByProcessDefinitionId();
    
    JobQuery orderByProcessDefinitionKey();
    
    JobQuery orderByExecutionId();
    
    JobQuery orderByTenantId();
}
