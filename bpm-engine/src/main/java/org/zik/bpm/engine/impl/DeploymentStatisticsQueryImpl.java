// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.Collection;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.db.PermissionCheck;
import java.util.List;
import org.zik.bpm.engine.management.DeploymentStatistics;
import org.zik.bpm.engine.management.DeploymentStatisticsQuery;

public class DeploymentStatisticsQueryImpl extends AbstractQuery<DeploymentStatisticsQuery, DeploymentStatistics> implements DeploymentStatisticsQuery
{
    protected static final long serialVersionUID = 1L;
    protected boolean includeFailedJobs;
    protected boolean includeIncidents;
    protected String includeIncidentsForType;
    protected List<PermissionCheck> processInstancePermissionChecks;
    protected List<PermissionCheck> jobPermissionChecks;
    protected List<PermissionCheck> incidentPermissionChecks;
    
    public DeploymentStatisticsQueryImpl(final CommandExecutor executor) {
        super(executor);
        this.includeFailedJobs = false;
        this.includeIncidents = false;
        this.processInstancePermissionChecks = new ArrayList<PermissionCheck>();
        this.jobPermissionChecks = new ArrayList<PermissionCheck>();
        this.incidentPermissionChecks = new ArrayList<PermissionCheck>();
    }
    
    @Override
    public DeploymentStatisticsQuery includeFailedJobs() {
        this.includeFailedJobs = true;
        return this;
    }
    
    @Override
    public DeploymentStatisticsQuery includeIncidents() {
        this.includeIncidents = true;
        return this;
    }
    
    @Override
    public DeploymentStatisticsQuery includeIncidentsForType(final String incidentType) {
        this.includeIncidentsForType = incidentType;
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getStatisticsManager().getStatisticsCountGroupedByDeployment(this);
    }
    
    @Override
    public List<DeploymentStatistics> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getStatisticsManager().getStatisticsGroupedByDeployment(this, page);
    }
    
    public boolean isFailedJobsToInclude() {
        return this.includeFailedJobs;
    }
    
    public boolean isIncidentsToInclude() {
        return this.includeIncidents || this.includeIncidentsForType != null;
    }
    
    @Override
    protected void checkQueryOk() {
        super.checkQueryOk();
        if (this.includeIncidents && this.includeIncidentsForType != null) {
            throw new ProcessEngineException("Invalid query: It is not possible to use includeIncident() and includeIncidentForType() to execute one query.");
        }
    }
    
    public List<PermissionCheck> getProcessInstancePermissionChecks() {
        return this.processInstancePermissionChecks;
    }
    
    public void setProcessInstancePermissionChecks(final List<PermissionCheck> processInstancePermissionChecks) {
        this.processInstancePermissionChecks = processInstancePermissionChecks;
    }
    
    public void addProcessInstancePermissionCheck(final List<PermissionCheck> permissionChecks) {
        this.processInstancePermissionChecks.addAll(permissionChecks);
    }
    
    public List<PermissionCheck> getJobPermissionChecks() {
        return this.jobPermissionChecks;
    }
    
    public void setJobPermissionChecks(final List<PermissionCheck> jobPermissionChecks) {
        this.jobPermissionChecks = jobPermissionChecks;
    }
    
    public void addJobPermissionCheck(final List<PermissionCheck> permissionChecks) {
        this.jobPermissionChecks.addAll(permissionChecks);
    }
    
    public List<PermissionCheck> getIncidentPermissionChecks() {
        return this.incidentPermissionChecks;
    }
    
    public void setIncidentPermissionChecks(final List<PermissionCheck> incidentPermissionChecks) {
        this.incidentPermissionChecks = incidentPermissionChecks;
    }
    
    public void addIncidentPermissionCheck(final List<PermissionCheck> permissionChecks) {
        this.incidentPermissionChecks.addAll(permissionChecks);
    }
}
