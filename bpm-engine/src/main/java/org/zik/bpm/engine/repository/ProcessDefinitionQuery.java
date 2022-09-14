// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import org.zik.bpm.engine.impl.ProcessDefinitionQueryImpl;
import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface ProcessDefinitionQuery extends Query<ProcessDefinitionQuery, ProcessDefinition>
{
    ProcessDefinitionQuery processDefinitionId(final String p0);
    
    ProcessDefinitionQuery processDefinitionIdIn(final String... p0);
    
    ProcessDefinitionQuery processDefinitionCategory(final String p0);
    
    ProcessDefinitionQuery processDefinitionCategoryLike(final String p0);
    
    ProcessDefinitionQuery processDefinitionName(final String p0);
    
    ProcessDefinitionQuery processDefinitionNameLike(final String p0);
    
    ProcessDefinitionQuery deploymentId(final String p0);
    
    ProcessDefinitionQuery deployedAfter(final Date p0);
    
    ProcessDefinitionQuery deployedAt(final Date p0);
    
    ProcessDefinitionQuery processDefinitionKey(final String p0);
    
    ProcessDefinitionQueryImpl processDefinitionKeysIn(final String... p0);
    
    ProcessDefinitionQuery processDefinitionKeyLike(final String p0);
    
    ProcessDefinitionQuery processDefinitionVersion(final Integer p0);
    
    ProcessDefinitionQuery latestVersion();
    
    ProcessDefinitionQuery processDefinitionResourceName(final String p0);
    
    ProcessDefinitionQuery processDefinitionResourceNameLike(final String p0);
    
    ProcessDefinitionQuery startableByUser(final String p0);
    
    ProcessDefinitionQuery suspended();
    
    ProcessDefinitionQuery active();
    
    ProcessDefinitionQuery incidentType(final String p0);
    
    ProcessDefinitionQuery incidentId(final String p0);
    
    ProcessDefinitionQuery incidentMessage(final String p0);
    
    ProcessDefinitionQuery incidentMessageLike(final String p0);
    
    ProcessDefinitionQuery versionTag(final String p0);
    
    ProcessDefinitionQuery versionTagLike(final String p0);
    
    ProcessDefinitionQuery withoutVersionTag();
    
    @Deprecated
    ProcessDefinitionQuery messageEventSubscription(final String p0);
    
    ProcessDefinitionQuery messageEventSubscriptionName(final String p0);
    
    ProcessDefinitionQuery tenantIdIn(final String... p0);
    
    ProcessDefinitionQuery withoutTenantId();
    
    ProcessDefinitionQuery includeProcessDefinitionsWithoutTenantId();
    
    ProcessDefinitionQuery startableInTasklist();
    
    ProcessDefinitionQuery notStartableInTasklist();
    
    ProcessDefinitionQuery startablePermissionCheck();
    
    ProcessDefinitionQuery orderByProcessDefinitionCategory();
    
    ProcessDefinitionQuery orderByProcessDefinitionKey();
    
    ProcessDefinitionQuery orderByProcessDefinitionId();
    
    ProcessDefinitionQuery orderByProcessDefinitionVersion();
    
    ProcessDefinitionQuery orderByProcessDefinitionName();
    
    ProcessDefinitionQuery orderByDeploymentId();
    
    ProcessDefinitionQuery orderByDeploymentTime();
    
    ProcessDefinitionQuery orderByTenantId();
    
    ProcessDefinitionQuery orderByVersionTag();
}
