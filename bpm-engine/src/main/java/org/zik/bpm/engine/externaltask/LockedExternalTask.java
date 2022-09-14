// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.externaltask;

import java.util.Map;
import org.camunda.bpm.engine.variable.VariableMap;
import java.util.Date;

public interface LockedExternalTask
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
    
    String getErrorDetails();
    
    VariableMap getVariables();
    
    String getTenantId();
    
    long getPriority();
    
    String getBusinessKey();
    
    Map<String, String> getExtensionProperties();
}
