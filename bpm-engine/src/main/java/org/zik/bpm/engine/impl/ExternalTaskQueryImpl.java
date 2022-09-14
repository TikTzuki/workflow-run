// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import java.util.List;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.CompareUtil;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.Date;
import java.util.Set;
import org.zik.bpm.engine.externaltask.ExternalTask;
import org.zik.bpm.engine.externaltask.ExternalTaskQuery;

public class ExternalTaskQueryImpl extends AbstractQuery<ExternalTaskQuery, ExternalTask> implements ExternalTaskQuery
{
    private static final long serialVersionUID = 1L;
    protected String externalTaskId;
    protected Set<String> externalTaskIds;
    protected String workerId;
    protected Date lockExpirationBefore;
    protected Date lockExpirationAfter;
    protected String topicName;
    protected Boolean locked;
    protected Boolean notLocked;
    protected String executionId;
    protected String processInstanceId;
    protected String[] processInstanceIdIn;
    protected String processDefinitionId;
    protected String activityId;
    protected String[] activityIdIn;
    protected SuspensionState suspensionState;
    protected Long priorityHigherThanOrEquals;
    protected Long priorityLowerThanOrEquals;
    protected Boolean retriesLeft;
    protected String[] tenantIds;
    
    public ExternalTaskQueryImpl() {
    }
    
    public ExternalTaskQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
    }
    
    @Override
    public ExternalTaskQuery externalTaskId(final String externalTaskId) {
        EnsureUtil.ensureNotNull("externalTaskId", (Object)externalTaskId);
        this.externalTaskId = externalTaskId;
        return this;
    }
    
    @Override
    public ExternalTaskQuery externalTaskIdIn(final Set<String> externalTaskIds) {
        EnsureUtil.ensureNotEmpty("Set of external task ids", externalTaskIds);
        this.externalTaskIds = externalTaskIds;
        return this;
    }
    
    @Override
    public ExternalTaskQuery workerId(final String workerId) {
        EnsureUtil.ensureNotNull("workerId", (Object)workerId);
        this.workerId = workerId;
        return this;
    }
    
    @Override
    public ExternalTaskQuery lockExpirationBefore(final Date lockExpirationDate) {
        EnsureUtil.ensureNotNull("lockExpirationBefore", lockExpirationDate);
        this.lockExpirationBefore = lockExpirationDate;
        return this;
    }
    
    @Override
    public ExternalTaskQuery lockExpirationAfter(final Date lockExpirationDate) {
        EnsureUtil.ensureNotNull("lockExpirationAfter", lockExpirationDate);
        this.lockExpirationAfter = lockExpirationDate;
        return this;
    }
    
    @Override
    public ExternalTaskQuery topicName(final String topicName) {
        EnsureUtil.ensureNotNull("topicName", (Object)topicName);
        this.topicName = topicName;
        return this;
    }
    
    @Override
    public ExternalTaskQuery locked() {
        this.locked = Boolean.TRUE;
        return this;
    }
    
    @Override
    public ExternalTaskQuery notLocked() {
        this.notLocked = Boolean.TRUE;
        return this;
    }
    
    @Override
    public ExternalTaskQuery executionId(final String executionId) {
        EnsureUtil.ensureNotNull("executionId", (Object)executionId);
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public ExternalTaskQuery processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull("processInstanceId", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public ExternalTaskQuery processInstanceIdIn(final String... processInstanceIdIn) {
        EnsureUtil.ensureNotNull("processInstanceIdIn", (Object[])processInstanceIdIn);
        this.processInstanceIdIn = processInstanceIdIn;
        return this;
    }
    
    @Override
    public ExternalTaskQuery processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("processDefinitionId", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public ExternalTaskQuery activityId(final String activityId) {
        EnsureUtil.ensureNotNull("activityId", (Object)activityId);
        this.activityId = activityId;
        return this;
    }
    
    @Override
    public ExternalTaskQuery activityIdIn(final String... activityIdIn) {
        EnsureUtil.ensureNotNull("activityIdIn", (Object[])activityIdIn);
        this.activityIdIn = activityIdIn;
        return this;
    }
    
    @Override
    public ExternalTaskQuery priorityHigherThanOrEquals(final long priority) {
        this.priorityHigherThanOrEquals = priority;
        return this;
    }
    
    @Override
    public ExternalTaskQuery priorityLowerThanOrEquals(final long priority) {
        this.priorityLowerThanOrEquals = priority;
        return this;
    }
    
    @Override
    public ExternalTaskQuery suspended() {
        this.suspensionState = SuspensionState.SUSPENDED;
        return this;
    }
    
    @Override
    public ExternalTaskQuery active() {
        this.suspensionState = SuspensionState.ACTIVE;
        return this;
    }
    
    @Override
    public ExternalTaskQuery withRetriesLeft() {
        this.retriesLeft = Boolean.TRUE;
        return this;
    }
    
    @Override
    public ExternalTaskQuery noRetriesLeft() {
        this.retriesLeft = Boolean.FALSE;
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || CompareUtil.areNotInAscendingOrder(this.priorityHigherThanOrEquals, this.priorityLowerThanOrEquals);
    }
    
    @Override
    public ExternalTaskQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        return this;
    }
    
    @Override
    public ExternalTaskQuery orderById() {
        return ((AbstractQuery<ExternalTaskQuery, U>)this).orderBy(ExternalTaskQueryProperty.ID);
    }
    
    @Override
    public ExternalTaskQuery orderByLockExpirationTime() {
        return ((AbstractQuery<ExternalTaskQuery, U>)this).orderBy(ExternalTaskQueryProperty.LOCK_EXPIRATION_TIME);
    }
    
    @Override
    public ExternalTaskQuery orderByProcessInstanceId() {
        return ((AbstractQuery<ExternalTaskQuery, U>)this).orderBy(ExternalTaskQueryProperty.PROCESS_INSTANCE_ID);
    }
    
    @Override
    public ExternalTaskQuery orderByProcessDefinitionId() {
        return ((AbstractQuery<ExternalTaskQuery, U>)this).orderBy(ExternalTaskQueryProperty.PROCESS_DEFINITION_ID);
    }
    
    @Override
    public ExternalTaskQuery orderByProcessDefinitionKey() {
        return ((AbstractQuery<ExternalTaskQuery, U>)this).orderBy(ExternalTaskQueryProperty.PROCESS_DEFINITION_KEY);
    }
    
    @Override
    public ExternalTaskQuery orderByTenantId() {
        return ((AbstractQuery<ExternalTaskQuery, U>)this).orderBy(ExternalTaskQueryProperty.TENANT_ID);
    }
    
    @Override
    public ExternalTaskQuery orderByPriority() {
        return ((AbstractQuery<ExternalTaskQuery, U>)this).orderBy(ExternalTaskQueryProperty.PRIORITY);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getExternalTaskManager().findExternalTaskCountByQueryCriteria(this);
    }
    
    @Override
    public List<ExternalTask> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getExternalTaskManager().findExternalTasksByQueryCriteria(this);
    }
    
    @Override
    public List<String> executeIdsList(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getExternalTaskManager().findExternalTaskIdsByQueryCriteria(this);
    }
    
    @Override
    public List<ImmutablePair<String, String>> executeDeploymentIdMappingsList(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getExternalTaskManager().findDeploymentIdMappingsByQueryCriteria(this);
    }
    
    public String getExternalTaskId() {
        return this.externalTaskId;
    }
    
    public String getWorkerId() {
        return this.workerId;
    }
    
    public Date getLockExpirationBefore() {
        return this.lockExpirationBefore;
    }
    
    public Date getLockExpirationAfter() {
        return this.lockExpirationAfter;
    }
    
    public String getTopicName() {
        return this.topicName;
    }
    
    public Boolean getLocked() {
        return this.locked;
    }
    
    public Boolean getNotLocked() {
        return this.notLocked;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }
    
    public String getActivityId() {
        return this.activityId;
    }
    
    public SuspensionState getSuspensionState() {
        return this.suspensionState;
    }
    
    public Boolean getRetriesLeft() {
        return this.retriesLeft;
    }
    
    public Date getNow() {
        return ClockUtil.getCurrentTime();
    }
}
