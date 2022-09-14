// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cfg;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.spi.el.DmnScriptEngineResolver;
import org.camunda.bpm.dmn.feel.impl.scala.function.FeelCustomFunctionProvider;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.type.ValueTypeResolver;
import org.camunda.connect.Connectors;
import org.camunda.connect.spi.Connector;
import org.camunda.connect.spi.ConnectorRequest;
import org.zik.bpm.engine.*;
import org.zik.bpm.engine.authorization.Permission;
import org.zik.bpm.engine.authorization.Permissions;
import org.zik.bpm.engine.impl.*;
import org.zik.bpm.engine.impl.application.ProcessApplicationManager;
import org.zik.bpm.engine.impl.batch.BatchJobHandler;
import org.zik.bpm.engine.impl.batch.BatchMonitorJobHandler;
import org.zik.bpm.engine.impl.batch.BatchSeedJobHandler;
import org.zik.bpm.engine.impl.batch.deletion.DeleteHistoricProcessInstancesJobHandler;
import org.zik.bpm.engine.impl.batch.deletion.DeleteProcessInstancesJobHandler;
import org.zik.bpm.engine.impl.batch.externaltask.SetExternalTaskRetriesJobHandler;
import org.zik.bpm.engine.impl.batch.job.SetJobRetriesJobHandler;
import org.zik.bpm.engine.impl.batch.message.MessageCorrelationBatchJobHandler;
import org.zik.bpm.engine.impl.batch.removaltime.BatchSetRemovalTimeJobHandler;
import org.zik.bpm.engine.impl.batch.removaltime.DecisionSetRemovalTimeJobHandler;
import org.zik.bpm.engine.impl.batch.removaltime.ProcessSetRemovalTimeJobHandler;
import org.zik.bpm.engine.impl.batch.update.UpdateProcessInstancesSuspendStateJobHandler;
import org.zik.bpm.engine.impl.batch.variables.BatchSetVariablesHandler;
import org.zik.bpm.engine.impl.bpmn.behavior.ExternalTaskActivityBehavior;
import org.zik.bpm.engine.impl.bpmn.deployer.BpmnDeployer;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParser;
import org.zik.bpm.engine.impl.bpmn.parser.DefaultFailedJobParseListener;
import org.zik.bpm.engine.impl.calendar.*;
import org.zik.bpm.engine.impl.cfg.auth.*;
import org.zik.bpm.engine.impl.cfg.multitenancy.TenantCommandChecker;
import org.zik.bpm.engine.impl.cfg.multitenancy.TenantIdProvider;
import org.zik.bpm.engine.impl.cfg.standalone.StandaloneTransactionContextFactory;
import org.zik.bpm.engine.impl.cmmn.CaseServiceImpl;
import org.zik.bpm.engine.impl.cmmn.deployer.CmmnDeployer;
import org.zik.bpm.engine.impl.cmmn.entity.repository.CaseDefinitionManager;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseExecutionManager;
import org.zik.bpm.engine.impl.cmmn.entity.runtime.CaseSentryPartManager;
import org.zik.bpm.engine.impl.cmmn.handler.DefaultCmmnElementHandlerRegistry;
import org.zik.bpm.engine.impl.cmmn.transformer.CmmnTransformFactory;
import org.zik.bpm.engine.impl.cmmn.transformer.CmmnTransformListener;
import org.zik.bpm.engine.impl.cmmn.transformer.CmmnTransformer;
import org.zik.bpm.engine.impl.cmmn.transformer.DefaultCmmnTransformFactory;
import org.zik.bpm.engine.impl.db.DbIdGenerator;
import org.zik.bpm.engine.impl.db.entitymanager.DbEntityManagerFactory;
import org.zik.bpm.engine.impl.db.entitymanager.cache.DbEntityCacheKeyMapping;
import org.zik.bpm.engine.impl.db.sql.DbSqlPersistenceProviderFactory;
import org.zik.bpm.engine.impl.db.sql.DbSqlSessionFactory;
import org.zik.bpm.engine.impl.delegate.DefaultDelegateInterceptor;
import org.zik.bpm.engine.impl.digest.*;
import org.zik.bpm.engine.impl.dmn.batch.DeleteHistoricDecisionInstancesJobHandler;
import org.zik.bpm.engine.impl.dmn.configuration.DmnEngineConfigurationBuilder;
import org.zik.bpm.engine.impl.dmn.deployer.DecisionDefinitionDeployer;
import org.zik.bpm.engine.impl.dmn.deployer.DecisionRequirementsDefinitionDeployer;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionDefinitionManager;
import org.zik.bpm.engine.impl.dmn.entity.repository.DecisionRequirementsDefinitionManager;
import org.zik.bpm.engine.impl.el.CommandContextFunctionMapper;
import org.zik.bpm.engine.impl.el.DateTimeFunctionMapper;
import org.zik.bpm.engine.impl.el.ExpressionManager;
import org.zik.bpm.engine.impl.event.*;
import org.zik.bpm.engine.impl.externaltask.DefaultExternalTaskPriorityProvider;
import org.zik.bpm.engine.impl.form.deployer.CamundaFormDefinitionDeployer;
import org.zik.bpm.engine.impl.form.engine.FormEngine;
import org.zik.bpm.engine.impl.form.engine.HtmlFormEngine;
import org.zik.bpm.engine.impl.form.engine.JuelFormEngine;
import org.zik.bpm.engine.impl.form.entity.CamundaFormDefinitionManager;
import org.zik.bpm.engine.impl.form.type.*;
import org.zik.bpm.engine.impl.form.validator.*;
import org.zik.bpm.engine.impl.history.DefaultHistoryRemovalTimeProvider;
import org.zik.bpm.engine.impl.history.HistoryLevel;
import org.zik.bpm.engine.impl.history.HistoryRemovalTimeProvider;
import org.zik.bpm.engine.impl.history.event.HistoricDecisionInstanceManager;
import org.zik.bpm.engine.impl.history.event.HostnameProvider;
import org.zik.bpm.engine.impl.history.event.SimpleIpBasedProvider;
import org.zik.bpm.engine.impl.history.handler.CompositeDbHistoryEventHandler;
import org.zik.bpm.engine.impl.history.handler.CompositeHistoryEventHandler;
import org.zik.bpm.engine.impl.history.handler.HistoryEventHandler;
import org.zik.bpm.engine.impl.history.parser.HistoryParseListener;
import org.zik.bpm.engine.impl.history.producer.*;
import org.zik.bpm.engine.impl.history.transformer.CmmnHistoryTransformListener;
import org.zik.bpm.engine.impl.identity.DefaultPasswordPolicyImpl;
import org.zik.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.zik.bpm.engine.impl.identity.WritableIdentityProvider;
import org.zik.bpm.engine.impl.identity.db.DbIdentityServiceProvider;
import org.zik.bpm.engine.impl.incident.CompositeIncidentHandler;
import org.zik.bpm.engine.impl.incident.DefaultIncidentHandler;
import org.zik.bpm.engine.impl.incident.IncidentHandler;
import org.zik.bpm.engine.impl.interceptor.*;
import org.zik.bpm.engine.impl.jobexecutor.*;
import org.zik.bpm.engine.impl.jobexecutor.historycleanup.BatchWindowManager;
import org.zik.bpm.engine.impl.jobexecutor.historycleanup.DefaultBatchWindowManager;
import org.zik.bpm.engine.impl.jobexecutor.historycleanup.HistoryCleanupHelper;
import org.zik.bpm.engine.impl.jobexecutor.historycleanup.HistoryCleanupJobHandler;
import org.zik.bpm.engine.impl.metrics.MetricsRegistry;
import org.zik.bpm.engine.impl.metrics.MetricsReporterIdProvider;
import org.zik.bpm.engine.impl.metrics.parser.MetricsBpmnParseListener;
import org.zik.bpm.engine.impl.metrics.parser.MetricsCmmnTransformListener;
import org.zik.bpm.engine.impl.metrics.reporter.DbMetricsReporter;
import org.zik.bpm.engine.impl.migration.DefaultMigrationActivityMatcher;
import org.zik.bpm.engine.impl.migration.DefaultMigrationInstructionGenerator;
import org.zik.bpm.engine.impl.migration.MigrationActivityMatcher;
import org.zik.bpm.engine.impl.migration.MigrationInstructionGenerator;
import org.zik.bpm.engine.impl.migration.batch.MigrationBatchJobHandler;
import org.zik.bpm.engine.impl.migration.validation.activity.MigrationActivityValidator;
import org.zik.bpm.engine.impl.migration.validation.activity.NoCompensationHandlerActivityValidator;
import org.zik.bpm.engine.impl.migration.validation.activity.SupportedActivityValidator;
import org.zik.bpm.engine.impl.migration.validation.activity.SupportedPassiveEventTriggerActivityValidator;
import org.zik.bpm.engine.impl.migration.validation.instance.*;
import org.zik.bpm.engine.impl.migration.validation.instruction.*;
import org.zik.bpm.engine.impl.optimize.OptimizeManager;
import org.zik.bpm.engine.impl.persistence.GenericManagerFactory;
import org.zik.bpm.engine.impl.persistence.deploy.Deployer;
import org.zik.bpm.engine.impl.persistence.deploy.cache.CacheFactory;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DefaultCacheFactory;
import org.zik.bpm.engine.impl.persistence.deploy.cache.DeploymentCache;
import org.zik.bpm.engine.impl.persistence.entity.*;
import org.zik.bpm.engine.impl.repository.DefaultDeploymentHandlerFactory;
import org.zik.bpm.engine.impl.runtime.*;
import org.zik.bpm.engine.impl.scripting.ScriptFactory;
import org.zik.bpm.engine.impl.scripting.engine.*;
import org.zik.bpm.engine.impl.scripting.env.ScriptEnvResolver;
import org.zik.bpm.engine.impl.scripting.env.ScriptingEnvironment;
import org.zik.bpm.engine.impl.telemetry.TelemetryRegistry;
import org.zik.bpm.engine.impl.telemetry.dto.*;
import org.zik.bpm.engine.impl.telemetry.reporter.TelemetryReporter;
import org.zik.bpm.engine.impl.util.*;
import org.zik.bpm.engine.impl.variable.ValueTypeResolverImpl;
import org.zik.bpm.engine.impl.variable.serializer.*;
import org.zik.bpm.engine.impl.variable.serializer.jpa.EntityManagerSession;
import org.zik.bpm.engine.impl.variable.serializer.jpa.EntityManagerSessionFactory;
import org.zik.bpm.engine.impl.variable.serializer.jpa.JPAVariableSerializer;
import org.zik.bpm.engine.repository.DeploymentHandlerFactory;
import org.zik.bpm.engine.runtime.WhitelistingDeserializationTypeValidator;
import org.zik.bpm.engine.test.mock.MocksResolverFactory;

import javax.naming.InitialContext;
import javax.script.ScriptEngineManager;
import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.sql.*;
import java.text.ParseException;
import java.util.Date;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class ProcessEngineConfigurationImpl extends ProcessEngineConfiguration {
    protected static final ConfigurationLogger LOG;
    public static final String DB_SCHEMA_UPDATE_CREATE = "create";
    public static final String DB_SCHEMA_UPDATE_DROP_CREATE = "drop-create";
    public static final int HISTORYLEVEL_NONE;
    public static final int HISTORYLEVEL_ACTIVITY;
    public static final int HISTORYLEVEL_AUDIT;
    public static final int HISTORYLEVEL_FULL;
    public static final String DEFAULT_WS_SYNC_FACTORY = "org.camunda.bpm.engine.impl.webservice.CxfWebServiceClientFactory";
    public static final String DEFAULT_MYBATIS_MAPPING_FILE = "org/camunda/bpm/engine/impl/mapping/mappings.xml";
    public static final int DEFAULT_FAILED_JOB_LISTENER_MAX_RETRIES = 3;
    public static final int DEFAULT_INVOCATIONS_PER_BATCH_JOB = 1;
    protected static final Map<Object, Object> DEFAULT_BEANS_MAP;
    protected static final String PRODUCT_NAME = "Camunda BPM Runtime";
    public static SqlSessionFactory cachedSqlSessionFactory;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected TaskService taskService;
    protected FormService formService;
    protected ManagementService managementService;
    protected AuthorizationService authorizationService;
    protected CaseService caseService;
    protected FilterService filterService;
    protected ExternalTaskService externalTaskService;
    protected DecisionService decisionService;
    protected OptimizeService optimizeService;
    protected List<CommandInterceptor> customPreCommandInterceptorsTxRequired;
    protected List<CommandInterceptor> customPostCommandInterceptorsTxRequired;
    protected List<CommandInterceptor> commandInterceptorsTxRequired;
    protected CommandExecutor commandExecutorTxRequired;
    protected List<CommandInterceptor> customPreCommandInterceptorsTxRequiresNew;
    protected List<CommandInterceptor> customPostCommandInterceptorsTxRequiresNew;
    protected List<CommandInterceptor> commandInterceptorsTxRequiresNew;
    protected CommandExecutor commandExecutorTxRequiresNew;
    protected CommandExecutor commandExecutorSchemaOperations;
    protected int commandRetries;
    protected List<SessionFactory> customSessionFactories;
    protected DbSqlSessionFactory dbSqlSessionFactory;
    protected Map<Class<?>, SessionFactory> sessionFactories;
    protected List<Deployer> customPreDeployers;
    protected List<Deployer> customPostDeployers;
    protected List<Deployer> deployers;
    protected DeploymentCache deploymentCache;
    protected CacheFactory cacheFactory;
    protected int cacheCapacity;
    protected boolean enableFetchProcessDefinitionDescription;
    protected List<JobHandler> customJobHandlers;
    protected Map<String, JobHandler> jobHandlers;
    protected JobExecutor jobExecutor;
    protected PriorityProvider<JobDeclaration<?, ?>> jobPriorityProvider;
    protected Long jobExecutorPriorityRangeMin;
    protected Long jobExecutorPriorityRangeMax;
    protected PriorityProvider<ExternalTaskActivityBehavior> externalTaskPriorityProvider;
    protected SqlSessionFactory sqlSessionFactory;
    protected TransactionFactory transactionFactory;
    protected IdGenerator idGenerator;
    protected DataSource idGeneratorDataSource;
    protected String idGeneratorDataSourceJndiName;
    protected Map<String, IncidentHandler> incidentHandlers;
    protected List<IncidentHandler> customIncidentHandlers;
    protected Map<String, BatchJobHandler<?>> batchHandlers;
    protected List<BatchJobHandler<?>> customBatchJobHandlers;
    protected int batchJobsPerSeed;
    protected int invocationsPerBatchJob;
    protected Map<String, Integer> invocationsPerBatchJobByBatchType;
    protected int batchPollTime;
    protected long batchJobPriority;
    protected List<FormEngine> customFormEngines;
    protected Map<String, FormEngine> formEngines;
    protected List<AbstractFormFieldType> customFormTypes;
    protected FormTypes formTypes;
    protected FormValidators formValidators;
    protected Map<String, Class<? extends FormFieldValidator>> customFormFieldValidators;
    protected boolean disableStrictCamundaFormParsing;
    protected List<TypedValueSerializer> customPreVariableSerializers;
    protected List<TypedValueSerializer> customPostVariableSerializers;
    protected VariableSerializers variableSerializers;
    protected VariableSerializerFactory fallbackSerializerFactory;
    protected String defaultSerializationFormat;
    protected boolean javaSerializationFormatEnabled;
    protected String defaultCharsetName;
    protected Charset defaultCharset;
    protected ExpressionManager expressionManager;
    protected ScriptingEngines scriptingEngines;
    protected List<ResolverFactory> resolverFactories;
    protected ScriptingEnvironment scriptingEnvironment;
    protected List<ScriptEnvResolver> scriptEnvResolvers;
    protected ScriptFactory scriptFactory;
    protected ScriptEngineResolver scriptEngineResolver;
    protected String scriptEngineNameJavaScript;
    protected boolean autoStoreScriptVariables;
    protected boolean enableScriptCompilation;
    protected boolean enableScriptEngineCaching;
    protected boolean enableFetchScriptEngineFromProcessApplication;
    protected boolean enableScriptEngineLoadExternalResources;
    protected boolean enableScriptEngineNashornCompatibility;
    protected boolean configureScriptEngineHostAccess;
    protected boolean cmmnEnabled;
    protected boolean dmnEnabled;
    protected boolean standaloneTasksEnabled;
    protected boolean enableGracefulDegradationOnContextSwitchFailure;
    protected BusinessCalendarManager businessCalendarManager;
    protected String wsSyncFactoryClassName;
    protected CommandContextFactory commandContextFactory;
    protected TransactionContextFactory transactionContextFactory;
    protected BpmnParseFactory bpmnParseFactory;
    protected CmmnTransformFactory cmmnTransformFactory;
    protected DefaultCmmnElementHandlerRegistry cmmnElementHandlerRegistry;
    protected DefaultDmnEngineConfiguration dmnEngineConfiguration;
    protected DmnEngine dmnEngine;
    protected List<FeelCustomFunctionProvider> dmnFeelCustomFunctionProviders;
    protected boolean dmnFeelEnableLegacyBehavior;
    protected HistoryLevel historyLevel;
    protected List<HistoryLevel> historyLevels;
    protected List<HistoryLevel> customHistoryLevels;
    protected List<BpmnParseListener> preParseListeners;
    protected List<BpmnParseListener> postParseListeners;
    protected List<CmmnTransformListener> customPreCmmnTransformListeners;
    protected List<CmmnTransformListener> customPostCmmnTransformListeners;
    protected Map<Object, Object> beans;
    protected boolean isDbIdentityUsed;
    protected boolean isDbHistoryUsed;
    protected DelegateInterceptor delegateInterceptor;
    protected CommandInterceptor actualCommandExecutor;
    protected RejectedJobsHandler customRejectedJobsHandler;
    protected Map<String, EventHandler> eventHandlers;
    protected List<EventHandler> customEventHandlers;
    protected FailedJobCommandFactory failedJobCommandFactory;
    protected String databaseTablePrefix;
    protected String databaseSchema;
    protected boolean isCreateDiagramOnDeploy;
    protected ProcessApplicationManager processApplicationManager;
    protected CorrelationHandler correlationHandler;
    protected ConditionHandler conditionHandler;
    protected SessionFactory identityProviderSessionFactory;
    protected PasswordEncryptor passwordEncryptor;
    protected List<PasswordEncryptor> customPasswordChecker;
    protected PasswordManager passwordManager;
    protected SaltGenerator saltGenerator;
    protected Set<String> registeredDeployments;
    protected DeploymentHandlerFactory deploymentHandlerFactory;
    protected ResourceAuthorizationProvider resourceAuthorizationProvider;
    protected List<ProcessEnginePlugin> processEnginePlugins;
    protected HistoryEventProducer historyEventProducer;
    protected CmmnHistoryEventProducer cmmnHistoryEventProducer;
    protected DmnHistoryEventProducer dmnHistoryEventProducer;
    protected HistoryEventHandler historyEventHandler;
    protected List<HistoryEventHandler> customHistoryEventHandlers;
    protected boolean enableDefaultDbHistoryEventHandler;
    protected PermissionProvider permissionProvider;
    protected boolean isExecutionTreePrefetchEnabled;
    protected boolean isCompositeIncidentHandlersEnabled;
    protected boolean isDeploymentLockUsed;
    protected boolean isDeploymentSynchronized;
    protected boolean isDbEntityCacheReuseEnabled;
    protected boolean isInvokeCustomVariableListeners;
    protected ProcessEngineImpl processEngine;
    protected ArtifactFactory artifactFactory;
    protected DbEntityCacheKeyMapping dbEntityCacheKeyMapping;
    protected MetricsRegistry metricsRegistry;
    protected DbMetricsReporter dbMetricsReporter;
    protected boolean isMetricsEnabled;
    protected boolean isDbMetricsReporterActivate;
    protected MetricsReporterIdProvider metricsReporterIdProvider;
    protected boolean isTaskMetricsEnabled;
    protected String hostname;
    protected HostnameProvider hostnameProvider;
    protected boolean enableExpressionsInAdhocQueries;
    protected boolean enableExpressionsInStoredQueries;
    protected boolean enableXxeProcessing;
    protected boolean restrictUserOperationLogToAuthenticatedUsers;
    protected boolean disableStrictCallActivityValidation;
    protected boolean isBpmnStacktraceVerbose;
    protected boolean forceCloseMybatisConnectionPool;
    protected TenantIdProvider tenantIdProvider;
    protected List<CommandChecker> commandCheckers;
    protected List<String> adminGroups;
    protected List<String> adminUsers;
    protected MigrationActivityMatcher migrationActivityMatcher;
    protected List<MigrationActivityValidator> customPreMigrationActivityValidators;
    protected List<MigrationActivityValidator> customPostMigrationActivityValidators;
    protected MigrationInstructionGenerator migrationInstructionGenerator;
    protected List<MigrationInstructionValidator> customPreMigrationInstructionValidators;
    protected List<MigrationInstructionValidator> customPostMigrationInstructionValidators;
    protected List<MigrationInstructionValidator> migrationInstructionValidators;
    protected List<MigratingActivityInstanceValidator> customPreMigratingActivityInstanceValidators;
    protected List<MigratingActivityInstanceValidator> customPostMigratingActivityInstanceValidators;
    protected List<MigratingActivityInstanceValidator> migratingActivityInstanceValidators;
    protected List<MigratingTransitionInstanceValidator> migratingTransitionInstanceValidators;
    protected List<MigratingCompensationInstanceValidator> migratingCompensationInstanceValidators;
    protected Permission defaultUserPermissionForTask;
    protected boolean enableHistoricInstancePermissions;
    protected boolean isUseSharedSqlSessionFactory;
    protected String historyCleanupBatchWindowStartTime;
    protected String historyCleanupBatchWindowEndTime;
    protected Date historyCleanupBatchWindowStartTimeAsDate;
    protected Date historyCleanupBatchWindowEndTimeAsDate;
    protected Map<Integer, BatchWindowConfiguration> historyCleanupBatchWindows;
    protected String mondayHistoryCleanupBatchWindowStartTime;
    protected String mondayHistoryCleanupBatchWindowEndTime;
    protected String tuesdayHistoryCleanupBatchWindowStartTime;
    protected String tuesdayHistoryCleanupBatchWindowEndTime;
    protected String wednesdayHistoryCleanupBatchWindowStartTime;
    protected String wednesdayHistoryCleanupBatchWindowEndTime;
    protected String thursdayHistoryCleanupBatchWindowStartTime;
    protected String thursdayHistoryCleanupBatchWindowEndTime;
    protected String fridayHistoryCleanupBatchWindowStartTime;
    protected String fridayHistoryCleanupBatchWindowEndTime;
    protected String saturdayHistoryCleanupBatchWindowStartTime;
    protected String saturdayHistoryCleanupBatchWindowEndTime;
    protected String sundayHistoryCleanupBatchWindowStartTime;
    protected String sundayHistoryCleanupBatchWindowEndTime;
    protected int historyCleanupDegreeOfParallelism;
    protected String historyTimeToLive;
    protected String batchOperationHistoryTimeToLive;
    protected Map<String, String> batchOperationsForHistoryCleanup;
    protected Map<String, Integer> parsedBatchOperationsForHistoryCleanup;
    protected long historyCleanupJobPriority;
    protected String historyCleanupJobLogTimeToLive;
    protected String taskMetricsTimeToLive;
    protected Integer parsedTaskMetricsTimeToLive;
    protected BatchWindowManager batchWindowManager;
    protected HistoryRemovalTimeProvider historyRemovalTimeProvider;
    protected String historyRemovalTimeStrategy;
    protected String historyCleanupStrategy;
    private int historyCleanupBatchSize;
    private int historyCleanupBatchThreshold;
    private boolean historyCleanupMetricsEnabled;
    protected boolean historyCleanupEnabled;
    private int failedJobListenerMaxRetries;
    protected String failedJobRetryTimeCycle;
    protected int loginMaxAttempts;
    protected int loginDelayFactor;
    protected int loginDelayMaxTime;
    protected int loginDelayBase;
    protected int queryMaxResultsLimit;
    protected String loggingContextActivityId;
    protected String loggingContextApplicationName;
    protected String loggingContextBusinessKey;
    protected String loggingContextProcessDefinitionId;
    protected String loggingContextProcessInstanceId;
    protected String loggingContextTenantId;
    protected Boolean initializeTelemetry;
    protected String telemetryEndpoint;
    protected int telemetryRequestRetries;
    protected TelemetryReporter telemetryReporter;
    protected boolean isTelemetryReporterActivate;
    protected Connector<? extends ConnectorRequest<?>> telemetryHttpConnector;
    protected long telemetryReportingPeriod;
    protected TelemetryDataImpl telemetryData;
    protected int telemetryRequestTimeout;
    protected static Properties databaseTypeMappings;
    protected static final String MY_SQL_PRODUCT_NAME = "MySQL";
    protected static final String MARIA_DB_PRODUCT_NAME = "MariaDB";
    protected static final String POSTGRES_DB_PRODUCT_NAME = "PostgreSQL";
    protected static final String CRDB_DB_PRODUCT_NAME = "CockroachDB";

    public ProcessEngineConfigurationImpl() {
        this.repositoryService = new RepositoryServiceImpl();
        this.runtimeService = new RuntimeServiceImpl();
        this.historyService = new HistoryServiceImpl();
        this.identityService = new IdentityServiceImpl();
        this.taskService = new TaskServiceImpl();
        this.formService = new FormServiceImpl();
        this.managementService = new ManagementServiceImpl(this);
        this.authorizationService = new AuthorizationServiceImpl();
        this.caseService = new CaseServiceImpl();
        this.filterService = new FilterServiceImpl();
        this.externalTaskService = new ExternalTaskServiceImpl();
        this.decisionService = new DecisionServiceImpl();
        this.optimizeService = new OptimizeService();
        this.commandRetries = 0;
        this.cacheCapacity = 1000;
        this.enableFetchProcessDefinitionDescription = true;
        this.jobExecutorPriorityRangeMin = null;
        this.jobExecutorPriorityRangeMax = null;
        this.batchJobsPerSeed = 100;
        this.invocationsPerBatchJob = 1;
        this.batchPollTime = 30;
        this.batchJobPriority = DefaultJobPriorityProvider.DEFAULT_PRIORITY;
        this.disableStrictCamundaFormParsing = false;
        this.defaultSerializationFormat = Variables.SerializationDataFormats.JAVA.getName();
        this.javaSerializationFormatEnabled = false;
        this.defaultCharsetName = null;
        this.defaultCharset = null;
        this.autoStoreScriptVariables = false;
        this.enableScriptCompilation = true;
        this.enableScriptEngineCaching = true;
        this.enableFetchScriptEngineFromProcessApplication = true;
        this.enableScriptEngineLoadExternalResources = false;
        this.enableScriptEngineNashornCompatibility = false;
        this.configureScriptEngineHostAccess = true;
        this.cmmnEnabled = true;
        this.dmnEnabled = true;
        this.standaloneTasksEnabled = true;
        this.enableGracefulDegradationOnContextSwitchFailure = true;
        this.wsSyncFactoryClassName = "org.camunda.bpm.engine.impl.webservice.CxfWebServiceClientFactory";
        this.dmnFeelEnableLegacyBehavior = false;
        this.isDbIdentityUsed = true;
        this.isDbHistoryUsed = true;
        this.databaseTablePrefix = "";
        this.databaseSchema = null;
        this.isCreateDiagramOnDeploy = false;
        this.processEnginePlugins = new ArrayList<ProcessEnginePlugin>();
        this.customHistoryEventHandlers = new ArrayList<HistoryEventHandler>();
        this.enableDefaultDbHistoryEventHandler = true;
        this.isExecutionTreePrefetchEnabled = true;
        this.isCompositeIncidentHandlersEnabled = false;
        this.isDeploymentLockUsed = true;
        this.isDeploymentSynchronized = true;
        this.isDbEntityCacheReuseEnabled = false;
        this.isInvokeCustomVariableListeners = true;
        this.dbEntityCacheKeyMapping = DbEntityCacheKeyMapping.defaultEntityCacheKeyMapping();
        this.isMetricsEnabled = true;
        this.isDbMetricsReporterActivate = true;
        this.isTaskMetricsEnabled = true;
        this.enableExpressionsInAdhocQueries = false;
        this.enableExpressionsInStoredQueries = true;
        this.enableXxeProcessing = false;
        this.restrictUserOperationLogToAuthenticatedUsers = true;
        this.disableStrictCallActivityValidation = false;
        this.isBpmnStacktraceVerbose = false;
        this.forceCloseMybatisConnectionPool = true;
        this.tenantIdProvider = null;
        this.commandCheckers = null;
        this.enableHistoricInstancePermissions = false;
        this.isUseSharedSqlSessionFactory = false;
        this.historyCleanupBatchWindowEndTime = "00:00";
        this.historyCleanupBatchWindows = new HashMap<Integer, BatchWindowConfiguration>();
        this.historyCleanupDegreeOfParallelism = 1;
        this.historyCleanupJobPriority = DefaultJobPriorityProvider.DEFAULT_PRIORITY;
        this.batchWindowManager = new DefaultBatchWindowManager();
        this.historyCleanupBatchSize = 500;
        this.historyCleanupBatchThreshold = 10;
        this.historyCleanupMetricsEnabled = true;
        this.historyCleanupEnabled = true;
        this.failedJobListenerMaxRetries = 3;
        this.loginMaxAttempts = 10;
        this.loginDelayFactor = 2;
        this.loginDelayMaxTime = 60;
        this.loginDelayBase = 3;
        this.queryMaxResultsLimit = Integer.MAX_VALUE;
        this.loggingContextActivityId = "activityId";
        this.loggingContextApplicationName = "applicationName";
        this.loggingContextProcessDefinitionId = "processDefinitionId";
        this.loggingContextProcessInstanceId = "processInstanceId";
        this.loggingContextTenantId = "tenantId";
        this.initializeTelemetry = null;
        this.telemetryEndpoint = "https://api.telemetry.camunda.cloud/pings";
        this.telemetryRequestRetries = 2;
        this.isTelemetryReporterActivate = true;
        this.telemetryReportingPeriod = 86400L;
        this.telemetryRequestTimeout = 15000;
    }

    @Override
    public ProcessEngine buildProcessEngine() {
        this.init();
        this.invokePostProcessEngineBuild(this.processEngine = new ProcessEngineImpl(this));
        return this.processEngine;
    }

    protected void init() {
        this.invokePreInit();
        this.initDefaultCharset();
        this.initHistoryLevel();
        this.initHistoryEventProducer();
        this.initCmmnHistoryEventProducer();
        this.initDmnHistoryEventProducer();
        this.initHistoryEventHandler();
        this.initExpressionManager();
        this.initBeans();
        this.initArtifactFactory();
        this.initFormEngines();
        this.initFormTypes();
        this.initFormFieldValidators();
        this.initScripting();
        this.initDmnEngine();
        this.initBusinessCalendarManager();
        this.initCommandContextFactory();
        this.initTransactionContextFactory();
        this.initDataSource();
        this.initCommandExecutors();
        this.initServices();
        this.initIdGenerator();
        this.initFailedJobCommandFactory();
        this.initDeployers();
        this.initJobProvider();
        this.initExternalTaskPriorityProvider();
        this.initBatchHandlers();
        this.initJobExecutor();
        this.initTransactionFactory();
        this.initSqlSessionFactory();
        this.initIdentityProviderSessionFactory();
        this.initSessionFactories();
        this.initValueTypeResolver();
        this.initTypeValidator();
        this.initSerialization();
        this.initJpa();
        this.initDelegateInterceptor();
        this.initEventHandlers();
        this.initProcessApplicationManager();
        this.initCorrelationHandler();
        this.initConditionHandler();
        this.initIncidentHandlers();
        this.initPasswordDigest();
        this.initDeploymentRegistration();
        this.initDeploymentHandlerFactory();
        this.initResourceAuthorizationProvider();
        this.initPermissionProvider();
        this.initHostName();
        this.initMetrics();
        this.initTelemetry();
        this.initMigration();
        this.initCommandCheckers();
        this.initDefaultUserPermissionForTask();
        this.initHistoryRemovalTime();
        this.initHistoryCleanup();
        this.initInvocationsPerBatchJobByBatchType();
        this.initAdminUser();
        this.initAdminGroups();
        this.initPasswordPolicy();
        this.invokePostInit();
    }

    protected void initTypeValidator() {
        if (this.deserializationTypeValidator == null) {
            this.deserializationTypeValidator = new DefaultDeserializationTypeValidator();
        }
        if (this.deserializationTypeValidator instanceof WhitelistingDeserializationTypeValidator) {
            final WhitelistingDeserializationTypeValidator validator = (WhitelistingDeserializationTypeValidator) this.deserializationTypeValidator;
            validator.setAllowedClasses(this.deserializationAllowedClasses);
            validator.setAllowedPackages(this.deserializationAllowedPackages);
        }
    }

    public void initHistoryRemovalTime() {
        this.initHistoryRemovalTimeProvider();
        this.initHistoryRemovalTimeStrategy();
    }

    public void initHistoryRemovalTimeStrategy() {
        if (this.historyRemovalTimeStrategy == null) {
            this.historyRemovalTimeStrategy = "end";
        }
        if (!"start".equals(this.historyRemovalTimeStrategy) && !"end".equals(this.historyRemovalTimeStrategy) && !"none".equals(this.historyRemovalTimeStrategy)) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("historyRemovalTimeStrategy", String.valueOf(this.historyRemovalTimeStrategy), String.format("history removal time strategy must be set to '%s', '%s' or '%s'", "start", "end", "none"));
        }
    }

    public void initHistoryRemovalTimeProvider() {
        if (this.historyRemovalTimeProvider == null) {
            this.historyRemovalTimeProvider = new DefaultHistoryRemovalTimeProvider();
        }
    }

    public void initHistoryCleanup() {
        this.initHistoryCleanupStrategy();
        if (this.historyCleanupDegreeOfParallelism < 1 || this.historyCleanupDegreeOfParallelism > 8) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("historyCleanupDegreeOfParallelism", String.valueOf(this.historyCleanupDegreeOfParallelism), String.format("value for number of threads for history cleanup should be between 1 and %s", 8));
        }
        if (this.historyCleanupBatchWindowStartTime != null) {
            this.initHistoryCleanupBatchWindowStartTime();
        }
        if (this.historyCleanupBatchWindowEndTime != null) {
            this.initHistoryCleanupBatchWindowEndTime();
        }
        this.initHistoryCleanupBatchWindowsMap();
        if (this.historyCleanupBatchSize > 500 || this.historyCleanupBatchSize <= 0) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("historyCleanupBatchSize", String.valueOf(this.historyCleanupBatchSize), String.format("value for batch size should be between 1 and %s", 500));
        }
        if (this.historyCleanupBatchThreshold < 0) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("historyCleanupBatchThreshold", String.valueOf(this.historyCleanupBatchThreshold), "History cleanup batch threshold cannot be negative.");
        }
        this.initHistoryTimeToLive();
        this.initBatchOperationsHistoryTimeToLive();
        this.initHistoryCleanupJobLogTimeToLive();
        this.initTaskMetricsTimeToLive();
    }

    protected void initHistoryCleanupStrategy() {
        if (this.historyCleanupStrategy == null) {
            this.historyCleanupStrategy = "removalTimeBased";
        }
        if (!"removalTimeBased".equals(this.historyCleanupStrategy) && !"endTimeBased".equals(this.historyCleanupStrategy)) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("historyCleanupStrategy", String.valueOf(this.historyCleanupStrategy), String.format("history cleanup strategy must be either set to '%s' or '%s'", "removalTimeBased", "endTimeBased"));
        }
        if ("removalTimeBased".equals(this.historyCleanupStrategy) && "none".equals(this.historyRemovalTimeStrategy)) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("historyRemovalTimeStrategy", String.valueOf(this.historyRemovalTimeStrategy), String.format("history removal time strategy cannot be set to '%s' in conjunction with '%s' history cleanup strategy", "none", "removalTimeBased"));
        }
    }

    private void initHistoryCleanupBatchWindowsMap() {
        if (this.mondayHistoryCleanupBatchWindowStartTime != null || this.mondayHistoryCleanupBatchWindowEndTime != null) {
            this.historyCleanupBatchWindows.put(2, new BatchWindowConfiguration(this.mondayHistoryCleanupBatchWindowStartTime, this.mondayHistoryCleanupBatchWindowEndTime));
        }
        if (this.tuesdayHistoryCleanupBatchWindowStartTime != null || this.tuesdayHistoryCleanupBatchWindowEndTime != null) {
            this.historyCleanupBatchWindows.put(3, new BatchWindowConfiguration(this.tuesdayHistoryCleanupBatchWindowStartTime, this.tuesdayHistoryCleanupBatchWindowEndTime));
        }
        if (this.wednesdayHistoryCleanupBatchWindowStartTime != null || this.wednesdayHistoryCleanupBatchWindowEndTime != null) {
            this.historyCleanupBatchWindows.put(4, new BatchWindowConfiguration(this.wednesdayHistoryCleanupBatchWindowStartTime, this.wednesdayHistoryCleanupBatchWindowEndTime));
        }
        if (this.thursdayHistoryCleanupBatchWindowStartTime != null || this.thursdayHistoryCleanupBatchWindowEndTime != null) {
            this.historyCleanupBatchWindows.put(5, new BatchWindowConfiguration(this.thursdayHistoryCleanupBatchWindowStartTime, this.thursdayHistoryCleanupBatchWindowEndTime));
        }
        if (this.fridayHistoryCleanupBatchWindowStartTime != null || this.fridayHistoryCleanupBatchWindowEndTime != null) {
            this.historyCleanupBatchWindows.put(6, new BatchWindowConfiguration(this.fridayHistoryCleanupBatchWindowStartTime, this.fridayHistoryCleanupBatchWindowEndTime));
        }
        if (this.saturdayHistoryCleanupBatchWindowStartTime != null || this.saturdayHistoryCleanupBatchWindowEndTime != null) {
            this.historyCleanupBatchWindows.put(7, new BatchWindowConfiguration(this.saturdayHistoryCleanupBatchWindowStartTime, this.saturdayHistoryCleanupBatchWindowEndTime));
        }
        if (this.sundayHistoryCleanupBatchWindowStartTime != null || this.sundayHistoryCleanupBatchWindowEndTime != null) {
            this.historyCleanupBatchWindows.put(1, new BatchWindowConfiguration(this.sundayHistoryCleanupBatchWindowStartTime, this.sundayHistoryCleanupBatchWindowEndTime));
        }
    }

    protected void initInvocationsPerBatchJobByBatchType() {
        if (this.invocationsPerBatchJobByBatchType == null) {
            this.invocationsPerBatchJobByBatchType = new HashMap<String, Integer>();
        } else {
            final Set<String> batchTypes = this.invocationsPerBatchJobByBatchType.keySet();
            final Stream<String> filter = batchTypes.stream().filter(batchType -> !this.batchHandlers.containsKey(batchType));
            final ConfigurationLogger log = ProcessEngineConfigurationImpl.LOG;
            Objects.requireNonNull(log);
            filter.forEach((Consumer<? super String>) log::invalidBatchTypeForInvocationsPerBatchJob);
        }
    }

    protected void initHistoryTimeToLive() {
        try {
            ParseUtil.parseHistoryTimeToLive(this.historyTimeToLive);
        } catch (Exception e) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("historyTimeToLive", this.historyTimeToLive, e);
        }
    }

    protected void initBatchOperationsHistoryTimeToLive() {
        try {
            ParseUtil.parseHistoryTimeToLive(this.batchOperationHistoryTimeToLive);
        } catch (Exception e) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("batchOperationHistoryTimeToLive", this.batchOperationHistoryTimeToLive, e);
        }
        if (this.batchOperationsForHistoryCleanup == null) {
            this.batchOperationsForHistoryCleanup = new HashMap<String, String>();
        } else {
            for (final String batchOperation : this.batchOperationsForHistoryCleanup.keySet()) {
                final String timeToLive = this.batchOperationsForHistoryCleanup.get(batchOperation);
                if (!this.batchHandlers.keySet().contains(batchOperation)) {
                    ProcessEngineConfigurationImpl.LOG.invalidBatchOperation(batchOperation, timeToLive);
                }
                try {
                    ParseUtil.parseHistoryTimeToLive(timeToLive);
                } catch (Exception e2) {
                    throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("history time to live for " + batchOperation + " batch operations", timeToLive, e2);
                }
            }
        }
        if (this.batchHandlers != null && this.batchOperationHistoryTimeToLive != null) {
            for (final String batchOperation : this.batchHandlers.keySet()) {
                if (!this.batchOperationsForHistoryCleanup.containsKey(batchOperation)) {
                    this.batchOperationsForHistoryCleanup.put(batchOperation, this.batchOperationHistoryTimeToLive);
                }
            }
        }
        this.parsedBatchOperationsForHistoryCleanup = new HashMap<String, Integer>();
        if (this.batchOperationsForHistoryCleanup != null) {
            for (final String operation : this.batchOperationsForHistoryCleanup.keySet()) {
                final Integer historyTimeToLive = ParseUtil.parseHistoryTimeToLive(this.batchOperationsForHistoryCleanup.get(operation));
                this.parsedBatchOperationsForHistoryCleanup.put(operation, historyTimeToLive);
            }
        }
    }

    private void initHistoryCleanupBatchWindowEndTime() {
        try {
            this.historyCleanupBatchWindowEndTimeAsDate = HistoryCleanupHelper.parseTimeConfiguration(this.historyCleanupBatchWindowEndTime);
        } catch (ParseException e) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("historyCleanupBatchWindowEndTime", this.historyCleanupBatchWindowEndTime);
        }
    }

    private void initHistoryCleanupBatchWindowStartTime() {
        try {
            this.historyCleanupBatchWindowStartTimeAsDate = HistoryCleanupHelper.parseTimeConfiguration(this.historyCleanupBatchWindowStartTime);
        } catch (ParseException e) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("historyCleanupBatchWindowStartTime", this.historyCleanupBatchWindowStartTime);
        }
    }

    protected void initHistoryCleanupJobLogTimeToLive() {
        try {
            ParseUtil.parseHistoryTimeToLive(this.historyCleanupJobLogTimeToLive);
        } catch (Exception e) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("historyCleanupJobLogTimeToLive", this.historyCleanupJobLogTimeToLive, e);
        }
    }

    protected void initTaskMetricsTimeToLive() {
        try {
            this.parsedTaskMetricsTimeToLive = ParseUtil.parseHistoryTimeToLive(this.taskMetricsTimeToLive);
        } catch (Exception e) {
            throw ProcessEngineConfigurationImpl.LOG.invalidPropertyValue("taskMetricsTimeToLive", this.taskMetricsTimeToLive, e);
        }
    }

    protected void invokePreInit() {
        for (final ProcessEnginePlugin plugin : this.processEnginePlugins) {
            ProcessEngineConfigurationImpl.LOG.pluginActivated(plugin.toString(), this.getProcessEngineName());
            plugin.preInit(this);
        }
    }

    protected void invokePostInit() {
        for (final ProcessEnginePlugin plugin : this.processEnginePlugins) {
            plugin.postInit(this);
        }
    }

    protected void invokePostProcessEngineBuild(final ProcessEngine engine) {
        for (final ProcessEnginePlugin plugin : this.processEnginePlugins) {
            plugin.postProcessEngineBuild(engine);
        }
    }

    protected void initFailedJobCommandFactory() {
        if (this.failedJobCommandFactory == null) {
            this.failedJobCommandFactory = new DefaultFailedJobCommandFactory();
        }
        if (this.postParseListeners == null) {
            this.postParseListeners = new ArrayList<BpmnParseListener>();
        }
        this.postParseListeners.add(new DefaultFailedJobParseListener());
    }

    protected void initIncidentHandlers() {
        if (this.incidentHandlers == null) {
            this.incidentHandlers = new HashMap<String, IncidentHandler>();
            final DefaultIncidentHandler failedJobIncidentHandler = new DefaultIncidentHandler("failedJob");
            final DefaultIncidentHandler failedExternalTaskIncidentHandler = new DefaultIncidentHandler("failedExternalTask");
            if (this.isCompositeIncidentHandlersEnabled) {
                this.addIncidentHandler(new CompositeIncidentHandler(failedJobIncidentHandler, new IncidentHandler[0]));
                this.addIncidentHandler(new CompositeIncidentHandler(failedExternalTaskIncidentHandler, new IncidentHandler[0]));
            } else {
                this.addIncidentHandler(failedJobIncidentHandler);
                this.addIncidentHandler(failedExternalTaskIncidentHandler);
            }
        }
        if (this.customIncidentHandlers != null) {
            for (final IncidentHandler incidentHandler : this.customIncidentHandlers) {
                this.addIncidentHandler(incidentHandler);
            }
        }
    }

    protected void initBatchHandlers() {
        if (this.batchHandlers == null) {
            this.batchHandlers = new HashMap<String, BatchJobHandler<?>>();
            final MigrationBatchJobHandler migrationHandler = new MigrationBatchJobHandler();
            this.batchHandlers.put(migrationHandler.getType(), migrationHandler);
            final ModificationBatchJobHandler modificationHandler = new ModificationBatchJobHandler();
            this.batchHandlers.put(modificationHandler.getType(), modificationHandler);
            final DeleteProcessInstancesJobHandler deleteProcessJobHandler = new DeleteProcessInstancesJobHandler();
            this.batchHandlers.put(deleteProcessJobHandler.getType(), deleteProcessJobHandler);
            final DeleteHistoricProcessInstancesJobHandler deleteHistoricProcessInstancesJobHandler = new DeleteHistoricProcessInstancesJobHandler();
            this.batchHandlers.put(deleteHistoricProcessInstancesJobHandler.getType(), deleteHistoricProcessInstancesJobHandler);
            final SetJobRetriesJobHandler setJobRetriesJobHandler = new SetJobRetriesJobHandler();
            this.batchHandlers.put(setJobRetriesJobHandler.getType(), setJobRetriesJobHandler);
            final SetExternalTaskRetriesJobHandler setExternalTaskRetriesJobHandler = new SetExternalTaskRetriesJobHandler();
            this.batchHandlers.put(setExternalTaskRetriesJobHandler.getType(), setExternalTaskRetriesJobHandler);
            final RestartProcessInstancesJobHandler restartProcessInstancesJobHandler = new RestartProcessInstancesJobHandler();
            this.batchHandlers.put(restartProcessInstancesJobHandler.getType(), restartProcessInstancesJobHandler);
            final UpdateProcessInstancesSuspendStateJobHandler suspendProcessInstancesJobHandler = new UpdateProcessInstancesSuspendStateJobHandler();
            this.batchHandlers.put(suspendProcessInstancesJobHandler.getType(), suspendProcessInstancesJobHandler);
            final DeleteHistoricDecisionInstancesJobHandler deleteHistoricDecisionInstancesJobHandler = new DeleteHistoricDecisionInstancesJobHandler();
            this.batchHandlers.put(deleteHistoricDecisionInstancesJobHandler.getType(), deleteHistoricDecisionInstancesJobHandler);
            final ProcessSetRemovalTimeJobHandler processSetRemovalTimeJobHandler = new ProcessSetRemovalTimeJobHandler();
            this.batchHandlers.put(processSetRemovalTimeJobHandler.getType(), processSetRemovalTimeJobHandler);
            final DecisionSetRemovalTimeJobHandler decisionSetRemovalTimeJobHandler = new DecisionSetRemovalTimeJobHandler();
            this.batchHandlers.put(decisionSetRemovalTimeJobHandler.getType(), decisionSetRemovalTimeJobHandler);
            final BatchSetRemovalTimeJobHandler batchSetRemovalTimeJobHandler = new BatchSetRemovalTimeJobHandler();
            this.batchHandlers.put(batchSetRemovalTimeJobHandler.getType(), batchSetRemovalTimeJobHandler);
            final BatchSetVariablesHandler batchSetVariablesHandler = new BatchSetVariablesHandler();
            this.batchHandlers.put(batchSetVariablesHandler.getType(), batchSetVariablesHandler);
            final MessageCorrelationBatchJobHandler messageCorrelationJobHandler = new MessageCorrelationBatchJobHandler();
            this.batchHandlers.put(messageCorrelationJobHandler.getType(), messageCorrelationJobHandler);
        }
        if (this.customBatchJobHandlers != null) {
            for (final BatchJobHandler<?> customBatchJobHandler : this.customBatchJobHandlers) {
                this.batchHandlers.put(customBatchJobHandler.getType(), customBatchJobHandler);
            }
        }
    }

    protected abstract Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequired();

    protected abstract Collection<? extends CommandInterceptor> getDefaultCommandInterceptorsTxRequiresNew();

    protected void initCommandExecutors() {
        this.initActualCommandExecutor();
        this.initCommandInterceptorsTxRequired();
        this.initCommandExecutorTxRequired();
        this.initCommandInterceptorsTxRequiresNew();
        this.initCommandExecutorTxRequiresNew();
        this.initCommandExecutorDbSchemaOperations();
    }

    protected void initActualCommandExecutor() {
        this.actualCommandExecutor = new CommandExecutorImpl();
    }

    protected void initCommandInterceptorsTxRequired() {
        if (this.commandInterceptorsTxRequired == null) {
            if (this.customPreCommandInterceptorsTxRequired != null) {
                this.commandInterceptorsTxRequired = new ArrayList<CommandInterceptor>(this.customPreCommandInterceptorsTxRequired);
            } else {
                this.commandInterceptorsTxRequired = new ArrayList<CommandInterceptor>();
            }
            this.commandInterceptorsTxRequired.addAll(this.getDefaultCommandInterceptorsTxRequired());
            if (this.customPostCommandInterceptorsTxRequired != null) {
                this.commandInterceptorsTxRequired.addAll(this.customPostCommandInterceptorsTxRequired);
            }
            this.commandInterceptorsTxRequired.add(this.actualCommandExecutor);
        }
    }

    protected void initCommandInterceptorsTxRequiresNew() {
        if (this.commandInterceptorsTxRequiresNew == null) {
            if (this.customPreCommandInterceptorsTxRequiresNew != null) {
                this.commandInterceptorsTxRequiresNew = new ArrayList<CommandInterceptor>(this.customPreCommandInterceptorsTxRequiresNew);
            } else {
                this.commandInterceptorsTxRequiresNew = new ArrayList<CommandInterceptor>();
            }
            this.commandInterceptorsTxRequiresNew.addAll(this.getDefaultCommandInterceptorsTxRequiresNew());
            if (this.customPostCommandInterceptorsTxRequiresNew != null) {
                this.commandInterceptorsTxRequiresNew.addAll(this.customPostCommandInterceptorsTxRequiresNew);
            }
            this.commandInterceptorsTxRequiresNew.add(this.actualCommandExecutor);
        }
    }

    protected void initCommandExecutorTxRequired() {
        if (this.commandExecutorTxRequired == null) {
            this.commandExecutorTxRequired = this.initInterceptorChain(this.commandInterceptorsTxRequired);
        }
    }

    protected void initCommandExecutorTxRequiresNew() {
        if (this.commandExecutorTxRequiresNew == null) {
            this.commandExecutorTxRequiresNew = this.initInterceptorChain(this.commandInterceptorsTxRequiresNew);
        }
    }

    protected void initCommandExecutorDbSchemaOperations() {
        if (this.commandExecutorSchemaOperations == null) {
            this.commandExecutorSchemaOperations = this.commandExecutorTxRequired;
        }
    }

    protected CommandInterceptor initInterceptorChain(final List<CommandInterceptor> chain) {
        if (chain == null || chain.isEmpty()) {
            throw new ProcessEngineException("invalid command interceptor chain configuration: " + chain);
        }
        for (int i = 0; i < chain.size() - 1; ++i) {
            chain.get(i).setNext(chain.get(i + 1));
        }
        return chain.get(0);
    }

    protected void initServices() {
        this.initService(this.repositoryService);
        this.initService(this.runtimeService);
        this.initService(this.historyService);
        this.initService(this.identityService);
        this.initService(this.taskService);
        this.initService(this.formService);
        this.initService(this.managementService);
        this.initService(this.authorizationService);
        this.initService(this.caseService);
        this.initService(this.filterService);
        this.initService(this.externalTaskService);
        this.initService(this.decisionService);
        this.initService(this.optimizeService);
    }

    protected void initService(final Object service) {
        if (service instanceof ServiceImpl) {
            ((ServiceImpl) service).setCommandExecutor(this.commandExecutorTxRequired);
        }
        if (service instanceof RepositoryServiceImpl) {
            ((RepositoryServiceImpl) service).setDeploymentCharset(this.getDefaultCharset());
        }
    }

    protected void initDataSource() {
        if (this.dataSource == null) {
            Label_0251:
            {
                if (this.dataSourceJndiName != null) {
                    try {
                        this.dataSource = (DataSource) new InitialContext().lookup(this.dataSourceJndiName);
                        break Label_0251;
                    } catch (Exception e) {
                        throw new ProcessEngineException("couldn't lookup datasource from " + this.dataSourceJndiName + ": " + e.getMessage(), e);
                    }
                }
                if (this.jdbcUrl != null) {
                    if (this.jdbcDriver == null || this.jdbcUrl == null || this.jdbcUsername == null) {
                        throw new ProcessEngineException("DataSource or JDBC properties have to be specified in a process engine configuration");
                    }
                    final PooledDataSource pooledDataSource = new PooledDataSource(ReflectUtil.getClassLoader(), this.jdbcDriver, this.jdbcUrl, this.jdbcUsername, this.jdbcPassword);
                    if (this.jdbcMaxActiveConnections > 0) {
                        pooledDataSource.setPoolMaximumActiveConnections(this.jdbcMaxActiveConnections);
                    }
                    if (this.jdbcMaxIdleConnections > 0) {
                        pooledDataSource.setPoolMaximumIdleConnections(this.jdbcMaxIdleConnections);
                    }
                    if (this.jdbcMaxCheckoutTime > 0) {
                        pooledDataSource.setPoolMaximumCheckoutTime(this.jdbcMaxCheckoutTime);
                    }
                    if (this.jdbcMaxWaitTime > 0) {
                        pooledDataSource.setPoolTimeToWait(this.jdbcMaxWaitTime);
                    }
                    if (this.jdbcPingEnabled) {
                        pooledDataSource.setPoolPingEnabled(true);
                        if (this.jdbcPingQuery != null) {
                            pooledDataSource.setPoolPingQuery(this.jdbcPingQuery);
                        }
                        pooledDataSource.setPoolPingConnectionsNotUsedFor(this.jdbcPingConnectionNotUsedFor);
                    }
                    this.dataSource = (DataSource) pooledDataSource;
                }
            }
            if (this.dataSource instanceof PooledDataSource) {
                ((PooledDataSource) this.dataSource).forceCloseAll();
            }
        }
        if (this.databaseType == null) {
            this.initDatabaseType();
        }
    }

    protected static Properties getDefaultDatabaseTypeMappings() {
        final Properties databaseTypeMappings = new Properties();
        databaseTypeMappings.setProperty("H2", "h2");
        databaseTypeMappings.setProperty("MySQL", "mysql");
        databaseTypeMappings.setProperty("MariaDB", "mariadb");
        databaseTypeMappings.setProperty("Oracle", "oracle");
        databaseTypeMappings.setProperty("PostgreSQL", "postgres");
        databaseTypeMappings.setProperty("CockroachDB", "cockroachdb");
        databaseTypeMappings.setProperty("Microsoft SQL Server", "mssql");
        databaseTypeMappings.setProperty("DB2", "db2");
        databaseTypeMappings.setProperty("DB2", "db2");
        databaseTypeMappings.setProperty("DB2/NT", "db2");
        databaseTypeMappings.setProperty("DB2/NT64", "db2");
        databaseTypeMappings.setProperty("DB2 UDP", "db2");
        databaseTypeMappings.setProperty("DB2/LINUX", "db2");
        databaseTypeMappings.setProperty("DB2/LINUX390", "db2");
        databaseTypeMappings.setProperty("DB2/LINUXX8664", "db2");
        databaseTypeMappings.setProperty("DB2/LINUXZ64", "db2");
        databaseTypeMappings.setProperty("DB2/400 SQL", "db2");
        databaseTypeMappings.setProperty("DB2/6000", "db2");
        databaseTypeMappings.setProperty("DB2 UDB iSeries", "db2");
        databaseTypeMappings.setProperty("DB2/AIX64", "db2");
        databaseTypeMappings.setProperty("DB2/HPUX", "db2");
        databaseTypeMappings.setProperty("DB2/HP64", "db2");
        databaseTypeMappings.setProperty("DB2/SUN", "db2");
        databaseTypeMappings.setProperty("DB2/SUN64", "db2");
        databaseTypeMappings.setProperty("DB2/PTX", "db2");
        databaseTypeMappings.setProperty("DB2/2", "db2");
        return databaseTypeMappings;
    }

    public void initDatabaseType() {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            final DatabaseMetaData databaseMetaData = connection.getMetaData();
            String databaseProductName = databaseMetaData.getDatabaseProductName();
            if ("MySQL".equals(databaseProductName)) {
                databaseProductName = this.checkForMariaDb(databaseMetaData, databaseProductName);
            }
            if ("PostgreSQL".equals(databaseProductName)) {
                databaseProductName = this.checkForCrdb(connection);
            }
            ProcessEngineConfigurationImpl.LOG.debugDatabaseproductName(databaseProductName);
            this.databaseType = ProcessEngineConfigurationImpl.databaseTypeMappings.getProperty(databaseProductName);
            EnsureUtil.ensureNotNull("couldn't deduct database type from database product name '" + databaseProductName + "'", "databaseType", this.databaseType);
            ProcessEngineConfigurationImpl.LOG.debugDatabaseType(this.databaseType);
            this.initDatabaseVendorAndVersion(databaseMetaData);
        } catch (SQLException e) {
            ProcessEngineConfigurationImpl.LOG.databaseConnectionAccessException(e);
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e1) {
                ProcessEngineConfigurationImpl.LOG.databaseConnectionCloseException(e1);
            }
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e2) {
                ProcessEngineConfigurationImpl.LOG.databaseConnectionCloseException(e2);
            }
        }
    }

    protected String checkForMariaDb(final DatabaseMetaData databaseMetaData, final String databaseName) {
        try {
            final String databaseProductVersion = databaseMetaData.getDatabaseProductVersion();
            if (databaseProductVersion != null && databaseProductVersion.toLowerCase().contains("mariadb")) {
                return "MariaDB";
            }
        } catch (SQLException ex) {
        }
        try {
            final String driverName = databaseMetaData.getDriverName();
            if (driverName != null && driverName.toLowerCase().contains("mariadb")) {
                return "MariaDB";
            }
        } catch (SQLException ex2) {
        }
        final String metaDataClassName = databaseMetaData.getClass().getName();
        if (metaDataClassName != null && metaDataClassName.toLowerCase().contains("mariadb")) {
            return "MariaDB";
        }
        return databaseName;
    }

    protected String checkForCrdb(final Connection connection) {
        try {
            final PreparedStatement preparedStatement = connection.prepareStatement("select version() as version;");
            try {
                final ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    final String versionData = result.getString(1);
                    if (versionData != null && versionData.toLowerCase().contains("cockroachdb")) {
                        final String s = "CockroachDB";
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                        return s;
                    }
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Throwable t) {
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        } catch (SQLException ex) {
        }
        return "PostgreSQL";
    }

    protected void initDatabaseVendorAndVersion(final DatabaseMetaData databaseMetaData) throws SQLException {
        this.databaseVendor = databaseMetaData.getDatabaseProductName();
        this.databaseVersion = databaseMetaData.getDatabaseProductVersion();
    }

    protected void initTransactionFactory() {
        if (this.transactionFactory == null) {
            if (this.transactionsExternallyManaged) {
                this.transactionFactory = (TransactionFactory) new ManagedTransactionFactory();
            } else {
                this.transactionFactory = (TransactionFactory) new JdbcTransactionFactory();
            }
        }
    }

    protected void initSqlSessionFactory() {
        synchronized (ProcessEngineConfigurationImpl.class) {
            if (this.isUseSharedSqlSessionFactory) {
                this.sqlSessionFactory = ProcessEngineConfigurationImpl.cachedSqlSessionFactory;
            }
            if (this.sqlSessionFactory == null) {
                InputStream inputStream = null;
                try {
                    inputStream = this.getMyBatisXmlConfigurationSteam();
                    final Environment environment = new Environment("default", this.transactionFactory, this.dataSource);
                    final Reader reader = new InputStreamReader(inputStream);
                    final Properties properties = new Properties();
                    if (this.isUseSharedSqlSessionFactory) {
                        properties.put("prefix", "${@org.camunda.bpm.engine.impl.context.Context@getProcessEngineConfiguration().databaseTablePrefix}");
                    } else {
                        properties.put("prefix", this.databaseTablePrefix);
                    }
                    initSqlSessionFactoryProperties(properties, this.databaseTablePrefix, this.databaseType);
                    final XMLConfigBuilder parser = new XMLConfigBuilder(reader, "", properties);
                    Configuration configuration = parser.getConfiguration();
                    configuration.setEnvironment(environment);
                    configuration = parser.parse();
                    configuration.setDefaultStatementTimeout(this.jdbcStatementTimeout);
                    if (this.isJdbcBatchProcessing()) {
                        configuration.setDefaultExecutorType(ExecutorType.BATCH);
                    }
                    this.sqlSessionFactory = (SqlSessionFactory) new DefaultSqlSessionFactory(configuration);
                    if (this.isUseSharedSqlSessionFactory) {
                        ProcessEngineConfigurationImpl.cachedSqlSessionFactory = this.sqlSessionFactory;
                    }
                } catch (Exception e) {
                    throw new ProcessEngineException("Error while building ibatis SqlSessionFactory: " + e.getMessage(), e);
                } finally {
                    IoUtil.closeSilently(inputStream);
                }
            }
        }
    }

    public static void initSqlSessionFactoryProperties(final Properties properties, final String databaseTablePrefix, final String databaseType) {
        if (databaseType != null) {
            properties.put("limitBefore", DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.get(databaseType));
            properties.put("limitAfter", DbSqlSessionFactory.databaseSpecificLimitAfterStatements.get(databaseType));
            properties.put("limitBeforeWithoutOffset", DbSqlSessionFactory.databaseSpecificLimitBeforeWithoutOffsetStatements.get(databaseType));
            properties.put("limitAfterWithoutOffset", DbSqlSessionFactory.databaseSpecificLimitAfterWithoutOffsetStatements.get(databaseType));
            properties.put("optimizeLimitBeforeWithoutOffset", DbSqlSessionFactory.optimizeDatabaseSpecificLimitBeforeWithoutOffsetStatements.get(databaseType));
            properties.put("optimizeLimitAfterWithoutOffset", DbSqlSessionFactory.optimizeDatabaseSpecificLimitAfterWithoutOffsetStatements.get(databaseType));
            properties.put("innerLimitAfter", DbSqlSessionFactory.databaseSpecificInnerLimitAfterStatements.get(databaseType));
            properties.put("limitBetween", DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.get(databaseType));
            properties.put("limitBetweenFilter", DbSqlSessionFactory.databaseSpecificLimitBetweenFilterStatements.get(databaseType));
            properties.put("limitBetweenAcquisition", DbSqlSessionFactory.databaseSpecificLimitBetweenAcquisitionStatements.get(databaseType));
            properties.put("orderBy", DbSqlSessionFactory.databaseSpecificOrderByStatements.get(databaseType));
            properties.put("limitBeforeNativeQuery", DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.get(databaseType));
            properties.put("distinct", DbSqlSessionFactory.databaseSpecificDistinct.get(databaseType));
            properties.put("numericCast", DbSqlSessionFactory.databaseSpecificNumericCast.get(databaseType));
            properties.put("countDistinctBeforeStart", DbSqlSessionFactory.databaseSpecificCountDistinctBeforeStart.get(databaseType));
            properties.put("countDistinctBeforeEnd", DbSqlSessionFactory.databaseSpecificCountDistinctBeforeEnd.get(databaseType));
            properties.put("countDistinctAfterEnd", DbSqlSessionFactory.databaseSpecificCountDistinctAfterEnd.get(databaseType));
            properties.put("escapeChar", DbSqlSessionFactory.databaseSpecificEscapeChar.get(databaseType));
            properties.put("bitand1", DbSqlSessionFactory.databaseSpecificBitAnd1.get(databaseType));
            properties.put("bitand2", DbSqlSessionFactory.databaseSpecificBitAnd2.get(databaseType));
            properties.put("bitand3", DbSqlSessionFactory.databaseSpecificBitAnd3.get(databaseType));
            properties.put("datepart1", DbSqlSessionFactory.databaseSpecificDatepart1.get(databaseType));
            properties.put("datepart2", DbSqlSessionFactory.databaseSpecificDatepart2.get(databaseType));
            properties.put("datepart3", DbSqlSessionFactory.databaseSpecificDatepart3.get(databaseType));
            properties.put("trueConstant", DbSqlSessionFactory.databaseSpecificTrueConstant.get(databaseType));
            properties.put("falseConstant", DbSqlSessionFactory.databaseSpecificFalseConstant.get(databaseType));
            properties.put("dbSpecificDummyTable", DbSqlSessionFactory.databaseSpecificDummyTable.get(databaseType));
            properties.put("dbSpecificIfNullFunction", DbSqlSessionFactory.databaseSpecificIfNull.get(databaseType));
            properties.put("dayComparator", DbSqlSessionFactory.databaseSpecificDaysComparator.get(databaseType));
            properties.put("collationForCaseSensitivity", DbSqlSessionFactory.databaseSpecificCollationForCaseSensitivity.get(databaseType));
            properties.put("authJoinStart", DbSqlSessionFactory.databaseSpecificAuthJoinStart.get(databaseType));
            properties.put("authJoinEnd", DbSqlSessionFactory.databaseSpecificAuthJoinEnd.get(databaseType));
            properties.put("authJoinSeparator", DbSqlSessionFactory.databaseSpecificAuthJoinSeparator.get(databaseType));
            properties.put("authJoin1Start", DbSqlSessionFactory.databaseSpecificAuth1JoinStart.get(databaseType));
            properties.put("authJoin1End", DbSqlSessionFactory.databaseSpecificAuth1JoinEnd.get(databaseType));
            properties.put("authJoin1Separator", DbSqlSessionFactory.databaseSpecificAuth1JoinSeparator.get(databaseType));
            final Map<String, String> constants = DbSqlSessionFactory.dbSpecificConstants.get(databaseType);
            for (final Map.Entry<String, String> entry : constants.entrySet()) {
                properties.put(entry.getKey(), entry.getValue());
            }
        }
    }

    protected InputStream getMyBatisXmlConfigurationSteam() {
        return ReflectUtil.getResourceAsStream("org/camunda/bpm/engine/impl/mapping/mappings.xml");
    }

    protected void initIdentityProviderSessionFactory() {
        if (this.identityProviderSessionFactory == null) {
            this.identityProviderSessionFactory = new GenericManagerFactory(DbIdentityServiceProvider.class);
        }
    }

    protected void initSessionFactories() {
        if (this.sessionFactories == null) {
            this.sessionFactories = new HashMap<Class<?>, SessionFactory>();
            this.initPersistenceProviders();
            this.addSessionFactory(new DbEntityManagerFactory(this.idGenerator));
            this.addSessionFactory(new GenericManagerFactory(AttachmentManager.class));
            this.addSessionFactory(new GenericManagerFactory(CommentManager.class));
            this.addSessionFactory(new GenericManagerFactory(DeploymentManager.class));
            this.addSessionFactory(new GenericManagerFactory(ExecutionManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricActivityInstanceManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricCaseActivityInstanceManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricStatisticsManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricDetailManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricProcessInstanceManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricCaseInstanceManager.class));
            this.addSessionFactory(new GenericManagerFactory(UserOperationLogManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricTaskInstanceManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricVariableInstanceManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricIncidentManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricIdentityLinkLogManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricJobLogManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricExternalTaskLogManager.class));
            this.addSessionFactory(new GenericManagerFactory(IdentityInfoManager.class));
            this.addSessionFactory(new GenericManagerFactory(IdentityLinkManager.class));
            this.addSessionFactory(new GenericManagerFactory(JobManager.class));
            this.addSessionFactory(new GenericManagerFactory(JobDefinitionManager.class));
            this.addSessionFactory(new GenericManagerFactory(ProcessDefinitionManager.class));
            this.addSessionFactory(new GenericManagerFactory(PropertyManager.class));
            this.addSessionFactory(new GenericManagerFactory(ResourceManager.class));
            this.addSessionFactory(new GenericManagerFactory(ByteArrayManager.class));
            this.addSessionFactory(new GenericManagerFactory(TableDataManager.class));
            this.addSessionFactory(new GenericManagerFactory(TaskManager.class));
            this.addSessionFactory(new GenericManagerFactory(TaskReportManager.class));
            this.addSessionFactory(new GenericManagerFactory(VariableInstanceManager.class));
            this.addSessionFactory(new GenericManagerFactory(EventSubscriptionManager.class));
            this.addSessionFactory(new GenericManagerFactory(StatisticsManager.class));
            this.addSessionFactory(new GenericManagerFactory(IncidentManager.class));
            this.addSessionFactory(new GenericManagerFactory(AuthorizationManager.class));
            this.addSessionFactory(new GenericManagerFactory(FilterManager.class));
            this.addSessionFactory(new GenericManagerFactory(MeterLogManager.class));
            this.addSessionFactory(new GenericManagerFactory(ExternalTaskManager.class));
            this.addSessionFactory(new GenericManagerFactory(ReportManager.class));
            this.addSessionFactory(new GenericManagerFactory(BatchManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricBatchManager.class));
            this.addSessionFactory(new GenericManagerFactory(TenantManager.class));
            this.addSessionFactory(new GenericManagerFactory(SchemaLogManager.class));
            this.addSessionFactory(new GenericManagerFactory(CaseDefinitionManager.class));
            this.addSessionFactory(new GenericManagerFactory(CaseExecutionManager.class));
            this.addSessionFactory(new GenericManagerFactory(CaseSentryPartManager.class));
            this.addSessionFactory(new GenericManagerFactory(DecisionDefinitionManager.class));
            this.addSessionFactory(new GenericManagerFactory(DecisionRequirementsDefinitionManager.class));
            this.addSessionFactory(new GenericManagerFactory(HistoricDecisionInstanceManager.class));
            this.addSessionFactory(new GenericManagerFactory(CamundaFormDefinitionManager.class));
            this.addSessionFactory(new GenericManagerFactory(OptimizeManager.class));
            this.sessionFactories.put(ReadOnlyIdentityProvider.class, this.identityProviderSessionFactory);
            final Class<?> identityProviderType = this.identityProviderSessionFactory.getSessionType();
            if (WritableIdentityProvider.class.isAssignableFrom(identityProviderType)) {
                this.sessionFactories.put(WritableIdentityProvider.class, this.identityProviderSessionFactory);
            }
        }
        if (this.customSessionFactories != null) {
            for (final SessionFactory sessionFactory : this.customSessionFactories) {
                this.addSessionFactory(sessionFactory);
            }
        }
    }

    protected void initPersistenceProviders() {
        this.ensurePrefixAndSchemaFitToegether(this.databaseTablePrefix, this.databaseSchema);
        (this.dbSqlSessionFactory = new DbSqlSessionFactory(this.jdbcBatchProcessing)).setDatabaseType(this.databaseType);
        this.dbSqlSessionFactory.setIdGenerator(this.idGenerator);
        this.dbSqlSessionFactory.setSqlSessionFactory(this.sqlSessionFactory);
        this.dbSqlSessionFactory.setDbIdentityUsed(this.isDbIdentityUsed);
        this.dbSqlSessionFactory.setDbHistoryUsed(this.isDbHistoryUsed);
        this.dbSqlSessionFactory.setCmmnEnabled(this.cmmnEnabled);
        this.dbSqlSessionFactory.setDmnEnabled(this.dmnEnabled);
        this.dbSqlSessionFactory.setDatabaseTablePrefix(this.databaseTablePrefix);
        if (this.databaseTablePrefix != null && this.databaseSchema == null && this.databaseTablePrefix.contains(".")) {
            this.databaseSchema = this.databaseTablePrefix.split("\\.")[0];
        }
        this.dbSqlSessionFactory.setDatabaseSchema(this.databaseSchema);
        this.addSessionFactory(this.dbSqlSessionFactory);
        this.addSessionFactory(new DbSqlPersistenceProviderFactory());
    }

    protected void initMigration() {
        this.initMigrationInstructionValidators();
        this.initMigrationActivityMatcher();
        this.initMigrationInstructionGenerator();
        this.initMigratingActivityInstanceValidators();
        this.initMigratingTransitionInstanceValidators();
        this.initMigratingCompensationInstanceValidators();
    }

    protected void initMigrationActivityMatcher() {
        if (this.migrationActivityMatcher == null) {
            this.migrationActivityMatcher = new DefaultMigrationActivityMatcher();
        }
    }

    protected void initMigrationInstructionGenerator() {
        if (this.migrationInstructionGenerator == null) {
            this.migrationInstructionGenerator = new DefaultMigrationInstructionGenerator(this.migrationActivityMatcher);
        }
        final List<MigrationActivityValidator> migrationActivityValidators = new ArrayList<MigrationActivityValidator>();
        if (this.customPreMigrationActivityValidators != null) {
            migrationActivityValidators.addAll(this.customPreMigrationActivityValidators);
        }
        migrationActivityValidators.addAll(this.getDefaultMigrationActivityValidators());
        if (this.customPostMigrationActivityValidators != null) {
            migrationActivityValidators.addAll(this.customPostMigrationActivityValidators);
        }
        this.migrationInstructionGenerator = this.migrationInstructionGenerator.migrationActivityValidators(migrationActivityValidators).migrationInstructionValidators(this.migrationInstructionValidators);
    }

    protected void initMigrationInstructionValidators() {
        if (this.migrationInstructionValidators == null) {
            this.migrationInstructionValidators = new ArrayList<MigrationInstructionValidator>();
            if (this.customPreMigrationInstructionValidators != null) {
                this.migrationInstructionValidators.addAll(this.customPreMigrationInstructionValidators);
            }
            this.migrationInstructionValidators.addAll(this.getDefaultMigrationInstructionValidators());
            if (this.customPostMigrationInstructionValidators != null) {
                this.migrationInstructionValidators.addAll(this.customPostMigrationInstructionValidators);
            }
        }
    }

    protected void initMigratingActivityInstanceValidators() {
        if (this.migratingActivityInstanceValidators == null) {
            this.migratingActivityInstanceValidators = new ArrayList<MigratingActivityInstanceValidator>();
            if (this.customPreMigratingActivityInstanceValidators != null) {
                this.migratingActivityInstanceValidators.addAll(this.customPreMigratingActivityInstanceValidators);
            }
            this.migratingActivityInstanceValidators.addAll(this.getDefaultMigratingActivityInstanceValidators());
            if (this.customPostMigratingActivityInstanceValidators != null) {
                this.migratingActivityInstanceValidators.addAll(this.customPostMigratingActivityInstanceValidators);
            }
        }
    }

    protected void initMigratingTransitionInstanceValidators() {
        if (this.migratingTransitionInstanceValidators == null) {
            (this.migratingTransitionInstanceValidators = new ArrayList<MigratingTransitionInstanceValidator>()).addAll(this.getDefaultMigratingTransitionInstanceValidators());
        }
    }

    protected void initMigratingCompensationInstanceValidators() {
        if (this.migratingCompensationInstanceValidators == null) {
            (this.migratingCompensationInstanceValidators = new ArrayList<MigratingCompensationInstanceValidator>()).add(new NoUnmappedLeafInstanceValidator());
            this.migratingCompensationInstanceValidators.add(new NoUnmappedCompensationStartEventValidator());
        }
    }

    protected void ensurePrefixAndSchemaFitToegether(final String prefix, final String schema) {
        if (schema == null) {
            return;
        }
        if (prefix == null || (prefix != null && !prefix.startsWith(schema + "."))) {
            throw new ProcessEngineException("When setting a schema the prefix has to be schema + '.'. Received schema: " + schema + " prefix: " + prefix);
        }
    }

    protected void addSessionFactory(final SessionFactory sessionFactory) {
        this.sessionFactories.put(sessionFactory.getSessionType(), sessionFactory);
    }

    protected void initDeployers() {
        if (this.deployers == null) {
            this.deployers = new ArrayList<Deployer>();
            if (this.customPreDeployers != null) {
                this.deployers.addAll(this.customPreDeployers);
            }
            this.deployers.addAll(this.getDefaultDeployers());
            if (this.customPostDeployers != null) {
                this.deployers.addAll(this.customPostDeployers);
            }
        }
        if (this.deploymentCache == null) {
            final List<Deployer> deployers = new ArrayList<Deployer>();
            if (this.customPreDeployers != null) {
                deployers.addAll(this.customPreDeployers);
            }
            deployers.addAll(this.getDefaultDeployers());
            if (this.customPostDeployers != null) {
                deployers.addAll(this.customPostDeployers);
            }
            this.initCacheFactory();
            (this.deploymentCache = new DeploymentCache(this.cacheFactory, this.cacheCapacity)).setDeployers(deployers);
        }
    }

    protected Collection<? extends Deployer> getDefaultDeployers() {
        final List<Deployer> defaultDeployers = new ArrayList<Deployer>();
        final BpmnDeployer bpmnDeployer = this.getBpmnDeployer();
        defaultDeployers.add(bpmnDeployer);
        defaultDeployers.add(this.getCamundaFormDeployer());
        if (this.isCmmnEnabled()) {
            final CmmnDeployer cmmnDeployer = this.getCmmnDeployer();
            defaultDeployers.add(cmmnDeployer);
        }
        if (this.isDmnEnabled()) {
            final DecisionRequirementsDefinitionDeployer decisionRequirementsDefinitionDeployer = this.getDecisionRequirementsDefinitionDeployer();
            final DecisionDefinitionDeployer decisionDefinitionDeployer = this.getDecisionDefinitionDeployer();
            defaultDeployers.add(decisionRequirementsDefinitionDeployer);
            defaultDeployers.add(decisionDefinitionDeployer);
        }
        return defaultDeployers;
    }

    protected BpmnDeployer getBpmnDeployer() {
        final BpmnDeployer bpmnDeployer = new BpmnDeployer();
        bpmnDeployer.setExpressionManager(this.expressionManager);
        bpmnDeployer.setIdGenerator(this.idGenerator);
        if (this.bpmnParseFactory == null) {
            this.bpmnParseFactory = new DefaultBpmnParseFactory();
        }
        final BpmnParser bpmnParser = new BpmnParser(this.expressionManager, this.bpmnParseFactory);
        if (this.preParseListeners != null) {
            bpmnParser.getParseListeners().addAll(this.preParseListeners);
        }
        bpmnParser.getParseListeners().addAll(this.getDefaultBPMNParseListeners());
        if (this.postParseListeners != null) {
            bpmnParser.getParseListeners().addAll(this.postParseListeners);
        }
        bpmnDeployer.setBpmnParser(bpmnParser);
        return bpmnDeployer;
    }

    protected List<BpmnParseListener> getDefaultBPMNParseListeners() {
        final List<BpmnParseListener> defaultListeners = new ArrayList<BpmnParseListener>();
        if (!HistoryLevel.HISTORY_LEVEL_NONE.equals(this.historyLevel)) {
            defaultListeners.add(new HistoryParseListener(this.historyEventProducer));
        }
        if (this.isMetricsEnabled) {
            defaultListeners.add(new MetricsBpmnParseListener());
        }
        return defaultListeners;
    }

    protected CamundaFormDefinitionDeployer getCamundaFormDeployer() {
        final CamundaFormDefinitionDeployer deployer = new CamundaFormDefinitionDeployer();
        deployer.setIdGenerator(this.idGenerator);
        return deployer;
    }

    protected CmmnDeployer getCmmnDeployer() {
        final CmmnDeployer cmmnDeployer = new CmmnDeployer();
        cmmnDeployer.setIdGenerator(this.idGenerator);
        if (this.cmmnTransformFactory == null) {
            this.cmmnTransformFactory = new DefaultCmmnTransformFactory();
        }
        if (this.cmmnElementHandlerRegistry == null) {
            this.cmmnElementHandlerRegistry = new DefaultCmmnElementHandlerRegistry();
        }
        final CmmnTransformer cmmnTransformer = new CmmnTransformer(this.expressionManager, this.cmmnElementHandlerRegistry, this.cmmnTransformFactory);
        if (this.customPreCmmnTransformListeners != null) {
            cmmnTransformer.getTransformListeners().addAll(this.customPreCmmnTransformListeners);
        }
        cmmnTransformer.getTransformListeners().addAll(this.getDefaultCmmnTransformListeners());
        if (this.customPostCmmnTransformListeners != null) {
            cmmnTransformer.getTransformListeners().addAll(this.customPostCmmnTransformListeners);
        }
        cmmnDeployer.setTransformer(cmmnTransformer);
        return cmmnDeployer;
    }

    protected List<CmmnTransformListener> getDefaultCmmnTransformListeners() {
        final List<CmmnTransformListener> defaultListener = new ArrayList<CmmnTransformListener>();
        if (!HistoryLevel.HISTORY_LEVEL_NONE.equals(this.historyLevel)) {
            defaultListener.add(new CmmnHistoryTransformListener(this.cmmnHistoryEventProducer));
        }
        if (this.isMetricsEnabled) {
            defaultListener.add(new MetricsCmmnTransformListener());
        }
        return defaultListener;
    }

    protected DecisionDefinitionDeployer getDecisionDefinitionDeployer() {
        final DecisionDefinitionDeployer decisionDefinitionDeployer = new DecisionDefinitionDeployer();
        decisionDefinitionDeployer.setIdGenerator(this.idGenerator);
        decisionDefinitionDeployer.setTransformer(this.dmnEngineConfiguration.getTransformer());
        return decisionDefinitionDeployer;
    }

    protected DecisionRequirementsDefinitionDeployer getDecisionRequirementsDefinitionDeployer() {
        final DecisionRequirementsDefinitionDeployer drdDeployer = new DecisionRequirementsDefinitionDeployer();
        drdDeployer.setIdGenerator(this.idGenerator);
        drdDeployer.setTransformer(this.dmnEngineConfiguration.getTransformer());
        return drdDeployer;
    }

    public DmnEngine getDmnEngine() {
        return this.dmnEngine;
    }

    public void setDmnEngine(final DmnEngine dmnEngine) {
        this.dmnEngine = dmnEngine;
    }

    public DefaultDmnEngineConfiguration getDmnEngineConfiguration() {
        return this.dmnEngineConfiguration;
    }

    public void setDmnEngineConfiguration(final DefaultDmnEngineConfiguration dmnEngineConfiguration) {
        this.dmnEngineConfiguration = dmnEngineConfiguration;
    }

    protected void initJobExecutor() {
        if (this.jobExecutor == null) {
            this.jobExecutor = new DefaultJobExecutor();
        }
        this.jobHandlers = new HashMap<String, JobHandler>();
        final TimerExecuteNestedActivityJobHandler timerExecuteNestedActivityJobHandler = new TimerExecuteNestedActivityJobHandler();
        this.jobHandlers.put(timerExecuteNestedActivityJobHandler.getType(), timerExecuteNestedActivityJobHandler);
        final TimerCatchIntermediateEventJobHandler timerCatchIntermediateEvent = new TimerCatchIntermediateEventJobHandler();
        this.jobHandlers.put(timerCatchIntermediateEvent.getType(), timerCatchIntermediateEvent);
        final TimerStartEventJobHandler timerStartEvent = new TimerStartEventJobHandler();
        this.jobHandlers.put(timerStartEvent.getType(), timerStartEvent);
        final TimerStartEventSubprocessJobHandler timerStartEventSubprocess = new TimerStartEventSubprocessJobHandler();
        this.jobHandlers.put(timerStartEventSubprocess.getType(), timerStartEventSubprocess);
        final AsyncContinuationJobHandler asyncContinuationJobHandler = new AsyncContinuationJobHandler();
        this.jobHandlers.put(asyncContinuationJobHandler.getType(), asyncContinuationJobHandler);
        final ProcessEventJobHandler processEventJobHandler = new ProcessEventJobHandler();
        this.jobHandlers.put(processEventJobHandler.getType(), processEventJobHandler);
        final TimerSuspendProcessDefinitionHandler suspendProcessDefinitionHandler = new TimerSuspendProcessDefinitionHandler();
        this.jobHandlers.put(suspendProcessDefinitionHandler.getType(), suspendProcessDefinitionHandler);
        final TimerActivateProcessDefinitionHandler activateProcessDefinitionHandler = new TimerActivateProcessDefinitionHandler();
        this.jobHandlers.put(activateProcessDefinitionHandler.getType(), activateProcessDefinitionHandler);
        final TimerSuspendJobDefinitionHandler suspendJobDefinitionHandler = new TimerSuspendJobDefinitionHandler();
        this.jobHandlers.put(suspendJobDefinitionHandler.getType(), suspendJobDefinitionHandler);
        final TimerActivateJobDefinitionHandler activateJobDefinitionHandler = new TimerActivateJobDefinitionHandler();
        this.jobHandlers.put(activateJobDefinitionHandler.getType(), activateJobDefinitionHandler);
        final TimerTaskListenerJobHandler taskListenerJobHandler = new TimerTaskListenerJobHandler();
        this.jobHandlers.put(taskListenerJobHandler.getType(), taskListenerJobHandler);
        final BatchSeedJobHandler batchSeedJobHandler = new BatchSeedJobHandler();
        this.jobHandlers.put(batchSeedJobHandler.getType(), batchSeedJobHandler);
        final BatchMonitorJobHandler batchMonitorJobHandler = new BatchMonitorJobHandler();
        this.jobHandlers.put(batchMonitorJobHandler.getType(), batchMonitorJobHandler);
        final HistoryCleanupJobHandler historyCleanupJobHandler = new HistoryCleanupJobHandler();
        this.jobHandlers.put(historyCleanupJobHandler.getType(), historyCleanupJobHandler);
        for (final JobHandler batchHandler : this.batchHandlers.values()) {
            this.jobHandlers.put(batchHandler.getType(), batchHandler);
        }
        if (this.getCustomJobHandlers() != null) {
            for (final JobHandler customJobHandler : this.getCustomJobHandlers()) {
                this.jobHandlers.put(customJobHandler.getType(), customJobHandler);
            }
        }
        this.jobExecutor.setAutoActivate(this.jobExecutorActivate);
        if (this.jobExecutor.getRejectedJobsHandler() == null) {
            if (this.customRejectedJobsHandler != null) {
                this.jobExecutor.setRejectedJobsHandler(this.customRejectedJobsHandler);
            } else {
                this.jobExecutor.setRejectedJobsHandler(new NotifyAcquisitionRejectedJobsHandler());
            }
        }
        final long effectiveJobExecutorPriorityRangeMin = (this.jobExecutorPriorityRangeMin == null) ? 0L : this.jobExecutorPriorityRangeMin;
        final long effectiveJobExecutorPriorityRangeMax = (this.jobExecutorPriorityRangeMax == null) ? Long.MAX_VALUE : this.jobExecutorPriorityRangeMax;
        if (effectiveJobExecutorPriorityRangeMin > effectiveJobExecutorPriorityRangeMax) {
            throw ProcessEngineLogger.JOB_EXECUTOR_LOGGER.jobExecutorPriorityRangeException("jobExecutorPriorityRangeMin can not be greater than jobExecutorPriorityRangeMax");
        }
        if (effectiveJobExecutorPriorityRangeMin < 0L) {
            throw ProcessEngineLogger.JOB_EXECUTOR_LOGGER.jobExecutorPriorityRangeException("job executor priority range can not be negative");
        }
        if (effectiveJobExecutorPriorityRangeMin > this.historyCleanupJobPriority || effectiveJobExecutorPriorityRangeMax < this.historyCleanupJobPriority) {
            ProcessEngineLogger.JOB_EXECUTOR_LOGGER.infoJobExecutorDoesNotHandleHistoryCleanupJobs(this);
        }
        if (effectiveJobExecutorPriorityRangeMin > this.batchJobPriority || effectiveJobExecutorPriorityRangeMax < this.batchJobPriority) {
            ProcessEngineLogger.JOB_EXECUTOR_LOGGER.infoJobExecutorDoesNotHandleBatchJobs(this);
        }
    }

    protected void initJobProvider() {
        if (this.producePrioritizedJobs && this.jobPriorityProvider == null) {
            this.jobPriorityProvider = new DefaultJobPriorityProvider();
        }
    }

    protected void initExternalTaskPriorityProvider() {
        if (this.producePrioritizedExternalTasks && this.externalTaskPriorityProvider == null) {
            this.externalTaskPriorityProvider = new DefaultExternalTaskPriorityProvider();
        }
    }

    public void initHistoryLevel() {
        if (this.historyLevel != null) {
            this.setHistory(this.historyLevel.getName());
        }
        if (this.historyLevels == null) {
            (this.historyLevels = new ArrayList<HistoryLevel>()).add(HistoryLevel.HISTORY_LEVEL_NONE);
            this.historyLevels.add(HistoryLevel.HISTORY_LEVEL_ACTIVITY);
            this.historyLevels.add(HistoryLevel.HISTORY_LEVEL_AUDIT);
            this.historyLevels.add(HistoryLevel.HISTORY_LEVEL_FULL);
        }
        if (this.customHistoryLevels != null) {
            this.historyLevels.addAll(this.customHistoryLevels);
        }
        if ("variable".equalsIgnoreCase(this.history)) {
            this.historyLevel = HistoryLevel.HISTORY_LEVEL_ACTIVITY;
            ProcessEngineConfigurationImpl.LOG.usingDeprecatedHistoryLevelVariable();
        } else {
            for (final HistoryLevel historyLevel : this.historyLevels) {
                if (historyLevel.getName().equalsIgnoreCase(this.history)) {
                    this.historyLevel = historyLevel;
                }
            }
        }
        if (this.historyLevel == null && !"auto".equalsIgnoreCase(this.history)) {
            throw new ProcessEngineException("invalid history level: " + this.history);
        }
    }

    protected void initIdGenerator() {
        if (this.idGenerator == null) {
            CommandExecutor idGeneratorCommandExecutor = null;
            if (this.idGeneratorDataSource != null) {
                final ProcessEngineConfigurationImpl processEngineConfiguration = new StandaloneProcessEngineConfiguration();
                processEngineConfiguration.setDataSource(this.idGeneratorDataSource);
                processEngineConfiguration.setDatabaseSchemaUpdate("false");
                processEngineConfiguration.init();
                idGeneratorCommandExecutor = processEngineConfiguration.getCommandExecutorTxRequiresNew();
            } else if (this.idGeneratorDataSourceJndiName != null) {
                final ProcessEngineConfigurationImpl processEngineConfiguration = new StandaloneProcessEngineConfiguration();
                processEngineConfiguration.setDataSourceJndiName(this.idGeneratorDataSourceJndiName);
                processEngineConfiguration.setDatabaseSchemaUpdate("false");
                processEngineConfiguration.init();
                idGeneratorCommandExecutor = processEngineConfiguration.getCommandExecutorTxRequiresNew();
            } else {
                idGeneratorCommandExecutor = this.commandExecutorTxRequiresNew;
            }
            final DbIdGenerator dbIdGenerator = new DbIdGenerator();
            dbIdGenerator.setIdBlockSize(this.idBlockSize);
            dbIdGenerator.setCommandExecutor(idGeneratorCommandExecutor);
            this.idGenerator = dbIdGenerator;
        }
    }

    protected void initCommandContextFactory() {
        if (this.commandContextFactory == null) {
            (this.commandContextFactory = new CommandContextFactory()).setProcessEngineConfiguration(this);
        }
    }

    protected void initTransactionContextFactory() {
        if (this.transactionContextFactory == null) {
            this.transactionContextFactory = new StandaloneTransactionContextFactory();
        }
    }

    protected void initValueTypeResolver() {
        if (this.valueTypeResolver == null) {
            this.valueTypeResolver = (ValueTypeResolver) new ValueTypeResolverImpl();
        }
    }

    protected void initDefaultCharset() {
        if (this.defaultCharset == null) {
            if (this.defaultCharsetName == null) {
                this.defaultCharsetName = "UTF-8";
            }
            this.defaultCharset = Charset.forName(this.defaultCharsetName);
        }
    }

    protected void initMetrics() {
        if (this.isMetricsEnabled) {
            if (this.metricsRegistry == null) {
                this.metricsRegistry = new MetricsRegistry();
            }
            this.initDefaultMetrics(this.metricsRegistry);
            if (this.dbMetricsReporter == null) {
                this.dbMetricsReporter = new DbMetricsReporter(this.metricsRegistry, this.commandExecutorTxRequired);
            }
        }
    }

    protected void initHostName() {
        if (this.hostname == null) {
            if (this.hostnameProvider == null) {
                this.hostnameProvider = new SimpleIpBasedProvider();
            }
            this.hostname = this.hostnameProvider.getHostname(this);
        }
    }

    protected void initDefaultMetrics(final MetricsRegistry metricsRegistry) {
        metricsRegistry.createMeter("activity-instance-start");
        metricsRegistry.createDbMeter("activity-instance-end");
        metricsRegistry.createDbMeter("job-acquisition-attempt");
        metricsRegistry.createDbMeter("job-acquired-success");
        metricsRegistry.createDbMeter("job-acquired-failure");
        metricsRegistry.createDbMeter("job-successful");
        metricsRegistry.createDbMeter("job-failed");
        metricsRegistry.createDbMeter("job-locked-exclusive");
        metricsRegistry.createDbMeter("job-execution-rejected");
        metricsRegistry.createMeter("root-process-instance-start");
        metricsRegistry.createMeter("executed-decision-instances");
        metricsRegistry.createMeter("executed-decision-elements");
    }

    protected void initSerialization() {
        if (this.variableSerializers == null) {
            this.variableSerializers = new DefaultVariableSerializers();
            if (this.customPreVariableSerializers != null) {
                for (final TypedValueSerializer<?> customVariableType : this.customPreVariableSerializers) {
                    this.variableSerializers.addSerializer(customVariableType);
                }
            }
            this.variableSerializers.addSerializer(new NullValueSerializer());
            this.variableSerializers.addSerializer(new StringValueSerializer());
            this.variableSerializers.addSerializer(new BooleanValueSerializer());
            this.variableSerializers.addSerializer(new ShortValueSerializer());
            this.variableSerializers.addSerializer(new IntegerValueSerializer());
            this.variableSerializers.addSerializer(new LongValueSerlializer());
            this.variableSerializers.addSerializer(new DateValueSerializer());
            this.variableSerializers.addSerializer(new DoubleValueSerializer());
            this.variableSerializers.addSerializer(new ByteArrayValueSerializer());
            this.variableSerializers.addSerializer(new JavaObjectSerializer());
            this.variableSerializers.addSerializer(new FileValueSerializer());
            if (this.customPostVariableSerializers != null) {
                for (final TypedValueSerializer<?> customVariableType : this.customPostVariableSerializers) {
                    this.variableSerializers.addSerializer(customVariableType);
                }
            }
        }
    }

    protected void initFormEngines() {
        if (this.formEngines == null) {
            this.formEngines = new HashMap<String, FormEngine>();
            final FormEngine defaultFormEngine = new HtmlFormEngine();
            this.formEngines.put(null, defaultFormEngine);
            this.formEngines.put(defaultFormEngine.getName(), defaultFormEngine);
            final FormEngine juelFormEngine = new JuelFormEngine();
            this.formEngines.put(juelFormEngine.getName(), juelFormEngine);
        }
        if (this.customFormEngines != null) {
            for (final FormEngine formEngine : this.customFormEngines) {
                this.formEngines.put(formEngine.getName(), formEngine);
            }
        }
    }

    protected void initFormTypes() {
        if (this.formTypes == null) {
            (this.formTypes = new FormTypes()).addFormType(new StringFormType());
            this.formTypes.addFormType(new LongFormType());
            this.formTypes.addFormType(new DateFormType("dd/MM/yyyy"));
            this.formTypes.addFormType(new BooleanFormType());
        }
        if (this.customFormTypes != null) {
            for (final AbstractFormFieldType customFormType : this.customFormTypes) {
                this.formTypes.addFormType(customFormType);
            }
        }
    }

    protected void initFormFieldValidators() {
        if (this.formValidators == null) {
            (this.formValidators = new FormValidators()).addValidator("min", MinValidator.class);
            this.formValidators.addValidator("max", MaxValidator.class);
            this.formValidators.addValidator("minlength", MinLengthValidator.class);
            this.formValidators.addValidator("maxlength", MaxLengthValidator.class);
            this.formValidators.addValidator("required", RequiredValidator.class);
            this.formValidators.addValidator("readonly", ReadOnlyValidator.class);
        }
        if (this.customFormFieldValidators != null) {
            for (final Map.Entry<String, Class<? extends FormFieldValidator>> validator : this.customFormFieldValidators.entrySet()) {
                this.formValidators.addValidator(validator.getKey(), validator.getValue());
            }
        }
    }

    protected void initScripting() {
        if (this.resolverFactories == null) {
            (this.resolverFactories = new ArrayList<ResolverFactory>()).add(new MocksResolverFactory());
            this.resolverFactories.add(new VariableScopeResolverFactory());
            this.resolverFactories.add(new BeansResolverFactory());
        }
        if (this.scriptEngineResolver == null) {
            this.scriptEngineResolver = new DefaultScriptEngineResolver(new ScriptEngineManager());
        }
        if (this.scriptingEngines == null) {
            (this.scriptingEngines = new ScriptingEngines(new ScriptBindingsFactory(this.resolverFactories), this.scriptEngineResolver)).setEnableScriptEngineCaching(this.enableScriptEngineCaching);
        }
        if (this.scriptFactory == null) {
            this.scriptFactory = new ScriptFactory();
        }
        if (this.scriptEnvResolvers == null) {
            this.scriptEnvResolvers = new ArrayList<ScriptEnvResolver>();
        }
        if (this.scriptingEnvironment == null) {
            this.scriptingEnvironment = new ScriptingEnvironment(this.scriptFactory, this.scriptEnvResolvers, this.scriptingEngines);
        }
    }

    protected void initDmnEngine() {
        if (this.dmnEngine == null) {
            if (this.dmnEngineConfiguration == null) {
                this.dmnEngineConfiguration = (DefaultDmnEngineConfiguration) DmnEngineConfiguration.createDefaultDmnEngineConfiguration();
            }
            this.dmnEngineConfiguration = new DmnEngineConfigurationBuilder(this.dmnEngineConfiguration).dmnHistoryEventProducer(this.dmnHistoryEventProducer).scriptEngineResolver((DmnScriptEngineResolver) this.scriptingEngines).expressionManager(this.expressionManager).feelCustomFunctionProviders(this.dmnFeelCustomFunctionProviders).enableFeelLegacyBehavior(this.dmnFeelEnableLegacyBehavior).build();
            this.dmnEngine = this.dmnEngineConfiguration.buildEngine();
        } else if (this.dmnEngineConfiguration == null) {
            this.dmnEngineConfiguration = (DefaultDmnEngineConfiguration) this.dmnEngine.getConfiguration();
        }
    }

    protected void initExpressionManager() {
        if (this.expressionManager == null) {
            this.expressionManager = new ExpressionManager(this.beans);
        }
        this.expressionManager.addFunctionMapper(new CommandContextFunctionMapper());
        this.expressionManager.addFunctionMapper(new DateTimeFunctionMapper());
    }

    protected void initBusinessCalendarManager() {
        if (this.businessCalendarManager == null) {
            final MapBusinessCalendarManager mapBusinessCalendarManager = new MapBusinessCalendarManager();
            mapBusinessCalendarManager.addBusinessCalendar(DurationBusinessCalendar.NAME, new DurationBusinessCalendar());
            mapBusinessCalendarManager.addBusinessCalendar("dueDate", new DueDateBusinessCalendar());
            mapBusinessCalendarManager.addBusinessCalendar(CycleBusinessCalendar.NAME, new CycleBusinessCalendar());
            this.businessCalendarManager = mapBusinessCalendarManager;
        }
    }

    protected void initDelegateInterceptor() {
        if (this.delegateInterceptor == null) {
            this.delegateInterceptor = new DefaultDelegateInterceptor();
        }
    }

    protected void initEventHandlers() {
        if (this.eventHandlers == null) {
            this.eventHandlers = new HashMap<String, EventHandler>();
            final SignalEventHandler signalEventHander = new SignalEventHandler();
            this.eventHandlers.put(signalEventHander.getEventHandlerType(), signalEventHander);
            final CompensationEventHandler compensationEventHandler = new CompensationEventHandler();
            this.eventHandlers.put(compensationEventHandler.getEventHandlerType(), compensationEventHandler);
            final EventHandler messageEventHandler = new EventHandlerImpl(EventType.MESSAGE);
            this.eventHandlers.put(messageEventHandler.getEventHandlerType(), messageEventHandler);
            final EventHandler conditionalEventHandler = new ConditionalEventHandler();
            this.eventHandlers.put(conditionalEventHandler.getEventHandlerType(), conditionalEventHandler);
        }
        if (this.customEventHandlers != null) {
            for (final EventHandler eventHandler : this.customEventHandlers) {
                this.eventHandlers.put(eventHandler.getEventHandlerType(), eventHandler);
            }
        }
    }

    protected void initCommandCheckers() {
        if (this.commandCheckers == null) {
            (this.commandCheckers = new ArrayList<CommandChecker>()).add(new TenantCommandChecker());
            this.commandCheckers.add(new AuthorizationCommandChecker());
        }
    }

    protected void initJpa() {
        if (this.jpaPersistenceUnitName != null) {
            this.jpaEntityManagerFactory = JpaHelper.createEntityManagerFactory(this.jpaPersistenceUnitName);
        }
        if (this.jpaEntityManagerFactory != null) {
            this.sessionFactories.put(EntityManagerSession.class, new EntityManagerSessionFactory(this.jpaEntityManagerFactory, this.jpaHandleTransaction, this.jpaCloseEntityManager));
            final JPAVariableSerializer jpaType = (JPAVariableSerializer) this.variableSerializers.getSerializerByName("jpa");
            if (jpaType == null) {
                final int serializableIndex = this.variableSerializers.getSerializerIndexByName(ValueType.BYTES.getName());
                if (serializableIndex > -1) {
                    this.variableSerializers.addSerializer(new JPAVariableSerializer(), serializableIndex);
                } else {
                    this.variableSerializers.addSerializer(new JPAVariableSerializer());
                }
            }
        }
    }

    protected void initBeans() {
        if (this.beans == null) {
            this.beans = ProcessEngineConfigurationImpl.DEFAULT_BEANS_MAP;
        }
    }

    protected void initArtifactFactory() {
        if (this.artifactFactory == null) {
            this.artifactFactory = new DefaultArtifactFactory();
        }
    }

    protected void initProcessApplicationManager() {
        if (this.processApplicationManager == null) {
            this.processApplicationManager = new ProcessApplicationManager();
        }
    }

    protected void initCorrelationHandler() {
        if (this.correlationHandler == null) {
            this.correlationHandler = new DefaultCorrelationHandler();
        }
    }

    protected void initConditionHandler() {
        if (this.conditionHandler == null) {
            this.conditionHandler = new DefaultConditionHandler();
        }
    }

    protected void initDeploymentHandlerFactory() {
        if (this.deploymentHandlerFactory == null) {
            this.deploymentHandlerFactory = new DefaultDeploymentHandlerFactory();
        }
    }

    protected void initHistoryEventProducer() {
        if (this.historyEventProducer == null) {
            this.historyEventProducer = new CacheAwareHistoryEventProducer();
        }
    }

    protected void initCmmnHistoryEventProducer() {
        if (this.cmmnHistoryEventProducer == null) {
            this.cmmnHistoryEventProducer = new CacheAwareCmmnHistoryEventProducer();
        }
    }

    protected void initDmnHistoryEventProducer() {
        if (this.dmnHistoryEventProducer == null) {
            this.dmnHistoryEventProducer = new DefaultDmnHistoryEventProducer();
        }
    }

    protected void initHistoryEventHandler() {
        if (this.historyEventHandler == null) {
            if (this.enableDefaultDbHistoryEventHandler) {
                this.historyEventHandler = new CompositeDbHistoryEventHandler(this.customHistoryEventHandlers);
            } else {
                this.historyEventHandler = new CompositeHistoryEventHandler(this.customHistoryEventHandlers);
            }
        }
    }

    protected void initPasswordDigest() {
        if (this.saltGenerator == null) {
            this.saltGenerator = new Default16ByteSaltGenerator();
        }
        if (this.passwordEncryptor == null) {
            this.passwordEncryptor = new Sha512HashDigest();
        }
        if (this.customPasswordChecker == null) {
            this.customPasswordChecker = Collections.emptyList();
        }
        if (this.passwordManager == null) {
            this.passwordManager = new PasswordManager(this.passwordEncryptor, this.customPasswordChecker);
        }
    }

    public void initPasswordPolicy() {
        if (this.passwordPolicy == null && this.enablePasswordPolicy) {
            this.passwordPolicy = new DefaultPasswordPolicyImpl();
        }
    }

    protected void initDeploymentRegistration() {
        if (this.registeredDeployments == null) {
            this.registeredDeployments = new CopyOnWriteArraySet<String>();
        }
    }

    protected void initCacheFactory() {
        if (this.cacheFactory == null) {
            this.cacheFactory = new DefaultCacheFactory();
        }
    }

    protected void initResourceAuthorizationProvider() {
        if (this.resourceAuthorizationProvider == null) {
            this.resourceAuthorizationProvider = new DefaultAuthorizationProvider();
        }
    }

    protected void initPermissionProvider() {
        if (this.permissionProvider == null) {
            this.permissionProvider = new DefaultPermissionProvider();
        }
    }

    protected void initDefaultUserPermissionForTask() {
        if (this.defaultUserPermissionForTask == null) {
            if (Permissions.UPDATE.getName().equals(this.defaultUserPermissionNameForTask)) {
                this.defaultUserPermissionForTask = Permissions.UPDATE;
            } else {
                if (!Permissions.TASK_WORK.getName().equals(this.defaultUserPermissionNameForTask)) {
                    throw ProcessEngineConfigurationImpl.LOG.invalidConfigDefaultUserPermissionNameForTask(this.defaultUserPermissionNameForTask, new String[]{Permissions.UPDATE.getName(), Permissions.TASK_WORK.getName()});
                }
                this.defaultUserPermissionForTask = Permissions.TASK_WORK;
            }
        }
    }

    protected void initAdminUser() {
        if (this.adminUsers == null) {
            this.adminUsers = new ArrayList<String>();
        }
    }

    protected void initAdminGroups() {
        if (this.adminGroups == null) {
            this.adminGroups = new ArrayList<String>();
        }
        if (this.adminGroups.isEmpty() || !this.adminGroups.contains("camunda-admin")) {
            this.adminGroups.add("camunda-admin");
        }
    }

    protected void initTelemetry() {
        if (this.telemetryRegistry == null) {
            this.telemetryRegistry = new TelemetryRegistry();
        }
        if (this.telemetryData == null) {
            this.initTelemetryData();
        }
        try {
            if (this.telemetryHttpConnector == null) {
                this.telemetryHttpConnector = (Connector<? extends ConnectorRequest<?>>) Connectors.getConnector(Connectors.HTTP_CONNECTOR_ID);
            }
        } catch (Exception e) {
            ProcessEngineLogger.TELEMETRY_LOGGER.unexpectedExceptionDuringHttpConnectorConfiguration(e);
        }
        if (this.telemetryHttpConnector == null) {
            ProcessEngineLogger.TELEMETRY_LOGGER.unableToConfigureHttpConnectorWarning();
        } else if (this.telemetryReporter == null) {
            this.telemetryReporter = new TelemetryReporter(this.commandExecutorTxRequired, this.telemetryEndpoint, this.telemetryRequestRetries, this.telemetryReportingPeriod, this.telemetryData, this.telemetryHttpConnector, this.telemetryRegistry, this.metricsRegistry, this.telemetryRequestTimeout);
        }
    }

    protected void initTelemetryData() {
        final DatabaseImpl database = new DatabaseImpl(this.databaseVendor, this.databaseVersion);
        final JdkImpl jdk = ParseUtil.parseJdkDetails();
        final InternalsImpl internals = new InternalsImpl(database, this.telemetryRegistry.getApplicationServer(), this.telemetryRegistry.getLicenseKey(), jdk);
        final String camundaIntegration = this.telemetryRegistry.getCamundaIntegration();
        if (camundaIntegration != null && !camundaIntegration.isEmpty()) {
            internals.getCamundaIntegration().add(camundaIntegration);
        }
        final ProcessEngineDetails engineInfo = ParseUtil.parseProcessEngineVersion(true);
        final ProductImpl product = new ProductImpl("Camunda BPM Runtime", engineInfo.getVersion(), engineInfo.getEdition(), internals);
        this.telemetryData = new TelemetryDataImpl(null, product);
    }

    @Override
    public String getProcessEngineName() {
        return this.processEngineName;
    }

    public HistoryLevel getHistoryLevel() {
        return this.historyLevel;
    }

    public void setHistoryLevel(final HistoryLevel historyLevel) {
        this.historyLevel = historyLevel;
    }

    public HistoryLevel getDefaultHistoryLevel() {
        if (this.historyLevels != null) {
            for (final HistoryLevel historyLevel : this.historyLevels) {
                if ("audit" != null && "audit".equalsIgnoreCase(historyLevel.getName())) {
                    return historyLevel;
                }
            }
        }
        return null;
    }

    @Override
    public ProcessEngineConfigurationImpl setProcessEngineName(final String processEngineName) {
        this.processEngineName = processEngineName;
        return this;
    }

    public List<CommandInterceptor> getCustomPreCommandInterceptorsTxRequired() {
        return this.customPreCommandInterceptorsTxRequired;
    }

    public ProcessEngineConfigurationImpl setCustomPreCommandInterceptorsTxRequired(final List<CommandInterceptor> customPreCommandInterceptorsTxRequired) {
        this.customPreCommandInterceptorsTxRequired = customPreCommandInterceptorsTxRequired;
        return this;
    }

    public List<CommandInterceptor> getCustomPostCommandInterceptorsTxRequired() {
        return this.customPostCommandInterceptorsTxRequired;
    }

    public ProcessEngineConfigurationImpl setCustomPostCommandInterceptorsTxRequired(final List<CommandInterceptor> customPostCommandInterceptorsTxRequired) {
        this.customPostCommandInterceptorsTxRequired = customPostCommandInterceptorsTxRequired;
        return this;
    }

    public List<CommandInterceptor> getCommandInterceptorsTxRequired() {
        return this.commandInterceptorsTxRequired;
    }

    public ProcessEngineConfigurationImpl setCommandInterceptorsTxRequired(final List<CommandInterceptor> commandInterceptorsTxRequired) {
        this.commandInterceptorsTxRequired = commandInterceptorsTxRequired;
        return this;
    }

    public CommandExecutor getCommandExecutorTxRequired() {
        return this.commandExecutorTxRequired;
    }

    public ProcessEngineConfigurationImpl setCommandExecutorTxRequired(final CommandExecutor commandExecutorTxRequired) {
        this.commandExecutorTxRequired = commandExecutorTxRequired;
        return this;
    }

    public List<CommandInterceptor> getCustomPreCommandInterceptorsTxRequiresNew() {
        return this.customPreCommandInterceptorsTxRequiresNew;
    }

    public ProcessEngineConfigurationImpl setCustomPreCommandInterceptorsTxRequiresNew(final List<CommandInterceptor> customPreCommandInterceptorsTxRequiresNew) {
        this.customPreCommandInterceptorsTxRequiresNew = customPreCommandInterceptorsTxRequiresNew;
        return this;
    }

    public List<CommandInterceptor> getCustomPostCommandInterceptorsTxRequiresNew() {
        return this.customPostCommandInterceptorsTxRequiresNew;
    }

    public ProcessEngineConfigurationImpl setCustomPostCommandInterceptorsTxRequiresNew(final List<CommandInterceptor> customPostCommandInterceptorsTxRequiresNew) {
        this.customPostCommandInterceptorsTxRequiresNew = customPostCommandInterceptorsTxRequiresNew;
        return this;
    }

    public List<CommandInterceptor> getCommandInterceptorsTxRequiresNew() {
        return this.commandInterceptorsTxRequiresNew;
    }

    public ProcessEngineConfigurationImpl setCommandInterceptorsTxRequiresNew(final List<CommandInterceptor> commandInterceptorsTxRequiresNew) {
        this.commandInterceptorsTxRequiresNew = commandInterceptorsTxRequiresNew;
        return this;
    }

    public CommandExecutor getCommandExecutorTxRequiresNew() {
        return this.commandExecutorTxRequiresNew;
    }

    public ProcessEngineConfigurationImpl setCommandExecutorTxRequiresNew(final CommandExecutor commandExecutorTxRequiresNew) {
        this.commandExecutorTxRequiresNew = commandExecutorTxRequiresNew;
        return this;
    }

    public RepositoryService getRepositoryService() {
        return this.repositoryService;
    }

    public ProcessEngineConfigurationImpl setRepositoryService(final RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
        return this;
    }

    public RuntimeService getRuntimeService() {
        return this.runtimeService;
    }

    public ProcessEngineConfigurationImpl setRuntimeService(final RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
        return this;
    }

    public HistoryService getHistoryService() {
        return this.historyService;
    }

    public ProcessEngineConfigurationImpl setHistoryService(final HistoryService historyService) {
        this.historyService = historyService;
        return this;
    }

    public IdentityService getIdentityService() {
        return this.identityService;
    }

    public ProcessEngineConfigurationImpl setIdentityService(final IdentityService identityService) {
        this.identityService = identityService;
        return this;
    }

    public TaskService getTaskService() {
        return this.taskService;
    }

    public ProcessEngineConfigurationImpl setTaskService(final TaskService taskService) {
        this.taskService = taskService;
        return this;
    }

    public FormService getFormService() {
        return this.formService;
    }

    public ProcessEngineConfigurationImpl setFormService(final FormService formService) {
        this.formService = formService;
        return this;
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

    public ProcessEngineConfigurationImpl setManagementService(final ManagementService managementService) {
        this.managementService = managementService;
        return this;
    }

    public CaseService getCaseService() {
        return this.caseService;
    }

    public void setCaseService(final CaseService caseService) {
        this.caseService = caseService;
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

    public OptimizeService getOptimizeService() {
        return this.optimizeService;
    }

    public Map<Class<?>, SessionFactory> getSessionFactories() {
        return this.sessionFactories;
    }

    public ProcessEngineConfigurationImpl setSessionFactories(final Map<Class<?>, SessionFactory> sessionFactories) {
        this.sessionFactories = sessionFactories;
        return this;
    }

    public List<Deployer> getDeployers() {
        return this.deployers;
    }

    public ProcessEngineConfigurationImpl setDeployers(final List<Deployer> deployers) {
        this.deployers = deployers;
        return this;
    }

    public JobExecutor getJobExecutor() {
        return this.jobExecutor;
    }

    public ProcessEngineConfigurationImpl setJobExecutor(final JobExecutor jobExecutor) {
        this.jobExecutor = jobExecutor;
        return this;
    }

    public PriorityProvider<JobDeclaration<?, ?>> getJobPriorityProvider() {
        return this.jobPriorityProvider;
    }

    public void setJobPriorityProvider(final PriorityProvider<JobDeclaration<?, ?>> jobPriorityProvider) {
        this.jobPriorityProvider = jobPriorityProvider;
    }

    public Long getJobExecutorPriorityRangeMin() {
        return this.jobExecutorPriorityRangeMin;
    }

    public ProcessEngineConfigurationImpl setJobExecutorPriorityRangeMin(final Long jobExecutorPriorityRangeMin) {
        this.jobExecutorPriorityRangeMin = jobExecutorPriorityRangeMin;
        return this;
    }

    public Long getJobExecutorPriorityRangeMax() {
        return this.jobExecutorPriorityRangeMax;
    }

    public ProcessEngineConfigurationImpl setJobExecutorPriorityRangeMax(final Long jobExecutorPriorityRangeMax) {
        this.jobExecutorPriorityRangeMax = jobExecutorPriorityRangeMax;
        return this;
    }

    public PriorityProvider<ExternalTaskActivityBehavior> getExternalTaskPriorityProvider() {
        return this.externalTaskPriorityProvider;
    }

    public void setExternalTaskPriorityProvider(final PriorityProvider<ExternalTaskActivityBehavior> externalTaskPriorityProvider) {
        this.externalTaskPriorityProvider = externalTaskPriorityProvider;
    }

    public IdGenerator getIdGenerator() {
        return this.idGenerator;
    }

    public ProcessEngineConfigurationImpl setIdGenerator(final IdGenerator idGenerator) {
        this.idGenerator = idGenerator;
        return this;
    }

    public String getWsSyncFactoryClassName() {
        return this.wsSyncFactoryClassName;
    }

    public ProcessEngineConfigurationImpl setWsSyncFactoryClassName(final String wsSyncFactoryClassName) {
        this.wsSyncFactoryClassName = wsSyncFactoryClassName;
        return this;
    }

    public Map<String, FormEngine> getFormEngines() {
        return this.formEngines;
    }

    public ProcessEngineConfigurationImpl setFormEngines(final Map<String, FormEngine> formEngines) {
        this.formEngines = formEngines;
        return this;
    }

    public FormTypes getFormTypes() {
        return this.formTypes;
    }

    public ProcessEngineConfigurationImpl setFormTypes(final FormTypes formTypes) {
        this.formTypes = formTypes;
        return this;
    }

    public ScriptingEngines getScriptingEngines() {
        return this.scriptingEngines;
    }

    public ProcessEngineConfigurationImpl setScriptingEngines(final ScriptingEngines scriptingEngines) {
        this.scriptingEngines = scriptingEngines;
        return this;
    }

    public VariableSerializers getVariableSerializers() {
        return this.variableSerializers;
    }

    public VariableSerializerFactory getFallbackSerializerFactory() {
        return this.fallbackSerializerFactory;
    }

    public void setFallbackSerializerFactory(final VariableSerializerFactory fallbackSerializerFactory) {
        this.fallbackSerializerFactory = fallbackSerializerFactory;
    }

    public ProcessEngineConfigurationImpl setVariableTypes(final VariableSerializers variableSerializers) {
        this.variableSerializers = variableSerializers;
        return this;
    }

    public ExpressionManager getExpressionManager() {
        return this.expressionManager;
    }

    public ProcessEngineConfigurationImpl setExpressionManager(final ExpressionManager expressionManager) {
        this.expressionManager = expressionManager;
        return this;
    }

    public BusinessCalendarManager getBusinessCalendarManager() {
        return this.businessCalendarManager;
    }

    public ProcessEngineConfigurationImpl setBusinessCalendarManager(final BusinessCalendarManager businessCalendarManager) {
        this.businessCalendarManager = businessCalendarManager;
        return this;
    }

    public CommandContextFactory getCommandContextFactory() {
        return this.commandContextFactory;
    }

    public ProcessEngineConfigurationImpl setCommandContextFactory(final CommandContextFactory commandContextFactory) {
        this.commandContextFactory = commandContextFactory;
        return this;
    }

    public TransactionContextFactory getTransactionContextFactory() {
        return this.transactionContextFactory;
    }

    public ProcessEngineConfigurationImpl setTransactionContextFactory(final TransactionContextFactory transactionContextFactory) {
        this.transactionContextFactory = transactionContextFactory;
        return this;
    }

    public BpmnParseFactory getBpmnParseFactory() {
        return this.bpmnParseFactory;
    }

    public ProcessEngineConfigurationImpl setBpmnParseFactory(final BpmnParseFactory bpmnParseFactory) {
        this.bpmnParseFactory = bpmnParseFactory;
        return this;
    }

    public List<Deployer> getCustomPreDeployers() {
        return this.customPreDeployers;
    }

    public ProcessEngineConfigurationImpl setCustomPreDeployers(final List<Deployer> customPreDeployers) {
        this.customPreDeployers = customPreDeployers;
        return this;
    }

    public List<Deployer> getCustomPostDeployers() {
        return this.customPostDeployers;
    }

    public ProcessEngineConfigurationImpl setCustomPostDeployers(final List<Deployer> customPostDeployers) {
        this.customPostDeployers = customPostDeployers;
        return this;
    }

    public void setCacheFactory(final CacheFactory cacheFactory) {
        this.cacheFactory = cacheFactory;
    }

    public void setCacheCapacity(final int cacheCapacity) {
        this.cacheCapacity = cacheCapacity;
    }

    public void setEnableFetchProcessDefinitionDescription(final boolean enableFetchProcessDefinitionDescription) {
        this.enableFetchProcessDefinitionDescription = enableFetchProcessDefinitionDescription;
    }

    public boolean getEnableFetchProcessDefinitionDescription() {
        return this.enableFetchProcessDefinitionDescription;
    }

    public Permission getDefaultUserPermissionForTask() {
        return this.defaultUserPermissionForTask;
    }

    public ProcessEngineConfigurationImpl setDefaultUserPermissionForTask(final Permission defaultUserPermissionForTask) {
        this.defaultUserPermissionForTask = defaultUserPermissionForTask;
        return this;
    }

    public ProcessEngineConfigurationImpl setEnableHistoricInstancePermissions(final boolean enable) {
        this.enableHistoricInstancePermissions = enable;
        return this;
    }

    public boolean isEnableHistoricInstancePermissions() {
        return this.enableHistoricInstancePermissions;
    }

    public Map<String, JobHandler> getJobHandlers() {
        return this.jobHandlers;
    }

    public ProcessEngineConfigurationImpl setJobHandlers(final Map<String, JobHandler> jobHandlers) {
        this.jobHandlers = jobHandlers;
        return this;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return this.sqlSessionFactory;
    }

    public ProcessEngineConfigurationImpl setSqlSessionFactory(final SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        return this;
    }

    public DbSqlSessionFactory getDbSqlSessionFactory() {
        return this.dbSqlSessionFactory;
    }

    public ProcessEngineConfigurationImpl setDbSqlSessionFactory(final DbSqlSessionFactory dbSqlSessionFactory) {
        this.dbSqlSessionFactory = dbSqlSessionFactory;
        return this;
    }

    public TransactionFactory getTransactionFactory() {
        return this.transactionFactory;
    }

    public ProcessEngineConfigurationImpl setTransactionFactory(final TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
        return this;
    }

    public List<SessionFactory> getCustomSessionFactories() {
        return this.customSessionFactories;
    }

    public ProcessEngineConfigurationImpl setCustomSessionFactories(final List<SessionFactory> customSessionFactories) {
        this.customSessionFactories = customSessionFactories;
        return this;
    }

    public List<JobHandler> getCustomJobHandlers() {
        return this.customJobHandlers;
    }

    public ProcessEngineConfigurationImpl setCustomJobHandlers(final List<JobHandler> customJobHandlers) {
        this.customJobHandlers = customJobHandlers;
        return this;
    }

    public List<FormEngine> getCustomFormEngines() {
        return this.customFormEngines;
    }

    public ProcessEngineConfigurationImpl setCustomFormEngines(final List<FormEngine> customFormEngines) {
        this.customFormEngines = customFormEngines;
        return this;
    }

    public List<AbstractFormFieldType> getCustomFormTypes() {
        return this.customFormTypes;
    }

    public ProcessEngineConfigurationImpl setCustomFormTypes(final List<AbstractFormFieldType> customFormTypes) {
        this.customFormTypes = customFormTypes;
        return this;
    }

    public List<TypedValueSerializer> getCustomPreVariableSerializers() {
        return this.customPreVariableSerializers;
    }

    public ProcessEngineConfigurationImpl setCustomPreVariableSerializers(final List<TypedValueSerializer> customPreVariableTypes) {
        this.customPreVariableSerializers = customPreVariableTypes;
        return this;
    }

    public List<TypedValueSerializer> getCustomPostVariableSerializers() {
        return this.customPostVariableSerializers;
    }

    public ProcessEngineConfigurationImpl setCustomPostVariableSerializers(final List<TypedValueSerializer> customPostVariableTypes) {
        this.customPostVariableSerializers = customPostVariableTypes;
        return this;
    }

    public List<BpmnParseListener> getCustomPreBPMNParseListeners() {
        return this.preParseListeners;
    }

    public void setCustomPreBPMNParseListeners(final List<BpmnParseListener> preParseListeners) {
        this.preParseListeners = preParseListeners;
    }

    public List<BpmnParseListener> getCustomPostBPMNParseListeners() {
        return this.postParseListeners;
    }

    public void setCustomPostBPMNParseListeners(final List<BpmnParseListener> postParseListeners) {
        this.postParseListeners = postParseListeners;
    }

    @Deprecated
    public List<BpmnParseListener> getPreParseListeners() {
        return this.preParseListeners;
    }

    @Deprecated
    public void setPreParseListeners(final List<BpmnParseListener> preParseListeners) {
        this.preParseListeners = preParseListeners;
    }

    @Deprecated
    public List<BpmnParseListener> getPostParseListeners() {
        return this.postParseListeners;
    }

    @Deprecated
    public void setPostParseListeners(final List<BpmnParseListener> postParseListeners) {
        this.postParseListeners = postParseListeners;
    }

    public List<CmmnTransformListener> getCustomPreCmmnTransformListeners() {
        return this.customPreCmmnTransformListeners;
    }

    public void setCustomPreCmmnTransformListeners(final List<CmmnTransformListener> customPreCmmnTransformListeners) {
        this.customPreCmmnTransformListeners = customPreCmmnTransformListeners;
    }

    public List<CmmnTransformListener> getCustomPostCmmnTransformListeners() {
        return this.customPostCmmnTransformListeners;
    }

    public void setCustomPostCmmnTransformListeners(final List<CmmnTransformListener> customPostCmmnTransformListeners) {
        this.customPostCmmnTransformListeners = customPostCmmnTransformListeners;
    }

    public Map<Object, Object> getBeans() {
        return this.beans;
    }

    public void setBeans(final Map<Object, Object> beans) {
        this.beans = beans;
    }

    @Override
    public ProcessEngineConfigurationImpl setClassLoader(final ClassLoader classLoader) {
        super.setClassLoader(classLoader);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setDatabaseType(final String databaseType) {
        super.setDatabaseType(databaseType);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setDataSource(final DataSource dataSource) {
        super.setDataSource(dataSource);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setDatabaseSchemaUpdate(final String databaseSchemaUpdate) {
        super.setDatabaseSchemaUpdate(databaseSchemaUpdate);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setHistory(final String history) {
        super.setHistory(history);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setIdBlockSize(final int idBlockSize) {
        super.setIdBlockSize(idBlockSize);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcDriver(final String jdbcDriver) {
        super.setJdbcDriver(jdbcDriver);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcPassword(final String jdbcPassword) {
        super.setJdbcPassword(jdbcPassword);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcUrl(final String jdbcUrl) {
        super.setJdbcUrl(jdbcUrl);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcUsername(final String jdbcUsername) {
        super.setJdbcUsername(jdbcUsername);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJobExecutorActivate(final boolean jobExecutorActivate) {
        super.setJobExecutorActivate(jobExecutorActivate);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setMailServerDefaultFrom(final String mailServerDefaultFrom) {
        super.setMailServerDefaultFrom(mailServerDefaultFrom);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setMailServerHost(final String mailServerHost) {
        super.setMailServerHost(mailServerHost);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setMailServerPassword(final String mailServerPassword) {
        super.setMailServerPassword(mailServerPassword);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setMailServerPort(final int mailServerPort) {
        super.setMailServerPort(mailServerPort);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setMailServerUseTLS(final boolean useTLS) {
        super.setMailServerUseTLS(useTLS);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setMailServerUsername(final String mailServerUsername) {
        super.setMailServerUsername(mailServerUsername);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcMaxActiveConnections(final int jdbcMaxActiveConnections) {
        super.setJdbcMaxActiveConnections(jdbcMaxActiveConnections);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcMaxCheckoutTime(final int jdbcMaxCheckoutTime) {
        super.setJdbcMaxCheckoutTime(jdbcMaxCheckoutTime);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcMaxIdleConnections(final int jdbcMaxIdleConnections) {
        super.setJdbcMaxIdleConnections(jdbcMaxIdleConnections);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcMaxWaitTime(final int jdbcMaxWaitTime) {
        super.setJdbcMaxWaitTime(jdbcMaxWaitTime);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setTransactionsExternallyManaged(final boolean transactionsExternallyManaged) {
        super.setTransactionsExternallyManaged(transactionsExternallyManaged);
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJpaEntityManagerFactory(final Object jpaEntityManagerFactory) {
        this.jpaEntityManagerFactory = jpaEntityManagerFactory;
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJpaHandleTransaction(final boolean jpaHandleTransaction) {
        this.jpaHandleTransaction = jpaHandleTransaction;
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJpaCloseEntityManager(final boolean jpaCloseEntityManager) {
        this.jpaCloseEntityManager = jpaCloseEntityManager;
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcPingEnabled(final boolean jdbcPingEnabled) {
        this.jdbcPingEnabled = jdbcPingEnabled;
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcPingQuery(final String jdbcPingQuery) {
        this.jdbcPingQuery = jdbcPingQuery;
        return this;
    }

    @Override
    public ProcessEngineConfigurationImpl setJdbcPingConnectionNotUsedFor(final int jdbcPingNotUsedFor) {
        this.jdbcPingConnectionNotUsedFor = jdbcPingNotUsedFor;
        return this;
    }

    public boolean isDbIdentityUsed() {
        return this.isDbIdentityUsed;
    }

    public void setDbIdentityUsed(final boolean isDbIdentityUsed) {
        this.isDbIdentityUsed = isDbIdentityUsed;
    }

    public boolean isDbHistoryUsed() {
        return this.isDbHistoryUsed;
    }

    public void setDbHistoryUsed(final boolean isDbHistoryUsed) {
        this.isDbHistoryUsed = isDbHistoryUsed;
    }

    public List<ResolverFactory> getResolverFactories() {
        return this.resolverFactories;
    }

    public void setResolverFactories(final List<ResolverFactory> resolverFactories) {
        this.resolverFactories = resolverFactories;
    }

    public DeploymentCache getDeploymentCache() {
        return this.deploymentCache;
    }

    public void setDeploymentCache(final DeploymentCache deploymentCache) {
        this.deploymentCache = deploymentCache;
    }

    public DeploymentHandlerFactory getDeploymentHandlerFactory() {
        return this.deploymentHandlerFactory;
    }

    public ProcessEngineConfigurationImpl setDeploymentHandlerFactory(final DeploymentHandlerFactory deploymentHandlerFactory) {
        this.deploymentHandlerFactory = deploymentHandlerFactory;
        return this;
    }

    public ProcessEngineConfigurationImpl setDelegateInterceptor(final DelegateInterceptor delegateInterceptor) {
        this.delegateInterceptor = delegateInterceptor;
        return this;
    }

    public DelegateInterceptor getDelegateInterceptor() {
        return this.delegateInterceptor;
    }

    public RejectedJobsHandler getCustomRejectedJobsHandler() {
        return this.customRejectedJobsHandler;
    }

    public ProcessEngineConfigurationImpl setCustomRejectedJobsHandler(final RejectedJobsHandler customRejectedJobsHandler) {
        this.customRejectedJobsHandler = customRejectedJobsHandler;
        return this;
    }

    public EventHandler getEventHandler(final String eventType) {
        return this.eventHandlers.get(eventType);
    }

    public void setEventHandlers(final Map<String, EventHandler> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    public Map<String, EventHandler> getEventHandlers() {
        return this.eventHandlers;
    }

    public List<EventHandler> getCustomEventHandlers() {
        return this.customEventHandlers;
    }

    public void setCustomEventHandlers(final List<EventHandler> customEventHandlers) {
        this.customEventHandlers = customEventHandlers;
    }

    public FailedJobCommandFactory getFailedJobCommandFactory() {
        return this.failedJobCommandFactory;
    }

    public ProcessEngineConfigurationImpl setFailedJobCommandFactory(final FailedJobCommandFactory failedJobCommandFactory) {
        this.failedJobCommandFactory = failedJobCommandFactory;
        return this;
    }

    public ProcessEngineConfiguration setDatabaseTablePrefix(final String databaseTablePrefix) {
        this.databaseTablePrefix = databaseTablePrefix;
        return this;
    }

    public String getDatabaseTablePrefix() {
        return this.databaseTablePrefix;
    }

    public boolean isCreateDiagramOnDeploy() {
        return this.isCreateDiagramOnDeploy;
    }

    public ProcessEngineConfiguration setCreateDiagramOnDeploy(final boolean createDiagramOnDeploy) {
        this.isCreateDiagramOnDeploy = createDiagramOnDeploy;
        return this;
    }

    public String getDatabaseSchema() {
        return this.databaseSchema;
    }

    public void setDatabaseSchema(final String databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    public DataSource getIdGeneratorDataSource() {
        return this.idGeneratorDataSource;
    }

    public void setIdGeneratorDataSource(final DataSource idGeneratorDataSource) {
        this.idGeneratorDataSource = idGeneratorDataSource;
    }

    public String getIdGeneratorDataSourceJndiName() {
        return this.idGeneratorDataSourceJndiName;
    }

    public void setIdGeneratorDataSourceJndiName(final String idGeneratorDataSourceJndiName) {
        this.idGeneratorDataSourceJndiName = idGeneratorDataSourceJndiName;
    }

    public ProcessApplicationManager getProcessApplicationManager() {
        return this.processApplicationManager;
    }

    public void setProcessApplicationManager(final ProcessApplicationManager processApplicationManager) {
        this.processApplicationManager = processApplicationManager;
    }

    public CommandExecutor getCommandExecutorSchemaOperations() {
        return this.commandExecutorSchemaOperations;
    }

    public void setCommandExecutorSchemaOperations(final CommandExecutor commandExecutorSchemaOperations) {
        this.commandExecutorSchemaOperations = commandExecutorSchemaOperations;
    }

    public CorrelationHandler getCorrelationHandler() {
        return this.correlationHandler;
    }

    public void setCorrelationHandler(final CorrelationHandler correlationHandler) {
        this.correlationHandler = correlationHandler;
    }

    public ConditionHandler getConditionHandler() {
        return this.conditionHandler;
    }

    public void setConditionHandler(final ConditionHandler conditionHandler) {
        this.conditionHandler = conditionHandler;
    }

    public ProcessEngineConfigurationImpl setHistoryEventHandler(final HistoryEventHandler historyEventHandler) {
        this.historyEventHandler = historyEventHandler;
        return this;
    }

    public HistoryEventHandler getHistoryEventHandler() {
        return this.historyEventHandler;
    }

    public boolean isEnableDefaultDbHistoryEventHandler() {
        return this.enableDefaultDbHistoryEventHandler;
    }

    public void setEnableDefaultDbHistoryEventHandler(final boolean enableDefaultDbHistoryEventHandler) {
        this.enableDefaultDbHistoryEventHandler = enableDefaultDbHistoryEventHandler;
    }

    public List<HistoryEventHandler> getCustomHistoryEventHandlers() {
        return this.customHistoryEventHandlers;
    }

    public void setCustomHistoryEventHandlers(final List<HistoryEventHandler> customHistoryEventHandlers) {
        this.customHistoryEventHandlers = customHistoryEventHandlers;
    }

    public IncidentHandler getIncidentHandler(final String incidentType) {
        return this.incidentHandlers.get(incidentType);
    }

    public void addIncidentHandler(final IncidentHandler incidentHandler) {
        final IncidentHandler existsHandler = this.incidentHandlers.get(incidentHandler.getIncidentHandlerType());
        if (existsHandler instanceof CompositeIncidentHandler) {
            ((CompositeIncidentHandler) existsHandler).add(incidentHandler);
        } else {
            this.incidentHandlers.put(incidentHandler.getIncidentHandlerType(), incidentHandler);
        }
    }

    public Map<String, IncidentHandler> getIncidentHandlers() {
        return this.incidentHandlers;
    }

    public void setIncidentHandlers(final Map<String, IncidentHandler> incidentHandlers) {
        this.incidentHandlers = incidentHandlers;
    }

    public List<IncidentHandler> getCustomIncidentHandlers() {
        return this.customIncidentHandlers;
    }

    public void setCustomIncidentHandlers(final List<IncidentHandler> customIncidentHandlers) {
        this.customIncidentHandlers = customIncidentHandlers;
    }

    public Map<String, BatchJobHandler<?>> getBatchHandlers() {
        return this.batchHandlers;
    }

    public void setBatchHandlers(final Map<String, BatchJobHandler<?>> batchHandlers) {
        this.batchHandlers = batchHandlers;
    }

    public List<BatchJobHandler<?>> getCustomBatchJobHandlers() {
        return this.customBatchJobHandlers;
    }

    public void setCustomBatchJobHandlers(final List<BatchJobHandler<?>> customBatchJobHandlers) {
        this.customBatchJobHandlers = customBatchJobHandlers;
    }

    public int getBatchJobsPerSeed() {
        return this.batchJobsPerSeed;
    }

    public void setBatchJobsPerSeed(final int batchJobsPerSeed) {
        this.batchJobsPerSeed = batchJobsPerSeed;
    }

    public Map<String, Integer> getInvocationsPerBatchJobByBatchType() {
        return this.invocationsPerBatchJobByBatchType;
    }

    public ProcessEngineConfigurationImpl setInvocationsPerBatchJobByBatchType(final Map<String, Integer> invocationsPerBatchJobByBatchType) {
        this.invocationsPerBatchJobByBatchType = invocationsPerBatchJobByBatchType;
        return this;
    }

    public int getInvocationsPerBatchJob() {
        return this.invocationsPerBatchJob;
    }

    public void setInvocationsPerBatchJob(final int invocationsPerBatchJob) {
        this.invocationsPerBatchJob = invocationsPerBatchJob;
    }

    public int getBatchPollTime() {
        return this.batchPollTime;
    }

    public void setBatchPollTime(final int batchPollTime) {
        this.batchPollTime = batchPollTime;
    }

    public long getBatchJobPriority() {
        return this.batchJobPriority;
    }

    public void setBatchJobPriority(final long batchJobPriority) {
        this.batchJobPriority = batchJobPriority;
    }

    public long getHistoryCleanupJobPriority() {
        return this.historyCleanupJobPriority;
    }

    public ProcessEngineConfigurationImpl setHistoryCleanupJobPriority(final long historyCleanupJobPriority) {
        this.historyCleanupJobPriority = historyCleanupJobPriority;
        return this;
    }

    public SessionFactory getIdentityProviderSessionFactory() {
        return this.identityProviderSessionFactory;
    }

    public void setIdentityProviderSessionFactory(final SessionFactory identityProviderSessionFactory) {
        this.identityProviderSessionFactory = identityProviderSessionFactory;
    }

    public SaltGenerator getSaltGenerator() {
        return this.saltGenerator;
    }

    public void setSaltGenerator(final SaltGenerator saltGenerator) {
        this.saltGenerator = saltGenerator;
    }

    public void setPasswordEncryptor(final PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    public PasswordEncryptor getPasswordEncryptor() {
        return this.passwordEncryptor;
    }

    public List<PasswordEncryptor> getCustomPasswordChecker() {
        return this.customPasswordChecker;
    }

    public void setCustomPasswordChecker(final List<PasswordEncryptor> customPasswordChecker) {
        this.customPasswordChecker = customPasswordChecker;
    }

    public PasswordManager getPasswordManager() {
        return this.passwordManager;
    }

    public void setPasswordManager(final PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }

    public Set<String> getRegisteredDeployments() {
        return this.registeredDeployments;
    }

    public void setRegisteredDeployments(final Set<String> registeredDeployments) {
        this.registeredDeployments = registeredDeployments;
    }

    public ResourceAuthorizationProvider getResourceAuthorizationProvider() {
        return this.resourceAuthorizationProvider;
    }

    public void setResourceAuthorizationProvider(final ResourceAuthorizationProvider resourceAuthorizationProvider) {
        this.resourceAuthorizationProvider = resourceAuthorizationProvider;
    }

    public PermissionProvider getPermissionProvider() {
        return this.permissionProvider;
    }

    public void setPermissionProvider(final PermissionProvider permissionProvider) {
        this.permissionProvider = permissionProvider;
    }

    public List<ProcessEnginePlugin> getProcessEnginePlugins() {
        return this.processEnginePlugins;
    }

    public void setProcessEnginePlugins(final List<ProcessEnginePlugin> processEnginePlugins) {
        this.processEnginePlugins = processEnginePlugins;
    }

    public ProcessEngineConfigurationImpl setHistoryEventProducer(final HistoryEventProducer historyEventProducer) {
        this.historyEventProducer = historyEventProducer;
        return this;
    }

    public HistoryEventProducer getHistoryEventProducer() {
        return this.historyEventProducer;
    }

    public ProcessEngineConfigurationImpl setCmmnHistoryEventProducer(final CmmnHistoryEventProducer cmmnHistoryEventProducer) {
        this.cmmnHistoryEventProducer = cmmnHistoryEventProducer;
        return this;
    }

    public CmmnHistoryEventProducer getCmmnHistoryEventProducer() {
        return this.cmmnHistoryEventProducer;
    }

    public ProcessEngineConfigurationImpl setDmnHistoryEventProducer(final DmnHistoryEventProducer dmnHistoryEventProducer) {
        this.dmnHistoryEventProducer = dmnHistoryEventProducer;
        return this;
    }

    public DmnHistoryEventProducer getDmnHistoryEventProducer() {
        return this.dmnHistoryEventProducer;
    }

    public Map<String, Class<? extends FormFieldValidator>> getCustomFormFieldValidators() {
        return this.customFormFieldValidators;
    }

    public void setCustomFormFieldValidators(final Map<String, Class<? extends FormFieldValidator>> customFormFieldValidators) {
        this.customFormFieldValidators = customFormFieldValidators;
    }

    public void setFormValidators(final FormValidators formValidators) {
        this.formValidators = formValidators;
    }

    public FormValidators getFormValidators() {
        return this.formValidators;
    }

    public ProcessEngineConfigurationImpl setDisableStrictCamundaFormParsing(final boolean disableStrictCamundaFormParsing) {
        this.disableStrictCamundaFormParsing = disableStrictCamundaFormParsing;
        return this;
    }

    public boolean isDisableStrictCamundaFormParsing() {
        return this.disableStrictCamundaFormParsing;
    }

    public boolean isExecutionTreePrefetchEnabled() {
        return this.isExecutionTreePrefetchEnabled;
    }

    public void setExecutionTreePrefetchEnabled(final boolean isExecutionTreePrefetchingEnabled) {
        this.isExecutionTreePrefetchEnabled = isExecutionTreePrefetchingEnabled;
    }

    public ProcessEngineImpl getProcessEngine() {
        return this.processEngine;
    }

    public void setAutoStoreScriptVariables(final boolean autoStoreScriptVariables) {
        this.autoStoreScriptVariables = autoStoreScriptVariables;
    }

    public boolean isAutoStoreScriptVariables() {
        return this.autoStoreScriptVariables;
    }

    public void setEnableScriptCompilation(final boolean enableScriptCompilation) {
        this.enableScriptCompilation = enableScriptCompilation;
    }

    public boolean isEnableScriptCompilation() {
        return this.enableScriptCompilation;
    }

    public boolean isEnableGracefulDegradationOnContextSwitchFailure() {
        return this.enableGracefulDegradationOnContextSwitchFailure;
    }

    public void setEnableGracefulDegradationOnContextSwitchFailure(final boolean enableGracefulDegradationOnContextSwitchFailure) {
        this.enableGracefulDegradationOnContextSwitchFailure = enableGracefulDegradationOnContextSwitchFailure;
    }

    public boolean isDeploymentLockUsed() {
        return this.isDeploymentLockUsed;
    }

    public void setDeploymentLockUsed(final boolean isDeploymentLockUsed) {
        this.isDeploymentLockUsed = isDeploymentLockUsed;
    }

    public boolean isDeploymentSynchronized() {
        return this.isDeploymentSynchronized;
    }

    public void setDeploymentSynchronized(final boolean deploymentSynchronized) {
        this.isDeploymentSynchronized = deploymentSynchronized;
    }

    public boolean isCmmnEnabled() {
        return this.cmmnEnabled;
    }

    public void setCmmnEnabled(final boolean cmmnEnabled) {
        this.cmmnEnabled = cmmnEnabled;
    }

    public boolean isDmnEnabled() {
        return this.dmnEnabled;
    }

    public void setDmnEnabled(final boolean dmnEnabled) {
        this.dmnEnabled = dmnEnabled;
    }

    public boolean isStandaloneTasksEnabled() {
        return this.standaloneTasksEnabled;
    }

    public ProcessEngineConfigurationImpl setStandaloneTasksEnabled(final boolean standaloneTasksEnabled) {
        this.standaloneTasksEnabled = standaloneTasksEnabled;
        return this;
    }

    public boolean isCompositeIncidentHandlersEnabled() {
        return this.isCompositeIncidentHandlersEnabled;
    }

    public ProcessEngineConfigurationImpl setCompositeIncidentHandlersEnabled(final boolean compositeIncidentHandlersEnabled) {
        this.isCompositeIncidentHandlersEnabled = compositeIncidentHandlersEnabled;
        return this;
    }

    public ScriptFactory getScriptFactory() {
        return this.scriptFactory;
    }

    public ScriptingEnvironment getScriptingEnvironment() {
        return this.scriptingEnvironment;
    }

    public void setScriptFactory(final ScriptFactory scriptFactory) {
        this.scriptFactory = scriptFactory;
    }

    public ScriptEngineResolver getScriptEngineResolver() {
        return this.scriptEngineResolver;
    }

    public ProcessEngineConfigurationImpl setScriptEngineResolver(final ScriptEngineResolver scriptEngineResolver) {
        this.scriptEngineResolver = scriptEngineResolver;
        if (this.scriptingEngines != null) {
            this.scriptingEngines.setScriptEngineResolver(scriptEngineResolver);
        }
        return this;
    }

    public void setScriptingEnvironment(final ScriptingEnvironment scriptingEnvironment) {
        this.scriptingEnvironment = scriptingEnvironment;
    }

    public List<ScriptEnvResolver> getEnvScriptResolvers() {
        return this.scriptEnvResolvers;
    }

    public void setEnvScriptResolvers(final List<ScriptEnvResolver> scriptEnvResolvers) {
        this.scriptEnvResolvers = scriptEnvResolvers;
    }

    public String getScriptEngineNameJavaScript() {
        return this.scriptEngineNameJavaScript;
    }

    public ProcessEngineConfigurationImpl setScriptEngineNameJavaScript(final String scriptEngineNameJavaScript) {
        this.scriptEngineNameJavaScript = scriptEngineNameJavaScript;
        return this;
    }

    public ProcessEngineConfiguration setArtifactFactory(final ArtifactFactory artifactFactory) {
        this.artifactFactory = artifactFactory;
        return this;
    }

    public ArtifactFactory getArtifactFactory() {
        return this.artifactFactory;
    }

    public String getDefaultSerializationFormat() {
        return this.defaultSerializationFormat;
    }

    public ProcessEngineConfigurationImpl setDefaultSerializationFormat(final String defaultSerializationFormat) {
        this.defaultSerializationFormat = defaultSerializationFormat;
        return this;
    }

    public boolean isJavaSerializationFormatEnabled() {
        return this.javaSerializationFormatEnabled;
    }

    public void setJavaSerializationFormatEnabled(final boolean javaSerializationFormatEnabled) {
        this.javaSerializationFormatEnabled = javaSerializationFormatEnabled;
    }

    public ProcessEngineConfigurationImpl setDefaultCharsetName(final String defaultCharsetName) {
        this.defaultCharsetName = defaultCharsetName;
        return this;
    }

    public ProcessEngineConfigurationImpl setDefaultCharset(final Charset defautlCharset) {
        this.defaultCharset = defautlCharset;
        return this;
    }

    public Charset getDefaultCharset() {
        return this.defaultCharset;
    }

    public boolean isDbEntityCacheReuseEnabled() {
        return this.isDbEntityCacheReuseEnabled;
    }

    public ProcessEngineConfigurationImpl setDbEntityCacheReuseEnabled(final boolean isDbEntityCacheReuseEnabled) {
        this.isDbEntityCacheReuseEnabled = isDbEntityCacheReuseEnabled;
        return this;
    }

    public DbEntityCacheKeyMapping getDbEntityCacheKeyMapping() {
        return this.dbEntityCacheKeyMapping;
    }

    public ProcessEngineConfigurationImpl setDbEntityCacheKeyMapping(final DbEntityCacheKeyMapping dbEntityCacheKeyMapping) {
        this.dbEntityCacheKeyMapping = dbEntityCacheKeyMapping;
        return this;
    }

    public ProcessEngineConfigurationImpl setCustomHistoryLevels(final List<HistoryLevel> customHistoryLevels) {
        this.customHistoryLevels = customHistoryLevels;
        return this;
    }

    public List<HistoryLevel> getHistoryLevels() {
        return this.historyLevels;
    }

    public List<HistoryLevel> getCustomHistoryLevels() {
        return this.customHistoryLevels;
    }

    public boolean isInvokeCustomVariableListeners() {
        return this.isInvokeCustomVariableListeners;
    }

    public ProcessEngineConfigurationImpl setInvokeCustomVariableListeners(final boolean isInvokeCustomVariableListeners) {
        this.isInvokeCustomVariableListeners = isInvokeCustomVariableListeners;
        return this;
    }

    public void close() {
        if (this.forceCloseMybatisConnectionPool && this.dataSource instanceof PooledDataSource) {
            ((PooledDataSource) this.dataSource).forceCloseAll();
        }
    }

    public MetricsRegistry getMetricsRegistry() {
        return this.metricsRegistry;
    }

    public ProcessEngineConfigurationImpl setMetricsRegistry(final MetricsRegistry metricsRegistry) {
        this.metricsRegistry = metricsRegistry;
        return this;
    }

    public ProcessEngineConfigurationImpl setMetricsEnabled(final boolean isMetricsEnabled) {
        this.isMetricsEnabled = isMetricsEnabled;
        return this;
    }

    public boolean isMetricsEnabled() {
        return this.isMetricsEnabled;
    }

    public DbMetricsReporter getDbMetricsReporter() {
        return this.dbMetricsReporter;
    }

    public ProcessEngineConfigurationImpl setDbMetricsReporter(final DbMetricsReporter dbMetricsReporter) {
        this.dbMetricsReporter = dbMetricsReporter;
        return this;
    }

    public boolean isDbMetricsReporterActivate() {
        return this.isDbMetricsReporterActivate;
    }

    public ProcessEngineConfigurationImpl setDbMetricsReporterActivate(final boolean isDbMetricsReporterEnabled) {
        this.isDbMetricsReporterActivate = isDbMetricsReporterEnabled;
        return this;
    }

    @Deprecated
    public MetricsReporterIdProvider getMetricsReporterIdProvider() {
        return this.metricsReporterIdProvider;
    }

    @Deprecated
    public ProcessEngineConfigurationImpl setMetricsReporterIdProvider(final MetricsReporterIdProvider metricsReporterIdProvider) {
        this.metricsReporterIdProvider = metricsReporterIdProvider;
        return this;
    }

    public String getHostname() {
        return this.hostname;
    }

    public ProcessEngineConfigurationImpl setHostname(final String hostname) {
        this.hostname = hostname;
        return this;
    }

    public HostnameProvider getHostnameProvider() {
        return this.hostnameProvider;
    }

    public ProcessEngineConfigurationImpl setHostnameProvider(final HostnameProvider hostnameProvider) {
        this.hostnameProvider = hostnameProvider;
        return this;
    }

    public boolean isTaskMetricsEnabled() {
        return this.isTaskMetricsEnabled;
    }

    public ProcessEngineConfigurationImpl setTaskMetricsEnabled(final boolean isTaskMetricsEnabled) {
        this.isTaskMetricsEnabled = isTaskMetricsEnabled;
        return this;
    }

    public boolean isEnableScriptEngineCaching() {
        return this.enableScriptEngineCaching;
    }

    public ProcessEngineConfigurationImpl setEnableScriptEngineCaching(final boolean enableScriptEngineCaching) {
        this.enableScriptEngineCaching = enableScriptEngineCaching;
        return this;
    }

    public boolean isEnableFetchScriptEngineFromProcessApplication() {
        return this.enableFetchScriptEngineFromProcessApplication;
    }

    public ProcessEngineConfigurationImpl setEnableFetchScriptEngineFromProcessApplication(final boolean enable) {
        this.enableFetchScriptEngineFromProcessApplication = enable;
        return this;
    }

    public boolean isEnableScriptEngineLoadExternalResources() {
        return this.enableScriptEngineLoadExternalResources;
    }

    public ProcessEngineConfigurationImpl setEnableScriptEngineLoadExternalResources(final boolean enableScriptEngineLoadExternalResources) {
        this.enableScriptEngineLoadExternalResources = enableScriptEngineLoadExternalResources;
        return this;
    }

    public boolean isEnableScriptEngineNashornCompatibility() {
        return this.enableScriptEngineNashornCompatibility;
    }

    public ProcessEngineConfigurationImpl setEnableScriptEngineNashornCompatibility(final boolean enableScriptEngineNashornCompatibility) {
        this.enableScriptEngineNashornCompatibility = enableScriptEngineNashornCompatibility;
        return this;
    }

    public boolean isConfigureScriptEngineHostAccess() {
        return this.configureScriptEngineHostAccess;
    }

    public ProcessEngineConfigurationImpl setConfigureScriptEngineHostAccess(final boolean configureScriptEngineHostAccess) {
        this.configureScriptEngineHostAccess = configureScriptEngineHostAccess;
        return this;
    }

    public boolean isEnableExpressionsInAdhocQueries() {
        return this.enableExpressionsInAdhocQueries;
    }

    public void setEnableExpressionsInAdhocQueries(final boolean enableExpressionsInAdhocQueries) {
        this.enableExpressionsInAdhocQueries = enableExpressionsInAdhocQueries;
    }

    public boolean isEnableExpressionsInStoredQueries() {
        return this.enableExpressionsInStoredQueries;
    }

    public void setEnableExpressionsInStoredQueries(final boolean enableExpressionsInStoredQueries) {
        this.enableExpressionsInStoredQueries = enableExpressionsInStoredQueries;
    }

    public boolean isEnableXxeProcessing() {
        return this.enableXxeProcessing;
    }

    public void setEnableXxeProcessing(final boolean enableXxeProcessing) {
        this.enableXxeProcessing = enableXxeProcessing;
    }

    public ProcessEngineConfigurationImpl setBpmnStacktraceVerbose(final boolean isBpmnStacktraceVerbose) {
        this.isBpmnStacktraceVerbose = isBpmnStacktraceVerbose;
        return this;
    }

    public boolean isBpmnStacktraceVerbose() {
        return this.isBpmnStacktraceVerbose;
    }

    public boolean isForceCloseMybatisConnectionPool() {
        return this.forceCloseMybatisConnectionPool;
    }

    public ProcessEngineConfigurationImpl setForceCloseMybatisConnectionPool(final boolean forceCloseMybatisConnectionPool) {
        this.forceCloseMybatisConnectionPool = forceCloseMybatisConnectionPool;
        return this;
    }

    public boolean isRestrictUserOperationLogToAuthenticatedUsers() {
        return this.restrictUserOperationLogToAuthenticatedUsers;
    }

    public ProcessEngineConfigurationImpl setRestrictUserOperationLogToAuthenticatedUsers(final boolean restrictUserOperationLogToAuthenticatedUsers) {
        this.restrictUserOperationLogToAuthenticatedUsers = restrictUserOperationLogToAuthenticatedUsers;
        return this;
    }

    public ProcessEngineConfigurationImpl setTenantIdProvider(final TenantIdProvider tenantIdProvider) {
        this.tenantIdProvider = tenantIdProvider;
        return this;
    }

    public TenantIdProvider getTenantIdProvider() {
        return this.tenantIdProvider;
    }

    public void setMigrationActivityMatcher(final MigrationActivityMatcher migrationActivityMatcher) {
        this.migrationActivityMatcher = migrationActivityMatcher;
    }

    public MigrationActivityMatcher getMigrationActivityMatcher() {
        return this.migrationActivityMatcher;
    }

    public void setCustomPreMigrationActivityValidators(final List<MigrationActivityValidator> customPreMigrationActivityValidators) {
        this.customPreMigrationActivityValidators = customPreMigrationActivityValidators;
    }

    public List<MigrationActivityValidator> getCustomPreMigrationActivityValidators() {
        return this.customPreMigrationActivityValidators;
    }

    public void setCustomPostMigrationActivityValidators(final List<MigrationActivityValidator> customPostMigrationActivityValidators) {
        this.customPostMigrationActivityValidators = customPostMigrationActivityValidators;
    }

    public List<MigrationActivityValidator> getCustomPostMigrationActivityValidators() {
        return this.customPostMigrationActivityValidators;
    }

    public List<MigrationActivityValidator> getDefaultMigrationActivityValidators() {
        final List<MigrationActivityValidator> migrationActivityValidators = new ArrayList<MigrationActivityValidator>();
        migrationActivityValidators.add(SupportedActivityValidator.INSTANCE);
        migrationActivityValidators.add(SupportedPassiveEventTriggerActivityValidator.INSTANCE);
        migrationActivityValidators.add(NoCompensationHandlerActivityValidator.INSTANCE);
        return migrationActivityValidators;
    }

    public void setMigrationInstructionGenerator(final MigrationInstructionGenerator migrationInstructionGenerator) {
        this.migrationInstructionGenerator = migrationInstructionGenerator;
    }

    public MigrationInstructionGenerator getMigrationInstructionGenerator() {
        return this.migrationInstructionGenerator;
    }

    public void setMigrationInstructionValidators(final List<MigrationInstructionValidator> migrationInstructionValidators) {
        this.migrationInstructionValidators = migrationInstructionValidators;
    }

    public List<MigrationInstructionValidator> getMigrationInstructionValidators() {
        return this.migrationInstructionValidators;
    }

    public void setCustomPostMigrationInstructionValidators(final List<MigrationInstructionValidator> customPostMigrationInstructionValidators) {
        this.customPostMigrationInstructionValidators = customPostMigrationInstructionValidators;
    }

    public List<MigrationInstructionValidator> getCustomPostMigrationInstructionValidators() {
        return this.customPostMigrationInstructionValidators;
    }

    public void setCustomPreMigrationInstructionValidators(final List<MigrationInstructionValidator> customPreMigrationInstructionValidators) {
        this.customPreMigrationInstructionValidators = customPreMigrationInstructionValidators;
    }

    public List<MigrationInstructionValidator> getCustomPreMigrationInstructionValidators() {
        return this.customPreMigrationInstructionValidators;
    }

    public List<MigrationInstructionValidator> getDefaultMigrationInstructionValidators() {
        final List<MigrationInstructionValidator> migrationInstructionValidators = new ArrayList<MigrationInstructionValidator>();
        migrationInstructionValidators.add(new SameBehaviorInstructionValidator());
        migrationInstructionValidators.add(new SameEventTypeValidator());
        migrationInstructionValidators.add(new OnlyOnceMappedActivityInstructionValidator());
        migrationInstructionValidators.add(new CannotAddMultiInstanceBodyValidator());
        migrationInstructionValidators.add(new CannotAddMultiInstanceInnerActivityValidator());
        migrationInstructionValidators.add(new CannotRemoveMultiInstanceInnerActivityValidator());
        migrationInstructionValidators.add(new GatewayMappingValidator());
        migrationInstructionValidators.add(new SameEventScopeInstructionValidator());
        migrationInstructionValidators.add(new UpdateEventTriggersValidator());
        migrationInstructionValidators.add(new AdditionalFlowScopeInstructionValidator());
        migrationInstructionValidators.add(new ConditionalEventUpdateEventTriggerValidator());
        return migrationInstructionValidators;
    }

    public void setMigratingActivityInstanceValidators(final List<MigratingActivityInstanceValidator> migratingActivityInstanceValidators) {
        this.migratingActivityInstanceValidators = migratingActivityInstanceValidators;
    }

    public List<MigratingActivityInstanceValidator> getMigratingActivityInstanceValidators() {
        return this.migratingActivityInstanceValidators;
    }

    public void setCustomPostMigratingActivityInstanceValidators(final List<MigratingActivityInstanceValidator> customPostMigratingActivityInstanceValidators) {
        this.customPostMigratingActivityInstanceValidators = customPostMigratingActivityInstanceValidators;
    }

    public List<MigratingActivityInstanceValidator> getCustomPostMigratingActivityInstanceValidators() {
        return this.customPostMigratingActivityInstanceValidators;
    }

    public void setCustomPreMigratingActivityInstanceValidators(final List<MigratingActivityInstanceValidator> customPreMigratingActivityInstanceValidators) {
        this.customPreMigratingActivityInstanceValidators = customPreMigratingActivityInstanceValidators;
    }

    public List<MigratingActivityInstanceValidator> getCustomPreMigratingActivityInstanceValidators() {
        return this.customPreMigratingActivityInstanceValidators;
    }

    public List<MigratingTransitionInstanceValidator> getMigratingTransitionInstanceValidators() {
        return this.migratingTransitionInstanceValidators;
    }

    public List<MigratingCompensationInstanceValidator> getMigratingCompensationInstanceValidators() {
        return this.migratingCompensationInstanceValidators;
    }

    public List<MigratingActivityInstanceValidator> getDefaultMigratingActivityInstanceValidators() {
        final List<MigratingActivityInstanceValidator> migratingActivityInstanceValidators = new ArrayList<MigratingActivityInstanceValidator>();
        migratingActivityInstanceValidators.add(new NoUnmappedLeafInstanceValidator());
        migratingActivityInstanceValidators.add(new VariableConflictActivityInstanceValidator());
        migratingActivityInstanceValidators.add(new SupportedActivityInstanceValidator());
        return migratingActivityInstanceValidators;
    }

    public List<MigratingTransitionInstanceValidator> getDefaultMigratingTransitionInstanceValidators() {
        final List<MigratingTransitionInstanceValidator> migratingTransitionInstanceValidators = new ArrayList<MigratingTransitionInstanceValidator>();
        migratingTransitionInstanceValidators.add(new NoUnmappedLeafInstanceValidator());
        migratingTransitionInstanceValidators.add(new AsyncAfterMigrationValidator());
        migratingTransitionInstanceValidators.add(new AsyncProcessStartMigrationValidator());
        migratingTransitionInstanceValidators.add(new AsyncMigrationValidator());
        return migratingTransitionInstanceValidators;
    }

    public List<CommandChecker> getCommandCheckers() {
        return this.commandCheckers;
    }

    public void setCommandCheckers(final List<CommandChecker> commandCheckers) {
        this.commandCheckers = commandCheckers;
    }

    public ProcessEngineConfigurationImpl setUseSharedSqlSessionFactory(final boolean isUseSharedSqlSessionFactory) {
        this.isUseSharedSqlSessionFactory = isUseSharedSqlSessionFactory;
        return this;
    }

    public boolean isUseSharedSqlSessionFactory() {
        return this.isUseSharedSqlSessionFactory;
    }

    public boolean getDisableStrictCallActivityValidation() {
        return this.disableStrictCallActivityValidation;
    }

    public void setDisableStrictCallActivityValidation(final boolean disableStrictCallActivityValidation) {
        this.disableStrictCallActivityValidation = disableStrictCallActivityValidation;
    }

    public String getHistoryCleanupBatchWindowStartTime() {
        return this.historyCleanupBatchWindowStartTime;
    }

    public void setHistoryCleanupBatchWindowStartTime(final String historyCleanupBatchWindowStartTime) {
        this.historyCleanupBatchWindowStartTime = historyCleanupBatchWindowStartTime;
    }

    public String getHistoryCleanupBatchWindowEndTime() {
        return this.historyCleanupBatchWindowEndTime;
    }

    public void setHistoryCleanupBatchWindowEndTime(final String historyCleanupBatchWindowEndTime) {
        this.historyCleanupBatchWindowEndTime = historyCleanupBatchWindowEndTime;
    }

    public String getMondayHistoryCleanupBatchWindowStartTime() {
        return this.mondayHistoryCleanupBatchWindowStartTime;
    }

    public void setMondayHistoryCleanupBatchWindowStartTime(final String mondayHistoryCleanupBatchWindowStartTime) {
        this.mondayHistoryCleanupBatchWindowStartTime = mondayHistoryCleanupBatchWindowStartTime;
    }

    public String getMondayHistoryCleanupBatchWindowEndTime() {
        return this.mondayHistoryCleanupBatchWindowEndTime;
    }

    public void setMondayHistoryCleanupBatchWindowEndTime(final String mondayHistoryCleanupBatchWindowEndTime) {
        this.mondayHistoryCleanupBatchWindowEndTime = mondayHistoryCleanupBatchWindowEndTime;
    }

    public String getTuesdayHistoryCleanupBatchWindowStartTime() {
        return this.tuesdayHistoryCleanupBatchWindowStartTime;
    }

    public void setTuesdayHistoryCleanupBatchWindowStartTime(final String tuesdayHistoryCleanupBatchWindowStartTime) {
        this.tuesdayHistoryCleanupBatchWindowStartTime = tuesdayHistoryCleanupBatchWindowStartTime;
    }

    public String getTuesdayHistoryCleanupBatchWindowEndTime() {
        return this.tuesdayHistoryCleanupBatchWindowEndTime;
    }

    public void setTuesdayHistoryCleanupBatchWindowEndTime(final String tuesdayHistoryCleanupBatchWindowEndTime) {
        this.tuesdayHistoryCleanupBatchWindowEndTime = tuesdayHistoryCleanupBatchWindowEndTime;
    }

    public String getWednesdayHistoryCleanupBatchWindowStartTime() {
        return this.wednesdayHistoryCleanupBatchWindowStartTime;
    }

    public void setWednesdayHistoryCleanupBatchWindowStartTime(final String wednesdayHistoryCleanupBatchWindowStartTime) {
        this.wednesdayHistoryCleanupBatchWindowStartTime = wednesdayHistoryCleanupBatchWindowStartTime;
    }

    public String getWednesdayHistoryCleanupBatchWindowEndTime() {
        return this.wednesdayHistoryCleanupBatchWindowEndTime;
    }

    public void setWednesdayHistoryCleanupBatchWindowEndTime(final String wednesdayHistoryCleanupBatchWindowEndTime) {
        this.wednesdayHistoryCleanupBatchWindowEndTime = wednesdayHistoryCleanupBatchWindowEndTime;
    }

    public String getThursdayHistoryCleanupBatchWindowStartTime() {
        return this.thursdayHistoryCleanupBatchWindowStartTime;
    }

    public void setThursdayHistoryCleanupBatchWindowStartTime(final String thursdayHistoryCleanupBatchWindowStartTime) {
        this.thursdayHistoryCleanupBatchWindowStartTime = thursdayHistoryCleanupBatchWindowStartTime;
    }

    public String getThursdayHistoryCleanupBatchWindowEndTime() {
        return this.thursdayHistoryCleanupBatchWindowEndTime;
    }

    public void setThursdayHistoryCleanupBatchWindowEndTime(final String thursdayHistoryCleanupBatchWindowEndTime) {
        this.thursdayHistoryCleanupBatchWindowEndTime = thursdayHistoryCleanupBatchWindowEndTime;
    }

    public String getFridayHistoryCleanupBatchWindowStartTime() {
        return this.fridayHistoryCleanupBatchWindowStartTime;
    }

    public void setFridayHistoryCleanupBatchWindowStartTime(final String fridayHistoryCleanupBatchWindowStartTime) {
        this.fridayHistoryCleanupBatchWindowStartTime = fridayHistoryCleanupBatchWindowStartTime;
    }

    public String getFridayHistoryCleanupBatchWindowEndTime() {
        return this.fridayHistoryCleanupBatchWindowEndTime;
    }

    public void setFridayHistoryCleanupBatchWindowEndTime(final String fridayHistoryCleanupBatchWindowEndTime) {
        this.fridayHistoryCleanupBatchWindowEndTime = fridayHistoryCleanupBatchWindowEndTime;
    }

    public String getSaturdayHistoryCleanupBatchWindowStartTime() {
        return this.saturdayHistoryCleanupBatchWindowStartTime;
    }

    public void setSaturdayHistoryCleanupBatchWindowStartTime(final String saturdayHistoryCleanupBatchWindowStartTime) {
        this.saturdayHistoryCleanupBatchWindowStartTime = saturdayHistoryCleanupBatchWindowStartTime;
    }

    public String getSaturdayHistoryCleanupBatchWindowEndTime() {
        return this.saturdayHistoryCleanupBatchWindowEndTime;
    }

    public void setSaturdayHistoryCleanupBatchWindowEndTime(final String saturdayHistoryCleanupBatchWindowEndTime) {
        this.saturdayHistoryCleanupBatchWindowEndTime = saturdayHistoryCleanupBatchWindowEndTime;
    }

    public String getSundayHistoryCleanupBatchWindowStartTime() {
        return this.sundayHistoryCleanupBatchWindowStartTime;
    }

    public void setSundayHistoryCleanupBatchWindowStartTime(final String sundayHistoryCleanupBatchWindowStartTime) {
        this.sundayHistoryCleanupBatchWindowStartTime = sundayHistoryCleanupBatchWindowStartTime;
    }

    public String getSundayHistoryCleanupBatchWindowEndTime() {
        return this.sundayHistoryCleanupBatchWindowEndTime;
    }

    public void setSundayHistoryCleanupBatchWindowEndTime(final String sundayHistoryCleanupBatchWindowEndTime) {
        this.sundayHistoryCleanupBatchWindowEndTime = sundayHistoryCleanupBatchWindowEndTime;
    }

    public Date getHistoryCleanupBatchWindowStartTimeAsDate() {
        return this.historyCleanupBatchWindowStartTimeAsDate;
    }

    public void setHistoryCleanupBatchWindowStartTimeAsDate(final Date historyCleanupBatchWindowStartTimeAsDate) {
        this.historyCleanupBatchWindowStartTimeAsDate = historyCleanupBatchWindowStartTimeAsDate;
    }

    public void setHistoryCleanupBatchWindowEndTimeAsDate(final Date historyCleanupBatchWindowEndTimeAsDate) {
        this.historyCleanupBatchWindowEndTimeAsDate = historyCleanupBatchWindowEndTimeAsDate;
    }

    public Date getHistoryCleanupBatchWindowEndTimeAsDate() {
        return this.historyCleanupBatchWindowEndTimeAsDate;
    }

    public Map<Integer, BatchWindowConfiguration> getHistoryCleanupBatchWindows() {
        return this.historyCleanupBatchWindows;
    }

    public void setHistoryCleanupBatchWindows(final Map<Integer, BatchWindowConfiguration> historyCleanupBatchWindows) {
        this.historyCleanupBatchWindows = historyCleanupBatchWindows;
    }

    public int getHistoryCleanupBatchSize() {
        return this.historyCleanupBatchSize;
    }

    public void setHistoryCleanupBatchSize(final int historyCleanupBatchSize) {
        this.historyCleanupBatchSize = historyCleanupBatchSize;
    }

    public int getHistoryCleanupBatchThreshold() {
        return this.historyCleanupBatchThreshold;
    }

    public void setHistoryCleanupBatchThreshold(final int historyCleanupBatchThreshold) {
        this.historyCleanupBatchThreshold = historyCleanupBatchThreshold;
    }

    public boolean isHistoryCleanupMetricsEnabled() {
        return this.historyCleanupMetricsEnabled;
    }

    public void setHistoryCleanupMetricsEnabled(final boolean historyCleanupMetricsEnabled) {
        this.historyCleanupMetricsEnabled = historyCleanupMetricsEnabled;
    }

    public boolean isHistoryCleanupEnabled() {
        return this.historyCleanupEnabled;
    }

    public ProcessEngineConfigurationImpl setHistoryCleanupEnabled(final boolean historyCleanupEnabled) {
        this.historyCleanupEnabled = historyCleanupEnabled;
        return this;
    }

    public String getHistoryTimeToLive() {
        return this.historyTimeToLive;
    }

    public void setHistoryTimeToLive(final String historyTimeToLive) {
        this.historyTimeToLive = historyTimeToLive;
    }

    public String getBatchOperationHistoryTimeToLive() {
        return this.batchOperationHistoryTimeToLive;
    }

    public int getHistoryCleanupDegreeOfParallelism() {
        return this.historyCleanupDegreeOfParallelism;
    }

    public void setHistoryCleanupDegreeOfParallelism(final int historyCleanupDegreeOfParallelism) {
        this.historyCleanupDegreeOfParallelism = historyCleanupDegreeOfParallelism;
    }

    public void setBatchOperationHistoryTimeToLive(final String batchOperationHistoryTimeToLive) {
        this.batchOperationHistoryTimeToLive = batchOperationHistoryTimeToLive;
    }

    public Map<String, String> getBatchOperationsForHistoryCleanup() {
        return this.batchOperationsForHistoryCleanup;
    }

    public void setBatchOperationsForHistoryCleanup(final Map<String, String> batchOperationsForHistoryCleanup) {
        this.batchOperationsForHistoryCleanup = batchOperationsForHistoryCleanup;
    }

    public Map<String, Integer> getParsedBatchOperationsForHistoryCleanup() {
        return this.parsedBatchOperationsForHistoryCleanup;
    }

    public void setParsedBatchOperationsForHistoryCleanup(final Map<String, Integer> parsedBatchOperationsForHistoryCleanup) {
        this.parsedBatchOperationsForHistoryCleanup = parsedBatchOperationsForHistoryCleanup;
    }

    public String getHistoryCleanupJobLogTimeToLive() {
        return this.historyCleanupJobLogTimeToLive;
    }

    public ProcessEngineConfigurationImpl setHistoryCleanupJobLogTimeToLive(final String historyCleanupJobLogTimeToLive) {
        this.historyCleanupJobLogTimeToLive = historyCleanupJobLogTimeToLive;
        return this;
    }

    public String getTaskMetricsTimeToLive() {
        return this.taskMetricsTimeToLive;
    }

    public ProcessEngineConfigurationImpl setTaskMetricsTimeToLive(final String taskMetricsTimeToLive) {
        this.taskMetricsTimeToLive = taskMetricsTimeToLive;
        return this;
    }

    public Integer getParsedTaskMetricsTimeToLive() {
        return this.parsedTaskMetricsTimeToLive;
    }

    public ProcessEngineConfigurationImpl setParsedTaskMetricsTimeToLive(final Integer parsedTaskMetricsTimeToLive) {
        this.parsedTaskMetricsTimeToLive = parsedTaskMetricsTimeToLive;
        return this;
    }

    public BatchWindowManager getBatchWindowManager() {
        return this.batchWindowManager;
    }

    public void setBatchWindowManager(final BatchWindowManager batchWindowManager) {
        this.batchWindowManager = batchWindowManager;
    }

    public HistoryRemovalTimeProvider getHistoryRemovalTimeProvider() {
        return this.historyRemovalTimeProvider;
    }

    public ProcessEngineConfigurationImpl setHistoryRemovalTimeProvider(final HistoryRemovalTimeProvider removalTimeProvider) {
        this.historyRemovalTimeProvider = removalTimeProvider;
        return this;
    }

    public String getHistoryRemovalTimeStrategy() {
        return this.historyRemovalTimeStrategy;
    }

    public ProcessEngineConfigurationImpl setHistoryRemovalTimeStrategy(final String removalTimeStrategy) {
        this.historyRemovalTimeStrategy = removalTimeStrategy;
        return this;
    }

    public String getHistoryCleanupStrategy() {
        return this.historyCleanupStrategy;
    }

    public ProcessEngineConfigurationImpl setHistoryCleanupStrategy(final String historyCleanupStrategy) {
        this.historyCleanupStrategy = historyCleanupStrategy;
        return this;
    }

    public int getFailedJobListenerMaxRetries() {
        return this.failedJobListenerMaxRetries;
    }

    public void setFailedJobListenerMaxRetries(final int failedJobListenerMaxRetries) {
        this.failedJobListenerMaxRetries = failedJobListenerMaxRetries;
    }

    public String getFailedJobRetryTimeCycle() {
        return this.failedJobRetryTimeCycle;
    }

    public void setFailedJobRetryTimeCycle(final String failedJobRetryTimeCycle) {
        this.failedJobRetryTimeCycle = failedJobRetryTimeCycle;
    }

    public int getLoginMaxAttempts() {
        return this.loginMaxAttempts;
    }

    public void setLoginMaxAttempts(final int loginMaxAttempts) {
        this.loginMaxAttempts = loginMaxAttempts;
    }

    public int getLoginDelayFactor() {
        return this.loginDelayFactor;
    }

    public void setLoginDelayFactor(final int loginDelayFactor) {
        this.loginDelayFactor = loginDelayFactor;
    }

    public int getLoginDelayMaxTime() {
        return this.loginDelayMaxTime;
    }

    public void setLoginDelayMaxTime(final int loginDelayMaxTime) {
        this.loginDelayMaxTime = loginDelayMaxTime;
    }

    public int getLoginDelayBase() {
        return this.loginDelayBase;
    }

    public void setLoginDelayBase(final int loginInitialDelay) {
        this.loginDelayBase = loginInitialDelay;
    }

    public List<String> getAdminGroups() {
        return this.adminGroups;
    }

    public void setAdminGroups(final List<String> adminGroups) {
        this.adminGroups = adminGroups;
    }

    public List<String> getAdminUsers() {
        return this.adminUsers;
    }

    public void setAdminUsers(final List<String> adminUsers) {
        this.adminUsers = adminUsers;
    }

    public int getQueryMaxResultsLimit() {
        return this.queryMaxResultsLimit;
    }

    public ProcessEngineConfigurationImpl setQueryMaxResultsLimit(final int queryMaxResultsLimit) {
        this.queryMaxResultsLimit = queryMaxResultsLimit;
        return this;
    }

    public String getLoggingContextActivityId() {
        return this.loggingContextActivityId;
    }

    public ProcessEngineConfigurationImpl setLoggingContextActivityId(final String loggingContextActivityId) {
        this.loggingContextActivityId = loggingContextActivityId;
        return this;
    }

    public String getLoggingContextApplicationName() {
        return this.loggingContextApplicationName;
    }

    public ProcessEngineConfigurationImpl setLoggingContextApplicationName(final String loggingContextApplicationName) {
        this.loggingContextApplicationName = loggingContextApplicationName;
        return this;
    }

    public String getLoggingContextBusinessKey() {
        return this.loggingContextBusinessKey;
    }

    public ProcessEngineConfigurationImpl setLoggingContextBusinessKey(final String loggingContextBusinessKey) {
        this.loggingContextBusinessKey = loggingContextBusinessKey;
        return this;
    }

    public String getLoggingContextProcessDefinitionId() {
        return this.loggingContextProcessDefinitionId;
    }

    public ProcessEngineConfigurationImpl setLoggingContextProcessDefinitionId(final String loggingContextProcessDefinitionId) {
        this.loggingContextProcessDefinitionId = loggingContextProcessDefinitionId;
        return this;
    }

    public String getLoggingContextProcessInstanceId() {
        return this.loggingContextProcessInstanceId;
    }

    public ProcessEngineConfigurationImpl setLoggingContextProcessInstanceId(final String loggingContextProcessInstanceId) {
        this.loggingContextProcessInstanceId = loggingContextProcessInstanceId;
        return this;
    }

    public String getLoggingContextTenantId() {
        return this.loggingContextTenantId;
    }

    public ProcessEngineConfigurationImpl setLoggingContextTenantId(final String loggingContextTenantId) {
        this.loggingContextTenantId = loggingContextTenantId;
        return this;
    }

    public List<FeelCustomFunctionProvider> getDmnFeelCustomFunctionProviders() {
        return this.dmnFeelCustomFunctionProviders;
    }

    public ProcessEngineConfigurationImpl setDmnFeelCustomFunctionProviders(final List<FeelCustomFunctionProvider> dmnFeelCustomFunctionProviders) {
        this.dmnFeelCustomFunctionProviders = dmnFeelCustomFunctionProviders;
        return this;
    }

    public boolean isDmnFeelEnableLegacyBehavior() {
        return this.dmnFeelEnableLegacyBehavior;
    }

    public ProcessEngineConfigurationImpl setDmnFeelEnableLegacyBehavior(final boolean dmnFeelEnableLegacyBehavior) {
        this.dmnFeelEnableLegacyBehavior = dmnFeelEnableLegacyBehavior;
        return this;
    }

    public Boolean isInitializeTelemetry() {
        return this.initializeTelemetry;
    }

    public ProcessEngineConfigurationImpl setInitializeTelemetry(final boolean telemetryInitialized) {
        this.initializeTelemetry = telemetryInitialized;
        return this;
    }

    public String getTelemetryEndpoint() {
        return this.telemetryEndpoint;
    }

    public ProcessEngineConfigurationImpl setTelemetryEndpoint(final String telemetryEndpoint) {
        this.telemetryEndpoint = telemetryEndpoint;
        return this;
    }

    public int getTelemetryRequestRetries() {
        return this.telemetryRequestRetries;
    }

    public ProcessEngineConfigurationImpl setTelemetryRequestRetries(final int telemetryRequestRetries) {
        this.telemetryRequestRetries = telemetryRequestRetries;
        return this;
    }

    public long getTelemetryReportingPeriod() {
        return this.telemetryReportingPeriod;
    }

    public ProcessEngineConfigurationImpl setTelemetryReportingPeriod(final long telemetryReportingPeriod) {
        this.telemetryReportingPeriod = telemetryReportingPeriod;
        return this;
    }

    public TelemetryReporter getTelemetryReporter() {
        return this.telemetryReporter;
    }

    public ProcessEngineConfigurationImpl setTelemetryReporter(final TelemetryReporter telemetryReporter) {
        this.telemetryReporter = telemetryReporter;
        return this;
    }

    public boolean isTelemetryReporterActivate() {
        return this.isTelemetryReporterActivate;
    }

    public ProcessEngineConfigurationImpl setTelemetryReporterActivate(final boolean isTelemetryReporterActivate) {
        this.isTelemetryReporterActivate = isTelemetryReporterActivate;
        return this;
    }

    public Connector<? extends ConnectorRequest<?>> getTelemetryHttpConnector() {
        return this.telemetryHttpConnector;
    }

    public ProcessEngineConfigurationImpl setTelemetryHttpConnector(final Connector<? extends ConnectorRequest<?>> telemetryHttp) {
        this.telemetryHttpConnector = telemetryHttp;
        return this;
    }

    public TelemetryDataImpl getTelemetryData() {
        return this.telemetryData;
    }

    public ProcessEngineConfigurationImpl setTelemetryData(final TelemetryDataImpl telemetryData) {
        this.telemetryData = telemetryData;
        return this;
    }

    public int getTelemetryRequestTimeout() {
        return this.telemetryRequestTimeout;
    }

    public ProcessEngineConfigurationImpl setTelemetryRequestTimeout(final int telemetryRequestTimeout) {
        this.telemetryRequestTimeout = telemetryRequestTimeout;
        return this;
    }

    public ProcessEngineConfigurationImpl setCommandRetries(final int commandRetries) {
        this.commandRetries = commandRetries;
        return this;
    }

    public int getCommandRetries() {
        return this.commandRetries;
    }

    protected CrdbTransactionRetryInterceptor getCrdbRetryInterceptor() {
        return new CrdbTransactionRetryInterceptor(this.commandRetries);
    }

    static {
        LOG = ConfigurationLogger.CONFIG_LOGGER;
        HISTORYLEVEL_NONE = HistoryLevel.HISTORY_LEVEL_NONE.getId();
        HISTORYLEVEL_ACTIVITY = HistoryLevel.HISTORY_LEVEL_ACTIVITY.getId();
        HISTORYLEVEL_AUDIT = HistoryLevel.HISTORY_LEVEL_AUDIT.getId();
        HISTORYLEVEL_FULL = HistoryLevel.HISTORY_LEVEL_FULL.getId();
        DEFAULT_BEANS_MAP = new HashMap<Object, Object>();
        ProcessEngineConfigurationImpl.databaseTypeMappings = getDefaultDatabaseTypeMappings();
    }
}
