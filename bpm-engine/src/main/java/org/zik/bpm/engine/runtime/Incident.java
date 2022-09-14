// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.runtime;

import java.util.Date;

public interface Incident
{
    public static final String FAILED_JOB_HANDLER_TYPE = "failedJob";
    public static final String EXTERNAL_TASK_HANDLER_TYPE = "failedExternalTask";
    
    String getId();
    
    Date getIncidentTimestamp();
    
    String getIncidentType();
    
    String getIncidentMessage();
    
    String getExecutionId();
    
    String getActivityId();
    
    String getFailedActivityId();
    
    String getProcessInstanceId();
    
    String getProcessDefinitionId();
    
    String getCauseIncidentId();
    
    String getRootCauseIncidentId();
    
    String getConfiguration();
    
    String getTenantId();
    
    String getJobDefinitionId();
    
    String getHistoryConfiguration();
    
    String getAnnotation();
}
