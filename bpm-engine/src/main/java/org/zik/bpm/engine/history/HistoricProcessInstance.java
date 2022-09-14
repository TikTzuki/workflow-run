// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface HistoricProcessInstance
{
    public static final String STATE_ACTIVE = "ACTIVE";
    public static final String STATE_SUSPENDED = "SUSPENDED";
    public static final String STATE_COMPLETED = "COMPLETED";
    public static final String STATE_EXTERNALLY_TERMINATED = "EXTERNALLY_TERMINATED";
    public static final String STATE_INTERNALLY_TERMINATED = "INTERNALLY_TERMINATED";
    
    String getId();
    
    String getBusinessKey();
    
    String getProcessDefinitionKey();
    
    String getProcessDefinitionId();
    
    String getProcessDefinitionName();
    
    Integer getProcessDefinitionVersion();
    
    Date getStartTime();
    
    Date getEndTime();
    
    Date getRemovalTime();
    
    Long getDurationInMillis();
    
    @Deprecated
    String getEndActivityId();
    
    String getStartUserId();
    
    String getStartActivityId();
    
    String getDeleteReason();
    
    String getSuperProcessInstanceId();
    
    String getRootProcessInstanceId();
    
    String getSuperCaseInstanceId();
    
    String getCaseInstanceId();
    
    String getTenantId();
    
    String getState();
}
