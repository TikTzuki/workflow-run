// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.Set;
import org.zik.bpm.engine.query.Query;

public interface ProcessInstanceQuery extends Query<ProcessInstanceQuery, ProcessInstance>
{
    ProcessInstanceQuery processInstanceId(final String p0);
    
    ProcessInstanceQuery processInstanceIds(final Set<String> p0);
    
    ProcessInstanceQuery processInstanceBusinessKey(final String p0);
    
    ProcessInstanceQuery processInstanceBusinessKey(final String p0, final String p1);
    
    ProcessInstanceQuery processInstanceBusinessKeyLike(final String p0);
    
    ProcessInstanceQuery processDefinitionKey(final String p0);
    
    ProcessInstanceQuery processDefinitionKeyIn(final String... p0);
    
    ProcessInstanceQuery processDefinitionKeyNotIn(final String... p0);
    
    ProcessInstanceQuery processDefinitionId(final String p0);
    
    ProcessInstanceQuery deploymentId(final String p0);
    
    ProcessInstanceQuery superProcessInstanceId(final String p0);
    
    ProcessInstanceQuery subProcessInstanceId(final String p0);
    
    ProcessInstanceQuery caseInstanceId(final String p0);
    
    ProcessInstanceQuery superCaseInstanceId(final String p0);
    
    ProcessInstanceQuery subCaseInstanceId(final String p0);
    
    ProcessInstanceQuery matchVariableNamesIgnoreCase();
    
    ProcessInstanceQuery matchVariableValuesIgnoreCase();
    
    ProcessInstanceQuery variableValueEquals(final String p0, final Object p1);
    
    ProcessInstanceQuery variableValueNotEquals(final String p0, final Object p1);
    
    ProcessInstanceQuery variableValueGreaterThan(final String p0, final Object p1);
    
    ProcessInstanceQuery variableValueGreaterThanOrEqual(final String p0, final Object p1);
    
    ProcessInstanceQuery variableValueLessThan(final String p0, final Object p1);
    
    ProcessInstanceQuery variableValueLessThanOrEqual(final String p0, final Object p1);
    
    ProcessInstanceQuery variableValueLike(final String p0, final String p1);
    
    ProcessInstanceQuery suspended();
    
    ProcessInstanceQuery active();
    
    ProcessInstanceQuery withIncident();
    
    ProcessInstanceQuery incidentType(final String p0);
    
    ProcessInstanceQuery incidentId(final String p0);
    
    ProcessInstanceQuery incidentMessage(final String p0);
    
    ProcessInstanceQuery incidentMessageLike(final String p0);
    
    ProcessInstanceQuery tenantIdIn(final String... p0);
    
    ProcessInstanceQuery withoutTenantId();
    
    ProcessInstanceQuery activityIdIn(final String... p0);
    
    ProcessInstanceQuery rootProcessInstances();
    
    ProcessInstanceQuery leafProcessInstances();
    
    ProcessInstanceQuery processDefinitionWithoutTenantId();
    
    ProcessInstanceQuery orderByProcessInstanceId();
    
    ProcessInstanceQuery orderByProcessDefinitionKey();
    
    ProcessInstanceQuery orderByProcessDefinitionId();
    
    ProcessInstanceQuery orderByTenantId();
    
    ProcessInstanceQuery orderByBusinessKey();
    
    ProcessInstanceQuery or();
    
    ProcessInstanceQuery endOr();
}
