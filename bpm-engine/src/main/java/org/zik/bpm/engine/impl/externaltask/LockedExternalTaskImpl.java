// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.externaltask;

import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import java.util.Collections;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import java.util.Collection;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskEntity;
import org.camunda.bpm.engine.variable.VariableMap;
import java.util.Map;
import org.camunda.bpm.engine.variable.impl.VariableMapImpl;
import java.util.Date;
import org.zik.bpm.engine.externaltask.LockedExternalTask;

public class LockedExternalTaskImpl implements LockedExternalTask
{
    protected String id;
    protected String topicName;
    protected String workerId;
    protected Date lockExpirationTime;
    protected Integer retries;
    protected String errorMessage;
    protected String errorDetails;
    protected String processInstanceId;
    protected String executionId;
    protected String activityId;
    protected String activityInstanceId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String processDefinitionVersionTag;
    protected String tenantId;
    protected long priority;
    protected VariableMapImpl variables;
    protected String businessKey;
    protected Map<String, String> extensionProperties;
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public String getTopicName() {
        return this.topicName;
    }
    
    @Override
    public String getWorkerId() {
        return this.workerId;
    }
    
    @Override
    public Date getLockExpirationTime() {
        return this.lockExpirationTime;
    }
    
    @Override
    public Integer getRetries() {
        return this.retries;
    }
    
    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    @Override
    public String getExecutionId() {
        return this.executionId;
    }
    
    @Override
    public String getActivityId() {
        return this.activityId;
    }
    
    @Override
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    @Override
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    @Override
    public String getProcessDefinitionVersionTag() {
        return this.processDefinitionVersionTag;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    @Override
    public VariableMap getVariables() {
        return (VariableMap)this.variables;
    }
    
    @Override
    public String getErrorDetails() {
        return this.errorDetails;
    }
    
    @Override
    public long getPriority() {
        return this.priority;
    }
    
    @Override
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    @Override
    public Map<String, String> getExtensionProperties() {
        return this.extensionProperties;
    }
    
    public static LockedExternalTaskImpl fromEntity(final ExternalTaskEntity externalTaskEntity, final List<String> variablesToFetch, final boolean isLocal, final boolean deserializeVariables, final boolean includeExtensionProperties) {
        final LockedExternalTaskImpl result = new LockedExternalTaskImpl();
        result.id = externalTaskEntity.getId();
        result.topicName = externalTaskEntity.getTopicName();
        result.workerId = externalTaskEntity.getWorkerId();
        result.lockExpirationTime = externalTaskEntity.getLockExpirationTime();
        result.retries = externalTaskEntity.getRetries();
        result.errorMessage = externalTaskEntity.getErrorMessage();
        result.errorDetails = externalTaskEntity.getErrorDetails();
        result.processInstanceId = externalTaskEntity.getProcessInstanceId();
        result.executionId = externalTaskEntity.getExecutionId();
        result.activityId = externalTaskEntity.getActivityId();
        result.activityInstanceId = externalTaskEntity.getActivityInstanceId();
        result.processDefinitionId = externalTaskEntity.getProcessDefinitionId();
        result.processDefinitionKey = externalTaskEntity.getProcessDefinitionKey();
        result.processDefinitionVersionTag = externalTaskEntity.getProcessDefinitionVersionTag();
        result.tenantId = externalTaskEntity.getTenantId();
        result.priority = externalTaskEntity.getPriority();
        result.businessKey = externalTaskEntity.getBusinessKey();
        final ExecutionEntity execution = externalTaskEntity.getExecution();
        execution.collectVariables(result.variables = new VariableMapImpl(), variablesToFetch, isLocal, deserializeVariables);
        if (includeExtensionProperties) {
            result.extensionProperties = (Map<String, String>)execution.getActivity().getProperty(BpmnProperties.EXTENSION_PROPERTIES.getName());
        }
        if (result.extensionProperties == null) {
            result.extensionProperties = Collections.emptyMap();
        }
        return result;
    }
}
