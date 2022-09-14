// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.impl.cmmn.execution.CaseExecutionState;
import java.util.Date;

public class HistoricCaseActivityInstanceEventEntity extends HistoricScopeInstanceEvent
{
    private static final long serialVersionUID = 1L;
    protected String caseActivityId;
    protected String caseActivityName;
    protected String caseActivityType;
    protected int caseActivityInstanceState;
    protected String parentCaseActivityInstanceId;
    protected String taskId;
    protected String calledProcessInstanceId;
    protected String calledCaseInstanceId;
    protected String tenantId;
    protected boolean required;
    
    public HistoricCaseActivityInstanceEventEntity() {
        this.required = false;
    }
    
    @Override
    public String getCaseExecutionId() {
        return this.getId();
    }
    
    public String getCaseActivityId() {
        return this.caseActivityId;
    }
    
    public void setCaseActivityId(final String caseActivityId) {
        this.caseActivityId = caseActivityId;
    }
    
    public String getCaseActivityName() {
        return this.caseActivityName;
    }
    
    public void setCaseActivityName(final String caseActivityName) {
        this.caseActivityName = caseActivityName;
    }
    
    public String getCaseActivityType() {
        return this.caseActivityType;
    }
    
    public void setCaseActivityType(final String caseActivityType) {
        this.caseActivityType = caseActivityType;
    }
    
    public int getCaseActivityInstanceState() {
        return this.caseActivityInstanceState;
    }
    
    public void setCaseActivityInstanceState(final int caseActivityInstanceState) {
        this.caseActivityInstanceState = caseActivityInstanceState;
    }
    
    public String getParentCaseActivityInstanceId() {
        return this.parentCaseActivityInstanceId;
    }
    
    public void setParentCaseActivityInstanceId(final String parentCaseActivityInstanceId) {
        this.parentCaseActivityInstanceId = parentCaseActivityInstanceId;
    }
    
    public String getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }
    
    public String getCalledProcessInstanceId() {
        return this.calledProcessInstanceId;
    }
    
    public void setCalledProcessInstanceId(final String calledProcessInstanceId) {
        this.calledProcessInstanceId = calledProcessInstanceId;
    }
    
    public String getCalledCaseInstanceId() {
        return this.calledCaseInstanceId;
    }
    
    public void setCalledCaseInstanceId(final String calledCaseInstanceId) {
        this.calledCaseInstanceId = calledCaseInstanceId;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public Date getCreateTime() {
        return this.startTime;
    }
    
    public void setCreateTime(final Date createTime) {
        this.setStartTime(createTime);
    }
    
    public boolean isRequired() {
        return this.required;
    }
    
    public void setRequired(final boolean required) {
        this.required = required;
    }
    
    public boolean isAvailable() {
        return this.caseActivityInstanceState == CaseExecutionState.AVAILABLE.getStateCode();
    }
    
    public boolean isEnabled() {
        return this.caseActivityInstanceState == CaseExecutionState.ENABLED.getStateCode();
    }
    
    public boolean isDisabled() {
        return this.caseActivityInstanceState == CaseExecutionState.DISABLED.getStateCode();
    }
    
    public boolean isActive() {
        return this.caseActivityInstanceState == CaseExecutionState.ACTIVE.getStateCode();
    }
    
    public boolean isSuspended() {
        return this.caseActivityInstanceState == CaseExecutionState.SUSPENDED.getStateCode();
    }
    
    public boolean isCompleted() {
        return this.caseActivityInstanceState == CaseExecutionState.COMPLETED.getStateCode();
    }
    
    public boolean isTerminated() {
        return this.caseActivityInstanceState == CaseExecutionState.TERMINATED.getStateCode();
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[caseActivityId=" + this.caseActivityId + ", caseActivityName=" + this.caseActivityName + ", caseActivityInstanceId=" + this.id + ", caseActivityInstanceState=" + this.caseActivityInstanceState + ", parentCaseActivityInstanceId=" + this.parentCaseActivityInstanceId + ", taskId=" + this.taskId + ", calledProcessInstanceId=" + this.calledProcessInstanceId + ", calledCaseInstanceId=" + this.calledCaseInstanceId + ", durationInMillis=" + this.durationInMillis + ", createTime=" + this.startTime + ", endTime=" + this.endTime + ", eventType=" + this.eventType + ", caseExecutionId=" + this.caseExecutionId + ", caseDefinitionId=" + this.caseDefinitionId + ", caseInstanceId=" + this.caseInstanceId + ", tenantId=" + this.tenantId + "]";
    }
}
