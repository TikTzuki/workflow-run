// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.zik.bpm.engine.query.Query;

public interface HistoricProcessInstanceQuery extends Query<HistoricProcessInstanceQuery, HistoricProcessInstance>
{
    HistoricProcessInstanceQuery processInstanceId(final String p0);
    
    HistoricProcessInstanceQuery processInstanceIds(final Set<String> p0);
    
    HistoricProcessInstanceQuery processDefinitionId(final String p0);
    
    HistoricProcessInstanceQuery processDefinitionKey(final String p0);
    
    HistoricProcessInstanceQuery processDefinitionKeyIn(final String... p0);
    
    HistoricProcessInstanceQuery processDefinitionKeyNotIn(final List<String> p0);
    
    HistoricProcessInstanceQuery processDefinitionName(final String p0);
    
    HistoricProcessInstanceQuery processDefinitionNameLike(final String p0);
    
    HistoricProcessInstanceQuery processInstanceBusinessKey(final String p0);
    
    HistoricProcessInstanceQuery processInstanceBusinessKeyIn(final String... p0);
    
    HistoricProcessInstanceQuery processInstanceBusinessKeyLike(final String p0);
    
    HistoricProcessInstanceQuery finished();
    
    HistoricProcessInstanceQuery unfinished();
    
    HistoricProcessInstanceQuery withIncidents();
    
    HistoricProcessInstanceQuery withRootIncidents();
    
    HistoricProcessInstanceQuery incidentStatus(final String p0);
    
    HistoricProcessInstanceQuery incidentType(final String p0);
    
    HistoricProcessInstanceQuery incidentMessage(final String p0);
    
    HistoricProcessInstanceQuery incidentMessageLike(final String p0);
    
    HistoricProcessInstanceQuery caseInstanceId(final String p0);
    
    HistoricProcessInstanceQuery matchVariableNamesIgnoreCase();
    
    HistoricProcessInstanceQuery matchVariableValuesIgnoreCase();
    
    HistoricProcessInstanceQuery variableValueEquals(final String p0, final Object p1);
    
    HistoricProcessInstanceQuery variableValueNotEquals(final String p0, final Object p1);
    
    HistoricProcessInstanceQuery variableValueGreaterThan(final String p0, final Object p1);
    
    HistoricProcessInstanceQuery variableValueGreaterThanOrEqual(final String p0, final Object p1);
    
    HistoricProcessInstanceQuery variableValueLessThan(final String p0, final Object p1);
    
    HistoricProcessInstanceQuery variableValueLessThanOrEqual(final String p0, final Object p1);
    
    HistoricProcessInstanceQuery variableValueLike(final String p0, final String p1);
    
    HistoricProcessInstanceQuery startedBefore(final Date p0);
    
    HistoricProcessInstanceQuery startedAfter(final Date p0);
    
    HistoricProcessInstanceQuery finishedBefore(final Date p0);
    
    HistoricProcessInstanceQuery finishedAfter(final Date p0);
    
    HistoricProcessInstanceQuery startedBy(final String p0);
    
    HistoricProcessInstanceQuery orderByProcessInstanceId();
    
    HistoricProcessInstanceQuery orderByProcessDefinitionId();
    
    HistoricProcessInstanceQuery orderByProcessDefinitionKey();
    
    HistoricProcessInstanceQuery orderByProcessDefinitionName();
    
    HistoricProcessInstanceQuery orderByProcessDefinitionVersion();
    
    HistoricProcessInstanceQuery orderByProcessInstanceBusinessKey();
    
    HistoricProcessInstanceQuery orderByProcessInstanceStartTime();
    
    HistoricProcessInstanceQuery orderByProcessInstanceEndTime();
    
    HistoricProcessInstanceQuery orderByProcessInstanceDuration();
    
    HistoricProcessInstanceQuery rootProcessInstances();
    
    HistoricProcessInstanceQuery superProcessInstanceId(final String p0);
    
    HistoricProcessInstanceQuery subProcessInstanceId(final String p0);
    
    HistoricProcessInstanceQuery superCaseInstanceId(final String p0);
    
    HistoricProcessInstanceQuery subCaseInstanceId(final String p0);
    
    HistoricProcessInstanceQuery tenantIdIn(final String... p0);
    
    HistoricProcessInstanceQuery withoutTenantId();
    
    HistoricProcessInstanceQuery orderByTenantId();
    
    @Deprecated
    HistoricProcessInstanceQuery startDateBy(final Date p0);
    
    @Deprecated
    HistoricProcessInstanceQuery startDateOn(final Date p0);
    
    @Deprecated
    HistoricProcessInstanceQuery finishDateBy(final Date p0);
    
    @Deprecated
    HistoricProcessInstanceQuery finishDateOn(final Date p0);
    
    HistoricProcessInstanceQuery executedActivityAfter(final Date p0);
    
    HistoricProcessInstanceQuery executedActivityBefore(final Date p0);
    
    HistoricProcessInstanceQuery executedActivityIdIn(final String... p0);
    
    HistoricProcessInstanceQuery activeActivityIdIn(final String... p0);
    
    HistoricProcessInstanceQuery executedJobAfter(final Date p0);
    
    HistoricProcessInstanceQuery executedJobBefore(final Date p0);
    
    HistoricProcessInstanceQuery active();
    
    HistoricProcessInstanceQuery suspended();
    
    HistoricProcessInstanceQuery completed();
    
    HistoricProcessInstanceQuery externallyTerminated();
    
    HistoricProcessInstanceQuery internallyTerminated();
    
    HistoricProcessInstanceQuery or();
    
    HistoricProcessInstanceQuery endOr();
}
