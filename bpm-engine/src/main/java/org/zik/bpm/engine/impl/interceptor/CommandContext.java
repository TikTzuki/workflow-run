// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.interceptor;

import org.zik.bpm.engine.impl.optimize.OptimizeManager;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.IdentityService;
import org.zik.bpm.engine.impl.identity.Authentication;
import org.zik.bpm.engine.impl.persistence.entity.ExternalTaskManager;
import org.zik.bpm.engine.impl.persistence.entity.FilterManager;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInstanceManager;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionManager;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionManager;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseSentryPartManager;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionManager;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionManager;
import org.zik.bpm.engine.impl.form.entity.CamundaFormDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.SchemaLogManager;
import org.zik.bpm.engine.impl.persistence.entity.TenantManager;
import org.zik.bpm.engine.impl.identity.WritableIdentityProvider;
import org.zik.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.zik.bpm.engine.impl.persistence.entity.AuthorizationManager;
import org.zik.bpm.engine.impl.persistence.entity.ReportManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricExternalTaskLogManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricJobLogManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricStatisticsManager;
import org.zik.bpm.engine.impl.persistence.entity.StatisticsManager;
import org.zik.bpm.engine.impl.persistence.entity.PropertyManager;
import org.zik.bpm.engine.impl.persistence.entity.EventSubscriptionManager;
import org.zik.bpm.engine.impl.persistence.entity.CommentManager;
import org.zik.bpm.engine.impl.persistence.entity.TableDataManager;
import org.zik.bpm.engine.impl.persistence.entity.AttachmentManager;
import org.zik.bpm.engine.impl.persistence.entity.IdentityInfoManager;
import org.zik.bpm.engine.impl.persistence.entity.IncidentManager;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricBatchManager;
import org.zik.bpm.engine.impl.persistence.entity.BatchManager;
import org.zik.bpm.engine.impl.persistence.entity.JobManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricIdentityLinkLogManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricIncidentManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricTaskInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricCaseActivityInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricActivityInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricVariableInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.UserOperationLogManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricDetailManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricCaseInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.HistoricProcessInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.VariableInstanceManager;
import org.zik.bpm.engine.impl.persistence.entity.IdentityLinkManager;
import org.zik.bpm.engine.impl.persistence.entity.MeterLogManager;
import org.zik.bpm.engine.impl.persistence.entity.TaskReportManager;
import org.zik.bpm.engine.impl.persistence.entity.TaskManager;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionManager;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionManager;
import org.zik.bpm.engine.impl.persistence.entity.ByteArrayManager;
import org.zik.bpm.engine.impl.persistence.entity.ResourceManager;
import org.zik.bpm.engine.impl.persistence.entity.DeploymentManager;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.util.Iterator;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.AuthorizationException;
import org.zik.bpm.engine.BadUserRequestException;
import org.zik.bpm.engine.OptimisticLockingException;
import org.zik.bpm.engine.TaskAlreadyClaimedException;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.db.sql.DbSqlSession;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.impl.cmmn.execution.CmmnExecution;
import org.zik.bpm.engine.impl.core.instance.CoreExecution;
import org.zik.bpm.engine.impl.core.operation.CoreAtomicOperation;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.application.InvocationContext;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionEntity;
import org.zik.bpm.engine.impl.cmmn.operation.CmmnAtomicOperation;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import org.zik.bpm.engine.impl.cfg.TransactionContextFactory;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.jobexecutor.FailedJobCommandFactory;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.List;
import java.util.Map;
import org.zik.bpm.engine.impl.cfg.TransactionContext;

public class CommandContext
{
    private static final ContextLogger LOG;
    protected boolean authorizationCheckEnabled;
    protected boolean userOperationLogEnabled;
    protected boolean tenantCheckEnabled;
    protected boolean restrictUserOperationLogToAuthenticatedUsers;
    protected TransactionContext transactionContext;
    protected Map<Class<?>, SessionFactory> sessionFactories;
    protected Map<Class<?>, Session> sessions;
    protected List<Session> sessionList;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;
    protected FailedJobCommandFactory failedJobCommandFactory;
    protected JobEntity currentJob;
    protected List<CommandContextListener> commandContextListeners;
    protected String operationId;
    
    public CommandContext(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        this(processEngineConfiguration, processEngineConfiguration.getTransactionContextFactory());
    }
    
    public CommandContext(final ProcessEngineConfigurationImpl processEngineConfiguration, final TransactionContextFactory transactionContextFactory) {
        this.authorizationCheckEnabled = true;
        this.userOperationLogEnabled = true;
        this.tenantCheckEnabled = true;
        this.sessions = new HashMap<Class<?>, Session>();
        this.sessionList = new ArrayList<Session>();
        this.currentJob = null;
        this.commandContextListeners = new LinkedList<CommandContextListener>();
        this.processEngineConfiguration = processEngineConfiguration;
        this.failedJobCommandFactory = processEngineConfiguration.getFailedJobCommandFactory();
        this.sessionFactories = processEngineConfiguration.getSessionFactories();
        this.transactionContext = transactionContextFactory.openTransactionContext(this);
        this.restrictUserOperationLogToAuthenticatedUsers = processEngineConfiguration.isRestrictUserOperationLogToAuthenticatedUsers();
    }
    
    public void performOperation(final CmmnAtomicOperation executionOperation, final CaseExecutionEntity execution) {
        final ProcessApplicationReference targetProcessApplication = this.getTargetProcessApplication(execution);
        if (this.requiresContextSwitch(targetProcessApplication)) {
            Context.executeWithinProcessApplication(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    CommandContext.this.performOperation(executionOperation, execution);
                    return null;
                }
            }, targetProcessApplication, new InvocationContext(execution));
        }
        else {
            try {
                Context.setExecutionContext(execution);
                CommandContext.LOG.debugExecutingAtomicOperation(executionOperation, execution);
                executionOperation.execute((CmmnExecution)execution);
            }
            finally {
                Context.removeExecutionContext();
            }
        }
    }
    
    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return this.processEngineConfiguration;
    }
    
    protected ProcessApplicationReference getTargetProcessApplication(final CaseExecutionEntity execution) {
        return ProcessApplicationContextUtil.getTargetProcessApplication(execution);
    }
    
    protected boolean requiresContextSwitch(final ProcessApplicationReference processApplicationReference) {
        return ProcessApplicationContextUtil.requiresContextSwitch(processApplicationReference);
    }
    
    public void close(final CommandInvocationContext commandInvocationContext) {
        try {
            try {
                try {
                    if (commandInvocationContext.getThrowable() == null) {
                        this.fireCommandContextClose();
                        this.flushSessions();
                    }
                }
                catch (Throwable exception) {
                    commandInvocationContext.trySetThrowable(exception);
                }
                finally {
                    try {
                        if (commandInvocationContext.getThrowable() == null) {
                            this.transactionContext.commit();
                        }
                    }
                    catch (Throwable exception2) {
                        if (DbSqlSession.isCrdbConcurrencyConflict(exception2)) {
                            exception2 = ProcessEngineLogger.PERSISTENCE_LOGGER.crdbTransactionRetryExceptionOnCommit(exception2);
                        }
                        commandInvocationContext.trySetThrowable(exception2);
                    }
                    if (commandInvocationContext.getThrowable() != null) {
                        this.fireCommandFailed(commandInvocationContext.getThrowable());
                        if (this.shouldLogCmdException()) {
                            if (this.shouldLogInfo(commandInvocationContext.getThrowable())) {
                                CommandContext.LOG.infoException(commandInvocationContext.getThrowable());
                            }
                            else if (this.shouldLogFine(commandInvocationContext.getThrowable())) {
                                CommandContext.LOG.debugException(commandInvocationContext.getThrowable());
                            }
                            else {
                                CommandContext.LOG.errorException(commandInvocationContext.getThrowable());
                            }
                        }
                        this.transactionContext.rollback();
                    }
                }
            }
            catch (Throwable exception) {
                commandInvocationContext.trySetThrowable(exception);
            }
            finally {
                this.closeSessions(commandInvocationContext);
            }
        }
        catch (Throwable exception) {
            commandInvocationContext.trySetThrowable(exception);
        }
        commandInvocationContext.rethrow();
    }
    
    protected boolean shouldLogInfo(final Throwable exception) {
        return exception instanceof TaskAlreadyClaimedException;
    }
    
    protected boolean shouldLogFine(final Throwable exception) {
        return exception instanceof OptimisticLockingException || exception instanceof BadUserRequestException || exception instanceof AuthorizationException;
    }
    
    protected boolean shouldLogCmdException() {
        return ProcessEngineLogger.shouldLogCmdException(this.processEngineConfiguration);
    }
    
    protected void fireCommandContextClose() {
        for (final CommandContextListener listener : this.commandContextListeners) {
            listener.onCommandContextClose(this);
        }
    }
    
    protected void fireCommandFailed(final Throwable t) {
        for (final CommandContextListener listener : this.commandContextListeners) {
            try {
                listener.onCommandFailed(this, t);
            }
            catch (Throwable ex) {
                CommandContext.LOG.exceptionWhileInvokingOnCommandFailed(t);
            }
        }
    }
    
    protected void flushSessions() {
        for (int i = 0; i < this.sessionList.size(); ++i) {
            this.sessionList.get(i).flush();
        }
    }
    
    protected void closeSessions(final CommandInvocationContext commandInvocationContext) {
        for (final Session session : this.sessionList) {
            try {
                session.close();
            }
            catch (Throwable exception) {
                commandInvocationContext.trySetThrowable(exception);
            }
        }
    }
    
    public <T> T getSession(final Class<T> sessionClass) {
        Session session = this.sessions.get(sessionClass);
        if (session == null) {
            final SessionFactory sessionFactory = this.sessionFactories.get(sessionClass);
            EnsureUtil.ensureNotNull("no session factory configured for " + sessionClass.getName(), "sessionFactory", sessionFactory);
            session = sessionFactory.openSession();
            this.sessions.put(sessionClass, session);
            this.sessionList.add(0, session);
        }
        return (T)session;
    }
    
    public DbEntityManager getDbEntityManager() {
        return this.getSession(DbEntityManager.class);
    }
    
    public DbSqlSession getDbSqlSession() {
        return this.getSession(DbSqlSession.class);
    }
    
    public DeploymentManager getDeploymentManager() {
        return this.getSession(DeploymentManager.class);
    }
    
    public ResourceManager getResourceManager() {
        return this.getSession(ResourceManager.class);
    }
    
    public ByteArrayManager getByteArrayManager() {
        return this.getSession(ByteArrayManager.class);
    }
    
    public ProcessDefinitionManager getProcessDefinitionManager() {
        return this.getSession(ProcessDefinitionManager.class);
    }
    
    public ExecutionManager getExecutionManager() {
        return this.getSession(ExecutionManager.class);
    }
    
    public TaskManager getTaskManager() {
        return this.getSession(TaskManager.class);
    }
    
    public TaskReportManager getTaskReportManager() {
        return this.getSession(TaskReportManager.class);
    }
    
    public MeterLogManager getMeterLogManager() {
        return this.getSession(MeterLogManager.class);
    }
    
    public IdentityLinkManager getIdentityLinkManager() {
        return this.getSession(IdentityLinkManager.class);
    }
    
    public VariableInstanceManager getVariableInstanceManager() {
        return this.getSession(VariableInstanceManager.class);
    }
    
    public HistoricProcessInstanceManager getHistoricProcessInstanceManager() {
        return this.getSession(HistoricProcessInstanceManager.class);
    }
    
    public HistoricCaseInstanceManager getHistoricCaseInstanceManager() {
        return this.getSession(HistoricCaseInstanceManager.class);
    }
    
    public HistoricDetailManager getHistoricDetailManager() {
        return this.getSession(HistoricDetailManager.class);
    }
    
    public UserOperationLogManager getOperationLogManager() {
        return this.getSession(UserOperationLogManager.class);
    }
    
    public HistoricVariableInstanceManager getHistoricVariableInstanceManager() {
        return this.getSession(HistoricVariableInstanceManager.class);
    }
    
    public HistoricActivityInstanceManager getHistoricActivityInstanceManager() {
        return this.getSession(HistoricActivityInstanceManager.class);
    }
    
    public HistoricCaseActivityInstanceManager getHistoricCaseActivityInstanceManager() {
        return this.getSession(HistoricCaseActivityInstanceManager.class);
    }
    
    public HistoricTaskInstanceManager getHistoricTaskInstanceManager() {
        return this.getSession(HistoricTaskInstanceManager.class);
    }
    
    public HistoricIncidentManager getHistoricIncidentManager() {
        return this.getSession(HistoricIncidentManager.class);
    }
    
    public HistoricIdentityLinkLogManager getHistoricIdentityLinkManager() {
        return this.getSession(HistoricIdentityLinkLogManager.class);
    }
    
    public JobManager getJobManager() {
        return this.getSession(JobManager.class);
    }
    
    public BatchManager getBatchManager() {
        return this.getSession(BatchManager.class);
    }
    
    public HistoricBatchManager getHistoricBatchManager() {
        return this.getSession(HistoricBatchManager.class);
    }
    
    public JobDefinitionManager getJobDefinitionManager() {
        return this.getSession(JobDefinitionManager.class);
    }
    
    public IncidentManager getIncidentManager() {
        return this.getSession(IncidentManager.class);
    }
    
    public IdentityInfoManager getIdentityInfoManager() {
        return this.getSession(IdentityInfoManager.class);
    }
    
    public AttachmentManager getAttachmentManager() {
        return this.getSession(AttachmentManager.class);
    }
    
    public TableDataManager getTableDataManager() {
        return this.getSession(TableDataManager.class);
    }
    
    public CommentManager getCommentManager() {
        return this.getSession(CommentManager.class);
    }
    
    public EventSubscriptionManager getEventSubscriptionManager() {
        return this.getSession(EventSubscriptionManager.class);
    }
    
    public Map<Class<?>, SessionFactory> getSessionFactories() {
        return this.sessionFactories;
    }
    
    public PropertyManager getPropertyManager() {
        return this.getSession(PropertyManager.class);
    }
    
    public StatisticsManager getStatisticsManager() {
        return this.getSession(StatisticsManager.class);
    }
    
    public HistoricStatisticsManager getHistoricStatisticsManager() {
        return this.getSession(HistoricStatisticsManager.class);
    }
    
    public HistoricJobLogManager getHistoricJobLogManager() {
        return this.getSession(HistoricJobLogManager.class);
    }
    
    public HistoricExternalTaskLogManager getHistoricExternalTaskLogManager() {
        return this.getSession(HistoricExternalTaskLogManager.class);
    }
    
    public ReportManager getHistoricReportManager() {
        return this.getSession(ReportManager.class);
    }
    
    public AuthorizationManager getAuthorizationManager() {
        return this.getSession(AuthorizationManager.class);
    }
    
    public ReadOnlyIdentityProvider getReadOnlyIdentityProvider() {
        return this.getSession(ReadOnlyIdentityProvider.class);
    }
    
    public WritableIdentityProvider getWritableIdentityProvider() {
        return this.getSession(WritableIdentityProvider.class);
    }
    
    public TenantManager getTenantManager() {
        return this.getSession(TenantManager.class);
    }
    
    public SchemaLogManager getSchemaLogManager() {
        return this.getSession(SchemaLogManager.class);
    }
    
    public CamundaFormDefinitionManager getCamundaFormDefinitionManager() {
        return this.getSession(CamundaFormDefinitionManager.class);
    }
    
    public CaseDefinitionManager getCaseDefinitionManager() {
        return this.getSession(CaseDefinitionManager.class);
    }
    
    public CaseExecutionManager getCaseExecutionManager() {
        return this.getSession(CaseExecutionManager.class);
    }
    
    public CaseSentryPartManager getCaseSentryPartManager() {
        return this.getSession(CaseSentryPartManager.class);
    }
    
    public DecisionDefinitionManager getDecisionDefinitionManager() {
        return this.getSession(DecisionDefinitionManager.class);
    }
    
    public DecisionRequirementsDefinitionManager getDecisionRequirementsDefinitionManager() {
        return this.getSession(DecisionRequirementsDefinitionManager.class);
    }
    
    public HistoricDecisionInstanceManager getHistoricDecisionInstanceManager() {
        return this.getSession(HistoricDecisionInstanceManager.class);
    }
    
    public FilterManager getFilterManager() {
        return this.getSession(FilterManager.class);
    }
    
    public ExternalTaskManager getExternalTaskManager() {
        return this.getSession(ExternalTaskManager.class);
    }
    
    public void registerCommandContextListener(final CommandContextListener commandContextListener) {
        if (!this.commandContextListeners.contains(commandContextListener)) {
            this.commandContextListeners.add(commandContextListener);
        }
    }
    
    public TransactionContext getTransactionContext() {
        return this.transactionContext;
    }
    
    public Map<Class<?>, Session> getSessions() {
        return this.sessions;
    }
    
    public FailedJobCommandFactory getFailedJobCommandFactory() {
        return this.failedJobCommandFactory;
    }
    
    public Authentication getAuthentication() {
        final IdentityService identityService = this.processEngineConfiguration.getIdentityService();
        return identityService.getCurrentAuthentication();
    }
    
    public <T> T runWithoutAuthorization(final Callable<T> runnable) {
        final CommandContext commandContext = Context.getCommandContext();
        return this.runWithoutAuthorization(runnable, commandContext);
    }
    
    public <T> T runWithoutAuthorization(final Command<T> command) {
        final CommandContext commandContext = Context.getCommandContext();
        return this.runWithoutAuthorization(() -> command.execute(commandContext), commandContext);
    }
    
    protected <T> T runWithoutAuthorization(final Callable<T> runnable, final CommandContext commandContext) {
        final boolean authorizationEnabled = commandContext.isAuthorizationCheckEnabled();
        try {
            commandContext.disableAuthorizationCheck();
            return runnable.call();
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ProcessEngineException(e2);
        }
        finally {
            if (authorizationEnabled) {
                commandContext.enableAuthorizationCheck();
            }
        }
    }
    
    public String getAuthenticatedUserId() {
        final IdentityService identityService = this.processEngineConfiguration.getIdentityService();
        final Authentication currentAuthentication = identityService.getCurrentAuthentication();
        if (currentAuthentication == null) {
            return null;
        }
        return currentAuthentication.getUserId();
    }
    
    public List<String> getAuthenticatedGroupIds() {
        final IdentityService identityService = this.processEngineConfiguration.getIdentityService();
        final Authentication currentAuthentication = identityService.getCurrentAuthentication();
        if (currentAuthentication == null) {
            return null;
        }
        return currentAuthentication.getGroupIds();
    }
    
    public void enableAuthorizationCheck() {
        this.authorizationCheckEnabled = true;
    }
    
    public void disableAuthorizationCheck() {
        this.authorizationCheckEnabled = false;
    }
    
    public boolean isAuthorizationCheckEnabled() {
        return this.authorizationCheckEnabled;
    }
    
    public void setAuthorizationCheckEnabled(final boolean authorizationCheckEnabled) {
        this.authorizationCheckEnabled = authorizationCheckEnabled;
    }
    
    public void enableUserOperationLog() {
        this.userOperationLogEnabled = true;
    }
    
    public void disableUserOperationLog() {
        this.userOperationLogEnabled = false;
    }
    
    public boolean isUserOperationLogEnabled() {
        return this.userOperationLogEnabled;
    }
    
    public void setLogUserOperationEnabled(final boolean userOperationLogEnabled) {
        this.userOperationLogEnabled = userOperationLogEnabled;
    }
    
    public void enableTenantCheck() {
        this.tenantCheckEnabled = true;
    }
    
    public void disableTenantCheck() {
        this.tenantCheckEnabled = false;
    }
    
    public void setTenantCheckEnabled(final boolean tenantCheckEnabled) {
        this.tenantCheckEnabled = tenantCheckEnabled;
    }
    
    public boolean isTenantCheckEnabled() {
        return this.tenantCheckEnabled;
    }
    
    public JobEntity getCurrentJob() {
        return this.currentJob;
    }
    
    public void setCurrentJob(final JobEntity currentJob) {
        this.currentJob = currentJob;
    }
    
    public boolean isRestrictUserOperationLogToAuthenticatedUsers() {
        return this.restrictUserOperationLogToAuthenticatedUsers;
    }
    
    public void setRestrictUserOperationLogToAuthenticatedUsers(final boolean restrictUserOperationLogToAuthenticatedUsers) {
        this.restrictUserOperationLogToAuthenticatedUsers = restrictUserOperationLogToAuthenticatedUsers;
    }
    
    public String getOperationId() {
        if (!this.getOperationLogManager().isUserOperationLogEnabled()) {
            return null;
        }
        if (this.operationId == null) {
            this.operationId = Context.getProcessEngineConfiguration().getIdGenerator().getNextId();
        }
        return this.operationId;
    }
    
    public void setOperationId(final String operationId) {
        this.operationId = operationId;
    }
    
    public OptimizeManager getOptimizeManager() {
        return this.getSession(OptimizeManager.class);
    }
    
    public <T> void executeWithOperationLogPrevented(final Command<T> command) {
        final boolean initialLegacyRestrictions = this.isRestrictUserOperationLogToAuthenticatedUsers();
        this.disableUserOperationLog();
        this.setRestrictUserOperationLogToAuthenticatedUsers(true);
        try {
            command.execute(this);
        }
        finally {
            this.enableUserOperationLog();
            this.setRestrictUserOperationLogToAuthenticatedUsers(initialLegacyRestrictions);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.CONTEXT_LOGGER;
    }
}
