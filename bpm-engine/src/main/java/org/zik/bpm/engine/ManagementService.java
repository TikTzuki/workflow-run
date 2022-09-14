// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.telemetry.TelemetryData;
import org.zik.bpm.engine.management.SchemaLogQuery;
import org.zik.bpm.engine.batch.BatchStatisticsQuery;
import org.zik.bpm.engine.batch.BatchQuery;
import org.zik.bpm.engine.management.MetricsQuery;
import org.zik.bpm.engine.management.ActivityStatisticsQuery;
import org.zik.bpm.engine.management.DeploymentStatisticsQuery;
import org.zik.bpm.engine.management.ProcessDefinitionStatisticsQuery;
import java.sql.Connection;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.runtime.ProcessInstanceQuery;
import org.zik.bpm.engine.batch.Batch;
import java.util.List;
import org.zik.bpm.engine.management.UpdateJobDefinitionSuspensionStateSelectBuilder;
import org.zik.bpm.engine.management.UpdateJobSuspensionStateSelectBuilder;
import java.util.Date;
import org.zik.bpm.engine.management.JobDefinitionQuery;
import org.zik.bpm.engine.runtime.JobQuery;
import org.zik.bpm.engine.management.TablePageQuery;
import org.zik.bpm.engine.management.TableMetaData;
import java.util.Map;
import java.util.Set;
import org.zik.bpm.application.ProcessApplicationRegistration;
import org.zik.bpm.application.ProcessApplicationReference;

public interface ManagementService
{
    ProcessApplicationRegistration registerProcessApplication(final String p0, final ProcessApplicationReference p1);
    
    void unregisterProcessApplication(final String p0, final boolean p1);
    
    void unregisterProcessApplication(final Set<String> p0, final boolean p1);
    
    String getProcessApplicationForDeployment(final String p0);
    
    Map<String, Long> getTableCount();
    
    String getTableName(final Class<?> p0);
    
    TableMetaData getTableMetaData(final String p0);
    
    TablePageQuery createTablePageQuery();
    
    JobQuery createJobQuery();
    
    JobDefinitionQuery createJobDefinitionQuery();
    
    void executeJob(final String p0);
    
    void deleteJob(final String p0);
    
    void activateJobDefinitionById(final String p0);
    
    void activateJobDefinitionByProcessDefinitionId(final String p0);
    
    void activateJobDefinitionByProcessDefinitionKey(final String p0);
    
    void activateJobDefinitionById(final String p0, final boolean p1);
    
    void activateJobDefinitionByProcessDefinitionId(final String p0, final boolean p1);
    
    void activateJobDefinitionByProcessDefinitionKey(final String p0, final boolean p1);
    
    void activateJobDefinitionById(final String p0, final boolean p1, final Date p2);
    
    void activateJobDefinitionByProcessDefinitionId(final String p0, final boolean p1, final Date p2);
    
    void activateJobDefinitionByProcessDefinitionKey(final String p0, final boolean p1, final Date p2);
    
    void suspendJobDefinitionById(final String p0);
    
    void suspendJobDefinitionByProcessDefinitionId(final String p0);
    
    void suspendJobDefinitionByProcessDefinitionKey(final String p0);
    
    void suspendJobDefinitionById(final String p0, final boolean p1);
    
    void suspendJobDefinitionByProcessDefinitionId(final String p0, final boolean p1);
    
    void suspendJobDefinitionByProcessDefinitionKey(final String p0, final boolean p1);
    
    void suspendJobDefinitionById(final String p0, final boolean p1, final Date p2);
    
    void suspendJobDefinitionByProcessDefinitionId(final String p0, final boolean p1, final Date p2);
    
    void suspendJobDefinitionByProcessDefinitionKey(final String p0, final boolean p1, final Date p2);
    
    void activateJobById(final String p0);
    
    void activateJobByJobDefinitionId(final String p0);
    
    void activateJobByProcessInstanceId(final String p0);
    
    void activateJobByProcessDefinitionId(final String p0);
    
    void activateJobByProcessDefinitionKey(final String p0);
    
    void suspendJobById(final String p0);
    
    void suspendJobByJobDefinitionId(final String p0);
    
    void suspendJobByProcessInstanceId(final String p0);
    
    void suspendJobByProcessDefinitionId(final String p0);
    
    void suspendJobByProcessDefinitionKey(final String p0);
    
    UpdateJobSuspensionStateSelectBuilder updateJobSuspensionState();
    
    UpdateJobDefinitionSuspensionStateSelectBuilder updateJobDefinitionSuspensionState();
    
    void setJobRetries(final String p0, final int p1);
    
    void setJobRetries(final List<String> p0, final int p1);
    
    Batch setJobRetriesAsync(final List<String> p0, final int p1);
    
    Batch setJobRetriesAsync(final JobQuery p0, final int p1);
    
    Batch setJobRetriesAsync(final List<String> p0, final JobQuery p1, final int p2);
    
    Batch setJobRetriesAsync(final List<String> p0, final ProcessInstanceQuery p1, final int p2);
    
    Batch setJobRetriesAsync(final List<String> p0, final ProcessInstanceQuery p1, final HistoricProcessInstanceQuery p2, final int p3);
    
    void setJobRetriesByJobDefinitionId(final String p0, final int p1);
    
    void setJobDuedate(final String p0, final Date p1);
    
    void setJobDuedate(final String p0, final Date p1, final boolean p2);
    
    void recalculateJobDuedate(final String p0, final boolean p1);
    
    void setJobPriority(final String p0, final long p1);
    
    void setOverridingJobPriorityForJobDefinition(final String p0, final long p1);
    
    void setOverridingJobPriorityForJobDefinition(final String p0, final long p1, final boolean p2);
    
    void clearOverridingJobPriorityForJobDefinition(final String p0);
    
    String getJobExceptionStacktrace(final String p0);
    
    Map<String, String> getProperties();
    
    void setProperty(final String p0, final String p1);
    
    void deleteProperty(final String p0);
    
    void setLicenseKey(final String p0);
    
    String getLicenseKey();
    
    void deleteLicenseKey();
    
    String databaseSchemaUpgrade(final Connection p0, final String p1, final String p2);
    
    ProcessDefinitionStatisticsQuery createProcessDefinitionStatisticsQuery();
    
    DeploymentStatisticsQuery createDeploymentStatisticsQuery();
    
    ActivityStatisticsQuery createActivityStatisticsQuery(final String p0);
    
    Set<String> getRegisteredDeployments();
    
    void registerDeploymentForJobExecutor(final String p0);
    
    void unregisterDeploymentForJobExecutor(final String p0);
    
    int getHistoryLevel();
    
    MetricsQuery createMetricsQuery();
    
    void deleteMetrics(final Date p0);
    
    void deleteMetrics(final Date p0, final String p1);
    
    void reportDbMetricsNow();
    
    long getUniqueTaskWorkerCount(final Date p0, final Date p1);
    
    void deleteTaskMetrics(final Date p0);
    
    BatchQuery createBatchQuery();
    
    void suspendBatchById(final String p0);
    
    void activateBatchById(final String p0);
    
    void deleteBatch(final String p0, final boolean p1);
    
    BatchStatisticsQuery createBatchStatisticsQuery();
    
    SchemaLogQuery createSchemaLogQuery();
    
    void toggleTelemetry(final boolean p0);
    
    Boolean isTelemetryEnabled();
    
    TelemetryData getTelemetryData();
}
