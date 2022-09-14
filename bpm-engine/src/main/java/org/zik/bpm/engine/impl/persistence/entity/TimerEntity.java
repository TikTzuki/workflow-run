// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.persistence.entity;

import java.util.Map;
import java.util.HashMap;
import org.zik.bpm.engine.impl.calendar.BusinessCalendar;
import org.zik.bpm.engine.impl.calendar.CycleBusinessCalendar;
import org.zik.bpm.engine.impl.jobexecutor.RepeatingFailedJobListener;
import org.zik.bpm.engine.impl.interceptor.CommandExecutor;
import org.zik.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import java.util.Date;
import org.zik.bpm.engine.impl.cfg.TransactionListener;
import org.zik.bpm.engine.impl.cfg.TransactionState;
import org.zik.bpm.engine.impl.context.Context;
import org.zik.bpm.engine.impl.jobexecutor.TimerEventJobHandler;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.jobexecutor.TimerDeclarationImpl;

public class TimerEntity extends JobEntity
{
    public static final String TYPE = "timer";
    private static final long serialVersionUID = 1L;
    protected String repeat;
    protected long repeatOffset;
    
    public TimerEntity() {
    }
    
    public TimerEntity(final TimerDeclarationImpl timerDeclaration) {
        this.repeat = timerDeclaration.getRepeat();
    }
    
    protected TimerEntity(final TimerEntity te) {
        this.jobHandlerConfiguration = te.jobHandlerConfiguration;
        this.jobHandlerType = te.jobHandlerType;
        this.isExclusive = te.isExclusive;
        this.repeat = te.repeat;
        this.repeatOffset = te.repeatOffset;
        this.retries = te.retries;
        this.executionId = te.executionId;
        this.processInstanceId = te.processInstanceId;
        this.jobDefinitionId = te.jobDefinitionId;
        this.suspensionState = te.suspensionState;
        this.deploymentId = te.deploymentId;
        this.processDefinitionId = te.processDefinitionId;
        this.processDefinitionKey = te.processDefinitionKey;
        this.tenantId = te.tenantId;
        this.priority = te.priority;
    }
    
    @Override
    protected void preExecute(final CommandContext commandContext) {
        if (this.getJobHandler() instanceof TimerEventJobHandler) {
            final TimerEventJobHandler.TimerJobConfiguration configuration = (TimerEventJobHandler.TimerJobConfiguration)this.getJobHandlerConfiguration();
            if (this.repeat != null && !configuration.isFollowUpJobCreated()) {
                final Date newDueDate = this.calculateRepeat();
                if (newDueDate != null) {
                    final ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
                    final CommandExecutor commandExecutor = processEngineConfiguration.getCommandExecutorTxRequiresNew();
                    final RepeatingFailedJobListener listener = this.createRepeatingFailedJobListener(commandExecutor);
                    commandContext.getTransactionContext().addTransactionListener(TransactionState.ROLLED_BACK, listener);
                    this.createNewTimerJob(newDueDate);
                }
            }
        }
    }
    
    protected RepeatingFailedJobListener createRepeatingFailedJobListener(final CommandExecutor commandExecutor) {
        return new RepeatingFailedJobListener(commandExecutor, this.getId());
    }
    
    public void createNewTimerJob(final Date dueDate) {
        final TimerEntity newTimer = new TimerEntity(this);
        newTimer.setDuedate(dueDate);
        Context.getCommandContext().getJobManager().schedule(newTimer);
    }
    
    public Date calculateRepeat() {
        final BusinessCalendar businessCalendar = Context.getProcessEngineConfiguration().getBusinessCalendarManager().getBusinessCalendar(CycleBusinessCalendar.NAME);
        return ((CycleBusinessCalendar)businessCalendar).resolveDuedate(this.repeat, null, this.repeatOffset);
    }
    
    public String getRepeat() {
        return this.repeat;
    }
    
    public void setRepeat(final String repeat) {
        this.repeat = repeat;
    }
    
    public long getRepeatOffset() {
        return this.repeatOffset;
    }
    
    public void setRepeatOffset(final long repeatOffset) {
        this.repeatOffset = repeatOffset;
    }
    
    @Override
    public String getType() {
        return "timer";
    }
    
    @Override
    public Object getPersistentState() {
        final Map<String, Object> persistentState = (Map<String, Object>)super.getPersistentState();
        persistentState.put("repeat", this.repeat);
        return persistentState;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[repeat=" + this.repeat + ", id=" + this.id + ", revision=" + this.revision + ", duedate=" + this.duedate + ", repeatOffset=" + this.repeatOffset + ", lockOwner=" + this.lockOwner + ", lockExpirationTime=" + this.lockExpirationTime + ", executionId=" + this.executionId + ", processInstanceId=" + this.processInstanceId + ", isExclusive=" + this.isExclusive + ", retries=" + this.retries + ", jobHandlerType=" + this.jobHandlerType + ", jobHandlerConfiguration=" + this.jobHandlerConfiguration + ", exceptionByteArray=" + this.exceptionByteArray + ", exceptionByteArrayId=" + this.exceptionByteArrayId + ", exceptionMessage=" + this.exceptionMessage + ", deploymentId=" + this.deploymentId + "]";
    }
}
