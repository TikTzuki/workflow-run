// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.management.IncidentStatistics;
import java.util.List;
import org.zik.bpm.engine.management.ProcessDefinitionStatistics;

public class ProcessDefinitionStatisticsEntity extends ProcessDefinitionEntity implements ProcessDefinitionStatistics
{
    protected static final long serialVersionUID = 1L;
    protected int instances;
    protected int failedJobs;
    protected List<IncidentStatistics> incidentStatistics;
    
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
        return this.getClass().getSimpleName() + "[instances=" + this.instances + ", failedJobs=" + this.failedJobs + ", id=" + this.id + ", deploymentId=" + this.deploymentId + ", description=" + this.description + ", historyLevel=" + this.historyLevel + ", category=" + this.category + ", hasStartFormKey=" + this.hasStartFormKey + ", diagramResourceName=" + this.diagramResourceName + ", key=" + this.key + ", name=" + this.name + ", resourceName=" + this.resourceName + ", revision=" + this.revision + ", version=" + this.version + ", suspensionState=" + this.suspensionState + "]";
    }
}
