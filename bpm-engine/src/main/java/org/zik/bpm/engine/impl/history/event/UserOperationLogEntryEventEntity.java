// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.history.event;

import java.util.Date;
import org.zik.bpm.engine.history.UserOperationLogEntry;

public class UserOperationLogEntryEventEntity extends HistoryEvent implements UserOperationLogEntry
{
    private static final long serialVersionUID = 1L;
    protected String operationId;
    protected String operationType;
    protected String jobId;
    protected String jobDefinitionId;
    protected String taskId;
    protected String userId;
    protected Date timestamp;
    protected String property;
    protected String orgValue;
    protected String newValue;
    protected String entityType;
    protected String deploymentId;
    protected String tenantId;
    protected String batchId;
    protected String category;
    protected String externalTaskId;
    protected String annotation;
    
    @Override
    public String getOperationId() {
        return this.operationId;
    }
    
    @Override
    public String getOperationType() {
        return this.operationType;
    }
    
    @Override
    public String getTaskId() {
        return this.taskId;
    }
    
    @Override
    public String getUserId() {
        return this.userId;
    }
    
    @Override
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    @Override
    public String getProperty() {
        return this.property;
    }
    
    @Override
    public String getOrgValue() {
        return this.orgValue;
    }
    
    @Override
    public String getNewValue() {
        return this.newValue;
    }
    
    public void setOperationId(final String operationId) {
        this.operationId = operationId;
    }
    
    public void setOperationType(final String operationType) {
        this.operationType = operationType;
    }
    
    public void setTaskId(final String taskId) {
        this.taskId = taskId;
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    public void setTimestamp(final Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setProperty(final String property) {
        this.property = property;
    }
    
    public void setOrgValue(final String orgValue) {
        this.orgValue = orgValue;
    }
    
    public void setNewValue(final String newValue) {
        this.newValue = newValue;
    }
    
    @Override
    public String getEntityType() {
        return this.entityType;
    }
    
    public void setEntityType(final String entityType) {
        this.entityType = entityType;
    }
    
    @Override
    public String getJobId() {
        return this.jobId;
    }
    
    public void setJobId(final String jobId) {
        this.jobId = jobId;
    }
    
    @Override
    public String getJobDefinitionId() {
        return this.jobDefinitionId;
    }
    
    public void setJobDefinitionId(final String jobDefinitionId) {
        this.jobDefinitionId = jobDefinitionId;
    }
    
    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getBatchId() {
        return this.batchId;
    }
    
    public void setBatchId(final String batchId) {
        this.batchId = batchId;
    }
    
    @Override
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(final String category) {
        this.category = category;
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
    public String getExternalTaskId() {
        return this.externalTaskId;
    }
    
    public void setExternalTaskId(final String externalTaskId) {
        this.externalTaskId = externalTaskId;
    }
    
    @Override
    public String getAnnotation() {
        return this.annotation;
    }
    
    public void setAnnotation(final String annotation) {
        this.annotation = annotation;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[taskId" + this.taskId + ", deploymentId" + this.deploymentId + ", processDefinitionKey =" + this.processDefinitionKey + ", jobId = " + this.jobId + ", jobDefinitionId = " + this.jobDefinitionId + ", batchId = " + this.batchId + ", operationId =" + this.operationId + ", operationType =" + this.operationType + ", userId =" + this.userId + ", timestamp =" + this.timestamp + ", property =" + this.property + ", orgValue =" + this.orgValue + ", newValue =" + this.newValue + ", id=" + this.id + ", eventType=" + this.eventType + ", executionId=" + this.executionId + ", processDefinitionId=" + this.processDefinitionId + ", rootProcessInstanceId=" + this.rootProcessInstanceId + ", processInstanceId=" + this.processInstanceId + ", externalTaskId=" + this.externalTaskId + ", tenantId=" + this.tenantId + ", entityType=" + this.entityType + ", category=" + this.category + ", annotation=" + this.annotation + "]";
    }
}
