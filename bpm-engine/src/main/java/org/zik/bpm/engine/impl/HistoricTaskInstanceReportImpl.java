// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.history.DurationReportResult;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.history.HistoricTaskInstanceReportResult;
import java.util.List;
import org.zik.bpm.engine.impl.db.TenantCheck;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.query.PeriodUnit;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricTaskInstanceReport;

public class HistoricTaskInstanceReportImpl implements HistoricTaskInstanceReport
{
    protected Date completedAfter;
    protected Date completedBefore;
    protected PeriodUnit durationPeriodUnit;
    protected CommandExecutor commandExecutor;
    protected TenantCheck tenantCheck;
    
    public HistoricTaskInstanceReportImpl(final CommandExecutor commandExecutor) {
        this.tenantCheck = new TenantCheck();
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public List<HistoricTaskInstanceReportResult> countByProcessDefinitionKey() {
        final CommandContext commandContext = Context.getCommandContext();
        if (commandContext == null) {
            return this.commandExecutor.execute((Command<List<HistoricTaskInstanceReportResult>>)new HistoricTaskInstanceCountByProcessDefinitionKey());
        }
        return this.executeCountByProcessDefinitionKey(commandContext);
    }
    
    protected List<HistoricTaskInstanceReportResult> executeCountByProcessDefinitionKey(final CommandContext commandContext) {
        return commandContext.getTaskReportManager().selectHistoricTaskInstanceCountByProcDefKeyReport(this);
    }
    
    @Override
    public List<HistoricTaskInstanceReportResult> countByTaskName() {
        final CommandContext commandContext = Context.getCommandContext();
        if (commandContext == null) {
            return this.commandExecutor.execute((Command<List<HistoricTaskInstanceReportResult>>)new HistoricTaskInstanceCountByNameCmd());
        }
        return this.executeCountByTaskName(commandContext);
    }
    
    protected List<HistoricTaskInstanceReportResult> executeCountByTaskName(final CommandContext commandContext) {
        return commandContext.getTaskReportManager().selectHistoricTaskInstanceCountByTaskNameReport(this);
    }
    
    @Override
    public List<DurationReportResult> duration(final PeriodUnit periodUnit) {
        EnsureUtil.ensureNotNull(NotValidException.class, "periodUnit", periodUnit);
        this.durationPeriodUnit = periodUnit;
        final CommandContext commandContext = Context.getCommandContext();
        if (commandContext == null) {
            return this.commandExecutor.execute((Command<List<DurationReportResult>>)new ExecuteDurationCmd());
        }
        return this.executeDuration(commandContext);
    }
    
    protected List<DurationReportResult> executeDuration(final CommandContext commandContext) {
        return commandContext.getTaskReportManager().createHistoricTaskDurationReport(this);
    }
    
    public Date getCompletedAfter() {
        return this.completedAfter;
    }
    
    public Date getCompletedBefore() {
        return this.completedBefore;
    }
    
    @Override
    public HistoricTaskInstanceReport completedAfter(final Date completedAfter) {
        EnsureUtil.ensureNotNull(NotValidException.class, "completedAfter", completedAfter);
        this.completedAfter = completedAfter;
        return this;
    }
    
    @Override
    public HistoricTaskInstanceReport completedBefore(final Date completedBefore) {
        EnsureUtil.ensureNotNull(NotValidException.class, "completedBefore", completedBefore);
        this.completedBefore = completedBefore;
        return this;
    }
    
    public TenantCheck getTenantCheck() {
        return this.tenantCheck;
    }
    
    public String getReportPeriodUnitName() {
        return this.durationPeriodUnit.name();
    }
    
    protected class ExecuteDurationCmd implements Command<List<DurationReportResult>>
    {
        @Override
        public List<DurationReportResult> execute(final CommandContext commandContext) {
            return HistoricTaskInstanceReportImpl.this.executeDuration(commandContext);
        }
    }
    
    protected class HistoricTaskInstanceCountByNameCmd implements Command<List<HistoricTaskInstanceReportResult>>
    {
        @Override
        public List<HistoricTaskInstanceReportResult> execute(final CommandContext commandContext) {
            return HistoricTaskInstanceReportImpl.this.executeCountByTaskName(commandContext);
        }
    }
    
    protected class HistoricTaskInstanceCountByProcessDefinitionKey implements Command<List<HistoricTaskInstanceReportResult>>
    {
        @Override
        public List<HistoricTaskInstanceReportResult> execute(final CommandContext commandContext) {
            return HistoricTaskInstanceReportImpl.this.executeCountByProcessDefinitionKey(commandContext);
        }
    }
}
