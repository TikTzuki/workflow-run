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
import org.zik.bpm.engine.history.CleanableHistoricCaseInstanceReportResult;
import org.zik.bpm.engine.history.CleanableHistoricCaseInstanceReport;

public class CleanableHistoricCaseInstanceReportImpl extends AbstractQuery<CleanableHistoricCaseInstanceReport, CleanableHistoricCaseInstanceReportResult> implements CleanableHistoricCaseInstanceReport
{
    private static final long serialVersionUID = 1L;
    protected String[] caseDefinitionIdIn;
    protected String[] caseDefinitionKeyIn;
    protected String[] tenantIdIn;
    protected boolean isTenantIdSet;
    protected boolean isCompact;
    protected Date currentTimestamp;
    
    public CleanableHistoricCaseInstanceReportImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
        this.isCompact = false;
    }
    
    @Override
    public CleanableHistoricCaseInstanceReport caseDefinitionIdIn(final String... caseDefinitionIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "caseDefinitionIdIn", (Object[])caseDefinitionIds);
        this.caseDefinitionIdIn = caseDefinitionIds;
        return this;
    }
    
    @Override
    public CleanableHistoricCaseInstanceReport caseDefinitionKeyIn(final String... caseDefinitionKeys) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "caseDefinitionKeyIn", (Object[])caseDefinitionKeys);
        this.caseDefinitionKeyIn = caseDefinitionKeys;
        return this;
    }
    
    @Override
    public CleanableHistoricCaseInstanceReport tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "", "tenantIdIn", (Object[])tenantIds);
        this.tenantIdIn = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CleanableHistoricCaseInstanceReport withoutTenantId() {
        this.tenantIdIn = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public CleanableHistoricCaseInstanceReport compact() {
        this.isCompact = true;
        return this;
    }
    
    @Override
    public CleanableHistoricCaseInstanceReport orderByFinished() {
        this.orderBy(CleanableHistoricInstanceReportProperty.FINISHED_AMOUNT);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricCaseInstanceManager().findCleanableHistoricCaseInstancesReportCountByCriteria(this);
    }
    
    @Override
    public List<CleanableHistoricCaseInstanceReportResult> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricCaseInstanceManager().findCleanableHistoricCaseInstancesReportByCriteria(this, page);
    }
    
    public String[] getCaseDefinitionIdIn() {
        return this.caseDefinitionIdIn;
    }
    
    public void setCaseDefinitionIdIn(final String[] caseDefinitionIdIn) {
        this.caseDefinitionIdIn = caseDefinitionIdIn;
    }
    
    public String[] getCaseDefinitionKeyIn() {
        return this.caseDefinitionKeyIn;
    }
    
    public void setCaseDefinitionKeyIn(final String[] caseDefinitionKeyIn) {
        this.caseDefinitionKeyIn = caseDefinitionKeyIn;
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
}
