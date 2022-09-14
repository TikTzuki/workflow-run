// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import java.util.Date;

public class HistoricTaskInstanceEventEntity extends HistoricScopeInstanceEvent
{
    private static final long serialVersionUID = 1L;
    protected String taskId;
    protected String assignee;
    protected String owner;
    protected String name;
    protected String description;
    protected Date dueDate;
    protected Date followUpDate;
    protected int priority;
    protected String parentTaskId;
    protected String deleteReason;
    protected String taskDefinitionKey;
    protected String activityInstanceId;
    protected String tenantId;
    
    public String getDeleteReason() {
        return this.deleteReason;
    }
    
    public String getAssignee() {
        return this.assignee;
    }
    
    public void setAssignee(final String assignee) {
        this.assignee = assignee;
    }
    
    public String getOwner() {
        return this.owner;
    }
    
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    public Date getDueDate() {
        return this.dueDate;
    }
    
    public void setDueDate(final Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public Date getFollowUpDate() {
        return this.followUpDate;
    }
    
    public void setFollowUpDate(final Date followUpDate) {
        this.followUpDate = followUpDate;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public void setPriority(final int priority) {
        this.priority = priority;
    }
    
    public String getParentTaskId() {
        return this.parentTaskId;
    }
    
    public void setParentTaskId(final String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }
    
    public void setDeleteReason(final String deleteReason) {
        this.deleteReason = deleteReason;
    }
    
    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }
    
    public String getTaskId() {
        return this.taskId;
    }
    
    public String getTaskDefinitionKey() {
        return this.taskDefinitionKey;
    }
    
    public void setTaskDefinitionKey(final String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }
    
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    public void setActivityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
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
        return this.getClass().getSimpleName() + "[taskId" + this.taskId + ", assignee=" + this.assignee + ", owner=" + this.owner + ", name=" + this.name + ", description=" + this.description + ", dueDate=" + this.dueDate + ", followUpDate=" + this.followUpDate + ", priority=" + this.priority + ", parentTaskId=" + this.parentTaskId + ", deleteReason=" + this.deleteReason + ", taskDefinitionKey=" + this.taskDefinitionKey + ", durationInMillis=" + this.durationInMillis + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", id=" + this.id + ", eventType=" + this.eventType + ", executionId=" + this.executionId + ", processDefinitionId=" + this.processDefinitionId + ", rootProcessInstanceId=" + this.rootProcessInstanceId + ", processInstanceId=" + this.processInstanceId + ", activityInstanceId=" + this.activityInstanceId + ", tenantId=" + this.tenantId + "]";
    }
}
