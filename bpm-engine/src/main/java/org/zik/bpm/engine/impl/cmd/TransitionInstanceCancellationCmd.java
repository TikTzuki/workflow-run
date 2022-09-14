// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.cmd;

import org.zik.bpm.engine.runtime.TransitionInstance;
import org.zik.bpm.engine.ProcessEngineException;
import org.zik.bpm.engine.impl.util.EnsureUtil;
import org.zik.bpm.engine.exception.NotValidException;
import org.zik.bpm.engine.impl.interceptor.Command;
import org.zik.bpm.engine.runtime.ActivityInstance;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.interceptor.CommandContext;

public class TransitionInstanceCancellationCmd extends AbstractInstanceCancellationCmd
{
    protected String transitionInstanceId;
    
    public TransitionInstanceCancellationCmd(final String processInstanceId, final String transitionInstanceId) {
        super(processInstanceId);
        this.transitionInstanceId = transitionInstanceId;
    }
    
    public String getTransitionInstanceId() {
        return this.transitionInstanceId;
    }
    
    @Override
    protected ExecutionEntity determineSourceInstanceExecution(final CommandContext commandContext) {
        final ActivityInstance instance = commandContext.runWithoutAuthorization((Command<ActivityInstance>)new GetActivityInstanceCmd(this.processInstanceId));
        final TransitionInstance instanceToCancel = this.findTransitionInstance(instance, this.transitionInstanceId);
        EnsureUtil.ensureNotNull(NotValidException.class, this.describeFailure("Transition instance '" + this.transitionInstanceId + "' does not exist"), "transitionInstance", instanceToCancel);
        final ExecutionEntity transitionExecution = commandContext.getExecutionManager().findExecutionById(instanceToCancel.getExecutionId());
        return transitionExecution;
    }
    
    @Override
    protected String describe() {
        return "Cancel transition instance '" + this.transitionInstanceId + "'";
    }
}
