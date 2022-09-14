// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.CompareUtil;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import java.util.Date;
import org.zik.bpm.engine.history.UserOperationLogEntry;
import org.zik.bpm.engine.history.UserOperationLogQuery;

public class UserOperationLogQueryImpl extends AbstractQuery<UserOperationLogQuery, UserOperationLogEntry> implements UserOperationLogQuery
{
    private static final long serialVersionUID = 1L;
    protected String deploymentId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected String processInstanceId;
    protected String executionId;
    protected String caseDefinitionId;
    protected String caseInstanceId;
    protected String caseExecutionId;
    protected String taskId;
    protected String jobId;
    protected String jobDefinitionId;
    protected String batchId;
    protected String userId;
    protected String operationId;
    protected String externalTaskId;
    protected String operationType;
    protected String property;
    protected String entityType;
    protected String category;
    protected Date timestampAfter;
    protected Date timestampBefore;
    protected String[] entityTypes;
    protected String[] categories;
    
    public UserOperationLogQueryImpl() {
    }
    
    public UserOperationLogQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public UserOperationLogQuery deploymentId(final String deploymentId) {
        EnsureUtil.ensureNotNull("deploymentId", (Object)deploymentId);
        this.deploymentId = deploymentId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery processDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("processDefinitionKey", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public UserOperationLogQuery processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull("processInstanceId", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery executionId(final String executionId) {
        EnsureUtil.ensureNotNull("executionId", (Object)executionId);
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery caseDefinitionId(final String caseDefinitionId) {
        EnsureUtil.ensureNotNull("caseDefinitionId", (Object)caseDefinitionId);
        this.caseDefinitionId = caseDefinitionId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery caseInstanceId(final String caseInstanceId) {
        EnsureUtil.ensureNotNull("caseInstanceId", (Object)caseInstanceId);
        this.caseInstanceId = caseInstanceId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery caseExecutionId(final String caseExecutionId) {
        EnsureUtil.ensureNotNull("caseExecutionId", (Object)caseExecutionId);
        this.caseExecutionId = caseExecutionId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery taskId(final String taskId) {
        EnsureUtil.ensureNotNull("taskId", (Object)taskId);
        this.taskId = taskId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery jobId(final String jobId) {
        EnsureUtil.ensureNotNull("jobId", (Object)jobId);
        this.jobId = jobId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery jobDefinitionId(final String jobDefinitionId) {
        EnsureUtil.ensureNotNull("jobDefinitionId", (Object)jobDefinitionId);
        this.jobDefinitionId = jobDefinitionId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery batchId(final String batchId) {
        EnsureUtil.ensureNotNull("batchId", (Object)batchId);
        this.batchId = batchId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery userId(final String userId) {
        EnsureUtil.ensureNotNull("userId", (Object)userId);
        this.userId = userId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery operationId(final String operationId) {
        EnsureUtil.ensureNotNull("operationId", (Object)operationId);
        this.operationId = operationId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery externalTaskId(final String externalTaskId) {
        EnsureUtil.ensureNotNull("externalTaskId", (Object)externalTaskId);
        this.externalTaskId = externalTaskId;
        return this;
    }
    
    @Override
    public UserOperationLogQuery operationType(final String operationType) {
        EnsureUtil.ensureNotNull("operationType", (Object)operationType);
        this.operationType = operationType;
        return this;
    }
    
    @Override
    public UserOperationLogQuery property(final String property) {
        EnsureUtil.ensureNotNull("property", (Object)property);
        this.property = property;
        return this;
    }
    
    @Override
    public UserOperationLogQuery entityType(final String entityType) {
        EnsureUtil.ensureNotNull("entityType", (Object)entityType);
        this.entityType = entityType;
        return this;
    }
    
    @Override
    public UserOperationLogQuery entityTypeIn(final String... entityTypes) {
        EnsureUtil.ensureNotNull("entity types", (Object[])entityTypes);
        this.entityTypes = entityTypes;
        return this;
    }
    
    @Override
    public UserOperationLogQuery category(final String category) {
        EnsureUtil.ensureNotNull("category", (Object)category);
        this.category = category;
        return this;
    }
    
    @Override
    public UserOperationLogQuery categoryIn(final String... categories) {
        EnsureUtil.ensureNotNull("categories", (Object[])categories);
        this.categories = categories;
        return this;
    }
    
    @Override
    public UserOperationLogQuery afterTimestamp(final Date after) {
        this.timestampAfter = after;
        return this;
    }
    
    @Override
    public UserOperationLogQuery beforeTimestamp(final Date before) {
        this.timestampBefore = before;
        return this;
    }
    
    @Override
    public UserOperationLogQuery orderByTimestamp() {
        return ((AbstractQuery<UserOperationLogQuery, U>)this).orderBy(OperationLogQueryProperty.TIMESTAMP);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getOperationLogManager().findOperationLogEntryCountByQueryCriteria(this);
    }
    
    @Override
    public List<UserOperationLogEntry> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getOperationLogManager().findOperationLogEntriesByQueryCriteria(this, page);
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.areNotInAscendingOrder(this.timestampAfter, this.timestampBefore);
    }
}
