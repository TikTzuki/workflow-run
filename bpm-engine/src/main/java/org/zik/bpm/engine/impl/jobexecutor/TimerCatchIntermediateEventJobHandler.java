// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public class TimerCatchIntermediateEventJobHandler extends TimerEventJobHandler
{
    public static final String TYPE = "timer-intermediate-transition";
    
    @Override
    public String getType() {
        return "timer-intermediate-transition";
    }
    
    @Override
    public void execute(final TimerJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final String activityId = configuration.getTimerElementKey();
        final ActivityImpl intermediateEventActivity = execution.getProcessDefinition().findActivity(activityId);
        EnsureUtil.ensureNotNull("Error while firing timer: intermediate event activity " + configuration + " not found", "intermediateEventActivity", intermediateEventActivity);
        try {
            if (activityId.equals(execution.getActivityId())) {
                execution.signal("signal", null);
            }
            else {
                execution.executeEventHandlerActivity(intermediateEventActivity);
            }
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ProcessEngineException("exception during timer execution: " + e2.getMessage(), e2);
        }
    }
}
