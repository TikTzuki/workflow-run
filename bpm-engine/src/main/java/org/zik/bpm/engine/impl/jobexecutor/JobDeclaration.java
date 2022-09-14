// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.zik.bpm.engine.ProcessEngineConfiguration;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.util.Date;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.persistence.entity.JobDefinitionEntity;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.io.Serializable;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;

public abstract class JobDeclaration<S, T extends JobEntity> implements Serializable
{
    private static final long serialVersionUID = 1L;
    protected String jobDefinitionId;
    protected String jobHandlerType;
    protected JobHandlerConfiguration jobHandlerConfiguration;
    protected String jobConfiguration;
    protected boolean exclusive;
    protected ActivityImpl activity;
    protected ParameterValueProvider jobPriorityProvider;
    
    public JobDeclaration(final String jobHandlerType) {
        this.exclusive = true;
        this.jobHandlerType = jobHandlerType;
    }
    
    public T createJobInstance(final S context) {
        final T job = this.newJobInstance(context);
        final String jobDefinitionId = this.resolveJobDefinitionId(context);
        job.setJobDefinitionId(jobDefinitionId);
        if (jobDefinitionId != null) {
            final JobDefinitionEntity jobDefinition = Context.getCommandContext().getJobDefinitionManager().findById(jobDefinitionId);
            if (jobDefinition != null) {
                job.setSuspensionState(jobDefinition.getSuspensionState());
                job.setProcessDefinitionKey(jobDefinition.getProcessDefinitionKey());
                job.setProcessDefinitionId(jobDefinition.getProcessDefinitionId());
                job.setTenantId(jobDefinition.getTenantId());
                job.setDeploymentId(jobDefinition.getDeploymentId());
            }
        }
        job.setJobHandlerConfiguration(this.resolveJobHandlerConfiguration(context));
        job.setJobHandlerType(this.resolveJobHandlerType(context));
        job.setExclusive(this.resolveExclusive(context));
        job.setRetries(this.resolveRetries(context));
        job.setDuedate(this.resolveDueDate(context));
        final ExecutionEntity contextExecution = this.resolveExecution(context);
        if (Context.getProcessEngineConfiguration().isProducePrioritizedJobs()) {
            final long priority = Context.getProcessEngineConfiguration().getJobPriorityProvider().determinePriority(contextExecution, this, jobDefinitionId);
            job.setPriority(priority);
        }
        if (contextExecution != null) {
            job.setTenantId(contextExecution.getTenantId());
        }
        this.postInitialize(context, job);
        return job;
    }
    
    public T reconfigure(final S context, final T job) {
        return job;
    }
    
    protected void postInitialize(final S context, final T job) {
    }
    
    protected abstract ExecutionEntity resolveExecution(final S p0);
    
    protected abstract T newJobInstance(final S p0);
    
    public String getJobDefinitionId() {
        return this.jobDefinitionId;
    }
    
    protected String resolveJobDefinitionId(final S context) {
        return this.jobDefinitionId;
    }
    
    public void setJobDefinitionId(final String jobDefinitionId) {
        this.jobDefinitionId = jobDefinitionId;
    }
    
    public String getJobHandlerType() {
        return this.jobHandlerType;
    }
    
    protected JobHandler resolveJobHandler() {
        final JobHandler jobHandler = Context.getProcessEngineConfiguration().getJobHandlers().get(this.jobHandlerType);
        EnsureUtil.ensureNotNull("Cannot find job handler '" + this.jobHandlerType + "' from job '" + this + "'", "jobHandler", jobHandler);
        return jobHandler;
    }
    
    protected String resolveJobHandlerType(final S context) {
        return this.jobHandlerType;
    }
    
    protected abstract JobHandlerConfiguration resolveJobHandlerConfiguration(final S p0);
    
    protected boolean resolveExclusive(final S context) {
        return this.exclusive;
    }
    
    protected int resolveRetries(final S context) {
        return Context.getProcessEngineConfiguration().getDefaultNumberOfRetries();
    }
    
    public Date resolveDueDate(final S context) {
        final ProcessEngineConfiguration processEngineConfiguration = Context.getProcessEngineConfiguration();
        if (processEngineConfiguration != null && (processEngineConfiguration.isJobExecutorAcquireByDueDate() || processEngineConfiguration.isEnsureJobDueDateNotNull())) {
            return ClockUtil.getCurrentTime();
        }
        return null;
    }
    
    public boolean isExclusive() {
        return this.exclusive;
    }
    
    public void setExclusive(final boolean exclusive) {
        this.exclusive = exclusive;
    }
    
    public String getActivityId() {
        if (this.activity != null) {
            return this.activity.getId();
        }
        return null;
    }
    
    public ActivityImpl getActivity() {
        return this.activity;
    }
    
    public void setActivity(final ActivityImpl activity) {
        this.activity = activity;
    }
    
    public ProcessDefinitionImpl getProcessDefinition() {
        if (this.activity != null) {
            return this.activity.getProcessDefinition();
        }
        return null;
    }
    
    public String getJobConfiguration() {
        return this.jobConfiguration;
    }
    
    public void setJobConfiguration(final String jobConfiguration) {
        this.jobConfiguration = jobConfiguration;
    }
    
    public ParameterValueProvider getJobPriorityProvider() {
        return this.jobPriorityProvider;
    }
    
    public void setJobPriorityProvider(final ParameterValueProvider jobPriorityProvider) {
        this.jobPriorityProvider = jobPriorityProvider;
    }
}
