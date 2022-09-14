// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.net.URL;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.impl.telemetry.TelemetryLogger;
import org.zik.bpm.engine.impl.identity.IndentityLogger;
import org.zik.bpm.engine.impl.incident.IncidentLogger;
import org.zik.bpm.engine.impl.digest.SecurityLogger;
import org.zik.bpm.engine.impl.externaltask.ExternalTaskLogger;
import org.zik.bpm.engine.impl.migration.MigrationLogger;
import org.zik.bpm.engine.impl.dmn.DecisionLogger;
import org.zik.bpm.engine.impl.scripting.ScriptLogger;
import org.zik.bpm.engine.impl.pvm.PvmLogger;
import org.zik.bpm.engine.impl.plugin.AdministratorAuthorizationPluginLogger;
import org.zik.bpm.engine.impl.metrics.MetricsLogger;
import org.zik.bpm.engine.impl.core.CoreLogger;
import org.zik.bpm.engine.impl.interceptor.ContextLogger;
import org.zik.bpm.engine.impl.test.TestLogger;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutorLogger;
import org.zik.bpm.engine.impl.cmd.CommandLogger;
import org.zik.bpm.engine.impl.cfg.ConfigurationLogger;
import org.zik.bpm.engine.impl.cfg.TransactionLogger;
import org.zik.bpm.engine.impl.util.EngineUtilLogger;
import org.zik.bpm.container.impl.ContainerIntegrationLogger;
import org.zik.bpm.application.impl.ProcessApplicationLogger;
import org.zik.bpm.engine.impl.cmmn.operation.CmmnOperationLogger;
import org.zik.bpm.engine.impl.cmmn.behavior.CmmnBehaviorLogger;
import org.zik.bpm.engine.impl.cmmn.transformer.CmmnTransformerLogger;
import org.zik.bpm.engine.impl.db.EnginePersistenceLogger;
import org.zik.bpm.engine.impl.bpmn.behavior.BpmnBehaviorLogger;
import org.zik.bpm.engine.impl.bpmn.parser.BpmnParseLogger;
import org.camunda.commons.logging.BaseLogger;

public class ProcessEngineLogger extends BaseLogger
{
    public static final String PROJECT_CODE = "ENGINE";
    public static final ProcessEngineLogger INSTANCE;
    public static final BpmnParseLogger BPMN_PARSE_LOGGER;
    public static final BpmnBehaviorLogger BPMN_BEHAVIOR_LOGGER;
    public static final EnginePersistenceLogger PERSISTENCE_LOGGER;
    public static final CmmnTransformerLogger CMMN_TRANSFORMER_LOGGER;
    public static final CmmnBehaviorLogger CMNN_BEHAVIOR_LOGGER;
    public static final CmmnOperationLogger CMMN_OPERATION_LOGGER;
    public static final ProcessApplicationLogger PROCESS_APPLICATION_LOGGER;
    public static final ContainerIntegrationLogger CONTAINER_INTEGRATION_LOGGER;
    public static final EngineUtilLogger UTIL_LOGGER;
    public static final TransactionLogger TX_LOGGER;
    public static final ConfigurationLogger CONFIG_LOGGER;
    public static final CommandLogger CMD_LOGGER;
    public static final JobExecutorLogger JOB_EXECUTOR_LOGGER;
    public static final TestLogger TEST_LOGGER;
    public static final ContextLogger CONTEXT_LOGGER;
    public static final CoreLogger CORE_LOGGER;
    public static final MetricsLogger METRICS_LOGGER;
    public static final AdministratorAuthorizationPluginLogger ADMIN_PLUGIN_LOGGER;
    public static final PvmLogger PVM_LOGGER;
    public static final ScriptLogger SCRIPT_LOGGER;
    public static final DecisionLogger DECISION_LOGGER;
    public static final MigrationLogger MIGRATION_LOGGER;
    public static final ExternalTaskLogger EXTERNAL_TASK_LOGGER;
    public static final SecurityLogger SECURITY_LOGGER;
    public static final IncidentLogger INCIDENT_LOGGER;
    public static final IndentityLogger INDENTITY_LOGGER;
    public static final TelemetryLogger TELEMETRY_LOGGER;
    
    public static boolean shouldLogJobException(final ProcessEngineConfiguration processEngineConfiguration, final JobEntity currentJob) {
        final boolean enableReducedJobExceptionLogging = processEngineConfiguration.isEnableReducedJobExceptionLogging();
        return currentJob == null || !enableReducedJobExceptionLogging || (enableReducedJobExceptionLogging && currentJob.getRetries() <= 1);
    }
    
    public static boolean shouldLogCmdException(final ProcessEngineConfiguration processEngineConfiguration) {
        return processEngineConfiguration.isEnableCmdExceptionLogging();
    }
    
    public void processEngineCreated(final String name) {
        this.logInfo("001", "Process Engine {} created.", new Object[] { name });
    }
    
    public void processEngineAlreadyInitialized() {
        this.logInfo("002", "Process engine already initialized", new Object[0]);
    }
    
    public void initializingProcessEngineForResource(final URL resourceUrl) {
        this.logInfo("003", "Initializing process engine for resource {}", new Object[] { resourceUrl });
    }
    
    public void initializingProcessEngine(final String name) {
        this.logInfo("004", "Initializing process engine {}", new Object[] { name });
    }
    
    public void exceptionWhileInitializingProcessengine(final Throwable e) {
        this.logError("005", "Exception while initializing process engine {}", new Object[] { e.getMessage(), e });
    }
    
    public void exceptionWhileClosingProcessEngine(final String string, final Exception e) {
        this.logError("006", "Exception while closing process engine {}", new Object[] { string, e });
    }
    
    public void processEngineClosed(final String name) {
        this.logInfo("007", "Process Engine {} closed", new Object[] { name });
    }
    
    public void historyCleanupJobReconfigurationFailure(final Exception exception) {
        this.logInfo("008", "History Cleanup Job reconfiguration failed on Process Engine Bootstrap. Possible concurrent execution with the JobExecutor: {}", new Object[] { exception.getMessage() });
    }
    
    public void couldNotDetermineIp(final Exception e) {
        this.logWarn("009", "Could not determine local IP address for generating a host name", new Object[] { e });
    }
    
    static {
        INSTANCE = (ProcessEngineLogger)BaseLogger.createLogger((Class)ProcessEngineLogger.class, "ENGINE", "org.camunda.bpm.engine", "00");
        BPMN_PARSE_LOGGER = (BpmnParseLogger)BaseLogger.createLogger((Class)BpmnParseLogger.class, "ENGINE", "org.camunda.bpm.engine.bpmn.parser", "01");
        BPMN_BEHAVIOR_LOGGER = (BpmnBehaviorLogger)BaseLogger.createLogger((Class)BpmnBehaviorLogger.class, "ENGINE", "org.camunda.bpm.engine.bpmn.behavior", "02");
        PERSISTENCE_LOGGER = (EnginePersistenceLogger)BaseLogger.createLogger((Class)EnginePersistenceLogger.class, "ENGINE", "org.camunda.bpm.engine.persistence", "03");
        CMMN_TRANSFORMER_LOGGER = (CmmnTransformerLogger)BaseLogger.createLogger((Class)CmmnTransformerLogger.class, "ENGINE", "org.camunda.bpm.engine.cmmn.transformer", "04");
        CMNN_BEHAVIOR_LOGGER = (CmmnBehaviorLogger)BaseLogger.createLogger((Class)CmmnBehaviorLogger.class, "ENGINE", "org.camunda.bpm.engine.cmmn.behavior", "05");
        CMMN_OPERATION_LOGGER = (CmmnOperationLogger)BaseLogger.createLogger((Class)CmmnOperationLogger.class, "ENGINE", "org.camunda.bpm.engine.cmmn.operation", "06");
        PROCESS_APPLICATION_LOGGER = (ProcessApplicationLogger)BaseLogger.createLogger((Class)ProcessApplicationLogger.class, "ENGINE", "org.camunda.bpm.application", "07");
        CONTAINER_INTEGRATION_LOGGER = (ContainerIntegrationLogger)BaseLogger.createLogger((Class)ContainerIntegrationLogger.class, "ENGINE", "org.camunda.bpm.container", "08");
        UTIL_LOGGER = (EngineUtilLogger)BaseLogger.createLogger((Class)EngineUtilLogger.class, "ENGINE", "org.camunda.bpm.engine.util", "09");
        TX_LOGGER = (TransactionLogger)BaseLogger.createLogger((Class)TransactionLogger.class, "ENGINE", "org.camunda.bpm.engine.tx", "11");
        CONFIG_LOGGER = (ConfigurationLogger)BaseLogger.createLogger((Class)ConfigurationLogger.class, "ENGINE", "org.camunda.bpm.engine.cfg", "12");
        CMD_LOGGER = (CommandLogger)BaseLogger.createLogger((Class)CommandLogger.class, "ENGINE", "org.camunda.bpm.engine.cmd", "13");
        JOB_EXECUTOR_LOGGER = (JobExecutorLogger)BaseLogger.createLogger((Class)JobExecutorLogger.class, "ENGINE", "org.camunda.bpm.engine.jobexecutor", "14");
        TEST_LOGGER = (TestLogger)BaseLogger.createLogger((Class)TestLogger.class, "ENGINE", "org.camunda.bpm.engine.test", "15");
        CONTEXT_LOGGER = (ContextLogger)BaseLogger.createLogger((Class)ContextLogger.class, "ENGINE", "org.camunda.bpm.engine.context", "16");
        CORE_LOGGER = (CoreLogger)BaseLogger.createLogger((Class)CoreLogger.class, "ENGINE", "org.camunda.bpm.engine.core", "17");
        METRICS_LOGGER = (MetricsLogger)BaseLogger.createLogger((Class)MetricsLogger.class, "ENGINE", "org.camunda.bpm.engine.metrics", "18");
        ADMIN_PLUGIN_LOGGER = (AdministratorAuthorizationPluginLogger)BaseLogger.createLogger((Class)AdministratorAuthorizationPluginLogger.class, "ENGINE", "org.camunda.bpm.engine.plugin.admin", "19");
        PVM_LOGGER = (PvmLogger)BaseLogger.createLogger((Class)PvmLogger.class, "ENGINE", "org.camunda.bpm.engine.pvm", "20");
        SCRIPT_LOGGER = (ScriptLogger)BaseLogger.createLogger((Class)ScriptLogger.class, "ENGINE", "org.camunda.bpm.engine.script", "21");
        DECISION_LOGGER = (DecisionLogger)BaseLogger.createLogger((Class)DecisionLogger.class, "ENGINE", "org.camunda.bpm.engine.dmn", "22");
        MIGRATION_LOGGER = (MigrationLogger)BaseLogger.createLogger((Class)MigrationLogger.class, "ENGINE", "org.camunda.bpm.engine.migration", "23");
        EXTERNAL_TASK_LOGGER = (ExternalTaskLogger)BaseLogger.createLogger((Class)ExternalTaskLogger.class, "ENGINE", "org.camunda.bpm.engine.externaltask", "24");
        SECURITY_LOGGER = (SecurityLogger)BaseLogger.createLogger((Class)SecurityLogger.class, "ENGINE", "org.camunda.bpm.engine.security", "25");
        INCIDENT_LOGGER = (IncidentLogger)BaseLogger.createLogger((Class)IncidentLogger.class, "ENGINE", "org.camunda.bpm.engine.incident", "26");
        INDENTITY_LOGGER = (IndentityLogger)BaseLogger.createLogger((Class)IndentityLogger.class, "ENGINE", "org.camunda.bpm.engine.identity", "27");
        TELEMETRY_LOGGER = (TelemetryLogger)BaseLogger.createLogger((Class)TelemetryLogger.class, "ENGINE", "org.camunda.bpm.engine.telemetry", "28");
    }
}
