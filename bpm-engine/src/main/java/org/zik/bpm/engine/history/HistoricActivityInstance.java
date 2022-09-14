// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface HistoricActivityInstance
{
    String getId();
    
    String getParentActivityInstanceId();
    
    String getActivityId();
    
    String getActivityName();
    
    String getActivityType();
    
    String getProcessDefinitionKey();
    
    String getProcessDefinitionId();
    
    String getRootProcessInstanceId();
    
    String getProcessInstanceId();
    
    String getExecutionId();
    
    String getTaskId();
    
    String getCalledProcessInstanceId();
    
    String getCalledCaseInstanceId();
    
    String getAssignee();
    
    Date getStartTime();
    
    Date getEndTime();
    
    Long getDurationInMillis();
    
    boolean isCompleteScope();
    
    boolean isCanceled();
    
    String getTenantId();
    
    Date getRemovalTime();
}
