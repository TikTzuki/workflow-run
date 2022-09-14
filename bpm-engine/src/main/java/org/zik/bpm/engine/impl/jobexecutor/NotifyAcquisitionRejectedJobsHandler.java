// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.util.List;

public class NotifyAcquisitionRejectedJobsHandler implements RejectedJobsHandler
{
    @Override
    public void jobsRejected(final List<String> jobIds, final ProcessEngineImpl processEngine, final JobExecutor jobExecutor) {
        final AcquireJobsRunnable acquireJobsRunnable = jobExecutor.getAcquireJobsRunnable();
        if (acquireJobsRunnable instanceof SequentialJobAcquisitionRunnable) {
            final JobAcquisitionContext context = ((SequentialJobAcquisitionRunnable)acquireJobsRunnable).getAcquisitionContext();
            context.submitRejectedBatch(processEngine.getName(), jobIds);
        }
        else {
            jobExecutor.getExecuteJobsRunnable(jobIds, processEngine).run();
        }
    }
}
