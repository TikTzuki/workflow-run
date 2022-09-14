// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import org.zik.bpm.engine.query.Query;

public interface DecisionRequirementsDefinitionQuery extends Query<DecisionRequirementsDefinitionQuery, DecisionRequirementsDefinition>
{
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionId(final String p0);
    
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionIdIn(final String... p0);
    
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionCategory(final String p0);
    
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionCategoryLike(final String p0);
    
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionName(final String p0);
    
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionNameLike(final String p0);
    
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionKey(final String p0);
    
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionKeyLike(final String p0);
    
    DecisionRequirementsDefinitionQuery deploymentId(final String p0);
    
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionVersion(final Integer p0);
    
    DecisionRequirementsDefinitionQuery latestVersion();
    
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionResourceName(final String p0);
    
    DecisionRequirementsDefinitionQuery decisionRequirementsDefinitionResourceNameLike(final String p0);
    
    DecisionRequirementsDefinitionQuery tenantIdIn(final String... p0);
    
    DecisionRequirementsDefinitionQuery withoutTenantId();
    
    DecisionRequirementsDefinitionQuery includeDecisionRequirementsDefinitionsWithoutTenantId();
    
    DecisionRequirementsDefinitionQuery orderByDecisionRequirementsDefinitionCategory();
    
    DecisionRequirementsDefinitionQuery orderByDecisionRequirementsDefinitionKey();
    
    DecisionRequirementsDefinitionQuery orderByDecisionRequirementsDefinitionId();
    
    DecisionRequirementsDefinitionQuery orderByDecisionRequirementsDefinitionVersion();
    
    DecisionRequirementsDefinitionQuery orderByDecisionRequirementsDefinitionName();
    
    DecisionRequirementsDefinitionQuery orderByDeploymentId();
    
    DecisionRequirementsDefinitionQuery orderByTenantId();
}
