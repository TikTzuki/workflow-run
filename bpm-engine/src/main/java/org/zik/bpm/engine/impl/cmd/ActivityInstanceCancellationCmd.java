// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.impl.ActivityExecutionTreeMapping;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class ActivityInstanceCancellationCmd extends AbstractInstanceCancellationCmd
{
    protected String activityInstanceId;
    
    public ActivityInstanceCancellationCmd(final String processInstanceId, final String activityInstanceId) {
        super(processInstanceId);
        this.activityInstanceId = activityInstanceId;
    }
    
    public ActivityInstanceCancellationCmd(final String processInstanceId, final String activityInstanceId, final String cancellationReason) {
        super(processInstanceId, cancellationReason);
        this.activityInstanceId = activityInstanceId;
    }
    
    public String getActivityInstanceId() {
        return this.activityInstanceId;
    }
    
    @Override
    protected ExecutionEntity determineSourceInstanceExecution(final CommandContext commandContext) {
        final ExecutionEntity processInstance = commandContext.getExecutionManager().findExecutionById(this.processInstanceId);
        final ActivityExecutionTreeMapping mapping = new ActivityExecutionTreeMapping(commandContext, this.processInstanceId);
        final ActivityInstance instance = commandContext.runWithoutAuthorization((Command<ActivityInstance>)new GetActivityInstanceCmd(this.processInstanceId));
        final ActivityInstance instanceToCancel = this.findActivityInstance(instance, this.activityInstanceId);
        EnsureUtil.ensureNotNull(NotValidException.class, this.describeFailure("Activity instance '" + this.activityInstanceId + "' does not exist"), "activityInstance", instanceToCancel);
        final ExecutionEntity scopeExecution = this.getScopeExecutionForActivityInstance(processInstance, mapping, instanceToCancel);
        return scopeExecution;
    }
    
    @Override
    protected String describe() {
        return "Cancel activity instance '" + this.activityInstanceId + "'";
    }
}
