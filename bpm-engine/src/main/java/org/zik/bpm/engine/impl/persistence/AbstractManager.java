// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence;

import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.zik.bpm.engine.impl.cfg.auth.ResourceAuthorizationProvider;
import org.zik.bpm.engine.impl.identity.Authentication;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Resource;
import org.zik.bpm.engine.impl.AbstractQuery;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.TenantManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricBatchManager;
import org.zik.bpm.engine.impl.persistence.entity.BatchManager;
import org.zik.bpm.engine.impl.persistence.entity.ReportManager;
import org.zik.bpm.engine.impl.persistence.entity.AttachmentManager;
import org.zik.bpm.engine.impl.persistence.entity.IdentityInfoManager;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionManager;
import org.zik.bpm.engine.impl.persistence.entity.UserOperationLogManager;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.JobManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricExternalTaskLogManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricJobLogManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricIdentityLinkLogManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricIncidentManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricTaskInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricCaseActivityInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricActivityInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricVariableInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricDetailManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricCaseInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricProcessInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.IdentityLinkManager;
import org.zik.bpm.engine.impl.persistence.entity.TaskReportManager;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionManager;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionManager;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInstanceManager;
import org.zik.bpm.engine.impl.form.entity.CamundaFormDefinitionManager;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionManager;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionManager;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayManager;
import org.zik.bpm.engine.impl.persistence.entity.ResourceManager;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentManager;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.db.sql.DbSqlSession;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.interceptor.Session;

public abstract class AbstractManager implements Session
{
    public void insert(final DbEntity dbEntity) {
        this.getDbEntityManager().insert(dbEntity);
    }
    
    public void delete(final DbEntity dbEntity) {
        this.getDbEntityManager().delete(dbEntity);
    }
    
    protected DbEntityManager getDbEntityManager() {
        return this.getSession(DbEntityManager.class);
    }
    
    protected DbSqlSession getDbSqlSession() {
        return this.getSession(DbSqlSession.class);
    }
    
    protected <T> T getSession(final Class<T> sessionClass) {
        return Context.getCommandContext().getSession(sessionClass);
    }
    
    protected DeploymentManager getDeploymentManager() {
        return this.getSession(DeploymentManager.class);
    }
    
    protected ResourceManager getResourceManager() {
        return this.getSession(ResourceManager.class);
    }
    
    protected ByteArrayManager getByteArrayManager() {
        return this.getSession(ByteArrayManager.class);
    }
    
    protected ProcessDefinitionManager getProcessDefinitionManager() {
        return this.getSession(ProcessDefinitionManager.class);
    }
    
    protected CaseDefinitionManager getCaseDefinitionManager() {
        return this.getSession(CaseDefinitionManager.class);
    }
    
    protected DecisionDefinitionManager getDecisionDefinitionManager() {
        return this.getSession(DecisionDefinitionManager.class);
    }
    
    protected DecisionRequirementsDefinitionManager getDecisionRequirementsDefinitionManager() {
        return this.getSession(DecisionRequirementsDefinitionManager.class);
    }
    
    protected CamundaFormDefinitionManager getCamundaFormDefinitionManager() {
        return this.getSession(CamundaFormDefinitionManager.class);
    }
    
    protected HistoricDecisionInstanceManager getHistoricDecisionInstanceManager() {
        return this.getSession(HistoricDecisionInstanceManager.class);
    }
    
    protected CaseExecutionManager getCaseInstanceManager() {
        return this.getSession(CaseExecutionManager.class);
    }
    
    protected CaseExecutionManager getCaseExecutionManager() {
        return this.getSession(CaseExecutionManager.class);
    }
    
    protected ExecutionManager getProcessInstanceManager() {
        return this.getSession(ExecutionManager.class);
    }
    
    protected TaskManager getTaskManager() {
        return this.getSession(TaskManager.class);
    }
    
    protected TaskReportManager getTaskReportManager() {
        return this.getSession(TaskReportManager.class);
    }
    
    protected IdentityLinkManager getIdentityLinkManager() {
        return this.getSession(IdentityLinkManager.class);
    }
    
    protected VariableInstanceManager getVariableInstanceManager() {
        return this.getSession(VariableInstanceManager.class);
    }
    
    protected HistoricProcessInstanceManager getHistoricProcessInstanceManager() {
        return this.getSession(HistoricProcessInstanceManager.class);
    }
    
    protected HistoricCaseInstanceManager getHistoricCaseInstanceManager() {
        return this.getSession(HistoricCaseInstanceManager.class);
    }
    
    protected HistoricDetailManager getHistoricDetailManager() {
        return this.getSession(HistoricDetailManager.class);
    }
    
    protected HistoricVariableInstanceManager getHistoricVariableInstanceManager() {
        return this.getSession(HistoricVariableInstanceManager.class);
    }
    
    protected HistoricActivityInstanceManager getHistoricActivityInstanceManager() {
        return this.getSession(HistoricActivityInstanceManager.class);
    }
    
    protected HistoricCaseActivityInstanceManager getHistoricCaseActivityInstanceManager() {
        return this.getSession(HistoricCaseActivityInstanceManager.class);
    }
    
    protected HistoricTaskInstanceManager getHistoricTaskInstanceManager() {
        return this.getSession(HistoricTaskInstanceManager.class);
    }
    
    protected HistoricIncidentManager getHistoricIncidentManager() {
        return this.getSession(HistoricIncidentManager.class);
    }
    
    protected HistoricIdentityLinkLogManager getHistoricIdentityLinkManager() {
        return this.getSession(HistoricIdentityLinkLogManager.class);
    }
    
    protected HistoricJobLogManager getHistoricJobLogManager() {
        return this.getSession(HistoricJobLogManager.class);
    }
    
    protected HistoricExternalTaskLogManager getHistoricExternalTaskLogManager() {
        return this.getSession(HistoricExternalTaskLogManager.class);
    }
    
    protected JobManager getJobManager() {
        return this.getSession(JobManager.class);
    }
    
    protected JobDefinitionManager getJobDefinitionManager() {
        return this.getSession(JobDefinitionManager.class);
    }
    
    protected UserOperationLogManager getUserOperationLogManager() {
        return this.getSession(UserOperationLogManager.class);
    }
    
    protected EventSubscriptionManager getEventSubscriptionManager() {
        return this.getSession(EventSubscriptionManager.class);
    }
    
    protected IdentityInfoManager getIdentityInfoManager() {
        return this.getSession(IdentityInfoManager.class);
    }
    
    protected AttachmentManager getAttachmentManager() {
        return this.getSession(AttachmentManager.class);
    }
    
    protected ReportManager getHistoricReportManager() {
        return this.getSession(ReportManager.class);
    }
    
    protected BatchManager getBatchManager() {
        return this.getSession(BatchManager.class);
    }
    
    protected HistoricBatchManager getHistoricBatchManager() {
        return this.getSession(HistoricBatchManager.class);
    }
    
    protected TenantManager getTenantManager() {
        return this.getSession(TenantManager.class);
    }
    
    @Override
    public void close() {
    }
    
    @Override
    public void flush() {
    }
    
    protected CommandContext getCommandContext() {
        return Context.getCommandContext();
    }
    
    protected AuthorizationManager getAuthorizationManager() {
        return this.getSession(AuthorizationManager.class);
    }
    
    protected void configureQuery(final AbstractQuery<?, ?> query, final Resource resource) {
        this.getAuthorizationManager().configureQuery(query, resource);
    }
    
    protected void checkAuthorization(final Permission permission, final Resource resource, final String resourceId) {
        this.getAuthorizationManager().checkAuthorization(permission, resource, resourceId);
    }
    
    public boolean isAuthorizationEnabled() {
        return Context.getProcessEngineConfiguration().isAuthorizationEnabled();
    }
    
    protected Authentication getCurrentAuthentication() {
        return Context.getCommandContext().getAuthentication();
    }
    
    protected ResourceAuthorizationProvider getResourceAuthorizationProvider() {
        return Context.getProcessEngineConfiguration().getResourceAuthorizationProvider();
    }
    
    protected void deleteAuthorizations(final Resource resource, final String resourceId) {
        this.getAuthorizationManager().deleteAuthorizationsByResourceId(resource, resourceId);
    }
    
    protected void deleteAuthorizationsForUser(final Resource resource, final String resourceId, final String userId) {
        this.getAuthorizationManager().deleteAuthorizationsByResourceIdAndUserId(resource, resourceId, userId);
    }
    
    protected void deleteAuthorizationsForGroup(final Resource resource, final String resourceId, final String groupId) {
        this.getAuthorizationManager().deleteAuthorizationsByResourceIdAndGroupId(resource, resourceId, groupId);
    }
    
    public void saveDefaultAuthorizations(final AuthorizationEntity[] authorizations) {
        if (authorizations != null && authorizations.length > 0) {
            Context.getCommandContext().runWithoutAuthorization((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() {
                    final AuthorizationManager authorizationManager = AbstractManager.this.getAuthorizationManager();
                    for (final AuthorizationEntity authorization : authorizations) {
                        if (authorization.getId() == null) {
                            authorizationManager.insert(authorization);
                        }
                        else {
                            authorizationManager.update(authorization);
                        }
                    }
                    return null;
                }
            });
        }
    }
    
    public void deleteDefaultAuthorizations(final AuthorizationEntity[] authorizations) {
        if (authorizations != null && authorizations.length > 0) {
            Context.getCommandContext().runWithoutAuthorization((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() {
                    final AuthorizationManager authorizationManager = AbstractManager.this.getAuthorizationManager();
                    for (final AuthorizationEntity authorization : authorizations) {
                        authorizationManager.delete(authorization);
                    }
                    return null;
                }
            });
        }
    }
}
