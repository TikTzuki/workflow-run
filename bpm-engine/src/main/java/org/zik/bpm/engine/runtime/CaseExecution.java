// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

public interface CaseExecution
{
    String getId();
    
    String getCaseInstanceId();
    
    String getCaseDefinitionId();
    
    String getActivityId();
    
    String getActivityName();
    
    String getActivityType();
    
    String getActivityDescription();
    
    String getParentId();
    
    boolean isRequired();
    
    boolean isAvailable();
    
    boolean isActive();
    
    boolean isEnabled();
    
    boolean isDisabled();
    
    boolean isTerminated();
    
    String getTenantId();
}
