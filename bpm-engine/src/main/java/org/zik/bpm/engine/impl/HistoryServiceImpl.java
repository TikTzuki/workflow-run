// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.history.HistoricIdentityLinkLogQuery;
import org.zik.bpm.engine.impl.history.SetRemovalTimeToHistoricBatchesBuilderImpl;
import org.zik.bpm.engine.history.SetRemovalTimeSelectModeForHistoricBatchesBuilder;
import org.zik.bpm.engine.impl.history.SetRemovalTimeToHistoricDecisionInstancesBuilderImpl;
import org.zik.bpm.engine.history.SetRemovalTimeSelectModeForHistoricDecisionInstancesBuilder;
import org.zik.bpm.engine.impl.history.SetRemovalTimeToHistoricProcessInstancesBuilderImpl;
import org.zik.bpm.engine.history.SetRemovalTimeSelectModeForHistoricProcessInstancesBuilder;
import org.zik.bpm.engine.impl.cmd.GetHistoricExternalTaskLogErrorDetailsCmd;
import org.zik.bpm.engine.history.HistoricExternalTaskLogQuery;
import org.zik.bpm.engine.history.HistoricDecisionInstanceStatisticsQuery;
import org.zik.bpm.engine.impl.batch.history.DeleteHistoricBatchCmd;
import org.zik.bpm.engine.impl.batch.history.HistoricBatchQueryImpl;
import org.zik.bpm.engine.batch.history.HistoricBatchQuery;
import org.zik.bpm.engine.history.CleanableHistoricBatchReport;
import org.zik.bpm.engine.history.CleanableHistoricCaseInstanceReport;
import org.zik.bpm.engine.history.CleanableHistoricDecisionInstanceReport;
import org.zik.bpm.engine.history.CleanableHistoricProcessInstanceReport;
import org.zik.bpm.engine.history.HistoricTaskInstanceReport;
import org.zik.bpm.engine.history.HistoricProcessInstanceReport;
import org.zik.bpm.engine.impl.cmd.GetHistoricJobLogExceptionStacktraceCmd;
import org.zik.bpm.engine.history.HistoricJobLogQuery;
import org.zik.bpm.engine.history.NativeHistoricVariableInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricDecisionInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricCaseActivityInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricCaseInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricActivityInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricTaskInstanceQuery;
import org.zik.bpm.engine.history.NativeHistoricProcessInstanceQuery;
import org.zik.bpm.engine.impl.cmd.DeleteHistoricVariableInstancesByProcessInstanceIdCmd;
import org.zik.bpm.engine.impl.cmd.DeleteHistoricVariableInstanceCmd;
import org.zik.bpm.engine.impl.dmn.cmd.DeleteHistoricDecisionInstancesBatchCmd;
import org.zik.bpm.engine.impl.dmn.cmd.DeleteHistoricDecisionInstanceByInstanceIdCmd;
import org.zik.bpm.engine.impl.dmn.cmd.DeleteHistoricDecisionInstanceByDefinitionIdCmd;
import org.zik.bpm.engine.impl.dmn.cmd.DeleteHistoricDecisionInstancesBulkCmd;
import org.zik.bpm.engine.impl.cmd.DeleteHistoricCaseInstancesBulkCmd;
import org.zik.bpm.engine.impl.cmd.DeleteHistoricCaseInstanceCmd;
import org.zik.bpm.engine.impl.cmd.DeleteUserOperationLogEntryCmd;
import org.zik.bpm.engine.impl.cmd.batch.DeleteHistoricProcessInstancesBatchCmd;
import org.zik.bpm.engine.batch.Batch;
import org.zik.bpm.engine.impl.cmd.FindHistoryCleanupJobsCmd;
import org.zik.bpm.engine.impl.cmd.HistoryCleanupCmd;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.impl.cmd.DeleteHistoricProcessInstancesCmd;
import java.util.List;
import java.util.Arrays;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.impl.cmd.DeleteHistoricTaskInstanceCmd;
import org.zik.bpm.engine.history.HistoricDecisionInstanceQuery;
import org.zik.bpm.engine.history.HistoricCaseActivityInstanceQuery;
import org.zik.bpm.engine.history.HistoricCaseInstanceQuery;
import org.zik.bpm.engine.history.HistoricIncidentQuery;
import org.zik.bpm.engine.history.HistoricVariableInstanceQuery;
import org.zik.bpm.engine.history.UserOperationLogQuery;
import org.zik.bpm.engine.history.HistoricDetailQuery;
import org.zik.bpm.engine.history.HistoricTaskInstanceQuery;
import org.zik.bpm.engine.history.HistoricCaseActivityStatisticsQuery;
import org.zik.bpm.engine.history.HistoricActivityStatisticsQuery;
import org.zik.bpm.engine.history.HistoricActivityInstanceQuery;
import org.zik.bpm.engine.history.HistoricProcessInstanceQuery;
import org.zik.bpm.engine.HistoryService;

public class HistoryServiceImpl extends ServiceImpl implements HistoryService
{
    @Override
    public HistoricProcessInstanceQuery createHistoricProcessInstanceQuery() {
        return new HistoricProcessInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricActivityInstanceQuery createHistoricActivityInstanceQuery() {
        return new HistoricActivityInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricActivityStatisticsQuery createHistoricActivityStatisticsQuery(final String processDefinitionId) {
        return new HistoricActivityStatisticsQueryImpl(processDefinitionId, this.commandExecutor);
    }
    
    @Override
    public HistoricCaseActivityStatisticsQuery createHistoricCaseActivityStatisticsQuery(final String caseDefinitionId) {
        return new HistoricCaseActivityStatisticsQueryImpl(caseDefinitionId, this.commandExecutor);
    }
    
    @Override
    public HistoricTaskInstanceQuery createHistoricTaskInstanceQuery() {
        return new HistoricTaskInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricDetailQuery createHistoricDetailQuery() {
        return new HistoricDetailQueryImpl(this.commandExecutor);
    }
    
    @Override
    public UserOperationLogQuery createUserOperationLogQuery() {
        return new UserOperationLogQueryImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricVariableInstanceQuery createHistoricVariableInstanceQuery() {
        return new HistoricVariableInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricIncidentQuery createHistoricIncidentQuery() {
        return new HistoricIncidentQueryImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricIdentityLinkLogQueryImpl createHistoricIdentityLinkLogQuery() {
        return new HistoricIdentityLinkLogQueryImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricCaseInstanceQuery createHistoricCaseInstanceQuery() {
        return new HistoricCaseInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricCaseActivityInstanceQuery createHistoricCaseActivityInstanceQuery() {
        return new HistoricCaseActivityInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricDecisionInstanceQuery createHistoricDecisionInstanceQuery() {
        return new HistoricDecisionInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public void deleteHistoricTaskInstance(final String taskId) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricTaskInstanceCmd(taskId));
    }
    
    @Override
    public void deleteHistoricProcessInstance(final String processInstanceId) {
        this.deleteHistoricProcessInstances(Arrays.asList(processInstanceId));
    }
    
    @Override
    public void deleteHistoricProcessInstanceIfExists(final String processInstanceId) {
        this.deleteHistoricProcessInstancesIfExists(Arrays.asList(processInstanceId));
    }
    
    @Override
    public void deleteHistoricProcessInstances(final List<String> processInstanceIds) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricProcessInstancesCmd(processInstanceIds, true));
    }
    
    @Override
    public void deleteHistoricProcessInstancesIfExists(final List<String> processInstanceIds) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricProcessInstancesCmd(processInstanceIds, false));
    }
    
    @Override
    public void deleteHistoricProcessInstancesBulk(final List<String> processInstanceIds) {
        this.deleteHistoricProcessInstances(processInstanceIds);
    }
    
    @Override
    public Job cleanUpHistoryAsync() {
        return this.cleanUpHistoryAsync(false);
    }
    
    @Override
    public Job cleanUpHistoryAsync(final boolean immediatelyDue) {
        return this.commandExecutor.execute((Command<Job>)new HistoryCleanupCmd(immediatelyDue));
    }
    
    @Override
    public Job findHistoryCleanupJob() {
        final List<Job> jobs = this.commandExecutor.execute((Command<List<Job>>)new FindHistoryCleanupJobsCmd());
        if (jobs.size() > 0) {
            return jobs.get(0);
        }
        return null;
    }
    
    @Override
    public List<Job> findHistoryCleanupJobs() {
        return this.commandExecutor.execute((Command<List<Job>>)new FindHistoryCleanupJobsCmd());
    }
    
    @Override
    public Batch deleteHistoricProcessInstancesAsync(final List<String> processInstanceIds, final String deleteReason) {
        return this.deleteHistoricProcessInstancesAsync(processInstanceIds, null, deleteReason);
    }
    
    @Override
    public Batch deleteHistoricProcessInstancesAsync(final HistoricProcessInstanceQuery query, final String deleteReason) {
        return this.deleteHistoricProcessInstancesAsync(null, query, deleteReason);
    }
    
    @Override
    public Batch deleteHistoricProcessInstancesAsync(final List<String> processInstanceIds, final HistoricProcessInstanceQuery query, final String deleteReason) {
        return this.commandExecutor.execute((Command<Batch>)new DeleteHistoricProcessInstancesBatchCmd(processInstanceIds, query, deleteReason));
    }
    
    @Override
    public void deleteUserOperationLogEntry(final String entryId) {
        this.commandExecutor.execute((Command<Object>)new DeleteUserOperationLogEntryCmd(entryId));
    }
    
    @Override
    public void deleteHistoricCaseInstance(final String caseInstanceId) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricCaseInstanceCmd(caseInstanceId));
    }
    
    @Override
    public void deleteHistoricCaseInstancesBulk(final List<String> caseInstanceIds) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricCaseInstancesBulkCmd(caseInstanceIds));
    }
    
    @Override
    public void deleteHistoricDecisionInstance(final String decisionDefinitionId) {
        this.deleteHistoricDecisionInstanceByDefinitionId(decisionDefinitionId);
    }
    
    @Override
    public void deleteHistoricDecisionInstancesBulk(final List<String> decisionInstanceIds) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricDecisionInstancesBulkCmd(decisionInstanceIds));
    }
    
    @Override
    public void deleteHistoricDecisionInstanceByDefinitionId(final String decisionDefinitionId) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricDecisionInstanceByDefinitionIdCmd(decisionDefinitionId));
    }
    
    @Override
    public void deleteHistoricDecisionInstanceByInstanceId(final String historicDecisionInstanceId) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricDecisionInstanceByInstanceIdCmd(historicDecisionInstanceId));
    }
    
    @Override
    public Batch deleteHistoricDecisionInstancesAsync(final List<String> decisionInstanceIds, final String deleteReason) {
        return this.deleteHistoricDecisionInstancesAsync(decisionInstanceIds, null, deleteReason);
    }
    
    @Override
    public Batch deleteHistoricDecisionInstancesAsync(final HistoricDecisionInstanceQuery query, final String deleteReason) {
        return this.deleteHistoricDecisionInstancesAsync(null, query, deleteReason);
    }
    
    @Override
    public Batch deleteHistoricDecisionInstancesAsync(final List<String> decisionInstanceIds, final HistoricDecisionInstanceQuery query, final String deleteReason) {
        return this.commandExecutor.execute((Command<Batch>)new DeleteHistoricDecisionInstancesBatchCmd(decisionInstanceIds, query, deleteReason));
    }
    
    @Override
    public void deleteHistoricVariableInstance(final String variableInstanceId) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricVariableInstanceCmd(variableInstanceId));
    }
    
    @Override
    public void deleteHistoricVariableInstancesByProcessInstanceId(final String processInstanceId) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricVariableInstancesByProcessInstanceIdCmd(processInstanceId));
    }
    
    @Override
    public NativeHistoricProcessInstanceQuery createNativeHistoricProcessInstanceQuery() {
        return new NativeHistoricProcessInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public NativeHistoricTaskInstanceQuery createNativeHistoricTaskInstanceQuery() {
        return new NativeHistoricTaskInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public NativeHistoricActivityInstanceQuery createNativeHistoricActivityInstanceQuery() {
        return new NativeHistoricActivityInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public NativeHistoricCaseInstanceQuery createNativeHistoricCaseInstanceQuery() {
        return new NativeHistoricCaseInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public NativeHistoricCaseActivityInstanceQuery createNativeHistoricCaseActivityInstanceQuery() {
        return new NativeHistoricCaseActivityInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public NativeHistoricDecisionInstanceQuery createNativeHistoricDecisionInstanceQuery() {
        return new NativeHistoryDecisionInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public NativeHistoricVariableInstanceQuery createNativeHistoricVariableInstanceQuery() {
        return new NativeHistoricVariableInstanceQueryImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricJobLogQuery createHistoricJobLogQuery() {
        return new HistoricJobLogQueryImpl(this.commandExecutor);
    }
    
    @Override
    public String getHistoricJobLogExceptionStacktrace(final String historicJobLogId) {
        return this.commandExecutor.execute((Command<String>)new GetHistoricJobLogExceptionStacktraceCmd(historicJobLogId));
    }
    
    @Override
    public HistoricProcessInstanceReport createHistoricProcessInstanceReport() {
        return new HistoricProcessInstanceReportImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricTaskInstanceReport createHistoricTaskInstanceReport() {
        return new HistoricTaskInstanceReportImpl(this.commandExecutor);
    }
    
    @Override
    public CleanableHistoricProcessInstanceReport createCleanableHistoricProcessInstanceReport() {
        return new CleanableHistoricProcessInstanceReportImpl(this.commandExecutor);
    }
    
    @Override
    public CleanableHistoricDecisionInstanceReport createCleanableHistoricDecisionInstanceReport() {
        return new CleanableHistoricDecisionInstanceReportImpl(this.commandExecutor);
    }
    
    @Override
    public CleanableHistoricCaseInstanceReport createCleanableHistoricCaseInstanceReport() {
        return new CleanableHistoricCaseInstanceReportImpl(this.commandExecutor);
    }
    
    @Override
    public CleanableHistoricBatchReport createCleanableHistoricBatchReport() {
        return new CleanableHistoricBatchReportImpl(this.commandExecutor);
    }
    
    @Override
    public HistoricBatchQuery createHistoricBatchQuery() {
        return new HistoricBatchQueryImpl(this.commandExecutor);
    }
    
    @Override
    public void deleteHistoricBatch(final String batchId) {
        this.commandExecutor.execute((Command<Object>)new DeleteHistoricBatchCmd(batchId));
    }
    
    @Override
    public HistoricDecisionInstanceStatisticsQuery createHistoricDecisionInstanceStatisticsQuery(final String decisionRequirementsDefinitionId) {
        return new HistoricDecisionInstanceStatisticsQueryImpl(decisionRequirementsDefinitionId, this.commandExecutor);
    }
    
    @Override
    public HistoricExternalTaskLogQuery createHistoricExternalTaskLogQuery() {
        return new HistoricExternalTaskLogQueryImpl(this.commandExecutor);
    }
    
    @Override
    public String getHistoricExternalTaskLogErrorDetails(final String historicExternalTaskLogId) {
        return this.commandExecutor.execute((Command<String>)new GetHistoricExternalTaskLogErrorDetailsCmd(historicExternalTaskLogId));
    }
    
    @Override
    public SetRemovalTimeSelectModeForHistoricProcessInstancesBuilder setRemovalTimeToHistoricProcessInstances() {
        return new SetRemovalTimeToHistoricProcessInstancesBuilderImpl(this.commandExecutor);
    }
    
    @Override
    public SetRemovalTimeSelectModeForHistoricDecisionInstancesBuilder setRemovalTimeToHistoricDecisionInstances() {
        return new SetRemovalTimeToHistoricDecisionInstancesBuilderImpl(this.commandExecutor);
    }
    
    @Override
    public SetRemovalTimeSelectModeForHistoricBatchesBuilder setRemovalTimeToHistoricBatches() {
        return new SetRemovalTimeToHistoricBatchesBuilderImpl(this.commandExecutor);
    }
    
    @Override
    public void setAnnotationForOperationLogById(final String operationId, final String annotation) {
        this.commandExecutor.execute((Command<Object>)new SetAnnotationForOperationLog(operationId, annotation));
    }
    
    @Override
    public void clearAnnotationForOperationLogById(final String operationId) {
        this.commandExecutor.execute((Command<Object>)new SetAnnotationForOperationLog(operationId, null));
    }
}
