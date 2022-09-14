// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.ProcessEngineBootstrapCommand;
import org.zik.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.zik.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import java.io.InputStream;
import org.zik.bpm.engine.impl.cfg.BeansConfigurationHelper;
import java.util.Collections;
import org.zik.bpm.engine.impl.BootstrapEngineCommand;
import org.zik.bpm.engine.impl.SchemaOperationsProcessEngineBuild;
import org.zik.bpm.engine.impl.telemetry.TelemetryRegistry;
import org.zik.bpm.engine.runtime.DeserializationTypeValidator;
import java.util.List;
import org.camunda.bpm.engine.variable.type.ValueTypeResolver;
import org.zik.bpm.engine.identity.PasswordPolicy;
import org.zik.bpm.engine.impl.HistoryLevelSetupCommand;
import javax.sql.DataSource;

public abstract class ProcessEngineConfiguration
{
    public static final String DB_SCHEMA_UPDATE_FALSE = "false";
    public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";
    public static final String DB_SCHEMA_UPDATE_TRUE = "true";
    public static final String HISTORY_NONE = "none";
    public static final String HISTORY_ACTIVITY = "activity";
    @Deprecated
    public static final String HISTORY_VARIABLE = "variable";
    public static final String HISTORY_AUDIT = "audit";
    public static final String HISTORY_FULL = "full";
    public static final String HISTORY_AUTO = "auto";
    public static final String HISTORY_DEFAULT = "audit";
    public static final String HISTORY_CLEANUP_STRATEGY_END_TIME_BASED = "endTimeBased";
    public static final String HISTORY_CLEANUP_STRATEGY_REMOVAL_TIME_BASED = "removalTimeBased";
    public static final String HISTORY_REMOVAL_TIME_STRATEGY_START = "start";
    public static final String HISTORY_REMOVAL_TIME_STRATEGY_END = "end";
    public static final String HISTORY_REMOVAL_TIME_STRATEGY_NONE = "none";
    public static final String AUTHORIZATION_CHECK_REVOKE_ALWAYS = "always";
    public static final String AUTHORIZATION_CHECK_REVOKE_NEVER = "never";
    public static final String AUTHORIZATION_CHECK_REVOKE_AUTO = "auto";
    protected String processEngineName;
    protected int idBlockSize;
    protected String history;
    protected boolean jobExecutorActivate;
    protected boolean jobExecutorDeploymentAware;
    protected boolean jobExecutorPreferTimerJobs;
    protected boolean jobExecutorAcquireByDueDate;
    protected boolean jobExecutorAcquireByPriority;
    protected boolean ensureJobDueDateNotNull;
    protected boolean producePrioritizedJobs;
    protected boolean producePrioritizedExternalTasks;
    protected boolean hintJobExecutor;
    protected String mailServerHost;
    protected String mailServerUsername;
    protected String mailServerPassword;
    protected int mailServerPort;
    protected boolean useTLS;
    protected String mailServerDefaultFrom;
    protected String databaseType;
    protected String databaseVendor;
    protected String databaseVersion;
    protected String databaseSchemaUpdate;
    protected String jdbcDriver;
    protected String jdbcUrl;
    protected String jdbcUsername;
    protected String jdbcPassword;
    protected String dataSourceJndiName;
    protected int jdbcMaxActiveConnections;
    protected int jdbcMaxIdleConnections;
    protected int jdbcMaxCheckoutTime;
    protected int jdbcMaxWaitTime;
    protected boolean jdbcPingEnabled;
    protected String jdbcPingQuery;
    protected int jdbcPingConnectionNotUsedFor;
    protected DataSource dataSource;
    protected SchemaOperationsCommand schemaOperationsCommand;
    protected ProcessEngineBootstrapCommand bootstrapCommand;
    protected HistoryLevelSetupCommand historyLevelCommand;
    protected boolean transactionsExternallyManaged;
    protected Integer jdbcStatementTimeout;
    protected boolean jdbcBatchProcessing;
    protected String jpaPersistenceUnitName;
    protected Object jpaEntityManagerFactory;
    protected boolean jpaHandleTransaction;
    protected boolean jpaCloseEntityManager;
    protected int defaultNumberOfRetries;
    protected ClassLoader classLoader;
    protected boolean createIncidentOnFailedJobEnabled;
    protected boolean enablePasswordPolicy;
    protected PasswordPolicy passwordPolicy;
    protected boolean authorizationEnabled;
    protected String defaultUserPermissionNameForTask;
    protected boolean authorizationEnabledForCustomCode;
    protected boolean tenantCheckEnabled;
    protected ValueTypeResolver valueTypeResolver;
    protected String authorizationCheckRevokes;
    protected String generalResourceWhitelistPattern;
    protected String userResourceWhitelistPattern;
    protected String groupResourceWhitelistPattern;
    protected String tenantResourceWhitelistPattern;
    protected boolean enableExceptionsAfterUnhandledBpmnError;
    protected boolean skipHistoryOptimisticLockingExceptions;
    protected boolean enforceSpecificVariablePermission;
    protected List<String> disabledPermissions;
    protected boolean enableCmdExceptionLogging;
    protected boolean enableReducedJobExceptionLogging;
    protected String deserializationAllowedClasses;
    protected String deserializationAllowedPackages;
    protected DeserializationTypeValidator deserializationTypeValidator;
    protected boolean deserializationTypeValidationEnabled;
    protected String installationId;
    protected TelemetryRegistry telemetryRegistry;
    protected boolean skipOutputMappingOnCanceledActivities;
    
    protected ProcessEngineConfiguration() {
        this.processEngineName = "default";
        this.idBlockSize = 100;
        this.history = "audit";
        this.jobExecutorDeploymentAware = false;
        this.jobExecutorPreferTimerJobs = false;
        this.jobExecutorAcquireByDueDate = false;
        this.jobExecutorAcquireByPriority = false;
        this.ensureJobDueDateNotNull = false;
        this.producePrioritizedJobs = true;
        this.producePrioritizedExternalTasks = true;
        this.hintJobExecutor = true;
        this.mailServerHost = "localhost";
        this.mailServerPort = 25;
        this.useTLS = false;
        this.mailServerDefaultFrom = "camunda@localhost";
        this.databaseSchemaUpdate = "false";
        this.jdbcDriver = "org.h2.Driver";
        this.jdbcUrl = "jdbc:h2:tcp://localhost/activiti";
        this.jdbcUsername = "sa";
        this.jdbcPassword = "";
        this.dataSourceJndiName = null;
        this.jdbcPingEnabled = false;
        this.jdbcPingQuery = null;
        this.schemaOperationsCommand = new SchemaOperationsProcessEngineBuild();
        this.bootstrapCommand = new BootstrapEngineCommand();
        this.historyLevelCommand = new HistoryLevelSetupCommand();
        this.transactionsExternallyManaged = false;
        this.jdbcBatchProcessing = true;
        this.defaultNumberOfRetries = 3;
        this.createIncidentOnFailedJobEnabled = true;
        this.authorizationEnabled = false;
        this.defaultUserPermissionNameForTask = "UPDATE";
        this.authorizationEnabledForCustomCode = false;
        this.tenantCheckEnabled = true;
        this.authorizationCheckRevokes = "auto";
        this.generalResourceWhitelistPattern = "[a-zA-Z0-9]+|camunda-admin";
        this.enableExceptionsAfterUnhandledBpmnError = false;
        this.skipHistoryOptimisticLockingExceptions = true;
        this.enforceSpecificVariablePermission = false;
        this.disabledPermissions = Collections.emptyList();
        this.enableCmdExceptionLogging = true;
        this.enableReducedJobExceptionLogging = false;
        this.deserializationTypeValidationEnabled = false;
        this.skipOutputMappingOnCanceledActivities = false;
    }
    
    public abstract ProcessEngine buildProcessEngine();
    
    public static ProcessEngineConfiguration createProcessEngineConfigurationFromResourceDefault() {
        ProcessEngineConfiguration processEngineConfiguration = null;
        try {
            processEngineConfiguration = createProcessEngineConfigurationFromResource("camunda.cfg.xml", "processEngineConfiguration");
        }
        catch (RuntimeException ex) {
            processEngineConfiguration = createProcessEngineConfigurationFromResource("activiti.cfg.xml", "processEngineConfiguration");
        }
        return processEngineConfiguration;
    }
    
    public static ProcessEngineConfiguration createProcessEngineConfigurationFromResource(final String resource) {
        return createProcessEngineConfigurationFromResource(resource, "processEngineConfiguration");
    }
    
    public static ProcessEngineConfiguration createProcessEngineConfigurationFromResource(final String resource, final String beanName) {
        return BeansConfigurationHelper.parseProcessEngineConfigurationFromResource(resource, beanName);
    }
    
    public static ProcessEngineConfiguration createProcessEngineConfigurationFromInputStream(final InputStream inputStream) {
        return createProcessEngineConfigurationFromInputStream(inputStream, "processEngineConfiguration");
    }
    
    public static ProcessEngineConfiguration createProcessEngineConfigurationFromInputStream(final InputStream inputStream, final String beanName) {
        return BeansConfigurationHelper.parseProcessEngineConfigurationFromInputStream(inputStream, beanName);
    }
    
    public static ProcessEngineConfiguration createStandaloneProcessEngineConfiguration() {
        return new StandaloneProcessEngineConfiguration();
    }
    
    public static ProcessEngineConfiguration createStandaloneInMemProcessEngineConfiguration() {
        return new StandaloneInMemProcessEngineConfiguration();
    }
    
    public String getProcessEngineName() {
        return this.processEngineName;
    }
    
    public ProcessEngineConfiguration setProcessEngineName(final String processEngineName) {
        this.processEngineName = processEngineName;
        return this;
    }
    
    public int getIdBlockSize() {
        return this.idBlockSize;
    }
    
    public ProcessEngineConfiguration setIdBlockSize(final int idBlockSize) {
        this.idBlockSize = idBlockSize;
        return this;
    }
    
    public String getHistory() {
        return this.history;
    }
    
    public ProcessEngineConfiguration setHistory(final String history) {
        this.history = history;
        return this;
    }
    
    public String getMailServerHost() {
        return this.mailServerHost;
    }
    
    public ProcessEngineConfiguration setMailServerHost(final String mailServerHost) {
        this.mailServerHost = mailServerHost;
        return this;
    }
    
    public String getMailServerUsername() {
        return this.mailServerUsername;
    }
    
    public ProcessEngineConfiguration setMailServerUsername(final String mailServerUsername) {
        this.mailServerUsername = mailServerUsername;
        return this;
    }
    
    public String getMailServerPassword() {
        return this.mailServerPassword;
    }
    
    public ProcessEngineConfiguration setMailServerPassword(final String mailServerPassword) {
        this.mailServerPassword = mailServerPassword;
        return this;
    }
    
    public int getMailServerPort() {
        return this.mailServerPort;
    }
    
    public ProcessEngineConfiguration setMailServerPort(final int mailServerPort) {
        this.mailServerPort = mailServerPort;
        return this;
    }
    
    public boolean getMailServerUseTLS() {
        return this.useTLS;
    }
    
    public ProcessEngineConfiguration setMailServerUseTLS(final boolean useTLS) {
        this.useTLS = useTLS;
        return this;
    }
    
    public String getMailServerDefaultFrom() {
        return this.mailServerDefaultFrom;
    }
    
    public ProcessEngineConfiguration setMailServerDefaultFrom(final String mailServerDefaultFrom) {
        this.mailServerDefaultFrom = mailServerDefaultFrom;
        return this;
    }
    
    public String getDatabaseType() {
        return this.databaseType;
    }
    
    public ProcessEngineConfiguration setDatabaseType(final String databaseType) {
        this.databaseType = databaseType;
        return this;
    }
    
    public String getDatabaseVendor() {
        return this.databaseVendor;
    }
    
    public ProcessEngineConfiguration setDatabaseVendor(final String databaseVendor) {
        this.databaseVendor = databaseVendor;
        return this;
    }
    
    public String getDatabaseVersion() {
        return this.databaseVersion;
    }
    
    public ProcessEngineConfiguration setDatabaseVersion(final String databaseVersion) {
        this.databaseVersion = databaseVersion;
        return this;
    }
    
    public String getDatabaseSchemaUpdate() {
        return this.databaseSchemaUpdate;
    }
    
    public ProcessEngineConfiguration setDatabaseSchemaUpdate(final String databaseSchemaUpdate) {
        this.databaseSchemaUpdate = databaseSchemaUpdate;
        return this;
    }
    
    public DataSource getDataSource() {
        return this.dataSource;
    }
    
    public ProcessEngineConfiguration setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }
    
    public SchemaOperationsCommand getSchemaOperationsCommand() {
        return this.schemaOperationsCommand;
    }
    
    public void setSchemaOperationsCommand(final SchemaOperationsCommand schemaOperationsCommand) {
        this.schemaOperationsCommand = schemaOperationsCommand;
    }
    
    public ProcessEngineBootstrapCommand getProcessEngineBootstrapCommand() {
        return this.bootstrapCommand;
    }
    
    public void setProcessEngineBootstrapCommand(final ProcessEngineBootstrapCommand bootstrapCommand) {
        this.bootstrapCommand = bootstrapCommand;
    }
    
    public HistoryLevelSetupCommand getHistoryLevelCommand() {
        return this.historyLevelCommand;
    }
    
    public void setHistoryLevelCommand(final HistoryLevelSetupCommand historyLevelCommand) {
        this.historyLevelCommand = historyLevelCommand;
    }
    
    public String getJdbcDriver() {
        return this.jdbcDriver;
    }
    
    public ProcessEngineConfiguration setJdbcDriver(final String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
        return this;
    }
    
    public String getJdbcUrl() {
        return this.jdbcUrl;
    }
    
    public ProcessEngineConfiguration setJdbcUrl(final String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
        return this;
    }
    
    public String getJdbcUsername() {
        return this.jdbcUsername;
    }
    
    public ProcessEngineConfiguration setJdbcUsername(final String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
        return this;
    }
    
    public String getJdbcPassword() {
        return this.jdbcPassword;
    }
    
    public ProcessEngineConfiguration setJdbcPassword(final String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
        return this;
    }
    
    public boolean isTransactionsExternallyManaged() {
        return this.transactionsExternallyManaged;
    }
    
    public ProcessEngineConfiguration setTransactionsExternallyManaged(final boolean transactionsExternallyManaged) {
        this.transactionsExternallyManaged = transactionsExternallyManaged;
        return this;
    }
    
    public int getJdbcMaxActiveConnections() {
        return this.jdbcMaxActiveConnections;
    }
    
    public ProcessEngineConfiguration setJdbcMaxActiveConnections(final int jdbcMaxActiveConnections) {
        this.jdbcMaxActiveConnections = jdbcMaxActiveConnections;
        return this;
    }
    
    public int getJdbcMaxIdleConnections() {
        return this.jdbcMaxIdleConnections;
    }
    
    public ProcessEngineConfiguration setJdbcMaxIdleConnections(final int jdbcMaxIdleConnections) {
        this.jdbcMaxIdleConnections = jdbcMaxIdleConnections;
        return this;
    }
    
    public int getJdbcMaxCheckoutTime() {
        return this.jdbcMaxCheckoutTime;
    }
    
    public ProcessEngineConfiguration setJdbcMaxCheckoutTime(final int jdbcMaxCheckoutTime) {
        this.jdbcMaxCheckoutTime = jdbcMaxCheckoutTime;
        return this;
    }
    
    public int getJdbcMaxWaitTime() {
        return this.jdbcMaxWaitTime;
    }
    
    public ProcessEngineConfiguration setJdbcMaxWaitTime(final int jdbcMaxWaitTime) {
        this.jdbcMaxWaitTime = jdbcMaxWaitTime;
        return this;
    }
    
    public boolean isJdbcPingEnabled() {
        return this.jdbcPingEnabled;
    }
    
    public ProcessEngineConfiguration setJdbcPingEnabled(final boolean jdbcPingEnabled) {
        this.jdbcPingEnabled = jdbcPingEnabled;
        return this;
    }
    
    public String getJdbcPingQuery() {
        return this.jdbcPingQuery;
    }
    
    public ProcessEngineConfiguration setJdbcPingQuery(final String jdbcPingQuery) {
        this.jdbcPingQuery = jdbcPingQuery;
        return this;
    }
    
    public int getJdbcPingConnectionNotUsedFor() {
        return this.jdbcPingConnectionNotUsedFor;
    }
    
    public ProcessEngineConfiguration setJdbcPingConnectionNotUsedFor(final int jdbcPingNotUsedFor) {
        this.jdbcPingConnectionNotUsedFor = jdbcPingNotUsedFor;
        return this;
    }
    
    public Integer getJdbcStatementTimeout() {
        return this.jdbcStatementTimeout;
    }
    
    public ProcessEngineConfiguration setJdbcStatementTimeout(final Integer jdbcStatementTimeout) {
        this.jdbcStatementTimeout = jdbcStatementTimeout;
        return this;
    }
    
    public boolean isJdbcBatchProcessing() {
        return this.jdbcBatchProcessing;
    }
    
    public ProcessEngineConfiguration setJdbcBatchProcessing(final boolean jdbcBatchProcessing) {
        this.jdbcBatchProcessing = jdbcBatchProcessing;
        return this;
    }
    
    public boolean isJobExecutorActivate() {
        return this.jobExecutorActivate;
    }
    
    public ProcessEngineConfiguration setJobExecutorActivate(final boolean jobExecutorActivate) {
        this.jobExecutorActivate = jobExecutorActivate;
        return this;
    }
    
    public boolean isJobExecutorDeploymentAware() {
        return this.jobExecutorDeploymentAware;
    }
    
    public ProcessEngineConfiguration setJobExecutorDeploymentAware(final boolean jobExecutorDeploymentAware) {
        this.jobExecutorDeploymentAware = jobExecutorDeploymentAware;
        return this;
    }
    
    public boolean isJobExecutorAcquireByDueDate() {
        return this.jobExecutorAcquireByDueDate;
    }
    
    public ProcessEngineConfiguration setJobExecutorAcquireByDueDate(final boolean jobExecutorAcquireByDueDate) {
        this.jobExecutorAcquireByDueDate = jobExecutorAcquireByDueDate;
        return this;
    }
    
    public boolean isJobExecutorPreferTimerJobs() {
        return this.jobExecutorPreferTimerJobs;
    }
    
    public ProcessEngineConfiguration setJobExecutorPreferTimerJobs(final boolean jobExecutorPreferTimerJobs) {
        this.jobExecutorPreferTimerJobs = jobExecutorPreferTimerJobs;
        return this;
    }
    
    public boolean isHintJobExecutor() {
        return this.hintJobExecutor;
    }
    
    public ProcessEngineConfiguration setHintJobExecutor(final boolean hintJobExecutor) {
        this.hintJobExecutor = hintJobExecutor;
        return this;
    }
    
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }
    
    public ProcessEngineConfiguration setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }
    
    public Object getJpaEntityManagerFactory() {
        return this.jpaEntityManagerFactory;
    }
    
    public ProcessEngineConfiguration setJpaEntityManagerFactory(final Object jpaEntityManagerFactory) {
        this.jpaEntityManagerFactory = jpaEntityManagerFactory;
        return this;
    }
    
    public boolean isJpaHandleTransaction() {
        return this.jpaHandleTransaction;
    }
    
    public ProcessEngineConfiguration setJpaHandleTransaction(final boolean jpaHandleTransaction) {
        this.jpaHandleTransaction = jpaHandleTransaction;
        return this;
    }
    
    public boolean isJpaCloseEntityManager() {
        return this.jpaCloseEntityManager;
    }
    
    public ProcessEngineConfiguration setJpaCloseEntityManager(final boolean jpaCloseEntityManager) {
        this.jpaCloseEntityManager = jpaCloseEntityManager;
        return this;
    }
    
    public String getJpaPersistenceUnitName() {
        return this.jpaPersistenceUnitName;
    }
    
    public void setJpaPersistenceUnitName(final String jpaPersistenceUnitName) {
        this.jpaPersistenceUnitName = jpaPersistenceUnitName;
    }
    
    public String getDataSourceJndiName() {
        return this.dataSourceJndiName;
    }
    
    public void setDataSourceJndiName(final String dataSourceJndiName) {
        this.dataSourceJndiName = dataSourceJndiName;
    }
    
    public boolean isCreateIncidentOnFailedJobEnabled() {
        return this.createIncidentOnFailedJobEnabled;
    }
    
    public ProcessEngineConfiguration setCreateIncidentOnFailedJobEnabled(final boolean createIncidentOnFailedJobEnabled) {
        this.createIncidentOnFailedJobEnabled = createIncidentOnFailedJobEnabled;
        return this;
    }
    
    public boolean isAuthorizationEnabled() {
        return this.authorizationEnabled;
    }
    
    public ProcessEngineConfiguration setAuthorizationEnabled(final boolean isAuthorizationChecksEnabled) {
        this.authorizationEnabled = isAuthorizationChecksEnabled;
        return this;
    }
    
    public String getDefaultUserPermissionNameForTask() {
        return this.defaultUserPermissionNameForTask;
    }
    
    public ProcessEngineConfiguration setDefaultUserPermissionNameForTask(final String defaultUserPermissionNameForTask) {
        this.defaultUserPermissionNameForTask = defaultUserPermissionNameForTask;
        return this;
    }
    
    public boolean isAuthorizationEnabledForCustomCode() {
        return this.authorizationEnabledForCustomCode;
    }
    
    public ProcessEngineConfiguration setAuthorizationEnabledForCustomCode(final boolean authorizationEnabledForCustomCode) {
        this.authorizationEnabledForCustomCode = authorizationEnabledForCustomCode;
        return this;
    }
    
    public boolean isTenantCheckEnabled() {
        return this.tenantCheckEnabled;
    }
    
    public ProcessEngineConfiguration setTenantCheckEnabled(final boolean isTenantCheckEnabled) {
        this.tenantCheckEnabled = isTenantCheckEnabled;
        return this;
    }
    
    public String getGeneralResourceWhitelistPattern() {
        return this.generalResourceWhitelistPattern;
    }
    
    public void setGeneralResourceWhitelistPattern(final String generalResourceWhitelistPattern) {
        this.generalResourceWhitelistPattern = generalResourceWhitelistPattern;
    }
    
    public String getUserResourceWhitelistPattern() {
        return this.userResourceWhitelistPattern;
    }
    
    public void setUserResourceWhitelistPattern(final String userResourceWhitelistPattern) {
        this.userResourceWhitelistPattern = userResourceWhitelistPattern;
    }
    
    public String getGroupResourceWhitelistPattern() {
        return this.groupResourceWhitelistPattern;
    }
    
    public void setGroupResourceWhitelistPattern(final String groupResourceWhitelistPattern) {
        this.groupResourceWhitelistPattern = groupResourceWhitelistPattern;
    }
    
    public String getTenantResourceWhitelistPattern() {
        return this.tenantResourceWhitelistPattern;
    }
    
    public void setTenantResourceWhitelistPattern(final String tenantResourceWhitelistPattern) {
        this.tenantResourceWhitelistPattern = tenantResourceWhitelistPattern;
    }
    
    public int getDefaultNumberOfRetries() {
        return this.defaultNumberOfRetries;
    }
    
    public void setDefaultNumberOfRetries(final int defaultNumberOfRetries) {
        this.defaultNumberOfRetries = defaultNumberOfRetries;
    }
    
    public ValueTypeResolver getValueTypeResolver() {
        return this.valueTypeResolver;
    }
    
    public ProcessEngineConfiguration setValueTypeResolver(final ValueTypeResolver valueTypeResolver) {
        this.valueTypeResolver = valueTypeResolver;
        return this;
    }
    
    public boolean isEnsureJobDueDateNotNull() {
        return this.ensureJobDueDateNotNull;
    }
    
    public void setEnsureJobDueDateNotNull(final boolean ensureJobDueDateNotNull) {
        this.ensureJobDueDateNotNull = ensureJobDueDateNotNull;
    }
    
    public boolean isProducePrioritizedJobs() {
        return this.producePrioritizedJobs;
    }
    
    public void setProducePrioritizedJobs(final boolean producePrioritizedJobs) {
        this.producePrioritizedJobs = producePrioritizedJobs;
    }
    
    public boolean isJobExecutorAcquireByPriority() {
        return this.jobExecutorAcquireByPriority;
    }
    
    public void setJobExecutorAcquireByPriority(final boolean jobExecutorAcquireByPriority) {
        this.jobExecutorAcquireByPriority = jobExecutorAcquireByPriority;
    }
    
    public boolean isProducePrioritizedExternalTasks() {
        return this.producePrioritizedExternalTasks;
    }
    
    public void setProducePrioritizedExternalTasks(final boolean producePrioritizedExternalTasks) {
        this.producePrioritizedExternalTasks = producePrioritizedExternalTasks;
    }
    
    public void setAuthorizationCheckRevokes(final String authorizationCheckRevokes) {
        this.authorizationCheckRevokes = authorizationCheckRevokes;
    }
    
    public String getAuthorizationCheckRevokes() {
        return this.authorizationCheckRevokes;
    }
    
    public boolean isEnableExceptionsAfterUnhandledBpmnError() {
        return this.enableExceptionsAfterUnhandledBpmnError;
    }
    
    public void setEnableExceptionsAfterUnhandledBpmnError(final boolean enableExceptionsAfterUnhandledBpmnError) {
        this.enableExceptionsAfterUnhandledBpmnError = enableExceptionsAfterUnhandledBpmnError;
    }
    
    public boolean isSkipHistoryOptimisticLockingExceptions() {
        return this.skipHistoryOptimisticLockingExceptions;
    }
    
    public ProcessEngineConfiguration setSkipHistoryOptimisticLockingExceptions(final boolean skipHistoryOptimisticLockingExceptions) {
        this.skipHistoryOptimisticLockingExceptions = skipHistoryOptimisticLockingExceptions;
        return this;
    }
    
    public boolean isEnforceSpecificVariablePermission() {
        return this.enforceSpecificVariablePermission;
    }
    
    public void setEnforceSpecificVariablePermission(final boolean ensureSpecificVariablePermission) {
        this.enforceSpecificVariablePermission = ensureSpecificVariablePermission;
    }
    
    public List<String> getDisabledPermissions() {
        return this.disabledPermissions;
    }
    
    public void setDisabledPermissions(final List<String> disabledPermissions) {
        this.disabledPermissions = disabledPermissions;
    }
    
    public boolean isEnablePasswordPolicy() {
        return this.enablePasswordPolicy;
    }
    
    public ProcessEngineConfiguration setEnablePasswordPolicy(final boolean enablePasswordPolicy) {
        this.enablePasswordPolicy = enablePasswordPolicy;
        return this;
    }
    
    public PasswordPolicy getPasswordPolicy() {
        return this.passwordPolicy;
    }
    
    public ProcessEngineConfiguration setPasswordPolicy(final PasswordPolicy passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
        return this;
    }
    
    public boolean isEnableCmdExceptionLogging() {
        return this.enableCmdExceptionLogging;
    }
    
    public ProcessEngineConfiguration setEnableCmdExceptionLogging(final boolean enableCmdExceptionLogging) {
        this.enableCmdExceptionLogging = enableCmdExceptionLogging;
        return this;
    }
    
    public boolean isEnableReducedJobExceptionLogging() {
        return this.enableReducedJobExceptionLogging;
    }
    
    public ProcessEngineConfiguration setEnableReducedJobExceptionLogging(final boolean enableReducedJobExceptionLogging) {
        this.enableReducedJobExceptionLogging = enableReducedJobExceptionLogging;
        return this;
    }
    
    public String getDeserializationAllowedClasses() {
        return this.deserializationAllowedClasses;
    }
    
    public ProcessEngineConfiguration setDeserializationAllowedClasses(final String deserializationAllowedClasses) {
        this.deserializationAllowedClasses = deserializationAllowedClasses;
        return this;
    }
    
    public String getDeserializationAllowedPackages() {
        return this.deserializationAllowedPackages;
    }
    
    public ProcessEngineConfiguration setDeserializationAllowedPackages(final String deserializationAllowedPackages) {
        this.deserializationAllowedPackages = deserializationAllowedPackages;
        return this;
    }
    
    public DeserializationTypeValidator getDeserializationTypeValidator() {
        return this.deserializationTypeValidator;
    }
    
    public ProcessEngineConfiguration setDeserializationTypeValidator(final DeserializationTypeValidator deserializationTypeValidator) {
        this.deserializationTypeValidator = deserializationTypeValidator;
        return this;
    }
    
    public boolean isDeserializationTypeValidationEnabled() {
        return this.deserializationTypeValidationEnabled;
    }
    
    public ProcessEngineConfiguration setDeserializationTypeValidationEnabled(final boolean deserializationTypeValidationEnabled) {
        this.deserializationTypeValidationEnabled = deserializationTypeValidationEnabled;
        return this;
    }
    
    public String getInstallationId() {
        return this.installationId;
    }
    
    public ProcessEngineConfiguration setInstallationId(final String installationId) {
        this.installationId = installationId;
        return this;
    }
    
    public TelemetryRegistry getTelemetryRegistry() {
        return this.telemetryRegistry;
    }
    
    public ProcessEngineConfiguration setTelemetryRegistry(final TelemetryRegistry telemetryRegistry) {
        this.telemetryRegistry = telemetryRegistry;
        return this;
    }
    
    public boolean isSkipOutputMappingOnCanceledActivities() {
        return this.skipOutputMappingOnCanceledActivities;
    }
    
    public void setSkipOutputMappingOnCanceledActivities(final boolean skipOutputMappingOnCanceledActivities) {
        this.skipOutputMappingOnCanceledActivities = skipOutputMappingOnCanceledActivities;
    }
}
