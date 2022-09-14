// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.delegate;

public interface DelegateCaseExecution extends BaseDelegateExecution, ProcessEngineServicesAware, CmmnModelExecutionContext
{
    String getId();
    
    String getCaseInstanceId();
    
    String getEventName();
    
    String getCaseBusinessKey();
    
    String getCaseDefinitionId();
    
    String getParentId();
    
    String getActivityId();
    
    String getActivityName();
    
    String getTenantId();
    
    boolean isAvailable();
    
    boolean isEnabled();
    
    boolean isDisabled();
    
    boolean isActive();
    
    boolean isSuspended();
    
    boolean isTerminated();
    
    boolean isCompleted();
    
    boolean isFailed();
    
    boolean isClosed();
}
