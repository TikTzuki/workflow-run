// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface HistoricTaskInstance
{
    String getId();
    
    String getProcessDefinitionKey();
    
    String getProcessDefinitionId();
    
    String getRootProcessInstanceId();
    
    String getProcessInstanceId();
    
    String getExecutionId();
    
    String getCaseDefinitionKey();
    
    String getCaseDefinitionId();
    
    String getCaseInstanceId();
    
    String getCaseExecutionId();
    
    String getActivityInstanceId();
    
    String getName();
    
    String getDescription();
    
    String getDeleteReason();
    
    String getOwner();
    
    String getAssignee();
    
    Date getStartTime();
    
    Date getEndTime();
    
    Long getDurationInMillis();
    
    String getTaskDefinitionKey();
    
    int getPriority();
    
    Date getDueDate();
    
    String getParentTaskId();
    
    Date getFollowUpDate();
    
    String getTenantId();
    
    Date getRemovalTime();
}
