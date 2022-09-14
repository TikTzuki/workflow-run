// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class BackoffJobAcquisitionStrategy implements JobAcquisitionStrategy
{
    public static long DEFAULT_EXECUTION_SATURATION_WAIT_TIME;
    protected long baseIdleWaitTime;
    protected float idleIncreaseFactor;
    protected int idleLevel;
    protected int maxIdleLevel;
    protected long maxIdleWaitTime;
    protected long baseBackoffWaitTime;
    protected float backoffIncreaseFactor;
    protected int backoffLevel;
    protected int maxBackoffLevel;
    protected long maxBackoffWaitTime;
    protected boolean applyJitter;
    protected int numAcquisitionsWithoutLockingFailure;
    protected int backoffDecreaseThreshold;
    protected int baseNumJobsToAcquire;
    protected Map<String, Integer> jobsToAcquire;
    protected boolean executionSaturated;
    protected long executionSaturationWaitTime;
    
    public BackoffJobAcquisitionStrategy(final long baseIdleWaitTime, final float idleIncreaseFactor, final long maxIdleTime, final long baseBackoffWaitTime, final float backoffIncreaseFactor, final long maxBackoffTime, final int backoffDecreaseThreshold, final int baseNumJobsToAcquire) {
        this.applyJitter = false;
        this.numAcquisitionsWithoutLockingFailure = 0;
        this.jobsToAcquire = new HashMap<String, Integer>();
        this.executionSaturated = false;
        this.executionSaturationWaitTime = BackoffJobAcquisitionStrategy.DEFAULT_EXECUTION_SATURATION_WAIT_TIME;
        this.baseIdleWaitTime = baseIdleWaitTime;
        this.idleIncreaseFactor = idleIncreaseFactor;
        this.idleLevel = 0;
        this.maxIdleWaitTime = maxIdleTime;
        this.baseBackoffWaitTime = baseBackoffWaitTime;
        this.backoffIncreaseFactor = backoffIncreaseFactor;
        this.backoffLevel = 0;
        this.maxBackoffWaitTime = maxBackoffTime;
        this.backoffDecreaseThreshold = backoffDecreaseThreshold;
        this.baseNumJobsToAcquire = baseNumJobsToAcquire;
        this.initializeMaxLevels();
    }
    
    public BackoffJobAcquisitionStrategy(final JobExecutor jobExecutor) {
        this(jobExecutor.getWaitTimeInMillis(), jobExecutor.getWaitIncreaseFactor(), jobExecutor.getMaxWait(), jobExecutor.getBackoffTimeInMillis(), jobExecutor.getWaitIncreaseFactor(), jobExecutor.getMaxBackoff(), jobExecutor.getBackoffDecreaseThreshold(), jobExecutor.getMaxJobsPerAcquisition());
    }
    
    protected void initializeMaxLevels() {
        if (this.baseIdleWaitTime > 0L && this.maxIdleWaitTime > 0L && this.idleIncreaseFactor > 0.0f && this.maxIdleWaitTime >= this.baseIdleWaitTime) {
            this.maxIdleLevel = (int)this.log(this.idleIncreaseFactor, (double)(this.maxIdleWaitTime / this.baseIdleWaitTime)) + 1;
            ++this.maxIdleLevel;
        }
        else {
            this.maxIdleLevel = 0;
        }
        if (this.baseBackoffWaitTime > 0L && this.maxBackoffWaitTime > 0L && this.backoffIncreaseFactor > 0.0f && this.maxBackoffWaitTime >= this.baseBackoffWaitTime) {
            this.maxBackoffLevel = (int)this.log(this.backoffIncreaseFactor, (double)(this.maxBackoffWaitTime / this.baseBackoffWaitTime)) + 1;
            ++this.maxBackoffLevel;
        }
        else {
            this.maxBackoffLevel = 0;
        }
    }
    
    protected double log(final double base, final double value) {
        return Math.log10(value) / Math.log10(base);
    }
    
    @Override
    public void reconfigure(final JobAcquisitionContext context) {
        this.reconfigureIdleLevel(context);
        this.reconfigureBackoffLevel(context);
        this.reconfigureNumberOfJobsToAcquire(context);
        this.executionSaturated = this.allSubmittedJobsRejected(context);
    }
    
    protected boolean allSubmittedJobsRejected(final JobAcquisitionContext context) {
        for (final Map.Entry<String, AcquiredJobs> acquiredJobsForEngine : context.getAcquiredJobsByEngine().entrySet()) {
            final String engineName = acquiredJobsForEngine.getKey();
            final List<List<String>> acquiredJobBatches = acquiredJobsForEngine.getValue().getJobIdBatches();
            final List<List<String>> resubmittedJobBatches = context.getAdditionalJobsByEngine().get(engineName);
            final List<List<String>> rejectedJobBatches = context.getRejectedJobsByEngine().get(engineName);
            int numJobsSubmittedForExecution = acquiredJobBatches.size();
            if (resubmittedJobBatches != null) {
                numJobsSubmittedForExecution += resubmittedJobBatches.size();
            }
            int numJobsRejected = 0;
            if (rejectedJobBatches != null) {
                numJobsRejected += rejectedJobBatches.size();
            }
            if (numJobsRejected == 0 || numJobsSubmittedForExecution > numJobsRejected) {
                return false;
            }
        }
        return true;
    }
    
    protected void reconfigureIdleLevel(final JobAcquisitionContext context) {
        if (context.isJobAdded()) {
            this.idleLevel = 0;
        }
        else if (context.areAllEnginesIdle() || context.getAcquisitionException() != null) {
            if (this.idleLevel < this.maxIdleLevel) {
                ++this.idleLevel;
            }
        }
        else {
            this.idleLevel = 0;
        }
    }
    
    protected void reconfigureBackoffLevel(final JobAcquisitionContext context) {
        if (context.hasJobAcquisitionLockFailureOccurred()) {
            this.numAcquisitionsWithoutLockingFailure = 0;
            this.applyJitter = true;
            if (this.backoffLevel < this.maxBackoffLevel) {
                ++this.backoffLevel;
            }
        }
        else {
            this.applyJitter = false;
            ++this.numAcquisitionsWithoutLockingFailure;
            if (this.numAcquisitionsWithoutLockingFailure >= this.backoffDecreaseThreshold && this.backoffLevel > 0) {
                --this.backoffLevel;
                this.numAcquisitionsWithoutLockingFailure = 0;
            }
        }
    }
    
    protected void reconfigureNumberOfJobsToAcquire(final JobAcquisitionContext context) {
        this.jobsToAcquire.clear();
        for (final Map.Entry<String, AcquiredJobs> acquiredJobsEntry : context.getAcquiredJobsByEngine().entrySet()) {
            final String engineName = acquiredJobsEntry.getKey();
            int numJobsToAcquire = (int)(this.baseNumJobsToAcquire * Math.pow(this.backoffIncreaseFactor, this.backoffLevel));
            final List<List<String>> rejectedJobBatchesForEngine = context.getRejectedJobsByEngine().get(engineName);
            if (rejectedJobBatchesForEngine != null) {
                numJobsToAcquire -= rejectedJobBatchesForEngine.size();
            }
            numJobsToAcquire = Math.max(0, numJobsToAcquire);
            this.jobsToAcquire.put(engineName, numJobsToAcquire);
        }
    }
    
    @Override
    public long getWaitTime() {
        if (this.idleLevel > 0) {
            return this.calculateIdleTime();
        }
        if (this.backoffLevel > 0) {
            return this.calculateBackoffTime();
        }
        if (this.executionSaturated) {
            return this.executionSaturationWaitTime;
        }
        return 0L;
    }
    
    protected long calculateIdleTime() {
        if (this.idleLevel <= 0) {
            return 0L;
        }
        if (this.idleLevel >= this.maxIdleLevel) {
            return this.maxIdleWaitTime;
        }
        return (long)(this.baseIdleWaitTime * Math.pow(this.idleIncreaseFactor, this.idleLevel - 1));
    }
    
    protected long calculateBackoffTime() {
        long backoffTime = 0L;
        if (this.backoffLevel <= 0) {
            backoffTime = 0L;
        }
        else if (this.backoffLevel >= this.maxBackoffLevel) {
            backoffTime = this.maxBackoffWaitTime;
        }
        else {
            backoffTime = (long)(this.baseBackoffWaitTime * Math.pow(this.backoffIncreaseFactor, this.backoffLevel - 1));
        }
        if (this.applyJitter) {
            backoffTime += (long)(Math.random() * (backoffTime / 2L));
        }
        return backoffTime;
    }
    
    @Override
    public int getNumJobsToAcquire(final String processEngine) {
        final Integer numJobsToAcquire = this.jobsToAcquire.get(processEngine);
        if (numJobsToAcquire != null) {
            return numJobsToAcquire;
        }
        return this.baseNumJobsToAcquire;
    }
    
    static {
        BackoffJobAcquisitionStrategy.DEFAULT_EXECUTION_SATURATION_WAIT_TIME = 100L;
    }
}
