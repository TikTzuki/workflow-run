// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import java.util.Collection;
import java.util.HashSet;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.impl.db.sql.DbSqlSession;
import org.zik.bpm.engine.impl.db.sql.DbSqlSessionFactory;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.metrics.MetricsRegistry;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.telemetry.dto.LicenseKeyDataImpl;
import org.zik.bpm.engine.impl.telemetry.TelemetryRegistry;
import org.zik.bpm.engine.impl.cmd.GetTelemetryDataCmd;
import org.zik.bpm.engine.telemetry.TelemetryData;
import org.zik.bpm.engine.impl.cmd.IsTelemetryEnabledCmd;
import org.zik.bpm.engine.impl.cmd.TelemetryConfigureCmd;
import org.zik.bpm.engine.management.SchemaLogQuery;
import org.zik.bpm.engine.impl.batch.BatchStatisticsQueryImpl;
import org.zik.bpm.engine.batch.BatchStatisticsQuery;
import org.zik.bpm.engine.impl.cmd.ActivateBatchCmd;
import org.zik.bpm.engine.impl.cmd.SuspendBatchCmd;
import org.zik.bpm.engine.impl.batch.DeleteBatchCmd;
import org.zik.bpm.engine.impl.batch.BatchQueryImpl;
import org.zik.bpm.engine.batch.BatchQuery;
import org.zik.bpm.engine.impl.cmd.SetJobDefinitionPriorityCmd;
import org.zik.bpm.engine.impl.cmd.DeleteTaskMetricsCmd;
import org.zik.bpm.engine.impl.cmd.GetUniqueTaskWorkerCountCmd;
import org.zik.bpm.engine.impl.cmd.ReportDbMetricsCmd;
import org.zik.bpm.engine.impl.cmd.DeleteMetricsCmd;
import org.zik.bpm.engine.impl.metrics.MetricsQueryImpl;
import org.zik.bpm.engine.management.MetricsQuery;
import org.zik.bpm.engine.impl.cmd.GetHistoryLevelCmd;
import org.zik.bpm.engine.impl.management.UpdateJobSuspensionStateBuilderImpl;
import org.zik.bpm.engine.management.UpdateJobSuspensionStateSelectBuilder;
import org.zik.bpm.engine.impl.management.UpdateJobDefinitionSuspensionStateBuilderImpl;
import org.zik.bpm.engine.management.UpdateJobDefinitionSuspensionStateSelectBuilder;
import org.zik.bpm.engine.impl.cmd.UnregisterDeploymentCmd;
import org.zik.bpm.engine.impl.cmd.RegisterDeploymentCmd;
import org.zik.bpm.engine.management.DeploymentStatisticsQuery;
import org.zik.bpm.engine.management.ActivityStatisticsQuery;
import org.zik.bpm.engine.management.ProcessDefinitionStatisticsQuery;
import org.zik.bpm.engine.impl.cmd.PurgeDatabaseAndCacheCmd;
import org.zik.bpm.engine.impl.management.PurgeReport;
import java.sql.Connection;
import org.zik.bpm.engine.impl.cmd.DeleteLicenseKeyCmd;
import org.zik.bpm.engine.impl.cmd.GetLicenseKeyCmd;
import org.zik.bpm.engine.impl.cmd.SetLicenseKeyCmd;
import org.zik.bpm.engine.impl.cmd.DeletePropertyCmd;
import org.zik.bpm.engine.impl.cmd.SetPropertyCmd;
import org.zik.bpm.engine.impl.cmd.GetPropertiesCmd;
import org.zik.bpm.engine.impl.cmd.GetJobExceptionStacktraceCmd;
import org.zik.bpm.engine.management.JobDefinitionQuery;
import org.zik.bpm.engine.management.TablePageQuery;
import org.zik.bpm.engine.impl.cmd.SetJobPriorityCmd;
import org.zik.bpm.engine.impl.cmd.RecalculateJobDuedateCmd;
import org.zik.bpm.engine.impl.cmd.SetJobDuedateCmd;
import java.util.Date;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import org.zik.bpm.engine.impl.cmd.SetJobsRetriesBatchCmd;
import org.zik.bpm.engine.runtime.JobQuery;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.cmd.SetJobsRetriesCmd;
import java.util.List;
import org.zik.bpm.engine.impl.cmd.SetJobRetriesCmd;
import org.zik.bpm.engine.impl.cmd.DeleteJobCmd;
import org.zik.bpm.engine.impl.jobexecutor.ExecuteJobHelper;
import org.zik.bpm.engine.impl.cmd.GetTableMetaDataCmd;
import org.zik.bpm.engine.management.TableMetaData;
import org.zik.bpm.engine.impl.cmd.GetTableNameCmd;
import org.zik.bpm.engine.impl.cmd.GetTableCountCmd;
import java.util.Map;
import org.zik.bpm.engine.impl.cmd.GetProcessApplicationForDeploymentCmd;
import java.util.Set;
import org.zik.bpm.engine.impl.cmd.UnregisterProcessApplicationCmd;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.RegisterProcessApplicationCmd;
import org.zik.bpm.application.ProcessApplicationRegistration;
import org.zik.bpm.application.ProcessApplicationReference;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.ManagementService;

public class ManagementServiceImpl extends ServiceImpl implements ManagementService
{
    protected ProcessEngineConfiguration processEngineConfiguration;
    
    public ManagementServiceImpl(final ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }
    
    @Override
    public ProcessApplicationRegistration registerProcessApplication(final String deploymentId, final ProcessApplicationReference reference) {
        return this.commandExecutor.execute((Command<ProcessApplicationRegistration>)new RegisterProcessApplicationCmd(deploymentId, reference));
    }
    
    @Override
    public void unregisterProcessApplication(final String deploymentId, final boolean removeProcessesFromCache) {
        this.commandExecutor.execute((Command<Object>)new UnregisterProcessApplicationCmd(deploymentId, removeProcessesFromCache));
    }
    
    @Override
    public void unregisterProcessApplication(final Set<String> deploymentIds, final boolean removeProcessesFromCache) {
        this.commandExecutor.execute((Command<Object>)new UnregisterProcessApplicationCmd(deploymentIds, removeProcessesFromCache));
    }
    
    @Override
    public String getProcessApplicationForDeployment(final String deploymentId) {
        return this.commandExecutor.execute((Command<String>)new GetProcessApplicationForDeploymentCmd(deploymentId));
    }
    
    @Override
    public Map<String, Long> getTableCount() {
        return this.commandExecutor.execute((Command<Map<String, Long>>)new GetTableCountCmd());
    }
    
    @Override
    public String getTableName(final Class<?> activitiEntityClass) {
        return this.commandExecutor.execute((Command<String>)new GetTableNameCmd(activitiEntityClass));
    }
    
    @Override
    public TableMetaData getTableMetaData(final String tableName) {
        return this.commandExecutor.execute((Command<TableMetaData>)new GetTableMetaDataCmd(tableName));
    }
    
    @Override
    public void executeJob(final String jobId) {
        ExecuteJobHelper.executeJob(jobId, this.commandExecutor);
    }
    
    @Override
    public void deleteJob(final String jobId) {
        this.commandExecutor.execute((Command<Object>)new DeleteJobCmd(jobId));
    }
    
    @Override
    public void setJobRetries(final String jobId, final int retries) {
        this.commandExecutor.execute((Command<Object>)new SetJobRetriesCmd(jobId, null, retries));
    }
    
    @Override
    public void setJobRetries(final List<String> jobIds, final int retries) {
        this.commandExecutor.execute((Command<Object>)new SetJobsRetriesCmd(jobIds, retries));
    }
    
    @Override
    public Batch setJobRetriesAsync(final List<String> jobIds, final int retries) {
        return this.setJobRetriesAsync(jobIds, (JobQuery)null, retries);
    }
    
    @Override
    public Batch setJobRetriesAsync(final JobQuery jobQuery, final int retries) {
        return this.setJobRetriesAsync(null, jobQuery, retries);
    }
    
    @Override
    public Batch setJobRetriesAsync(final List<String> jobIds, final JobQuery jobQuery, final int retries) {
        return this.commandExecutor.execute((Command<Batch>)new SetJobsRetriesBatchCmd(jobIds, jobQuery, retries));
    }
    
    @Override
    public Batch setJobRetriesAsync(final List<String> processInstanceIds, final ProcessInstanceQuery query, final int retries) {
        return this.commandExecutor.execute((Command<Batch>)new SetJobsRetriesByProcessBatchCmd(processInstanceIds, query, null, retries));
    }
    
    @Override
    public Batch setJobRetriesAsync(final List<String> processInstanceIds, final ProcessInstanceQuery query, final HistoricProcessInstanceQuery historicProcessInstanceQuery, final int retries) {
        return this.commandExecutor.execute((Command<Batch>)new SetJobsRetriesByProcessBatchCmd(processInstanceIds, query, historicProcessInstanceQuery, retries));
    }
    
    @Override
    public void setJobRetriesByJobDefinitionId(final String jobDefinitionId, final int retries) {
        this.commandExecutor.execute((Command<Object>)new SetJobRetriesCmd(null, jobDefinitionId, retries));
    }
    
    @Override
    public void setJobDuedate(final String jobId, final Date newDuedate) {
        this.setJobDuedate(jobId, newDuedate, false);
    }
    
    @Override
    public void setJobDuedate(final String jobId, final Date newDuedate, final boolean cascade) {
        this.commandExecutor.execute((Command<Object>)new SetJobDuedateCmd(jobId, newDuedate, cascade));
    }
    
    @Override
    public void recalculateJobDuedate(final String jobId, final boolean creationDateBased) {
        this.commandExecutor.execute((Command<Object>)new RecalculateJobDuedateCmd(jobId, creationDateBased));
    }
    
    @Override
    public void setJobPriority(final String jobId, final long priority) {
        this.commandExecutor.execute((Command<Object>)new SetJobPriorityCmd(jobId, priority));
    }
    
    @Override
    public TablePageQuery createTablePageQuery() {
        return new TablePageQueryImpl(this.commandExecutor);
    }
    
    @Override
    public JobQuery createJobQuery() {
        return new JobQueryImpl(this.commandExecutor);
    }
    
    @Override
    public JobDefinitionQuery createJobDefinitionQuery() {
        return new JobDefinitionQueryImpl(this.commandExecutor);
    }
    
    @Override
    public String getJobExceptionStacktrace(final String jobId) {
        return this.commandExecutor.execute((Command<String>)new GetJobExceptionStacktraceCmd(jobId));
    }
    
    @Override
    public Map<String, String> getProperties() {
        return this.commandExecutor.execute((Command<Map<String, String>>)new GetPropertiesCmd());
    }
    
    @Override
    public void setProperty(final String name, final String value) {
        this.commandExecutor.execute((Command<Object>)new SetPropertyCmd(name, value));
    }
    
    @Override
    public void deleteProperty(final String name) {
        this.commandExecutor.execute((Command<Object>)new DeletePropertyCmd(name));
    }
    
    @Override
    public void setLicenseKey(final String licenseKey) {
        this.commandExecutor.execute((Command<Object>)new SetLicenseKeyCmd(licenseKey));
    }
    
    @Override
    public String getLicenseKey() {
        return this.commandExecutor.execute((Command<String>)new GetLicenseKeyCmd());
    }
    
    @Override
    public void deleteLicenseKey() {
        this.commandExecutor.execute((Command<Object>)new DeleteLicenseKeyCmd(true));
    }
    
    @Override
    public String databaseSchemaUpgrade(final Connection connection, final String catalog, final String schema) {
        return this.commandExecutor.execute((Command<String>)new DbSchemaUpgradeCmd(connection, catalog, schema));
    }
    
    public PurgeReport purge() {
        return this.commandExecutor.execute((Command<PurgeReport>)new PurgeDatabaseAndCacheCmd());
    }
    
    @Override
    public ProcessDefinitionStatisticsQuery createProcessDefinitionStatisticsQuery() {
        return new ProcessDefinitionStatisticsQueryImpl(this.commandExecutor);
    }
    
    @Override
    public ActivityStatisticsQuery createActivityStatisticsQuery(final String processDefinitionId) {
        return new ActivityStatisticsQueryImpl(processDefinitionId, this.commandExecutor);
    }
    
    @Override
    public DeploymentStatisticsQuery createDeploymentStatisticsQuery() {
        return new DeploymentStatisticsQueryImpl(this.commandExecutor);
    }
    
    @Override
    public Set<String> getRegisteredDeployments() {
        return this.commandExecutor.execute((Command<Set<String>>)new GetRegisteredDeploymentsCmd());
    }
    
    @Override
    public void registerDeploymentForJobExecutor(final String deploymentId) {
        this.commandExecutor.execute((Command<Object>)new RegisterDeploymentCmd(deploymentId));
    }
    
    @Override
    public void unregisterDeploymentForJobExecutor(final String deploymentId) {
        this.commandExecutor.execute((Command<Object>)new UnregisterDeploymentCmd(deploymentId));
    }
    
    @Override
    public void activateJobDefinitionById(final String jobDefinitionId) {
        this.updateJobDefinitionSuspensionState().byJobDefinitionId(jobDefinitionId).activate();
    }
    
    @Override
    public void activateJobDefinitionById(final String jobDefinitionId, final boolean activateJobs) {
        this.updateJobDefinitionSuspensionState().byJobDefinitionId(jobDefinitionId).includeJobs(activateJobs).activate();
    }
    
    @Override
    public void activateJobDefinitionById(final String jobDefinitionId, final boolean activateJobs, final Date activationDate) {
        this.updateJobDefinitionSuspensionState().byJobDefinitionId(jobDefinitionId).includeJobs(activateJobs).executionDate(activationDate).activate();
    }
    
    @Override
    public void suspendJobDefinitionById(final String jobDefinitionId) {
        this.updateJobDefinitionSuspensionState().byJobDefinitionId(jobDefinitionId).suspend();
    }
    
    @Override
    public void suspendJobDefinitionById(final String jobDefinitionId, final boolean suspendJobs) {
        this.updateJobDefinitionSuspensionState().byJobDefinitionId(jobDefinitionId).includeJobs(suspendJobs).suspend();
    }
    
    @Override
    public void suspendJobDefinitionById(final String jobDefinitionId, final boolean suspendJobs, final Date suspensionDate) {
        this.updateJobDefinitionSuspensionState().byJobDefinitionId(jobDefinitionId).includeJobs(suspendJobs).executionDate(suspensionDate).suspend();
    }
    
    @Override
    public void activateJobDefinitionByProcessDefinitionId(final String processDefinitionId) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionId(processDefinitionId).activate();
    }
    
    @Override
    public void activateJobDefinitionByProcessDefinitionId(final String processDefinitionId, final boolean activateJobs) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionId(processDefinitionId).includeJobs(activateJobs).activate();
    }
    
    @Override
    public void activateJobDefinitionByProcessDefinitionId(final String processDefinitionId, final boolean activateJobs, final Date activationDate) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionId(processDefinitionId).includeJobs(activateJobs).executionDate(activationDate).activate();
    }
    
    @Override
    public void suspendJobDefinitionByProcessDefinitionId(final String processDefinitionId) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionId(processDefinitionId).suspend();
    }
    
    @Override
    public void suspendJobDefinitionByProcessDefinitionId(final String processDefinitionId, final boolean suspendJobs) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionId(processDefinitionId).includeJobs(suspendJobs).suspend();
    }
    
    @Override
    public void suspendJobDefinitionByProcessDefinitionId(final String processDefinitionId, final boolean suspendJobs, final Date suspensionDate) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionId(processDefinitionId).includeJobs(suspendJobs).executionDate(suspensionDate).suspend();
    }
    
    @Override
    public void activateJobDefinitionByProcessDefinitionKey(final String processDefinitionKey) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionKey(processDefinitionKey).activate();
    }
    
    @Override
    public void activateJobDefinitionByProcessDefinitionKey(final String processDefinitionKey, final boolean activateJobs) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionKey(processDefinitionKey).includeJobs(activateJobs).activate();
    }
    
    @Override
    public void activateJobDefinitionByProcessDefinitionKey(final String processDefinitionKey, final boolean activateJobs, final Date activationDate) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionKey(processDefinitionKey).includeJobs(activateJobs).executionDate(activationDate).activate();
    }
    
    @Override
    public void suspendJobDefinitionByProcessDefinitionKey(final String processDefinitionKey) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionKey(processDefinitionKey).suspend();
    }
    
    @Override
    public void suspendJobDefinitionByProcessDefinitionKey(final String processDefinitionKey, final boolean suspendJobs) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionKey(processDefinitionKey).includeJobs(suspendJobs).suspend();
    }
    
    @Override
    public void suspendJobDefinitionByProcessDefinitionKey(final String processDefinitionKey, final boolean suspendJobs, final Date suspensionDate) {
        this.updateJobDefinitionSuspensionState().byProcessDefinitionKey(processDefinitionKey).includeJobs(suspendJobs).executionDate(suspensionDate).suspend();
    }
    
    @Override
    public UpdateJobDefinitionSuspensionStateSelectBuilder updateJobDefinitionSuspensionState() {
        return new UpdateJobDefinitionSuspensionStateBuilderImpl(this.commandExecutor);
    }
    
    @Override
    public void activateJobById(final String jobId) {
        this.updateJobSuspensionState().byJobId(jobId).activate();
    }
    
    @Override
    public void activateJobByProcessInstanceId(final String processInstanceId) {
        this.updateJobSuspensionState().byProcessInstanceId(processInstanceId).activate();
    }
    
    @Override
    public void activateJobByJobDefinitionId(final String jobDefinitionId) {
        this.updateJobSuspensionState().byJobDefinitionId(jobDefinitionId).activate();
    }
    
    @Override
    public void activateJobByProcessDefinitionId(final String processDefinitionId) {
        this.updateJobSuspensionState().byProcessDefinitionId(processDefinitionId).activate();
    }
    
    @Override
    public void activateJobByProcessDefinitionKey(final String processDefinitionKey) {
        this.updateJobSuspensionState().byProcessDefinitionKey(processDefinitionKey).activate();
    }
    
    @Override
    public void suspendJobById(final String jobId) {
        this.updateJobSuspensionState().byJobId(jobId).suspend();
    }
    
    @Override
    public void suspendJobByJobDefinitionId(final String jobDefinitionId) {
        this.updateJobSuspensionState().byJobDefinitionId(jobDefinitionId).suspend();
    }
    
    @Override
    public void suspendJobByProcessInstanceId(final String processInstanceId) {
        this.updateJobSuspensionState().byProcessInstanceId(processInstanceId).suspend();
    }
    
    @Override
    public void suspendJobByProcessDefinitionId(final String processDefinitionId) {
        this.updateJobSuspensionState().byProcessDefinitionId(processDefinitionId).suspend();
    }
    
    @Override
    public void suspendJobByProcessDefinitionKey(final String processDefinitionKey) {
        this.updateJobSuspensionState().byProcessDefinitionKey(processDefinitionKey).suspend();
    }
    
    @Override
    public UpdateJobSuspensionStateSelectBuilder updateJobSuspensionState() {
        return new UpdateJobSuspensionStateBuilderImpl(this.commandExecutor);
    }
    
    @Override
    public int getHistoryLevel() {
        return this.commandExecutor.execute((Command<Integer>)new GetHistoryLevelCmd());
    }
    
    @Override
    public MetricsQuery createMetricsQuery() {
        return new MetricsQueryImpl(this.commandExecutor);
    }
    
    @Override
    public void deleteMetrics(final Date timestamp) {
        this.commandExecutor.execute((Command<Object>)new DeleteMetricsCmd(timestamp, null));
    }
    
    @Override
    public void deleteMetrics(final Date timestamp, final String reporter) {
        this.commandExecutor.execute((Command<Object>)new DeleteMetricsCmd(timestamp, reporter));
    }
    
    @Override
    public void reportDbMetricsNow() {
        this.commandExecutor.execute((Command<Object>)new ReportDbMetricsCmd());
    }
    
    @Override
    public long getUniqueTaskWorkerCount(final Date startTime, final Date endTime) {
        return this.commandExecutor.execute((Command<Long>)new GetUniqueTaskWorkerCountCmd(startTime, endTime));
    }
    
    @Override
    public void deleteTaskMetrics(final Date timestamp) {
        this.commandExecutor.execute((Command<Object>)new DeleteTaskMetricsCmd(timestamp));
    }
    
    @Override
    public void setOverridingJobPriorityForJobDefinition(final String jobDefinitionId, final long priority) {
        this.commandExecutor.execute((Command<Object>)new SetJobDefinitionPriorityCmd(jobDefinitionId, priority, false));
    }
    
    @Override
    public void setOverridingJobPriorityForJobDefinition(final String jobDefinitionId, final long priority, final boolean cascade) {
        this.commandExecutor.execute((Command<Object>)new SetJobDefinitionPriorityCmd(jobDefinitionId, priority, true));
    }
    
    @Override
    public void clearOverridingJobPriorityForJobDefinition(final String jobDefinitionId) {
        this.commandExecutor.execute((Command<Object>)new SetJobDefinitionPriorityCmd(jobDefinitionId, null, false));
    }
    
    @Override
    public BatchQuery createBatchQuery() {
        return new BatchQueryImpl(this.commandExecutor);
    }
    
    @Override
    public void deleteBatch(final String batchId, final boolean cascade) {
        this.commandExecutor.execute((Command<Object>)new DeleteBatchCmd(batchId, cascade));
    }
    
    @Override
    public void suspendBatchById(final String batchId) {
        this.commandExecutor.execute((Command<Object>)new SuspendBatchCmd(batchId));
    }
    
    @Override
    public void activateBatchById(final String batchId) {
        this.commandExecutor.execute((Command<Object>)new ActivateBatchCmd(batchId));
    }
    
    @Override
    public BatchStatisticsQuery createBatchStatisticsQuery() {
        return new BatchStatisticsQueryImpl(this.commandExecutor);
    }
    
    @Override
    public SchemaLogQuery createSchemaLogQuery() {
        return new SchemaLogQueryImpl(this.commandExecutor);
    }
    
    @Override
    public void toggleTelemetry(final boolean enabled) {
        this.commandExecutor.execute((Command<Object>)new TelemetryConfigureCmd(enabled));
    }
    
    @Override
    public Boolean isTelemetryEnabled() {
        return this.commandExecutor.execute((Command<Boolean>)new IsTelemetryEnabledCmd());
    }
    
    @Override
    public TelemetryData getTelemetryData() {
        return this.commandExecutor.execute((Command<TelemetryData>)new GetTelemetryDataCmd());
    }
    
    public boolean addWebappToTelemetry(final String webapp) {
        final TelemetryRegistry telemetryRegistry = this.processEngineConfiguration.getTelemetryRegistry();
        if (telemetryRegistry != null) {
            telemetryRegistry.addWebapp(webapp);
            return true;
        }
        return false;
    }
    
    public void addApplicationServerInfoToTelemetry(final String appServerInfo) {
        final TelemetryRegistry telemetryRegistry = this.processEngineConfiguration.getTelemetryRegistry();
        if (telemetryRegistry != null) {
            telemetryRegistry.setApplicationServer(appServerInfo);
        }
    }
    
    public void setLicenseKeyForTelemetry(final LicenseKeyDataImpl licenseKeyData) {
        final TelemetryRegistry telemetryRegistry = this.processEngineConfiguration.getTelemetryRegistry();
        if (telemetryRegistry != null) {
            telemetryRegistry.setLicenseKey(licenseKeyData);
        }
    }
    
    public LicenseKeyDataImpl getLicenseKeyFromTelemetry() {
        final TelemetryRegistry telemetryRegistry = this.processEngineConfiguration.getTelemetryRegistry();
        if (telemetryRegistry != null) {
            return telemetryRegistry.getLicenseKey();
        }
        return null;
    }
    
    public void clearTelemetryData() {
        final TelemetryRegistry telemetryRegistry = this.processEngineConfiguration.getTelemetryRegistry();
        if (telemetryRegistry != null) {
            telemetryRegistry.clear();
        }
        final MetricsRegistry metricsRegistry = ((ProcessEngineConfigurationImpl)this.processEngineConfiguration).getMetricsRegistry();
        if (metricsRegistry != null) {
            metricsRegistry.clearTelemetryMetrics();
        }
        this.deleteMetrics(null);
    }
    
    protected class DbSchemaUpgradeCmd implements Command<String>
    {
        protected Connection connection;
        protected String catalog;
        protected String schema;
        
        public DbSchemaUpgradeCmd(final Connection connection, final String catalog, final String schema) {
            this.connection = connection;
            this.catalog = catalog;
            this.schema = schema;
        }
        
        @Override
        public String execute(final CommandContext commandContext) {
            commandContext.getAuthorizationManager().checkCamundaAdmin();
            final DbSqlSessionFactory dbSqlSessionFactory = commandContext.getSessionFactories().get(DbSqlSession.class);
            final DbSqlSession dbSqlSession = dbSqlSessionFactory.openSession(this.connection, this.catalog, this.schema);
            commandContext.getSessions().put(DbSqlSession.class, dbSqlSession);
            dbSqlSession.dbSchemaUpdate();
            return "";
        }
    }
    
    protected class GetRegisteredDeploymentsCmd implements Command<Set<String>>
    {
        @Override
        public Set<String> execute(final CommandContext commandContext) {
            commandContext.getAuthorizationManager().checkCamundaAdminOrPermission(CommandChecker::checkReadRegisteredDeployments);
            final Set<String> registeredDeployments = Context.getProcessEngineConfiguration().getRegisteredDeployments();
            return new HashSet<String>(registeredDeployments);
        }
    }
}
