// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.ProcessEngineException;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.management.ProcessDefinitionStatistics;
import org.zik.bpm.engine.management.ProcessDefinitionStatisticsQuery;

public class ProcessDefinitionStatisticsQueryImpl extends AbstractQuery<ProcessDefinitionStatisticsQuery, ProcessDefinitionStatistics> implements ProcessDefinitionStatisticsQuery
{
    protected static final long serialVersionUID = 1L;
    protected boolean includeFailedJobs;
    protected boolean includeIncidents;
    protected boolean includeRootIncidents;
    protected String includeIncidentsForType;
    
    public ProcessDefinitionStatisticsQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.includeFailedJobs = false;
        this.includeIncidents = false;
        this.includeRootIncidents = false;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getStatisticsManager().getStatisticsCountGroupedByProcessDefinitionVersion(this);
    }
    
    @Override
    public List<ProcessDefinitionStatistics> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getStatisticsManager().getStatisticsGroupedByProcessDefinitionVersion(this, page);
    }
    
    @Override
    public ProcessDefinitionStatisticsQuery includeFailedJobs() {
        this.includeFailedJobs = true;
        return this;
    }
    
    @Override
    public ProcessDefinitionStatisticsQuery includeIncidents() {
        this.includeIncidents = true;
        return this;
    }
    
    @Override
    public ProcessDefinitionStatisticsQuery includeIncidentsForType(final String incidentType) {
        this.includeIncidentsForType = incidentType;
        return this;
    }
    
    public boolean isFailedJobsToInclude() {
        return this.includeFailedJobs;
    }
    
    public boolean isIncidentsToInclude() {
        return this.includeIncidents || this.includeRootIncidents || this.includeIncidentsForType != null;
    }
    
    @Override
    protected void checkQueryOk() {
        super.checkQueryOk();
        if (this.includeIncidents && this.includeIncidentsForType != null) {
            throw new ProcessEngineException("Invalid query: It is not possible to use includeIncident() and includeIncidentForType() to execute one query.");
        }
        if (this.includeRootIncidents && this.includeIncidentsForType != null) {
            throw new ProcessEngineException("Invalid query: It is not possible to use includeRootIncident() and includeIncidentForType() to execute one query.");
        }
        if (this.includeIncidents && this.includeRootIncidents) {
            throw new ProcessEngineException("Invalid query: It is not possible to use includeIncident() and includeRootIncidents() to execute one query.");
        }
    }
    
    @Override
    public ProcessDefinitionStatisticsQuery includeRootIncidents() {
        this.includeRootIncidents = true;
        return this;
    }
}
