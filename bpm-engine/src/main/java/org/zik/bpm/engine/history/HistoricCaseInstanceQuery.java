// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.zik.bpm.engine.query.Query;

public interface HistoricCaseInstanceQuery extends Query<HistoricCaseInstanceQuery, HistoricCaseInstance>
{
    HistoricCaseInstanceQuery caseInstanceId(final String p0);
    
    HistoricCaseInstanceQuery caseInstanceIds(final Set<String> p0);
    
    HistoricCaseInstanceQuery caseDefinitionId(final String p0);
    
    HistoricCaseInstanceQuery caseDefinitionKey(final String p0);
    
    HistoricCaseInstanceQuery caseDefinitionKeyNotIn(final List<String> p0);
    
    HistoricCaseInstanceQuery caseDefinitionName(final String p0);
    
    HistoricCaseInstanceQuery caseDefinitionNameLike(final String p0);
    
    HistoricCaseInstanceQuery caseInstanceBusinessKey(final String p0);
    
    HistoricCaseInstanceQuery caseInstanceBusinessKeyLike(final String p0);
    
    HistoricCaseInstanceQuery caseActivityIdIn(final String... p0);
    
    HistoricCaseInstanceQuery createdBefore(final Date p0);
    
    HistoricCaseInstanceQuery createdAfter(final Date p0);
    
    HistoricCaseInstanceQuery closedBefore(final Date p0);
    
    HistoricCaseInstanceQuery closedAfter(final Date p0);
    
    HistoricCaseInstanceQuery createdBy(final String p0);
    
    HistoricCaseInstanceQuery superCaseInstanceId(final String p0);
    
    HistoricCaseInstanceQuery subCaseInstanceId(final String p0);
    
    HistoricCaseInstanceQuery superProcessInstanceId(final String p0);
    
    HistoricCaseInstanceQuery subProcessInstanceId(final String p0);
    
    HistoricCaseInstanceQuery tenantIdIn(final String... p0);
    
    HistoricCaseInstanceQuery withoutTenantId();
    
    HistoricCaseInstanceQuery active();
    
    HistoricCaseInstanceQuery completed();
    
    HistoricCaseInstanceQuery terminated();
    
    HistoricCaseInstanceQuery closed();
    
    HistoricCaseInstanceQuery notClosed();
    
    HistoricCaseInstanceQuery matchVariableNamesIgnoreCase();
    
    HistoricCaseInstanceQuery matchVariableValuesIgnoreCase();
    
    HistoricCaseInstanceQuery variableValueEquals(final String p0, final Object p1);
    
    HistoricCaseInstanceQuery variableValueNotEquals(final String p0, final Object p1);
    
    HistoricCaseInstanceQuery variableValueGreaterThan(final String p0, final Object p1);
    
    HistoricCaseInstanceQuery variableValueGreaterThanOrEqual(final String p0, final Object p1);
    
    HistoricCaseInstanceQuery variableValueLessThan(final String p0, final Object p1);
    
    HistoricCaseInstanceQuery variableValueLessThanOrEqual(final String p0, final Object p1);
    
    HistoricCaseInstanceQuery variableValueLike(final String p0, final String p1);
    
    HistoricCaseInstanceQuery variableValueNotLike(final String p0, final String p1);
    
    HistoricCaseInstanceQuery orderByCaseInstanceId();
    
    HistoricCaseInstanceQuery orderByCaseDefinitionId();
    
    HistoricCaseInstanceQuery orderByCaseInstanceBusinessKey();
    
    HistoricCaseInstanceQuery orderByCaseInstanceCreateTime();
    
    HistoricCaseInstanceQuery orderByCaseInstanceCloseTime();
    
    HistoricCaseInstanceQuery orderByCaseInstanceDuration();
    
    HistoricCaseInstanceQuery orderByTenantId();
}
