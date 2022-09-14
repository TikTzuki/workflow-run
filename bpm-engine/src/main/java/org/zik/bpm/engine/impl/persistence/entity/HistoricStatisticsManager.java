// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.history.HistoricCaseActivityStatistics;
import org.zik.bpm.engine.impl.HistoricCaseActivityStatisticsQueryImpl;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.history.HistoricActivityStatistics;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.HistoricActivityStatisticsQueryImpl;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class HistoricStatisticsManager extends AbstractManager
{
    public List<HistoricActivityStatistics> getHistoricStatisticsGroupedByActivity(final HistoricActivityStatisticsQueryImpl query, final Page page) {
        if (this.ensureHistoryReadOnProcessDefinition(query)) {
            return (List<HistoricActivityStatistics>)this.getDbEntityManager().selectList("selectHistoricActivityStatistics", query, page);
        }
        return new ArrayList<HistoricActivityStatistics>();
    }
    
    public long getHistoricStatisticsCountGroupedByActivity(final HistoricActivityStatisticsQueryImpl query) {
        if (this.ensureHistoryReadOnProcessDefinition(query)) {
            return (long)this.getDbEntityManager().selectOne("selectHistoricActivityStatisticsCount", query);
        }
        return 0L;
    }
    
    public List<HistoricCaseActivityStatistics> getHistoricStatisticsGroupedByCaseActivity(final HistoricCaseActivityStatisticsQueryImpl query, final Page page) {
        return (List<HistoricCaseActivityStatistics>)this.getDbEntityManager().selectList("selectHistoricCaseActivityStatistics", query, page);
    }
    
    public long getHistoricStatisticsCountGroupedByCaseActivity(final HistoricCaseActivityStatisticsQueryImpl query) {
        return (long)this.getDbEntityManager().selectOne("selectHistoricCaseActivityStatisticsCount", query);
    }
    
    protected boolean ensureHistoryReadOnProcessDefinition(final HistoricActivityStatisticsQueryImpl query) {
        final CommandContext commandContext = this.getCommandContext();
        if (this.isAuthorizationEnabled() && this.getCurrentAuthentication() != null && commandContext.isAuthorizationCheckEnabled()) {
            final String processDefinitionId = query.getProcessDefinitionId();
            final ProcessDefinitionEntity definition = this.getProcessDefinitionManager().findLatestProcessDefinitionById(processDefinitionId);
            return definition != null && this.getAuthorizationManager().isAuthorized(Permissions.READ_HISTORY, Resources.PROCESS_DEFINITION, definition.getKey());
        }
        return true;
    }
}
