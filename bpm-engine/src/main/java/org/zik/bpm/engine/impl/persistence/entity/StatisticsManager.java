// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.history.HistoricDecisionInstanceStatistics;
import org.zik.bpm.engine.repository.DecisionRequirementsDefinition;
import org.zik.bpm.engine.impl.HistoricDecisionInstanceStatisticsQueryImpl;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resources;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.batch.BatchStatistics;
import org.zik.bpm.engine.impl.batch.BatchStatisticsQueryImpl;
import org.zik.bpm.engine.management.DeploymentStatistics;
import org.zik.bpm.engine.impl.DeploymentStatisticsQueryImpl;
import org.zik.bpm.engine.management.ActivityStatistics;
import org.zik.bpm.engine.impl.ActivityStatisticsQueryImpl;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.management.ProcessDefinitionStatistics;
import java.util.List;
import org.zik.bpm.engine.impl.Page;
import org.zik.bpm.engine.impl.ProcessDefinitionStatisticsQueryImpl;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class StatisticsManager extends AbstractManager
{
    public List<ProcessDefinitionStatistics> getStatisticsGroupedByProcessDefinitionVersion(final ProcessDefinitionStatisticsQueryImpl query, final Page page) {
        this.configureQuery(query);
        return (List<ProcessDefinitionStatistics>)this.getDbEntityManager().selectList("selectProcessDefinitionStatistics", query, page);
    }
    
    public long getStatisticsCountGroupedByProcessDefinitionVersion(final ProcessDefinitionStatisticsQueryImpl query) {
        this.configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectProcessDefinitionStatisticsCount", query);
    }
    
    public List<ActivityStatistics> getStatisticsGroupedByActivity(final ActivityStatisticsQueryImpl query, final Page page) {
        this.configureQuery(query);
        return (List<ActivityStatistics>)this.getDbEntityManager().selectList("selectActivityStatistics", query, page);
    }
    
    public long getStatisticsCountGroupedByActivity(final ActivityStatisticsQueryImpl query) {
        this.configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectActivityStatisticsCount", query);
    }
    
    public List<DeploymentStatistics> getStatisticsGroupedByDeployment(final DeploymentStatisticsQueryImpl query, final Page page) {
        this.configureQuery(query);
        return (List<DeploymentStatistics>)this.getDbEntityManager().selectList("selectDeploymentStatistics", query, page);
    }
    
    public long getStatisticsCountGroupedByDeployment(final DeploymentStatisticsQueryImpl query) {
        this.configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectDeploymentStatisticsCount", query);
    }
    
    public List<BatchStatistics> getStatisticsGroupedByBatch(final BatchStatisticsQueryImpl query, final Page page) {
        this.configureQuery(query);
        return (List<BatchStatistics>)this.getDbEntityManager().selectList("selectBatchStatistics", query, page);
    }
    
    public long getStatisticsCountGroupedByBatch(final BatchStatisticsQueryImpl query) {
        this.configureQuery(query);
        return (long)this.getDbEntityManager().selectOne("selectBatchStatisticsCount", query);
    }
    
    protected void configureQuery(final DeploymentStatisticsQueryImpl query) {
        this.getAuthorizationManager().configureDeploymentStatisticsQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected void configureQuery(final ProcessDefinitionStatisticsQueryImpl query) {
        this.getAuthorizationManager().configureProcessDefinitionStatisticsQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected void configureQuery(final ActivityStatisticsQueryImpl query) {
        this.checkReadProcessDefinition(query);
        this.getAuthorizationManager().configureActivityStatisticsQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected void configureQuery(final BatchStatisticsQueryImpl batchQuery) {
        this.getAuthorizationManager().configureBatchStatisticsQuery(batchQuery);
        this.getTenantManager().configureQuery(batchQuery);
    }
    
    protected void checkReadProcessDefinition(final ActivityStatisticsQueryImpl query) {
        final CommandContext commandContext = this.getCommandContext();
        if (this.isAuthorizationEnabled() && this.getCurrentAuthentication() != null && commandContext.isAuthorizationCheckEnabled()) {
            final String processDefinitionId = query.getProcessDefinitionId();
            final ProcessDefinitionEntity definition = this.getProcessDefinitionManager().findLatestProcessDefinitionById(processDefinitionId);
            EnsureUtil.ensureNotNull("no deployed process definition found with id '" + processDefinitionId + "'", "processDefinition", definition);
            this.getAuthorizationManager().checkAuthorization(Permissions.READ, Resources.PROCESS_DEFINITION, definition.getKey());
        }
    }
    
    public long getStatisticsCountGroupedByDecisionRequirementsDefinition(final HistoricDecisionInstanceStatisticsQueryImpl decisionRequirementsDefinitionStatisticsQuery) {
        this.configureQuery(decisionRequirementsDefinitionStatisticsQuery);
        return (long)this.getDbEntityManager().selectOne("selectDecisionDefinitionStatisticsCount", decisionRequirementsDefinitionStatisticsQuery);
    }
    
    protected void configureQuery(final HistoricDecisionInstanceStatisticsQueryImpl decisionRequirementsDefinitionStatisticsQuery) {
        this.checkReadDecisionRequirementsDefinition(decisionRequirementsDefinitionStatisticsQuery);
        this.getTenantManager().configureQuery(decisionRequirementsDefinitionStatisticsQuery);
    }
    
    protected void checkReadDecisionRequirementsDefinition(final HistoricDecisionInstanceStatisticsQueryImpl query) {
        final CommandContext commandContext = this.getCommandContext();
        if (this.isAuthorizationEnabled() && this.getCurrentAuthentication() != null && commandContext.isAuthorizationCheckEnabled()) {
            final String decisionRequirementsDefinitionId = query.getDecisionRequirementsDefinitionId();
            final DecisionRequirementsDefinition definition = this.getDecisionRequirementsDefinitionManager().findDecisionRequirementsDefinitionById(decisionRequirementsDefinitionId);
            EnsureUtil.ensureNotNull("no deployed decision requirements definition found with id '" + decisionRequirementsDefinitionId + "'", "decisionRequirementsDefinition", definition);
            this.getAuthorizationManager().checkAuthorization(Permissions.READ, Resources.DECISION_REQUIREMENTS_DEFINITION, definition.getKey());
        }
    }
    
    public List<HistoricDecisionInstanceStatistics> getStatisticsGroupedByDecisionRequirementsDefinition(final HistoricDecisionInstanceStatisticsQueryImpl query, final Page page) {
        this.configureQuery(query);
        return (List<HistoricDecisionInstanceStatistics>)this.getDbEntityManager().selectList("selectDecisionDefinitionStatistics", query, page);
    }
}
