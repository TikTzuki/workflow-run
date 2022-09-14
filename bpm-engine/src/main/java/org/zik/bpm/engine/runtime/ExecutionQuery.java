// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.query.Query;

public interface ExecutionQuery extends Query<ExecutionQuery, Execution>
{
    ExecutionQuery processDefinitionKey(final String p0);
    
    ExecutionQuery processDefinitionId(final String p0);
    
    ExecutionQuery processInstanceId(final String p0);
    
    ExecutionQuery processInstanceBusinessKey(final String p0);
    
    ExecutionQuery executionId(final String p0);
    
    ExecutionQuery activityId(final String p0);
    
    ExecutionQuery matchVariableNamesIgnoreCase();
    
    ExecutionQuery matchVariableValuesIgnoreCase();
    
    ExecutionQuery variableValueEquals(final String p0, final Object p1);
    
    ExecutionQuery variableValueNotEquals(final String p0, final Object p1);
    
    ExecutionQuery variableValueGreaterThan(final String p0, final Object p1);
    
    ExecutionQuery variableValueGreaterThanOrEqual(final String p0, final Object p1);
    
    ExecutionQuery variableValueLessThan(final String p0, final Object p1);
    
    ExecutionQuery variableValueLessThanOrEqual(final String p0, final Object p1);
    
    ExecutionQuery variableValueLike(final String p0, final String p1);
    
    ExecutionQuery processVariableValueEquals(final String p0, final Object p1);
    
    ExecutionQuery processVariableValueNotEquals(final String p0, final Object p1);
    
    @Deprecated
    ExecutionQuery signalEventSubscription(final String p0);
    
    ExecutionQuery signalEventSubscriptionName(final String p0);
    
    ExecutionQuery messageEventSubscriptionName(final String p0);
    
    ExecutionQuery messageEventSubscription();
    
    ExecutionQuery suspended();
    
    ExecutionQuery active();
    
    ExecutionQuery incidentType(final String p0);
    
    ExecutionQuery incidentId(final String p0);
    
    ExecutionQuery incidentMessage(final String p0);
    
    ExecutionQuery incidentMessageLike(final String p0);
    
    ExecutionQuery tenantIdIn(final String... p0);
    
    ExecutionQuery withoutTenantId();
    
    ExecutionQuery orderByProcessInstanceId();
    
    ExecutionQuery orderByProcessDefinitionKey();
    
    ExecutionQuery orderByProcessDefinitionId();
    
    ExecutionQuery orderByTenantId();
}
