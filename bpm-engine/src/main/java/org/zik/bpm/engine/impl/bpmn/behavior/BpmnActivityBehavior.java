// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.impl.pvm.runtime.CompensationBehavior;
import org.zik.bpm.engine.impl.pvm.runtime.PvmExecutionImpl;
import java.util.Iterator;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.List;
import java.util.Arrays;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.Condition;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class BpmnActivityBehavior
{
    protected static BpmnBehaviorLogger LOG;
    
    public void performDefaultOutgoingBehavior(final ActivityExecution activityExecution) {
        this.performOutgoingBehavior(activityExecution, true);
    }
    
    public void performIgnoreConditionsOutgoingBehavior(final ActivityExecution activityExecution) {
        this.performOutgoingBehavior(activityExecution, false);
    }
    
    protected void performOutgoingBehavior(final ActivityExecution execution, final boolean checkConditions) {
        BpmnActivityBehavior.LOG.leavingActivity(execution.getActivity().getId());
        final String defaultSequenceFlow = (String)execution.getActivity().getProperty("default");
        final List<PvmTransition> transitionsToTake = new ArrayList<PvmTransition>();
        final List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
        for (final PvmTransition outgoingTransition : outgoingTransitions) {
            if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
                final Condition condition = (Condition)outgoingTransition.getProperty("condition");
                if (condition != null && checkConditions && !condition.evaluate(execution)) {
                    continue;
                }
                transitionsToTake.add(outgoingTransition);
            }
        }
        if (transitionsToTake.size() == 1) {
            execution.leaveActivityViaTransition(transitionsToTake.get(0));
        }
        else if (transitionsToTake.size() >= 1) {
            execution.leaveActivityViaTransitions(transitionsToTake, Arrays.asList(execution));
        }
        else if (defaultSequenceFlow != null) {
            final PvmTransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
            if (defaultTransition == null) {
                throw BpmnActivityBehavior.LOG.missingDefaultFlowException(execution.getActivity().getId(), defaultSequenceFlow);
            }
            execution.leaveActivityViaTransition(defaultTransition);
        }
        else {
            if (!outgoingTransitions.isEmpty()) {
                throw BpmnActivityBehavior.LOG.missingConditionalFlowException(execution.getActivity().getId());
            }
            if (((ActivityImpl)execution.getActivity()).isCompensationHandler() && this.isAncestorCompensationThrowing(execution)) {
                execution.endCompensation();
            }
            else {
                BpmnActivityBehavior.LOG.missingOutgoingSequenceFlow(execution.getActivity().getId());
                execution.end(true);
            }
        }
    }
    
    protected boolean isAncestorCompensationThrowing(final ActivityExecution execution) {
        for (ActivityExecution parent = execution.getParent(); parent != null; parent = parent.getParent()) {
            if (CompensationBehavior.isCompensationThrowing((PvmExecutionImpl)parent)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        BpmnActivityBehavior.LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
