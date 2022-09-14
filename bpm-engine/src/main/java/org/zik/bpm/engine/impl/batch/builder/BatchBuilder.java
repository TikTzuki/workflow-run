// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch.builder;

import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.batch.DeploymentMapping;
import java.util.List;
import org.zik.bpm.engine.impl.jobexecutor.JobHandler;
import java.util.Map;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.batch.BatchJobHandler;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.batch.BatchEntity;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.impl.batch.BatchConfiguration;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class BatchBuilder
{
    protected CommandContext commandContext;
    protected BatchConfiguration config;
    protected String tenantId;
    protected String type;
    protected Integer totalJobsCount;
    protected Permission permission;
    protected PermissionHandler permissionHandler;
    protected OperationLogInstanceCountHandler operationLogInstanceCountHandler;
    protected OperationLogHandler operationLogHandler;
    
    public BatchBuilder(final CommandContext commandContext) {
        this.commandContext = commandContext;
    }
    
    public BatchBuilder tenantId(final String tenantId) {
        this.tenantId = tenantId;
        return this;
    }
    
    public BatchBuilder config(final BatchConfiguration config) {
        this.config = config;
        return this;
    }
    
    public BatchBuilder type(final String batchType) {
        this.type = batchType;
        return this;
    }
    
    public BatchBuilder totalJobs(final int totalJobsCount) {
        this.totalJobsCount = totalJobsCount;
        return this;
    }
    
    public BatchBuilder permission(final Permission permission) {
        this.permission = permission;
        return this;
    }
    
    public BatchBuilder permissionHandler(final PermissionHandler permissionCheckHandler) {
        this.permissionHandler = permissionCheckHandler;
        return this;
    }
    
    public BatchBuilder operationLogHandler(final OperationLogInstanceCountHandler operationLogHandler) {
        this.operationLogInstanceCountHandler = operationLogHandler;
        return this;
    }
    
    public BatchBuilder operationLogHandler(final OperationLogHandler operationLogHandler) {
        this.operationLogHandler = operationLogHandler;
        return this;
    }
    
    public Batch build() {
        this.checkPermissions();
        final BatchEntity batch = new BatchEntity();
        this.configure(batch);
        this.save(batch);
        this.writeOperationLog();
        return batch;
    }
    
    protected void checkPermissions() {
        if (this.permission == null && this.permissionHandler == null) {
            throw new ProcessEngineException("No permission check performed!");
        }
        if (this.permission != null) {
            this.commandContext.getProcessEngineConfiguration().getCommandCheckers().forEach(checker -> checker.checkCreateBatch(this.permission));
        }
        if (this.permissionHandler != null) {
            this.permissionHandler.check(this.commandContext);
        }
    }
    
    protected BatchEntity configure(final BatchEntity batch) {
        final ProcessEngineConfigurationImpl engineConfig = this.commandContext.getProcessEngineConfiguration();
        final Map<String, JobHandler> jobHandlers = engineConfig.getJobHandlers();
        final BatchJobHandler jobHandler = jobHandlers.get(this.type);
        final String type = jobHandler.getType();
        batch.setType(type);
        final int invocationPerBatchJobCount = this.calculateInvocationsPerBatchJob(type);
        batch.setInvocationsPerBatchJob(invocationPerBatchJobCount);
        batch.setTenantId(this.tenantId);
        final byte[] configAsBytes = jobHandler.writeConfiguration(this.config);
        batch.setConfigurationBytes(configAsBytes);
        this.setTotalJobs(batch, invocationPerBatchJobCount);
        final int jobCount = engineConfig.getBatchJobsPerSeed();
        batch.setBatchJobsPerSeed(jobCount);
        return batch;
    }
    
    protected void setTotalJobs(final BatchEntity batch, final int invocationPerBatchJobCount) {
        if (this.totalJobsCount != null) {
            batch.setTotalJobs(this.totalJobsCount);
        }
        else {
            final List<String> instanceIds = this.config.getIds();
            final int instanceCount = instanceIds.size();
            final int totalJobsCount = this.calculateTotalJobs(instanceCount, invocationPerBatchJobCount);
            batch.setTotalJobs(totalJobsCount);
        }
    }
    
    protected void save(final BatchEntity batch) {
        this.commandContext.getBatchManager().insertBatch(batch);
        String seedDeploymentId = null;
        if (this.config.getIdMappings() != null && !this.config.getIdMappings().isEmpty()) {
            seedDeploymentId = this.config.getIdMappings().get(0).getDeploymentId();
        }
        batch.createSeedJobDefinition(seedDeploymentId);
        batch.createMonitorJobDefinition();
        batch.createBatchJobDefinition();
        batch.fireHistoricStartEvent();
        batch.createSeedJob();
    }
    
    protected void writeOperationLog() {
        if (this.operationLogInstanceCountHandler == null && this.operationLogHandler == null) {
            throw new ProcessEngineException("No operation log handler specified!");
        }
        if (this.operationLogInstanceCountHandler != null) {
            final List<String> instanceIds = this.config.getIds();
            final int instanceCount = instanceIds.size();
            this.operationLogInstanceCountHandler.write(this.commandContext, instanceCount);
        }
        else {
            this.operationLogHandler.write(this.commandContext);
        }
    }
    
    protected int calculateTotalJobs(final int instanceCount, final int invocationPerBatchJobCount) {
        if (instanceCount == 0 || invocationPerBatchJobCount == 0) {
            return 0;
        }
        if (instanceCount % invocationPerBatchJobCount == 0) {
            return instanceCount / invocationPerBatchJobCount;
        }
        return instanceCount / invocationPerBatchJobCount + 1;
    }
    
    protected int calculateInvocationsPerBatchJob(final String batchType) {
        final ProcessEngineConfigurationImpl engineConfig = this.commandContext.getProcessEngineConfiguration();
        final Map<String, Integer> invocationsPerBatchJobByBatchType = engineConfig.getInvocationsPerBatchJobByBatchType();
        final Integer invocationCount = invocationsPerBatchJobByBatchType.get(batchType);
        if (invocationCount != null) {
            return invocationCount;
        }
        return engineConfig.getInvocationsPerBatchJob();
    }
}
