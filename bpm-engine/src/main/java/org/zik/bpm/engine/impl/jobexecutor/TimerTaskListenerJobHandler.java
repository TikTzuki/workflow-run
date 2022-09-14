// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import java.util.Iterator;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.persistence.entity.TaskEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public class TimerTaskListenerJobHandler extends TimerEventJobHandler
{
    public static final String TYPE = "timer-task-listener";
    
    @Override
    public String getType() {
        return "timer-task-listener";
    }
    
    @Override
    public void execute(final TimerJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final String activityId = configuration.getTimerElementKey();
        TaskEntity targetTask = null;
        for (final TaskEntity task : execution.getTasks()) {
            if (task.getTaskDefinitionKey().equals(activityId)) {
                targetTask = task;
            }
        }
        if (targetTask != null) {
            targetTask.triggerTimeoutEvent(configuration.getTimerElementSecondaryKey());
            return;
        }
        throw new ProcessEngineException("Error while triggering timeout task listener '" + configuration.getTimerElementSecondaryKey() + "': cannot find task for activity id '" + configuration.getTimerElementKey() + "'.");
    }
}
