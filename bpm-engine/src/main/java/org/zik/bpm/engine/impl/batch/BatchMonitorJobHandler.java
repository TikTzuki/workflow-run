// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.jobexecutor.JobHandler;

public class BatchMonitorJobHandler implements JobHandler<BatchMonitorJobHandler.BatchMonitorJobConfiguration>
{
    public static final String TYPE = "batch-monitor-job";
    
    @Override
    public String getType() {
        return "batch-monitor-job";
    }
    
    @Override
    public void execute(final BatchMonitorJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final String batchId = configuration.getBatchId();
        final BatchEntity batch = commandContext.getBatchManager().findBatchById(configuration.getBatchId());
        EnsureUtil.ensureNotNull("Batch with id '" + batchId + "' cannot be found", "batch", batch);
        final boolean completed = batch.isCompleted();
        if (!completed) {
            batch.createMonitorJob(true);
        }
        else {
            batch.delete(false, false);
        }
    }
    
    @Override
    public BatchMonitorJobConfiguration newConfiguration(final String canonicalString) {
        return new BatchMonitorJobConfiguration(canonicalString);
    }
    
    @Override
    public void onDelete(final BatchMonitorJobConfiguration configuration, final JobEntity jobEntity) {
    }
    
    public static class BatchMonitorJobConfiguration implements JobHandlerConfiguration
    {
        protected String batchId;
        
        public BatchMonitorJobConfiguration(final String batchId) {
            this.batchId = batchId;
        }
        
        public String getBatchId() {
            return this.batchId;
        }
        
        @Override
        public String toCanonicalString() {
            return this.batchId;
        }
    }
}
