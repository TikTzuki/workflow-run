// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Date;

public class HistoricDetailEventEntity extends HistoryEvent
{
    private static final long serialVersionUID = 1L;
    protected String activityInstanceId;
    protected String taskId;
    protected Date timestamp;
    protected String tenantId;
    protected String userOperationId;
    
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    public void setActivityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }
    
    public String getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }
    
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getUserOperationId() {
        return this.userOperationId;
    }
    
    public void setUserOperationId(final String userOperationId) {
        this.userOperationId = userOperationId;
    }
    
    @Override
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    @Override
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    public void delete() {
        Context.getCommandContext().getDbEntityManager().delete(this);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[activityInstanceId=" + this.activityInstanceId + ", taskId=" + this.taskId + ", timestamp=" + this.timestamp + ", eventType=" + this.eventType + ", executionId=" + this.executionId + ", processDefinitionId=" + this.processDefinitionId + ", rootProcessInstanceId=" + this.rootProcessInstanceId + ", removalTime=" + this.removalTime + ", processInstanceId=" + this.processInstanceId + ", id=" + this.id + ", tenantId=" + this.tenantId + ", userOperationId=" + this.userOperationId + "]";
    }
}
