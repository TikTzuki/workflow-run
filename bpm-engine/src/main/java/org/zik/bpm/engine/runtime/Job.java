// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.Date;

public interface Job
{
    String getId();
    
    Date getDuedate();
    
    String getProcessInstanceId();
    
    String getProcessDefinitionId();
    
    String getProcessDefinitionKey();
    
    String getExecutionId();
    
    int getRetries();
    
    String getExceptionMessage();
    
    String getFailedActivityId();
    
    String getDeploymentId();
    
    String getJobDefinitionId();
    
    boolean isSuspended();
    
    long getPriority();
    
    String getTenantId();
    
    Date getCreateTime();
}
