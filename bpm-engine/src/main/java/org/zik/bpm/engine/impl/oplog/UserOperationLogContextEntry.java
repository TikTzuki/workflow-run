// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.oplog;

import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.List;

public class UserOperationLogContextEntry
{
    protected String deploymentId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String processInstanceId;
    protected String executionId;
    protected String caseDefinitionId;
    protected String caseInstanceId;
    protected String caseExecutionId;
    protected String taskId;
    protected String operationType;
    protected String entityType;
    protected List<PropertyChange> propertyChanges;
    protected String jobDefinitionId;
    protected String jobId;
    protected String batchId;
    protected String category;
    protected String rootProcessInstanceId;
    protected String externalTaskId;
    protected String annotation;
    
    public UserOperationLogContextEntry(final String operationType, final String entityType) {
        this.operationType = operationType;
        this.entityType = entityType;
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }
    
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
    
    public void setCaseDefinitionId(final String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }
    
    public String getCaseInstanceId() {
        return this.caseInstanceId;
    }
    
    public void setCaseInstanceId(final String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }
    
    public String getCaseExecutionId() {
        return this.caseExecutionId;
    }
    
    public void setCaseExecutionId(final String caseExecutionId) {
        this.caseExecutionId = caseExecutionId;
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
    
    public String getEntityType() {
        return this.entityType;
    }
    
    public void setEntityType(final String entityType) {
        this.entityType = entityType;
    }
    
    public List<PropertyChange> getPropertyChanges() {
        return this.propertyChanges;
    }
    
    public void setPropertyChanges(final List<PropertyChange> propertyChanges) {
        this.propertyChanges = propertyChanges;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public void setProcessDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    public String getJobDefinitionId() {
        return this.jobDefinitionId;
    }
    
    public void setJobDefinitionId(final String jobDefinitionId) {
        this.jobDefinitionId = jobDefinitionId;
    }
    
    public String getJobId() {
        return this.jobId;
    }
    
    public void setJobId(final String jobId) {
        this.jobId = jobId;
    }
    
    public String getBatchId() {
        return this.batchId;
    }
    
    public void setBatchId(final String batchId) {
        this.batchId = batchId;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(final String category) {
        this.category = category;
    }
    
    public String getRootProcessInstanceId() {
        return this.rootProcessInstanceId;
    }
    
    public void setRootProcessInstanceId(final String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }
    
    public String getExternalTaskId() {
        return this.externalTaskId;
    }
    
    public void setExternalTaskId(final String externalTaskId) {
        this.externalTaskId = externalTaskId;
    }
    
    public String getAnnotation() {
        return this.annotation;
    }
    
    public void setAnnotation(final String annotation) {
        this.annotation = annotation;
    }
}
