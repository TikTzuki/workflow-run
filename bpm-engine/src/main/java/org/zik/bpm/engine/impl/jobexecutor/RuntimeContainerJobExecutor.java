// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.container.ExecutorService;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.util.List;
import org.zik.bpm.container.RuntimeContainerDelegate;
import org.zik.bpm.engine.ProcessEngineException;

public class RuntimeContainerJobExecutor extends JobExecutor
{
    @Override
    protected void startExecutingJobs() {
        final RuntimeContainerDelegate runtimeContainerDelegate = this.getRuntimeContainerDelegate();
        if (!runtimeContainerDelegate.getExecutorService().schedule(this.acquireJobsRunnable, true)) {
            throw new ProcessEngineException("Could not schedule AcquireJobsRunnable for execution.");
        }
    }
    
    @Override
    protected void stopExecutingJobs() {
    }
    
    @Override
    public void executeJobs(final List<String> jobIds, final ProcessEngineImpl processEngine) {
        final RuntimeContainerDelegate runtimeContainerDelegate = this.getRuntimeContainerDelegate();
        final ExecutorService executorService = runtimeContainerDelegate.getExecutorService();
        final Runnable executeJobsRunnable = this.getExecuteJobsRunnable(jobIds, processEngine);
        if (!executorService.schedule(executeJobsRunnable, false)) {
            this.logRejectedExecution(processEngine, jobIds.size());
            this.rejectedJobsHandler.jobsRejected(jobIds, processEngine, this);
        }
    }
    
    protected RuntimeContainerDelegate getRuntimeContainerDelegate() {
        return RuntimeContainerDelegate.INSTANCE.get();
    }
    
    @Override
    public Runnable getExecuteJobsRunnable(final List<String> jobIds, final ProcessEngineImpl processEngine) {
        final RuntimeContainerDelegate runtimeContainerDelegate = this.getRuntimeContainerDelegate();
        final ExecutorService executorService = runtimeContainerDelegate.getExecutorService();
        return executorService.getExecuteJobsRunnable(jobIds, processEngine);
    }
}
