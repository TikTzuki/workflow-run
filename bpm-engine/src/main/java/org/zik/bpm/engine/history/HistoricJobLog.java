// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface HistoricJobLog
{
    String getId();
    
    Date getTimestamp();
    
    String getJobId();
    
    Date getJobDueDate();
    
    int getJobRetries();
    
    long getJobPriority();
    
    String getJobExceptionMessage();
    
    String getJobDefinitionId();
    
    String getJobDefinitionType();
    
    String getJobDefinitionConfiguration();
    
    String getActivityId();
    
    String getFailedActivityId();
    
    String getExecutionId();
    
    String getRootProcessInstanceId();
    
    String getProcessInstanceId();
    
    String getProcessDefinitionId();
    
    String getProcessDefinitionKey();
    
    String getDeploymentId();
    
    String getTenantId();
    
    String getHostname();
    
    boolean isCreationLog();
    
    boolean isFailureLog();
    
    boolean isSuccessLog();
    
    boolean isDeletionLog();
    
    Date getRemovalTime();
}
