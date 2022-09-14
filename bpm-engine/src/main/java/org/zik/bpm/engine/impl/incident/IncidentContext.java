// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.incident;

import org.zik.bpm.engine.runtime.Incident;

public class IncidentContext
{
    protected String processDefinitionId;
    protected String activityId;
    protected String executionId;
    protected String configuration;
    protected String tenantId;
    protected String jobDefinitionId;
    protected String historyConfiguration;
    protected String failedActivityId;
    
    public IncidentContext() {
    }
    
    public IncidentContext(final Incident incident) {
        this.processDefinitionId = incident.getProcessDefinitionId();
        this.activityId = incident.getActivityId();
        this.executionId = incident.getExecutionId();
        this.configuration = incident.getConfiguration();
        this.tenantId = incident.getTenantId();
        this.jobDefinitionId = incident.getJobDefinitionId();
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }
    
    public String getConfiguration() {
        return this.configuration;
    }
    
    public void setConfiguration(final String configuration) {
        this.configuration = configuration;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getJobDefinitionId() {
        return this.jobDefinitionId;
    }
    
    public void setJobDefinitionId(final String jobDefinitionId) {
        this.jobDefinitionId = jobDefinitionId;
    }
    
    public String getHistoryConfiguration() {
        return this.historyConfiguration;
    }
    
    public void setHistoryConfiguration(final String historicConfiguration) {
        this.historyConfiguration = historicConfiguration;
    }
    
    public String getFailedActivityId() {
        return this.failedActivityId;
    }
    
    public void setFailedActivityId(final String failedActivityId) {
        this.failedActivityId = failedActivityId;
    }
}
