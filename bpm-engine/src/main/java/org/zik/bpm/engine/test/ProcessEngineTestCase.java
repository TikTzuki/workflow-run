// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.test;

import java.util.Date;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import java.io.FileNotFoundException;
import org.zik.bpm.engine.impl.test.TestHelper;
import org.zik.bpm.engine.impl.test.ProcessEngineAssert;
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
import org.zik.bpm.engine.ProcessEngine;
import junit.framework.TestCase;

public class ProcessEngineTestCase extends TestCase
{
    protected String configurationResource;
    protected String configurationResourceCompat;
    protected String deploymentId;
    protected ProcessEngine processEngine;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    @Deprecated
    protected HistoryService historicDataService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;
    protected FormService formService;
    protected FilterService filterService;
    protected AuthorizationService authorizationService;
    protected CaseService caseService;
    protected boolean skipTest;
    
    public ProcessEngineTestCase() {
        this.configurationResource = "camunda.cfg.xml";
        this.configurationResourceCompat = "activiti.cfg.xml";
        this.deploymentId = null;
        this.skipTest = false;
    }
    
    public void assertProcessEnded(final String processInstanceId) {
        ProcessEngineAssert.assertProcessEnded(this.processEngine, processInstanceId);
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        if (this.processEngine == null) {
            this.initializeProcessEngine();
            this.initializeServices();
        }
        final boolean hasRequiredHistoryLevel = TestHelper.annotationRequiredHistoryLevelCheck(this.processEngine, this.getClass(), this.getName(), (Class<?>[])new Class[0]);
        if (!(this.skipTest = !hasRequiredHistoryLevel)) {
            this.deploymentId = TestHelper.annotationDeploymentSetUp(this.processEngine, this.getClass(), this.getName(), (Class<?>[])new Class[0]);
        }
    }
    
    protected void runTest() throws Throwable {
        if (!this.skipTest) {
            super.runTest();
        }
    }
    
    protected void initializeProcessEngine() {
        try {
            this.processEngine = TestHelper.getProcessEngine(this.getConfigurationResource());
        }
        catch (RuntimeException ex) {
            if (ex.getCause() == null || !(ex.getCause() instanceof FileNotFoundException)) {
                throw ex;
            }
            this.processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(this.configurationResourceCompat).buildProcessEngine();
        }
    }
    
    protected void initializeServices() {
        this.repositoryService = this.processEngine.getRepositoryService();
        this.runtimeService = this.processEngine.getRuntimeService();
        this.taskService = this.processEngine.getTaskService();
        this.historicDataService = this.processEngine.getHistoryService();
        this.historyService = this.processEngine.getHistoryService();
        this.identityService = this.processEngine.getIdentityService();
        this.managementService = this.processEngine.getManagementService();
        this.formService = this.processEngine.getFormService();
        this.filterService = this.processEngine.getFilterService();
        this.authorizationService = this.processEngine.getAuthorizationService();
        this.caseService = this.processEngine.getCaseService();
    }
    
    protected void tearDown() throws Exception {
        TestHelper.annotationDeploymentTearDown(this.processEngine, this.deploymentId, this.getClass(), this.getName());
        ClockUtil.reset();
        super.tearDown();
    }
    
    public static void closeProcessEngines() {
        TestHelper.closeProcessEngines();
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
}
