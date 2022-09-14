// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface HistoricDetail
{
    String getId();
    
    String getProcessDefinitionKey();
    
    String getProcessDefinitionId();
    
    String getRootProcessInstanceId();
    
    String getProcessInstanceId();
    
    String getActivityInstanceId();
    
    String getExecutionId();
    
    String getCaseDefinitionKey();
    
    String getCaseDefinitionId();
    
    String getCaseInstanceId();
    
    String getCaseExecutionId();
    
    String getTaskId();
    
    Date getTime();
    
    String getTenantId();
    
    String getUserOperationId();
    
    Date getRemovalTime();
}
