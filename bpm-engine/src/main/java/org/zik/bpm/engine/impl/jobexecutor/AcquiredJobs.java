// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public class AcquiredJobs
{
    protected int numberOfJobsAttemptedToAcquire;
    protected List<List<String>> acquiredJobBatches;
    protected Set<String> acquiredJobs;
    protected int numberOfJobsFailedToLock;
    
    public AcquiredJobs(final int numberOfJobsAttemptedToAcquire) {
        this.acquiredJobBatches = new ArrayList<List<String>>();
        this.acquiredJobs = new HashSet<String>();
        this.numberOfJobsFailedToLock = 0;
        this.numberOfJobsAttemptedToAcquire = numberOfJobsAttemptedToAcquire;
    }
    
    public List<List<String>> getJobIdBatches() {
        return this.acquiredJobBatches;
    }
    
    public void addJobIdBatch(final List<String> jobIds) {
        if (!jobIds.isEmpty()) {
            this.acquiredJobBatches.add(jobIds);
            this.acquiredJobs.addAll(jobIds);
        }
    }
    
    public void addJobIdBatch(final String jobId) {
        final ArrayList<String> list = new ArrayList<String>();
        list.add(jobId);
        this.addJobIdBatch(list);
    }
    
    public boolean contains(final String jobId) {
        return this.acquiredJobs.contains(jobId);
    }
    
    public int size() {
        return this.acquiredJobs.size();
    }
    
    public void removeJobId(final String id) {
        ++this.numberOfJobsFailedToLock;
        this.acquiredJobs.remove(id);
        final Iterator<List<String>> batchIterator = this.acquiredJobBatches.iterator();
        while (batchIterator.hasNext()) {
            final List<String> batch = batchIterator.next();
            batch.remove(id);
            if (batch.isEmpty()) {
                batchIterator.remove();
            }
        }
    }
    
    public int getNumberOfJobsFailedToLock() {
        return this.numberOfJobsFailedToLock;
    }
    
    public int getNumberOfJobsAttemptedToAcquire() {
        return this.numberOfJobsAttemptedToAcquire;
    }
}
