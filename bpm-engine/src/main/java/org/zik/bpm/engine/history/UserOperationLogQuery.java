// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;
import org.zik.bpm.engine.query.Query;

public interface UserOperationLogQuery extends Query<UserOperationLogQuery, UserOperationLogEntry>
{
    UserOperationLogQuery entityType(final String p0);
    
    UserOperationLogQuery entityTypeIn(final String... p0);
    
    UserOperationLogQuery operationType(final String p0);
    
    UserOperationLogQuery deploymentId(final String p0);
    
    UserOperationLogQuery processDefinitionId(final String p0);
    
    UserOperationLogQuery processDefinitionKey(final String p0);
    
    UserOperationLogQuery processInstanceId(final String p0);
    
    UserOperationLogQuery executionId(final String p0);
    
    UserOperationLogQuery caseDefinitionId(final String p0);
    
    UserOperationLogQuery caseInstanceId(final String p0);
    
    UserOperationLogQuery caseExecutionId(final String p0);
    
    UserOperationLogQuery taskId(final String p0);
    
    UserOperationLogQuery jobId(final String p0);
    
    UserOperationLogQuery jobDefinitionId(final String p0);
    
    UserOperationLogQuery batchId(final String p0);
    
    UserOperationLogQuery userId(final String p0);
    
    UserOperationLogQuery operationId(final String p0);
    
    UserOperationLogQuery externalTaskId(final String p0);
    
    UserOperationLogQuery property(final String p0);
    
    UserOperationLogQuery category(final String p0);
    
    UserOperationLogQuery categoryIn(final String... p0);
    
    UserOperationLogQuery afterTimestamp(final Date p0);
    
    UserOperationLogQuery beforeTimestamp(final Date p0);
    
    UserOperationLogQuery orderByTimestamp();
}
