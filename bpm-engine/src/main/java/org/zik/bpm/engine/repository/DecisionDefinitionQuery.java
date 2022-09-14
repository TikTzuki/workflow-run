// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface DecisionDefinitionQuery extends Query<DecisionDefinitionQuery, DecisionDefinition>
{
    DecisionDefinitionQuery decisionDefinitionId(final String p0);
    
    DecisionDefinitionQuery decisionDefinitionIdIn(final String... p0);
    
    DecisionDefinitionQuery decisionDefinitionCategory(final String p0);
    
    DecisionDefinitionQuery decisionDefinitionCategoryLike(final String p0);
    
    DecisionDefinitionQuery decisionDefinitionName(final String p0);
    
    DecisionDefinitionQuery decisionDefinitionKey(final String p0);
    
    DecisionDefinitionQuery decisionDefinitionKeyLike(final String p0);
    
    DecisionDefinitionQuery decisionDefinitionNameLike(final String p0);
    
    DecisionDefinitionQuery deploymentId(final String p0);
    
    DecisionDefinitionQuery deployedAfter(final Date p0);
    
    DecisionDefinitionQuery deployedAt(final Date p0);
    
    DecisionDefinitionQuery decisionDefinitionVersion(final Integer p0);
    
    DecisionDefinitionQuery latestVersion();
    
    DecisionDefinitionQuery decisionDefinitionResourceName(final String p0);
    
    DecisionDefinitionQuery decisionDefinitionResourceNameLike(final String p0);
    
    DecisionDefinitionQuery decisionRequirementsDefinitionId(final String p0);
    
    DecisionDefinitionQuery decisionRequirementsDefinitionKey(final String p0);
    
    DecisionDefinitionQuery withoutDecisionRequirementsDefinition();
    
    DecisionDefinitionQuery tenantIdIn(final String... p0);
    
    DecisionDefinitionQuery withoutTenantId();
    
    DecisionDefinitionQuery includeDecisionDefinitionsWithoutTenantId();
    
    DecisionDefinitionQuery versionTag(final String p0);
    
    DecisionDefinitionQuery versionTagLike(final String p0);
    
    DecisionDefinitionQuery orderByDecisionDefinitionCategory();
    
    DecisionDefinitionQuery orderByDecisionDefinitionKey();
    
    DecisionDefinitionQuery orderByDecisionDefinitionId();
    
    DecisionDefinitionQuery orderByDecisionDefinitionVersion();
    
    DecisionDefinitionQuery orderByDecisionDefinitionName();
    
    DecisionDefinitionQuery orderByDeploymentId();
    
    DecisionDefinitionQuery orderByDeploymentTime();
    
    DecisionDefinitionQuery orderByTenantId();
    
    DecisionDefinitionQuery orderByDecisionRequirementsDefinitionKey();
    
    DecisionDefinitionQuery orderByVersionTag();
}
