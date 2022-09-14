// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.history.ExternalTaskState;
import org.zik.bpm.engine.history.HistoricExternalTaskLog;
import org.zik.bpm.engine.history.HistoricExternalTaskLogQuery;

public class HistoricExternalTaskLogQueryImpl extends AbstractQuery<HistoricExternalTaskLogQuery, HistoricExternalTaskLog> implements HistoricExternalTaskLogQuery
{
    private static final long serialVersionUID = 1L;
    protected String id;
    protected String externalTaskId;
    protected String topicName;
    protected String workerId;
    protected String errorMessage;
    protected String[] activityIds;
    protected String[] activityInstanceIds;
    protected String[] executionIds;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected Long priorityHigherThanOrEqual;
    protected Long priorityLowerThanOrEqual;
    protected String[] tenantIds;
    protected boolean isTenantIdSet;
    protected ExternalTaskState state;
    
    public HistoricExternalTaskLogQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public HistoricExternalTaskLogQuery logId(final String historicExternalTaskLogId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "historicExternalTaskLogId", (Object)historicExternalTaskLogId);
        this.id = historicExternalTaskLogId;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery externalTaskId(final String externalTaskId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "externalTaskId", (Object)externalTaskId);
        this.externalTaskId = externalTaskId;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery topicName(final String topicName) {
        EnsureUtil.ensureNotNull(NotValidException.class, "topicName", (Object)topicName);
        this.topicName = topicName;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery workerId(final String workerId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "workerId", (Object)workerId);
        this.workerId = workerId;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery errorMessage(final String errorMessage) {
        EnsureUtil.ensureNotNull(NotValidException.class, "errorMessage", (Object)errorMessage);
        this.errorMessage = errorMessage;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery activityIdIn(final String... activityIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityIds", (Object[])activityIds);
        final List<String> activityIdList = CollectionUtil.asArrayList(activityIds);
        EnsureUtil.ensureNotContainsNull("activityIds", activityIdList);
        EnsureUtil.ensureNotContainsEmptyString("activityIds", activityIdList);
        this.activityIds = activityIds;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery activityInstanceIdIn(final String... activityInstanceIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityIds", (Object[])activityInstanceIds);
        final List<String> activityInstanceIdList = CollectionUtil.asArrayList(activityInstanceIds);
        EnsureUtil.ensureNotContainsNull("activityInstanceIds", activityInstanceIdList);
        EnsureUtil.ensureNotContainsEmptyString("activityInstanceIds", activityInstanceIdList);
        this.activityInstanceIds = activityInstanceIds;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery executionIdIn(final String... executionIds) {
        EnsureUtil.ensureNotNull(NotValidException.class, "activityIds", (Object[])executionIds);
        final List<String> executionIdList = CollectionUtil.asArrayList(executionIds);
        EnsureUtil.ensureNotContainsNull("executionIds", executionIdList);
        EnsureUtil.ensureNotContainsEmptyString("executionIds", executionIdList);
        this.executionIds = executionIds;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "processInstanceId", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull(NotValidException.class, "processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery processDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull(NotValidException.class, "processDefinitionKey", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery withoutTenantId() {
        this.tenantIds = null;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery priorityHigherThanOrEquals(final long priority) {
        this.priorityHigherThanOrEqual = priority;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery priorityLowerThanOrEquals(final long priority) {
        this.priorityLowerThanOrEqual = priority;
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery creationLog() {
        this.setState(ExternalTaskState.CREATED);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery failureLog() {
        this.setState(ExternalTaskState.FAILED);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery successLog() {
        this.setState(ExternalTaskState.SUCCESSFUL);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery deletionLog() {
        this.setState(ExternalTaskState.DELETED);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByTimestamp() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.TIMESTAMP);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByExternalTaskId() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.EXTERNAL_TASK_ID);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByRetries() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.RETRIES);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByPriority() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.PRIORITY);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByTopicName() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.TOPIC_NAME);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByWorkerId() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.WORKER_ID);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByActivityId() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.ACTIVITY_ID);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByActivityInstanceId() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.ACTIVITY_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByExecutionId() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.EXECUTION_ID);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByProcessInstanceId() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.PROCESS_INSTANCE_ID);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByProcessDefinitionId() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.PROCESS_DEFINITION_ID);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByProcessDefinitionKey() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.PROCESS_DEFINITION_KEY);
        return this;
    }
    
    @Override
    public HistoricExternalTaskLogQuery orderByTenantId() {
        this.orderBy(HistoricExternalTaskLogQueryProperty.TENANT_ID);
        return this;
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getHistoricExternalTaskLogManager().findHistoricExternalTaskLogsCountByQueryCriteria(this);
    }
    
    @Override
    public List<HistoricExternalTaskLog> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getHistoricExternalTaskLogManager().findHistoricExternalTaskLogsByQueryCriteria(this, page);
    }
    
    protected void setState(final ExternalTaskState state) {
        this.state = state;
    }
    
    public boolean isTenantIdSet() {
        return this.isTenantIdSet;
    }
}
