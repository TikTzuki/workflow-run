// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.util.StringUtil;
import org.zik.bpm.engine.impl.util.ExceptionUtil;
import org.zik.bpm.engine.impl.incident.IncidentContext;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.incident.IncidentHandling;
import org.zik.bpm.engine.runtime.Incident;
import java.util.HashMap;
import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.jobexecutor.JobHandler;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.jobexecutor.DefaultJobPriorityProvider;
import java.util.Map;
import org.zik.bpm.engine.management.JobDefinition;
import java.util.Date;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.db.DbEntityLifecycleAware;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.runtime.Job;
import java.io.Serializable;

public abstract class JobEntity extends AcquirableJobEntity implements Serializable, Job, DbEntity, HasDbRevision, HasDbReferences, DbEntityLifecycleAware
{
    private static final EnginePersistenceLogger LOG;
    public static final int DEFAULT_RETRIES = 3;
    private static final long serialVersionUID = 1L;
    protected String executionId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected int retries;
    protected int suspensionState;
    protected String jobHandlerType;
    protected String jobHandlerConfiguration;
    protected ByteArrayEntity exceptionByteArray;
    protected String exceptionByteArrayId;
    protected String exceptionMessage;
    protected String deploymentId;
    protected String jobDefinitionId;
    protected long priority;
    protected String tenantId;
    protected Date createTime;
    protected String activityId;
    protected JobDefinition jobDefinition;
    protected ExecutionEntity execution;
    protected long sequenceCounter;
    protected String lastFailureLogId;
    protected String failedActivityId;
    protected Map<String, Class> persistedDependentEntities;
    
    public JobEntity() {
        this.executionId = null;
        this.processDefinitionId = null;
        this.processDefinitionKey = null;
        this.retries = 3;
        this.suspensionState = SuspensionState.ACTIVE.getStateCode();
        this.jobHandlerType = null;
        this.jobHandlerConfiguration = null;
        this.priority = DefaultJobPriorityProvider.DEFAULT_PRIORITY;
        this.sequenceCounter = 1L;
    }
    
    public void execute(final CommandContext commandContext) {
        if (this.executionId != null) {
            final ExecutionEntity execution = this.getExecution();
            EnsureUtil.ensureNotNull("Cannot find execution with id '" + this.executionId + "' referenced from job '" + this + "'", "execution", execution);
        }
        this.getActivityId();
        this.incrementSequenceCounter();
        this.preExecute(commandContext);
        final JobHandler jobHandler = this.getJobHandler();
        final JobHandlerConfiguration configuration = this.getJobHandlerConfiguration();
        EnsureUtil.ensureNotNull("Cannot find job handler '" + this.jobHandlerType + "' from job '" + this + "'", "jobHandler", jobHandler);
        jobHandler.execute(configuration, this.execution, commandContext, this.tenantId);
        this.postExecute(commandContext);
    }
    
    protected void preExecute(final CommandContext commandContext) {
    }
    
    protected void postExecute(final CommandContext commandContext) {
        JobEntity.LOG.debugJobExecuted(this);
        this.delete(true);
        commandContext.getHistoricJobLogManager().fireJobSuccessfulEvent(this);
    }
    
    public void init(final CommandContext commandContext) {
    }
    
    public void insert() {
        final CommandContext commandContext = Context.getCommandContext();
        final ExecutionEntity execution = this.getExecution();
        if (execution != null) {
            execution.addJob(this);
            final ProcessDefinitionImpl processDefinition = execution.getProcessDefinition();
            this.deploymentId = processDefinition.getDeploymentId();
        }
        commandContext.getJobManager().insertJob(this);
    }
    
    public void delete() {
        this.delete(false);
    }
    
    public void delete(final boolean incidentResolved) {
        final CommandContext commandContext = Context.getCommandContext();
        this.incrementSequenceCounter();
        final JobHandler jobHandler = this.getJobHandler();
        if (jobHandler != null) {
            jobHandler.onDelete(this.getJobHandlerConfiguration(), this);
        }
        final boolean executingJob = this.equals(commandContext.getCurrentJob());
        commandContext.getJobManager().deleteJob(this, !executingJob);
        if (this.exceptionByteArrayId != null) {
            commandContext.getByteArrayManager().deleteByteArrayById(this.exceptionByteArrayId);
        }
        final ExecutionEntity execution = this.getExecution();
        if (execution != null) {
            execution.removeJob(this);
        }
        this.removeFailedJobIncident(incidentResolved);
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = (Map<String, Object>)super.getPersistentState();
        persistentState.put("executionId", this.executionId);
        persistentState.put("retries", this.retries);
        persistentState.put("exceptionMessage", this.exceptionMessage);
        persistentState.put("suspensionState", this.suspensionState);
        persistentState.put("processDefinitionId", this.processDefinitionId);
        persistentState.put("jobDefinitionId", this.jobDefinitionId);
        persistentState.put("deploymentId", this.deploymentId);
        persistentState.put("jobHandlerConfiguration", this.jobHandlerConfiguration);
        persistentState.put("priority", this.priority);
        persistentState.put("tenantId", this.tenantId);
        if (this.exceptionByteArrayId != null) {
            persistentState.put("exceptionByteArrayId", this.exceptionByteArrayId);
        }
        return persistentState;
    }
    
    public void setExecution(final ExecutionEntity execution) {
        if (execution != null) {
            this.execution = execution;
            this.executionId = execution.getId();
            this.processInstanceId = execution.getProcessInstanceId();
            this.execution.addJob(this);
        }
        else {
            this.execution.removeJob(this);
            this.execution = execution;
            this.processInstanceId = null;
            this.executionId = null;
        }
    }
    
    public long getSequenceCounter() {
        return this.sequenceCounter;
    }
    
    public void setSequenceCounter(final long sequenceCounter) {
        this.sequenceCounter = sequenceCounter;
    }
    
    public void incrementSequenceCounter() {
        ++this.sequenceCounter;
    }
    
    @Override
    public String getExecutionId() {
        return this.executionId;
    }
    
    public void setExecutionId(final String executionId) {
        this.executionId = executionId;
    }
    
    public ExecutionEntity getExecution() {
        this.ensureExecutionInitialized();
        return this.execution;
    }
    
    protected void ensureExecutionInitialized() {
        if (this.execution == null && this.executionId != null) {
            this.execution = Context.getCommandContext().getExecutionManager().findExecutionById(this.executionId);
        }
    }
    
    @Override
    public int getRetries() {
        return this.retries;
    }
    
    public void setRetries(int retries) {
        if (retries < 0) {
            retries = 0;
        }
        if (this.retries == 0 && retries > 0) {
            this.removeFailedJobIncident(true);
        }
        if (retries == 0 && this.retries > 0) {
            this.createFailedJobIncident();
        }
        this.retries = retries;
    }
    
    public void setRetriesFromPersistence(final int retries) {
        this.retries = retries;
    }
    
    protected void createFailedJobIncident() {
        final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration.isCreateIncidentOnFailedJobEnabled()) {
            final String incidentHandlerType = "failedJob";
            if (this.id == null) {
                this.id = processEngineConfiguration.getIdGenerator().getNextId();
            }
            else {
                final List<Incident> failedJobIncidents = Context.getCommandContext().getIncidentManager().findIncidentByConfigurationAndIncidentType(this.id, incidentHandlerType);
                if (!failedJobIncidents.isEmpty()) {
                    for (final Incident incident : failedJobIncidents) {
                        final HistoricIncidentEntity historicIncidentEvent = Context.getCommandContext().getHistoricIncidentManager().findHistoricIncidentById(incident.getId());
                        if (historicIncidentEvent != null) {
                            historicIncidentEvent.setHistoryConfiguration(this.getLastFailureLogId());
                            Context.getCommandContext().getDbEntityManager().merge(historicIncidentEvent);
                        }
                    }
                    return;
                }
            }
            final IncidentContext incidentContext = this.createIncidentContext();
            incidentContext.setActivityId(this.getActivityId());
            incidentContext.setHistoryConfiguration(this.getLastFailureLogId());
            incidentContext.setFailedActivityId(this.getFailedActivityId());
            IncidentHandling.createIncident(incidentHandlerType, incidentContext, this.exceptionMessage);
        }
    }
    
    protected void removeFailedJobIncident(final boolean incidentResolved) {
        final IncidentContext incidentContext = this.createIncidentContext();
        IncidentHandling.removeIncidents("failedJob", incidentContext, incidentResolved);
    }
    
    protected IncidentContext createIncidentContext() {
        final IncidentContext incidentContext = new IncidentContext();
        incidentContext.setProcessDefinitionId(this.processDefinitionId);
        incidentContext.setExecutionId(this.executionId);
        incidentContext.setTenantId(this.tenantId);
        incidentContext.setConfiguration(this.id);
        incidentContext.setJobDefinitionId(this.jobDefinitionId);
        return incidentContext;
    }
    
    public String getExceptionStacktrace() {
        final ByteArrayEntity byteArray = this.getExceptionByteArray();
        return ExceptionUtil.getExceptionStacktrace(byteArray);
    }
    
    public void setSuspensionState(final int state) {
        this.suspensionState = state;
    }
    
    public int getSuspensionState() {
        return this.suspensionState;
    }
    
    @Override
    public boolean isSuspended() {
        return this.suspensionState == SuspensionState.SUSPENDED.getStateCode();
    }
    
    @Override
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public void setProcessDefinitionId(final String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public void setProcessDefinitionKey(final String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }
    
    public void setExceptionStacktrace(final String exception) {
        final byte[] exceptionBytes = StringUtil.toByteArray(exception);
        ByteArrayEntity byteArray = this.getExceptionByteArray();
        if (byteArray == null) {
            byteArray = ExceptionUtil.createJobExceptionByteArray(exceptionBytes, ResourceTypes.RUNTIME);
            this.exceptionByteArrayId = byteArray.getId();
            this.exceptionByteArray = byteArray;
        }
        else {
            byteArray.setBytes(exceptionBytes);
        }
    }
    
    protected JobHandler getJobHandler() {
        final Map<String, JobHandler> jobHandlers = Context.getProcessEngineConfiguration().getJobHandlers();
        return jobHandlers.get(this.jobHandlerType);
    }
    
    public JobHandlerConfiguration getJobHandlerConfiguration() {
        return this.getJobHandler().newConfiguration(this.jobHandlerConfiguration);
    }
    
    public void setJobHandlerConfiguration(final JobHandlerConfiguration configuration) {
        this.jobHandlerConfiguration = configuration.toCanonicalString();
    }
    
    public String getJobHandlerType() {
        return this.jobHandlerType;
    }
    
    public void setJobHandlerType(final String jobHandlerType) {
        this.jobHandlerType = jobHandlerType;
    }
    
    public String getJobHandlerConfigurationRaw() {
        return this.jobHandlerConfiguration;
    }
    
    public void setJobHandlerConfigurationRaw(final String jobHandlerConfiguration) {
        this.jobHandlerConfiguration = jobHandlerConfiguration;
    }
    
    @Override
    public String getExceptionMessage() {
        return this.exceptionMessage;
    }
    
    @Override
    public String getJobDefinitionId() {
        return this.jobDefinitionId;
    }
    
    public void setJobDefinitionId(final String jobDefinitionId) {
        this.jobDefinitionId = jobDefinitionId;
    }
    
    public JobDefinition getJobDefinition() {
        this.ensureJobDefinitionInitialized();
        return this.jobDefinition;
    }
    
    public void setJobDefinition(final JobDefinition jobDefinition) {
        this.jobDefinition = jobDefinition;
        if (jobDefinition != null) {
            this.jobDefinitionId = jobDefinition.getId();
        }
        else {
            this.jobDefinitionId = null;
        }
    }
    
    protected void ensureJobDefinitionInitialized() {
        if (this.jobDefinition == null && this.jobDefinitionId != null) {
            this.jobDefinition = Context.getCommandContext().getJobDefinitionManager().findById(this.jobDefinitionId);
        }
    }
    
    public void setExceptionMessage(final String exceptionMessage) {
        this.exceptionMessage = StringUtil.trimToMaximumLengthAllowed(exceptionMessage);
    }
    
    public String getExceptionByteArrayId() {
        return this.exceptionByteArrayId;
    }
    
    protected ByteArrayEntity getExceptionByteArray() {
        this.ensureExceptionByteArrayInitialized();
        return this.exceptionByteArray;
    }
    
    protected void ensureExceptionByteArrayInitialized() {
        if (this.exceptionByteArray == null && this.exceptionByteArrayId != null) {
            this.exceptionByteArray = Context.getCommandContext().getDbEntityManager().selectById(ByteArrayEntity.class, this.exceptionByteArrayId);
        }
    }
    
    protected void clearFailedJobException() {
        final ByteArrayEntity byteArray = this.getExceptionByteArray();
        if (byteArray != null) {
            Context.getCommandContext().getDbEntityManager().delete(byteArray);
        }
        this.exceptionByteArrayId = null;
        this.exceptionMessage = null;
    }
    
    @Override
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public void setDeploymentId(final String deploymentId) {
        this.deploymentId = deploymentId;
    }
    
    public boolean isInInconsistentLockState() {
        return (this.lockOwner != null && this.lockExpirationTime == null) || (this.retries == 0 && (this.lockOwner != null || this.lockExpirationTime != null));
    }
    
    public void resetLock() {
        this.lockOwner = null;
        this.lockExpirationTime = null;
    }
    
    public String getActivityId() {
        this.ensureActivityIdInitialized();
        return this.activityId;
    }
    
    public void setActivityId(final String activityId) {
        this.activityId = activityId;
    }
    
    @Override
    public long getPriority() {
        return this.priority;
    }
    
    public void setPriority(final long priority) {
        this.priority = priority;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }
    
    protected void ensureActivityIdInitialized() {
        if (this.activityId == null) {
            final JobDefinition jobDefinition = this.getJobDefinition();
            if (jobDefinition != null) {
                this.activityId = jobDefinition.getActivityId();
            }
            else {
                final ExecutionEntity execution = this.getExecution();
                if (execution != null) {
                    this.activityId = execution.getActivityId();
                }
            }
        }
    }
    
    public void unlock() {
        this.lockOwner = null;
        this.lockExpirationTime = null;
    }
    
    public abstract String getType();
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final JobEntity other = (JobEntity)obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        }
        else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        if (this.exceptionByteArrayId != null) {
            referenceIdAndClass.put(this.exceptionByteArrayId, ByteArrayEntity.class);
        }
        return referenceIdAndClass;
    }
    
    @Override
    public Map<String, Class> getDependentEntities() {
        return this.persistedDependentEntities;
    }
    
    @Override
    public void postLoad() {
        if (this.exceptionByteArrayId != null) {
            (this.persistedDependentEntities = new HashMap<String, Class>()).put(this.exceptionByteArrayId, ByteArrayEntity.class);
        }
        else {
            this.persistedDependentEntities = (Map<String, Class>)Collections.EMPTY_MAP;
        }
    }
    
    public String getLastFailureLogId() {
        return this.lastFailureLogId;
    }
    
    public void setLastFailureLogId(final String lastFailureLogId) {
        this.lastFailureLogId = lastFailureLogId;
    }
    
    @Override
    public String getFailedActivityId() {
        return this.failedActivityId;
    }
    
    public void setFailedActivityId(final String failedActivityId) {
        this.failedActivityId = failedActivityId;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", revision=" + this.revision + ", duedate=" + this.duedate + ", lockOwner=" + this.lockOwner + ", lockExpirationTime=" + this.lockExpirationTime + ", executionId=" + this.executionId + ", processInstanceId=" + this.processInstanceId + ", isExclusive=" + this.isExclusive + ", isExclusive=" + this.isExclusive + ", jobDefinitionId=" + this.jobDefinitionId + ", jobHandlerType=" + this.jobHandlerType + ", jobHandlerConfiguration=" + this.jobHandlerConfiguration + ", exceptionByteArray=" + this.exceptionByteArray + ", exceptionByteArrayId=" + this.exceptionByteArrayId + ", exceptionMessage=" + this.exceptionMessage + ", failedActivityId=" + this.failedActivityId + ", deploymentId=" + this.deploymentId + ", priority=" + this.priority + ", tenantId=" + this.tenantId + "]";
    }
    
    static {
        LOG = ProcessEngineLogger.PERSISTENCE_LOGGER;
    }
}
