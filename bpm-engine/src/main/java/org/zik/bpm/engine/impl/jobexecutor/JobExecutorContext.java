// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import java.util.LinkedList;
import org.zik.bpm.engine.impl.db.entitymanager.cache.DbEntityCache;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import java.util.List;

public class JobExecutorContext
{
    protected List<String> currentProcessorJobQueue;
    protected JobEntity currentJob;
    protected DbEntityCache entityCache;
    
    public JobExecutorContext() {
        this.currentProcessorJobQueue = new LinkedList<String>();
    }
    
    public List<String> getCurrentProcessorJobQueue() {
        return this.currentProcessorJobQueue;
    }
    
    public boolean isExecutingExclusiveJob() {
        return this.currentJob != null && this.currentJob.isExclusive();
    }
    
    public void setCurrentJob(final JobEntity currentJob) {
        this.currentJob = currentJob;
    }
    
    public JobEntity getCurrentJob() {
        return this.currentJob;
    }
    
    public DbEntityCache getEntityCache() {
        return this.entityCache;
    }
    
    public void setEntityCache(final DbEntityCache entityCache) {
        this.entityCache = entityCache;
    }
}
