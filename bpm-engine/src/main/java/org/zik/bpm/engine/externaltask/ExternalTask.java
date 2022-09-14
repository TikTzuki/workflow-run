// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.externaltask;

import java.util.Map;
import java.util.Date;

public interface ExternalTask
{
    String getId();
    
    String getTopicName();
    
    String getWorkerId();
    
    Date getLockExpirationTime();
    
    String getProcessInstanceId();
    
    String getExecutionId();
    
    String getActivityId();
    
    String getActivityInstanceId();
    
    String getProcessDefinitionId();
    
    String getProcessDefinitionKey();
    
    String getProcessDefinitionVersionTag();
    
    Integer getRetries();
    
    String getErrorMessage();
    
    boolean isSuspended();
    
    String getTenantId();
    
    long getPriority();
    
    Map<String, String> getExtensionProperties();
    
    String getBusinessKey();
}
