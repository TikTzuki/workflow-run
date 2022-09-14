// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.oplog;

import java.util.ArrayList;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.zik.bpm.engine.impl.repository.ResourceDefinitionEntity;
import org.zik.bpm.engine.impl.history.event.HistoryEvent;
import org.zik.bpm.engine.history.HistoricTaskInstance;
import java.util.Arrays;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;

public class UserOperationLogContextEntryBuilder
{
    protected UserOperationLogContextEntry entry;
    
    public static UserOperationLogContextEntryBuilder entry(final String operationType, final String entityType) {
        final UserOperationLogContextEntryBuilder builder = new UserOperationLogContextEntryBuilder();
        builder.entry = new UserOperationLogContextEntry(operationType, entityType);
        return builder;
    }
    
    public UserOperationLogContextEntryBuilder inContextOf(final JobEntity job) {
        this.entry.setJobDefinitionId(job.getJobDefinitionId());
        this.entry.setProcessInstanceId(job.getProcessInstanceId());
        this.entry.setProcessDefinitionId(job.getProcessDefinitionId());
        this.entry.setProcessDefinitionKey(job.getProcessDefinitionKey());
        this.entry.setDeploymentId(job.getDeploymentId());
        final ExecutionEntity execution = job.getExecution();
        if (execution != null) {
            this.entry.setRootProcessInstanceId(execution.getRootProcessInstanceId());
        }
        return this;
    }
    
    public UserOperationLogContextEntryBuilder inContextOf(final JobDefinitionEntity jobDefinition) {
        this.entry.setJobDefinitionId(jobDefinition.getId());
        this.entry.setProcessDefinitionId(jobDefinition.getProcessDefinitionId());
        this.entry.setProcessDefinitionKey(jobDefinition.getProcessDefinitionKey());
        if (jobDefinition.getProcessDefinitionId() != null) {
            final ProcessDefinitionEntity processDefinition = Context.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(jobDefinition.getProcessDefinitionId());
            this.entry.setDeploymentId(processDefinition.getDeploymentId());
        }
        return this;
    }
    
    public UserOperationLogContextEntryBuilder inContextOf(final ExecutionEntity execution) {
        this.entry.setProcessInstanceId(execution.getProcessInstanceId());
        this.entry.setRootProcessInstanceId(execution.getRootProcessInstanceId());
        this.entry.setProcessDefinitionId(execution.getProcessDefinitionId());
        final ProcessDefinitionEntity processDefinition = execution.getProcessDefinition();
        this.entry.setProcessDefinitionKey(processDefinition.getKey());
        this.entry.setDeploymentId(processDefinition.getDeploymentId());
        return this;
    }
    
    public UserOperationLogContextEntryBuilder inContextOf(final ProcessDefinitionEntity processDefinition) {
        this.entry.setProcessDefinitionId(processDefinition.getId());
        this.entry.setProcessDefinitionKey(processDefinition.getKey());
        this.entry.setDeploymentId(processDefinition.getDeploymentId());
        return this;
    }
    
    public UserOperationLogContextEntryBuilder inContextOf(final TaskEntity task, List<PropertyChange> propertyChanges) {
        if ((propertyChanges == null || propertyChanges.isEmpty()) && "Create".equals(this.entry.getOperationType())) {
            propertyChanges = Arrays.asList(PropertyChange.EMPTY_CHANGE);
        }
        this.entry.setPropertyChanges(propertyChanges);
        final ProcessDefinitionEntity definition = task.getProcessDefinition();
        if (definition != null) {
            this.entry.setProcessDefinitionKey(definition.getKey());
            this.entry.setDeploymentId(definition.getDeploymentId());
        }
        else if (task.getCaseDefinitionId() != null) {
            this.entry.setDeploymentId(task.getCaseDefinition().getDeploymentId());
        }
        this.entry.setProcessDefinitionId(task.getProcessDefinitionId());
        this.entry.setProcessInstanceId(task.getProcessInstanceId());
        this.entry.setExecutionId(task.getExecutionId());
        this.entry.setCaseDefinitionId(task.getCaseDefinitionId());
        this.entry.setCaseInstanceId(task.getCaseInstanceId());
        this.entry.setCaseExecutionId(task.getCaseExecutionId());
        this.entry.setTaskId(task.getId());
        final ExecutionEntity execution = task.getExecution();
        if (execution != null) {
            this.entry.setRootProcessInstanceId(execution.getRootProcessInstanceId());
        }
        return this;
    }
    
    public UserOperationLogContextEntryBuilder inContextOf(final HistoricTaskInstance task, List<PropertyChange> propertyChanges) {
        if ((propertyChanges == null || propertyChanges.isEmpty()) && "Create".equals(this.entry.getOperationType())) {
            propertyChanges = Arrays.asList(PropertyChange.EMPTY_CHANGE);
        }
        this.entry.setPropertyChanges(propertyChanges);
        this.entry.setProcessDefinitionKey(task.getProcessDefinitionKey());
        this.entry.setProcessDefinitionId(task.getProcessDefinitionId());
        this.entry.setProcessInstanceId(task.getProcessInstanceId());
        this.entry.setExecutionId(task.getExecutionId());
        this.entry.setCaseDefinitionId(task.getCaseDefinitionId());
        this.entry.setCaseInstanceId(task.getCaseInstanceId());
        this.entry.setCaseExecutionId(task.getCaseExecutionId());
        this.entry.setTaskId(task.getId());
        this.entry.setRootProcessInstanceId(task.getRootProcessInstanceId());
        return this;
    }
    
    public UserOperationLogContextEntryBuilder inContextOf(final ExecutionEntity processInstance, List<PropertyChange> propertyChanges) {
        if ((propertyChanges == null || propertyChanges.isEmpty()) && "Create".equals(this.entry.getOperationType())) {
            propertyChanges = Arrays.asList(PropertyChange.EMPTY_CHANGE);
        }
        this.entry.setPropertyChanges(propertyChanges);
        this.entry.setRootProcessInstanceId(processInstance.getRootProcessInstanceId());
        this.entry.setProcessInstanceId(processInstance.getProcessInstanceId());
        this.entry.setProcessDefinitionId(processInstance.getProcessDefinitionId());
        this.entry.setExecutionId(processInstance.getId());
        this.entry.setCaseInstanceId(processInstance.getCaseInstanceId());
        final ProcessDefinitionEntity definition = processInstance.getProcessDefinition();
        if (definition != null) {
            this.entry.setProcessDefinitionKey(definition.getKey());
            this.entry.setDeploymentId(definition.getDeploymentId());
        }
        return this;
    }
    
    public UserOperationLogContextEntryBuilder inContextOf(final HistoryEvent historyEvent, final ResourceDefinitionEntity<?> definition, List<PropertyChange> propertyChanges) {
        if ((propertyChanges == null || propertyChanges.isEmpty()) && "Create".equals(this.entry.getOperationType())) {
            propertyChanges = Arrays.asList(PropertyChange.EMPTY_CHANGE);
        }
        this.entry.setPropertyChanges(propertyChanges);
        this.entry.setRootProcessInstanceId(historyEvent.getRootProcessInstanceId());
        this.entry.setProcessDefinitionId(historyEvent.getProcessDefinitionId());
        this.entry.setProcessInstanceId(historyEvent.getProcessInstanceId());
        this.entry.setExecutionId(historyEvent.getExecutionId());
        this.entry.setCaseDefinitionId(historyEvent.getCaseDefinitionId());
        this.entry.setCaseInstanceId(historyEvent.getCaseInstanceId());
        this.entry.setCaseExecutionId(historyEvent.getCaseExecutionId());
        if (definition != null) {
            if (definition instanceof ProcessDefinitionEntity) {
                this.entry.setProcessDefinitionKey(definition.getKey());
            }
            this.entry.setDeploymentId(definition.getDeploymentId());
        }
        return this;
    }
    
    public UserOperationLogContextEntryBuilder inContextOf(final HistoricVariableInstanceEntity variable, final ResourceDefinitionEntity<?> definition, List<PropertyChange> propertyChanges) {
        if ((propertyChanges == null || propertyChanges.isEmpty()) && "Create".equals(this.entry.getOperationType())) {
            propertyChanges = Arrays.asList(PropertyChange.EMPTY_CHANGE);
        }
        this.entry.setPropertyChanges(propertyChanges);
        this.entry.setRootProcessInstanceId(variable.getRootProcessInstanceId());
        this.entry.setProcessDefinitionId(variable.getProcessDefinitionId());
        this.entry.setProcessInstanceId(variable.getProcessInstanceId());
        this.entry.setExecutionId(variable.getExecutionId());
        this.entry.setCaseDefinitionId(variable.getCaseDefinitionId());
        this.entry.setCaseInstanceId(variable.getCaseInstanceId());
        this.entry.setCaseExecutionId(variable.getCaseExecutionId());
        this.entry.setTaskId(variable.getTaskId());
        if (definition != null) {
            if (definition instanceof ProcessDefinitionEntity) {
                this.entry.setProcessDefinitionKey(definition.getKey());
            }
            this.entry.setDeploymentId(definition.getDeploymentId());
        }
        return this;
    }
    
    public UserOperationLogContextEntryBuilder inContextOf(final ExternalTaskEntity task, final ExecutionEntity execution, final ProcessDefinitionEntity definition) {
        if (execution != null) {
            this.inContextOf(execution);
        }
        else if (definition != null) {
            this.inContextOf(definition);
        }
        this.entry.setExternalTaskId(task.getId());
        return this;
    }
    
    public UserOperationLogContextEntryBuilder propertyChanges(final List<PropertyChange> propertyChanges) {
        this.entry.setPropertyChanges(propertyChanges);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder propertyChanges(final PropertyChange propertyChange) {
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(propertyChange);
        this.entry.setPropertyChanges(propertyChanges);
        return this;
    }
    
    public UserOperationLogContextEntry create() {
        return this.entry;
    }
    
    public UserOperationLogContextEntryBuilder jobId(final String jobId) {
        this.entry.setJobId(jobId);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder jobDefinitionId(final String jobDefinitionId) {
        this.entry.setJobDefinitionId(jobDefinitionId);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder processDefinitionId(final String processDefinitionId) {
        this.entry.setProcessDefinitionId(processDefinitionId);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder processDefinitionKey(final String processDefinitionKey) {
        this.entry.setProcessDefinitionKey(processDefinitionKey);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder processInstanceId(final String processInstanceId) {
        this.entry.setProcessInstanceId(processInstanceId);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder caseDefinitionId(final String caseDefinitionId) {
        this.entry.setCaseDefinitionId(caseDefinitionId);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder deploymentId(final String deploymentId) {
        this.entry.setDeploymentId(deploymentId);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder batchId(final String batchId) {
        this.entry.setBatchId(batchId);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder taskId(final String taskId) {
        this.entry.setTaskId(taskId);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder caseInstanceId(final String caseInstanceId) {
        this.entry.setCaseInstanceId(caseInstanceId);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder category(final String category) {
        this.entry.setCategory(category);
        return this;
    }
    
    public UserOperationLogContextEntryBuilder annotation(final String annotation) {
        this.entry.setAnnotation(annotation);
        return this;
    }
}
