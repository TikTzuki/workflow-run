// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.CompareUtil;
import java.util.List;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.history.JobState;
import org.zik.bpm.engine.history.HistoricJobLog;
import org.zik.bpm.engine.history.HistoricJobLogQuery;

public class HistoricJobLogQueryImpl extends AbstractQuery<HistoricJobLogQuery, HistoricJobLog> implements HistoricJobLogQuery
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String jobId;
    protected String jobExceptionMessage;
    protected String jobDefinitionId;
    protected String jobDefinitionType;
    protected String jobDefinitionConfiguration;
    protected String[] activityIds;
    protected String[] failedActivityIds;
    protected String[] executionIds;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String deploymentId;
    protected JobState state;
    protected Long jobPriorityHigherThanOrEqual;
    protected Long jobPriorityLowerThanOrEqual;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    protected String hostname;
    
    public HistoricJobLogQueryImpl() {
    }
    
    public HistoricJobLogQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public HistoricJobLogQuery logId(final String historicJobLogId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "historicJobLogId", (Object)historicJobLogId);
        this.id = historicJobLogId;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery jobId(final String jobId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "jobId", (Object)jobId);
        this.jobId = jobId;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery jobExceptionMessage(final String jobExceptionMessage) {
        EnsureUtil.ensureNotNull(NotValidException.class, "jobExceptionMessage", (Object)jobExceptionMessage);
        this.jobExceptionMessage = jobExceptionMessage;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery jobDefinitionId(final String jobDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "jobDefinitionId", (Object)jobDefinitionId);
        this.jobDefinitionId = jobDefinitionId;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery jobDefinitionType(final String jobDefinitionType) {
        EnsureUtil.ensureNotNull(NotValidException.class, "jobDefinitionType", (Object)jobDefinitionType);
        this.jobDefinitionType = jobDefinitionType;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery jobDefinitionConfiguration(final String jobDefinitionConfiguration) {
        EnsureUtil.ensureNotNull(NotValidException.class, "jobDefinitionConfiguration", (Object)jobDefinitionConfiguration);
        this.jobDefinitionConfiguration = jobDefinitionConfiguration;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery activityIdIn(final String... activityIds) {
        final List<String> activityIdList = CollectionUtil.asArrayList(activityIds);
        EnsureUtil.ensureNotContainsNull("activityIds", activityIdList);
        EnsureUtil.ensureNotContainsEmptyString("activityIds", activityIdList);
        this.activityIds = activityIds;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery failedActivityIdIn(final String... activityIds) {
        final List<String> activityIdList = CollectionUtil.asArrayList(activityIds);
        EnsureUtil.ensureNotContainsNull("activityIds", activityIdList);
        EnsureUtil.ensureNotContainsEmptyString("activityIds", activityIdList);
        this.failedActivityIds = activityIds;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery executionIdIn(final String... executionIds) {
        final List<String> executionIdList = CollectionUtil.asArrayList(executionIds);
        EnsureUtil.ensureNotContainsNull("executionIds", executionIdList);
        EnsureUtil.ensureNotContainsEmptyString("executionIds", executionIdList);
        this.executionIds = executionIds;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "processInstanceId", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery processDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "processDefinitionKey", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery deploymentId(final String deploymentId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "deploymentId", (Object)deploymentId);
        this.deploymentId = deploymentId;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery jobPriorityHigherThanOrEquals(final long priority) {
        this.jobPriorityHigherThanOrEqual = priority;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery jobPriorityLowerThanOrEquals(final long priority) {
        this.jobPriorityLowerThanOrEqual = priority;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery hostname(final String hostname) {
        EnsureUtil.ensureNotEmpty("hostName", hostname);
        this.hostname = hostname;
        return this;
    }
    
    @Override
    public HistoricJobLogQuery creationLog() {
        this.setState(JobState.CREATED);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery failureLog() {
        this.setState(JobState.FAILED);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery successLog() {
        this.setState(JobState.SUCCESSFUL);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery deletionLog() {
        this.setState(JobState.DELETED);
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.areNotInAscendingOrder(this.jobPriorityHigherThanOrEqual, this.jobPriorityLowerThanOrEqual);
    }
    
    @Override
    public HistoricJobLogQuery orderByTimestamp() {
        this.orderBy(HistoricJobLogQueryProperty.TIMESTAMP);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByJobId() {
        this.orderBy(HistoricJobLogQueryProperty.JOB_ID);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByJobDueDate() {
        this.orderBy(HistoricJobLogQueryProperty.DUEDATE);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByJobRetries() {
        this.orderBy(HistoricJobLogQueryProperty.RETRIES);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByJobPriority() {
        this.orderBy(HistoricJobLogQueryProperty.PRIORITY);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByJobDefinitionId() {
        this.orderBy(HistoricJobLogQueryProperty.JOB_DEFINITION_ID);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByActivityId() {
        this.orderBy(HistoricJobLogQueryProperty.ACTIVITY_ID);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByExecutionId() {
        this.orderBy(HistoricJobLogQueryProperty.EXECUTION_ID);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByProcessInstanceId() {
        this.orderBy(HistoricJobLogQueryProperty.PROCESS_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByProcessDefinitionId() {
        this.orderBy(HistoricJobLogQueryProperty.PROCESS_DEFINITION_ID);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByProcessDefinitionKey() {
        this.orderBy(HistoricJobLogQueryProperty.PROCESS_DEFINITION_KEY);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByDeploymentId() {
        this.orderBy(HistoricJobLogQueryProperty.DEPLOYMENT_ID);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderPartiallyByOccurrence() {
        this.orderBy(HistoricJobLogQueryProperty.SEQUENCE_COUNTER);
        return this;
    }
    
    @Override
    public HistoricJobLogQuery orderByTenantId() {
        return ((AbstractQuery<HistoricJobLogQuery, U>)this).orderBy(HistoricJobLogQueryProperty.TENANT_ID);
    }
    
    @Override
    public HistoricJobLogQuery orderByHostname() {
        return ((AbstractQuery<HistoricJobLogQuery, U>)this).orderBy(HistoricJobLogQueryProperty.HOSTNAME);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricJobLogManager().findHistoricJobLogsCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricJobLog> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricJobLogManager().findHistoricJobLogsByQueryCriteria(this, page);
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
    
    public String getJobId() {
        return this.jobId;
    }
    
    public String getJobExceptionMessage() {
        return this.jobExceptionMessage;
    }
    
    public String getJobDefinitionId() {
        return this.jobDefinitionId;
    }
    
    public String getJobDefinitionType() {
        return this.jobDefinitionType;
    }
    
    public String getJobDefinitionConfiguration() {
        return this.jobDefinitionConfiguration;
    }
    
    public String[] getActivityIds() {
        return this.activityIds;
    }
    
    public String[] getFailedActivityIds() {
        return this.failedActivityIds;
    }
    
    public String[] getExecutionIds() {
        return this.executionIds;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getProcessDefinitionKey() {
        return this.processDefinitionKey;
    }
    
    public String getDeploymentId() {
        return this.deploymentId;
    }
    
    public JobState getState() {
        return this.state;
    }
    
    public String[] getTenantIds() {
        return this.tenantIds;
    }
    
    public String getHostname() {
        return this.hostname;
    }
    
    protected void setState(final JobState state) {
        this.state = state;
    }
}
