// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import java.util.Iterator;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobAcquisitionContext
{
    protected Map<String, List<List<String>>> rejectedJobBatchesByEngine;
    protected Map<String, AcquiredJobs> acquiredJobsByEngine;
    protected Map<String, List<List<String>>> additionalJobBatchesByEngine;
    protected Exception acquisitionException;
    protected long acquisitionTime;
    protected boolean isJobAdded;
    
    public JobAcquisitionContext() {
        this.rejectedJobBatchesByEngine = new HashMap<String, List<List<String>>>();
        this.additionalJobBatchesByEngine = new HashMap<String, List<List<String>>>();
        this.acquiredJobsByEngine = new HashMap<String, AcquiredJobs>();
    }
    
    public void submitRejectedBatch(final String engineName, final List<String> jobIds) {
        CollectionUtil.addToMapOfLists(this.rejectedJobBatchesByEngine, engineName, jobIds);
    }
    
    public void submitAcquiredJobs(final String engineName, final AcquiredJobs acquiredJobs) {
        this.acquiredJobsByEngine.put(engineName, acquiredJobs);
    }
    
    public void submitAdditionalJobBatch(final String engineName, final List<String> jobIds) {
        CollectionUtil.addToMapOfLists(this.additionalJobBatchesByEngine, engineName, jobIds);
    }
    
    public void reset() {
        this.additionalJobBatchesByEngine.clear();
        this.additionalJobBatchesByEngine.putAll(this.rejectedJobBatchesByEngine);
        this.rejectedJobBatchesByEngine.clear();
        this.acquiredJobsByEngine.clear();
        this.acquisitionException = null;
        this.acquisitionTime = 0L;
        this.isJobAdded = false;
    }
    
    public boolean areAllEnginesIdle() {
        for (final AcquiredJobs acquiredJobs : this.acquiredJobsByEngine.values()) {
            final int jobsAcquired = acquiredJobs.getJobIdBatches().size() + acquiredJobs.getNumberOfJobsFailedToLock();
            if (jobsAcquired >= acquiredJobs.getNumberOfJobsAttemptedToAcquire()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean hasJobAcquisitionLockFailureOccurred() {
        for (final AcquiredJobs acquiredJobs : this.acquiredJobsByEngine.values()) {
            if (acquiredJobs.getNumberOfJobsFailedToLock() > 0) {
                return true;
            }
        }
        return false;
    }
    
    public void setAcquisitionTime(final long acquisitionTime) {
        this.acquisitionTime = acquisitionTime;
    }
    
    public long getAcquisitionTime() {
        return this.acquisitionTime;
    }
    
    public Map<String, AcquiredJobs> getAcquiredJobsByEngine() {
        return this.acquiredJobsByEngine;
    }
    
    public Map<String, List<List<String>>> getRejectedJobsByEngine() {
        return this.rejectedJobBatchesByEngine;
    }
    
    public Map<String, List<List<String>>> getAdditionalJobsByEngine() {
        return this.additionalJobBatchesByEngine;
    }
    
    public void setAcquisitionException(final Exception e) {
        this.acquisitionException = e;
    }
    
    public Exception getAcquisitionException() {
        return this.acquisitionException;
    }
    
    public void setJobAdded(final boolean isJobAdded) {
        this.isJobAdded = isJobAdded;
    }
    
    public boolean isJobAdded() {
        return this.isJobAdded;
    }
}
