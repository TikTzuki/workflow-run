// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import java.util.List;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class ParallelGatewayActivityBehavior extends GatewayActivityBehavior
{
    protected static final BpmnBehaviorLogger LOG;
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        final PvmActivity activity = execution.getActivity();
        final List<PvmTransition> outgoingTransitions = execution.getActivity().getOutgoingTransitions();
        execution.inactivate();
        this.lockConcurrentRoot(execution);
        final List<ActivityExecution> joinedExecutions = execution.findInactiveConcurrentExecutions(activity);
        final int nbrOfExecutionsToJoin = execution.getActivity().getIncomingTransitions().size();
        final int nbrOfExecutionsJoined = joinedExecutions.size();
        if (nbrOfExecutionsJoined == nbrOfExecutionsToJoin) {
            ParallelGatewayActivityBehavior.LOG.activityActivation(activity.getId(), nbrOfExecutionsJoined, nbrOfExecutionsToJoin);
            execution.leaveActivityViaTransitions(outgoingTransitions, joinedExecutions);
        }
        else {
            ParallelGatewayActivityBehavior.LOG.noActivityActivation(activity.getId(), nbrOfExecutionsJoined, nbrOfExecutionsToJoin);
        }
    }
    
    static {
        LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
