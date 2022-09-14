// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.jobexecutor;

import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.impl.interceptor.CommandContext;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;

public class TimerExecuteNestedActivityJobHandler extends TimerEventJobHandler
{
    public static final String TYPE = "timer-transition";
    
    @Override
    public String getType() {
        return "timer-transition";
    }
    
    @Override
    public void execute(final TimerJobConfiguration configuration, final ExecutionEntity execution, final CommandContext commandContext, final String tenantId) {
        final String activityId = configuration.getTimerElementKey();
        final ActivityImpl activity = execution.getProcessDefinition().findActivity(activityId);
        EnsureUtil.ensureNotNull("Error while firing timer: boundary event activity " + configuration + " not found", "boundary event activity", activity);
        try {
            execution.executeEventHandlerActivity(activity);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ProcessEngineException("exception during timer execution: " + e2.getMessage(), e2);
        }
    }
}
