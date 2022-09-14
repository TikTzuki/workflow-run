// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.repository.ProcessDefinition;
import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import java.util.Collections;
import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.history.DurationReportResult;
import java.util.List;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.db.TenantCheck;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.query.PeriodUnit;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricProcessInstanceReport;

public class HistoricProcessInstanceReportImpl implements HistoricProcessInstanceReport
{
    private static final long serialVersionUID = 1L;
    protected Date startedAfter;
    protected Date startedBefore;
    protected String[] processDefinitionIdIn;
    protected String[] processDefinitionKeyIn;
    protected PeriodUnit durationPeriodUnit;
    protected CommandExecutor commandExecutor;
    protected TenantCheck tenantCheck;
    
    public HistoricProcessInstanceReportImpl(final CommandExecutor commandExecutor) {
        this.tenantCheck = new TenantCheck();
        this.commandExecutor = commandExecutor;
    }
    
    @Override
    public HistoricProcessInstanceReport startedAfter(final Date startedAfter) {
        EnsureUtil.ensureNotNull(NotValidException.class, "startedAfter", startedAfter);
        this.startedAfter = startedAfter;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceReport startedBefore(final Date startedBefore) {
        EnsureUtil.ensureNotNull(NotValidException.class, "startedBefore", startedBefore);
        this.startedBefore = startedBefore;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceReport processDefinitionIdIn(final String... processDefinitionIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "processDefinitionIdIn", (Object[])processDefinitionIds);
        this.processDefinitionIdIn = processDefinitionIds;
        return this;
    }
    
    @Override
    public HistoricProcessInstanceReport processDefinitionKeyIn(final String... processDefinitionKeys) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "processDefinitionKeyIn", (Object[])processDefinitionKeys);
        this.processDefinitionKeyIn = processDefinitionKeys;
        return this;
    }
    
    @Override
    public List<DurationReportResult> duration(final PeriodUnit periodUnit) {
        EnsureUtil.ensureNotNull(NotValidException.class, "periodUnit", periodUnit);
        this.durationPeriodUnit = periodUnit;
        final CommandContext commandContext = Context.getCommandContext();
        if (commandContext == null) {
            return this.commandExecutor.execute((Command<List<DurationReportResult>>)new ExecuteDurationReportCmd());
        }
        return this.executeDurationReport(commandContext);
    }
    
    public List<DurationReportResult> executeDurationReport(final CommandContext commandContext) {
        this.doAuthCheck(commandContext);
        if (CompareUtil.areNotInAscendingOrder(this.startedAfter, this.startedBefore)) {
            return Collections.emptyList();
        }
        return commandContext.getHistoricReportManager().selectHistoricProcessInstanceDurationReport(this);
    }
    
    protected void doAuthCheck(final CommandContext commandContext) {
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            if (this.processDefinitionIdIn == null && this.processDefinitionKeyIn == null) {
                checker.checkReadHistoryAnyProcessDefinition();
            }
            else {
                final List<String> processDefinitionKeys = new ArrayList<String>();
                if (this.processDefinitionKeyIn != null) {
                    processDefinitionKeys.addAll(Arrays.asList(this.processDefinitionKeyIn));
                }
                if (this.processDefinitionIdIn != null) {
                    for (final String processDefinitionId : this.processDefinitionIdIn) {
                        final ProcessDefinition processDefinition = commandContext.getProcessDefinitionManager().findLatestProcessDefinitionById(processDefinitionId);
                        if (processDefinition != null && processDefinition.getKey() != null) {
                            processDefinitionKeys.add(processDefinition.getKey());
                        }
                    }
                }
                if (processDefinitionKeys.isEmpty()) {
                    continue;
                }
                for (final String processDefinitionKey : processDefinitionKeys) {
                    checker.checkReadHistoryProcessDefinition(processDefinitionKey);
                }
            }
        }
    }
    
    public Date getStartedAfter() {
        return this.startedAfter;
    }
    
    public Date getStartedBefore() {
        return this.startedBefore;
    }
    
    public String[] getProcessDefinitionIdIn() {
        return this.processDefinitionIdIn;
    }
    
    public String[] getProcessDefinitionKeyIn() {
        return this.processDefinitionKeyIn;
    }
    
    public TenantCheck getTenantCheck() {
        return this.tenantCheck;
    }
    
    public String getReportPeriodUnitName() {
        return this.durationPeriodUnit.name();
    }
    
    protected class ExecuteDurationReportCmd implements Command<List<DurationReportResult>>
    {
        @Override
        public List<DurationReportResult> execute(final CommandContext commandContext) {
            return HistoricProcessInstanceReportImpl.this.executeDurationReport(commandContext);
        }
    }
}
