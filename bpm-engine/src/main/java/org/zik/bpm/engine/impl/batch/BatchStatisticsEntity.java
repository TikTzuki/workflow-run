// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.batch;

import org.zik.bpm.engine.batch.BatchStatistics;

public class BatchStatisticsEntity extends BatchEntity implements BatchStatistics
{
    protected int remainingJobs;
    protected int failedJobs;
    
    @Override
    public int getRemainingJobs() {
        return this.remainingJobs + this.getJobsToCreate();
    }
    
    public void setRemainingJobs(final int remainingJobs) {
        this.remainingJobs = remainingJobs;
    }
    
    @Override
    public int getCompletedJobs() {
        return this.totalJobs - this.getRemainingJobs();
    }
    
    @Override
    public int getFailedJobs() {
        return this.failedJobs;
    }
    
    public void setFailedJobs(final int failedJobs) {
        this.failedJobs = failedJobs;
    }
    
    public int getJobsToCreate() {
        return this.totalJobs - this.jobsCreated;
    }
    
    @Override
    public String toString() {
        return "BatchStatisticsEntity{batchHandler=" + this.batchJobHandler + ", id='" + this.id + '\'' + ", type='" + this.type + '\'' + ", size=" + this.totalJobs + ", jobCreated=" + this.jobsCreated + ", remainingJobs=" + this.remainingJobs + ", failedJobs=" + this.failedJobs + ", batchJobsPerSeed=" + this.batchJobsPerSeed + ", invocationsPerBatchJob=" + this.invocationsPerBatchJob + ", seedJobDefinitionId='" + this.seedJobDefinitionId + '\'' + ", monitorJobDefinitionId='" + this.seedJobDefinitionId + '\'' + ", batchJobDefinitionId='" + this.batchJobDefinitionId + '\'' + ", configurationId='" + this.configuration.getByteArrayId() + '\'' + '}';
    }
}
