// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.history;

import java.util.Date;

public interface HistoricIncident
{
    String getId();
    
    Date getCreateTime();
    
    Date getEndTime();
    
    String getIncidentType();
    
    String getIncidentMessage();
    
    String getExecutionId();
    
    String getActivityId();
    
    String getRootProcessInstanceId();
    
    String getProcessInstanceId();
    
    String getProcessDefinitionId();
    
    String getProcessDefinitionKey();
    
    String getCauseIncidentId();
    
    String getRootCauseIncidentId();
    
    String getConfiguration();
    
    String getHistoryConfiguration();
    
    boolean isOpen();
    
    boolean isDeleted();
    
    boolean isResolved();
    
    String getTenantId();
    
    String getJobDefinitionId();
    
    Date getRemovalTime();
    
    String getFailedActivityId();
    
    String getAnnotation();
}
