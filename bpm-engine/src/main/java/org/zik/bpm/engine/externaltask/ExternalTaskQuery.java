// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.externaltask;

import java.util.Date;
import java.util.Set;
import org.zik.bpm.engine.query.Query;

public interface ExternalTaskQuery extends Query<ExternalTaskQuery, ExternalTask>
{
    ExternalTaskQuery externalTaskId(final String p0);
    
    ExternalTaskQuery externalTaskIdIn(final Set<String> p0);
    
    ExternalTaskQuery workerId(final String p0);
    
    ExternalTaskQuery lockExpirationBefore(final Date p0);
    
    ExternalTaskQuery lockExpirationAfter(final Date p0);
    
    ExternalTaskQuery topicName(final String p0);
    
    ExternalTaskQuery locked();
    
    ExternalTaskQuery notLocked();
    
    ExternalTaskQuery executionId(final String p0);
    
    ExternalTaskQuery processInstanceId(final String p0);
    
    ExternalTaskQuery processInstanceIdIn(final String... p0);
    
    ExternalTaskQuery processDefinitionId(final String p0);
    
    ExternalTaskQuery activityId(final String p0);
    
    ExternalTaskQuery activityIdIn(final String... p0);
    
    ExternalTaskQuery priorityHigherThanOrEquals(final long p0);
    
    ExternalTaskQuery priorityLowerThanOrEquals(final long p0);
    
    ExternalTaskQuery suspended();
    
    ExternalTaskQuery active();
    
    ExternalTaskQuery withRetriesLeft();
    
    ExternalTaskQuery noRetriesLeft();
    
    ExternalTaskQuery tenantIdIn(final String... p0);
    
    ExternalTaskQuery orderById();
    
    ExternalTaskQuery orderByLockExpirationTime();
    
    ExternalTaskQuery orderByProcessInstanceId();
    
    ExternalTaskQuery orderByProcessDefinitionId();
    
    ExternalTaskQuery orderByProcessDefinitionKey();
    
    ExternalTaskQuery orderByTenantId();
    
    ExternalTaskQuery orderByPriority();
}
