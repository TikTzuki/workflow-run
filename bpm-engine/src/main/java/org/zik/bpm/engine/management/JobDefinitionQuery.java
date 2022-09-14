// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.management;

import org.zik.bpm.engine.query.Query;

public interface JobDefinitionQuery extends Query<JobDefinitionQuery, JobDefinition>
{
    JobDefinitionQuery jobDefinitionId(final String p0);
    
    JobDefinitionQuery activityIdIn(final String... p0);
    
    JobDefinitionQuery processDefinitionId(final String p0);
    
    JobDefinitionQuery processDefinitionKey(final String p0);
    
    JobDefinitionQuery jobType(final String p0);
    
    JobDefinitionQuery jobConfiguration(final String p0);
    
    JobDefinitionQuery active();
    
    JobDefinitionQuery suspended();
    
    JobDefinitionQuery withOverridingJobPriority();
    
    JobDefinitionQuery tenantIdIn(final String... p0);
    
    JobDefinitionQuery withoutTenantId();
    
    JobDefinitionQuery includeJobDefinitionsWithoutTenantId();
    
    JobDefinitionQuery orderByJobDefinitionId();
    
    JobDefinitionQuery orderByActivityId();
    
    JobDefinitionQuery orderByProcessDefinitionId();
    
    JobDefinitionQuery orderByProcessDefinitionKey();
    
    JobDefinitionQuery orderByJobType();
    
    JobDefinitionQuery orderByJobConfiguration();
    
    JobDefinitionQuery orderByTenantId();
}
