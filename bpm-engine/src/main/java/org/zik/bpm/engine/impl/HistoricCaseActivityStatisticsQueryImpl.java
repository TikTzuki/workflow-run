// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.history.HistoricCaseActivityStatistics;
import org.zik.bpm.engine.history.HistoricCaseActivityStatisticsQuery;

public class HistoricCaseActivityStatisticsQueryImpl extends AbstractQuery<HistoricCaseActivityStatisticsQuery, HistoricCaseActivityStatistics> implements HistoricCaseActivityStatisticsQuery
{
    private static final long serialVersionUID = 1L;
    protected String caseDefinitionId;
    
    public HistoricCaseActivityStatisticsQueryImpl(final String caseDefinitionId, final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.caseDefinitionId = caseDefinitionId;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricStatisticsManager().getHistoricStatisticsCountGroupedByCaseActivity(this);
    }
    
    @Override
    public List<HistoricCaseActivityStatistics> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricStatisticsManager().getHistoricStatisticsGroupedByCaseActivity(this, page);
    }
    
    @Override
    protected void checkQueryOk() {
        super.checkQueryOk();
        EnsureUtil.ensureNotNull("No valid case definition id supplied", "caseDefinitionId", this.caseDefinitionId);
    }
    
    public String getCaseDefinitionId() {
        return this.caseDefinitionId;
    }
}
