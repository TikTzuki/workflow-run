// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import java.util.Date;

public class HistoricIdentityLinkLogEventEntity extends HistoryEvent
{
    private static final long serialVersionUID = 1L;
    protected Date time;
    protected String type;
    protected String userId;
    protected String groupId;
    protected String taskId;
    protected String operationType;
    protected String assignerId;
    protected String tenantId;
    
    public Date getTime() {
        return this.time;
    }
    
    public void setTime(final Date time) {
        this.time = time;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    public String getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final String groupId) {
        this.groupId = groupId;
    }
    
    public String getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }
    
    public String getOperationType() {
        return this.operationType;
    }
    
    public void setOperationType(final String operationType) {
        this.operationType = operationType;
    }
    
    public String getAssignerId() {
        return this.assignerId;
    }
    
    public void setAssignerId(final String assignerId) {
        this.assignerId = assignerId;
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
}
