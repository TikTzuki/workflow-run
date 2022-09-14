// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import java.util.Date;

public class HistoricCaseInstanceEventEntity extends HistoricScopeInstanceEvent
{
    private static final long serialVersionUID = 1L;
    protected String businessKey;
    protected String createUserId;
    protected int state;
    protected String superCaseInstanceId;
    protected String superProcessInstanceId;
    protected String tenantId;
    
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public void setBusinessKey(final String businessKey) {
        this.businessKey = businessKey;
    }
    
    public Date getCreateTime() {
        return this.getStartTime();
    }
    
    public void setCreateTime(final Date createTime) {
        this.setStartTime(createTime);
    }
    
    public Date getCloseTime() {
        return this.getEndTime();
    }
    
    public void setCloseTime(final Date closeTime) {
        this.setEndTime(closeTime);
    }
    
    public String getCreateUserId() {
        return this.createUserId;
    }
    
    public void setCreateUserId(final String userId) {
        this.createUserId = userId;
    }
    
    public String getSuperCaseInstanceId() {
        return this.superCaseInstanceId;
    }
    
    public void setSuperCaseInstanceId(final String superCaseInstanceId) {
        this.superCaseInstanceId = superCaseInstanceId;
    }
    
    public String getSuperProcessInstanceId() {
        return this.superProcessInstanceId;
    }
    
    public void setSuperProcessInstanceId(final String superProcessInstanceId) {
        this.superProcessInstanceId = superProcessInstanceId;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public int getState() {
        return this.state;
    }
    
    public void setState(final int state) {
        this.state = state;
    }
    
    public boolean isActive() {
        return this.state == CaseExecutionState.ACTIVE.getStateCode();
    }
    
    public boolean isCompleted() {
        return this.state == CaseExecutionState.COMPLETED.getStateCode();
    }
    
    public boolean isTerminated() {
        return this.state == CaseExecutionState.TERMINATED.getStateCode();
    }
    
    public boolean isFailed() {
        return this.state == CaseExecutionState.FAILED.getStateCode();
    }
    
    public boolean isSuspended() {
        return this.state == CaseExecutionState.SUSPENDED.getStateCode();
    }
    
    public boolean isClosed() {
        return this.state == CaseExecutionState.CLOSED.getStateCode();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[businessKey=" + this.businessKey + ", startUserId=" + this.createUserId + ", superCaseInstanceId=" + this.superCaseInstanceId + ", superProcessInstanceId=" + this.superProcessInstanceId + ", durationInMillis=" + this.durationInMillis + ", createTime=" + this.startTime + ", closeTime=" + this.endTime + ", id=" + this.id + ", eventType=" + this.eventType + ", caseExecutionId=" + this.caseExecutionId + ", caseDefinitionId=" + this.caseDefinitionId + ", caseInstanceId=" + this.caseInstanceId + ", tenantId=" + this.tenantId + "]";
    }
}
