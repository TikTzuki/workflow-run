// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl;

import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import java.util.List;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.util.CompareUtil;
import org.zik.bpm.engine.ProcessEngineException;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.persistence.entity.SuspensionState;
import java.util.Date;
import java.util.Set;
import java.io.Serializable;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.runtime.JobQuery;

public class JobQueryImpl extends AbstractQuery<JobQuery, Job> implements JobQuery, Serializable
{
    private static final long serialVersionUID = 1L;
    protected String activityId;
    protected String id;
    protected Set<String> ids;
    protected String jobDefinitionId;
    protected String processInstanceId;
    protected Set<String> processInstanceIds;
    protected String executionId;
    protected String processDefinitionId;
    protected String processDefinitionKey;
    protected boolean retriesLeft;
    protected boolean executable;
    protected boolean onlyTimers;
    protected boolean onlyMessages;
    protected Date duedateHigherThan;
    protected Date duedateLowerThan;
    protected Date duedateHigherThanOrEqual;
    protected Date duedateLowerThanOrEqual;
    protected Date createdBefore;
    protected Date createdAfter;
    protected Long priorityHigherThanOrEqual;
    protected Long priorityLowerThanOrEqual;
    protected boolean withException;
    protected String exceptionMessage;
    protected String failedActivityId;
    protected boolean noRetriesLeft;
    protected SuspensionState suspensionState;
    protected boolean isTenantIdSet;
    protected String[] tenantIds;
    protected boolean includeJobsWithoutTenantId;
    
    public JobQueryImpl() {
        this.isTenantIdSet = false;
        this.includeJobsWithoutTenantId = false;
    }
    
    public JobQueryImpl(final CommandExecutor commandExecutor) {
        super(commandExecutor);
        this.isTenantIdSet = false;
        this.includeJobsWithoutTenantId = false;
    }
    
    @Override
    public JobQuery jobId(final String jobId) {
        EnsureUtil.ensureNotNull("Provided job id", (Object)jobId);
        this.id = jobId;
        return this;
    }
    
    @Override
    public JobQuery jobIds(final Set<String> ids) {
        EnsureUtil.ensureNotEmpty("Set of job ids", ids);
        this.ids = ids;
        return this;
    }
    
    @Override
    public JobQuery jobDefinitionId(final String jobDefinitionId) {
        EnsureUtil.ensureNotNull("Provided job definition id", (Object)jobDefinitionId);
        this.jobDefinitionId = jobDefinitionId;
        return this;
    }
    
    @Override
    public JobQueryImpl processInstanceId(final String processInstanceId) {
        EnsureUtil.ensureNotNull("Provided process instance id", (Object)processInstanceId);
        this.processInstanceId = processInstanceId;
        return this;
    }
    
    @Override
    public JobQuery processInstanceIds(final Set<String> processInstanceIds) {
        EnsureUtil.ensureNotEmpty("Set of process instance ids", processInstanceIds);
        this.processInstanceIds = processInstanceIds;
        return this;
    }
    
    @Override
    public JobQueryImpl executionId(final String executionId) {
        EnsureUtil.ensureNotNull("Provided execution id", (Object)executionId);
        this.executionId = executionId;
        return this;
    }
    
    @Override
    public JobQuery processDefinitionId(final String processDefinitionId) {
        EnsureUtil.ensureNotNull("Provided process definition id", (Object)processDefinitionId);
        this.processDefinitionId = processDefinitionId;
        return this;
    }
    
    @Override
    public JobQuery processDefinitionKey(final String processDefinitionKey) {
        EnsureUtil.ensureNotNull("Provided process instance key", (Object)processDefinitionKey);
        this.processDefinitionKey = processDefinitionKey;
        return this;
    }
    
    @Override
    public JobQuery activityId(final String activityId) {
        EnsureUtil.ensureNotNull("Provided activity id", (Object)activityId);
        this.activityId = activityId;
        return this;
    }
    
    @Override
    public JobQuery withRetriesLeft() {
        this.retriesLeft = true;
        return this;
    }
    
    @Override
    public JobQuery executable() {
        this.executable = true;
        return this;
    }
    
    @Override
    public JobQuery timers() {
        if (this.onlyMessages) {
            throw new ProcessEngineException("Cannot combine onlyTimers() with onlyMessages() in the same query");
        }
        this.onlyTimers = true;
        return this;
    }
    
    @Override
    public JobQuery messages() {
        if (this.onlyTimers) {
            throw new ProcessEngineException("Cannot combine onlyTimers() with onlyMessages() in the same query");
        }
        this.onlyMessages = true;
        return this;
    }
    
    @Override
    public JobQuery duedateHigherThan(final Date date) {
        EnsureUtil.ensureNotNull("Provided date", date);
        this.duedateHigherThan = date;
        return this;
    }
    
    @Override
    public JobQuery duedateLowerThan(final Date date) {
        EnsureUtil.ensureNotNull("Provided date", date);
        this.duedateLowerThan = date;
        return this;
    }
    
    @Override
    public JobQuery duedateHigherThen(final Date date) {
        return this.duedateHigherThan(date);
    }
    
    @Override
    public JobQuery duedateHigherThenOrEquals(final Date date) {
        EnsureUtil.ensureNotNull("Provided date", date);
        this.duedateHigherThanOrEqual = date;
        return this;
    }
    
    @Override
    public JobQuery duedateLowerThen(final Date date) {
        return this.duedateLowerThan(date);
    }
    
    @Override
    public JobQuery duedateLowerThenOrEquals(final Date date) {
        EnsureUtil.ensureNotNull("Provided date", date);
        this.duedateLowerThanOrEqual = date;
        return this;
    }
    
    @Override
    public JobQuery createdBefore(final Date date) {
        EnsureUtil.ensureNotNull("Provided date", date);
        this.createdBefore = date;
        return this;
    }
    
    @Override
    public JobQuery createdAfter(final Date date) {
        EnsureUtil.ensureNotNull("Provided date", date);
        this.createdAfter = date;
        return this;
    }
    
    @Override
    public JobQuery priorityHigherThanOrEquals(final long priority) {
        this.priorityHigherThanOrEqual = priority;
        return this;
    }
    
    @Override
    public JobQuery priorityLowerThanOrEquals(final long priority) {
        this.priorityLowerThanOrEqual = priority;
        return this;
    }
    
    @Override
    public JobQuery withException() {
        this.withException = true;
        return this;
    }
    
    @Override
    public JobQuery exceptionMessage(final String exceptionMessage) {
        EnsureUtil.ensureNotNull("Provided exception message", (Object)exceptionMessage);
        this.exceptionMessage = exceptionMessage;
        return this;
    }
    
    @Override
    public JobQuery failedActivityId(final String activityId) {
        EnsureUtil.ensureNotNull("Provided activity id", (Object)activityId);
        this.failedActivityId = activityId;
        return this;
    }
    
    @Override
    public JobQuery noRetriesLeft() {
        this.noRetriesLeft = true;
        return this;
    }
    
    @Override
    public JobQuery active() {
        this.suspensionState = SuspensionState.ACTIVE;
        return this;
    }
    
    @Override
    public JobQuery suspended() {
        this.suspensionState = SuspensionState.SUSPENDED;
        return this;
    }
    
    @Override
    protected boolean hasExcludingConditions() {
        return super.hasExcludingConditions() || (CompareUtil.areNotInAscendingOrder(this.priorityHigherThanOrEqual, this.priorityLowerThanOrEqual) || this.hasExcludingDueDateParameters()) || CompareUtil.areNotInAscendingOrder(this.createdAfter, this.createdBefore);
    }
    
    private boolean hasExcludingDueDateParameters() {
        final List<Date> dueDates = new ArrayList<Date>();
        if (this.duedateHigherThan != null && this.duedateHigherThanOrEqual != null) {
            dueDates.add(CompareUtil.min(this.duedateHigherThan, this.duedateHigherThanOrEqual));
            dueDates.add(CompareUtil.max(this.duedateHigherThan, this.duedateHigherThanOrEqual));
        }
        else if (this.duedateHigherThan != null) {
            dueDates.add(this.duedateHigherThan);
        }
        else if (this.duedateHigherThanOrEqual != null) {
            dueDates.add(this.duedateHigherThanOrEqual);
        }
        if (this.duedateLowerThan != null && this.duedateLowerThanOrEqual != null) {
            dueDates.add(CompareUtil.min(this.duedateLowerThan, this.duedateLowerThanOrEqual));
            dueDates.add(CompareUtil.max(this.duedateLowerThan, this.duedateLowerThanOrEqual));
        }
        else if (this.duedateLowerThan != null) {
            dueDates.add(this.duedateLowerThan);
        }
        else if (this.duedateLowerThanOrEqual != null) {
            dueDates.add(this.duedateLowerThanOrEqual);
        }
        return CompareUtil.areNotInAscendingOrder(dueDates);
    }
    
    @Override
    public JobQuery tenantIdIn(final String... tenantIds) {
        EnsureUtil.ensureNotNull("tenantIds", (Object[])tenantIds);
        this.tenantIds = tenantIds;
        this.isTenantIdSet = true;
        return this;
    }
    
    @Override
    public JobQuery withoutTenantId() {
        this.isTenantIdSet = true;
        this.tenantIds = null;
        return this;
    }
    
    @Override
    public JobQuery includeJobsWithoutTenantId() {
        this.includeJobsWithoutTenantId = true;
        return this;
    }
    
    @Override
    public JobQuery orderByJobDuedate() {
        return ((AbstractQuery<JobQuery, U>)this).orderBy(JobQueryProperty.DUEDATE);
    }
    
    @Override
    public JobQuery orderByExecutionId() {
        return ((AbstractQuery<JobQuery, U>)this).orderBy(JobQueryProperty.EXECUTION_ID);
    }
    
    @Override
    public JobQuery orderByJobId() {
        return ((AbstractQuery<JobQuery, U>)this).orderBy(JobQueryProperty.JOB_ID);
    }
    
    @Override
    public JobQuery orderByProcessInstanceId() {
        return ((AbstractQuery<JobQuery, U>)this).orderBy(JobQueryProperty.PROCESS_INSTANCE_ID);
    }
    
    @Override
    public JobQuery orderByProcessDefinitionId() {
        return ((AbstractQuery<JobQuery, U>)this).orderBy(JobQueryProperty.PROCESS_DEFINITION_ID);
    }
    
    @Override
    public JobQuery orderByProcessDefinitionKey() {
        return ((AbstractQuery<JobQuery, U>)this).orderBy(JobQueryProperty.PROCESS_DEFINITION_KEY);
    }
    
    @Override
    public JobQuery orderByJobRetries() {
        return ((AbstractQuery<JobQuery, U>)this).orderBy(JobQueryProperty.RETRIES);
    }
    
    @Override
    public JobQuery orderByJobPriority() {
        return ((AbstractQuery<JobQuery, U>)this).orderBy(JobQueryProperty.PRIORITY);
    }
    
    @Override
    public JobQuery orderByTenantId() {
        return ((AbstractQuery<JobQuery, U>)this).orderBy(JobQueryProperty.TENANT_ID);
    }
    
    @Override
    public long executeCount(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getJobManager().findJobCountByQueryCriteria(this);
    }
    
    @Override
    public List<Job> executeList(final CommandContext commandContext, final Page page) {
        this.checkQueryOk();
        return commandContext.getJobManager().findJobsByQueryCriteria(this, page);
    }
    
    @Override
    public List<ImmutablePair<String, String>> executeDeploymentIdMappingsList(final CommandContext commandContext) {
        this.checkQueryOk();
        return commandContext.getJobManager().findDeploymentIdMappingsByQueryCriteria(this);
    }
    
    public Set<String> getIds() {
        return this.ids;
    }
    
    public String getProcessInstanceId() {
        return this.processInstanceId;
    }
    
    public Set<String> getProcessInstanceIds() {
        return this.processInstanceIds;
    }
    
    public String getExecutionId() {
        return this.executionId;
    }
    
    public boolean getRetriesLeft() {
        return this.retriesLeft;
    }
    
    public boolean getExecutable() {
        return this.executable;
    }
    
    public Date getNow() {
        return ClockUtil.getCurrentTime();
    }
    
    public boolean isWithException() {
        return this.withException;
    }
    
    public String getExceptionMessage() {
        return this.exceptionMessage;
    }
}
