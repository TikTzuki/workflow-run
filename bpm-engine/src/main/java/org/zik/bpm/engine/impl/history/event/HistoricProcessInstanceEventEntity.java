// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

public class HistoricProcessInstanceEventEntity extends HistoricScopeInstanceEvent
{
    private static final long serialVersionUID = 1L;
    protected String businessKey;
    protected String startUserId;
    protected String superProcessInstanceId;
    protected String superCaseInstanceId;
    protected String deleteReason;
    protected String endActivityId;
    protected String startActivityId;
    protected String tenantId;
    protected String state;
    
    public String getEndActivityId() {
        return this.endActivityId;
    }
    
    public void setEndActivityId(final String endActivityId) {
        this.endActivityId = endActivityId;
    }
    
    public String getStartActivityId() {
        return this.startActivityId;
    }
    
    public void setStartActivityId(final String startActivityId) {
        this.startActivityId = startActivityId;
    }
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public void setBusinessKey(final String businessKey) {
        this.businessKey = businessKey;
    }
    
    public String getStartUserId() {
        return this.startUserId;
    }
    
    public void setStartUserId(final String startUserId) {
        this.startUserId = startUserId;
    }
    
    public String getSuperProcessInstanceId() {
        return this.superProcessInstanceId;
    }
    
    public void setSuperProcessInstanceId(final String superProcessInstanceId) {
        this.superProcessInstanceId = superProcessInstanceId;
    }
    
    public String getSuperCaseInstanceId() {
        return this.superCaseInstanceId;
    }
    
    public void setSuperCaseInstanceId(final String superCaseInstanceId) {
        this.superCaseInstanceId = superCaseInstanceId;
    }
    
    public String getDeleteReason() {
        return this.deleteReason;
    }
    
    public void setDeleteReason(final String deleteReason) {
        this.deleteReason = deleteReason;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getState() {
        return this.state;
    }
    
    public void setState(final String state) {
        this.state = state;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[businessKey=" + this.businessKey + ", startUserId=" + this.startUserId + ", superProcessInstanceId=" + this.superProcessInstanceId + ", rootProcessInstanceId=" + this.rootProcessInstanceId + ", superCaseInstanceId=" + this.superCaseInstanceId + ", deleteReason=" + this.deleteReason + ", durationInMillis=" + this.durationInMillis + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", removalTime=" + this.removalTime + ", endActivityId=" + this.endActivityId + ", startActivityId=" + this.startActivityId + ", id=" + this.id + ", eventType=" + this.eventType + ", executionId=" + this.executionId + ", processDefinitionId=" + this.processDefinitionId + ", processInstanceId=" + this.processInstanceId + ", tenantId=" + this.tenantId + "]";
    }
}
