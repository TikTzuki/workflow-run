// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface HistoricExternalTaskLog
{
    String getId();
    
    Date getTimestamp();
    
    String getExternalTaskId();
    
    Integer getRetries();
    
    long getPriority();
    
    String getTopicName();
    
    String getWorkerId();
    
    String getErrorMessage();
    
    String getActivityId();
    
    String getActivityInstanceId();
    
    String getExecutionId();
    
    String getRootProcessInstanceId();
    
    String getProcessInstanceId();
    
    String getProcessDefinitionId();
    
    String getProcessDefinitionKey();
    
    String getTenantId();
    
    boolean isCreationLog();
    
    boolean isFailureLog();
    
    boolean isSuccessLog();
    
    boolean isDeletionLog();
    
    Date getRemovalTime();
}
