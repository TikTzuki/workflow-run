// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.query.Query;

public interface CaseInstanceQuery extends Query<CaseInstanceQuery, CaseInstance>
{
    CaseInstanceQuery caseInstanceId(final String p0);
    
    CaseInstanceQuery caseInstanceBusinessKey(final String p0);
    
    CaseInstanceQuery caseDefinitionKey(final String p0);
    
    CaseInstanceQuery caseDefinitionId(final String p0);
    
    CaseInstanceQuery deploymentId(final String p0);
    
    CaseInstanceQuery superProcessInstanceId(final String p0);
    
    CaseInstanceQuery subProcessInstanceId(final String p0);
    
    CaseInstanceQuery superCaseInstanceId(final String p0);
    
    CaseInstanceQuery subCaseInstanceId(final String p0);
    
    CaseInstanceQuery active();
    
    CaseInstanceQuery completed();
    
    CaseInstanceQuery terminated();
    
    CaseInstanceQuery matchVariableNamesIgnoreCase();
    
    CaseInstanceQuery matchVariableValuesIgnoreCase();
    
    CaseInstanceQuery variableValueEquals(final String p0, final Object p1);
    
    CaseInstanceQuery variableValueNotEquals(final String p0, final Object p1);
    
    CaseInstanceQuery variableValueGreaterThan(final String p0, final Object p1);
    
    CaseInstanceQuery variableValueGreaterThanOrEqual(final String p0, final Object p1);
    
    CaseInstanceQuery variableValueLessThan(final String p0, final Object p1);
    
    CaseInstanceQuery variableValueLessThanOrEqual(final String p0, final Object p1);
    
    CaseInstanceQuery variableValueLike(final String p0, final String p1);
    
    CaseInstanceQuery variableValueNotLike(final String p0, final String p1);
    
    CaseInstanceQuery tenantIdIn(final String... p0);
    
    CaseInstanceQuery withoutTenantId();
    
    CaseInstanceQuery orderByCaseInstanceId();
    
    CaseInstanceQuery orderByCaseDefinitionKey();
    
    CaseInstanceQuery orderByCaseDefinitionId();
    
    CaseInstanceQuery orderByTenantId();
}
