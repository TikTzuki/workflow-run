// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.test;

import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.query.Query;
import org.zik.bpm.engine.impl.dmn.deployer.DecisionDefinitionDeployer;
import org.zik.bpm.engine.impl.cmmn.deployer.CmmnDeployer;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.cmmn.behavior.CaseControlRuleImpl;
import org.zik.bpm.engine.impl.el.FixedValue;
import org.zik.bpm.engine.HistoryService;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManager;
import org.zik.bpm.engine.impl.HistoryLevelSetupCommand;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyEntity;
import org.zik.bpm.engine.impl.db.PersistenceSession;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.impl.cfg.IdGenerator;
import org.zik.bpm.engine.impl.db.DbIdGenerator;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutor;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.TimerTask;
import java.util.Timer;
import org.zik.bpm.engine.impl.application.ProcessApplicationManager;
import org.zik.bpm.engine.impl.management.DatabasePurgeReport;
import org.zik.bpm.engine.impl.persistence.deploy.cache.CachePurgeReport;
import org.zik.bpm.engine.impl.management.PurgeReport;
import org.junit.Assert;
import org.zik.bpm.engine.impl.ManagementServiceImpl;
import org.zik.bpm.engine.impl.ProcessEngineImpl;
import java.lang.annotation.Annotation;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.test.RequiredHistoryLevel;
import java.io.InputStream;
import java.util.Iterator;
import org.zik.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.zik.bpm.engine.impl.util.ReflectUtil;
import org.zik.bpm.engine.repository.DeploymentBuilder;
import java.lang.reflect.Method;
import org.zik.bpm.engine.impl.util.ClassNameUtil;
import org.zik.bpm.engine.test.Deployment;
import org.zik.bpm.engine.ProcessEngine;
import java.util.Map;
import java.util.List;
import org.slf4j.Logger;

public abstract class TestHelper
{
    private static Logger LOG;
    public static final String EMPTY_LINE = "                                                                                           ";
    public static final List<String> TABLENAMES_EXCLUDED_FROM_DB_CLEAN_CHECK;
    static Map<String, ProcessEngine> processEngines;
    public static final List<String> RESOURCE_SUFFIXES;
    
    @Deprecated
    public static void assertProcessEnded(final ProcessEngine processEngine, final String processInstanceId) {
        ProcessEngineAssert.assertProcessEnded(processEngine, processInstanceId);
    }
    
    public static String annotationDeploymentSetUp(final ProcessEngine processEngine, Class<?> testClass, final String methodName, Deployment deploymentAnnotation, final Class<?>... parameterTypes) {
        Method method = null;
        boolean onMethod = true;
        try {
            method = getMethod(testClass, methodName, parameterTypes);
        }
        catch (Exception e) {
            if (deploymentAnnotation == null) {
                return null;
            }
        }
        if (deploymentAnnotation == null) {
            deploymentAnnotation = method.getAnnotation(Deployment.class);
        }
        if (deploymentAnnotation == null) {
            onMethod = false;
            for (Class<?> lookForAnnotationClass = testClass; lookForAnnotationClass != Object.class; lookForAnnotationClass = lookForAnnotationClass.getSuperclass()) {
                deploymentAnnotation = lookForAnnotationClass.getAnnotation(Deployment.class);
                if (deploymentAnnotation != null) {
                    testClass = lookForAnnotationClass;
                    break;
                }
            }
        }
        if (deploymentAnnotation != null) {
            final String[] resources = deploymentAnnotation.resources();
            TestHelper.LOG.debug("annotation @Deployment creates deployment for {}.{}", (Object)ClassNameUtil.getClassNameWithoutPackage(testClass), (Object)methodName);
            return annotationDeploymentSetUp(processEngine, resources, testClass, onMethod, methodName);
        }
        return null;
    }
    
    public static String annotationDeploymentSetUp(final ProcessEngine processEngine, final String[] resources, final Class<?> testClass, final String methodName) {
        return annotationDeploymentSetUp(processEngine, resources, testClass, true, methodName);
    }
    
    public static String annotationDeploymentSetUp(final ProcessEngine processEngine, String[] resources, final Class<?> testClass, final boolean onMethod, final String methodName) {
        if (resources != null) {
            if (resources.length == 0 && methodName != null) {
                final String name = onMethod ? methodName : null;
                final String resource = getBpmnProcessDefinitionResource(testClass, name);
                resources = new String[] { resource };
            }
            final DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService().createDeployment().name(ClassNameUtil.getClassNameWithoutPackage(testClass) + "." + methodName);
            for (final String resource2 : resources) {
                deploymentBuilder.addClasspathResource(resource2);
            }
            return deploymentBuilder.deploy().getId();
        }
        return null;
    }
    
    public static String annotationDeploymentSetUp(final ProcessEngine processEngine, final Class<?> testClass, final String methodName, final Class<?>... parameterTypes) {
        return annotationDeploymentSetUp(processEngine, testClass, methodName, (Deployment)null, parameterTypes);
    }
    
    public static void annotationDeploymentTearDown(final ProcessEngine processEngine, final String deploymentId, final Class<?> testClass, final String methodName) {
        TestHelper.LOG.debug("annotation @Deployment deletes deployment for {}.{}", (Object)ClassNameUtil.getClassNameWithoutPackage(testClass), (Object)methodName);
        deleteDeployment(processEngine, deploymentId);
    }
    
    public static void deleteDeployment(final ProcessEngine processEngine, final String deploymentId) {
        if (deploymentId != null) {
            processEngine.getRepositoryService().deleteDeployment(deploymentId, true, true, true);
        }
    }
    
    public static String getBpmnProcessDefinitionResource(final Class<?> type, final String name) {
        for (final String suffix : TestHelper.RESOURCE_SUFFIXES) {
            final String resource = createResourceName(type, name, suffix);
            final InputStream inputStream = ReflectUtil.getResourceAsStream(resource);
            if (inputStream == null) {
                continue;
            }
            return resource;
        }
        return createResourceName(type, name, BpmnDeployer.BPMN_RESOURCE_SUFFIXES[0]);
    }
    
    private static String createResourceName(final Class<?> type, final String name, final String suffix) {
        final StringBuilder r = new StringBuilder(type.getName().replace('.', '/'));
        if (name != null) {
            r.append("." + name);
        }
        return r.append("." + suffix).toString();
    }
    
    public static boolean annotationRequiredHistoryLevelCheck(final ProcessEngine processEngine, final RequiredHistoryLevel annotation, final Class<?> testClass, final String methodName) {
        if (annotation != null) {
            return historyLevelCheck(processEngine, annotation);
        }
        return annotationRequiredHistoryLevelCheck(processEngine, testClass, methodName, (Class<?>[])new Class[0]);
    }
    
    private static boolean historyLevelCheck(final ProcessEngine processEngine, final RequiredHistoryLevel annotation) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl)processEngine.getProcessEngineConfiguration();
        final HistoryLevel requiredHistoryLevel = getHistoryLevelForName(processEngineConfiguration.getHistoryLevels(), annotation.value());
        final HistoryLevel currentHistoryLevel = processEngineConfiguration.getHistoryLevel();
        return currentHistoryLevel.getId() >= requiredHistoryLevel.getId();
    }
    
    private static HistoryLevel getHistoryLevelForName(final List<HistoryLevel> historyLevels, final String name) {
        for (final HistoryLevel historyLevel : historyLevels) {
            if (historyLevel.getName().equalsIgnoreCase(name)) {
                return historyLevel;
            }
        }
        throw new IllegalArgumentException("Unknown history level: " + name);
    }
    
    public static boolean annotationRequiredHistoryLevelCheck(final ProcessEngine processEngine, final Class<?> testClass, final String methodName, final Class<?>... parameterTypes) {
        final RequiredHistoryLevel annotation = getAnnotation(processEngine, testClass, methodName, RequiredHistoryLevel.class, parameterTypes);
        return annotation == null || historyLevelCheck(processEngine, annotation);
    }
    
    public static boolean annotationRequiredDatabaseCheck(final ProcessEngine processEngine, final RequiredDatabase annotation, final Class<?> testClass, final String methodName, final Class<?>... parameterTypes) {
        if (annotation != null) {
            return databaseCheck(processEngine, annotation);
        }
        return annotationRequiredDatabaseCheck(processEngine, testClass, methodName, parameterTypes);
    }
    
    public static boolean annotationRequiredDatabaseCheck(final ProcessEngine processEngine, final Class<?> testClass, final String methodName, final Class<?>... parameterTypes) {
        final RequiredDatabase annotation = getAnnotation(processEngine, testClass, methodName, RequiredDatabase.class, parameterTypes);
        return annotation == null || databaseCheck(processEngine, annotation);
    }
    
    private static boolean databaseCheck(final ProcessEngine processEngine, final RequiredDatabase annotation) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl)processEngine.getProcessEngineConfiguration();
        final String actualDbType = processEngineConfiguration.getDbSqlSessionFactory().getDatabaseType();
        final String[] excludes = annotation.excludes();
        if (excludes != null) {
            for (final String exclude : excludes) {
                if (exclude.equals(actualDbType)) {
                    return false;
                }
            }
        }
        final String[] includes = annotation.includes();
        if (includes != null && includes.length > 0) {
            for (final String include : includes) {
                if (include.equals(actualDbType)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    private static <T extends Annotation> T getAnnotation(final ProcessEngine processEngine, final Class<?> testClass, final String methodName, final Class<T> annotationClass, final Class<?>... parameterTypes) {
        Method method = null;
        T annotation = null;
        try {
            method = getMethod(testClass, methodName, parameterTypes);
            annotation = method.getAnnotation(annotationClass);
        }
        catch (Exception ex) {}
        if (annotation == null) {
            annotation = testClass.getAnnotation(annotationClass);
        }
        return annotation;
    }
    
    protected static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException {
        return clazz.getMethod(methodName, parameterTypes);
    }
    
    public static void assertAndEnsureCleanDbAndCache(final ProcessEngine processEngine) {
        assertAndEnsureCleanDbAndCache(processEngine, true);
    }
    
    public static String assertAndEnsureCleanDbAndCache(final ProcessEngine processEngine, final boolean fail) {
        final ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl)processEngine).getProcessEngineConfiguration();
        clearUserOperationLog(processEngineConfiguration);
        TestHelper.LOG.debug("verifying that db is clean after test");
        final PurgeReport purgeReport = ((ManagementServiceImpl)processEngine.getManagementService()).purge();
        final String paRegistrationMessage = assertAndEnsureNoProcessApplicationsRegistered(processEngine);
        final StringBuilder message = new StringBuilder();
        final CachePurgeReport cachePurgeReport = purgeReport.getCachePurgeReport();
        if (!cachePurgeReport.isEmpty()) {
            message.append("Deployment cache is not clean:\n").append(cachePurgeReport.getPurgeReportAsString());
        }
        else {
            TestHelper.LOG.debug("Deployment cache was clean.");
        }
        final DatabasePurgeReport databasePurgeReport = purgeReport.getDatabasePurgeReport();
        if (!databasePurgeReport.isEmpty()) {
            message.append("Database is not clean:\n").append(databasePurgeReport.getPurgeReportAsString());
        }
        else {
            TestHelper.LOG.debug(purgeReport.getDatabasePurgeReport().isDbContainsLicenseKey() ? "Database contains license key but is considered clean." : "Database was clean.");
        }
        if (paRegistrationMessage != null) {
            message.append(paRegistrationMessage);
        }
        if (fail && message.length() > 0) {
            Assert.fail(message.toString());
        }
        return message.toString();
    }
    
    public static void assertAndEnsureCleanDeploymentCache(final ProcessEngine processEngine) {
        assertAndEnsureCleanDeploymentCache(processEngine, true);
    }
    
    public static String assertAndEnsureCleanDeploymentCache(final ProcessEngine processEngine, final boolean fail) {
        final StringBuilder outputMessage = new StringBuilder();
        final ProcessEngineConfigurationImpl processEngineConfiguration = ((ProcessEngineImpl)processEngine).getProcessEngineConfiguration();
        final CachePurgeReport cachePurgeReport = processEngineConfiguration.getDeploymentCache().purgeCache();
        outputMessage.append(cachePurgeReport.getPurgeReportAsString());
        if (outputMessage.length() > 0) {
            outputMessage.insert(0, "Deployment cache not clean:\n");
            TestHelper.LOG.error(outputMessage.toString());
            if (fail) {
                Assert.fail(outputMessage.toString());
            }
            return outputMessage.toString();
        }
        TestHelper.LOG.debug("Deployment cache was clean");
        return null;
    }
    
    public static String assertAndEnsureNoProcessApplicationsRegistered(final ProcessEngine processEngine) {
        final ProcessEngineConfigurationImpl engineConfiguration = (ProcessEngineConfigurationImpl)processEngine.getProcessEngineConfiguration();
        final ProcessApplicationManager processApplicationManager = engineConfiguration.getProcessApplicationManager();
        if (processApplicationManager.hasRegistrations()) {
            processApplicationManager.clearRegistrations();
            return "There are still process applications registered";
        }
        return null;
    }
    
    public static void waitForJobExecutorToProcessAllJobs(final ProcessEngineConfigurationImpl processEngineConfiguration, final long maxMillisToWait, final long intervalMillis) {
        final JobExecutor jobExecutor = processEngineConfiguration.getJobExecutor();
        jobExecutor.start();
        try {
            final Timer timer = new Timer();
            final InteruptTask task = new InteruptTask(Thread.currentThread());
            timer.schedule(task, maxMillisToWait);
            boolean areJobsAvailable = true;
            try {
                while (areJobsAvailable && !task.isTimeLimitExceeded()) {
                    Thread.sleep(intervalMillis);
                    areJobsAvailable = areJobsAvailable(processEngineConfiguration);
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
    
    public static boolean areJobsAvailable(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        return !((Query<T, Job>)processEngineConfiguration.getManagementService().createJobQuery().executable()).list().isEmpty();
    }
    
    public static void resetIdGenerator(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        final IdGenerator idGenerator = processEngineConfiguration.getIdGenerator();
        if (idGenerator instanceof DbIdGenerator) {
            ((DbIdGenerator)idGenerator).reset();
        }
    }
    
    public static ProcessEngine getProcessEngine(final String configurationResource) {
        ProcessEngine processEngine = TestHelper.processEngines.get(configurationResource);
        if (processEngine == null) {
            TestHelper.LOG.debug("==== BUILDING PROCESS ENGINE ========================================================================");
            processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(configurationResource).buildProcessEngine();
            TestHelper.LOG.debug("==== PROCESS ENGINE CREATED =========================================================================");
            TestHelper.processEngines.put(configurationResource, processEngine);
        }
        return processEngine;
    }
    
    public static void closeProcessEngines() {
        for (final ProcessEngine processEngine : TestHelper.processEngines.values()) {
            processEngine.close();
        }
        TestHelper.processEngines.clear();
    }
    
    public static void createSchema(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        processEngineConfiguration.getCommandExecutorTxRequired().execute(commandContext -> {
            commandContext.getSession(PersistenceSession.class).dbSchemaCreate();
            return null;
        });
    }
    
    public static void dropSchema(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        processEngineConfiguration.getCommandExecutorTxRequired().execute(commandContext -> {
            commandContext.getDbSqlSession().dbSchemaDrop();
            return null;
        });
    }
    
    public static void createOrUpdateHistoryLevel(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        final DbEntityManager dbEntityManager;
        final PropertyEntity historyLevelProperty;
        processEngineConfiguration.getCommandExecutorTxRequired().execute(commandContext -> {
            dbEntityManager = commandContext.getDbEntityManager();
            historyLevelProperty = dbEntityManager.selectById(PropertyEntity.class, "historyLevel");
            if (historyLevelProperty != null) {
                if (processEngineConfiguration.getHistoryLevel().getId() != new Integer(historyLevelProperty.getValue())) {
                    historyLevelProperty.setValue(Integer.toString(processEngineConfiguration.getHistoryLevel().getId()));
                    dbEntityManager.merge(historyLevelProperty);
                }
            }
            else {
                HistoryLevelSetupCommand.dbCreateHistoryLevel(commandContext);
            }
            return null;
        });
    }
    
    public static void deleteHistoryLevel(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        final DbEntityManager dbEntityManager;
        final PropertyEntity historyLevelProperty;
        processEngineConfiguration.getCommandExecutorTxRequired().execute(commandContext -> {
            dbEntityManager = commandContext.getDbEntityManager();
            historyLevelProperty = dbEntityManager.selectById(PropertyEntity.class, "historyLevel");
            if (historyLevelProperty != null) {
                dbEntityManager.delete(historyLevelProperty);
            }
            return null;
        });
    }
    
    public static void clearUserOperationLog(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        if (processEngineConfiguration.getHistoryLevel().equals(HistoryLevel.HISTORY_LEVEL_FULL)) {
            final HistoryService historyService = processEngineConfiguration.getHistoryService();
            final List<UserOperationLogEntry> logs = ((Query<T, UserOperationLogEntry>)historyService.createUserOperationLogQuery()).list();
            for (final UserOperationLogEntry log : logs) {
                historyService.deleteUserOperationLogEntry(log.getId());
            }
        }
    }
    
    public static void deleteTelemetryProperty(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        final DbEntityManager dbEntityManager;
        final PropertyEntity telemetryProperty;
        processEngineConfiguration.getCommandExecutorTxRequired().execute(commandContext -> {
            dbEntityManager = commandContext.getDbEntityManager();
            telemetryProperty = dbEntityManager.selectById(PropertyEntity.class, "camunda.telemetry.enabled");
            if (telemetryProperty != null) {
                dbEntityManager.delete(telemetryProperty);
            }
            return null;
        });
    }
    
    public static void deleteInstallationId(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        final DbEntityManager dbEntityManager;
        final PropertyEntity installationIdProperty;
        processEngineConfiguration.getCommandExecutorTxRequired().execute(commandContext -> {
            dbEntityManager = commandContext.getDbEntityManager();
            installationIdProperty = dbEntityManager.selectById(PropertyEntity.class, "camunda.installation.id");
            if (installationIdProperty != null) {
                dbEntityManager.delete(installationIdProperty);
            }
            return null;
        });
    }
    
    public static Object defaultManualActivation() {
        final Expression expression = new FixedValue(true);
        final CaseControlRuleImpl caseControlRule = new CaseControlRuleImpl(expression);
        return caseControlRule;
    }
    
    static {
        TestHelper.LOG = ProcessEngineLogger.TEST_LOGGER.getLogger();
        TABLENAMES_EXCLUDED_FROM_DB_CLEAN_CHECK = Arrays.asList("ACT_GE_PROPERTY", "ACT_GE_SCHEMA_LOG");
        TestHelper.processEngines = new HashMap<String, ProcessEngine>();
        (RESOURCE_SUFFIXES = new ArrayList<String>()).addAll(Arrays.asList(BpmnDeployer.BPMN_RESOURCE_SUFFIXES));
        TestHelper.RESOURCE_SUFFIXES.addAll(Arrays.asList(CmmnDeployer.CMMN_RESOURCE_SUFFIXES));
        TestHelper.RESOURCE_SUFFIXES.addAll(Arrays.asList(DecisionDefinitionDeployer.DMN_RESOURCE_SUFFIXES));
    }
    
    private static class InteruptTask extends TimerTask
    {
        protected boolean timeLimitExceeded;
        protected Thread thread;
        
        public InteruptTask(final Thread thread) {
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
