// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.query.Query;

public interface CaseExecutionQuery extends Query<CaseExecutionQuery, CaseExecution>
{
    CaseExecutionQuery caseInstanceId(final String p0);
    
    CaseExecutionQuery caseDefinitionId(final String p0);
    
    CaseExecutionQuery caseDefinitionKey(final String p0);
    
    CaseExecutionQuery caseInstanceBusinessKey(final String p0);
    
    CaseExecutionQuery caseExecutionId(final String p0);
    
    CaseExecutionQuery activityId(final String p0);
    
    CaseExecutionQuery required();
    
    CaseExecutionQuery available();
    
    CaseExecutionQuery enabled();
    
    CaseExecutionQuery active();
    
    CaseExecutionQuery disabled();
    
    CaseExecutionQuery matchVariableNamesIgnoreCase();
    
    CaseExecutionQuery matchVariableValuesIgnoreCase();
    
    CaseExecutionQuery variableValueEquals(final String p0, final Object p1);
    
    CaseExecutionQuery variableValueNotEquals(final String p0, final Object p1);
    
    CaseExecutionQuery variableValueGreaterThan(final String p0, final Object p1);
    
    CaseExecutionQuery variableValueGreaterThanOrEqual(final String p0, final Object p1);
    
    CaseExecutionQuery variableValueLessThan(final String p0, final Object p1);
    
    CaseExecutionQuery variableValueLessThanOrEqual(final String p0, final Object p1);
    
    CaseExecutionQuery variableValueLike(final String p0, final String p1);
    
    CaseExecutionQuery caseInstanceVariableValueEquals(final String p0, final Object p1);
    
    CaseExecutionQuery caseInstanceVariableValueNotEquals(final String p0, final Object p1);
    
    CaseExecutionQuery caseInstanceVariableValueGreaterThan(final String p0, final Object p1);
    
    CaseExecutionQuery caseInstanceVariableValueGreaterThanOrEqual(final String p0, final Object p1);
    
    CaseExecutionQuery caseInstanceVariableValueLessThan(final String p0, final Object p1);
    
    CaseExecutionQuery caseInstanceVariableValueLessThanOrEqual(final String p0, final Object p1);
    
    CaseExecutionQuery caseInstanceVariableValueLike(final String p0, final String p1);
    
    CaseExecutionQuery tenantIdIn(final String... p0);
    
    CaseExecutionQuery withoutTenantId();
    
    CaseExecutionQuery orderByCaseExecutionId();
    
    CaseExecutionQuery orderByCaseDefinitionKey();
    
    CaseExecutionQuery orderByCaseDefinitionId();
    
    CaseExecutionQuery orderByTenantId();
}
