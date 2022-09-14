// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import org.zik.bpm.engine.history.CleanableHistoricProcessInstanceReportResult;
import org.zik.bpm.engine.history.CleanableHistoricProcessInstanceReport;

public class CleanableHistoricProcessInstanceReportImpl extends AbstractQuery<CleanableHistoricProcessInstanceReport, CleanableHistoricProcessInstanceReportResult> implements CleanableHistoricProcessInstanceReport
{
    private static final long serialVersionUID = 1L;
    protected String[] processDefinitionIdIn;
    protected String[] processDefinitionKeyIn;
    protected String[] tenantIdIn;
    protected boolean isTenantIdSet;
    protected boolean isCompact;
    protected Date currentTimestamp;
    protected boolean isHistoryCleanupStrategyRemovalTimeBased;
    
    public CleanableHistoricProcessInstanceReportImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
        this.isCompact = false;
    }
    
    @Override
    public CleanableHistoricProcessInstanceReport processDefinitionIdIn(final String... processDefinitionIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "processDefinitionIdIn", (Object[])processDefinitionIds);
        this.processDefinitionIdIn = processDefinitionIds;
        return this;
    }
    
    @Override
    public CleanableHistoricProcessInstanceReport processDefinitionKeyIn(final String... processDefinitionKeys) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "processDefinitionKeyIn", (Object[])processDefinitionKeys);
        this.processDefinitionKeyIn = processDefinitionKeys;
        return this;
    }
    
    @Override
    public CleanableHistoricProcessInstanceReport tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "tenantIdIn", (Object[])tenantIds);
        this.tenantIdIn = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CleanableHistoricProcessInstanceReport withoutTenantId() {
        this.tenantIdIn = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CleanableHistoricProcessInstanceReport compact() {
        this.isCompact = true;
        return this;
    }
    
    @Override
    public CleanableHistoricProcessInstanceReport orderByFinished() {
        this.orderBy(CleanableHistoricInstanceReportProperty.FINISHED_AMOUNT);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.provideHistoryCleanupStrategy(commandContext);
        this.checkQueryOk();
        return commandContext.getHistoricProcessInstanceManager().findCleanableHistoricProcessInstancesReportCountByCriteria(this);
    }
    
    @Override
    public List<CleanableHistoricProcessInstanceReportResult> executeList(final CommandContext commandContext, final Page page) {
        this.provideHistoryCleanupStrategy(commandContext);
        this.checkQueryOk();
        return commandContext.getHistoricProcessInstanceManager().findCleanableHistoricProcessInstancesReportByCriteria(this, page);
    }
    
    public Date getCurrentTimestamp() {
        return this.currentTimestamp;
    }
    
    public void setCurrentTimestamp(final Date currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
    }
    
    public String[] getProcessDefinitionIdIn() {
        return this.processDefinitionIdIn;
    }
    
    public String[] getProcessDefinitionKeyIn() {
        return this.processDefinitionKeyIn;
    }
    
    public String[] getTenantIdIn() {
        return this.tenantIdIn;
    }
    
    public void setTenantIdIn(final String[] tenantIdIn) {
        this.tenantIdIn = tenantIdIn;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public boolean isCompact() {
        return this.isCompact;
    }
    
    protected void provideHistoryCleanupStrategy(final CommandContext commandContext) {
        final String historyCleanupStrategy = commandContext.getProcessEngineConfiguration().getHistoryCleanupStrategy();
        this.isHistoryCleanupStrategyRemovalTimeBased = "removalTimeBased".equals(historyCleanupStrategy);
    }
    
    public boolean isHistoryCleanupStrategyRemovalTimeBased() {
        return this.isHistoryCleanupStrategyRemovalTimeBased;
    }
}
