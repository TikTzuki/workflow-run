// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricActivityStatistics;
import org.zik.bpm.engine.history.HistoricActivityStatisticsQuery;

public class HistoricActivityStatisticsQueryImpl extends AbstractQuery<HistoricActivityStatisticsQuery, HistoricActivityStatistics> implements HistoricActivityStatisticsQuery
{
    private static final long serialVersionUID = 1L;
    protected String processDefinitionId;
    protected boolean includeFinished;
    protected boolean includeCanceled;
    protected boolean includeCompleteScope;
    protected boolean includeIncidents;
    protected Date startedBefore;
    protected Date startedAfter;
    protected Date finishedBefore;
    protected Date finishedAfter;
    protected String[] processInstanceIds;
    
    public HistoricActivityStatisticsQueryImpl(final String processDefinitionId, final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.processDefinitionId = processDefinitionId;
    }
    
    @Override
    public HistoricActivityStatisticsQuery includeFinished() {
        this.includeFinished = true;
        return this;
    }
    
    @Override
    public HistoricActivityStatisticsQuery includeCanceled() {
        this.includeCanceled = true;
        return this;
    }
    
    @Override
    public HistoricActivityStatisticsQuery includeCompleteScope() {
        this.includeCompleteScope = true;
        return this;
    }
    
    @Override
    public HistoricActivityStatisticsQuery includeIncidents() {
        this.includeIncidents = true;
        return this;
    }
    
    @Override
    public HistoricActivityStatisticsQuery startedAfter(final Date date) {
        this.startedAfter = date;
        return this;
    }
    
    @Override
    public HistoricActivityStatisticsQuery startedBefore(final Date date) {
        this.startedBefore = date;
        return this;
    }
    
    @Override
    public HistoricActivityStatisticsQuery finishedAfter(final Date date) {
        this.finishedAfter = date;
        return this;
    }
    
    @Override
    public HistoricActivityStatisticsQuery finishedBefore(final Date date) {
        this.finishedBefore = date;
        return this;
    }
    
    @Override
    public HistoricActivityStatisticsQuery processInstanceIdIn(final String... processInstanceIds) {
        EnsureUtil.ensureNotNull("processInstanceIds", (Object[])processInstanceIds);
        this.processInstanceIds = processInstanceIds;
        return this;
    }
    
    @Override
    public HistoricActivityStatisticsQuery orderByActivityId() {
        return ((AbstractQuery<HistoricActivityStatisticsQuery, U>)this).orderBy(HistoricActivityStatisticsQueryProperty.ACTIVITY_ID_);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricStatisticsManager().getHistoricStatisticsCountGroupedByActivity(this);
    }
    
    @Override
    public List<HistoricActivityStatistics> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricStatisticsManager().getHistoricStatisticsGroupedByActivity(this, page);
    }
    
    @Override
    protected void checkQueryOk() {
        super.checkQueryOk();
        EnsureUtil.ensureNotNull("No valid process definition id supplied", "processDefinitionId", this.processDefinitionId);
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public boolean isIncludeFinished() {
        return this.includeFinished;
    }
    
    public boolean isIncludeCanceled() {
        return this.includeCanceled;
    }
    
    public boolean isIncludeCompleteScope() {
        return this.includeCompleteScope;
    }
    
    public String[] getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public boolean isIncludeIncidents() {
        return this.includeIncidents;
    }
}
