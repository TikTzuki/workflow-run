// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import org.zik.bpm.engine.query.Query;

public interface HistoricVariableInstanceQuery extends Query<HistoricVariableInstanceQuery, HistoricVariableInstance>
{
    HistoricVariableInstanceQuery variableId(final String p0);
    
    HistoricVariableInstanceQuery processInstanceId(final String p0);
    
    HistoricVariableInstanceQuery processDefinitionId(final String p0);
    
    HistoricVariableInstanceQuery processDefinitionKey(final String p0);
    
    HistoricVariableInstanceQuery caseInstanceId(final String p0);
    
    HistoricVariableInstanceQuery variableName(final String p0);
    
    HistoricVariableInstanceQuery variableNameLike(final String p0);
    
    HistoricVariableInstanceQuery variableTypeIn(final String... p0);
    
    HistoricVariableInstanceQuery matchVariableNamesIgnoreCase();
    
    HistoricVariableInstanceQuery matchVariableValuesIgnoreCase();
    
    HistoricVariableInstanceQuery variableValueEquals(final String p0, final Object p1);
    
    HistoricVariableInstanceQuery orderByProcessInstanceId();
    
    HistoricVariableInstanceQuery orderByVariableName();
    
    HistoricVariableInstanceQuery processInstanceIdIn(final String... p0);
    
    HistoricVariableInstanceQuery taskIdIn(final String... p0);
    
    HistoricVariableInstanceQuery executionIdIn(final String... p0);
    
    HistoricVariableInstanceQuery caseExecutionIdIn(final String... p0);
    
    HistoricVariableInstanceQuery caseActivityIdIn(final String... p0);
    
    HistoricVariableInstanceQuery activityInstanceIdIn(final String... p0);
    
    HistoricVariableInstanceQuery tenantIdIn(final String... p0);
    
    HistoricVariableInstanceQuery withoutTenantId();
    
    HistoricVariableInstanceQuery orderByTenantId();
    
    HistoricVariableInstanceQuery disableBinaryFetching();
    
    HistoricVariableInstanceQuery disableCustomObjectDeserialization();
    
    HistoricVariableInstanceQuery includeDeleted();
    
    HistoricVariableInstanceQuery variableNameIn(final String... p0);
}
