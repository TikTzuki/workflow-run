// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.HistoricTaskInstanceReportResult;

public class TaskReportResultEntity implements HistoricTaskInstanceReportResult
{
    protected Long count;
    protected String processDefinitionKey;
    protected String processDefinitionId;
    protected String processDefinitionName;
    protected String taskName;
    protected String tenantId;
    
    @Override
    public Long getCount() {
        return this.count;
    }
    
    public void setCount(final Long count) {
        this.count = count;
    }
    
    @Override
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public void setProcessDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public String getProcessDefinitionName() {
        return this.processDefinitionName;
    }
    
    public void setProcessDefinitionName(final String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }
    
    @Override
    public String getTaskName() {
        return this.taskName;
    }
    
    public void setTaskName(final String taskName) {
        this.taskName = taskName;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[count=" + this.count + ", processDefinitionKey='" + this.processDefinitionKey + '\'' + ", processDefinitionId='" + this.processDefinitionId + '\'' + ", processDefinitionName='" + this.processDefinitionName + '\'' + ", taskName='" + this.taskName + '\'' + ", tenantId='" + this.tenantId + '\'' + ']';
    }
}
