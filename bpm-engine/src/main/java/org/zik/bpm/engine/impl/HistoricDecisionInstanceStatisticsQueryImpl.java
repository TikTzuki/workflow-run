// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.history.HistoricDecisionInstanceStatistics;
import org.zik.bpm.engine.history.HistoricDecisionInstanceStatisticsQuery;

public class HistoricDecisionInstanceStatisticsQueryImpl extends AbstractQuery<HistoricDecisionInstanceStatisticsQuery, HistoricDecisionInstanceStatistics> implements HistoricDecisionInstanceStatisticsQuery
{
    protected final String decisionRequirementsDefinitionId;
    protected String decisionInstanceId;
    
    public HistoricDecisionInstanceStatisticsQueryImpl(final String decisionRequirementsDefinitionId, final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.decisionRequirementsDefinitionId = decisionRequirementsDefinitionId;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        final long count = commandContext.getStatisticsManager().getStatisticsCountGroupedByDecisionRequirementsDefinition(this);
        return count;
    }
    
    @Override
    public List<HistoricDecisionInstanceStatistics> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        final List<HistoricDecisionInstanceStatistics> statisticsList = commandContext.getStatisticsManager().getStatisticsGroupedByDecisionRequirementsDefinition(this, page);
        return statisticsList;
    }
    
    @Override
    protected void checkQueryOk() {
        super.checkQueryOk();
        EnsureUtil.ensureNotNull("decisionRequirementsDefinitionId", (Object)this.decisionRequirementsDefinitionId);
    }
    
    public String getDecisionRequirementsDefinitionId() {
        return this.decisionRequirementsDefinitionId;
    }
    
    @Override
    public HistoricDecisionInstanceStatisticsQuery decisionInstanceId(final String decisionInstanceId) {
        this.decisionInstanceId = decisionInstanceId;
        return this;
    }
    
    public String getDecisionInstanceId() {
        return this.decisionInstanceId;
    }
    
    public void setDecisionInstanceId(final String decisionInstanceId) {
        this.decisionInstanceId = decisionInstanceId;
    }
}
