// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.pvm.runtime.operation.PvmAtomicOperation;
import org.zik.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.zik.bpm.engine.impl.pvm.delegate.SignallableActivityBehavior;

public abstract class FlowNodeActivityBehavior implements SignallableActivityBehavior
{
    protected static final BpmnBehaviorLogger LOG;
    protected BpmnActivityBehavior bpmnActivityBehavior;
    
    public FlowNodeActivityBehavior() {
        this.bpmnActivityBehavior = new BpmnActivityBehavior();
    }
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        this.leave(execution);
    }
    
    public void leave(final ActivityExecution execution) {
        ((ExecutionEntity)execution).dispatchDelayedEventsAndPerformOperation(PvmAtomicOperation.ACTIVITY_LEAVE);
    }
    
    public void doLeave(final ActivityExecution execution) {
        this.bpmnActivityBehavior.performDefaultOutgoingBehavior(execution);
    }
    
    protected void leaveIgnoreConditions(final ActivityExecution activityContext) {
        this.bpmnActivityBehavior.performIgnoreConditionsOutgoingBehavior(activityContext);
    }
    
    @Override
    public void signal(final ActivityExecution execution, final String signalName, final Object signalData) throws Exception {
        throw FlowNodeActivityBehavior.LOG.unsupportedSignalException(execution.getActivity().getId());
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
