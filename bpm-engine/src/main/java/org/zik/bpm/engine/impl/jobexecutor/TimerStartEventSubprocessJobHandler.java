// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public class TimerStartEventSubprocessJobHandler extends TimerEventJobHandler
{
    public static final String TYPE = "timer-start-event-subprocess";
    
    @Override
    public String getType() {
        return "timer-start-event-subprocess";
    }
    
    @Override
    public void execute(final TimerJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final String activityId = configuration.getTimerElementKey();
        final ActivityImpl eventSubprocessActivity = execution.getProcessDefinition().findActivity(activityId);
        if (eventSubprocessActivity != null) {
            execution.executeEventHandlerActivity(eventSubprocessActivity);
            return;
        }
        throw new ProcessEngineException("Error while triggering event subprocess using timer start event: cannot find activity with id '" + configuration + "'.");
    }
}
