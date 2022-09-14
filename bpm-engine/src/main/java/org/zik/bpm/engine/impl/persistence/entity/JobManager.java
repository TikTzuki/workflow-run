// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import org.zik.bpm.engine.impl.Direction;
import org.zik.bpm.engine.impl.JobQueryProperty;
import java.util.HashSet;
import org.zik.bpm.engine.impl.util.CollectionUtil;
import java.util.Collection;
import org.zik.bpm.engine.impl.util.ImmutablePair;
import org.zik.bpm.engine.impl.db.ListQueryParameterObject;
import org.zik.bpm.engine.impl.JobQueryImpl;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import org.zik.bpm.engine.impl.Page;
import java.util.Iterator;
import java.util.List;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutorContext;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import org.zik.bpm.engine.impl.jobexecutor.MessageAddedNotification;
import org.zik.bpm.engine.impl.jobexecutor.ExclusiveJobAddedNotification;
import org.zik.bpm.engine.impl.jobexecutor.JobExecutor;
import java.util.Date;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.runtime.Job;
import org.zik.bpm.engine.impl.util.ClockUtil;
import org.zik.bpm.engine.impl.db.DbEntity;
import org.zik.bpm.engine.impl.QueryOrderingProperty;
import org.zik.bpm.engine.impl.persistence.AbstractManager;

public class JobManager extends AbstractManager
{
    public static QueryOrderingProperty JOB_PRIORITY_ORDERING_PROPERTY;
    public static QueryOrderingProperty JOB_TYPE_ORDERING_PROPERTY;
    public static QueryOrderingProperty JOB_DUEDATE_ORDERING_PROPERTY;
    
    public void updateJob(final JobEntity job) {
        this.getDbEntityManager().merge(job);
    }
    
    public void insertJob(final JobEntity job) {
        job.setCreateTime(ClockUtil.getCurrentTime());
        this.getDbEntityManager().insert(job);
        this.getHistoricJobLogManager().fireJobCreatedEvent(job);
    }
    
    public void deleteJob(final JobEntity job) {
        this.deleteJob(job, true);
    }
    
    public void deleteJob(final JobEntity job, final boolean fireDeleteEvent) {
        this.getDbEntityManager().delete(job);
        if (fireDeleteEvent) {
            this.getHistoricJobLogManager().fireJobDeletedEvent(job);
        }
    }
    
    public void insertAndHintJobExecutor(final JobEntity jobEntity) {
        jobEntity.insert();
        if (Context.getProcessEngineConfiguration().isHintJobExecutor()) {
            this.hintJobExecutor(jobEntity);
        }
    }
    
    public void send(final MessageEntity message) {
        message.insert();
        if (Context.getProcessEngineConfiguration().isHintJobExecutor()) {
            this.hintJobExecutor(message);
        }
    }
    
    public void schedule(final TimerEntity timer) {
        final Date duedate = timer.getDuedate();
        EnsureUtil.ensureNotNull("duedate", duedate);
        timer.insert();
        this.hintJobExecutorIfNeeded(timer, duedate);
    }
    
    public void reschedule(final JobEntity jobEntity, final Date newDuedate) {
        ((EverLivingJobEntity)jobEntity).init(Context.getCommandContext(), true);
        jobEntity.setSuspensionState(SuspensionState.ACTIVE.getStateCode());
        jobEntity.setDuedate(newDuedate);
        this.hintJobExecutorIfNeeded(jobEntity, newDuedate);
    }
    
    private void hintJobExecutorIfNeeded(final JobEntity jobEntity, final Date duedate) {
        final JobExecutor jobExecutor = Context.getProcessEngineConfiguration().getJobExecutor();
        final int waitTimeInMillis = jobExecutor.getWaitTimeInMillis();
        if (duedate.getTime() < ClockUtil.getCurrentTime().getTime() + waitTimeInMillis) {
            this.hintJobExecutor(jobEntity);
        }
    }
    
    protected void hintJobExecutor(final JobEntity job) {
        final JobExecutor jobExecutor = Context.getProcessEngineConfiguration().getJobExecutor();
        if (!jobExecutor.isActive()) {
            return;
        }
        final JobExecutorContext jobExecutorContext = Context.getJobExecutorContext();
        TransactionListener transactionListener = null;
        if (this.isJobPriorityInJobExecutorPriorityRange(job.getPriority())) {
            if (!job.isSuspended() && job.isExclusive() && this.isJobDue(job) && jobExecutorContext != null && jobExecutorContext.isExecutingExclusiveJob() && this.areInSameProcessInstance(job, jobExecutorContext.getCurrentJob())) {
                final Date currentTime = ClockUtil.getCurrentTime();
                job.setLockExpirationTime(new Date(currentTime.getTime() + jobExecutor.getLockTimeInMillis()));
                job.setLockOwner(jobExecutor.getLockOwner());
                transactionListener = new ExclusiveJobAddedNotification(job.getId(), jobExecutorContext);
            }
            else {
                transactionListener = new MessageAddedNotification(jobExecutor);
            }
            Context.getCommandContext().getTransactionContext().addTransactionListener(TransactionState.COMMITTED, transactionListener);
        }
    }
    
    protected boolean areInSameProcessInstance(final JobEntity job1, final JobEntity job2) {
        if (job1 == null || job2 == null) {
            return false;
        }
        final String instance1 = job1.getProcessInstanceId();
        final String instance2 = job2.getProcessInstanceId();
        return instance1 != null && instance1.equals(instance2);
    }
    
    protected boolean isJobPriorityInJobExecutorPriorityRange(final long jobPriority) {
        final ProcessEngineConfigurationImpl configuration = Context.getProcessEngineConfiguration();
        final Long jobExecutorPriorityRangeMin = configuration.getJobExecutorPriorityRangeMin();
        final Long jobExecutorPriorityRangeMax = configuration.getJobExecutorPriorityRangeMax();
        return (jobExecutorPriorityRangeMin == null || jobExecutorPriorityRangeMin <= jobPriority) && (jobExecutorPriorityRangeMax == null || jobExecutorPriorityRangeMax >= jobPriority);
    }
    
    public void cancelTimers(final ExecutionEntity execution) {
        final List<TimerEntity> timers = Context.getCommandContext().getJobManager().findTimersByExecutionId(execution.getId());
        for (final TimerEntity timer : timers) {
            timer.delete();
        }
    }
    
    public JobEntity findJobById(final String jobId) {
        return (JobEntity)this.getDbEntityManager().selectOne("selectJob", jobId);
    }
    
    public List<AcquirableJobEntity> findNextJobsToExecute(final Page page) {
        final ProcessEngineConfigurationImpl engineConfiguration = Context.getProcessEngineConfiguration();
        final Map<String, Object> params = new HashMap<String, Object>();
        final Date now = ClockUtil.getCurrentTime();
        params.put("now", now);
        params.put("alwaysSetDueDate", this.isEnsureJobDueDateNotNull());
        params.put("deploymentAware", engineConfiguration.isJobExecutorDeploymentAware());
        if (engineConfiguration.isJobExecutorDeploymentAware()) {
            final Set<String> registeredDeployments = engineConfiguration.getRegisteredDeployments();
            if (!registeredDeployments.isEmpty()) {
                params.put("deploymentIds", registeredDeployments);
            }
        }
        params.put("jobPriorityMin", engineConfiguration.getJobExecutorPriorityRangeMin());
        params.put("jobPriorityMax", engineConfiguration.getJobExecutorPriorityRangeMax());
        params.put("historyCleanupEnabled", engineConfiguration.isHistoryCleanupEnabled());
        final List<QueryOrderingProperty> orderingProperties = new ArrayList<QueryOrderingProperty>();
        if (engineConfiguration.isJobExecutorAcquireByPriority()) {
            orderingProperties.add(JobManager.JOB_PRIORITY_ORDERING_PROPERTY);
        }
        if (engineConfiguration.isJobExecutorPreferTimerJobs()) {
            orderingProperties.add(JobManager.JOB_TYPE_ORDERING_PROPERTY);
        }
        if (engineConfiguration.isJobExecutorAcquireByDueDate()) {
            orderingProperties.add(JobManager.JOB_DUEDATE_ORDERING_PROPERTY);
        }
        params.put("orderingProperties", orderingProperties);
        params.put("applyOrdering", !orderingProperties.isEmpty());
        return (List<AcquirableJobEntity>)this.getDbEntityManager().selectList("selectNextJobsToExecute", params, page);
    }
    
    public List<JobEntity> findJobsByExecutionId(final String executionId) {
        return (List<JobEntity>)this.getDbEntityManager().selectList("selectJobsByExecutionId", executionId);
    }
    
    public List<JobEntity> findJobsByProcessInstanceId(final String processInstanceId) {
        return (List<JobEntity>)this.getDbEntityManager().selectList("selectJobsByProcessInstanceId", processInstanceId);
    }
    
    public List<JobEntity> findJobsByJobDefinitionId(final String jobDefinitionId) {
        return (List<JobEntity>)this.getDbEntityManager().selectList("selectJobsByJobDefinitionId", jobDefinitionId);
    }
    
    public List<Job> findJobsByHandlerType(final String handlerType) {
        return (List<Job>)this.getDbEntityManager().selectList("selectJobsByHandlerType", handlerType);
    }
    
    public List<TimerEntity> findUnlockedTimersByDuedate(final Date duedate, final Page page) {
        final String query = "selectUnlockedTimersByDuedate";
        return (List<TimerEntity>)this.getDbEntityManager().selectList("selectUnlockedTimersByDuedate", duedate, page);
    }
    
    public List<TimerEntity> findTimersByExecutionId(final String executionId) {
        return (List<TimerEntity>)this.getDbEntityManager().selectList("selectTimersByExecutionId", executionId);
    }
    
    public List<Job> findJobsByQueryCriteria(final JobQueryImpl jobQuery, final Page page) {
        this.configureQuery(jobQuery);
        return (List<Job>)this.getDbEntityManager().selectList("selectJobByQueryCriteria", jobQuery, page);
    }
    
    public List<ImmutablePair<String, String>> findDeploymentIdMappingsByQueryCriteria(final JobQueryImpl jobQuery) {
        this.configureQuery(jobQuery);
        final Set<String> processInstanceIds = jobQuery.getProcessInstanceIds();
        if (processInstanceIds != null && !processInstanceIds.isEmpty()) {
            final List<List<String>> partitions = CollectionUtil.partition(new ArrayList<String>(processInstanceIds), 2000);
            final List<ImmutablePair<String, String>> result = new ArrayList<ImmutablePair<String, String>>();
            final List list;
            partitions.stream().forEach(partition -> {
                jobQuery.processInstanceIds(new HashSet<String>(partition));
                list.addAll(this.getDbEntityManager().selectList("selectJobDeploymentIdMappingsByQueryCriteria", jobQuery));
                return;
            });
            return result;
        }
        return (List<ImmutablePair<String, String>>)this.getDbEntityManager().selectList("selectJobDeploymentIdMappingsByQueryCriteria", jobQuery);
    }
    
    public List<JobEntity> findJobsByConfiguration(final String jobHandlerType, final String jobHandlerConfiguration, final String tenantId) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("handlerType", jobHandlerType);
        params.put("handlerConfiguration", jobHandlerConfiguration);
        params.put("tenantId", tenantId);
        if ("timer-intermediate-transition".equals(jobHandlerType) || "timer-transition".equals(jobHandlerType) || "timer-start-event".equals(jobHandlerType) || "timer-start-event-subprocess".equals(jobHandlerType)) {
            final String queryValue = jobHandlerConfiguration + "$" + "followUpJobCreated";
            params.put("handlerConfigurationWithFollowUpJobCreatedProperty", queryValue);
        }
        return (List<JobEntity>)this.getDbEntityManager().selectList("selectJobsByConfiguration", params);
    }
    
    public long findJobCountByQueryCriteria(final JobQueryImpl jobQuery) {
        this.configureQuery(jobQuery);
        return (long)this.getDbEntityManager().selectOne("selectJobCountByQueryCriteria", jobQuery);
    }
    
    public void updateJobSuspensionStateById(final String jobId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("jobId", jobId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(JobEntity.class, "updateJobSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateJobSuspensionStateByJobDefinitionId(final String jobDefinitionId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("jobDefinitionId", jobDefinitionId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(JobEntity.class, "updateJobSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateJobSuspensionStateByProcessInstanceId(final String processInstanceId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processInstanceId", processInstanceId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(JobEntity.class, "updateJobSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateJobSuspensionStateByProcessDefinitionId(final String processDefinitionId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionId", processDefinitionId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(JobEntity.class, "updateJobSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateStartTimerJobSuspensionStateByProcessDefinitionId(final String processDefinitionId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionId", processDefinitionId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        parameters.put("handlerType", "timer-start-event");
        this.getDbEntityManager().update(JobEntity.class, "updateJobSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateJobSuspensionStateByProcessDefinitionKey(final String processDefinitionKey, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isProcessDefinitionTenantIdSet", false);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(JobEntity.class, "updateJobSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateJobSuspensionStateByProcessDefinitionKeyAndTenantId(final String processDefinitionKey, final String processDefinitionTenantId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isProcessDefinitionTenantIdSet", true);
        parameters.put("processDefinitionTenantId", processDefinitionTenantId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        this.getDbEntityManager().update(JobEntity.class, "updateJobSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateStartTimerJobSuspensionStateByProcessDefinitionKey(final String processDefinitionKey, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isProcessDefinitionTenantIdSet", false);
        parameters.put("suspensionState", suspensionState.getStateCode());
        parameters.put("handlerType", "timer-start-event");
        this.getDbEntityManager().update(JobEntity.class, "updateJobSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateStartTimerJobSuspensionStateByProcessDefinitionKeyAndTenantId(final String processDefinitionKey, final String processDefinitionTenantId, final SuspensionState suspensionState) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("processDefinitionKey", processDefinitionKey);
        parameters.put("isProcessDefinitionTenantIdSet", true);
        parameters.put("processDefinitionTenantId", processDefinitionTenantId);
        parameters.put("suspensionState", suspensionState.getStateCode());
        parameters.put("handlerType", "timer-start-event");
        this.getDbEntityManager().update(JobEntity.class, "updateJobSuspensionStateByParameters", this.configureParameterizedQuery(parameters));
    }
    
    public void updateFailedJobRetriesByJobDefinitionId(final String jobDefinitionId, final int retries) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("jobDefinitionId", jobDefinitionId);
        parameters.put("retries", retries);
        this.getDbEntityManager().update(JobEntity.class, "updateFailedJobRetriesByParameters", parameters);
    }
    
    public void updateJobPriorityByDefinitionId(final String jobDefinitionId, final long priority) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("jobDefinitionId", jobDefinitionId);
        parameters.put("priority", priority);
        this.getDbEntityManager().update(JobEntity.class, "updateJobPriorityByDefinitionId", parameters);
    }
    
    protected void configureQuery(final JobQueryImpl query) {
        this.getAuthorizationManager().configureJobQuery(query);
        this.getTenantManager().configureQuery(query);
    }
    
    protected ListQueryParameterObject configureParameterizedQuery(final Object parameter) {
        return this.getTenantManager().configureQuery(parameter);
    }
    
    protected boolean isEnsureJobDueDateNotNull() {
        return Context.getProcessEngineConfiguration().isEnsureJobDueDateNotNull();
    }
    
    protected boolean isJobDue(final JobEntity job) {
        final Date duedate = job.getDuedate();
        final Date now = ClockUtil.getCurrentTime();
        return duedate == null || duedate.getTime() <= now.getTime();
    }
    
    static {
        JobManager.JOB_PRIORITY_ORDERING_PROPERTY = new QueryOrderingProperty(null, JobQueryProperty.PRIORITY);
        JobManager.JOB_TYPE_ORDERING_PROPERTY = new QueryOrderingProperty(null, JobQueryProperty.TYPE);
        JobManager.JOB_DUEDATE_ORDERING_PROPERTY = new QueryOrderingProperty(null, JobQueryProperty.DUEDATE);
        JobManager.JOB_PRIORITY_ORDERING_PROPERTY.setDirection(Direction.DESCENDING);
        JobManager.JOB_TYPE_ORDERING_PROPERTY.setDirection(Direction.DESCENDING);
        JobManager.JOB_DUEDATE_ORDERING_PROPERTY.setDirection(Direction.ASCENDING);
    }
}
