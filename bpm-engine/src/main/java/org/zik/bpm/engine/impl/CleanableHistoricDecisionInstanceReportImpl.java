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
import org.zik.bpm.engine.history.CleanableHistoricDecisionInstanceReportResult;
import org.zik.bpm.engine.history.CleanableHistoricDecisionInstanceReport;

public class CleanableHistoricDecisionInstanceReportImpl extends AbstractQuery<CleanableHistoricDecisionInstanceReport, CleanableHistoricDecisionInstanceReportResult> implements CleanableHistoricDecisionInstanceReport
{
    private static final long serialVersionUID = 1L;
    protected String[] decisionDefinitionIdIn;
    protected String[] decisionDefinitionKeyIn;
    protected String[] tenantIdIn;
    protected boolean isTenantIdSet;
    protected boolean isCompact;
    protected Date currentTimestamp;
    protected boolean isHistoryCleanupStrategyRemovalTimeBased;
    
    public CleanableHistoricDecisionInstanceReportImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
        this.isCompact = false;
    }
    
    @Override
    public CleanableHistoricDecisionInstanceReport decisionDefinitionIdIn(final String... decisionDefinitionIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "decisionDefinitionIdIn", (Object[])decisionDefinitionIds);
        this.decisionDefinitionIdIn = decisionDefinitionIds;
        return this;
    }
    
    @Override
    public CleanableHistoricDecisionInstanceReport decisionDefinitionKeyIn(final String... decisionDefinitionKeys) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "decisionDefinitionKeyIn", (Object[])decisionDefinitionKeys);
        this.decisionDefinitionKeyIn = decisionDefinitionKeys;
        return this;
    }
    
    @Override
    public CleanableHistoricDecisionInstanceReport tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "tenantIdIn", (Object[])tenantIds);
        this.tenantIdIn = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CleanableHistoricDecisionInstanceReport withoutTenantId() {
        this.tenantIdIn = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CleanableHistoricDecisionInstanceReport compact() {
        this.isCompact = true;
        return this;
    }
    
    @Override
    public CleanableHistoricDecisionInstanceReport orderByFinished() {
        this.orderBy(CleanableHistoricInstanceReportProperty.FINISHED_AMOUNT);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.provideHistoryCleanupStrategy(commandContext);
        this.checkQueryOk();
        return commandContext.getHistoricDecisionInstanceManager().findCleanableHistoricDecisionInstancesReportCountByCriteria(this);
    }
    
    @Override
    public List<CleanableHistoricDecisionInstanceReportResult> executeList(final CommandContext commandContext, final Page page) {
        this.provideHistoryCleanupStrategy(commandContext);
        this.checkQueryOk();
        return commandContext.getHistoricDecisionInstanceManager().findCleanableHistoricDecisionInstancesReportByCriteria(this, page);
    }
    
    public String[] getDecisionDefinitionIdIn() {
        return this.decisionDefinitionIdIn;
    }
    
    public void setDecisionDefinitionIdIn(final String[] decisionDefinitionIdIn) {
        this.decisionDefinitionIdIn = decisionDefinitionIdIn;
    }
    
    public String[] getDecisionDefinitionKeyIn() {
        return this.decisionDefinitionKeyIn;
    }
    
    public void setDecisionDefinitionKeyIn(final String[] decisionDefinitionKeyIn) {
        this.decisionDefinitionKeyIn = decisionDefinitionKeyIn;
    }
    
    public Date getCurrentTimestamp() {
        return this.currentTimestamp;
    }
    
    public void setCurrentTimestamp(final Date currentTimestamp) {
        this.currentTimestamp = currentTimestamp;
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
