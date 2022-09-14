// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceEntity;
import org.zik.bpm.engine.impl.persistence.entity.HistoricJobLogManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricIncidentManager;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionManager;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;
import java.util.HashMap;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import org.zik.bpm.engine.repository.ResourceType;
import org.zik.bpm.engine.repository.ResourceTypes;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.persistence.entity.util.ByteArrayField;
import org.zik.bpm.engine.impl.db.HasDbRevision;
import org.zik.bpm.engine.impl.persistence.entity.Nameable;
import org.zik.bpm.engine.impl.db.HasDbReferences;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.batch.Batch;

public class BatchEntity implements Batch, DbEntity, HasDbReferences, Nameable, HasDbRevision
{
    public static final BatchSeedJobDeclaration BATCH_SEED_JOB_DECLARATION;
    public static final BatchMonitorJobDeclaration BATCH_MONITOR_JOB_DECLARATION;
    protected String id;
    protected String type;
    protected int totalJobs;
    protected int jobsCreated;
    protected int batchJobsPerSeed;
    protected int invocationsPerBatchJob;
    protected String seedJobDefinitionId;
    protected String monitorJobDefinitionId;
    protected String batchJobDefinitionId;
    protected ByteArrayField configuration;
    protected String tenantId;
    protected String createUserId;
    protected int suspensionState;
    protected int revision;
    protected JobDefinitionEntity seedJobDefinition;
    protected JobDefinitionEntity monitorJobDefinition;
    protected JobDefinitionEntity batchJobDefinition;
    protected BatchJobHandler<?> batchJobHandler;
    
    public BatchEntity() {
        this.configuration = new ByteArrayField(this, ResourceTypes.RUNTIME);
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
    public String getName() {
        return this.id;
    }
    
    @Override
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public int getTotalJobs() {
        return this.totalJobs;
    }
    
    public void setTotalJobs(final int totalJobs) {
        this.totalJobs = totalJobs;
    }
    
    @Override
    public int getJobsCreated() {
        return this.jobsCreated;
    }
    
    public void setJobsCreated(final int jobsCreated) {
        this.jobsCreated = jobsCreated;
    }
    
    @Override
    public int getBatchJobsPerSeed() {
        return this.batchJobsPerSeed;
    }
    
    public void setBatchJobsPerSeed(final int batchJobsPerSeed) {
        this.batchJobsPerSeed = batchJobsPerSeed;
    }
    
    @Override
    public int getInvocationsPerBatchJob() {
        return this.invocationsPerBatchJob;
    }
    
    public void setInvocationsPerBatchJob(final int invocationsPerBatchJob) {
        this.invocationsPerBatchJob = invocationsPerBatchJob;
    }
    
    @Override
    public String getSeedJobDefinitionId() {
        return this.seedJobDefinitionId;
    }
    
    public void setSeedJobDefinitionId(final String seedJobDefinitionId) {
        this.seedJobDefinitionId = seedJobDefinitionId;
    }
    
    @Override
    public String getMonitorJobDefinitionId() {
        return this.monitorJobDefinitionId;
    }
    
    public void setMonitorJobDefinitionId(final String monitorJobDefinitionId) {
        this.monitorJobDefinitionId = monitorJobDefinitionId;
    }
    
    @Override
    public String getBatchJobDefinitionId() {
        return this.batchJobDefinitionId;
    }
    
    public void setBatchJobDefinitionId(final String batchJobDefinitionId) {
        this.batchJobDefinitionId = batchJobDefinitionId;
    }
    
    @Override
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    @Override
    public String getCreateUserId() {
        return this.createUserId;
    }
    
    public void setCreateUserId(final String createUserId) {
        this.createUserId = createUserId;
    }
    
    public String getConfiguration() {
        return this.configuration.getByteArrayId();
    }
    
    public void setConfiguration(final String configuration) {
        this.configuration.setByteArrayId(configuration);
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
    public void setRevision(final int revision) {
        this.revision = revision;
    }
    
    @Override
    public int getRevision() {
        return this.revision;
    }
    
    @Override
    public int getRevisionNext() {
        return this.revision + 1;
    }
    
    public JobDefinitionEntity getSeedJobDefinition() {
        if (this.seedJobDefinition == null && this.seedJobDefinitionId != null) {
            this.seedJobDefinition = Context.getCommandContext().getJobDefinitionManager().findById(this.seedJobDefinitionId);
        }
        return this.seedJobDefinition;
    }
    
    public JobDefinitionEntity getMonitorJobDefinition() {
        if (this.monitorJobDefinition == null && this.monitorJobDefinitionId != null) {
            this.monitorJobDefinition = Context.getCommandContext().getJobDefinitionManager().findById(this.monitorJobDefinitionId);
        }
        return this.monitorJobDefinition;
    }
    
    public JobDefinitionEntity getBatchJobDefinition() {
        if (this.batchJobDefinition == null && this.batchJobDefinitionId != null) {
            this.batchJobDefinition = Context.getCommandContext().getJobDefinitionManager().findById(this.batchJobDefinitionId);
        }
        return this.batchJobDefinition;
    }
    
    public byte[] getConfigurationBytes() {
        return this.configuration.getByteArrayValue();
    }
    
    public void setConfigurationBytes(final byte[] configuration) {
        this.configuration.setByteArrayValue(configuration);
    }
    
    public BatchJobHandler<?> getBatchJobHandler() {
        if (this.batchJobHandler == null) {
            this.batchJobHandler = Context.getCommandContext().getProcessEngineConfiguration().getBatchHandlers().get(this.type);
        }
        return this.batchJobHandler;
    }
    
    @Override
    public Object getPersistentState() {
        final HashMap<String, Object> persistentState = new HashMap<String, Object>();
        persistentState.put("jobsCreated", this.jobsCreated);
        return persistentState;
    }
    
    public JobDefinitionEntity createSeedJobDefinition(final String deploymentId) {
        (this.seedJobDefinition = new JobDefinitionEntity(BatchEntity.BATCH_SEED_JOB_DECLARATION)).setJobConfiguration(this.id);
        this.seedJobDefinition.setTenantId(this.tenantId);
        this.seedJobDefinition.setDeploymentId(deploymentId);
        Context.getCommandContext().getJobDefinitionManager().insert(this.seedJobDefinition);
        this.seedJobDefinitionId = this.seedJobDefinition.getId();
        return this.seedJobDefinition;
    }
    
    public JobDefinitionEntity createMonitorJobDefinition() {
        (this.monitorJobDefinition = new JobDefinitionEntity(BatchEntity.BATCH_MONITOR_JOB_DECLARATION)).setJobConfiguration(this.id);
        this.monitorJobDefinition.setTenantId(this.tenantId);
        Context.getCommandContext().getJobDefinitionManager().insert(this.monitorJobDefinition);
        this.monitorJobDefinitionId = this.monitorJobDefinition.getId();
        return this.monitorJobDefinition;
    }
    
    public JobDefinitionEntity createBatchJobDefinition() {
        (this.batchJobDefinition = new JobDefinitionEntity(this.getBatchJobHandler().getJobDeclaration())).setJobConfiguration(this.id);
        this.batchJobDefinition.setTenantId(this.tenantId);
        Context.getCommandContext().getJobDefinitionManager().insert(this.batchJobDefinition);
        this.batchJobDefinitionId = this.batchJobDefinition.getId();
        return this.batchJobDefinition;
    }
    
    public JobEntity createSeedJob() {
        final JobEntity seedJob = ((JobDeclaration<BatchEntity, JobEntity>)BatchEntity.BATCH_SEED_JOB_DECLARATION).createJobInstance(this);
        Context.getCommandContext().getJobManager().insertAndHintJobExecutor(seedJob);
        return seedJob;
    }
    
    public void deleteSeedJob() {
        final List<JobEntity> seedJobs = Context.getCommandContext().getJobManager().findJobsByJobDefinitionId(this.seedJobDefinitionId);
        for (final JobEntity job : seedJobs) {
            job.delete();
        }
    }
    
    public JobEntity createMonitorJob(final boolean setDueDate) {
        final JobEntity monitorJob = ((JobDeclaration<BatchEntity, JobEntity>)BatchEntity.BATCH_MONITOR_JOB_DECLARATION).createJobInstance(this);
        if (setDueDate) {
            monitorJob.setDuedate(this.calculateMonitorJobDueDate());
        }
        Context.getCommandContext().getJobManager().insertAndHintJobExecutor(monitorJob);
        return monitorJob;
    }
    
    protected Date calculateMonitorJobDueDate() {
        final int pollTime = Context.getCommandContext().getProcessEngineConfiguration().getBatchPollTime();
        final long dueTime = ClockUtil.getCurrentTime().getTime() + pollTime * 1000;
        return new Date(dueTime);
    }
    
    public void deleteMonitorJob() {
        final List<JobEntity> monitorJobs = Context.getCommandContext().getJobManager().findJobsByJobDefinitionId(this.monitorJobDefinitionId);
        for (final JobEntity monitorJob : monitorJobs) {
            monitorJob.delete();
        }
    }
    
    public void delete(final boolean cascadeToHistory, final boolean deleteJobs) {
        final CommandContext commandContext = Context.getCommandContext();
        if ("set-variables".equals(this.type) || "instance-migration".equals(this.type) || "correlate-message".equals(this.type)) {
            this.deleteVariables(commandContext);
        }
        this.deleteSeedJob();
        this.deleteMonitorJob();
        if (deleteJobs) {
            this.getBatchJobHandler().deleteJobs(this);
        }
        final JobDefinitionManager jobDefinitionManager = commandContext.getJobDefinitionManager();
        jobDefinitionManager.delete(this.getSeedJobDefinition());
        jobDefinitionManager.delete(this.getMonitorJobDefinition());
        jobDefinitionManager.delete(this.getBatchJobDefinition());
        commandContext.getBatchManager().delete(this);
        this.configuration.deleteByteArrayValue();
        this.fireHistoricEndEvent();
        if (cascadeToHistory) {
            final HistoricIncidentManager historicIncidentManager = commandContext.getHistoricIncidentManager();
            historicIncidentManager.deleteHistoricIncidentsByJobDefinitionId(this.seedJobDefinitionId);
            historicIncidentManager.deleteHistoricIncidentsByJobDefinitionId(this.monitorJobDefinitionId);
            historicIncidentManager.deleteHistoricIncidentsByJobDefinitionId(this.batchJobDefinitionId);
            final HistoricJobLogManager historicJobLogManager = commandContext.getHistoricJobLogManager();
            historicJobLogManager.deleteHistoricJobLogsByJobDefinitionId(this.seedJobDefinitionId);
            historicJobLogManager.deleteHistoricJobLogsByJobDefinitionId(this.monitorJobDefinitionId);
            historicJobLogManager.deleteHistoricJobLogsByJobDefinitionId(this.batchJobDefinitionId);
            commandContext.getHistoricBatchManager().deleteHistoricBatchById(this.id);
        }
    }
    
    protected void deleteVariables(final CommandContext commandContext) {
        final VariableInstanceManager variableInstanceManager = commandContext.getVariableInstanceManager();
        final List<VariableInstanceEntity> variableInstances = variableInstanceManager.findVariableInstancesByBatchId(this.id);
        variableInstances.forEach(VariableInstanceEntity::delete);
    }
    
    public void fireHistoricStartEvent() {
        Context.getCommandContext().getHistoricBatchManager().createHistoricBatch(this);
    }
    
    public void fireHistoricEndEvent() {
        Context.getCommandContext().getHistoricBatchManager().completeHistoricBatch(this);
    }
    
    public boolean isCompleted() {
        return Context.getCommandContext().getProcessEngineConfiguration().getManagementService().createJobQuery().jobDefinitionId(this.batchJobDefinitionId).count() == 0L;
    }
    
    @Override
    public String toString() {
        return "BatchEntity{batchHandler=" + this.batchJobHandler + ", id='" + this.id + '\'' + ", type='" + this.type + '\'' + ", size=" + this.totalJobs + ", jobCreated=" + this.jobsCreated + ", batchJobsPerSeed=" + this.batchJobsPerSeed + ", invocationsPerBatchJob=" + this.invocationsPerBatchJob + ", seedJobDefinitionId='" + this.seedJobDefinitionId + '\'' + ", monitorJobDefinitionId='" + this.seedJobDefinitionId + '\'' + ", batchJobDefinitionId='" + this.batchJobDefinitionId + '\'' + ", configurationId='" + this.configuration.getByteArrayId() + '\'' + '}';
    }
    
    @Override
    public Set<String> getReferencedEntityIds() {
        final Set<String> referencedEntityIds = new HashSet<String>();
        return referencedEntityIds;
    }
    
    @Override
    public Map<String, Class> getReferencedEntitiesIdAndClass() {
        final Map<String, Class> referenceIdAndClass = new HashMap<String, Class>();
        if (this.seedJobDefinitionId != null) {
            referenceIdAndClass.put(this.seedJobDefinitionId, JobDefinitionEntity.class);
        }
        if (this.batchJobDefinitionId != null) {
            referenceIdAndClass.put(this.batchJobDefinitionId, JobDefinitionEntity.class);
        }
        if (this.monitorJobDefinitionId != null) {
            referenceIdAndClass.put(this.monitorJobDefinitionId, JobDefinitionEntity.class);
        }
        return referenceIdAndClass;
    }
    
    static {
        BATCH_SEED_JOB_DECLARATION = new BatchSeedJobDeclaration();
        BATCH_MONITOR_JOB_DECLARATION = new BatchMonitorJobDeclaration();
    }
}
