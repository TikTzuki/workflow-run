// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine;

import org.zik.bpm.engine.history.SetRemovalTimeSelectModeForHistoricBatchesBuilder;
import org.zik.bpm.engine.history.SetRemovalTimeSelectModeForHistoricDecisionInstancesBuilder;
import org.zik.bpm.engine.history.SetRemovalTimeSelectModeForHistoricProcessInstancesBuilder;
import org.zik.bpm.engine.history.HistoricExternalTaskLogQuery;
import org.zik.bpm.engine.history.HistoricDecisionInstanceStatisticsQuery;
import org.zik.bpm.engine.batch.history.HistoricBatchQuery;
import org.zik.bpm.engine.history.CleanableHistoricBatchReport;
import org.zik.bpm.engine.history.CleanableHistoricCaseInstanceReport;
import org.zik.bpm.engine.history.CleanableHistoricDecisionInstanceReport;
import org.zik.bpm.engine.history.CleanableHistoricProcessInstanceReport;
import org.zik.bpm.engine.history.HistoricTaskInstanceReport;
import org.zik.bpm.engine.history.HistoricProcessInstanceReport;
import org.zik.bpm.engine.history.HistoricJobLogQuery;
import org.zik.bpm.engine.history.NativeHistoricVariableInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricDecisionInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricCaseActivityInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricCaseInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricActivityInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricTaskInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricProcessInstanceQuery;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.runtime.Job;
import java.util.List;
import org.zik.bpm.engine.history.HistoricDecisionInstanceQuery;
import org.zik.bpm.engine.history.HistoricCaseActivityInstanceQuery;
import org.zik.bpm.engine.history.HistoricCaseInstanceQuery;
import org.zik.bpm.engine.history.HistoricIdentityLinkLogQuery;
import org.zik.bpm.engine.history.HistoricIncidentQuery;
import org.zik.bpm.engine.history.UserOperationLogQuery;
import org.zik.bpm.engine.history.HistoricVariableInstanceQuery;
import org.zik.bpm.engine.history.HistoricDetailQuery;
import org.zik.bpm.engine.history.HistoricTaskInstanceQuery;
import org.zik.bpm.engine.history.HistoricCaseActivityStatisticsQuery;
import org.zik.bpm.engine.history.HistoricActivityStatisticsQuery;
import org.zik.bpm.engine.history.HistoricActivityInstanceQuery;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;

public interface HistoryService
{
    HistoricProcessInstanceQuery createHistoricProcessInstanceQuery();
    
    HistoricActivityInstanceQuery createHistoricActivityInstanceQuery();
    
    HistoricActivityStatisticsQuery createHistoricActivityStatisticsQuery(final String p0);
    
    HistoricCaseActivityStatisticsQuery createHistoricCaseActivityStatisticsQuery(final String p0);
    
    HistoricTaskInstanceQuery createHistoricTaskInstanceQuery();
    
    HistoricDetailQuery createHistoricDetailQuery();
    
    HistoricVariableInstanceQuery createHistoricVariableInstanceQuery();
    
    UserOperationLogQuery createUserOperationLogQuery();
    
    HistoricIncidentQuery createHistoricIncidentQuery();
    
    HistoricIdentityLinkLogQuery createHistoricIdentityLinkLogQuery();
    
    HistoricCaseInstanceQuery createHistoricCaseInstanceQuery();
    
    HistoricCaseActivityInstanceQuery createHistoricCaseActivityInstanceQuery();
    
    HistoricDecisionInstanceQuery createHistoricDecisionInstanceQuery();
    
    void deleteHistoricTaskInstance(final String p0);
    
    void deleteHistoricProcessInstance(final String p0);
    
    void deleteHistoricProcessInstanceIfExists(final String p0);
    
    void deleteHistoricProcessInstances(final List<String> p0);
    
    void deleteHistoricProcessInstancesIfExists(final List<String> p0);
    
    void deleteHistoricProcessInstancesBulk(final List<String> p0);
    
    Job cleanUpHistoryAsync();
    
    Job cleanUpHistoryAsync(final boolean p0);
    
    @Deprecated
    Job findHistoryCleanupJob();
    
    List<Job> findHistoryCleanupJobs();
    
    Batch deleteHistoricProcessInstancesAsync(final List<String> p0, final String p1);
    
    Batch deleteHistoricProcessInstancesAsync(final HistoricProcessInstanceQuery p0, final String p1);
    
    Batch deleteHistoricProcessInstancesAsync(final List<String> p0, final HistoricProcessInstanceQuery p1, final String p2);
    
    void deleteUserOperationLogEntry(final String p0);
    
    void deleteHistoricCaseInstance(final String p0);
    
    void deleteHistoricCaseInstancesBulk(final List<String> p0);
    
    @Deprecated
    void deleteHistoricDecisionInstance(final String p0);
    
    void deleteHistoricDecisionInstancesBulk(final List<String> p0);
    
    void deleteHistoricDecisionInstanceByDefinitionId(final String p0);
    
    void deleteHistoricDecisionInstanceByInstanceId(final String p0);
    
    Batch deleteHistoricDecisionInstancesAsync(final List<String> p0, final String p1);
    
    Batch deleteHistoricDecisionInstancesAsync(final HistoricDecisionInstanceQuery p0, final String p1);
    
    Batch deleteHistoricDecisionInstancesAsync(final List<String> p0, final HistoricDecisionInstanceQuery p1, final String p2);
    
    void deleteHistoricVariableInstance(final String p0);
    
    void deleteHistoricVariableInstancesByProcessInstanceId(final String p0);
    
    NativeHistoricProcessInstanceQuery createNativeHistoricProcessInstanceQuery();
    
    NativeHistoricTaskInstanceQuery createNativeHistoricTaskInstanceQuery();
    
    NativeHistoricActivityInstanceQuery createNativeHistoricActivityInstanceQuery();
    
    NativeHistoricCaseInstanceQuery createNativeHistoricCaseInstanceQuery();
    
    NativeHistoricCaseActivityInstanceQuery createNativeHistoricCaseActivityInstanceQuery();
    
    NativeHistoricDecisionInstanceQuery createNativeHistoricDecisionInstanceQuery();
    
    NativeHistoricVariableInstanceQuery createNativeHistoricVariableInstanceQuery();
    
    HistoricJobLogQuery createHistoricJobLogQuery();
    
    String getHistoricJobLogExceptionStacktrace(final String p0);
    
    HistoricProcessInstanceReport createHistoricProcessInstanceReport();
    
    HistoricTaskInstanceReport createHistoricTaskInstanceReport();
    
    CleanableHistoricProcessInstanceReport createCleanableHistoricProcessInstanceReport();
    
    CleanableHistoricDecisionInstanceReport createCleanableHistoricDecisionInstanceReport();
    
    CleanableHistoricCaseInstanceReport createCleanableHistoricCaseInstanceReport();
    
    CleanableHistoricBatchReport createCleanableHistoricBatchReport();
    
    HistoricBatchQuery createHistoricBatchQuery();
    
    void deleteHistoricBatch(final String p0);
    
    HistoricDecisionInstanceStatisticsQuery createHistoricDecisionInstanceStatisticsQuery(final String p0);
    
    HistoricExternalTaskLogQuery createHistoricExternalTaskLogQuery();
    
    String getHistoricExternalTaskLogErrorDetails(final String p0);
    
    SetRemovalTimeSelectModeForHistoricProcessInstancesBuilder setRemovalTimeToHistoricProcessInstances();
    
    SetRemovalTimeSelectModeForHistoricDecisionInstancesBuilder setRemovalTimeToHistoricDecisionInstances();
    
    SetRemovalTimeSelectModeForHistoricBatchesBuilder setRemovalTimeToHistoricBatches();
    
    void setAnnotationForOperationLogById(final String p0, final String p1);
    
    void clearAnnotationForOperationLogById(final String p0);
}
