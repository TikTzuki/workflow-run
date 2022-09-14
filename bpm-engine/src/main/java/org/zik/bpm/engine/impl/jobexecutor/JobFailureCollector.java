// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContextListener;

public class JobFailureCollector implements CommandContextListener
{
    protected Throwable failure;
    protected JobEntity job;
    protected String jobId;
    protected String failedActivityId;
    
    public JobFailureCollector(final String jobId) {
        this.jobId = jobId;
    }
    
    public void setFailure(final Throwable failure) {
        if (this.failure == null) {
            this.failure = failure;
        }
    }
    
    public Throwable getFailure() {
        return this.failure;
    }
    
    @Override
    public void onCommandFailed(final CommandContext commandContext, final Throwable t) {
        this.setFailure(t);
    }
    
    @Override
    public void onCommandContextClose(final CommandContext commandContext) {
    }
    
    public void setJob(final JobEntity job) {
        this.job = job;
    }
    
    public JobEntity getJob() {
        return this.job;
    }
    
    public String getJobId() {
        return this.jobId;
    }
    
    public String getFailedActivityId() {
        return this.failedActivityId;
    }
    
    public void setFailedActivityId(final String activityId) {
        this.failedActivityId = activityId;
    }
}
