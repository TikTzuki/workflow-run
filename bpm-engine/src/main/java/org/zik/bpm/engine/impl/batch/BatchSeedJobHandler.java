// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.jobexecutor.JobHandler;
import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.util.EnsureUtil;

public class BatchSeedJobHandler implements JobHandler<BatchSeedJobHandler.BatchSeedJobConfiguration> {
    public static final String TYPE = "batch-seed-job";

    @Override
    public String getType() {
        return "batch-seed-job";
    }

    @Override
    public void execute(final BatchSeedJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final String batchId = configuration.getBatchId();
        final BatchEntity batch = commandContext.getBatchManager().findBatchById(batchId);
        EnsureUtil.ensureNotNull("Batch with id '" + batchId + "' cannot be found", "batch", batch);
        final BatchJobHandler<?> batchJobHandler = commandContext.getProcessEngineConfiguration().getBatchHandlers().get(batch.getType());
        final boolean done = batchJobHandler.createJobs(batch);
        if (!done) {
            batch.createSeedJob();
        } else {
            batch.createMonitorJob(false);
        }
    }

    @Override
    public BatchSeedJobConfiguration newConfiguration(final String canonicalString) {
        return new BatchSeedJobConfiguration(canonicalString);
    }

    @Override
    public void onDelete(final BatchSeedJobConfiguration configuration, final JobEntity jobEntity) {
    }

    public static class BatchSeedJobConfiguration implements JobHandlerConfiguration {
        protected String batchId;

        public BatchSeedJobConfiguration(final String batchId) {
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
