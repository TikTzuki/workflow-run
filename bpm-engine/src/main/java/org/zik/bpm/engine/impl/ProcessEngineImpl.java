// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.impl.telemetry.reporter.TelemetryReporter;
import org.zik.bpm.engine.OptimisticLockingException;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.metrics.reporter.DbMetricsReporter;
import org.zik.bpm.engine.impl.history.event.SimpleIpBasedProvider;
import org.zik.bpm.engine.ProcessEngines;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.cfg.TransactionContextFactory;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.interceptor.SessionFactory;
import java.util.Map;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutor;
import org.zik.bpm.engine.DecisionService;
import org.zik.bpm.engine.ExternalTaskService;
import org.zik.bpm.engine.FilterService;
import org.zik.bpm.engine.CaseService;
import org.zik.bpm.engine.AuthorizationService;
import org.zik.bpm.engine.ManagementService;
import org.zik.bpm.engine.FormService;
import org.zik.bpm.engine.TaskService;
import org.zik.bpm.engine.IdentityService;
import org.zik.bpm.engine.HistoryService;
import org.zik.bpm.engine.RuntimeService;
import org.zik.bpm.engine.RepositoryService;
import org.zik.bpm.engine.impl.util.CompositeCondition;
import org.zik.bpm.engine.ProcessEngine;

public class ProcessEngineImpl implements ProcessEngine
{
    public static final CompositeCondition EXT_TASK_CONDITIONS;
    private static final ProcessEngineLogger LOG;
    protected String name;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected HistoryService historicDataService;
    protected IdentityService identityService;
    protected TaskService taskService;
    protected FormService formService;
    protected ManagementService managementService;
    protected AuthorizationService authorizationService;
    protected CaseService caseService;
    protected FilterService filterService;
    protected ExternalTaskService externalTaskService;
    protected DecisionService decisionService;
    protected String databaseSchemaUpdate;
    protected JobExecutor jobExecutor;
    protected CommandExecutor commandExecutor;
    protected CommandExecutor commandExecutorSchemaOperations;
    protected Map<Class<?>, SessionFactory> sessionFactories;
    protected ExpressionManager expressionManager;
    protected HistoryLevel historyLevel;
    protected TransactionContextFactory transactionContextFactory;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;
    
    public ProcessEngineImpl(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
        this.name = processEngineConfiguration.getProcessEngineName();
        this.repositoryService = processEngineConfiguration.getRepositoryService();
        this.runtimeService = processEngineConfiguration.getRuntimeService();
        this.historicDataService = processEngineConfiguration.getHistoryService();
        this.identityService = processEngineConfiguration.getIdentityService();
        this.taskService = processEngineConfiguration.getTaskService();
        this.formService = processEngineConfiguration.getFormService();
        this.managementService = processEngineConfiguration.getManagementService();
        this.authorizationService = processEngineConfiguration.getAuthorizationService();
        this.caseService = processEngineConfiguration.getCaseService();
        this.filterService = processEngineConfiguration.getFilterService();
        this.externalTaskService = processEngineConfiguration.getExternalTaskService();
        this.decisionService = processEngineConfiguration.getDecisionService();
        this.databaseSchemaUpdate = processEngineConfiguration.getDatabaseSchemaUpdate();
        this.jobExecutor = processEngineConfiguration.getJobExecutor();
        this.commandExecutor = processEngineConfiguration.getCommandExecutorTxRequired();
        this.commandExecutorSchemaOperations = processEngineConfiguration.getCommandExecutorSchemaOperations();
        this.sessionFactories = processEngineConfiguration.getSessionFactories();
        this.historyLevel = processEngineConfiguration.getHistoryLevel();
        this.transactionContextFactory = processEngineConfiguration.getTransactionContextFactory();
        this.executeSchemaOperations();
        if (this.name == null) {
            ProcessEngineImpl.LOG.processEngineCreated("default");
        }
        else {
            ProcessEngineImpl.LOG.processEngineCreated(this.name);
        }
        ProcessEngines.registerProcessEngine(this);
        if (this.jobExecutor != null) {
            this.jobExecutor.registerProcessEngine(this);
        }
        if (processEngineConfiguration.isMetricsEnabled()) {
            String reporterId;
            if (processEngineConfiguration.getMetricsReporterIdProvider() != null && processEngineConfiguration.getHostnameProvider() instanceof SimpleIpBasedProvider) {
                reporterId = processEngineConfiguration.getMetricsReporterIdProvider().provideId(this);
            }
            else {
                reporterId = processEngineConfiguration.getHostname();
            }
            final DbMetricsReporter dbMetricsReporter = processEngineConfiguration.getDbMetricsReporter();
            dbMetricsReporter.setReporterId(reporterId);
            if (processEngineConfiguration.isDbMetricsReporterActivate()) {
                dbMetricsReporter.start();
            }
        }
    }
    
    protected void executeSchemaOperations() {
        this.commandExecutorSchemaOperations.execute(this.processEngineConfiguration.getSchemaOperationsCommand());
        this.commandExecutorSchemaOperations.execute(this.processEngineConfiguration.getHistoryLevelCommand());
        try {
            this.commandExecutorSchemaOperations.execute(this.processEngineConfiguration.getProcessEngineBootstrapCommand());
        }
        catch (OptimisticLockingException ole) {
            ProcessEngineImpl.LOG.historyCleanupJobReconfigurationFailure(ole);
            final String databaseType = this.getProcessEngineConfiguration().getDatabaseType();
            if ("cockroachdb".equals(databaseType)) {
                throw ole;
            }
        }
    }
    
    @Override
    public void close() {
        ProcessEngines.unregister(this);
        if (this.processEngineConfiguration.isMetricsEnabled()) {
            this.processEngineConfiguration.getDbMetricsReporter().stop();
        }
        final TelemetryReporter telemetryReporter = this.processEngineConfiguration.getTelemetryReporter();
        if (telemetryReporter != null) {
            telemetryReporter.stop();
        }
        if (this.jobExecutor != null) {
            this.jobExecutor.unregisterProcessEngine(this);
        }
        this.commandExecutorSchemaOperations.execute((Command<Object>)new SchemaOperationProcessEngineClose());
        this.processEngineConfiguration.close();
        ProcessEngineImpl.LOG.processEngineClosed(this.name);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return this.processEngineConfiguration;
    }
    
    @Override
    public IdentityService getIdentityService() {
        return this.identityService;
    }
    
    @Override
    public ManagementService getManagementService() {
        return this.managementService;
    }
    
    @Override
    public TaskService getTaskService() {
        return this.taskService;
    }
    
    @Override
    public HistoryService getHistoryService() {
        return this.historicDataService;
    }
    
    @Override
    public RuntimeService getRuntimeService() {
        return this.runtimeService;
    }
    
    @Override
    public RepositoryService getRepositoryService() {
        return this.repositoryService;
    }
    
    @Override
    public FormService getFormService() {
        return this.formService;
    }
    
    @Override
    public AuthorizationService getAuthorizationService() {
        return this.authorizationService;
    }
    
    @Override
    public CaseService getCaseService() {
        return this.caseService;
    }
    
    @Override
    public FilterService getFilterService() {
        return this.filterService;
    }
    
    @Override
    public ExternalTaskService getExternalTaskService() {
        return this.externalTaskService;
    }
    
    @Override
    public DecisionService getDecisionService() {
        return this.decisionService;
    }
    
    static {
        EXT_TASK_CONDITIONS = new CompositeCondition();
        LOG = ProcessEngineLogger.INSTANCE;
    }
}
