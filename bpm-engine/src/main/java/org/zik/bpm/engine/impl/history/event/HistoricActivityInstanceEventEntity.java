// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.impl.pvm.runtime.ActivityInstanceState;

public class HistoricActivityInstanceEventEntity extends HistoricScopeInstanceEvent
{
    private static final long serialVersionUID = 1L;
    protected String activityId;
    protected String activityName;
    protected String activityType;
    protected String activityInstanceId;
    protected int activityInstanceState;
    protected String parentActivityInstanceId;
    protected String calledProcessInstanceId;
    protected String calledCaseInstanceId;
    protected String taskId;
    protected String taskAssignee;
    protected String tenantId;
    
    public String getAssignee() {
        return this.taskAssignee;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    public String getActivityType() {
        return this.activityType;
    }
    
    public void setActivityType(final String activityType) {
        this.activityType = activityType;
    }
    
    public String getActivityName() {
        return this.activityName;
    }
    
    public void setActivityName(final String activityName) {
        this.activityName = activityName;
    }
    
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    public void setActivityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }
    
    public String getParentActivityInstanceId() {
        return this.parentActivityInstanceId;
    }
    
    public void setParentActivityInstanceId(final String parentActivityInstanceId) {
        this.parentActivityInstanceId = parentActivityInstanceId;
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
    
    public String getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }
    
    public String getTaskAssignee() {
        return this.taskAssignee;
    }
    
    public void setTaskAssignee(final String taskAssignee) {
        this.taskAssignee = taskAssignee;
    }
    
    public void setActivityInstanceState(final int activityInstanceState) {
        this.activityInstanceState = activityInstanceState;
    }
    
    public int getActivityInstanceState() {
        return this.activityInstanceState;
    }
    
    public boolean isCompleteScope() {
        return ActivityInstanceState.SCOPE_COMPLETE.getStateCode() == this.activityInstanceState;
    }
    
    public boolean isCanceled() {
        return ActivityInstanceState.CANCELED.getStateCode() == this.activityInstanceState;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    @Override
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[activityId=" + this.activityId + ", activityName=" + this.activityName + ", activityType=" + this.activityType + ", activityInstanceId=" + this.activityInstanceId + ", activityInstanceState=" + this.activityInstanceState + ", parentActivityInstanceId=" + this.parentActivityInstanceId + ", calledProcessInstanceId=" + this.calledProcessInstanceId + ", calledCaseInstanceId=" + this.calledCaseInstanceId + ", taskId=" + this.taskId + ", taskAssignee=" + this.taskAssignee + ", durationInMillis=" + this.durationInMillis + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", eventType=" + this.eventType + ", executionId=" + this.executionId + ", processDefinitionId=" + this.processDefinitionId + ", rootProcessInstanceId=" + this.rootProcessInstanceId + ", processInstanceId=" + this.processInstanceId + ", tenantId=" + this.tenantId + "]";
    }
}
