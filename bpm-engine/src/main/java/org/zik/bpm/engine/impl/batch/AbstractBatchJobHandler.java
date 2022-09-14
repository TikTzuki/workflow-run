// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.json.JsonObjectConverter;
import com.google.gson.JsonElement;
import org.zik.bpm.engine.impl.util.JsonUtil;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobManager;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayManager;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.context.Context;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.MessageEntity;
import org.zik.bpm.engine.impl.jobexecutor.JobDeclaration;

public abstract class AbstractBatchJobHandler<T extends BatchConfiguration> implements BatchJobHandler<T>
{
    @Override
    public abstract JobDeclaration<BatchJobContext, MessageEntity> getJobDeclaration();
    
    @Override
    public boolean createJobs(final BatchEntity batch) {
        final T configuration = this.readConfiguration(batch.getConfigurationBytes());
        String deploymentId = null;
        final DeploymentMappings idMappings = configuration.getIdMappings();
        final boolean deploymentAware = idMappings != null && !idMappings.isEmpty();
        List<String> ids = configuration.getIds();
        if (deploymentAware) {
            this.sanitizeMappings(idMappings, ids);
            final DeploymentMapping mappingToProcess = idMappings.get(0);
            ids = mappingToProcess.getIds(ids);
            deploymentId = mappingToProcess.getDeploymentId();
        }
        final int batchJobsPerSeed = batch.getBatchJobsPerSeed();
        final int invocationsPerBatchJob = batch.getInvocationsPerBatchJob();
        final int numberOfItemsToProcess = Math.min(invocationsPerBatchJob * batchJobsPerSeed, ids.size());
        final List<String> processIds = ids.subList(0, numberOfItemsToProcess);
        this.createJobEntities(batch, configuration, deploymentId, processIds, invocationsPerBatchJob);
        if (deploymentAware) {
            if (ids.isEmpty()) {
                idMappings.remove(0);
            }
            else {
                idMappings.get(0).removeIds(numberOfItemsToProcess);
            }
        }
        batch.setConfigurationBytes(this.writeConfiguration(configuration));
        return deploymentAware ? idMappings.isEmpty() : ids.isEmpty();
    }
    
    protected void sanitizeMappings(final DeploymentMappings idMappings, final List<String> ids) {
        int elementsToRemove = idMappings.getOverallIdCount() - ids.size();
        if (elementsToRemove > 0) {
            final Iterator<DeploymentMapping> iterator = idMappings.iterator();
            while (iterator.hasNext()) {
                final DeploymentMapping deploymentMapping = iterator.next();
                if (deploymentMapping.getCount() > elementsToRemove) {
                    deploymentMapping.removeIds(elementsToRemove);
                    break;
                }
                iterator.remove();
                elementsToRemove -= deploymentMapping.getCount();
                if (elementsToRemove == 0) {
                    break;
                }
            }
        }
    }
    
    protected void createJobEntities(final BatchEntity batch, final T configuration, final String deploymentId, final List<String> processIds, final int invocationsPerBatchJob) {
        if (processIds == null || processIds.isEmpty()) {
            return;
        }
        final CommandContext commandContext = Context.getCommandContext();
        final ByteArrayManager byteArrayManager = commandContext.getByteArrayManager();
        final JobManager jobManager = commandContext.getJobManager();
        int createdJobs = 0;
        while (!processIds.isEmpty()) {
            final int lastIdIndex = Math.min(invocationsPerBatchJob, processIds.size());
            final List<String> idsForJob = processIds.subList(0, lastIdIndex);
            final T jobConfiguration = this.createJobConfiguration(configuration, idsForJob);
            jobConfiguration.setBatchId(batch.getId());
            final ByteArrayEntity configurationEntity = this.saveConfiguration(byteArrayManager, jobConfiguration);
            final JobEntity job = this.createBatchJob(batch, configurationEntity);
            job.setDeploymentId(deploymentId);
            this.postProcessJob(configuration, job, jobConfiguration);
            jobManager.insertAndHintJobExecutor(job);
            idsForJob.clear();
            ++createdJobs;
        }
        batch.setJobsCreated(batch.getJobsCreated() + createdJobs);
    }
    
    protected abstract T createJobConfiguration(final T p0, final List<String> p1);
    
    protected void postProcessJob(final T configuration, final JobEntity job, final T jobConfiguration) {
    }
    
    protected JobEntity createBatchJob(final BatchEntity batch, final ByteArrayEntity configuration) {
        final BatchJobContext creationContext = new BatchJobContext(batch, configuration);
        return this.getJobDeclaration().createJobInstance(creationContext);
    }
    
    @Override
    public void deleteJobs(final BatchEntity batch) {
        final List<JobEntity> jobs = Context.getCommandContext().getJobManager().findJobsByJobDefinitionId(batch.getBatchJobDefinitionId());
        for (final JobEntity job : jobs) {
            job.delete();
        }
    }
    
    @Override
    public BatchJobConfiguration newConfiguration(final String canonicalString) {
        return new BatchJobConfiguration(canonicalString);
    }
    
    @Override
    public void onDelete(final BatchJobConfiguration configuration, final JobEntity jobEntity) {
        final String byteArrayId = configuration.getConfigurationByteArrayId();
        if (byteArrayId != null) {
            Context.getCommandContext().getByteArrayManager().deleteByteArrayById(byteArrayId);
        }
    }
    
    protected ByteArrayEntity saveConfiguration(final ByteArrayManager byteArrayManager, final T jobConfiguration) {
        final ByteArrayEntity configurationEntity = new ByteArrayEntity();
        configurationEntity.setBytes(this.writeConfiguration(jobConfiguration));
        byteArrayManager.insert(configurationEntity);
        return configurationEntity;
    }
    
    @Override
    public byte[] writeConfiguration(final T configuration) {
        final JsonElement jsonObject = this.getJsonConverterInstance().toJsonObject(configuration);
        return JsonUtil.asBytes(jsonObject);
    }
    
    @Override
    public T readConfiguration(final byte[] serializedConfiguration) {
        return this.getJsonConverterInstance().toObject(JsonUtil.asObject(serializedConfiguration));
    }
    
    protected abstract JsonObjectConverter<T> getJsonConverterInstance();
}
