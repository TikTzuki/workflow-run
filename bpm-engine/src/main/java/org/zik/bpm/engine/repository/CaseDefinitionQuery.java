// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.repository;

import org.zik.bpm.engine.query.Query;

public interface CaseDefinitionQuery extends Query<CaseDefinitionQuery, CaseDefinition>
{
    CaseDefinitionQuery caseDefinitionId(final String p0);
    
    CaseDefinitionQuery caseDefinitionIdIn(final String... p0);
    
    CaseDefinitionQuery caseDefinitionCategory(final String p0);
    
    CaseDefinitionQuery caseDefinitionCategoryLike(final String p0);
    
    CaseDefinitionQuery caseDefinitionName(final String p0);
    
    CaseDefinitionQuery caseDefinitionKey(final String p0);
    
    CaseDefinitionQuery caseDefinitionKeyLike(final String p0);
    
    CaseDefinitionQuery caseDefinitionNameLike(final String p0);
    
    CaseDefinitionQuery deploymentId(final String p0);
    
    CaseDefinitionQuery caseDefinitionVersion(final Integer p0);
    
    CaseDefinitionQuery latestVersion();
    
    CaseDefinitionQuery caseDefinitionResourceName(final String p0);
    
    CaseDefinitionQuery caseDefinitionResourceNameLike(final String p0);
    
    CaseDefinitionQuery tenantIdIn(final String... p0);
    
    CaseDefinitionQuery withoutTenantId();
    
    CaseDefinitionQuery includeCaseDefinitionsWithoutTenantId();
    
    CaseDefinitionQuery orderByCaseDefinitionCategory();
    
    CaseDefinitionQuery orderByCaseDefinitionKey();
    
    CaseDefinitionQuery orderByCaseDefinitionId();
    
    CaseDefinitionQuery orderByCaseDefinitionVersion();
    
    CaseDefinitionQuery orderByCaseDefinitionName();
    
    CaseDefinitionQuery orderByDeploymentId();
    
    CaseDefinitionQuery orderByTenantId();
}
