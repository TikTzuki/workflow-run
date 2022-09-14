// 
// Decompiled by Procyon v0.5.36
// 

package org.zik.bpm.engine.impl.bpmn.behavior;

import org.zik.bpm.engine.impl.ProcessEngineLogger;
import org.zik.bpm.engine.delegate.BaseDelegateExecution;
import org.zik.bpm.engine.impl.pvm.process.ScopeImpl;
import org.zik.bpm.engine.impl.pvm.process.ActivityImpl;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Iterator;
import org.zik.bpm.engine.impl.pvm.PvmActivity;
import java.util.List;
import org.zik.bpm.engine.delegate.DelegateExecution;
import org.zik.bpm.engine.impl.Condition;
import org.zik.bpm.engine.impl.pvm.PvmTransition;
import java.util.ArrayList;
import org.zik.bpm.engine.impl.pvm.delegate.ActivityExecution;

public class InclusiveGatewayActivityBehavior extends GatewayActivityBehavior
{
    protected static BpmnBehaviorLogger LOG;
    
    @Override
    public void execute(final ActivityExecution execution) throws Exception {
        execution.inactivate();
        this.lockConcurrentRoot(execution);
        final PvmActivity activity = execution.getActivity();
        if (this.activatesGateway(execution, activity)) {
            InclusiveGatewayActivityBehavior.LOG.activityActivation(activity.getId());
            final List<ActivityExecution> joinedExecutions = execution.findInactiveConcurrentExecutions(activity);
            final String defaultSequenceFlow = (String)execution.getActivity().getProperty("default");
            final List<PvmTransition> transitionsToTake = new ArrayList<PvmTransition>();
            for (final PvmTransition outgoingTransition : execution.getActivity().getOutgoingTransitions()) {
                if (defaultSequenceFlow == null || !outgoingTransition.getId().equals(defaultSequenceFlow)) {
                    final Condition condition = (Condition)outgoingTransition.getProperty("condition");
                    if (condition != null && !condition.evaluate(execution)) {
                        continue;
                    }
                    transitionsToTake.add(outgoingTransition);
                }
            }
            if (transitionsToTake.isEmpty()) {
                if (defaultSequenceFlow == null) {
                    throw InclusiveGatewayActivityBehavior.LOG.stuckExecutionException(execution.getActivity().getId());
                }
                final PvmTransition defaultTransition = execution.getActivity().findOutgoingTransition(defaultSequenceFlow);
                if (defaultTransition == null) {
                    throw InclusiveGatewayActivityBehavior.LOG.missingDefaultFlowException(execution.getActivity().getId(), defaultSequenceFlow);
                }
                transitionsToTake.add(defaultTransition);
            }
            execution.leaveActivityViaTransitions(transitionsToTake, joinedExecutions);
        }
        else {
            InclusiveGatewayActivityBehavior.LOG.noActivityActivation(activity.getId());
        }
    }
    
    protected Collection<ActivityExecution> getLeafExecutions(final ActivityExecution parent) {
        final List<ActivityExecution> executionlist = new ArrayList<ActivityExecution>();
        final List<? extends ActivityExecution> subExecutions = parent.getNonEventScopeExecutions();
        if (subExecutions.size() == 0) {
            executionlist.add(parent);
        }
        else {
            for (final ActivityExecution concurrentExecution : subExecutions) {
                executionlist.addAll(this.getLeafExecutions(concurrentExecution));
            }
        }
        return executionlist;
    }
    
    protected boolean activatesGateway(final ActivityExecution execution, final PvmActivity gatewayActivity) {
        final int numExecutionsGuaranteedToActivate = gatewayActivity.getIncomingTransitions().size();
        final ActivityExecution scopeExecution = execution.isScope() ? execution : execution.getParent();
        final List<ActivityExecution> executionsAtGateway = execution.findInactiveConcurrentExecutions(gatewayActivity);
        if (executionsAtGateway.size() >= numExecutionsGuaranteedToActivate) {
            return true;
        }
        final Collection<ActivityExecution> executionsNotAtGateway = this.getLeafExecutions(scopeExecution);
        executionsNotAtGateway.removeAll(executionsAtGateway);
        for (final ActivityExecution executionNotAtGateway : executionsNotAtGateway) {
            if (this.canReachActivity(executionNotAtGateway, gatewayActivity)) {
                return false;
            }
        }
        return true;
    }
    
    protected boolean canReachActivity(final ActivityExecution execution, final PvmActivity activity) {
        final PvmTransition pvmTransition = execution.getTransition();
        if (pvmTransition != null) {
            return this.isReachable(pvmTransition.getDestination(), activity, new HashSet<PvmActivity>());
        }
        return this.isReachable(execution.getActivity(), activity, new HashSet<PvmActivity>());
    }
    
    protected boolean isReachable(final PvmActivity srcActivity, final PvmActivity targetActivity, final Set<PvmActivity> visitedActivities) {
        if (srcActivity.equals(targetActivity)) {
            return true;
        }
        if (visitedActivities.contains(srcActivity)) {
            return false;
        }
        visitedActivities.add(srcActivity);
        final List<PvmTransition> outgoingTransitions = srcActivity.getOutgoingTransitions();
        if (outgoingTransitions.isEmpty()) {
            if (srcActivity.getActivityBehavior() instanceof EventBasedGatewayActivityBehavior) {
                final ActivityImpl eventBasedGateway = (ActivityImpl)srcActivity;
                final Set<ActivityImpl> eventActivities = eventBasedGateway.getEventActivities();
                for (final ActivityImpl eventActivity : eventActivities) {
                    final boolean isReachable = this.isReachable(eventActivity, targetActivity, visitedActivities);
                    if (isReachable) {
                        return true;
                    }
                }
            }
            else {
                final ScopeImpl flowScope = srcActivity.getFlowScope();
                if (flowScope != null && flowScope instanceof PvmActivity) {
                    return this.isReachable((PvmActivity)flowScope, targetActivity, visitedActivities);
                }
            }
            return false;
        }
        for (final PvmTransition pvmTransition : outgoingTransitions) {
            final PvmActivity destinationActivity = pvmTransition.getDestination();
            if (destinationActivity != null && !visitedActivities.contains(destinationActivity)) {
                final boolean reachable = this.isReachable(destinationActivity, targetActivity, visitedActivities);
                if (reachable) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    static {
        InclusiveGatewayActivityBehavior.LOG = ProcessEngineLogger.BPMN_BEHAVIOR_LOGGER;
    }
}
