// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.util.List;

public class CallerRunsRejectedJobsHandler implements RejectedJobsHandler
{
    @Override
    public void jobsRejected(final List<String> jobIds, final ProcessEngineImpl processEngine, final JobExecutor jobExecutor) {
        jobExecutor.getExecuteJobsRunnable(jobIds, processEngine).run();
    }
}
