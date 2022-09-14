// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.test;

import java.util.Date;
import java.util.Iterator;
import org.zik.bpm.engine.impl.telemetry.PlatformTelemetryRegistry;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.io.FileNotFoundException;
import org.junit.Assume;
import org.zik.bpm.engine.impl.test.RequiredDatabase;
import org.junit.runners.model.Statement;
import org.zik.bpm.engine.impl.test.TestHelper;
import org.junit.runner.Description;
import java.util.ArrayList;
import org.zik.bpm.engine.DecisionService;
import org.zik.bpm.engine.ExternalTaskService;
import org.zik.bpm.engine.CaseService;
import org.zik.bpm.engine.AuthorizationService;
import org.zik.bpm.engine.FilterService;
import org.zik.bpm.engine.FormService;
import org.zik.bpm.engine.ManagementService;
import org.zik.bpm.engine.IdentityService;
import org.zik.bpm.engine.HistoryService;
import org.zik.bpm.engine.TaskService;
import org.zik.bpm.engine.RuntimeService;
import org.zik.bpm.engine.RepositoryService;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.ProcessEngine;
import java.util.List;
import org.zik.bpm.engine.ProcessEngineServices;
import org.junit.rules.TestWatcher;

public class ProcessEngineRule extends TestWatcher implements ProcessEngineServices
{
    protected String configurationResource;
    protected String configurationResourceCompat;
    protected String deploymentId;
    protected List<String> additionalDeployments;
    protected boolean ensureCleanAfterTest;
    protected ProcessEngine processEngine;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;
    protected FormService formService;
    protected FilterService filterService;
    protected AuthorizationService authorizationService;
    protected CaseService caseService;
    protected ExternalTaskService externalTaskService;
    protected DecisionService decisionService;
    
    public ProcessEngineRule() {
        this(false);
    }
    
    public ProcessEngineRule(final boolean ensureCleanAfterTest) {
        this.configurationResource = "camunda.cfg.xml";
        this.configurationResourceCompat = "activiti.cfg.xml";
        this.deploymentId = null;
        this.additionalDeployments = new ArrayList<String>();
        this.ensureCleanAfterTest = false;
        this.ensureCleanAfterTest = ensureCleanAfterTest;
    }
    
    public ProcessEngineRule(final String configurationResource) {
        this(configurationResource, false);
    }
    
    public ProcessEngineRule(final String configurationResource, final boolean ensureCleanAfterTest) {
        this.configurationResource = "camunda.cfg.xml";
        this.configurationResourceCompat = "activiti.cfg.xml";
        this.deploymentId = null;
        this.additionalDeployments = new ArrayList<String>();
        this.ensureCleanAfterTest = false;
        this.configurationResource = configurationResource;
        this.ensureCleanAfterTest = ensureCleanAfterTest;
    }
    
    public ProcessEngineRule(final ProcessEngine processEngine) {
        this(processEngine, false);
    }
    
    public ProcessEngineRule(final ProcessEngine processEngine, final boolean ensureCleanAfterTest) {
        this.configurationResource = "camunda.cfg.xml";
        this.configurationResourceCompat = "activiti.cfg.xml";
        this.deploymentId = null;
        this.additionalDeployments = new ArrayList<String>();
        this.ensureCleanAfterTest = false;
        this.processEngine = processEngine;
        this.ensureCleanAfterTest = ensureCleanAfterTest;
    }
    
    public void starting(final Description description) {
        this.deploymentId = TestHelper.annotationDeploymentSetUp(this.processEngine, description.getTestClass(), description.getMethodName(), (Deployment)description.getAnnotation((Class)Deployment.class), (Class<?>[])new Class[0]);
    }
    
    public Statement apply(final Statement base, final Description description) {
        if (this.processEngine == null) {
            this.initializeProcessEngine();
        }
        this.initializeServices();
        final Class<?> testClass = (Class<?>)description.getTestClass();
        final String methodName = description.getMethodName();
        final RequiredHistoryLevel reqHistoryLevel = (RequiredHistoryLevel)description.getAnnotation((Class)RequiredHistoryLevel.class);
        final boolean hasRequiredHistoryLevel = TestHelper.annotationRequiredHistoryLevelCheck(this.processEngine, reqHistoryLevel, testClass, methodName);
        final RequiredDatabase requiredDatabase = (RequiredDatabase)description.getAnnotation((Class)RequiredDatabase.class);
        final boolean runsWithRequiredDatabase = TestHelper.annotationRequiredDatabaseCheck(this.processEngine, requiredDatabase, testClass, methodName, (Class<?>[])new Class[0]);
        return new Statement() {
            public void evaluate() throws Throwable {
                Assume.assumeTrue("ignored because the current history level is too low", hasRequiredHistoryLevel);
                Assume.assumeTrue("ignored because the database doesn't match the required ones", runsWithRequiredDatabase);
                ProcessEngineRule.access$001(ProcessEngineRule.this, base, description).evaluate();
            }
        };
    }
    
    protected void initializeProcessEngine() {
        try {
            this.processEngine = TestHelper.getProcessEngine(this.configurationResource);
        }
        catch (RuntimeException ex) {
            if (ex.getCause() == null || !(ex.getCause() instanceof FileNotFoundException)) {
                throw ex;
            }
            this.processEngine = TestHelper.getProcessEngine(this.configurationResourceCompat);
        }
    }
    
    protected void initializeServices() {
        this.processEngineConfiguration = ((ProcessEngineImpl)this.processEngine).getProcessEngineConfiguration();
        this.repositoryService = this.processEngine.getRepositoryService();
        this.runtimeService = this.processEngine.getRuntimeService();
        this.taskService = this.processEngine.getTaskService();
        this.historyService = this.processEngine.getHistoryService();
        this.identityService = this.processEngine.getIdentityService();
        this.managementService = this.processEngine.getManagementService();
        this.formService = this.processEngine.getFormService();
        this.authorizationService = this.processEngine.getAuthorizationService();
        this.caseService = this.processEngine.getCaseService();
        this.filterService = this.processEngine.getFilterService();
        this.externalTaskService = this.processEngine.getExternalTaskService();
        this.decisionService = this.processEngine.getDecisionService();
    }
    
    protected void clearServiceReferences() {
        this.processEngineConfiguration = null;
        this.repositoryService = null;
        this.runtimeService = null;
        this.taskService = null;
        this.formService = null;
        this.historyService = null;
        this.identityService = null;
        this.managementService = null;
        this.authorizationService = null;
        this.caseService = null;
        this.filterService = null;
        this.externalTaskService = null;
        this.decisionService = null;
    }
    
    public void finished(final Description description) {
        this.identityService.clearAuthentication();
        this.processEngine.getProcessEngineConfiguration().setTenantCheckEnabled(true);
        TestHelper.annotationDeploymentTearDown(this.processEngine, this.deploymentId, description.getTestClass(), description.getMethodName());
        for (final String additionalDeployment : this.additionalDeployments) {
            TestHelper.deleteDeployment(this.processEngine, additionalDeployment);
        }
        if (this.ensureCleanAfterTest) {
            TestHelper.assertAndEnsureCleanDbAndCache(this.processEngine);
        }
        TestHelper.resetIdGenerator(this.processEngineConfiguration);
        ClockUtil.reset();
        this.clearServiceReferences();
        PlatformTelemetryRegistry.clear();
    }
    
    public void setCurrentTime(final Date currentTime) {
        ClockUtil.setCurrentTime(currentTime);
    }
    
    public String getConfigurationResource() {
        return this.configurationResource;
    }
    
    public void setConfigurationResource(final String configurationResource) {
        this.configurationResource = configurationResource;
    }
    
    public ProcessEngine getProcessEngine() {
        return this.processEngine;
    }
    
    public void setProcessEngine(final ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }
    
    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
        return this.processEngineConfiguration;
    }
    
    public void setProcessEngineConfiguration(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
    
    public RepositoryService getRepositoryService() {
        return this.repositoryService;
    }
    
    public void setRepositoryService(final RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }
    
    public RuntimeService getRuntimeService() {
        return this.runtimeService;
    }
    
    public void setRuntimeService(final RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }
    
    public TaskService getTaskService() {
        return this.taskService;
    }
    
    public void setTaskService(final TaskService taskService) {
        this.taskService = taskService;
    }
    
    public HistoryService getHistoryService() {
        return this.historyService;
    }
    
    public void setHistoryService(final HistoryService historyService) {
        this.historyService = historyService;
    }
    
    public void setHistoricDataService(final HistoryService historicService) {
        this.setHistoryService(historicService);
    }
    
    public IdentityService getIdentityService() {
        return this.identityService;
    }
    
    public void setIdentityService(final IdentityService identityService) {
        this.identityService = identityService;
    }
    
    public ManagementService getManagementService() {
        return this.managementService;
    }
    
    public AuthorizationService getAuthorizationService() {
        return this.authorizationService;
    }
    
    public void setAuthorizationService(final AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }
    
    public CaseService getCaseService() {
        return this.caseService;
    }
    
    public void setCaseService(final CaseService caseService) {
        this.caseService = caseService;
    }
    
    public FormService getFormService() {
        return this.formService;
    }
    
    public void setFormService(final FormService formService) {
        this.formService = formService;
    }
    
    public void setManagementService(final ManagementService managementService) {
        this.managementService = managementService;
    }
    
    public FilterService getFilterService() {
        return this.filterService;
    }
    
    public void setFilterService(final FilterService filterService) {
        this.filterService = filterService;
    }
    
    public ExternalTaskService getExternalTaskService() {
        return this.externalTaskService;
    }
    
    public void setExternalTaskService(final ExternalTaskService externalTaskService) {
        this.externalTaskService = externalTaskService;
    }
    
    public DecisionService getDecisionService() {
        return this.decisionService;
    }
    
    public void setDecisionService(final DecisionService decisionService) {
        this.decisionService = decisionService;
    }
    
    public void manageDeployment(final org.zik.bpm.engine.repository.Deployment deployment) {
        this.additionalDeployments.add(deployment.getId());
    }
    
    static /* synthetic */ Statement access$001(final ProcessEngineRule x0, final Statement x1, final Description x2) {
        return x0.apply(x1, x2);
    }
}
