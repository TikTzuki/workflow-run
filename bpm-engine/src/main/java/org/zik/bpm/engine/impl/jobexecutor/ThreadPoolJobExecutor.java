// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import java.util.concurrent.RejectedExecutionException;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadPoolJobExecutor extends JobExecutor
{
    protected ThreadPoolExecutor threadPoolExecutor;
    
    @Override
    protected void startExecutingJobs() {
        this.startJobAcquisitionThread();
    }
    
    @Override
    protected void stopExecutingJobs() {
        this.stopJobAcquisitionThread();
    }
    
    @Override
    public void executeJobs(final List<String> jobIds, final ProcessEngineImpl processEngine) {
        try {
            this.threadPoolExecutor.execute(this.getExecuteJobsRunnable(jobIds, processEngine));
        }
        catch (RejectedExecutionException e) {
            this.logRejectedExecution(processEngine, jobIds.size());
            this.rejectedJobsHandler.jobsRejected(jobIds, processEngine, this);
        }
    }
    
    public ThreadPoolExecutor getThreadPoolExecutor() {
        return this.threadPoolExecutor;
    }
    
    public void setThreadPoolExecutor(final ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }
}
