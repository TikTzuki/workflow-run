// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface HistoricCaseActivityInstance
{
    String getId();
    
    String getParentCaseActivityInstanceId();
    
    String getCaseActivityId();
    
    String getCaseActivityName();
    
    String getCaseActivityType();
    
    String getCaseDefinitionId();
    
    String getCaseInstanceId();
    
    String getCaseExecutionId();
    
    String getTaskId();
    
    String getCalledProcessInstanceId();
    
    String getCalledCaseInstanceId();
    
    String getTenantId();
    
    Date getCreateTime();
    
    Date getEndTime();
    
    Long getDurationInMillis();
    
    boolean isRequired();
    
    boolean isAvailable();
    
    boolean isEnabled();
    
    boolean isDisabled();
    
    boolean isActive();
    
    boolean isCompleted();
    
    boolean isTerminated();
}
