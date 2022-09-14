// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import java.util.Iterator;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.Condition;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class ExclusiveGatewayActivityBehavior extends GatewayActivityBehavior
{
    protected static BpmnBehaviorLogger LOG;
    
    @Override
    public void doLeave(final ActivityExecution execution) {
        ExclusiveGatewayActivityBehavior.LOG.leavingActivity(execution.getActivity().getId());
        PvmTransition outgoingSeqFlow = null;
        final String defaultSequenceFlow = (String)execution.getActivity().getProperty("default");
        PvmTransition seqFlow;
        for (Iterator<PvmTransition> transitionIterator = execution.getActivity().getOutgoingTransitions().iterator(); outgoingSeqFlow == null && transitionIterator.hasNext(); outgoingSeqFlow = seqFlow) {
            seqFlow = transitionIterator.next();
            final Condition condition = (Condition)seqFlow.getProperty("condition");
            if ((condition == null && (defaultSequenceFlow == null || !defaultSequenceFlow.equals(seqFlow.getId()))) || (condition != null && condition.evaluate(execution))) {
                ExclusiveGatewayActivityBehavior.LOG.outgoingSequenceFlowSelected(seqFlow.getId());
            }
        }
        if (outgoingSeqFlow != null) {
            execution.leaveActivityViaTransition(outgoingSeqFlow);
        }
        else {
            if (defaultSequenceFlow == null) {
                throw ExclusiveGatewayActivityBehavior.LOG.stuckExecutionException(execution.getActivity().getId());
            }
            final PvmTransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
            if (defaultTransition == null) {
                throw ExclusiveGatewayActivityBehavior.LOG.missingDefaultFlowException(execution.getActivity().getId(), defaultSequenceFlow);
            }
            execution.leaveActivityViaTransition(defaultTransition);
        }
    }
    
    static {
        ExclusiveGatewayActivityBehavior.LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
