// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.Date;

public interface EventSubscription
{
    String getId();
    
    String getEventType();
    
    String getEventName();
    
    String getExecutionId();
    
    String getProcessInstanceId();
    
    String getActivityId();
    
    String getTenantId();
    
    Date getCreated();
}
