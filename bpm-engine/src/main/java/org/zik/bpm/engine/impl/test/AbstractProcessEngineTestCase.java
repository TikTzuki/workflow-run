// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.test;

import org.zik.bpm.engine.query.Query;
import org.apache.ibatis.logging.LogFactory;
import org.zik.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import java.util.Collection;
import java.util.ArrayList;
import org.zik.bpm.engine.runtime.ActivityInstance;
import java.util.concurrent.Callable;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutor;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.TimerTask;
import java.util.Timer;
import org.zik.bpm.engine.runtime.CaseInstance;
import org.zik.bpm.engine.runtime.ProcessInstance;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.impl.util.ClockUtil;
import junit.framework.AssertionFailedError;
import java.util.HashSet;
import org.zik.bpm.engine.DecisionService;
import org.zik.bpm.engine.ExternalTaskService;
import org.zik.bpm.engine.FilterService;
import org.zik.bpm.engine.CaseService;
import org.zik.bpm.engine.AuthorizationService;
import org.zik.bpm.engine.ManagementService;
import org.zik.bpm.engine.IdentityService;
import org.zik.bpm.engine.HistoryService;
import org.zik.bpm.engine.FormService;
import org.zik.bpm.engine.TaskService;
import org.zik.bpm.engine.RuntimeService;
import org.zik.bpm.engine.RepositoryService;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Set;
import org.zik.bpm.engine.ProcessEngine;
import org.slf4j.Logger;

public abstract class AbstractProcessEngineTestCase extends PvmTestCase
{
    private static final Logger LOG;
    protected ProcessEngine processEngine;
    protected String deploymentId;
    protected Set<String> deploymentIds;
    protected Throwable exception;
    protected ProcessEngineConfigurationImpl processEngineConfiguration;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected FormService formService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;
    protected AuthorizationService authorizationService;
    protected CaseService caseService;
    protected FilterService filterService;
    protected ExternalTaskService externalTaskService;
    protected DecisionService decisionService;
    
    public AbstractProcessEngineTestCase() {
        this.deploymentIds = new HashSet<String>();
    }
    
    protected abstract void initializeProcessEngine();
    
    protected void closeDownProcessEngine() {
    }
    
    public void runBare() throws Throwable {
        this.initializeProcessEngine();
        while (true) {
            if (this.repositoryService == null) {
                this.initializeServices();
                try {
                    final boolean hasRequiredHistoryLevel = TestHelper.annotationRequiredHistoryLevelCheck(this.processEngine, this.getClass(), this.getName(), (Class<?>[])new Class[0]);
                    final boolean runsWithRequiredDatabase = TestHelper.annotationRequiredDatabaseCheck(this.processEngine, this.getClass(), this.getName(), (Class<?>[])new Class[0]);
                    if (hasRequiredHistoryLevel && runsWithRequiredDatabase) {
                        this.deploymentId = TestHelper.annotationDeploymentSetUp(this.processEngine, this.getClass(), this.getName(), (Class<?>[])new Class[0]);
                        super.runBare();
                    }
                }
                catch (AssertionFailedError e) {
                    AbstractProcessEngineTestCase.LOG.error("ASSERTION FAILED: " + e, (Throwable)e);
                    throw this.exception = (Throwable)e;
                }
                catch (Throwable e2) {
                    AbstractProcessEngineTestCase.LOG.error("EXCEPTION: " + e2, e2);
                    throw this.exception = e2;
                }
                finally {
                    this.identityService.clearAuthentication();
                    this.processEngineConfiguration.setTenantCheckEnabled(true);
                    this.deleteDeployments();
                    this.deleteHistoryCleanupJobs();
                    TestHelper.assertAndEnsureCleanDbAndCache(this.processEngine, this.exception == null);
                    TestHelper.resetIdGenerator(this.processEngineConfiguration);
                    ClockUtil.reset();
                    this.closeDownProcessEngine();
                    this.clearServiceReferences();
                }
                return;
            }
            continue;
        }
    }
    
    protected void deleteHistoryCleanupJobs() {
        final List<Job> jobs = this.historyService.findHistoryCleanupJobs();
        for (final Job job : jobs) {
            this.processEngineConfiguration.getCommandExecutorTxRequired().execute((Command<Object>)new Command<Void>() {
                @Override
                public Void execute(final CommandContext commandContext) {
                    commandContext.getJobManager().deleteJob((JobEntity)job);
                    return null;
                }
            });
        }
    }
    
    protected void deleteDeployments() {
        if (this.deploymentId != null) {
            this.deploymentIds.add(this.deploymentId);
        }
        for (final String deploymentId : this.deploymentIds) {
            TestHelper.annotationDeploymentTearDown(this.processEngine, deploymentId, this.getClass(), this.getName());
        }
        this.deploymentId = null;
        this.deploymentIds.clear();
    }
    
    protected void initializeServices() {
        this.processEngineConfiguration = ((ProcessEngineImpl)this.processEngine).getProcessEngineConfiguration();
        this.repositoryService = this.processEngine.getRepositoryService();
        this.runtimeService = this.processEngine.getRuntimeService();
        this.taskService = this.processEngine.getTaskService();
        this.formService = this.processEngine.getFormService();
        this.historyService = this.processEngine.getHistoryService();
        this.identityService = this.processEngine.getIdentityService();
        this.managementService = this.processEngine.getManagementService();
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
    
    public void assertProcessEnded(final String processInstanceId) {
        final ProcessInstance processInstance = ((Query<T, ProcessInstance>)this.processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId)).singleResult();
        if (processInstance != null) {
            throw new AssertionFailedError("Expected finished process instance '" + processInstanceId + "' but it was still in the db");
        }
    }
    
    public void assertProcessNotEnded(final String processInstanceId) {
        final ProcessInstance processInstance = ((Query<T, ProcessInstance>)this.processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId)).singleResult();
        if (processInstance == null) {
            throw new AssertionFailedError("Expected process instance '" + processInstanceId + "' to be still active but it was not in the db");
        }
    }
    
    public void assertCaseEnded(final String caseInstanceId) {
        final CaseInstance caseInstance = ((Query<T, CaseInstance>)this.processEngine.getCaseService().createCaseInstanceQuery().caseInstanceId(caseInstanceId)).singleResult();
        if (caseInstance != null) {
            throw new AssertionFailedError("Expected finished case instance '" + caseInstanceId + "' but it was still in the db");
        }
    }
    
    @Deprecated
    public void waitForJobExecutorToProcessAllJobs(final long maxMillisToWait, final long intervalMillis) {
        this.waitForJobExecutorToProcessAllJobs(maxMillisToWait);
    }
    
    public void waitForJobExecutorToProcessAllJobs(long maxMillisToWait) {
        final JobExecutor jobExecutor = this.processEngineConfiguration.getJobExecutor();
        jobExecutor.start();
        final long intervalMillis = 1000L;
        final int jobExecutorWaitTime = jobExecutor.getWaitTimeInMillis() * 2;
        if (maxMillisToWait < jobExecutorWaitTime) {
            maxMillisToWait = jobExecutorWaitTime;
        }
        try {
            final Timer timer = new Timer();
            final InterruptTask task = new InterruptTask(Thread.currentThread());
            timer.schedule(task, maxMillisToWait);
            boolean areJobsAvailable = true;
            try {
                while (areJobsAvailable && !task.isTimeLimitExceeded()) {
                    Thread.sleep(intervalMillis);
                    try {
                        areJobsAvailable = this.areJobsAvailable();
                    }
                    catch (Throwable t) {}
                }
            }
            catch (InterruptedException ex) {}
            finally {
                timer.cancel();
            }
            if (areJobsAvailable) {
                throw new ProcessEngineException("time limit of " + maxMillisToWait + " was exceeded");
            }
        }
        finally {
            jobExecutor.shutdown();
        }
    }
    
    @Deprecated
    public void waitForJobExecutorOnCondition(final long maxMillisToWait, final long intervalMillis, final Callable<Boolean> condition) {
        this.waitForJobExecutorOnCondition(maxMillisToWait, condition);
    }
    
    public void waitForJobExecutorOnCondition(long maxMillisToWait, final Callable<Boolean> condition) {
        final JobExecutor jobExecutor = this.processEngineConfiguration.getJobExecutor();
        jobExecutor.start();
        final long intervalMillis = 500L;
        if (maxMillisToWait < jobExecutor.getWaitTimeInMillis() * 2) {
            maxMillisToWait = jobExecutor.getWaitTimeInMillis() * 2;
        }
        try {
            final Timer timer = new Timer();
            final InterruptTask task = new InterruptTask(Thread.currentThread());
            timer.schedule(task, maxMillisToWait);
            boolean conditionIsViolated = true;
            try {
                while (conditionIsViolated && !task.isTimeLimitExceeded()) {
                    Thread.sleep(intervalMillis);
                    conditionIsViolated = !condition.call();
                }
            }
            catch (InterruptedException ex) {}
            catch (Exception e) {
                throw new ProcessEngineException("Exception while waiting on condition: " + e.getMessage(), e);
            }
            finally {
                timer.cancel();
            }
            if (conditionIsViolated) {
                throw new ProcessEngineException("time limit of " + maxMillisToWait + " was exceeded");
            }
        }
        finally {
            jobExecutor.shutdown();
        }
    }
    
    public void executeAvailableJobs() {
        this.executeAvailableJobs(0, Integer.MAX_VALUE, true, true);
    }
    
    public void executeAvailableJobs(final int expectedExecutions) {
        this.executeAvailableJobs(0, expectedExecutions, false, true);
    }
    
    public void executeAvailableJobs(final boolean recursive) {
        this.executeAvailableJobs(0, Integer.MAX_VALUE, true, recursive);
    }
    
    private void executeAvailableJobs(int jobsExecuted, final int expectedExecutions, final boolean ignoreLessExecutions, final boolean recursive) {
        final List<Job> jobs = ((Query<T, Job>)this.managementService.createJobQuery().withRetriesLeft()).list();
        if (jobs.isEmpty()) {
            assertTrue("executed less jobs than expected. expected <" + expectedExecutions + "> actual <" + jobsExecuted + ">", jobsExecuted == expectedExecutions || ignoreLessExecutions);
            return;
        }
        for (final Job job : jobs) {
            try {
                this.managementService.executeJob(job.getId());
                ++jobsExecuted;
            }
            catch (Exception ex) {}
        }
        assertTrue("executed more jobs than expected. expected <" + expectedExecutions + "> actual <" + jobsExecuted + ">", jobsExecuted <= expectedExecutions);
        if (recursive) {
            this.executeAvailableJobs(jobsExecuted, expectedExecutions, ignoreLessExecutions, recursive);
        }
    }
    
    public boolean areJobsAvailable() {
        final List<Job> list = ((Query<T, Job>)this.managementService.createJobQuery()).list();
        for (final Job job : list) {
            if (!job.isSuspended() && job.getRetries() > 0 && (job.getDuedate() == null || ClockUtil.getCurrentTime().after(job.getDuedate()))) {
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    protected List<ActivityInstance> getInstancesForActivitiyId(final ActivityInstance activityInstance, final String activityId) {
        return this.getInstancesForActivityId(activityInstance, activityId);
    }
    
    protected List<ActivityInstance> getInstancesForActivityId(final ActivityInstance activityInstance, final String activityId) {
        final List<ActivityInstance> result = new ArrayList<ActivityInstance>();
        if (activityInstance.getActivityId().equals(activityId)) {
            result.add(activityInstance);
        }
        for (final ActivityInstance childInstance : activityInstance.getChildActivityInstances()) {
            result.addAll(this.getInstancesForActivityId(childInstance, activityId));
        }
        return result;
    }
    
    protected void runAsUser(final String userId, final List<String> groupIds, final Runnable r) {
        try {
            this.identityService.setAuthenticatedUserId(userId);
            this.processEngineConfiguration.setAuthorizationEnabled(true);
            r.run();
        }
        finally {
            this.identityService.setAuthenticatedUserId(null);
            this.processEngineConfiguration.setAuthorizationEnabled(false);
        }
    }
    
    protected String deployment(final BpmnModelInstance... bpmnModelInstances) {
        final DeploymentBuilder deploymentBuilder = this.repositoryService.createDeployment();
        return this.deployment(deploymentBuilder, bpmnModelInstances);
    }
    
    protected String deployment(final String... resources) {
        final DeploymentBuilder deploymentBuilder = this.repositoryService.createDeployment();
        return this.deployment(deploymentBuilder, resources);
    }
    
    protected String deploymentForTenant(final String tenantId, final BpmnModelInstance... bpmnModelInstances) {
        final DeploymentBuilder deploymentBuilder = this.repositoryService.createDeployment().tenantId(tenantId);
        return this.deployment(deploymentBuilder, bpmnModelInstances);
    }
    
    protected String deploymentForTenant(final String tenantId, final String... resources) {
        final DeploymentBuilder deploymentBuilder = this.repositoryService.createDeployment().tenantId(tenantId);
        return this.deployment(deploymentBuilder, resources);
    }
    
    protected String deploymentForTenant(final String tenantId, final String classpathResource, final BpmnModelInstance modelInstance) {
        return this.deployment(this.repositoryService.createDeployment().tenantId(tenantId).addClasspathResource(classpathResource), modelInstance);
    }
    
    protected String deployment(final DeploymentBuilder deploymentBuilder, final BpmnModelInstance... bpmnModelInstances) {
        for (int i = 0; i < bpmnModelInstances.length; ++i) {
            final BpmnModelInstance bpmnModelInstance = bpmnModelInstances[i];
            deploymentBuilder.addModelInstance("testProcess-" + i + ".bpmn", bpmnModelInstance);
        }
        return this.deploymentWithBuilder(deploymentBuilder);
    }
    
    protected String deployment(final DeploymentBuilder deploymentBuilder, final String... resources) {
        for (int i = 0; i < resources.length; ++i) {
            deploymentBuilder.addClasspathResource(resources[i]);
        }
        return this.deploymentWithBuilder(deploymentBuilder);
    }
    
    protected String deploymentWithBuilder(final DeploymentBuilder builder) {
        this.deploymentId = builder.deploy().getId();
        this.deploymentIds.add(this.deploymentId);
        return this.deploymentId;
    }
    
    static {
        LOG = TestLogger.TEST_LOGGER.getLogger();
        LogFactory.useSlf4jLogging();
    }
    
    private static class InterruptTask extends TimerTask
    {
        protected boolean timeLimitExceeded;
        protected Thread thread;
        
        public InterruptTask(final Thread thread) {
            this.timeLimitExceeded = false;
            this.thread = thread;
        }
        
        public boolean isTimeLimitExceeded() {
            return this.timeLimitExceeded;
        }
        
        @Override
        public void run() {
            this.timeLimitExceeded = true;
            this.thread.interrupt();
        }
    }
}
