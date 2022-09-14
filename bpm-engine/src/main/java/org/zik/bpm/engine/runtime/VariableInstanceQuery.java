// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import org.zik.bpm.engine.query.Query;

public interface VariableInstanceQuery extends Query<VariableInstanceQuery, VariableInstance>
{
    VariableInstanceQuery variableId(final String p0);
    
    VariableInstanceQuery variableName(final String p0);
    
    VariableInstanceQuery variableNameIn(final String... p0);
    
    VariableInstanceQuery variableNameLike(final String p0);
    
    VariableInstanceQuery executionIdIn(final String... p0);
    
    VariableInstanceQuery processInstanceIdIn(final String... p0);
    
    VariableInstanceQuery caseExecutionIdIn(final String... p0);
    
    VariableInstanceQuery caseInstanceIdIn(final String... p0);
    
    VariableInstanceQuery taskIdIn(final String... p0);
    
    VariableInstanceQuery batchIdIn(final String... p0);
    
    VariableInstanceQuery variableScopeIdIn(final String... p0);
    
    VariableInstanceQuery activityInstanceIdIn(final String... p0);
    
    VariableInstanceQuery matchVariableNamesIgnoreCase();
    
    VariableInstanceQuery matchVariableValuesIgnoreCase();
    
    VariableInstanceQuery variableValueEquals(final String p0, final Object p1);
    
    VariableInstanceQuery variableValueNotEquals(final String p0, final Object p1);
    
    VariableInstanceQuery variableValueGreaterThan(final String p0, final Object p1);
    
    VariableInstanceQuery variableValueGreaterThanOrEqual(final String p0, final Object p1);
    
    VariableInstanceQuery variableValueLessThan(final String p0, final Object p1);
    
    VariableInstanceQuery variableValueLessThanOrEqual(final String p0, final Object p1);
    
    VariableInstanceQuery disableBinaryFetching();
    
    VariableInstanceQuery disableCustomObjectDeserialization();
    
    VariableInstanceQuery variableValueLike(final String p0, final String p1);
    
    VariableInstanceQuery tenantIdIn(final String... p0);
    
    VariableInstanceQuery orderByVariableName();
    
    VariableInstanceQuery orderByVariableType();
    
    VariableInstanceQuery orderByActivityInstanceId();
    
    VariableInstanceQuery orderByTenantId();
}
