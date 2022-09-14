// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.impl.jobexecutor.JobHandlerConfiguration;
import org.zik.bpm.engine.impl.jobexecutor.TimerEventJobHandler;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import java.util.List;
import org.zik.bpm.engine.impl.persistence.entity.ProcessDefinitionEntity;
import java.util.Date;
import java.util.Iterator;
import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.persistence.entity.PropertyChange;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.context.ProcessApplicationContextUtil;
import org.zik.bpm.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.zik.bpm.engine.impl.persistence.entity.TimerEntity;
import org.zik.bpm.engine.impl.cfg.CommandChecker;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.exception.NotFoundException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import java.io.Serializable;
import org.zik.bpm.engine.impl.interceptor.Command;

public class RecalculateJobDuedateCmd implements Command<Void>, Serializable
{
    private static final long serialVersionUID = 1L;
    private final String jobId;
    private boolean creationDateBased;
    
    public RecalculateJobDuedateCmd(final String jobId, final boolean creationDateBased) {
        EnsureUtil.ensureNotEmpty("The job id is mandatory", "jobId", jobId);
        this.jobId = jobId;
        this.creationDateBased = creationDateBased;
    }
    
    @Override
    public Void execute(final CommandContext commandContext) {
        final JobEntity job = commandContext.getJobManager().findJobById(this.jobId);
        EnsureUtil.ensureNotNull(NotFoundException.class, "No job found with id '" + this.jobId + "'", "job", job);
        this.checkJobType(job);
        for (final CommandChecker checker : commandContext.getProcessEngineConfiguration().getCommandCheckers()) {
            checker.checkUpdateJob(job);
        }
        final TimerDeclarationImpl timerDeclaration = this.findTimerDeclaration(commandContext, job);
        final TimerEntity timer = (TimerEntity)job;
        final Date oldDuedate = job.getDuedate();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                timerDeclaration.resolveAndSetDuedate(timer.getExecution(), timer, RecalculateJobDuedateCmd.this.creationDateBased);
            }
        };
        final ProcessDefinitionEntity contextDefinition = commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(job.getProcessDefinitionId());
        ProcessApplicationContextUtil.doContextSwitch(runnable, contextDefinition);
        final List<PropertyChange> propertyChanges = new ArrayList<PropertyChange>();
        propertyChanges.add(new PropertyChange("duedate", oldDuedate, job.getDuedate()));
        propertyChanges.add(new PropertyChange("creationDateBased", null, this.creationDateBased));
        commandContext.getOperationLogManager().logJobOperation("RecalculateDueDate", this.jobId, job.getJobDefinitionId(), job.getProcessInstanceId(), job.getProcessDefinitionId(), job.getProcessDefinitionKey(), propertyChanges);
        return null;
    }
    
    protected void checkJobType(final JobEntity job) {
        final String type = job.getJobHandlerType();
        if ((!"timer-transition".equals(type) && !"timer-intermediate-transition".equals(type) && !"timer-start-event".equals(type) && !"timer-start-event-subprocess".equals(type) && !"timer-task-listener".equals(type)) || !(job instanceof TimerEntity)) {
            throw new ProcessEngineException("Only timer jobs can be recalculated, but the job with id '" + this.jobId + "' is of type '" + type + "'.");
        }
    }
    
    protected TimerDeclarationImpl findTimerDeclaration(final CommandContext commandContext, final JobEntity job) {
        TimerDeclarationImpl timerDeclaration = null;
        if (job.getExecutionId() != null) {
            timerDeclaration = this.findTimerDeclarationForActivity(commandContext, job);
        }
        else {
            timerDeclaration = this.findTimerDeclarationForProcessStartEvent(commandContext, job);
        }
        if (timerDeclaration == null) {
            throw new ProcessEngineException("No timer declaration found for job id '" + this.jobId + "'.");
        }
        return timerDeclaration;
    }
    
    protected TimerDeclarationImpl findTimerDeclarationForActivity(final CommandContext commandContext, final JobEntity job) {
        final ExecutionEntity execution = commandContext.getExecutionManager().findExecutionById(job.getExecutionId());
        if (execution == null) {
            throw new ProcessEngineException("No execution found with id '" + job.getExecutionId() + "' for job id '" + this.jobId + "'.");
        }
        final ActivityImpl activity = execution.getProcessDefinition().findActivity(job.getActivityId());
        if (activity != null) {
            if ("timer-task-listener".equals(job.getJobHandlerType())) {
                return this.findTimeoutListenerDeclaration(job, activity);
            }
            final Map<String, TimerDeclarationImpl> timerDeclarations = TimerDeclarationImpl.getDeclarationsForScope(activity.getEventScope());
            if (!timerDeclarations.isEmpty() && timerDeclarations.containsKey(job.getActivityId())) {
                return timerDeclarations.get(job.getActivityId());
            }
        }
        return null;
    }
    
    protected TimerDeclarationImpl findTimeoutListenerDeclaration(final JobEntity job, final ActivityImpl activity) {
        final Map<String, Map<String, TimerDeclarationImpl>> timeoutDeclarations = TimerDeclarationImpl.getTimeoutListenerDeclarationsForScope(activity.getEventScope());
        if (!timeoutDeclarations.isEmpty()) {
            final Map<String, TimerDeclarationImpl> activityTimeouts = timeoutDeclarations.get(job.getActivityId());
            if (activityTimeouts != null && !activityTimeouts.isEmpty()) {
                final JobHandlerConfiguration jobHandlerConfiguration = job.getJobHandlerConfiguration();
                if (jobHandlerConfiguration instanceof TimerEventJobHandler.TimerJobConfiguration) {
                    return activityTimeouts.get(((TimerEventJobHandler.TimerJobConfiguration)jobHandlerConfiguration).getTimerElementSecondaryKey());
                }
            }
        }
        return null;
    }
    
    protected TimerDeclarationImpl findTimerDeclarationForProcessStartEvent(final CommandContext commandContext, final JobEntity job) {
        final ProcessDefinitionEntity processDefinition = commandContext.getProcessEngineConfiguration().getDeploymentCache().findDeployedProcessDefinitionById(job.getProcessDefinitionId());
        final List<TimerDeclarationImpl> timerDeclarations = (List<TimerDeclarationImpl>)processDefinition.getProperty("timerStart");
        for (final TimerDeclarationImpl timerDeclarationCandidate : timerDeclarations) {
            if (timerDeclarationCandidate.getJobDefinitionId().equals(job.getJobDefinitionId())) {
                return timerDeclarationCandidate;
            }
        }
        return null;
    }
}
