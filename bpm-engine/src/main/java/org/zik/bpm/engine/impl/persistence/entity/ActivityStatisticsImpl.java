// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.management.IncidentStatistics;
import java.util.List;
import org.zik.bpm.engine.management.ActivityStatistics;

public class ActivityStatisticsImpl implements ActivityStatistics
{
    protected String id;
    protected int instances;
    protected int failedJobs;
    protected List<IncidentStatistics> incidentStatistics;
    
    @Override
    public String getId() {
        return this.id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    @Override
    public int getInstances() {
        return this.instances;
    }
    
    public void setInstances(final int instances) {
        this.instances = instances;
    }
    
    @Override
    public int getFailedJobs() {
        return this.failedJobs;
    }
    
    public void setFailedJobs(final int failedJobs) {
        this.failedJobs = failedJobs;
    }
    
    @Override
    public List<IncidentStatistics> getIncidentStatistics() {
        return this.incidentStatistics;
    }
    
    public void setIncidentStatistics(final List<IncidentStatistics> incidentStatistics) {
        this.incidentStatistics = incidentStatistics;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[id=" + this.id + ", instances=" + this.instances + ", failedJobs=" + this.failedJobs + "]";
    }
}
