// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.history.IncidentState;
import java.util.Date;

public class HistoricIncidentEventEntity extends HistoryEvent
{
    private static final long serialVersionUID = 1L;
    protected Date createTime;
    protected Date endTime;
    protected String incidentType;
    protected String activityId;
    protected String causeIncidentId;
    protected String rootCauseIncidentId;
    protected String configuration;
    protected String incidentMessage;
    protected int incidentState;
    protected String tenantId;
    protected String jobDefinitionId;
    protected String historyConfiguration;
    protected String failedActivityId;
    protected String annotation;
    
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(final Date endTime) {
        this.endTime = endTime;
    }
    
    public String getIncidentType() {
        return this.incidentType;
    }
    
    public void setIncidentType(final String incidentType) {
        this.incidentType = incidentType;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    public String getCauseIncidentId() {
        return this.causeIncidentId;
    }
    
    public void setCauseIncidentId(final String causeIncidentId) {
        this.causeIncidentId = causeIncidentId;
    }
    
    public String getRootCauseIncidentId() {
        return this.rootCauseIncidentId;
    }
    
    public void setRootCauseIncidentId(final String rootCauseIncidentId) {
        this.rootCauseIncidentId = rootCauseIncidentId;
    }
    
    public String getConfiguration() {
        return this.configuration;
    }
    
    public void setConfiguration(final String configuration) {
        this.configuration = configuration;
    }
    
    public String getHistoryConfiguration() {
        return this.historyConfiguration;
    }
    
    public void setHistoryConfiguration(final String historyConfiguration) {
        this.historyConfiguration = historyConfiguration;
    }
    
    public String getIncidentMessage() {
        return this.incidentMessage;
    }
    
    public void setIncidentMessage(final String incidentMessage) {
        this.incidentMessage = incidentMessage;
    }
    
    public void setIncidentState(final int incidentState) {
        this.incidentState = incidentState;
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
    
    public boolean isOpen() {
        return IncidentState.DEFAULT.getStateCode() == this.incidentState;
    }
    
    public boolean isDeleted() {
        return IncidentState.DELETED.getStateCode() == this.incidentState;
    }
    
    public boolean isResolved() {
        return IncidentState.RESOLVED.getStateCode() == this.incidentState;
    }
    
    @Override
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    @Override
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    public String getFailedActivityId() {
        return this.failedActivityId;
    }
    
    public void setFailedActivityId(final String failedActivityId) {
        this.failedActivityId = failedActivityId;
    }
    
    public String getAnnotation() {
        return this.annotation;
    }
    
    public void setAnnotation(final String annotation) {
        this.annotation = annotation;
    }
}
