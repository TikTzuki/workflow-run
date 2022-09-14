// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.delegate.VariableScope;
import java.util.Iterator;
import org.zik.bpm.engine.impl.bpmn.parser.CamundaErrorEventDefinition;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnExceptionHandler;
import org.zik.bpm.engine.delegate.BpmnError;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.incident.IncidentContext;
import org.zik.bpm.engine.impl.incident.IncidentHandling;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import org.zik.bpm.engine.impl.context.Context;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.externaltask.ExternalTask;

public class ExternalTaskEntity implements ExternalTask, DbEntity, HasDbRevision, HasDbReferences
{
    protected static final EnginePersistenceLogger LOG;
    private static final String EXCEPTION_NAME = "externalTask.exceptionByteArray";
    public static final int MAX_EXCEPTION_MESSAGE_LENGTH = 666;
    protected String id;
    protected int revision;
    protected String topicName;
    protected String workerId;
    protected Date lockExpirationTime;
    protected Integer retries;
    protected String errorMessage;
    protected ByteArrayEntity errorDetailsByteArray;
    protected String errorDetailsByteArrayId;
    protected int suspensionState;
    protected String executionId;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String processDefinitionVersionTag;
    protected String activityId;
    protected String activityInstanceId;
    protected String tenantId;
    protected long priority;
    protected Map<String, String> extensionProperties;
    protected ExecutionEntity execution;
    protected String businessKey;
    protected String lastFailureLogId;
    
    public ExternalTaskEntity() {
        this.suspensionState = SuspensionState.ACTIVE.getStateCode();
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public String getTopicName() {
        return this.topicName;
    }
    
    public void setTopicName(final String topic) {
        this.topicName = topic;
    }
    
    @Override
    public String getWorkerId() {
        return this.workerId;
    }
    
    public void setWorkerId(final String workerId) {
        this.workerId = workerId;
    }
    
    @Override
    public Date getLockExpirationTime() {
        return this.lockExpirationTime;
    }
    
    public void setLockExpirationTime(final Date lockExpirationTime) {
        this.lockExpirationTime = lockExpirationTime;
    }
    
    @Override
    public String getExecutionId() {
        return this.executionId;
    }
    
    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }
    
    @Override
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public void setProcessDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    @Override
    public String getProcessDefinitionVersionTag() {
        return this.processDefinitionVersionTag;
    }
    
    public void setProcessDefinitionVersionTag(final String processDefinitionVersionTag) {
        this.processDefinitionVersionTag = processDefinitionVersionTag;
    }
    
    @Override
    public String getActivityId() {
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    @Override
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    public void setActivityInstanceId(final String activityInstanceId) {
        this.activityInstanceId = activityInstanceId;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    public int getSuspensionState() {
        return this.suspensionState;
    }
    
    public void setSuspensionState(final int suspensionState) {
        this.suspensionState = suspensionState;
    }
    
    @Override
    public boolean isSuspended() {
        return this.suspensionState == SuspensionState.SUSPENDED.getStateCode();
    }
    
    @Override
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public void setProcessInstanceId(final String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public Integer getRetries() {
        return this.retries;
    }
    
    public void setRetries(final Integer retries) {
        this.retries = retries;
    }
    
    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public boolean areRetriesLeft() {
        return this.retries == null || this.retries > 0;
    }
    
    @Override
    public long getPriority() {
        return this.priority;
    }
    
    public void setPriority(final long priority) {
        this.priority = priority;
    }
    
    @Override
    public String getBusinessKey() {
        return this.businessKey;
    }
    
    public void setBusinessKey(final String businessKey) {
        this.businessKey = businessKey;
    }
    
    @Override
    public Map<String, String> getExtensionProperties() {
        return this.extensionProperties;
    }
    
    public void setExtensionProperties(final Map<String, String> extensionProperties) {
        this.extensionProperties = extensionProperties;
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("topic", this.topicName);
        persistentState.put("workerId", this.workerId);
        persistentState.put("lockExpirationTime", this.lockExpirationTime);
        persistentState.put("retries", this.retries);
        persistentState.put("errorMessage", this.errorMessage);
        persistentState.put("executionId", this.executionId);
        persistentState.put("processInstanceId", this.processInstanceId);
        persistentState.put("processDefinitionId", this.processDefinitionId);
        persistentState.put("processDefinitionKey", this.processDefinitionKey);
        persistentState.put("processDefinitionVersionTag", this.processDefinitionVersionTag);
        persistentState.put("activityId", this.activityId);
        persistentState.put("activityInstanceId", this.activityInstanceId);
        persistentState.put("suspensionState", this.suspensionState);
        persistentState.put("tenantId", this.tenantId);
        persistentState.put("priority", this.priority);
        if (this.errorDetailsByteArrayId != null) {
            persistentState.put("errorDetailsByteArrayId", this.errorDetailsByteArrayId);
        }
        return persistentState;
    }
    
    public void insert() {
        Context.getCommandContext().getExternalTaskManager().insert(this);
        this.getExecution().addExternalTask(this);
    }
    
    public String getErrorDetails() {
        final ByteArrayEntity byteArray = this.getErrorByteArray();
        return ExceptionUtil.getExceptionStacktrace(byteArray);
    }
    
    public void setErrorMessage(final String errorMessage) {
        if (errorMessage != null && errorMessage.length() > 666) {
            this.errorMessage = errorMessage.substring(0, 666);
        }
        else {
            this.errorMessage = errorMessage;
        }
    }
    
    protected void setErrorDetails(final String exception) {
        EnsureUtil.ensureNotNull("exception", (Object)exception);
        final byte[] exceptionBytes = StringUtil.toByteArray(exception);
        ByteArrayEntity byteArray = this.getErrorByteArray();
        if (byteArray == null) {
            byteArray = ExceptionUtil.createExceptionByteArray("externalTask.exceptionByteArray", exceptionBytes, ResourceTypes.RUNTIME);
            this.errorDetailsByteArrayId = byteArray.getId();
            this.errorDetailsByteArray = byteArray;
        }
        else {
            byteArray.setBytes(exceptionBytes);
        }
    }
    
    public String getErrorDetailsByteArrayId() {
        return this.errorDetailsByteArrayId;
    }
    
    protected ByteArrayEntity getErrorByteArray() {
        this.ensureErrorByteArrayInitialized();
        return this.errorDetailsByteArray;
    }
    
    protected void ensureErrorByteArrayInitialized() {
        if (this.errorDetailsByteArray == null && this.errorDetailsByteArrayId != null) {
            this.errorDetailsByteArray = Context.getCommandContext().getDbEntityManager().selectById(ByteArrayEntity.class, this.errorDetailsByteArrayId);
        }
    }
    
    public void delete() {
        this.deleteFromExecutionAndRuntimeTable(false);
        this.produceHistoricExternalTaskDeletedEvent();
    }
    
    protected void deleteFromExecutionAndRuntimeTable(final boolean incidentResolved) {
        this.getExecution().removeExternalTask(this);
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.getExternalTaskManager().delete(this);
        if (this.errorDetailsByteArrayId != null) {
            commandContext.getByteArrayManager().deleteByteArrayById(this.errorDetailsByteArrayId);
        }
        this.removeIncidents(incidentResolved);
    }
    
    protected void removeIncidents(final boolean incidentResolved) {
        final IncidentContext incidentContext = this.createIncidentContext();
        IncidentHandling.removeIncidents("failedExternalTask", incidentContext, incidentResolved);
    }
    
    public void complete(final Map<String, Object> variables, final Map<String, Object> localVariables) {
        this.ensureActive();
        final ExecutionEntity associatedExecution = this.getExecution();
        this.ensureVariablesSet(associatedExecution, variables, localVariables);
        if (this.evaluateThrowBpmnError(associatedExecution, false)) {
            return;
        }
        this.deleteFromExecutionAndRuntimeTable(true);
        this.produceHistoricExternalTaskSuccessfulEvent();
        associatedExecution.signal(null, null);
    }
    
    public void failed(final String errorMessage, final String errorDetails, final int retries, final long retryDuration, final Map<String, Object> variables, final Map<String, Object> localVariables) {
        this.ensureActive();
        final ExecutionEntity associatedExecution = this.getExecution();
        this.ensureVariablesSet(this.execution, variables, localVariables);
        this.setErrorMessage(errorMessage);
        if (errorDetails != null) {
            this.setErrorDetails(errorDetails);
        }
        if (this.evaluateThrowBpmnError(associatedExecution, true)) {
            return;
        }
        this.lockExpirationTime = new Date(ClockUtil.getCurrentTime().getTime() + retryDuration);
        this.produceHistoricExternalTaskFailedEvent();
        this.setRetriesAndManageIncidents(retries);
    }
    
    public void bpmnError(final String errorCode, final String errorMessage, final Map<String, Object> variables) {
        this.ensureActive();
        final ActivityExecution activityExecution = this.getExecution();
        BpmnError bpmnError = null;
        if (errorMessage != null) {
            bpmnError = new BpmnError(errorCode, errorMessage);
        }
        else {
            bpmnError = new BpmnError(errorCode);
        }
        try {
            if (variables != null && !variables.isEmpty()) {
                activityExecution.setVariables(variables);
            }
            BpmnExceptionHandler.propagateBpmnError(bpmnError, activityExecution);
        }
        catch (Exception ex) {
            throw ProcessEngineLogger.CMD_LOGGER.exceptionBpmnErrorPropagationFailed(errorCode, ex);
        }
    }
    
    public void setRetriesAndManageIncidents(final int retries) {
        if (this.areRetriesLeft() && retries <= 0) {
            this.createIncident();
        }
        else if (!this.areRetriesLeft() && retries > 0) {
            this.removeIncidents(true);
        }
        this.setRetries(retries);
    }
    
    protected void createIncident() {
        final IncidentContext incidentContext = this.createIncidentContext();
        incidentContext.setHistoryConfiguration(this.getLastFailureLogId());
        IncidentHandling.createIncident("failedExternalTask", incidentContext, this.errorMessage);
    }
    
    protected IncidentContext createIncidentContext() {
        final IncidentContext context = new IncidentContext();
        context.setProcessDefinitionId(this.processDefinitionId);
        context.setExecutionId(this.executionId);
        context.setActivityId(this.activityId);
        context.setTenantId(this.tenantId);
        context.setConfiguration(this.id);
        return context;
    }
    
    public void lock(final String workerId, final long lockDuration) {
        this.workerId = workerId;
        this.lockExpirationTime = new Date(ClockUtil.getCurrentTime().getTime() + lockDuration);
    }
    
    public ExecutionEntity getExecution() {
        return this.getExecution(true);
    }
    
    public ExecutionEntity getExecution(final boolean validateExistence) {
        this.ensureExecutionInitialized(validateExistence);
        return this.execution;
    }
    
    public void setExecution(final ExecutionEntity execution) {
        this.execution = execution;
    }
    
    protected void ensureExecutionInitialized(final boolean validateExistence) {
        if (this.execution == null) {
            this.execution = Context.getCommandContext().getExecutionManager().findExecutionById(this.executionId);
            if (validateExistence) {
                EnsureUtil.ensureNotNull("Cannot find execution with id " + this.executionId + " for external task " + this.id, "execution", this.execution);
            }
        }
    }
    
    protected void ensureActive() {
        if (this.suspensionState == SuspensionState.SUSPENDED.getStateCode()) {
            throw ExternalTaskEntity.LOG.suspendedEntityException("ExternalTask", this.id);
        }
    }
    
    protected void ensureVariablesSet(final ExecutionEntity execution, final Map<String, Object> variables, final Map<String, Object> localVariables) {
        if (variables != null) {
            execution.setVariables(variables);
        }
        if (localVariables != null) {
            execution.setVariablesLocal(localVariables);
        }
    }
    
    protected boolean evaluateThrowBpmnError(final ExecutionEntity execution, final boolean continueOnException) {
        final List<CamundaErrorEventDefinition> camundaErrorEventDefinitions = (List<CamundaErrorEventDefinition>)execution.getActivity().getProperty(BpmnProperties.CAMUNDA_ERROR_EVENT_DEFINITION.getName());
        if (camundaErrorEventDefinitions != null && !camundaErrorEventDefinitions.isEmpty()) {
            for (final CamundaErrorEventDefinition camundaErrorEventDefinition : camundaErrorEventDefinitions) {
                if (this.errorEventDefinitionMatches(camundaErrorEventDefinition, continueOnException)) {
                    this.bpmnError(camundaErrorEventDefinition.getErrorCode(), this.errorMessage, null);
                    return true;
                }
            }
        }
        return false;
    }
    
    protected boolean errorEventDefinitionMatches(final CamundaErrorEventDefinition camundaErrorEventDefinition, final boolean continueOnException) {
        try {
            return camundaErrorEventDefinition.getExpression() != null && Boolean.TRUE.equals(camundaErrorEventDefinition.getExpression().getValue(this.getExecution()));
        }
        catch (Exception exception) {
            if (continueOnException) {
                ProcessEngineLogger.EXTERNAL_TASK_LOGGER.errorEventDefinitionEvaluationException(this.id, camundaErrorEventDefinition, exception);
                return false;
            }
            throw exception;
        }
    }
    
    @Override
    public String toString() {
        return "ExternalTaskEntity [id=" + this.id + ", revision=" + this.revision + ", topicName=" + this.topicName + ", workerId=" + this.workerId + ", lockExpirationTime=" + this.lockExpirationTime + ", priority=" + this.priority + ", errorMessage=" + this.errorMessage + ", errorDetailsByteArray=" + this.errorDetailsByteArray + ", errorDetailsByteArrayId=" + this.errorDetailsByteArrayId + ", executionId=" + this.executionId + "]";
    }
    
    public void unlock() {
        this.workerId = null;
        this.lockExpirationTime = null;
        Context.getCommandContext().getExternalTaskManager().fireExternalTaskAvailableEvent();
    }
    
    public static ExternalTaskEntity createAndInsert(final ExecutionEntity execution, final String topic, final long priority) {
        final ExternalTaskEntity externalTask = new ExternalTaskEntity();
        externalTask.setTopicName(topic);
        externalTask.setExecutionId(execution.getId());
        externalTask.setProcessInstanceId(execution.getProcessInstanceId());
        externalTask.setProcessDefinitionId(execution.getProcessDefinitionId());
        externalTask.setActivityId(execution.getActivityId());
        externalTask.setActivityInstanceId(execution.getActivityInstanceId());
        externalTask.setTenantId(execution.getTenantId());
        externalTask.setPriority(priority);
        final ProcessDefinitionEntity processDefinition = execution.getProcessDefinition();
        externalTask.setProcessDefinitionKey(processDefinition.getKey());
        externalTask.insert();
        externalTask.produceHistoricExternalTaskCreatedEvent();
        return externalTask;
    }
    
    protected void produceHistoricExternalTaskCreatedEvent() {
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.getHistoricExternalTaskLogManager().fireExternalTaskCreatedEvent(this);
    }
    
    protected void produceHistoricExternalTaskFailedEvent() {
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.getHistoricExternalTaskLogManager().fireExternalTaskFailedEvent(this);
    }
    
    protected void produceHistoricExternalTaskSuccessfulEvent() {
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.getHistoricExternalTaskLogManager().fireExternalTaskSuccessfulEvent(this);
    }
    
    protected void produceHistoricExternalTaskDeletedEvent() {
        final CommandContext commandContext = Context.getCommandContext();
        commandContext.getHistoricExternalTaskLogManager().fireExternalTaskDeletedEvent(this);
    }
    
    public void extendLock(final long newLockExpirationTime) {
        this.ensureActive();
        final long newTime = ClockUtil.getCurrentTime().getTime() + newLockExpirationTime;
        this.lockExpirationTime = new Date(newTime);
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        if (this.executionId != null) {
            referenceIdAndClass.put(this.executionId, ExecutionEntity.class);
        }
        if (this.errorDetailsByteArrayId != null) {
            referenceIdAndClass.put(this.errorDetailsByteArrayId, ByteArrayEntity.class);
        }
        return referenceIdAndClass;
    }
    
    public String getLastFailureLogId() {
        return this.lastFailureLogId;
    }
    
    public void setLastFailureLogId(final String lastFailureLogId) {
        this.lastFailureLogId = lastFailureLogId;
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
