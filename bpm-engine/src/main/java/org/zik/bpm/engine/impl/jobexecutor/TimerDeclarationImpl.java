// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.persistence.entity.JobEntity;
import org.zik.bpm.engine.impl.bpmn.helper.BpmnProperties;
import java.util.Collections;
import java.util.Map;
import org.zik.bpm.engine.impl.pvm.PvmScope;
import org.zik.bpm.engine.impl.util.ClockUtil;
import java.text.SimpleDateFormat;
import org.zik.bpm.engine.delegate.VariableScope;
import org.zik.bpm.engine.impl.calendar.BusinessCalendar;
import java.util.Date;
import org.zik.bpm.engine.impl.el.StartProcessVariableScope;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.delegate.Expression;
import org.zik.bpm.engine.impl.persistence.entity.TimerEntity;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public class TimerDeclarationImpl extends JobDeclaration<ExecutionEntity, TimerEntity>
{
    private static final long serialVersionUID = 1L;
    protected Expression description;
    protected TimerDeclarationType type;
    protected String repeat;
    protected boolean isInterruptingTimer;
    protected String eventScopeActivityId;
    protected Boolean isParallelMultiInstance;
    protected String rawJobHandlerConfiguration;
    
    public TimerDeclarationImpl(final Expression expression, final TimerDeclarationType type, final String jobHandlerType) {
        super(jobHandlerType);
        this.eventScopeActivityId = null;
        this.description = expression;
        this.type = type;
    }
    
    public boolean isInterruptingTimer() {
        return this.isInterruptingTimer;
    }
    
    public void setInterruptingTimer(final boolean isInterruptingTimer) {
        this.isInterruptingTimer = isInterruptingTimer;
    }
    
    public String getRepeat() {
        return this.repeat;
    }
    
    public void setEventScopeActivityId(final String eventScopeActivityId) {
        this.eventScopeActivityId = eventScopeActivityId;
    }
    
    public String getEventScopeActivityId() {
        return this.eventScopeActivityId;
    }
    
    @Override
    protected TimerEntity newJobInstance(final ExecutionEntity execution) {
        final TimerEntity timer = new TimerEntity(this);
        if (execution != null) {
            timer.setExecution(execution);
        }
        return timer;
    }
    
    public void setRawJobHandlerConfiguration(final String rawJobHandlerConfiguration) {
        this.rawJobHandlerConfiguration = rawJobHandlerConfiguration;
    }
    
    public void updateJob(final TimerEntity timer) {
        this.initializeConfiguration(timer.getExecution(), timer);
    }
    
    protected void initializeConfiguration(final ExecutionEntity context, final TimerEntity job) {
        final String dueDateString = this.resolveAndSetDuedate(context, job, false);
        if (this.type == TimerDeclarationType.CYCLE && this.jobHandlerType != "timer-intermediate-transition" && !this.isInterruptingTimer) {
            final String prepared = this.prepareRepeat(dueDateString);
            job.setRepeat(prepared);
        }
    }
    
    public String resolveAndSetDuedate(final ExecutionEntity context, final TimerEntity job, final boolean creationDateBased) {
        final BusinessCalendar businessCalendar = Context.getProcessEngineConfiguration().getBusinessCalendarManager().getBusinessCalendar(this.type.calendarName);
        if (this.description == null) {
            throw new ProcessEngineException("Timer '" + context.getActivityId() + "' was not configured with a valid duration/time");
        }
        String dueDateString = null;
        Date duedate = null;
        VariableScope scopeForExpression = context;
        if (scopeForExpression == null) {
            scopeForExpression = StartProcessVariableScope.getSharedInstance();
        }
        final Object dueDateValue = this.description.getValue(scopeForExpression);
        if (dueDateValue instanceof String) {
            dueDateString = (String)dueDateValue;
        }
        else {
            if (!(dueDateValue instanceof Date)) {
                throw new ProcessEngineException("Timer '" + context.getActivityId() + "' was not configured with a valid duration/time, either hand in a java.util.Date or a String in format 'yyyy-MM-dd'T'hh:mm:ss'");
            }
            duedate = (Date)dueDateValue;
        }
        if (duedate == null) {
            if (creationDateBased) {
                if (job.getCreateTime() == null) {
                    throw new ProcessEngineException("Timer '" + context.getActivityId() + "' has no creation time and cannot be recalculated based on creation date. Either recalculate on your own or trigger recalculation with creationDateBased set to false.");
                }
                duedate = businessCalendar.resolveDuedate(dueDateString, job.getCreateTime());
            }
            else {
                duedate = businessCalendar.resolveDuedate(dueDateString);
            }
        }
        job.setDuedate(duedate);
        return dueDateString;
    }
    
    @Override
    protected void postInitialize(final ExecutionEntity execution, final TimerEntity timer) {
        this.initializeConfiguration(execution, timer);
    }
    
    protected String prepareRepeat(final String dueDate) {
        if (dueDate.startsWith("R") && dueDate.split("/").length == 2) {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            return dueDate.replace("/", "/" + sdf.format(ClockUtil.getCurrentTime()) + "/");
        }
        return dueDate;
    }
    
    public TimerEntity createTimerInstance(final ExecutionEntity execution) {
        return this.createTimer(execution);
    }
    
    public TimerEntity createStartTimerInstance(final String deploymentId) {
        return this.createTimer(deploymentId);
    }
    
    public TimerEntity createTimer(final String deploymentId) {
        final TimerEntity timer = super.createJobInstance(null);
        timer.setDeploymentId(deploymentId);
        this.scheduleTimer(timer);
        return timer;
    }
    
    public TimerEntity createTimer(final ExecutionEntity execution) {
        final TimerEntity timer = super.createJobInstance(execution);
        this.scheduleTimer(timer);
        return timer;
    }
    
    protected void scheduleTimer(final TimerEntity timer) {
        Context.getCommandContext().getJobManager().schedule(timer);
    }
    
    @Override
    protected ExecutionEntity resolveExecution(final ExecutionEntity context) {
        return context;
    }
    
    @Override
    protected JobHandlerConfiguration resolveJobHandlerConfiguration(final ExecutionEntity context) {
        return this.resolveJobHandler().newConfiguration(this.rawJobHandlerConfiguration);
    }
    
    public static Map<String, TimerDeclarationImpl> getDeclarationsForScope(final PvmScope scope) {
        if (scope == null) {
            return Collections.emptyMap();
        }
        final Map<String, TimerDeclarationImpl> result = scope.getProperties().get(BpmnProperties.TIMER_DECLARATIONS);
        if (result != null) {
            return result;
        }
        return Collections.emptyMap();
    }
    
    public static Map<String, Map<String, TimerDeclarationImpl>> getTimeoutListenerDeclarationsForScope(final PvmScope scope) {
        if (scope == null) {
            return Collections.emptyMap();
        }
        final Map<String, Map<String, TimerDeclarationImpl>> result = scope.getProperties().get(BpmnProperties.TIMEOUT_LISTENER_DECLARATIONS);
        if (result != null) {
            return result;
        }
        return Collections.emptyMap();
    }
}
