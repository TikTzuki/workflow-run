// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface HistoricDetailQuery extends Query<HistoricDetailQuery, HistoricDetail>
{
    HistoricDetailQuery detailId(final String p0);
    
    HistoricDetailQuery processInstanceId(final String p0);
    
    HistoricDetailQuery caseInstanceId(final String p0);
    
    HistoricDetailQuery executionId(final String p0);
    
    HistoricDetailQuery caseExecutionId(final String p0);
    
    @Deprecated
    HistoricDetailQuery activityId(final String p0);
    
    HistoricDetailQuery activityInstanceId(final String p0);
    
    HistoricDetailQuery taskId(final String p0);
    
    HistoricDetailQuery variableInstanceId(final String p0);
    
    HistoricDetailQuery variableTypeIn(final String... p0);
    
    @Deprecated
    HistoricDetailQuery formProperties();
    
    HistoricDetailQuery formFields();
    
    HistoricDetailQuery variableUpdates();
    
    HistoricDetailQuery disableBinaryFetching();
    
    HistoricDetailQuery disableCustomObjectDeserialization();
    
    HistoricDetailQuery excludeTaskDetails();
    
    HistoricDetailQuery tenantIdIn(final String... p0);
    
    HistoricDetailQuery withoutTenantId();
    
    HistoricDetailQuery processInstanceIdIn(final String... p0);
    
    HistoricDetailQuery userOperationId(final String p0);
    
    HistoricDetailQuery occurredBefore(final Date p0);
    
    HistoricDetailQuery occurredAfter(final Date p0);
    
    HistoricDetailQuery initial();
    
    HistoricDetailQuery orderByTenantId();
    
    HistoricDetailQuery orderByProcessInstanceId();
    
    HistoricDetailQuery orderByVariableName();
    
    HistoricDetailQuery orderByFormPropertyId();
    
    HistoricDetailQuery orderByVariableType();
    
    HistoricDetailQuery orderByVariableRevision();
    
    HistoricDetailQuery orderByTime();
    
    HistoricDetailQuery orderPartiallyByOccurrence();
}
