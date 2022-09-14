// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface HistoricIdentityLinkLog
{
    String getId();
    
    String getType();
    
    String getUserId();
    
    String getGroupId();
    
    String getTaskId();
    
    String getAssignerId();
    
    String getOperationType();
    
    Date getTime();
    
    String getProcessDefinitionId();
    
    String getProcessDefinitionKey();
    
    String getTenantId();
    
    String getRootProcessInstanceId();
    
    Date getRemovalTime();
}
