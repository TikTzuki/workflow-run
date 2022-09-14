// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface HistoricCaseInstance
{
    String getId();
    
    String getBusinessKey();
    
    String getCaseDefinitionId();
    
    String getCaseDefinitionKey();
    
    String getCaseDefinitionName();
    
    Date getCreateTime();
    
    Date getCloseTime();
    
    Long getDurationInMillis();
    
    String getCreateUserId();
    
    String getSuperCaseInstanceId();
    
    String getSuperProcessInstanceId();
    
    String getTenantId();
    
    boolean isActive();
    
    boolean isCompleted();
    
    boolean isTerminated();
    
    boolean isClosed();
}
